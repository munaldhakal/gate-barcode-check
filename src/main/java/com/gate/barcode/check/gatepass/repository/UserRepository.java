package com.gate.barcode.check.gatepass.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gate.barcode.check.gatepass.model.User;
import com.gate.barcode.check.gatepass.utilities.UserType;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	//User findOne(Long userId);

	User findAllById(Long userId);

	/**
	 *<<Returns list of user by userType>>
	 * @param userType
	 * @return List<User>
	 * @author Munal
	 * @since 17/04/2018, Modified In: @version, By @author
	 */
	List<User> findAllByUserType(UserType userType);

	

}
