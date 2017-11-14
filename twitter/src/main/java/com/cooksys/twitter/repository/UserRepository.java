package com.cooksys.twitter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cooksys.twitter.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	public User findby_username(String username);

	public List<User> findByStatusTrue(); // list of all active users
	
	public User findByUsernameAndStatusFalse(String username); // find "deleted" user
	
	public User findByUsernameAndStatusTrue(String username); // find active user

	
}
