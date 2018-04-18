package cn.bookstore.test;

import java.sql.Connection;

import org.junit.Test;

import cn.bookstore.utils.JDBCUtil;

public class ConnTest {
	@Test
	public void testConn(){
		Connection conn = null;
		conn = JDBCUtil.getConn();
		System.out.println(conn);
		
	}
}
