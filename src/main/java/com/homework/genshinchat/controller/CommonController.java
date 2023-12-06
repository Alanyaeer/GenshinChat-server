package com.homework.genshinchat.controller;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.homework.genshinchat.common.R;
import com.homework.genshinchat.dto.FileSliceDto;
import com.homework.genshinchat.entity.Message;
import com.homework.genshinchat.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.homework.genshinchat.constants.RedisConstants.CHATLIST_PERSON_KEY;
import static com.homework.genshinchat.constants.RedisConstants.FILE_SIZE_KEY;

/**
 * @author 吴嘉豪
 * @date 2023/10/22 23:12
 */

@RestController
@RequestMapping("/api")
@Slf4j
@Api(tags = "文件相关操作")
public class CommonController {
    @Autowired
    private MessageService messageService;
    private String basepath ="/opt/fileandpicture/";
    private Map<String, Integer> uploadcache= new HashMap<>();
    private Map<String, Integer> uploadEnable = new HashMap<>();
    private Map<String, Integer> downloadcache = new HashMap<>();
    @Autowired
    private StringRedisTemplate redisTemplate;
    private static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool(10);

    @PostMapping("/upload")
    @ApiOperation("上传文件")
    public R<String> uploadFileAndPicture(MultipartFile e)  {
        String originFileName = e.getOriginalFilename();
        String suffix = originFileName.substring(originFileName.lastIndexOf("."));
        File dir = new File(basepath + originFileName);

        if(!dir.exists()){
            dir.mkdirs();
        }
        try{
            e.transferTo(new File(basepath + originFileName));

        }catch(Exception ex){
            log.info("{}",ex.getMessage());
        }
        log.info("{}", originFileName);

        return R.success("发送成功");
    }
    @PutMapping("/saveMsg")
    @ApiOperation("保存信息")
    public R<String> saveMessage(@RequestBody Message message) throws IOException {

        CACHE_REBUILD_EXECUTOR.submit(()->{

            String myId = message.getMyId();
            String friendId = message.getFriendId();
            if(message.getChatType() == 1 && message.getImgType() == 2){
                message.setMsg("");
            }

            messageService.save(message);
            redisTemplate.opsForList().rightPush(CHATLIST_PERSON_KEY + myId + ":" + friendId , JSON.toJSONString(message));
            message.setMyId(friendId);
            message.setFriendId(myId);
            redisTemplate.opsForList().rightPush(CHATLIST_PERSON_KEY +friendId + ":" + myId  , JSON.toJSONString(message));
            messageService.save(message);

        });
        return R.success("存储成功");
    }
    @GetMapping("/downloadfile")
    @ApiOperation( "下载文件")
    public String downloadFile(HttpServletResponse response, HttpServletRequest request, String fileName, String extend) throws IOException {
        JSONObject result = new JSONObject();
        File file = new File(basepath + fileName);
        log.info("{}", extend);
        if (!file.exists()) {
            result.put("error", "下载的文件不存在");
            return result.toString();
        }
        response.reset();
        response.setContentType(extend);
        response.setCharacterEncoding("utf-8");
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
        byte[] readeBytes = FileUtil.readBytes(file);
        OutputStream os = response.getOutputStream();
        os.write(readeBytes);
        result.put("success", "下载成功");
        return result.toString();
    }

    @PostMapping("/uploadslice")
    @ApiOperation("上传文件分片")

    public R<Integer> uploadchunks(@RequestPart("file")MultipartFile file , @RequestPart("hash") String hash, @RequestPart("chunkcnt") String chunkcnt ,@RequestPart("filename") String filename, @RequestPart("totalCnt") String totalCnt) throws IOException, InterruptedException {
        log.info("{}", filename);
        String suffix = filename.substring(filename.lastIndexOf("."));
        String prefix = filename.substring(0, filename.lastIndexOf(".") - 1);
        log.info("{}", suffix);
        log.info("{}", prefix);
        File dir = new File(basepath  + hash+ "/" + prefix+  "_" + chunkcnt);
        if(!dir.exists()){
            dir.mkdirs();
        }
        file.transferTo(dir);
//        uploadcache.put(hash, Integer.parseInt(chunkcnt));
        if(uploadcache.containsKey(hash)) uploadcache.put(hash, uploadcache.get(hash) + 1);
        else uploadcache.put(hash, 0);
        log.info("当前写了: {}", chunkcnt);
        if(chunkcnt.equals(totalCnt)){
            //执行合并
            boolean ismerge = false;
            while(!ismerge){
                int n = Integer.parseInt(totalCnt);
                for(int i = 0; i <= n; ++i){
                    File fileitem    = new File(basepath + hash + "/" + prefix + "_" + i);
                    if(!fileitem.exists()){
                        log.info("还有文件没有输入结束");
                        break;
                    }
                    if(i == n) ismerge = true;
                }
            }
            log.info("当前的文件为{}/{}",chunkcnt ,totalCnt);
        }
        return R.success(1);
    }

