package io.hhplus.server_construction.domain.concert.service;

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

            for (long i = 1; i <= 1000; i++) {
                preparedStatement.setLong(1, i);
                preparedStatement.setString(2, "Concert " + i);
                preparedStatement.setString(3, getCurrentDateTime());
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
            connection.commit();
        }
    }

    private void insertConcertSchedules() throws SQLException {
        String sql = "INSERT INTO concert_schedule (id, concert_id, concert_datetime, status, create_datetime) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);

            preparedStatement.setString(4, "AVAILABLE");
            long id = 1;
            for (long concertId = 1; concertId <= 1000; concertId++) {
                // 각 콘서트에 대해 랜덤한 일정 수를 생성 (1 ~ 30개)
                preparedStatement.setLong(2, concertId);
                int scheduleCount = random.nextInt(30) + 1;
                for (int j = 0; j < scheduleCount; j++) {
                    preparedStatement.setLong(1, id++);
                    preparedStatement.setString(3, getRandomDateTime(random));
                    preparedStatement.setString(5, getCurrentDateTime());
                    preparedStatement.addBatch();
                }
            }

            preparedStatement.executeBatch();
            connection.commit();
        }
    }

    private void insertConcertSeats() throws SQLException {
        String sql = "INSERT INTO concert_seat (id, concert_schedule_id, seat_num, grade, price, status, create_datetime) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);

            preparedStatement.setString(6, "POSSIBLE");
            for (long i = 1; i <= 3000; i++) {
                preparedStatement.setLong(1, i);
                preparedStatement.setLong(2, (i % 1000) + 1);
                preparedStatement.setString(3, String.format("%02d", (i % 50) + 1));
                String grade = getGrade(random);
                preparedStatement.setString(4, grade);
                preparedStatement.setInt(5, getPrice(grade));
                preparedStatement.setString(7, getCurrentDateTime());
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
            connection.commit();
        }
    }

    private String getCurrentDateTime() {
        return LocalDateTime.now().toString();
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
        if (grade.equals("GOLD")) return 3000;
        else if (grade.equals("SILVER")) return 2000;
        else return 1000;
    }
}
