package io.hhplus.server_construction.infra;

import io.hhplus.server_construction.infra.entity.PaymentEntity;
import io.hhplus.server_construction.infra.entity.ReservationItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, Long> {

}
