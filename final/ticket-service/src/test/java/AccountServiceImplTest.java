import com.epam.training.ticketservice.core.account.AccountServiceImpl;
import com.epam.training.ticketservice.core.account.model.AccountDto;
import com.epam.training.ticketservice.core.account.persistence.Account;
import com.epam.training.ticketservice.core.account.persistence.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSignInSuccess() {
        Account account = new Account("user", "pass", Account.Role.USER);
        when(accountRepository.findByUsernameAndPassword("user", "pass")).thenReturn(Optional.of(account));

        Optional<AccountDto> result = accountService.signIn("user", "pass");
        assertTrue(result.isPresent());
        assertEquals("user", result.get().username());
    }

    @Test
    void testSignInFailed() {
        when(accountRepository.findByUsernameAndPassword("user", "pass")).thenReturn(Optional.empty());

        Optional<AccountDto> result = accountService.signIn("user", "pass");
        assertFalse(result.isPresent());
    }

    @Test
    void testSignInPrivilegedSuccess() {
        Account account = new Account("admin", "pass", Account.Role.ADMIN);
        when(accountRepository.findByUsernameAndPassword("admin", "pass")).thenReturn(Optional.of(account));

        Optional<AccountDto> result = accountService.signInPrivileged("admin", "pass");
        assertTrue(result.isPresent());
        assertEquals("admin", result.get().username());
    }

    @Test
    void testSignInPrivilegedFailed() {
        when(accountRepository.findByUsernameAndPassword("admin", "pass")).thenReturn(Optional.empty());

        Optional<AccountDto> result = accountService.signInPrivileged("admin", "pass");
        assertFalse(result.isPresent());
    }

    @Test
    void testSignOut() {
        accountService.signOut();
        assertFalse(accountService.describe().isPresent());
    }

    @Test
    void testDescribe() {
        Account account = new Account("user", "pass", Account.Role.USER);
        when(accountRepository.findByUsernameAndPassword("user", "pass")).thenReturn(Optional.of(account));
        accountService.signIn("user", "pass");

        Optional<AccountDto> result = accountService.describe();
        assertTrue(result.isPresent());
        assertEquals("user", result.get().username());
    }

    @Test
    void testSignUp() {
        accountService.signUp("user", "pass");
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testCreateAdminAccount() {
        when(accountRepository.findByUsername("admin")).thenReturn(Optional.empty());

        accountService.init();
        verify(accountRepository, times(1)).save(any(Account.class));
    }
}
