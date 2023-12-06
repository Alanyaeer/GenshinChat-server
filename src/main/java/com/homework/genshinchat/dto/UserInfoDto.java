package com.homework.genshinchat.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 吴嘉豪
 * @date 2023/10/22 13:57
 */
@Data

public class UserInfoDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String headImg;
    private String userid;
    private String detail;
    private String name;
    // 这个似乎没有什么用
    private String img;
}
