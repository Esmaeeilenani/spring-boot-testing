package com.testing.cource.repository;

import com.testing.cource.AbstractTestContainer;
import com.testing.cource.model.Employee;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EmployeeRepositoryTest extends AbstractTestContainer {

    @Autowired
    private EmployeeRepository employeeRepository;


    @BeforeEach
    public void deleteAllEmployees() {
        employeeRepository.deleteAll();
    }

    @Test
    void givenEmployee_whenSave_thenReturnResult() {
        // Given
        Employee employee = new Employee()
                .setFirstName("esmaeeil")
                .setLastName("enani")
                .setEmail("esmaeeil@gmail.com");

        // When
        Employee savedEmp = employeeRepository.save(employee);

        // Then
        assertThat(savedEmp).isNotNull();
        assertThat(savedEmp.getId()).isGreaterThan(0);


    }


    @Test
    void givenListOfEmployees_whenRetrieve_thenReturnNotEmptyList() {
        // Given
        List<Employee> employees = List.of(
                new Employee()
                        .setFirstName("esmaeeil")
                        .setLastName("enani")
                        .setEmail("esmaeeil@gmail.com"),
                new Employee()
                        .setFirstName("esmaeeil2")
                        .setLastName("enani2")
                        .setEmail("esmaeeil2@gmail.com")

        );
        employeeRepository.saveAll(employees);

        // When
        List<Employee> savedEmployees = employeeRepository.findAll();


        // Then
        assertThat(savedEmployees).isNotEmpty();
        assertThat(savedEmployees.size()).isEqualTo(2);
    }


    @Test
    void givenEmployee_whenGetEmployeeById_thenReturnEmployeeObj() {
        // given precondition or setup

        Employee employee = new Employee()
                .setFirstName("esmaeeil")
                .setLastName("enani")
                .setEmail("esmaeeil@gmail.com");


        Employee savedEmp = employeeRepository.save(employee);

        // when - action or the behaviour that we are going test
        Employee dbEmp = employeeRepository
                .findById(savedEmp.getId())
                .orElse(null);

        // then verify the output
        assertThat(dbEmp).isNotNull();

    }

    @Test
    void givenEmployee_whenRetrieveByEmail_thenReturnEmployee() {
        // given precondition or setup
        Employee employee = new Employee()
                .setFirstName("esmaeeil")
                .setLastName("enani")
                .setEmail("esmaeeil@gmail.com");
        employeeRepository.save(employee);


        // when - action or the behaviour that we are going test
        Employee dbEmp = employeeRepository
                .findByEmail(employee.getEmail())
                .orElse(null);


        // then verify the output
        assertThat(dbEmp).isNotNull();

    }


    @Test
    void givenEmployee_whenUpdateEmployeeData_thenReturnEmployeeWithUpdatedData() {
        // given precondition or setup
        Employee employee = new Employee()
                .setFirstName("esmaeeil")
                .setLastName("enani")
                .setEmail("esmaeeil@gmail.com");
        employeeRepository.save(employee);

        // when - action or the behaviour that we are going test
        String newFirstName = "esma";
        employee.setFirstName(newFirstName);
        employeeRepository.save(employee);
        Employee bdEmp = employeeRepository.findById(employee.getId())
                .orElse(null);

        // then verify the output
        assertThat(bdEmp).isNotNull();
        assertThat(bdEmp.getFirstName()).isEqualTo(newFirstName);

    }


    @Test
    void givenNewEmployee_whenDelete_thenRemoveEmployee() {
        // given precondition or setup
        Employee employee = new Employee()
                .setFirstName("esmaeeil")
                .setLastName("enani")
                .setEmail("esmaeeil@gmail.com");
        employeeRepository.save(employee);

        // when - action or the behaviour that we are going test
        employeeRepository.deleteById(employee.getId());
        Employee deleted = employeeRepository.findById(employee.getId())
                .orElse(null);

        // then verify the output
        assertThat(deleted).isNull();

    }


    //testing JPQL with Index
    @Test
    void givenEmployee_whenFindByJPQLIndexParams_thenReturnEmployee() {
        // given precondition or setup
        Employee employee = new Employee()
                .setFirstName("esmaeeil")
                .setLastName("enani")
                .setEmail("esmaeeil@gmail.com");
        employeeRepository.save(employee);

        // when - action or the behaviour that we are going test
        Optional<Employee> employeeOptional = employeeRepository
                .findByFirstNameAndLastName(employee.getFirstName(), employee.getLastName());

        // then verify the output
        assertThat(employeeOptional).isNotEmpty();

    }

    //testing JPQL with Named Params
    @Test
    void givenEmployee_whenFindByJPQLNamedParams_thenReturnEmployee() {
        // given precondition or setup
        Employee employee = new Employee()
                .setFirstName("esmaeeil")
                .setLastName("enani")
                .setEmail("esmaeeil@gmail.com");
        employeeRepository.save(employee);

        // when - action or the behaviour that we are going test
        Optional<Employee> employeeOptional = employeeRepository
                .findByFirstNameAndLastNameNamedParams(employee.getFirstName(), employee.getLastName());

        // then verify the output
        assertThat(employeeOptional).isNotEmpty();

    }
}
