package com.gate.barcode.check.gatepass.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gate.barcode.check.gatepass.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
