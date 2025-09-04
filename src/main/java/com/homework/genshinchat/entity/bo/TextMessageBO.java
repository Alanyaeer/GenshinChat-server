package com.homework.genshinchat.entity.bo;

import lombok.Data;

@Data
public class TextMessageBO extends MessageBO{
    private String text;
    private Integer type;
}
