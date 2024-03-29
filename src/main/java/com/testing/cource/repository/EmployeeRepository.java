package com.testing.cource.repository;

import com.testing.cource.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String name);

    @Query("select e from Employee e where e.firstName= ?1 and e.lastName = ?2")
    Optional<Employee> findByFirstNameAndLastName(String firstName,String lastName);


    @Query("select e from Employee e where e.firstName= :firstName and e.lastName = :lastName")
    Optional<Employee> findByFirstNameAndLastNameNamedParams(@Param("firstName") String firstName,
                                                             @Param("lastName") String lastName);
}
