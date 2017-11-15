package com.cooksys.twitter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cooksys.twitter.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	 User findby_username(String username);

	 List<User> findByStatusTrue(); // list of all active users
	
	 User findByUsernameAndStatusFalse(String username); // find "deleted" user
	
	 User findByUsernameAndStatusTrue(String username); // find active user
	 
	 User findById(Integer id);
	
}
