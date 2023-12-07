package com.homework.genshinchat.utils;

import com.homework.genshinchat.controller.FriendController;
import com.homework.genshinchat.dto.FriendDto;
import com.homework.genshinchat.entity.Friend;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 吴嘉豪
 * @date 2023/10/24 18:30
 */
@Slf4j
public class LivePerson {
    public static Map<String,Boolean> liveList = new HashMap<>();
    public static List<FriendDto> liveFriendDtoList = new ArrayList<>();
    public static boolean checkPersonInLive(String id){
        if(liveList.containsKey(id)){
            return true;
        }
        else return false;
    }
    public static void inLivePerson(String id){
//        System.out.println(liveList.containsKey(id) + "  " + id);

        if(liveList.containsKey(id) == false)
            liveList.put(id, true);
    }
    public static void outLivePerson(String id){
        log.info("移除id为：{}的用户", id);
        liveList.remove(id);
    }
    public static List<FriendDto> getLiveFriendDtoList (){
        return liveFriendDtoList;
    }
    public void getFriendDtoFromDataBase () throws InterruptedException {
        while(true){

            Thread.sleep(50000);
        }
    }



}
