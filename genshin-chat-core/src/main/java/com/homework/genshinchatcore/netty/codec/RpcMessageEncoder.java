package com.homework.genshinchatcore.netty.codec;

import com.homework.chatserver.compress.Compress;
import com.homework.chatserver.context.SpringContextHolder;
import com.homework.chatserver.serialize.Serialization;
import com.homework.chatserver.utils.idGenerator.IdGenerator;
import com.homework.common.entity.constants.RpcConstants;
import com.homework.common.entity.enums.CompressTypeEnum;
import com.homework.common.entity.enums.SerializationTypeEnum;
import com.homework.common.entity.rpc.message.BaseMessage;
import com.homework.common.utils.hash.MurMurHash;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.homework.common.entity.constants.RpcConstants.ID_GENERATOR_VERSION;

/**
 <pre>
 *   0     1     2     3     4        5     6     7     8         9          10      11     12  13  14   15 16
 *   +-----+-----+-----+-----+--------+----+----+----+------+-----------+-------+----- --+-----+-----+-------+
 *   |   magic   code        |version | full length         | messageType| codec|compress|    RequestId       |
 *   +-----------------------+--------+---------------------+-----------+-----------+-----------+------------+
 *   |                                                                                                       |
 *   |                                         body                                                          |
 *   |                                                                                                       |
 *   |                                        ... ...                                                        |
 *   +-------------------------------------------------------------------------------------------------------+
 * 4B  magic code（魔法数）   1B version（版本）   4B full length（消息长度）    1B messageType（消息类型）
 * 1B compress（压缩类型） 1B codec（序列化类型）    4B  requestId（请求的Id 经过hash操作的）
 * body（object类型数据）
 * </pre>
 * @author 嘉豪舞团-吴嘉豪
 * @date 2025/9/9
 */
@Slf4j
public class RpcMessageEncoder extends MessageToMessageEncoder<BaseMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, BaseMessage message, List<Object> out) throws Exception {
        try {
            IdGenerator idGenerator = SpringContextHolder.getBean(ID_GENERATOR_VERSION.getName(), IdGenerator.class);
            ByteBuf byteBuf = ctx.alloc().buffer();
            byteBuf.writeBytes(RpcConstants.MAGIC_NUMBER);
            byteBuf.writeByte(RpcConstants.VERSION);
            // 长度字段必须填写
            byteBuf.writeInt(0); // 占位，后面再回填
            byteBuf.writeByte(message.getMessageType());
            byteBuf.writeByte(message.getCodecType());
            byteBuf.writeByte(message.getCompressType());
            // 使用murmurHash映射
            long nextId = idGenerator.nextId();
            byteBuf.writeInt(MurMurHash.hashLong(nextId));
            int fullLength = RpcConstants.HEAD_LENGTH;
            if(!message.isHeartbeatMessage()){
                // 序列化
                String serializeName = SerializationTypeEnum.getName(message.getCodecType());
                Serialization serialization = SpringContextHolder.getBean(serializeName, Serialization.class);
                byte[] serializationBytes = serialization.serialize(message);
                // 压缩
                String compressName = CompressTypeEnum.fromCode(message.getCompressType()).getName();
                Compress compress = SpringContextHolder.getBean(compressName, Compress.class);
                byte[] compressBytes = compress.compress(serializationBytes);
                byteBuf.writeBytes(compressBytes);
                fullLength += compressBytes.length;
            }
            // 考虑ByteBuf的前面如果放了一些东西，就不能直接定位length的位置是RpcConstants.MAGIC_NUMBER的长度
            // 考虑到fullLength表示当前已经放入消息的长度， writeIndex表示目前写入的消息长度
            // 那么length字段的起始位置就在 writeIndex - fullLength + RpcConstants.MAGIC_NUMBER.length + 1
            int writeIndex = byteBuf.writerIndex();
            byteBuf.writerIndex(writeIndex - fullLength + RpcConstants.MAGIC_NUMBER.length + 1);
            byteBuf.writeInt(fullLength);
            // 复原原来的索引
            byteBuf.writerIndex(writeIndex);
            out.add(new BinaryWebSocketFrame(byteBuf));
        } catch (Exception e) {
            log.error("编码失败: {}", e.getMessage());
            ctx.fireExceptionCaught(e);
        }
    }
}
