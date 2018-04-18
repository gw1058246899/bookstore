package cn.bookstore.daoImpl;

import cn.bookstore.dao.BaseDao;
import cn.bookstore.dao.UserDao;
import cn.bookstore.entity.User;

public class UserDaoImpl extends BaseDao<User> implements UserDao{

	@Override
	public User getUseByUsernameAndPassword(User user) {
		// TODO Auto-generated method stub
		String sql = "select id,username,password,email from user where username=? and password=?";
		
		return this.queryOne(sql,user.getUsername(),user.getPassword());
	}

	@Override
	public int saveUser(User user) {
		// TODO Auto-generated method stub
		String sql = "insert into user values(?,?,?,?)";
		return this.update(sql,user.getId(), user.getUsername(),user.getPassword(),user.getEmail());
	}

}
