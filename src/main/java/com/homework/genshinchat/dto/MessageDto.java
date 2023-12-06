package com.homework.genshinchat.dto;


import lombok.Data;


/**
 * @author 吴嘉豪
 * @date 2023/10/22 17:27
 */
@Data

public class MessageDto {
    private static final long serialVersionUID = 1L;
    private String myId;
    private String friendId;
    private String msg;
    private String uid;
}
