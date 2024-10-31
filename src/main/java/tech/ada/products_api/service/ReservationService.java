package tech.ada.products_api.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.ada.products_api.dto.ReservationDTO;
import tech.ada.products_api.dto.ResponseDTO;
import tech.ada.products_api.model.Reservation;
import tech.ada.products_api.model.ReservationTime;
import tech.ada.products_api.repository.ReservationRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public ReservationDTO createReservation(ReservationDTO reservationDTO) {
        Reservation reservationFromDTO = reservationDTO.toReservation();
        Reservation reservation = reservationRepository.save(reservationFromDTO);
        return reservation.toDTO();
    }

    public List<ReservationDTO> createReservations(final List<ReservationDTO> reservationsDTO) {
        List<Reservation> reservations = reservationsDTO.stream()
                .map(reservationDTO -> reservationDTO.toReservation())
                .collect(Collectors.toList());

        return reservationRepository.saveAll(reservations).stream()
                .map(reservation -> reservation.toDTO())
                .collect(Collectors.toList());
    }

    public ResponseDTO<ReservationDTO> getReservationById(Long id) {
        return reservationRepository.findById(id)
                .map(reservation -> ResponseDTO.<ReservationDTO>builder()
                        .message("Reservation found with ID: " + id)
                        .timestamp(LocalDateTime.now())
                        .data(reservation.toDTO())
                        .build())
                .orElseGet(() -> ResponseDTO.<ReservationDTO>builder()
                        .message("No reservation found with ID: " + id)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    public ResponseDTO<List<ReservationDTO>> getAllReservations() {
        List<ReservationDTO> reservations = reservationRepository.findAll()
                .stream()
                .map(Reservation::toDTO)
                .collect(Collectors.toList());

        return ResponseDTO.<List<ReservationDTO>>builder()
                .message("Reservations found")
                .timestamp(LocalDateTime.now())
                .data(reservations)
                .build();
    }

    public ResponseDTO<List<ReservationDTO>> getAllReservationsByCustomerName(String customerName) {
        List<ReservationDTO> reservations = reservationRepository.findByCustomerNameNative(customerName)
                .stream()
                .map(Reservation::toDTO)
                .collect(Collectors.toList());

        return ResponseDTO.<List<ReservationDTO>>builder()
                .message("Reservations found for customer: " + customerName)
                .timestamp(LocalDateTime.now())
                .data(reservations)
                .build();
    }

    public ResponseDTO<Void> deleteReservationByTableNumberAndTime(int tableNumber, LocalDate reservationDate, LocalTime reservationTime) {
        Optional<Reservation> reservation = reservationRepository.findByTableNumberAndReservationTime_DateAndReservationTime_Time(tableNumber, reservationDate, reservationTime);

        if (reservation.isPresent()) {
            reservationRepository.delete(reservation.get());
            return ResponseDTO.<Void>builder()
                    .message("Reservation deleted successfully for table " + tableNumber)
                    .timestamp(LocalDateTime.now())
                    .build();
        } else {
            return ResponseDTO.<Void>builder()
                    .message("No reservation found for table " + tableNumber)
                    .timestamp(LocalDateTime.now())
                    .build();
        }
    }

    @Transactional
    public ResponseDTO<Void> deleteReservationById(Long id) {
        String deleteQuery = "DELETE FROM reservation WHERE id = ?1";

        int rowsAffected = entityManager.createNativeQuery(deleteQuery)
                .setParameter(1, id)
                .executeUpdate();

        if (rowsAffected > 0) {
            return ResponseDTO.<Void>builder()
                    .message("Reservation with ID " + id + " deleted successfully.")
                    .timestamp(LocalDateTime.now())
                    .build();
        } else {
            return ResponseDTO.<Void>builder()
                    .message("No reservation found with ID " + id)
                    .timestamp(LocalDateTime.now())
                    .build();
        }
    }

    public ReservationDTO updateReservation(ReservationDTO reservationDTO) {
        Reservation existingReservation = reservationRepository.findById(reservationDTO.id())
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found with ID: " + reservationDTO.id()));

        existingReservation.setCustomerName(reservationDTO.customerName());
        existingReservation.setTableNumber(reservationDTO.tableNumber());

        ReservationTime reservationTime = new ReservationTime();
        reservationTime.setDate(reservationDTO.reservationDate());
        reservationTime.setTime(reservationDTO.reservationTime());
        existingReservation.setReservationTime(reservationTime);

        Reservation updatedReservation = reservationRepository.save(existingReservation);
        return updatedReservation.toDTO();
    }
}
