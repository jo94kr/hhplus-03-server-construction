package io.hhplus.server_construction.interfaces.controller.reservation.dto;

import io.hhplus.server_construction.application.reservation.dto.ReservationConcertCommand;
import io.hhplus.server_construction.application.reservation.dto.ReservationConcertResult;
import io.hhplus.server_construction.domain.reservation.ReservationItem;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

public record ReservationConcert(

) {

    public record Request(
            @Schema(name = "콘서트 좌석 Ids")
            List<Long> concertSeatIdList,

            @Schema(name = "사용자 Id")
            Long userId
    ) {

        public ReservationConcertCommand toCommand() {
            return new ReservationConcertCommand(this.concertSeatIdList, this.userId);
        }
    }

    public record Response(
            @Schema(name = "예약 Id")
            Long reservationId,
            
            @Schema(name = "총 결제 금액")
            BigDecimal totalPrice,
            
            @Schema(name = "예약 품목")
            List<Item> reservationItemList
    ) {

        public static Response from(ReservationConcertResult reservationConcertResult) {
            return new Response(reservationConcertResult.reservationId(),
                    reservationConcertResult.totalPrice(),
                    reservationConcertResult.reservationItemList().stream()
                            .map(Item::from)
                            .toList());
        }

        public record Item(
                @Schema(name = "콘서트 좌석 Id")
                Long concertSeatId,
                
                @Schema(name = "좌석 번호")
                String seatNum,

                @Schema(name = "가격")
                BigDecimal price
        ) {

            public static Item from(ReservationItem reservationItem) {
                return new Item(reservationItem.getConcertSeat().getId(),
                        reservationItem.getConcertSeat().getSeatNum(),
                        reservationItem.getPrice());
            }
        }
    }

}
