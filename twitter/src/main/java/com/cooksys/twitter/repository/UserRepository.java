package com.cooksys.twitter.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cooksys.twitter.entity.User;
import com.cooksys.twitter.entity.Tweet;

public interface UserRepository extends JpaRepository<User, Integer> {

	Set<User> findByDeleted(boolean deleted);
	

	User findByUserName(String userName);

	User findByIdAndFollowing(Integer id, User followedUser);


	Set<User> findByFollowersAndDeleted(User usr, boolean status);

	Set<User> findByFollowingAndDeleted(User usr, boolean status);

	

	Set<User> findByLikesAndDeleted(Tweet tweet, boolean status);

	List<User> findByMentionsAndDeleted(Tweet tweet, boolean status);

}
