package com.lzdn.note.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lzdn.note.entity.User;
import com.lzdn.note.repository.UserRepository;
import com.lzdn.note.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public List<User> findUserByName(String userName) {
		return userRepository.findUserByName(userName);
	}

	@Override
	@Transactional
	public void updateUser(User user) {
		userRepository.updateUser(user);
	}

}
