package com.homework.genshinchat.utils;

import io.netty.buffer.ByteBuf;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileUtils {

    /**
     * 将ByteBuf中的二进制数据写入D盘test文件夹下的指定文件
     * @param byteBuf 包含二进制数据的ByteBuf
     * @param fileName 要写入的文件名
     * @throws IOException 如果发生I/O错误
     */
    public static void writeBinaryDataToDiskFile(ByteBuf byteBuf, String fileName) throws IOException {
        // 定义文件路径：D盘test文件夹下的指定文件
        Path filePath = Paths.get("D:/test", fileName);

        // 确保test文件夹存在，如果不存在则创建
        Files.createDirectories(filePath.getParent());

        // 将ByteBuf中的数据写入文件
        // 使用try-with-resources自动关闭流
        try (var outputStream = Files.newOutputStream(
                filePath,
                StandardOpenOption.CREATE,    // 如果文件不存在则创建
                StandardOpenOption.TRUNCATE_EXISTING  // 如果文件存在则截断
        )) {

            // 如果ByteBuf有数组支持，直接获取数组写入，效率更高
            if (byteBuf.hasArray()) {
                outputStream.write(
                        byteBuf.array(),
                        byteBuf.arrayOffset() + byteBuf.readerIndex(),
                        byteBuf.readableBytes()
                );
            } else {
                // 否则创建临时缓冲区进行复制
                byte[] buffer = new byte[byteBuf.readableBytes()];
                byteBuf.getBytes(byteBuf.readerIndex(), buffer);
                outputStream.write(buffer);
            }
        }
    }
}
