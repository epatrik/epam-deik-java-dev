package com.epam.training.ticketservice.core.account;

import com.epam.training.ticketservice.core.account.model.AccountDto;
import com.epam.training.ticketservice.core.account.persistence.Account;
import com.epam.training.ticketservice.core.account.persistence.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private AccountDto signedInAccount = null;

    @Override
    public Optional<AccountDto> signIn(String username, String password) {
        Optional<Account> account = accountRepository.findByUsernameAndPassword(username, password);
        if (account.isEmpty() || account.get().getRole() == Account.Role.ADMIN){
            return Optional.empty();
        }
        signedInAccount = new AccountDto(account.get().getUsername(), account.get().getRole());
        return describe();
    }

    @Override
    public Optional<AccountDto> signInPrivileged(String username, String password) {
        Optional<Account> account = accountRepository.findByUsernameAndPassword(username, password);
        if (account.isEmpty() || account.get().getRole() == Account.Role.USER) {
            return Optional.empty();
        }
        signedInAccount = new AccountDto(account.get().getUsername(), account.get().getRole());
        return describe();
    }

    @Override
    public Optional<AccountDto> signOut() {
        Optional<AccountDto> previouslyLoggedInAccount = describe();
        signedInAccount = null;
        return previouslyLoggedInAccount;
    }

    @Override
    public Optional<AccountDto> describe() {
        return Optional.ofNullable(signedInAccount);
    }

    @Override
    public void signUp(String username, String password) {
        Account account = new Account(username, password, Account.Role.USER);
        accountRepository.save(account);
    }
}
