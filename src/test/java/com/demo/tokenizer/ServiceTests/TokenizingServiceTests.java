package com.demo.tokenizer.ServiceTests;

import com.demo.tokenizer.Model.AccountEntity;
import com.demo.tokenizer.Model.RawAccounts;
import com.demo.tokenizer.Model.TokenizedAccounts;
import com.demo.tokenizer.Repository.TokenizedAccountRepository;
import com.demo.tokenizer.Service.TokenizingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.encrypt.TextEncryptor;

import java.util.Objects;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenizingServiceTests {

    @Mock
    private TokenizedAccountRepository mockRepository;
    @Mock
    private TextEncryptor mockEncryptor;
    private TokenizingService testSubject;

    @BeforeEach
    void setUp(){
        testSubject = new TokenizingService(mockRepository, mockEncryptor);
    }

    @Test
    void testTokenize()
    {
        Set<String> rawAccountNumbers = Set.of("acc1", "acc2");
        RawAccounts rawAccounts = new RawAccounts();
        rawAccounts.setAccountNumbers(rawAccountNumbers);

        AccountEntity entity1 = AccountEntity.builder().id(1L)
                .rawAccountNumber("acc1")
                .tokenizedAccountNumber("tokenAcc1")
                .build();

        AccountEntity entity2 = AccountEntity.builder().id(2L)
                .rawAccountNumber("acc2")
                .tokenizedAccountNumber("tokenAcc2")
                .build();

        when(mockRepository.findAllByRawAccountNumberIn(rawAccountNumbers))
                .thenReturn(Set.of(entity1, entity2));

        when(mockEncryptor.encrypt(anyString())).thenReturn("tokenAcc1", "tokenAcc2");

        TokenizedAccounts actual = testSubject.tokenize(rawAccounts);

        assertThat(Objects.requireNonNull(actual).getTokenizedAccountNumbers())
                .isEqualTo(Set.of("tokenAcc1", "tokenAcc2"));
    }

    @Test
    void testDetokenize()
    {
        Set<String> tokenizedAccountNumbers = Set.of("tokenAcc1", "tokenAcc2");
        TokenizedAccounts tokenizedAccounts = new TokenizedAccounts();
        tokenizedAccounts.setTokenizedAccountNumbers(tokenizedAccountNumbers);

        AccountEntity entity1 = AccountEntity.builder().id(1L)
                .rawAccountNumber("acc1")
                .tokenizedAccountNumber("tokenAcc1")
                .build();

        AccountEntity entity2 = AccountEntity.builder().id(2L)
                .rawAccountNumber("acc2")
                .tokenizedAccountNumber("tokenAcc2")
                .build();

        when(mockRepository.findAllByTokenizedAccountNumberIn(tokenizedAccountNumbers))
                .thenReturn(Set.of(entity1, entity2));

        RawAccounts actual = testSubject.detokenize(tokenizedAccounts);

        assertThat(Objects.requireNonNull(actual).getAccountNumbers())
                .isEqualTo(Set.of("acc1", "acc2"));
    }


}
