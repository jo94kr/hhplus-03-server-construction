package io.hhplus.server_construction.domain.concert.vo;

public class ConcertSeatEnums {

    public enum Grade {
        GOLD, SILVER, BRONZE
    }

    public enum Status {
        POSSIBLE, PENDING, SOLD_OUT;

        public boolean isPossible() {
            return this == POSSIBLE;
        }
    }
}
