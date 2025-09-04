package com.homework.genshinchat.entity.bo;

import lombok.Data;

/**
 * 短信bo
 *
 * @author wps
 * @date 2025/09/04
 */
@Data
public class MessageBO {
    /**
     * 发给谁
     */
    private String to;
    /**
     * 我的id
     */
    private String myId;
}
