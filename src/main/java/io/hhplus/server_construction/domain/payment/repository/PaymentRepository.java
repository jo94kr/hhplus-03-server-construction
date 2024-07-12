package io.hhplus.server_construction.domain.payment.repository;

import io.hhplus.server_construction.domain.payment.Payment;
import io.hhplus.server_construction.domain.reservation.Reservation;
import io.hhplus.server_construction.domain.reservation.ReservationItem;

import java.util.List;

public interface PaymentRepository {

    Payment save(Payment payment);
}
