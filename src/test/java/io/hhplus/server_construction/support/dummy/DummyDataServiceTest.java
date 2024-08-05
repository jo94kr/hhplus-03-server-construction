package io.hhplus.server_construction.support.dummy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DummyDataServiceTest {

    @Autowired
    DummyDataService dummyDataService;

    @Test
    void initializeData() throws SQLException {
        dummyDataService.initializeData();
    }

}
