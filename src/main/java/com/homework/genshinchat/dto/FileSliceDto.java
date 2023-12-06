package com.homework.genshinchat.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * @author 吴嘉豪
 * @date 2023/11/4 16:11
 *
 */
@Data

public class FileSliceDto implements Serializable {
    private static final long serialVersionUID = 1L;
    @JSONField(serialize = false)
    private MultipartFile file;
    private String md5;
    private Integer chunkcnt;
}
