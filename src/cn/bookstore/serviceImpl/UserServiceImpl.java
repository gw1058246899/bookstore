package cn.bookstore.serviceImpl;

import cn.bookstore.dao.UserDao;
import cn.bookstore.daoImpl.UserDaoImpl;
import cn.bookstore.entity.User;
import cn.bookstore.service.UserService;

public class UserServiceImpl implements UserService{
	UserDao dao = new UserDaoImpl();
	@Override
	public User login(User user) {
		// TODO Auto-generated method stub
		
		return dao.getUseByUsernameAndPassword(user);
	}

	@Override
	public boolean regist(User user) {
		// TODO Auto-generated method stub
		int count = 0;
		int saveUser = dao.saveUser(user);
		return saveUser!=0;
	}

}
