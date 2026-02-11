package com.demo.tokenizer.RepositoryTests;

import com.demo.tokenizer.Model.AccountEntity;
import com.demo.tokenizer.Repository.TokenizedAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
public class TokenizedAccountRepositoryTests {
    @Autowired
    private TokenizedAccountRepository testSubject;

    @BeforeEach
    void setUp()
    {
        AccountEntity entity1 = new AccountEntity(1L, "accToken1", "acc1");
        AccountEntity entity2 = new AccountEntity(2L, "accToken2", "acc2");

        testSubject.save(entity1);
        testSubject.save(entity2);
    }

    @Test
    void testFindAllByTokenizedAccountNumber()
    {
        Set<AccountEntity> result = testSubject.findAllByTokenizedAccountNumberIn(Set.of("accToken1", "accToken2"));

        assertThat(result)
                .extracting(AccountEntity::getRawAccountNumber)
                .containsExactlyInAnyOrder("acc1", "acc2");
    }

    @Test
    void testFindAllByRawAccountNumber()
    {
        Set<AccountEntity> result = testSubject.findAllByRawAccountNumberIn(Set.of("acc1", "acc2"));

        assertThat(result)
                .extracting(AccountEntity::getTokenizedAccountNumber)
                .containsExactlyInAnyOrder("accToken1", "accToken2");
    }
}
