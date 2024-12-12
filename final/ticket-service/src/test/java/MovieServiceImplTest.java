import com.epam.training.ticketservice.core.movie.MovieServiceImpl;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.Movie;
import com.epam.training.ticketservice.core.movie.persistence.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovieServiceImplTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieServiceImpl movieService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateMovie() {
        movieService.createMovie("Movie1", "Genre1", 120);
        verify(movieRepository, times(1)).save(any(Movie.class));
    }

    @Test
    void testUpdateMovie() {
        Movie movie = new Movie("Movie1", "Genre1", 120);
        when(movieRepository.findByTitle("Movie1")).thenReturn(Optional.of(movie));

        movieService.updateMovie("Movie1", "Genre2", 130);
        assertEquals("Genre2", movie.getGenre());
        assertEquals(130, movie.getLength());
        verify(movieRepository, times(1)).save(movie);
    }

    @Test
    void testDeleteMovie() {
        Movie movie = new Movie("Movie1", "Genre1", 120);
        when(movieRepository.findByTitle("Movie1")).thenReturn(Optional.of(movie));

        movieService.deleteMovie("Movie1");
        verify(movieRepository, times(1)).delete(movie);
    }

    @Test
    void testListMovies() {
        assertTrue(movieService.listMovies().isEmpty());
        Movie movie = new Movie("Movie1", "Genre1", 120);
        when(movieRepository.findAll()).thenReturn(List.of(movie));

        Optional<List<MovieDto>> movies = movieService.listMovies();
        assertTrue(movies.isPresent());
        assertEquals(1, movies.get().size());
        assertEquals("Movie1", movies.get().get(0).title());
    }

    @Test
    void testFindByTitle() {
        Movie movie = new Movie("Movie1", "Genre1", 120);
        when(movieRepository.findByTitle("Movie1")).thenReturn(Optional.of(movie));

        Optional<MovieDto> movieDto = movieService.getMovieByTitle("Movie1");
        assertTrue(movieDto.isPresent());
        assertEquals("Movie1", movieDto.get().title());
    }
}