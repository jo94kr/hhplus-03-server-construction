package io.hhplus.server_construction.interfaces.controller.dummy;

import io.hhplus.server_construction.domain.concert.service.DummyDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@RequiredArgsConstructor
public class DummyDataController {

    private final DummyDataService dummyDataService;

    @PostMapping("/initialize")
    public String initializeData() {
        try {
            dummyDataService.initializeData();
            return "Data initialization successful!";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Data initialization failed!";
        }
    }
}
