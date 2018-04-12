package com.gate.barcode.check.gatepass.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gate.barcode.check.gatepass.model.Station;

/**
 * <<This is the repository to handle station operations>>
 * @author Munal
 * @version 1.0.0
 * @since , 12 Apr 2018
 */
@Repository
public interface StationRepository extends JpaRepository<Station, Long> {

}
