package io.hhplus.server_construction.domain.reservation.vo;

public enum ReservationStatusEnums {

    PAYMENT_WAITING, PAYMENT_COMPLETE, CANCEL;

    public boolean isPaymentWaiting() {
        return this.equals(PAYMENT_WAITING);
    }
}
