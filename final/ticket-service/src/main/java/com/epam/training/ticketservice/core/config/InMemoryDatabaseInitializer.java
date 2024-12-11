package com.epam.training.ticketservice.core.config;

import com.epam.training.ticketservice.core.account.persistence.Account;
import com.epam.training.ticketservice.core.account.persistence.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("!prod")
public class InMemoryDatabaseInitializer {

    private final AccountRepository accountRepository;

    public void init() {
        Account admin = new Account("admin", "admin", Account.Role.ADMIN);
        accountRepository.save(admin);
    }
}
