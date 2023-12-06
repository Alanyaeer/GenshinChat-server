package com.homework.genshinchat.dto;


import lombok.Data;

import java.io.Serializable;

/**
 * @author 吴嘉豪
 * @date 2023/10/22 17:06
 */
@Data

public class FriendDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String img;
    private String name;
    private String detail;
    // 这个字段可以不填入值 直接 给他设置一个“”
    private String lastMsg;
    private String id;
    private String headImg;
    private boolean status;
    private String mutiId;
    private String mutiChatName;
}
