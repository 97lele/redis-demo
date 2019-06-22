package com.gdut.redisdemo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author lulu
 * @Date 2019/6/21 23:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVO implements Serializable {
    private static final long serialVersionUID = -4510647711621744011L;

    private String name;
    private Integer age;
    private String messageId;
    private String id;

}
