package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.account.AccountService;
import com.epam.training.ticketservice.core.account.persistence.Account;
import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@AllArgsConstructor
public class AccountCommand {
    private final AccountService accountService;

    @ShellMethod(key = "sign out", value = "Sing out from the account")
    public String signOut() {
        return accountService.signOut()
                .map(accountDto -> accountDto + " is signed out!")
                .orElse("You need to sign in first!");
    }

    @ShellMethod(key = "sign in", value = "User sign in")
    public String signIn(String username, String password) {
        return accountService.signIn(username, password)
                .map(accountDto -> accountDto + " is successfully logged in!")
                .orElse("Login failed due to incorrect credentials");
    }

    @ShellMethod(key = "sign in privileged", value = "Admin sign in")
    public String signInPrivileged(String username, String password) {
        return accountService.signInPrivileged(username, password)
                .map(accountDto -> accountDto + " is successfully logged in!")
                .orElse("Login failed due to incorrect credentials");
    }

    @ShellMethod(key = "describe account", value = "Get account information")
    public String describe() {
        if (accountService.describe().isPresent()) {
            if (accountService.describe().get().role() == Account.Role.ADMIN) {
                return accountService.describe()
                        .map(accountDto -> "Signed in with privileged account '" + accountDto.username() + "'")
                        .orElse("You need to login first!");
            }
            else {
                return accountService.describe()
                        .map(accountDto -> "Signed in with account '" + accountDto.username() + "'")
                        .orElse("You need to login first!");
            }
        }
        else {
            return "You need to login first!";
        }

    }

    @ShellMethod(key = "sign up", value = "Account registration")
    public String signUp(String username, String password) {
        try {
            accountService.signUp(username, password);
            return "Registration was successful!";
        } catch (Exception e) {
            return "Registration failed!";
        }
    }

}
