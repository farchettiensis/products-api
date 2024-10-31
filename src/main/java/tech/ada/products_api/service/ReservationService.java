package tech.ada.products_api.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.ada.products_api.dto.ReservationDTO;
import tech.ada.products_api.model.Reservation;
import tech.ada.products_api.model.ReservationTime;
import tech.ada.products_api.repository.ReservationRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
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

    public List<ReservationDTO> listAll() {
        return reservationRepository.findAll()
                .stream()
                .map(reservation -> ReservationDTO.fromReservation(reservation))
                .collect(Collectors.toList());
    }

    public List<ReservationDTO> listAll(String customerName) {
        return reservationRepository.findByCustomerNameNative(customerName)
                .stream()
                .map(Reservation::toDTO)
                .collect(Collectors.toList());
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

    public void deleteReservationByTableNumberAndTime(int tableNumber, LocalDate reservationDate, LocalTime reservationTime) {
        Reservation reservation = reservationRepository
                .findByTableNumberAndReservationTime_DateAndReservationTime_Time(tableNumber, reservationDate, reservationTime)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found for table number: " + tableNumber
                        + " on date: " + reservationDate + " at time: " + reservationTime));

        reservationRepository.delete(reservation);
    }

    @Transactional
    public void deleteReservationById(Long id) {
        String deleteQuery = "DELETE FROM reservation WHERE id = ?1";

        entityManager.createNativeQuery(deleteQuery)
                .setParameter(1, id)
                .executeUpdate();
    }
}
