package com.yjb.test250502.controllers;

import com.yjb.test250502.entities.TickerEntity;
import com.yjb.test250502.results.AddResult;
import com.yjb.test250502.services.TickerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/")
public class TickerController {
    private final TickerService tickerService;

    @Autowired
    public TickerController(TickerService tickerService) {
        this.tickerService = tickerService;
    }

    @RequestMapping(value = "/ticker", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, String> deleteTicker(@RequestParam("id") String id) {
        boolean deleted = tickerService.deleteTicker(id);
        Map<String, String> response = new HashMap<>();
        response.put("result", deleted ? "success" : "failure");
        return response;
    }

    @RequestMapping(value = "/ticker", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, String> postTicker(@RequestParam("id") String id, @RequestParam("name") String name) {
        AddResult result = tickerService.add(id, name);
        Map<String, String> response = new HashMap<>();
        response.put("result", result == null ? "failure" : result.name().toLowerCase());
        return response;
    }

    @RequestMapping(value = "/ticker", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getTicker() {
        return "ticker";
    }

    @RequestMapping(value = "/tickers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public TickerEntity[] getTickers() {
        return this.tickerService.getAll();
    }
}
