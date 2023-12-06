package com.homework.genshinchat.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
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
//@Schema(description = "好友实体")
public class Friend implements Serializable {
    private static final long serialVersionUID = 1L;
//    @Schema(description ="id")
    private String id;
//    @TableId(type= IdType.NONE)
//    @Schema(description ="好友id")

    private String friendId;
//    @Schema(description ="创建时间")

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    //更新时间
//    @Schema(description ="更新时间")

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
