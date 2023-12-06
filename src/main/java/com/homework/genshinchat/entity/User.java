package com.homework.genshinchat.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author 吴嘉豪
 * @date 2023/10/21 18:46
 */
@Data
//@Schema(description = "用户实体")

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
//    @Schema(description ="id")

    private String id;
//    @Schema(description ="用户名称")

    private String name;
//    private String
//@Schema(description ="创建时间")

@TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime createTime;
    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
//    @Schema(description ="更新时间")

    private LocalDateTime updateTime;
//    @Schema(description ="密码")

    private String password;
    // 判断用户的登录状态， status， 为0的时候表示下线， 否则为上线
//    @Schema(description ="用户状态")

    private Boolean status;
}
