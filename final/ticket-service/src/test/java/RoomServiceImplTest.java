import com.epam.training.ticketservice.core.room.RoomServiceImpl;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.room.persistence.Room;
import com.epam.training.ticketservice.core.room.persistence.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoomServiceImplTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomServiceImpl roomService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateRoom() {
        roomService.createRoom("Room1", 10, 20);
        verify(roomRepository, times(1)).save(any(Room.class));
    }

    @Test
    void testUpdateRoom() {
        Room room = new Room("Room1", 10, 20);
        when(roomRepository.findByRoomName("Room1")).thenReturn(Optional.of(room));

        roomService.updateRoom("Room1", 15, 25);
        assertEquals(15, room.getSeatRowNumber());
        assertEquals(25, room.getSeatColNumber());
        verify(roomRepository, times(1)).save(room);
    }

    @Test
    void testDeleteRoom() {
        Room room = new Room("Room1", 10, 20);
        when(roomRepository.findByRoomName("Room1")).thenReturn(Optional.of(room));

        roomService.deleteRoom("Room1");
        verify(roomRepository, times(1)).delete(room);
    }

    @Test
    void testListRooms() {
        assertTrue(roomService.listRooms().isEmpty());
        Room room = new Room("Room1", 10, 20);
        when(roomRepository.findAll()).thenReturn(List.of(room));

        Optional<List<RoomDto>> rooms = roomService.listRooms();
        assertTrue(rooms.isPresent());
        assertEquals(1, rooms.get().size());
        assertEquals("Room1", rooms.get().get(0).roomName());
    }
}

