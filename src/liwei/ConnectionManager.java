package liwei;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionManager {

	private static int initSizeInt;
	private static int maxSizeInt;
	private static int currentTotalConnCount = 0;
	private static int currentInuseConnCount = 0;
	private static int connStepInt = 0;
	private static String driverName;
	private static String url;
	private static String userName;
	private static String password;

	private static Lock lock = new ReentrantLock();
	private static ThreadLocal<Connection> connContainer = new ThreadLocal<Connection>();
	private static List<Connection> connectionList = new ArrayList<Connection>();
	private static String jdbc_conn_pro = "jdbc_connection.properties";

	private static Boolean isInit = false;

	static {
		try {
			createConnection();
			isInit = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static Properties getJdbcConnProperties(String resourceName) throws IOException {
		InputStream inStream = ClassLoaderUtil.getResourceAsStream(resourceName, ConnectionManager.class);
		Properties pro = new Properties();
		if (inStream == null) {
			throw new RuntimeException("properties�ļ���Ϊ��");
		}
		pro.load(inStream);
		return pro;
	}

	// ���������ʱ��ʼ������ conn����
	private static void createConnection() throws IOException, SQLException, ClassNotFoundException {
		Properties pro = getJdbcConnProperties(jdbc_conn_pro);
		driverName = pro.getProperty("driverName");
		url = pro.getProperty("url");
		userName = pro.getProperty("userName");
		password = pro.getProperty("password");
		String initSize = pro.getProperty("initSize");
		String maxSize = pro.getProperty("maxSize");
		String connStep = pro.getProperty("connStep");

		try {
			initSizeInt = Integer.parseInt(initSize);
			maxSizeInt = Integer.parseInt(maxSize);
			connStepInt = Integer.parseInt(connStep);
		} catch (ArithmeticException e) {
			throw new ArithmeticException("��ʼ�������������������Ϊ����");
		}
		Class.forName(driverName);
		for (int i = 0; i < initSizeInt; i++) {
			Connection conn = DriverManager.getConnection(url, userName, password);
			connectionList.add(conn);
			currentTotalConnCount++;
		}
	}

	public static Connection getConnection() throws Exception {
		Connection conn = connContainer.get();
		if (conn == null) {
			conn = getFromConnList();
			connContainer.set(conn);
		}
		return conn;
	}

	private static Connection getFromConnList() throws Exception {
		// ��������ȫ�ֱ���connectionList������
		if (lock.tryLock(5, TimeUnit.SECONDS)) {
			try {
				if (connectionList.size() == 0) {
					if (isInit == false) {
						createConnection();
						isInit = true;
						return getFromConnList();
					}
					if (currentTotalConnCount >= maxSizeInt) {
						throw new Exception("û�п������ӣ����Ժ�");
					}
					if (initSizeInt <= 0) {
						Class.forName(driverName);
					}
					for (int i = 0; (i < connStepInt) && (currentTotalConnCount <= maxSizeInt); i++) {
						Connection conn = DriverManager.getConnection(url, userName, password);
						connectionList.add(conn);
						currentTotalConnCount++;
						System.out.println("total+++++ : " + currentTotalConnCount);
					}
				}
				Connection conn = connectionList.remove(0);
				currentInuseConnCount++;
				System.out.println("inuse+++++ : " + currentInuseConnCount);
				return conn;
			} finally {
				lock.unlock();
			}
		} else {
			throw new RuntimeException("����������ʱ");
		}
	}

	// �����ӷŻص����ӳ�
	public static void close() throws SQLException, InterruptedException {
		// ��������ȫ�ֱ���connectionList������
		if (lock.tryLock(5, TimeUnit.SECONDS)) {
			try {
				Connection conn = connContainer.get();
				if (conn != null) {
					connContainer.remove();
					connectionList.add(conn);
					currentInuseConnCount--;
					System.out.println("inuse----- : " + currentInuseConnCount);
					if (currentInuseConnCount != 0 && currentTotalConnCount / currentInuseConnCount >= 2) {
						// ��������������ʹ�����ӵ�2������2��֮������������٣�ͬʱʣ��������Ҫ��С�ڳ�ʼ������
						for (int i = 0; i <= (currentTotalConnCount - currentInuseConnCount * 2) && currentTotalConnCount > initSizeInt; i++) {
							Connection currentConn = connectionList.remove(0);
							currentTotalConnCount--;
							System.out.println("total----- : " + currentTotalConnCount);
							if (currentConn != null) {
								try {
									currentConn.close();
								} catch (SQLException e) {
									throw new SQLException("�ر����ӳ���");
								}
							}
						}
					}
				}
			} finally {
				lock.unlock();
			}
		} else {
			throw new RuntimeException("����������ʱ");
		}
	}

	// ��������
	public static void beginTransction(Connection conn) {
		if (conn != null) {
			try {
				if (conn.getAutoCommit()) {
					conn.setAutoCommit(false);
				}
			} catch (SQLException e) {
				throw new RuntimeException("�ر����ӵ��Զ��ύ��ʧ��");
			}
		}
	}

	// �ύ����
	public static void endTransction(Connection conn) {
		if (conn != null) {
			try {
				if (!conn.getAutoCommit()) {
					conn.commit();
				}
			} catch (SQLException e) {
				throw new RuntimeException("�ύ���ӵ�����ʧ��");
			}
		}
	}

	// �ָ����Զ��ύ
	public static void recoverTransction(Connection conn) {
		if (conn != null) {
			try {
				if (!conn.getAutoCommit()) {
					conn.setAutoCommit(true);
				}
			} catch (SQLException e) {
				throw new RuntimeException("��ԭ���ӵ��Զ��ύ��ʧ��");
			}
		}
	}

	// �ع�����
	public static void rollback(Connection conn) {
		if (conn != null) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				throw new RuntimeException("�ع�����ʧ��");
			}
		}
	}
}
