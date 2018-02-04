package com.lzdn.note.repository;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.lzdn.note.entity.User;

@RepositoryDefinition(domainClass = User.class, idClass = Integer.class)
public interface UserRepository {

	public List<User> findUserByName(String userName);
	
	public void updateUser(User user);
}
