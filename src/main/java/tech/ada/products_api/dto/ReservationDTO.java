package tech.ada.products_api.dto;

import tech.ada.products_api.model.Reservation;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationDTO(
        Long id,
        String customerName,
        int tableNumber,
        LocalDate reservationDate,
        LocalTime reservationTime
) {
    public static ReservationDTO fromReservation(Reservation reservation) {
        return new ReservationDTO(
                reservation.getId(),
                reservation.getCustomerName(),
                reservation.getTableNumber(),
                reservation.getReservationTime().getDate(),
                reservation.getReservationTime().getTime()
        );
    }
}
