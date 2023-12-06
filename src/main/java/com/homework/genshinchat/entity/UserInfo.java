package com.homework.genshinchat.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 吴嘉豪
 * @date 2023/10/21 19:20
 */
@Data
//@Schema(description = "用户信息实体")

public class UserInfo implements Serializable {
    private static final long serialVersionUID = 1L;
//    @Schema(description ="用户头像")

    private String userimg;
    @TableId(value="userid")
//    @Schema(description ="用户id")

    private String userid;
    @TableField(fill = FieldFill.INSERT)
//    @Schema(description ="创建时间")

    private LocalDateTime createTime;
    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
//    @Schema(description ="更新时间")

    private LocalDateTime updateTime;
//    @Schema(description ="用户简介")

    private String userdetail;
//    @Schema(description ="用户名称")

    private String username;
//    @Schema(description ="废弃功能")

    // 这个似乎没有什么用
    private String userimmg;
}
