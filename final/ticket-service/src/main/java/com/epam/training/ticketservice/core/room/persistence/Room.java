package com.epam.training.ticketservice.core.room.persistence;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "Rooms")
@Data
@NoArgsConstructor
public class Room{

    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String roomName;
    private int seatRowNumber;
    private int seatColNumber;

    public Room(String roomName, int seatRowNumber, int seatColNumber) {
        this.roomName = roomName;
        this.seatRowNumber = seatRowNumber;
        this.seatColNumber = seatColNumber;
    }

}
