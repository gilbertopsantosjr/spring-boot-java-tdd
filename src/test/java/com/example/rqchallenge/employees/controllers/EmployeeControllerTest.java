package com.example.rqchallenge.employees.controllers;

import com.example.rqchallenge.employees.application.EmployeeService;
import com.example.rqchallenge.employees.controller.EmployeeController;
import com.example.rqchallenge.employees.model.Employee;
import com.example.rqchallenge.employees.usecases.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    public static final String API_V_1_EMPLOYEES = "/api/v1/employees";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private EmployeeService employeeService;

    @MockBean
    private GetAllEmployeesUseCase getAllEmployeesUseCase;

    @MockBean
    private CreateEmployeeUseCase createEmployeeUseCase;

    @MockBean
    private GetHighestSalaryEmployeeUseCase getHighestSalaryEmployeeUseCase;

    @MockBean
    private GetEmployeeByIdUseCase getEmployeeByIdUseCase;

    @MockBean
    private GetEmployeeByNameUseCase getEmployeeByNameUseCase;

    @MockBean
    private GetTop10HighestEarningEmployeeNames getTop10HighestEarningEmployeeNames;

    @MockBean
    private DeleteEmployeeUseCase deleteEmployeeUseCase;


    @Test
    public void testGetAllEmployees() throws Exception {
        Employee employee = new Employee();
        employee.setId(1);
        employee.setName("John Doe");

        when(getAllEmployeesUseCase.execute()).thenReturn(Arrays.asList(employee));

        mockMvc.perform(get(API_V_1_EMPLOYEES))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].employee_name").value("John Doe"));
    }

    @Test
    public void testCreateEmployee() throws Exception {
        Employee employee = new Employee(1, "John Doe", 50000.0d, 30, "");
        when(createEmployeeUseCase.execute(any())).thenReturn(employee);

        mockMvc.perform(post(API_V_1_EMPLOYEES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employee_name", is("John Doe")))
                .andExpect(jsonPath("$.employee_salary", is(50000.0)));
    }

    @Test
    public void testCreateEmployee_badRequest() throws Exception {
        Employee employee = new Employee(1, null, 0, 0, "");
        when(createEmployeeUseCase.execute(any())).thenReturn(employee);

        mockMvc.perform(post(API_V_1_EMPLOYEES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetHighestSalaryEmployee_found() throws Exception {
        Employee employee = new Employee(1, "John Doe", 90000d, 30, "");
        when(getHighestSalaryEmployeeUseCase.execute()).thenReturn(Optional.of(employee));

        mockMvc.perform(get(API_V_1_EMPLOYEES + "/highestSalary")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("90000.0"));
    }

    @Test
    public void testGetHighestSalaryEmployee_notFound() throws Exception {
        when(getHighestSalaryEmployeeUseCase.execute()).thenReturn(Optional.empty());

        mockMvc.perform(get(API_V_1_EMPLOYEES  + "/highestSalary")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetEmployeesById_found() throws Exception {
        Employee employee = new Employee(1, "John Doe", 50000, 30, "");
        when(getEmployeeByIdUseCase.execute("1")).thenReturn(Optional.of(employee));

        mockMvc.perform(get(API_V_1_EMPLOYEES + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employee_name", is("John Doe")))
                .andExpect(jsonPath("$.employee_salary", is(50000.0)));
    }

    @Test
    public void testGetEmployeesById_notFound() throws Exception {
        when(getEmployeeByIdUseCase.execute("Unknown")).thenReturn(Optional.empty());

        mockMvc.perform(get(API_V_1_EMPLOYEES + "Unknown")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetEmployeesByName_notFound() throws Exception {
        when(getEmployeeByNameUseCase.execute("Unknown")).thenReturn(List.of());

        mockMvc.perform(get(API_V_1_EMPLOYEES + "/search/Unknown")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetEmployeesByName_found() throws Exception {
        Employee employee = new Employee(1, "John Doe", 50000, 30, "");
        when(getEmployeeByNameUseCase.execute("John")).thenReturn(List.of(employee));

        mockMvc.perform(get(API_V_1_EMPLOYEES + "/search/John")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].employee_name", is("John Doe")))
                .andExpect(jsonPath("$[0].employee_salary", is(50000.0)));
    }

    @Test
    public void testGetTop10HighestEarningEmployeeNames() throws Exception {
        List<String> top10Employees = List.of("John Doe", "Jane Smith", "Emily Davis", "Michael Brown", "David Wilson",
                "Robert Johnson", "James Taylor", "Mary Anderson", "William Harris", "Thomas Clark");
        when(getTop10HighestEarningEmployeeNames.execute()).thenReturn(top10Employees);

        mockMvc.perform(get(API_V_1_EMPLOYEES + "/topTenHighestEarningEmployeeNames")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(10)))
                .andExpect(jsonPath("$[0]", is("John Doe")))
                .andExpect(jsonPath("$[9]", is("Thomas Clark")));
    }

    @Test
    public void testDeleteEmployee_Success() throws Exception {
        when(deleteEmployeeUseCase.execute("1")).thenReturn(true);

        mockMvc.perform(delete(API_V_1_EMPLOYEES + "/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteEmployee_NotFound() throws Exception {
        when(deleteEmployeeUseCase.execute("1")).thenReturn(false);

        mockMvc.perform(delete(API_V_1_EMPLOYEES + "/1"))
                .andExpect(status().isNotFound());
    }

}
