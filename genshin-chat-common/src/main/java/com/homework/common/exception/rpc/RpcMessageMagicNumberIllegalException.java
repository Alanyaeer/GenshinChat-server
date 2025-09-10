package com.homework.common.exception.rpc;

/**
 * 非法消息参数异常
 *
 * @author 嘉豪舞团-吴嘉豪
 * @date 2025/9/10
 */
public class RpcMessageMagicNumberIllegalException extends RuntimeException {
    /**
     * 非法消息参数异常
     *
     * @param message 消息
     */
    public RpcMessageMagicNumberIllegalException(String message) {
        super(message);
    }
}
