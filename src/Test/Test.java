package Test;

import liwei.ConnectionManager;

import org.apache.log4j.Logger;

public class Test extends Thread {

	public static Logger log = Logger.getLogger(Test.class);
	static int money;
	static boolean ready;

	public static void main(String[] args) throws Exception {
		ready = true;
		new Test().start();
		Thread.sleep(1000);
		money = 4;

	}

	public void run() {

		while (!ready) {
			Thread.yield();
		}
		System.out.println(money);

	}
}

class ConnTest implements Runnable {

	public void run() {
		try {
			ConnectionManager.getConnection();
			ConnectionManager.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

class ConnTest2 implements Runnable {

	public void run() {
		try {
			ConnectionManager.getConnection();
			ConnectionManager.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}