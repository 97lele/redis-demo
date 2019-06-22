package com.gdut.redisdemo.VO;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author lulu
 * @Date 2019/6/22 21:11
 */
@Data
@Accessors(chain = true)
public class MessageVO {
    private String messageId;
    private Integer reTryCount = 0;
    private String content;
    private Integer status =0;
    private String topic;
}
