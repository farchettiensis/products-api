package tech.ada.products_api.dto;

import tech.ada.products_api.model.Reservation;
import tech.ada.products_api.model.ReservationTime;

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

    public Reservation toReservation() {
        Reservation reservation = new Reservation();
        reservation.setId(this.id());
        reservation.setCustomerName(this.customerName());
        reservation.setTableNumber(this.tableNumber());

        ReservationTime reservationTime = new ReservationTime();
        reservationTime.setDate(this.reservationDate());
        reservationTime.setTime(this.reservationTime());
        reservation.setReservationTime(reservationTime);

        return reservation;
    }
}
