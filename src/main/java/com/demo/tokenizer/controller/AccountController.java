package com.demo.tokenizer.controller;

import com.demo.tokenizer.model.RawAccounts;
import com.demo.tokenizer.model.TokenizedAccounts;
import com.demo.tokenizer.service.TokenizingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    private final TokenizingService tokenizingService;

    public AccountController(TokenizingService tokenizingService) {
        this.tokenizingService = tokenizingService;
    }

    @PostMapping("/tokenize")
    public ResponseEntity<TokenizedAccounts> tokenizeAccountNumbers(@RequestBody RawAccounts rawAccounts){
        TokenizedAccounts tokenizedAccounts = tokenizingService.tokenize(rawAccounts);

        return ResponseEntity.status(HttpStatus.CREATED).body(tokenizedAccounts);
    }

    @PostMapping("/detokenize")
    public ResponseEntity<RawAccounts> detokenizeAccountNumbers(@RequestBody TokenizedAccounts tokenizedAccounts){
        RawAccounts rawAccounts = tokenizingService.detokenize(tokenizedAccounts);

        return ResponseEntity.status(HttpStatus.CREATED).body(rawAccounts);
    }

}
