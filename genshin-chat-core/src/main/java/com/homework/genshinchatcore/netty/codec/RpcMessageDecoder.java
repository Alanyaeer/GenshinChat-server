package com.homework.genshinchatcore.netty.codec;

import com.homework.common.entity.constants.RpcConstants;
import com.homework.common.entity.enums.CompressTypeEnum;
import com.homework.common.entity.enums.MessageTypeEnum;
import com.homework.common.entity.enums.SerializationTypeEnum;
import com.homework.common.entity.rpc.message.*;
import com.homework.common.exception.rpc.RpcMessageMagicNumberIllegalException;
import com.homework.genshinchatcore.compress.Compress;
import com.homework.genshinchatcore.context.SpringContextHolder;
import com.homework.genshinchatcore.serializer.Serialization;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * <pre>
 *   0     1     2     3     4        5     6     7     8         9          10      11     12  13  14   15 16
 *   +-----+-----+-----+-----+--------+----+----+----+------+-----------+-------+----- --+-----+-----+-------+
 *   |   magic   code        |version | full length         | messageType| codec|compress|    RequestId
 *   |    17     18    19   20
 *   +----+-----------------+--------+---------------------+-----------+-----------+-----------+------------+
 *                         |                                                                               |
 *                         |                 body                                                          |
 *   +----------------------+                                                                                |
 *   |                                        ... ...                                                        |
 *   +-------------------------------------------------------------------------------------------------------+
 * 4B  magic code（魔法数）   1B version（版本）   4B full length（消息长度）    1B messageType（消息类型）
 * 1B compress（压缩类型） 1B codec（序列化类型）    8B  requestId
 * body（object类型数据）
 * </pre>
 *
 * @author 嘉豪舞团-吴嘉豪
 * @date 2025/9/10
 */
@Slf4j
public class RpcMessageDecoder extends LengthFieldBasedFrameDecoder {

    public RpcMessageDecoder() {
        this(RpcConstants.MAX_FRAME_LENGTH, 5, 4, -9, 0);
    }

    public RpcMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        Object decoded = super.decode(ctx, in);
        if (decoded instanceof ByteBuf frame) {
            if (frame.readableBytes() >= RpcConstants.HEAD_LENGTH) {
                try {
                    return decodeFrame(frame.retain());
                } catch (Exception e) {
                    log.error("Decode frame error!", e);
                    throw e;
                }
            }
        }
        return decoded;
    }

    private Object decodeFrame(ByteBuf in) {
        // note: must read ByteBuf in order
        checkMagicNumber(in);
        checkVersion(in);
        int fullLength = in.readInt();
        // build RpcMessage object
        byte messageType = in.readByte();
        byte codecType = in.readByte();
        byte compressType = in.readByte();
        long requestId = in.readLong();
        // todo 消息幂等判断
        BaseMessage message = BaseMessage.builder()
                .messageType(messageType)
                .compressType(compressType)
                .codecType(codecType)
                .id(requestId)
                .build();
        if (isHeartbeatMessage(messageType)) {
            if (messageType == MessageTypeEnum.PING.getCode()) return new PingMessage();
            else return new PongMessage();
        }
        int bodyLength = fullLength - RpcConstants.HEAD_LENGTH;
        if (bodyLength > 0) {
            byte[] bodyBytes = new byte[bodyLength];
            in.readBytes(bodyBytes);
            // 解码
            String compressName = CompressTypeEnum.fromCode(compressType).getName();
            Compress compress = SpringContextHolder.getBean(compressName, Compress.class);
            byte[] decompressBytes = compress.decompress(bodyBytes);
            // 反序列化
            String serializationName = SerializationTypeEnum.getName(codecType);
            Serialization serialization = SpringContextHolder.getBean(serializationName, Serialization.class);

            if (messageType == MessageTypeEnum.TEXT.getCode()) {
                TextMessage textMessage = serialization.deserialize(decompressBytes, TextMessage.class);
                textMessage.fillHeaderFields(message);
                return textMessage;
            } else if (messageType == MessageTypeEnum.MEDIA.getCode()) {
                MediaMessage mediaMessage = serialization.deserialize(decompressBytes, MediaMessage.class);
                mediaMessage.fillHeaderFields(message);
                return mediaMessage;
            }

        }
        throw new RuntimeException("未知消息类型" + messageType);
    }

    private void checkVersion(ByteBuf in) {
        // read the version and compare
        byte version = in.readByte();
        if (version != RpcConstants.VERSION) {
            throw new RuntimeException("version isn't compatible" + version);
        }
    }

    private void checkMagicNumber(ByteBuf in) {
        int length = RpcConstants.MAGIC_NUMBER.length;
        byte[] bytes = new byte[length];
        in.readBytes(bytes);
        if (RpcConstants.MAGIC_NUMBER.equals(bytes)) {
            throw new RpcMessageMagicNumberIllegalException("魔数消息不正确" + Arrays.toString(bytes));
        }
    }

    public boolean isHeartbeatMessage(byte messageType){
        return messageType == MessageTypeEnum.PING.getCode() || messageType == MessageTypeEnum.PONG.getCode();
    }
}
