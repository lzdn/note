package com.lzdn.note.service;

import java.util.List;

import com.lzdn.note.entity.User;

public interface UserService {

	public List<User> findUserByName(String userName);
	
	public void updateUser(User user);
}
