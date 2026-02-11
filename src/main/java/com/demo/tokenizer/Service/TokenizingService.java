package com.demo.tokenizer.Service;

import com.demo.tokenizer.Model.AccountEntity;
import com.demo.tokenizer.Model.RawAccounts;
import com.demo.tokenizer.Model.TokenizedAccounts;
import com.demo.tokenizer.Repository.TokenizedAccountRepository;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TokenizingService {

    private final TokenizedAccountRepository repository;
    private final TextEncryptor encryptor;

    public TokenizingService(TokenizedAccountRepository repository, TextEncryptor encryptor)
    {
        this.repository = repository;
        this.encryptor = encryptor;
    }

    public TokenizedAccounts tokenize(RawAccounts rawAccounts) {
        rawAccounts.getAccountNumbers().forEach(this::createAndStoreAccountEntity);

        Set<AccountEntity> createdAccounts =
                repository.findAllByRawAccountNumberIn(rawAccounts.getAccountNumbers());

        Set<String> tokenizedAccountNumbers =
                createdAccounts.stream()
                        .map(AccountEntity::getTokenizedAccountNumber)
                        .collect(Collectors.toSet());

        TokenizedAccounts retVal = new TokenizedAccounts();
        retVal.setTokenizedAccountNumbers(tokenizedAccountNumbers);

        return retVal;
    }

    public RawAccounts detokenize(TokenizedAccounts tokenizedAccounts) {
        Set<String> tokenizedNumbers = tokenizedAccounts.getTokenizedAccountNumbers();

        Set<AccountEntity> fetchedAccountNumbers =
                repository.findAllByTokenizedAccountNumberIn(tokenizedNumbers);

        Set<String> detokenizedAccountNumbers =
                fetchedAccountNumbers.stream()
                        .map(AccountEntity::getRawAccountNumber)
                        .collect(Collectors.toSet());

        RawAccounts retVal = new RawAccounts();
        retVal.setAccountNumbers(detokenizedAccountNumbers);

        return retVal;
    }

    private void createAndStoreAccountEntity(String rawAccountNumber){
        AccountEntity entity = new AccountEntity();
        entity.setRawAccountNumber(rawAccountNumber);
        entity.setTokenizedAccountNumber(encryptor.encrypt(rawAccountNumber));
        repository.save(entity);
    }

}
