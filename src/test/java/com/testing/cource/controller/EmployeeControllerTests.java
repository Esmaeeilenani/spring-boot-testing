package com.testing.cource.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.testing.cource.AbstractTestContainer;
import com.testing.cource.exception.ResourceNotFoundException;
import com.testing.cource.model.Employee;
import com.testing.cource.service.EmployeeService;

import static org.hamcrest.CoreMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.BDDMockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@WebMvcTest
@ExtendWith(MockitoExtension.class)
class EmployeeControllerTests extends AbstractTestContainer {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;

    private static final String API_URL = "/api/v1/employees";

    @BeforeEach
    void setUp() {
        this.employee = new Employee()
                .setId(1L)
                .setFirstName("esmaeeil")
                .setLastName("enani")
                .setEmail("esmaeeil@gmail.com");
    }

    @Test
    void givenEmployee_whenSaveEmployee_thenReturnSavedEmployee() throws Exception {
        // given precondition or setup
        given(employeeService.saveEmp(any(Employee.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(post(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        // then verify the output
        response
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));


    }

    @Test
    void givenEmployeeList_whenDoRestRequest_thenReturnListOfEmployee() throws Exception {
        // given precondition or setup
        Employee employee1 = new Employee()
                .setId(2L)
                .setFirstName("esmaeeil2")
                .setLastName("enani2")
                .setEmail("esmaeeil2@email.com");

        List<Employee> employees = List.of(employee, employee1);
        given(employeeService.getAll()).willReturn(employees);


        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get(API_URL));


        // then verify the output
        response
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(employees.size())));


    }


    @Test
    void givenEmployeeId_whenFindById_thenReturnEmployeeObj() throws Exception {
        // given precondition or setup
        given(employeeService.findById(employee.getId())).willReturn(employee);

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get(API_URL + "/{id}", employee.getId()));


        // then verify the output
        response
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));

    }

    @Test
    void givenEmployeeId_whenFindById_thenReturnError() throws Exception {
        // given precondition or setup
        given(employeeService.findById(1L)).willThrow(ResourceNotFoundException.class);


        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get(API_URL + "/{id}", employee.getId()));


        // then verify the output
        response
                .andDo(print())
                .andExpect(status().isNotFound());


    }

    @Test
    void givenEmployeeIdAndUpdatedInfo_whenUpdateEmp_thenUpdatedEmp() throws Exception {
        // given precondition or setup
        Employee updateEmployee = new Employee()
                .setFirstName("esmaeeil2")
                .setLastName("enani2")
                .setEmail("esmaeeil2@gmail.com");

        given(employeeService.findById(1L)).willReturn(employee);

        given(employeeService.saveEmp(any(Employee.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        updateEmployee.setId(1L);
        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put(API_URL + "/{id}", employee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateEmployee)));



        // then verify the output
        response
                .andDo(print())
                .andExpect(status().isOk());


    }

}