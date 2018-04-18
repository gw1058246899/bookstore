package cn.bookstore.service;

import cn.bookstore.entity.User;

public interface UserService {
	User login(User user);
	boolean regist(User user);
}
