package io.hhplus.server_construction.domain.waiting.vo;

public class WaitingConstant {

    // 스케쥴러 한 주기 (10초) 당 진입시키는 유저 수
    public static final Long ENTRY_LIMIT = 1000L;

    // 분당 처리량
    public static final Long THROUGHPUT_PER_MINUTE = 10L;
    public static final Long CYCLE_TIME = 1L;

    public static final String WAITING_KEY = "waiting";
    public static final String ACTIVE_KEY_PREFIX = "active:";
}
