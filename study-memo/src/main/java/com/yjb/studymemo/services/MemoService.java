package com.yjb.studymemo.services;

import com.yjb.studymemo.entities.MemoEntity;
import com.yjb.studymemo.mappers.MemoMapper;
import com.yjb.studymemo.results.memo.AddResult;
import com.yjb.studymemo.results.memo.DeleteByIndexResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemoService {
    private final MemoMapper memoMapper;

    @Autowired
    public MemoService(MemoMapper memoMapper) {
        this.memoMapper = memoMapper;
    }

    public AddResult add(String writer, String content) {
        if (writer == null || content == null ||
        !writer.matches("^([\\da-zA-Z가-힣]{2,10})$") ||
        content.isEmpty() ||
        content.length() > 100) {
            return AddResult.FAILURE_INVALID_VALUE;
        }
        return this.memoMapper.insert(writer, content) > 0 ? AddResult.SUCCESS : AddResult.FAILURE;
    }

    public DeleteByIndexResult deleteByIndex(int index) {
        return this.memoMapper.deleteByIndex(index) > 0 ? DeleteByIndexResult.SUCCESS : DeleteByIndexResult.FAILURE;

//        if (this.memoMapper.deleteByIndex(index) > 0) {
//            return DeleteByIndexResult.SUCCESS;
//        } else {
//            return DeleteByIndexResult.FAILURE;
//        }
    }



    public MemoEntity[] getAll() {
        return this.memoMapper.selectAll();
    }
}
