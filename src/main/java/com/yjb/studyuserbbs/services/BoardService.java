package com.yjb.studyuserbbs.services;

import com.yjb.studyuserbbs.entities.BoardEntity;
import com.yjb.studyuserbbs.mappers.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardService {
    private final BoardMapper boardMapper;

    @Autowired
  public BoardService(BoardMapper boardMapper) {
        this.boardMapper = boardMapper;
    }

    public BoardEntity getById(String id) {
        if (id == null) {
            return null;
        }
        return this.boardMapper.selectById(id);
    }
}
