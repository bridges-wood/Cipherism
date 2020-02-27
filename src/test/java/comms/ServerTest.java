package comms;

import cipher.Manager;

public class ServerTest {
	public static void main(String[] args) {
		String text = "Vygh ba i vvqi ba i vvqi ba i vvqi ba i vvqi ba i vvqi ba i vvqi ba i vvqi ba i vvqi ba i vvqi ba i vvqi ba i vvqi".toLowerCase();
		Manager m = new Manager(text, false);
		System.out.println(m.getResult());
	}
}
