package cn.bookstore.test;

import static org.junit.Assert.*;

import org.junit.Test;

import cn.bookstore.entity.User;
import cn.bookstore.service.UserService;
import cn.bookstore.serviceImpl.UserServiceImpl;

public class TestService {
	UserService userService = new UserServiceImpl();
	@Test
	public void testLogin() {
		User user = new User(null, "zhangsan", "123456", null);
		User login = userService.login(user);
		System.out.println(login.toString());
	}

	@Test
	public void testRegist() {
		User user = new User(33, "qwe", "123", "wioji");
		boolean regist = userService.regist(user);
		System.out.println(regist);
	}

}
