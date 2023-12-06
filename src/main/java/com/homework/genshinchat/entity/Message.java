package com.homework.genshinchat.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * @author 吴嘉豪
 * @date 2023/10/21 19:19
 */
@Data
@TableName("message")
@NoArgsConstructor
@AllArgsConstructor
//@Schema(description = "消息实体")

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
//    private BigInteger id;
//    @Schema(description ="我的id")

    private String myId;
//    private Integer id;
//    @TableId(value = "id", type= IdType.AUTO)
//    private Long id;
//    @Schema(description ="朋友的id")

private String friendId;
//    @Schema(description ="消息")

    private String msg;
    // 根据时间顺序查询time
    @JsonFormat(pattern = "yyyy年MM月dd日 HH:mm")
//    @Schema(description ="发送消息时间")

    private LocalDateTime time;
//    @Schema(description ="发送消息类型")

    private int chatType;
//    @Schema(description ="拓展")

    private String extend;
//    @Schema(description ="谁发送的")

    private String uid;
//    @Schema(description ="文件名称")

    private String fileName;
//    @Schema(description ="文件大小")

    private String size;
//    @Schema(description ="头像")

    private String headImg;
//    @Schema(description ="文件类型")

    private Integer fileType;
//    @Schema(description ="图片类型")

    private Integer imgType;
}
