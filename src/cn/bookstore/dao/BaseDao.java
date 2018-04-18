package cn.bookstore.dao;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import cn.bookstore.utils.JDBCUtil;

public class BaseDao<T> {
	QueryRunner runn = new QueryRunner();
	public Class<T> type;

	public BaseDao() {
		// User extends BaseDao<User>
		// 获取当前带泛型类的父类
		// this.getClass(),获取到User
		ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
		// 获取到具体的泛型类型
		Type[] types = pt.getActualTypeArguments();
		/*
		 * 由于返回的是一个数组，但是我们写的泛型中只有一个类型，所以我们使用下标 的形式取出这唯一的一个值 this.type
		 * 就是22行的那个type
		 */
		this.type = (Class<T>) types[0];
	}
	/*
	 * 数据库的增删改
	 */
	/*
	 * public void update(String sql,Object...args){ Connection conn = null;
	 * PreparedStatement prep = null; try { //1.连接数据库 conn = JDBCUtil.getConn();
	 * //2.创建sql语句(此处省略) //3.预编译 prep = conn.prepareStatement(sql); //4.填充占位符
	 * for(int i=0;i<args.length;i++){ prep.setObject(i+1, args[i]); } //5.执行
	 * prep.executeUpdate(); } catch (Exception e) { e.printStackTrace(); }
	 * finally{ JDBCUtil.releaseConn(null, prep, conn); }
	 * 
	 * }
	 */

	/*
	 * 通用的增删改
	 */
	public int update(String sql, Object... args) {
		int count = 0;
		Connection conn = JDBCUtil.getConn();

		try {
			count = runn.update(conn, sql, args);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			JDBCUtil.releaseConn(null, null, conn);
		}
		return count;
	}

	/*
	 * 查询单行
	 */
	public T queryOne(String sql, Object... args) {
		T t = null;
		Connection conn = JDBCUtil.getConn();
		try {

			t = runn.query(conn, sql, new BeanHandler<T>(type), args);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.releaseConn(null, null, conn);
		}
		return t;
	}

	/*
	 * 查询多行
	 */
	public List<T> queryMore(String sql, Object... args) {
		List<T> list = null;
		Connection conn = JDBCUtil.getConn();
		try {
			list = runn.query(conn, sql, new BeanListHandler<T>(type), args);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCUtil.releaseConn(null, null, conn);
		}
		return list;
	}

	/*
	 * 数据库查询单行
	 */
	/*
	 * public <T>T getBean(Class<T> clazz,String sql,Object...args){ Connection
	 * conn = null; PreparedStatement prep = null; ResultSet rs = null; try {
	 * //1.连接数据库 conn = JDBCUtil.getConn(); //2.创建sql语句(此处省略) //3.预编译 prep =
	 * conn.prepareStatement(sql); //4.填充占位符 for(int i=0;i<args.length;i++){
	 * prep.setObject(i+1, args[i]); } //5.执行 rs = prep.executeQuery(); //获取元数据
	 * ResultSetMetaData rsmd = rs.getMetaData(); //获取具体的列 int columnCount =
	 * rsmd.getColumnCount(); T t = clazz.newInstance(); if(rs.next()){ for(int
	 * i=0;i<columnCount;i++){ //获取具体列的值 Object value = rs.getObject(i+1);
	 * //获取列的别名 String columnLabel = rsmd.getColumnLabel(i+1); //通过反射向对象设置值
	 * Field field = clazz.getDeclaredField(columnLabel); //为其设置权限
	 * field.setAccessible(true); field.set(t, value); } } return t; } catch
	 * (Exception e) { e.printStackTrace(); } finally{ JDBCUtil.releaseConn(rs,
	 * prep, conn); } return null;
	 * 
	 * }
	 */

	/*
	 * 数据库查询多行
	 */
	public <T> List<T> getBeanList(Class<T> clazz, String sql, Object... args) {
		Connection conn = null;
		PreparedStatement prep = null;
		ResultSet rs = null;
		List<T> list = new ArrayList<T>();
		try {
			// 1.连接数据库
			conn = JDBCUtil.getConn();
			// 2.创建sql语句(此处省略)
			// 3.预编译
			prep = conn.prepareStatement(sql);
			// 4.填充占位符
			for (int i = 0; i < args.length; i++) {
				prep.setObject(i + 1, args[i]);
			}
			// 5.执行
			rs = prep.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			while (rs.next()) {
				// 实例化一个对象
				T t = clazz.newInstance();
				for (int i = 0; i < columnCount; i++) {
					// 1.获取具体列的值
					Object value = rs.getObject(i + 1);
					// 2.获取列的别名
					String columnLabel = rsmd.getColumnLabel(i + 1);
					// 3.通过反射向对象设置值
					Field field = clazz.getDeclaredField(columnLabel);
					// 4.给其设置权限
					field.setAccessible(true);
					field.set(t, value);
				}
				list.add(t);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.releaseConn(rs, prep, conn);
		}
		return null;
	}
}
