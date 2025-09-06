package com.homework.common.entity.dto;

import com.homework.common.entity.Message;
import lombok.Data;

import java.io.File;
import java.io.Serializable;

/**
 * @author 吴嘉豪
 * @date 2023/10/25 18:30
 */
@Data

public class FileDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private File file;
    private Message message;
}
