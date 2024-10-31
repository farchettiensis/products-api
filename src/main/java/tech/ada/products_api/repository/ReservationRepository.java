package tech.ada.products_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.ada.products_api.model.Reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByTableNumberAndReservationTime_DateAndReservationTime_Time(
            int tableNumber,
            LocalDate date,
            LocalTime time);

    @Query(value = "SELECT * FROM reservation WHERE LOWER(customer_name) LIKE LOWER(CONCAT('%', :customerName, '%'))", nativeQuery = true)
    List<Reservation> findByCustomerNameNative(@Param("customerName") String customerName);
}
