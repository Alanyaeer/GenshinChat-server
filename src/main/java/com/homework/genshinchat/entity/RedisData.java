package com.homework.genshinchat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author 吴嘉豪
 * @date 2023/12/5 15:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedisData {
    Object data;
    LocalDateTime expireTime;
}
