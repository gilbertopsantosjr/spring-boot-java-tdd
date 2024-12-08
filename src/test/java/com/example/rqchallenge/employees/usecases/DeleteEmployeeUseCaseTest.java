package com.example.rqchallenge.employees.usecases;

import com.example.rqchallenge.employees.application.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class DeleteEmployeeUseCaseTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    public void testDeleteEmployeeById_Success() {
        // Simulating successful deletion
        boolean result = employeeService.deleteEmployeeById("1");

        assertTrue(result);
    }

    @Test
    public void testDeleteEmployeeById_NotFound() {
        // Simulating a 404 Not Found error
        doThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND))
                .when(restTemplate).delete("null/delete/1");

        boolean result = employeeService.deleteEmployeeById("1");

        assertFalse(result);
    }

    @Test
    public void testDeleteEmployeeById_InternalServerError() {
        // Simulating a 500 Internal Server Error
        doThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR))
                .when(restTemplate).delete("null/delete/1");

        Exception exception = assertThrows(HttpClientErrorException.class, () -> {
            employeeService.deleteEmployeeById("1");
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ((HttpClientErrorException) exception).getStatusCode());
    }

}
