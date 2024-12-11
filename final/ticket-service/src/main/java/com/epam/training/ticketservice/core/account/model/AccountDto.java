package com.epam.training.ticketservice.core.account.model;

import com.epam.training.ticketservice.core.account.persistence.Account;

public record AccountDto(String username, Account.Role role) {
}
