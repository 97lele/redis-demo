package com.gdut.redisdemo.repository;

import com.gdut.redisdemo.entity.MessageVO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author lulu
 * @Date 2019/6/23 13:08
 */
public interface MessageVODao extends JpaRepository<MessageVO,String> {
    List<MessageVO> findAllByStatus(Integer status);
}
