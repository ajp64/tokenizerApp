package com.demo.tokenizer.Repository;

import com.demo.tokenizer.Model.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TokenizedAccountRepository extends JpaRepository<AccountEntity, Long> {
    Set<AccountEntity> findAllByTokenizedAccountNumberIn(final Set<String> tokenizedAccountNumbers);
    Set<AccountEntity> findAllByRawAccountNumberIn(final Set<String> rawAccountNumbers);
}
