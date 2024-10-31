package tech.ada.products_api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.ada.products_api.dto.ReservationDTO;
import tech.ada.products_api.dto.ResponseDTO;
import tech.ada.products_api.service.ReservationService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/reservations")
@Tag(name = "Reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/list-all")
    public ResponseEntity<ResponseDTO<List<ReservationDTO>>> listAllReservations() {
        ResponseDTO<List<ReservationDTO>> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/list-by-customer-name")
    public ResponseEntity<ResponseDTO<List<ReservationDTO>>> listAllByCustomerName(@RequestParam("customerName") String customerName) {
        ResponseDTO<List<ReservationDTO>> reservations = reservationService.getAllReservationsByCustomerName(customerName);
        return ResponseEntity.ok(reservations);
    }

    @PostMapping
    public ResponseEntity<ReservationDTO> createReservation(@RequestBody ReservationDTO reservationDTO) {
        ReservationDTO createdReservation = reservationService.createReservation(reservationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReservation);
    }

    @PutMapping
    public ResponseEntity<ReservationDTO> updateReservation(@RequestBody ReservationDTO reservationDTO) {
        ReservationDTO updatedReservation = reservationService.updateReservation(reservationDTO);
        return ResponseEntity.ok(updatedReservation);
    }

    @DeleteMapping("/{table-number}")
    public ResponseEntity<Void> delete(
            @PathVariable("table-number") Integer tableNumber,
            @RequestHeader("reservation-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reservationDate,
            @RequestHeader("reservation-time") @DateTimeFormat(pattern = "HH:mm") LocalTime reservationTime) {

        reservationService.deleteReservationByTableNumberAndTime(tableNumber, reservationDate, reservationTime);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

