package io.hhplus.server_construction.domain.concert.vo;

public class ConcertScheduleEnums {

    public enum ScheduleStatus {
        SOLD_OUT, AVAILABLE, UNAVAILABLE;

        public boolean isAvailable() {
            return this.name().equals(AVAILABLE.name());
        }
    }
}
