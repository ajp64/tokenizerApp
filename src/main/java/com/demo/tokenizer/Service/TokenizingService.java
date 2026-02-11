package com.demo.tokenizer.Service;

import com.demo.tokenizer.Model.RawAccounts;
import com.demo.tokenizer.Model.TokenizedAccounts;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TokenizingService {

    public TokenizedAccounts tokenize(RawAccounts rawAccounts) {
        Set<String> placeholderTokenNos = rawAccounts.getAccountNumbers()
                .stream().map(acc -> acc + "salt")
                .collect(Collectors.toSet());

        TokenizedAccounts retVal = new TokenizedAccounts();
        retVal.setTokenizedAccountNumbers(placeholderTokenNos);

        return retVal;
    }

    public RawAccounts detokenize(TokenizedAccounts tokenizedAccounts) {
        Set<String> placeholderRawNos = tokenizedAccounts.getTokenizedAccountNumbers()
                .stream().map(acc -> acc + "salt")
                .collect(Collectors.toSet());

        RawAccounts retVal = new RawAccounts();
        retVal.setAccountNumbers(placeholderRawNos);

        return retVal;
    }

}
