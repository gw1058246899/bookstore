package cn.bookstore.daoImpl;

import org.junit.Test;

import cn.bookstore.entity.User;

public class TestSql {
	UserDaoImpl dao = new UserDaoImpl();
	@Test
	public void testGetUseByUsernameAndPassword() {
		User user = new User(null, "zhangsan", "123456", null);
		User user1 = dao.getUseByUsernameAndPassword(user);
		System.out.println(user1.toString());
	}

	@Test
	public void testSaveUser() {
		User user = new User(22, "lisi", "123456", "lisi@qq.com");
		int saveUser = dao.saveUser(user);
		System.out.println(saveUser);
	}

}
