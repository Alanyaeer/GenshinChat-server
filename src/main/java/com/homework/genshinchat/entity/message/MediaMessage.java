package com.homework.genshinchat.entity.message;

import com.homework.genshinchat.entity.enums.MessageTypeEnum;
import lombok.Builder;

@Builder
public class MediaMessage extends Message {
    private String url;
    private Long userId;
    private Long toUserId;

    public MediaMessage() {
        super(MessageTypeEnum.MEDIA);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }
}