    @PostMapping("/merge")
    @ApiOperation("文件合并")

    public R<Integer> fileMerge(@RequestBody Map map) throws IOException {
        String hash = map.get("hash").toString();
        String filename = map.get("filename").toString();
        Integer totalCnt = Integer.valueOf(map.get("totalCnt").toString());
        log.info("开始合并");
        String suffix = filename.substring(filename.lastIndexOf("."));
        String prefix = filename.substring(0, filename.lastIndexOf(".") - 1);
        File mergeFile = new File(basepath + filename);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(mergeFile));
        // 获取到所有的files
        log.info("{}", basepath +  hash);
        for(int i = 0; i <= totalCnt; ++i){

            File file = new File(basepath + hash + "/" + prefix + "_" +Integer.toString(i));
            log.info("{}:{}", file.length(), file.getName());
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[1024];
            // 记录每次读取的字节数
            int len;
            // 循环读取分片文件，直到读完
            while ((len = bis.read(buffer)) != -1) {
                // 将读取的数据写入合并后的文件
                bos.write(buffer, 0, len);
            }
            bis.close();
        }
        bos.flush();
        bos.close();
        log.info("合并成功");
        return R.success(1);
    }

    @PostMapping("/reupload")
    @ApiOperation("判断文件上传的进度")

    public R<Integer> reUpload(@RequestBody Map map) {
        String hash = map.get("result").toString();
        if(!uploadcache.containsKey(hash)) return R.success(0);
        // 上传下一位就好了
        return R.success(uploadcache.get(hash) + 1);
    }

    @RequestMapping("/getsize")
    @ApiOperation( "获取文件的大小")
    public R<Long> getFileSize(@RequestBody Map map){
        String fileName = map.get("fileName").toString();
        File file = new File(basepath + fileName);
        return R.success(file.length());
    }

    @RequestMapping("/downloadslicefile")
    @ApiOperation("下载文件的分片")

    public String downloadSliceFile(HttpServletResponse response, HttpServletRequest request , String fileName, String extend, Long start, Long end, int curcnt, String userid, int totalcnt) throws IOException {
        int contentLength = request.getContentLength();
        log.info("{} : {}" , start, end);
        JSONObject result = new JSONObject();
        File file = new File(basepath + fileName);
        if (!file.exists()) {
            result.put("error", "下载的文件不存在");
            return result.toString();
        }
        response.reset();
        response.setCharacterEncoding("utf-8");
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
        response.setHeader("Accept-Range", "bytes");
        String contentRange = String.valueOf(new StringBuffer("bytes ").append(start).append("-").append(end));
        response.setHeader("Content-Range", contentRange);
        response.setHeader("Content-Length", String.valueOf(end - start + 1));
        RandomAccessFile rf = new RandomAccessFile(basepath + fileName, "r");
        rf.seek(start);
        OutputStream os = response.getOutputStream();
        byte[] bytes = new byte[(int) (end - start + 1)];
        int len = rf.read(bytes);
        if(len != -1) os.write(bytes ,0, (int) (end - start + 1));
        rf.close();
        os.close();
        result.put("success", "文件下载成功");

        String encrypted= DigestUtils.md5DigestAsHex((basepath + fileName + userid).getBytes());
        downloadcache.put(encrypted, curcnt);
        log.info("{}: {}", curcnt, totalcnt);
        if(curcnt == totalcnt){
            downloadcache.remove(encrypted);
        }
        return result.toString();

    }
    
    @PostMapping("/redownload")
    @ApiOperation("已经下载了多个分片")
    public R<Integer> redownload(@RequestBody Map map){
        String fileName = map.get("fileName").toString();
        String userid = map.get("userid").toString();
        String encrypted= DigestUtils.md5DigestAsHex((basepath + fileName + userid).getBytes());
        if(downloadcache.containsKey(encrypted) == false) return R.success(0);
        return R.success(downloadcache.get(encrypted) + 1);
    }
}
