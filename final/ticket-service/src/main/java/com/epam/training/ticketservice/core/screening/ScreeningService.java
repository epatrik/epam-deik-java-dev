package com.epam.training.ticketservice.core.screening;

import com.epam.training.ticketservice.core.screening.model.ScreeningDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScreeningService {

    String createScreening(String movieTitle, String roomName, LocalDateTime screeningTime);

    void deleteScreening(String movieTitle, String roomName, LocalDateTime screeningTime);

    Optional<List<ScreeningDto>> listScreenings();
}
