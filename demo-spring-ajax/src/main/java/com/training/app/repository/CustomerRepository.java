package com.training.app.repository;

import com.training.app.entity.Customers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customers, Long> {

    @Query("select e from  Customers e where e.customerid = :id")
    Customers findByCustomerId(@Param("id") Long id);

    @Query("select e.customername from  Customers e where e.customername = :name")
    Customers findByCustomerName(@Param("name") String name);

    @Query("select e from  Customers e where e.customername = :name and e.customerid = :id")
    Customers findByCustomerNameAndBrandIdNot(@Param("name")String name,@Param("id") Long id);

    @Query("select e from Customers e where concat(e.customername,'',e.age,'') like :customername")
    List<Customers> findByCustomerNameLike(@Param("customername") String customername);

}
