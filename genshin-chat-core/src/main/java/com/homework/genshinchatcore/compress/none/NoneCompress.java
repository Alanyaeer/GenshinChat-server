package com.homework.genshinchatcore.compress.none;

import com.homework.chatserver.compress.Compress;
import org.springframework.stereotype.Component;

/**
 * @author 嘉豪舞团-吴嘉豪
 * @date 2025/9/13
 */
@Component
public class NoneCompress implements Compress {
    @Override
    public byte[] compress(byte[] bytes) {
        return bytes;
    }

    @Override
    public byte[] decompress(byte[] bytes) {
        return bytes;
    }
}
