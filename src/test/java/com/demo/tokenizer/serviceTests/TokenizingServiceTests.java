package com.demo.tokenizer.serviceTests;

import com.demo.tokenizer.model.RawAccounts;
import com.demo.tokenizer.model.AccountEntity;
import com.demo.tokenizer.model.TokenizedAccounts;
import com.demo.tokenizer.repository.TokenizedAccountRepository;
import com.demo.tokenizer.service.TokenizingService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.encrypt.TextEncryptor;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenizingServiceTests {

    @Mock
    private TokenizedAccountRepository mockRepository;
    @Mock
    private TextEncryptor mockEncryptor;
    @Captor
    ArgumentCaptor<AccountEntity> entityCaptor;
    private TokenizingService testSubject;

    public String acc1 = "acc1";
    public String acc2 = "acc2";
    public String tokenAcc1 = "tokenAcc1";
    public String tokenAcc2 = "tokenAcc2";

    public AccountEntity entity1 = AccountEntity.builder().id(1L)
            .rawAccountNumber(acc1)
            .tokenizedAccountNumber(tokenAcc1)
            .build();

    public AccountEntity entity2 = AccountEntity.builder().id(2L)
            .rawAccountNumber(acc2)
            .tokenizedAccountNumber(tokenAcc2)
            .build();

    @BeforeEach
    void setUp(){
        testSubject = new TokenizingService(mockRepository, mockEncryptor);
    }

    @Test
    @SuppressWarnings("unchecked")
    void testTokenize()
    {
        Set<String> rawAccountNumbers = Set.of(acc1, acc2);
        RawAccounts rawAccounts = new RawAccounts();
        rawAccounts.setAccountNumbers(rawAccountNumbers);

        when(mockRepository.findAllByRawAccountNumberIn(rawAccountNumbers))
                .thenReturn(Set.of(), Set.of(entity1, entity2));

        when(mockEncryptor.encrypt(anyString())).thenReturn(tokenAcc1, tokenAcc2);

        TokenizedAccounts actual = testSubject.tokenize(rawAccounts);

        assertThat(Objects.requireNonNull(actual).getTokenizedAccountNumbers())
                .isEqualTo(Set.of(tokenAcc1, tokenAcc2));

        verify(mockRepository, times(2)).save(entityCaptor.capture());

        List<AccountEntity> savedEntities = entityCaptor.getAllValues();

        assertThat(savedEntities)
                .extracting(AccountEntity::getTokenizedAccountNumber)
                .containsExactlyInAnyOrder(tokenAcc1, tokenAcc2);
    }

    @Test
    void testTokenizeExistingAccountFound()
    {
        Set<String> rawAccountNumbers = Set.of(acc1, acc2);
        RawAccounts rawAccounts = new RawAccounts();
        rawAccounts.setAccountNumbers(rawAccountNumbers);

        when(mockRepository.findAllByRawAccountNumberIn(rawAccounts.getAccountNumbers()))
                .thenReturn(Set.of(entity2));

        Exception exception = assertThrows(EntityExistsException.class, () -> {
            testSubject.tokenize(rawAccounts);
        });

        assertEquals("Some provided accounts already exist.", exception.getMessage());
    }

    @Test
    void testDetokenizeAllAccountsFound()
    {
        Set<String> tokenizedAccountNumbers = Set.of(tokenAcc1, tokenAcc2);
        TokenizedAccounts tokenizedAccounts = new TokenizedAccounts();
        tokenizedAccounts.setTokenizedAccountNumbers(tokenizedAccountNumbers);

        when(mockRepository.findAllByTokenizedAccountNumberIn(tokenizedAccountNumbers))
                .thenReturn(Set.of(entity1, entity2));

        RawAccounts actual = testSubject.detokenize(tokenizedAccounts);

        assertThat(Objects.requireNonNull(actual).getAccountNumbers())
                .isEqualTo(Set.of(acc1, acc2));
    }

    @Test
    void testDetokenizeAccountNotFound()
    {
        Set<String> tokenizedAccountNumbers = Set.of(tokenAcc1, tokenAcc2);
        TokenizedAccounts tokenizedAccounts = new TokenizedAccounts();
        tokenizedAccounts.setTokenizedAccountNumbers(tokenizedAccountNumbers);

        when(mockRepository.findAllByTokenizedAccountNumberIn(tokenizedAccountNumbers))
                .thenReturn(Set.of(entity1));

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            testSubject.detokenize(tokenizedAccounts);
        });

        assertEquals("Some account numbers were not found.", exception.getMessage());
    }

}
