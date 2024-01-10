package com.testing.cource.service;

import com.testing.cource.AbstractTestContainer;
import com.testing.cource.exception.DuplicateResourceException;
import com.testing.cource.exception.ResourceNotFoundException;
import com.testing.cource.model.Employee;
import com.testing.cource.repository.EmployeeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {


    @Mock
    //using the repository here is only for the setup and precondition phase
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService underTest;
    private Employee employee;

    @BeforeEach
    void setUp() {
        this.employee = new Employee()
                .setId(1L)
                .setFirstName("esmaeeil")
                .setLastName("enani")
                .setEmail("esmaeeil@gmail.com");
    }


    @Test
    void givenEmployee_whenSave_thenReturnSavedEmp() {
        // given precondition or setup

        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.empty());

        given(employeeRepository.save(employee))
                .willReturn(employee);


        // when - action or the behaviour that we are going test
        Employee employee1 = underTest.saveEmp(employee);


        // then verify the output
        assertThat(employee1).isNotNull();
        assertThat(employee1.getFirstName()).isEqualTo(employee.getFirstName());
    }

    @Test
    void givenExistedEmail_whenSaveNewEmp_thenReturnException() {
        // given precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.of(employee));

        // when - action or the behaviour that we are going test
        Assertions.assertThrows(DuplicateResourceException.class, () -> {
            underTest.saveEmp(employee);
        });

        // then verify the output
        //this line used to make sure the save method is never called in case of error
        verify(employeeRepository, never()).save(any(Employee.class));
    }


    @Test
    void givenListOfEmployees_whenRetrieve_thenReturnNonEmptyList() {
        // given precondition or setup

        given(employeeRepository.findAll())
                .willReturn(List.of(
                        new Employee()
                                .setFirstName("esmaeeil")
                                .setLastName("enani")
                                .setEmail("esmaeeil@gmail.com"),
                        new Employee()
                                .setFirstName("esmaeeil2")
                                .setLastName("enani2")
                                .setEmail("esmaeeil2@gmail.com")

                ));


        // when - action or the behaviour that we are going test
        List<Employee> employees = underTest.findAllEmployees();


        // then verify the output
        assertThat(employees).isNotEmpty();
        assertThat(employees.size()).isEqualTo(2);

    }


    @Test
    void givenEmployeeId_whenFindById_thenReturnEmployee() {
        // given precondition or setup
        given(employeeRepository.findById(employee.getId())).willReturn(Optional.of(employee));

        // when - action or the behaviour that we are going test
        Employee bdEmp = underTest.findById(employee.getId());

        // then verify the output
        assertThat(bdEmp).isEqualTo(employee);

    }

    @Test
    void givenEmployeeId_whenFindById_thenReturnError() {
        // given precondition or setup
        given(employeeRepository.findById(employee.getId())).willReturn(Optional.empty());
        // when - action or the behaviour that we are going test
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            underTest.findById(1L);
        });

        // then verify the output


    }
}