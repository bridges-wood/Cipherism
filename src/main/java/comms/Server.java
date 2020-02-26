package comms;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cipher.Manager;
public class Server {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("src/main/Beans.xml");
		Manager manager = (Manager) context.getBean("Manager");
		manager.
	}
}
