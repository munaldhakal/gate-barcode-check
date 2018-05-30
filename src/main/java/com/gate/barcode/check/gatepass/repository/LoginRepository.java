package com.gate.barcode.check.gatepass.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gate.barcode.check.gatepass.model.Login;
@Repository
public interface LoginRepository extends JpaRepository<Login, Long> {
	/**
	 * This method returns user according to the username
	 * @param username
	 * @return Login
	 * @author Munal
	 * @since 11/04/2018
	 */
	Login findByUsername(String username);
	/**
	 * This method returns login with respect to username and password
	 * @param username
	 * @param password
	 * @return Login
	 * @author Munal
	 * @param string 
	 * @since 11/04/2018
	 */
	Login findByUsernameAndPassword(String username, String password);
	/**
	 *<<Add description here>>
	 * @param userId
	 * @return
	 * @author
	 * @since , Modified In: @version, By @author
	 */
	Login findByUserId(Long userId);

}
