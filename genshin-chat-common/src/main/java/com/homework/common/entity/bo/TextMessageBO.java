package com.homework.common.entity.bo;

import lombok.Data;

@Data
@Deprecated
public class TextMessageBO extends MessageBO{
    private String text;
    private Integer type;
}
