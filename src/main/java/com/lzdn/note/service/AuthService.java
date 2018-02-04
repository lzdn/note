package com.lzdn.note.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.lzdn.note.entity.Role;
import com.lzdn.note.repository.UserRepository;

public class AuthService implements UserDetailsService {

	@Resource
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		List<com.lzdn.note.entity.User> users = userRepository.findUserByName(username);
		if (CollectionUtils.isEmpty(users))
			throw new BadCredentialsException(username + " not found");
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		com.lzdn.note.entity.User loginUser = users.get(0);
		Set<Role> roles = loginUser.getRoles();
		if (!CollectionUtils.isEmpty(roles)) {
			StringBuffer role_name = new StringBuffer();
			for (Role role : roles) {
				role_name.append(role.getRoleName() + "|");
			}
			String roleName = role_name.toString();
			if (!StringUtils.isEmpty(roleName)) {
				authorities.add(new SimpleGrantedAuthority(roleName));
			}
		}

		return new User(username, loginUser.getPassword(), authorities);
	}
}
