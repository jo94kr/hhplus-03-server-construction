package io.hhplus.server_construction.support.dummy;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class DummyDataService {

    private final JdbcTemplate jdbcTemplate;
    private final Random random = new Random();

    private static final int BATCH_SIZE = 1000; // 배치 크기 설정
    private static final int CONCERT_CNT = 20;
    private static final int SCHEDULE_CNT = 10000;
    private static final int SEAT_CNT = 100000;

    public void initializeData() throws SQLException {
        deleteAllData();
        insertConcerts();
        insertConcertSchedules();
        insertConcertSeats();
    }

    private void deleteAllData() throws SQLException {
        String[] tables = {"concert_seat", "concert_schedule", "concert"};
        try (Connection connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
            connection.setAutoCommit(false);
            for (String table : tables) {
                try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM " + table)) {
                    preparedStatement.executeUpdate();
                }
            }
            connection.commit();
        }
    }

    private void insertConcerts() throws SQLException {
        String sql = "INSERT INTO concert (id, name, create_datetime) VALUES (?, ?, ?)";
        try (Connection connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);

            int batchCounter = 0;
            for (long i = 1; i <= CONCERT_CNT; i++) {
                preparedStatement.setLong(1, i);
                preparedStatement.setString(2, "Concert " + i);
                preparedStatement.setString(3, getCurrentDateTime());
                preparedStatement.addBatch();

                batchCounter++;
                if (batchCounter % BATCH_SIZE == 0) {
                    preparedStatement.executeBatch();
                    connection.commit();
                    batchCounter = 0;
                }
            }

            if (batchCounter > 0) {
                preparedStatement.executeBatch();
                connection.commit();
            }
        }
    }

    private void insertConcertSchedules() throws SQLException {
        String sql = "INSERT INTO concert_schedule (id, concert_id, concert_datetime, create_datetime) VALUES (?, ?, ?, ?)";
        try (Connection connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);

            long id = 1;
            int batchCounter = 0;
            for (long concertId = 1; concertId <= CONCERT_CNT; concertId++) {
                preparedStatement.setLong(2, concertId);
                for (int j = 0; j < SCHEDULE_CNT / CONCERT_CNT; j++) {
                    preparedStatement.setLong(1, id++);
                    preparedStatement.setString(3, getRandomDateTime(random));
                    preparedStatement.setString(4, getCurrentDateTime());
                    preparedStatement.addBatch();

                    batchCounter++;
                    if (batchCounter % BATCH_SIZE == 0) {
                        preparedStatement.executeBatch();
                        connection.commit();
                        batchCounter = 0;
                    }
                }
            }

            if (batchCounter > 0) {
                preparedStatement.executeBatch();
                connection.commit();
            }
        }
    }

    private void insertConcertSeats() throws SQLException {
        String sql = "INSERT INTO concert_seat (id, concert_schedule_id, seat_num, grade, price, status, create_datetime) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);

            int batchCounter = 0;
            for (long i = 1; i <= SEAT_CNT; i++) {
                preparedStatement.setLong(1, i);
                preparedStatement.setLong(2, (i % SCHEDULE_CNT) + 1);
                preparedStatement.setString(3, String.format("%02d", (i % 50) + 1));
                String grade = getGrade(random);
                preparedStatement.setString(4, grade);
                preparedStatement.setInt(5, getPrice(grade));
                preparedStatement.setString(6, getRandomStatus(random));
                preparedStatement.setString(7, getCurrentDateTime());
                preparedStatement.addBatch();

                batchCounter++;
                if (batchCounter % BATCH_SIZE == 0) {
                    preparedStatement.executeBatch();
                    connection.commit();
                    batchCounter = 0;
                }
            }

            if (batchCounter > 0) {
                preparedStatement.executeBatch();
                connection.commit();
            }
        }
    }

    private String getCurrentDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private String getRandomDateTime(Random random) {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        long days = startDate.toEpochDay() + random.nextInt(365);
        LocalDate randomDate = LocalDate.ofEpochDay(days);
        LocalTime randomTime = LocalTime.of(random.nextInt(24), random.nextInt(60));
        return LocalDateTime.of(randomDate, randomTime).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private String getGrade(Random random) {
        int val = random.nextInt(3);
        if (val == 0) return "GOLD";
        else if (val == 1) return "SILVER";
        else return "BRONZE";
    }

    private int getPrice(String grade) {
        return switch (grade) {
            case "GOLD" -> 3000;
            case "SILVER" -> 2000;
            case "BRONZE" -> 1000;
            default -> throw new IllegalArgumentException("Unknown grade: " + grade);
        };
    }

    private String getRandomStatus(Random random) {
        int val = random.nextInt(3);
        if (val == 0) return "POSSIBLE";
        else if (val == 1) return "PENDING";
        else return "SOLD_OUT";
    }
}
