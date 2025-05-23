package com.yjb.studyadvanceduserbbs.services;

import com.yjb.studyadvanceduserbbs.entities.BoardEntity;
import com.yjb.studyadvanceduserbbs.mappers.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardService {
    public static boolean isIdValid(String input) {
        return input != null && input.matches("^([a-z_]{1,10})$");
    }

    private final BoardMapper boardMapper;

    @Autowired
    public BoardService(BoardMapper boardMapper) {
        this.boardMapper = boardMapper;
    }

    public BoardEntity getById(String id) {
        if (!BoardService.isIdValid(id)) {
            return null;
        }
        return this.boardMapper.selectById(id);
    }

    public BoardEntity[] getAll() {
        return this.boardMapper.selectAll();
    }
}
