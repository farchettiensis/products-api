package tech.ada.products_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tech.ada.products_api.dto.ReservationDTO;

@Entity
@Table(name = "reservation",
        indexes = {
                @Index(name = "idx_reservation_date", columnList = "date"),
                @Index(name = "idx_reservation_time", columnList = "time")
        },
        uniqueConstraints = @UniqueConstraint(columnNames = {"tableNumber", "date", "time"})
)
@Getter
@Setter
@ToString
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String customerName;

    @Embedded
    private ReservationTime reservationTime;

    @Column(nullable = false)
    @Positive
    private int tableNumber;

    public ReservationDTO toDTO() {
        return ReservationDTO.fromReservation(this);
    }
}
