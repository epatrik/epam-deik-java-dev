import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.screening.ScreeningServiceImpl;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.Screening;
import com.epam.training.ticketservice.core.screening.persistence.ScreeningRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScreeningServiceImplTest {

    @Mock
    private ScreeningRepository screeningRepository;

    @Mock
    private MovieService movieService;

    @Mock
    private RoomService roomService;

    @InjectMocks
    private ScreeningServiceImpl screeningService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDeleteScreening() {
        Screening screening = new Screening("Movie1", "Room1", LocalDateTime.now());
        when(screeningRepository.findByMovieTitleAndRoomNameAndStartTime("Movie1", "Room1", screening.getStartTime()))
                .thenReturn(Optional.of(screening));

        screeningService.deleteScreening("Movie1", "Room1", screening.getStartTime());
        verify(screeningRepository, times(1)).delete(screening);
    }

    @Test
    void testListScreenings() {
        assertTrue(screeningService.listScreenings().isEmpty());
        Screening screening = new Screening("Movie1", "Room1", LocalDateTime.now());
        when(screeningRepository.findAll()).thenReturn(List.of(screening));

        Optional<List<ScreeningDto>> screenings = screeningService.listScreenings();
        assertTrue(screenings.isPresent());
        assertEquals(1, screenings.get().size());
        assertEquals("Movie1", screenings.get().get(0).movieTitle());
    }
}