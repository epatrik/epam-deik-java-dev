package com.epam.training.ticketservice.core.screening;

import com.epam.training.ticketservice.core.movie.persistence.Movie;
import com.epam.training.ticketservice.core.movie.persistence.MovieRepository;
import com.epam.training.ticketservice.core.room.persistence.Room;
import com.epam.training.ticketservice.core.room.persistence.RoomRepository;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.Screening;
import com.epam.training.ticketservice.core.screening.persistence.ScreeningRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ScreeningServiceImpl implements ScreeningService{

    private final ScreeningRepository screeningRepository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;

    public String createScreening(String movieTitle, String roomName, LocalDateTime startTime) {
        Optional<Movie> movie = movieRepository.findByTitle(movieTitle);
        Optional<Room> room = roomRepository.findByRoomName(roomName);

        if (movie.isPresent() && room.isPresent()) {
            LocalDateTime endTime = startTime.plusMinutes(movie.get().getLength());

            List<Screening> screenings = screeningRepository.findAllByRoomName(roomName);
            for (Screening screening : screenings) {
                LocalDateTime screeningStart = screening.getStartTime();
                LocalDateTime screeningEnd = screeningStart.plusMinutes(movieRepository.findByTitle(screening.getMovieTitle()).get().getLength());
                LocalDateTime breakStart = screeningStart.minusMinutes(10);
                LocalDateTime breakEnd = screeningEnd.plusMinutes(10);

                if ((startTime.isAfter(screeningStart) && startTime.isBefore(screeningEnd)) ||
                        (endTime.isAfter(screeningStart) && endTime.isBefore(screeningEnd))) {
                    return "There is an overlapping screening";
                }
                if (startTime.isAfter(screeningEnd) && startTime.isBefore(breakEnd) ||
                        endTime.isAfter(breakStart) && endTime.isBefore(screeningStart)) {
                    return "This would start in the break period after another screening in this room";
                }
            }

            Screening newScreening = new Screening(movieTitle, roomName, startTime);
            screeningRepository.save(newScreening);
            return "Screening created";
        }
        else {
            return "Movie or room not found";
        }
    }

    public void deleteScreening(String movieTitle, String roomName, LocalDateTime startTime) {
        Optional<Screening> screening = screeningRepository.findByMovieTitleAndRoomNameAndStartTime(movieTitle, roomName, startTime);
        screening.ifPresent(screeningRepository::delete);
    }

    public Optional<List<ScreeningDto>> listScreenings() {
        List<Screening> screenings = screeningRepository.findAll();
        if (screenings.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(screenings.stream()
                .map(screening -> new ScreeningDto(screening.getMovieTitle(), screening.getRoomName(), screening.getStartTime()))
                .collect(Collectors.toList()));
    }
}
