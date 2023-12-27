package com.revspeed.services;

import com.revspeed.dao.ServicesDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;

import static org.mockito.Mockito.verify;

public class ServicesServiceTest {
    @Mock
    private ServicesDao servicesDao;

    @InjectMocks
    private ServicesService servicesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSeeServices() throws SQLException {
        // Arrange (optional)

        // Act
        servicesService.seeServices();

        // Assert
        verify(servicesDao).seeServices();
    }

    @Test
    void testSeeMyBill() throws SQLException {
        // Arrange
        int userId = 1;

        // Act
        servicesService.seeMyBill(userId);

        // Assert
        verify(servicesDao).seeMyBill(userId);
    }

    @Test
    void testGetUsersOTTPlatforms() throws SQLException {
        // Arrange
        int userId = 1;

        // Act
        servicesService.getUsersOTTPlatforms(userId);

        // Assert
        verify(servicesDao).getUsersOTTPlatforms(userId);
    }
}
