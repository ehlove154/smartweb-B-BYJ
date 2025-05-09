package com.yjb.test250425.controllers;

import com.yjb.test250425.results.CryptoResult;
import com.yjb.test250425.services.CryptoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/util")
public class CryptoController {
    private final CryptoService cryptoService;

    @Autowired
    public CryptoController(CryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    @RequestMapping(value = "/crypto", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String postCrypto(@RequestParam(value = "op") String op,
                             @RequestParam(value = "plaintext") String plaintext,
                             @RequestParam(value = "check", required = false) String check) {
        Pair<CryptoResult, String> resultPair = this.cryptoService.cryptoHash(op, plaintext);
        String hashResult = cryptoService.cryptoHash(op, plaintext).getSecond();

        if ("true".equalsIgnoreCase(check)) {
            hashResult = hashResult.toUpperCase();
        }
        return hashResult;
    }

    @RequestMapping(value = "/crypto", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String getCrypto() {
        return "util/crypto";
    }
}
