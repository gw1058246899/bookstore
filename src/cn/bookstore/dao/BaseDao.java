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
		// ��ȡ��ǰ��������ĸ���
		// this.getClass(),��ȡ��User
		ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
		// ��ȡ������ķ�������
		Type[] types = pt.getActualTypeArguments();
		/*
		 * ���ڷ��ص���һ�����飬��������д�ķ�����ֻ��һ�����ͣ���������ʹ���±� ����ʽȡ����Ψһ��һ��ֵ this.type
		 * ����22�е��Ǹ�type
		 */
		this.type = (Class<T>) types[0];
	}
	/*
	 * ���ݿ����ɾ��
	 */
	/*
	 * public void update(String sql,Object...args){ Connection conn = null;
	 * PreparedStatement prep = null; try { //1.�������ݿ� conn = JDBCUtil.getConn();
	 * //2.����sql���(�˴�ʡ��) //3.Ԥ���� prep = conn.prepareStatement(sql); //4.���ռλ��
	 * for(int i=0;i<args.length;i++){ prep.setObject(i+1, args[i]); } //5.ִ��
	 * prep.executeUpdate(); } catch (Exception e) { e.printStackTrace(); }
	 * finally{ JDBCUtil.releaseConn(null, prep, conn); }
	 * 
	 * }
	 */

	/*
	 * ͨ�õ���ɾ��
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
	 * ��ѯ����
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
	 * ��ѯ����
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
	 * ���ݿ��ѯ����
	 */
	/*
	 * public <T>T getBean(Class<T> clazz,String sql,Object...args){ Connection
	 * conn = null; PreparedStatement prep = null; ResultSet rs = null; try {
	 * //1.�������ݿ� conn = JDBCUtil.getConn(); //2.����sql���(�˴�ʡ��) //3.Ԥ���� prep =
	 * conn.prepareStatement(sql); //4.���ռλ�� for(int i=0;i<args.length;i++){
	 * prep.setObject(i+1, args[i]); } //5.ִ�� rs = prep.executeQuery(); //��ȡԪ����
	 * ResultSetMetaData rsmd = rs.getMetaData(); //��ȡ������� int columnCount =
	 * rsmd.getColumnCount(); T t = clazz.newInstance(); if(rs.next()){ for(int
	 * i=0;i<columnCount;i++){ //��ȡ�����е�ֵ Object value = rs.getObject(i+1);
	 * //��ȡ�еı��� String columnLabel = rsmd.getColumnLabel(i+1); //ͨ���������������ֵ
	 * Field field = clazz.getDeclaredField(columnLabel); //Ϊ������Ȩ��
	 * field.setAccessible(true); field.set(t, value); } } return t; } catch
	 * (Exception e) { e.printStackTrace(); } finally{ JDBCUtil.releaseConn(rs,
	 * prep, conn); } return null;
	 * 
	 * }
	 */

	/*
	 * ���ݿ��ѯ����
	 */
	public <T> List<T> getBeanList(Class<T> clazz, String sql, Object... args) {
		Connection conn = null;
		PreparedStatement prep = null;
		ResultSet rs = null;
		List<T> list = new ArrayList<T>();
		try {
			// 1.�������ݿ�
			conn = JDBCUtil.getConn();
			// 2.����sql���(�˴�ʡ��)
			// 3.Ԥ����
			prep = conn.prepareStatement(sql);
			// 4.���ռλ��
			for (int i = 0; i < args.length; i++) {
				prep.setObject(i + 1, args[i]);
			}
			// 5.ִ��
			rs = prep.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			while (rs.next()) {
				// ʵ����һ������
				T t = clazz.newInstance();
				for (int i = 0; i < columnCount; i++) {
					// 1.��ȡ�����е�ֵ
					Object value = rs.getObject(i + 1);
					// 2.��ȡ�еı���
					String columnLabel = rsmd.getColumnLabel(i + 1);
					// 3.ͨ���������������ֵ
					Field field = clazz.getDeclaredField(columnLabel);
					// 4.��������Ȩ��
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
