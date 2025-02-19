package com.main.project.repo;






import org.springframework.data.jpa.repository.JpaRepository;

import com.main.project.entities.User;


public interface UserRepository extends JpaRepository<User, Integer> {
	
//	@Query( = true, value =  "SELECT * FROM User ORDER BY Id")
//	List<User> findUserById(@Param("id")int id);
}
