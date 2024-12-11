package com.epam.training.ticketservice.core.account;

import com.epam.training.ticketservice.core.account.model.AccountDto;
import java.util.Optional;

public interface AccountService {

    Optional<AccountDto> signIn(String username, String password);

    Optional<AccountDto> signInPrivileged(String username, String password);

    Optional<AccountDto> signOut();

    Optional<AccountDto> describe();

    void signUp(String username, String password);
}