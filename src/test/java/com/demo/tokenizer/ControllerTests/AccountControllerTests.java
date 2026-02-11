package com.demo.tokenizer.ControllerTests;

import com.demo.tokenizer.Controller.AccountController;
import com.demo.tokenizer.Model.RawAccounts;
import com.demo.tokenizer.Model.TokenizedAccounts;
import com.demo.tokenizer.Service.TokenizingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Objects;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTests {
    @Mock
    private TokenizingService mockTokenizingService;
    private AccountController testSubject;

    @BeforeEach
    void setUp(){
        testSubject = new AccountController(mockTokenizingService);
    }

    @Test
    void testTokenizeAccountNumbers()
    {
        Set<String> rawAccountNumbers = Set.of("raw");
        RawAccounts request = new RawAccounts();
        request.setAccountNumbers(rawAccountNumbers);

        Set<String> tokenizedAccountNumbers = Set.of("tokenized");
        TokenizedAccounts expected = new TokenizedAccounts();
        expected.setTokenizedAccountNumbers(tokenizedAccountNumbers);

        when(mockTokenizingService.tokenize(request)).thenReturn(expected);

        ResponseEntity<TokenizedAccounts> actual = testSubject.tokenizeAccountNumbers(request);

        assertThat(Objects.requireNonNull(actual.getBody()).getTokenizedAccountNumbers())
                .isEqualTo(expected.getTokenizedAccountNumbers());
    }

    @Test
    void testDetokenizeAccountNumbers()
    {
        Set<String> tokenizedAccountNumbers = Set.of("tokenized");
        TokenizedAccounts request = new TokenizedAccounts();
        request.setTokenizedAccountNumbers(tokenizedAccountNumbers);

        Set<String> rawAccountNumbers = Set.of("raw");
        RawAccounts expected = new RawAccounts();
        expected.setAccountNumbers(rawAccountNumbers);

        when(mockTokenizingService.detokenize(request)).thenReturn(expected);

        ResponseEntity<RawAccounts> actual = testSubject.detokenizeAccountNumbers(request);

        assertThat(Objects.requireNonNull(actual.getBody()).getAccountNumbers())
                .isEqualTo(expected.getAccountNumbers());
    }
}
