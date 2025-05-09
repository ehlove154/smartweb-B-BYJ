package com.yjb.test250502.services;

import com.yjb.test250502.entities.TickerEntity;
import com.yjb.test250502.mappers.TickerMapper;
import com.yjb.test250502.results.AddResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TickerService {
    private final TickerMapper tickerMapper;

    @Autowired
    public TickerService(TickerMapper tickerMapper) {
        this.tickerMapper = tickerMapper;
    }

    public AddResult add(String id, String name) {
        if (id == null ||
                name == null ||
                id.isEmpty() ||
                name.isEmpty() ||
        !id.matches("^([A-Z]{1,5})$") ||
        !name.matches("^([\\da-zA-Z\\-,.\\s]{1,100})$")) {
            return AddResult.FAILURE;
        }

        if (tickerMapper.selectById(id) != null) {
            return AddResult.FAILURE_DUPLICATE_ID;
        }
        return this.tickerMapper.insert(new TickerEntity(id, name)) > 0 ? AddResult.SUCCESS : AddResult.FAILURE;
    }

    public boolean deleteTicker(String id) {
        if (id == null || !id.matches("^([A-Z]{1,5})$")) {
            return false;
        }
        return this.tickerMapper.deleteById(id) > 0;
    }

    public TickerEntity[] getAll() {
        return this.tickerMapper.selectAll();
    }
}
