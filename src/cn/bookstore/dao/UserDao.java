package cn.bookstore.dao;

import cn.bookstore.entity.User;

public interface UserDao {
	User getUseByUsernameAndPassword(User user);
	int saveUser(User user);
	
}
