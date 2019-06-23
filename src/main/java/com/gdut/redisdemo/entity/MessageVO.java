package com.gdut.redisdemo.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author lulu
 * @Date 2019/6/22 21:11
 */
@Data
@Accessors(chain = true)
@Entity
@Table(name="message_vo")
public class MessageVO {
    @Id
    private String messageId;
    private Integer reTryCount = 0;
    private String content;
    private Integer status =0;
    private String topic;
}
