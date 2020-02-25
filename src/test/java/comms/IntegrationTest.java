package comms;

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import org.junit.Test;

public class IntegrationTest {

	private final String TO_SEND = "This is the text to be sent.";

	@Test
	public void test() {
		try {
			InetAddress ipAddress = InetAddress.getLocalHost();
			Socket socket = new Socket(ipAddress, 4848);
			String returned = "";
			if (!socket.equals(null)) {
				System.out.println("Client is connected!");
				send(TO_SEND, socket);
				returned = receive(socket);
				System.out.println("Received '" + returned + "' from the server.");
			}
		} catch (Exception e) {
			System.err.println("Failed to open socket to server.");
		}
		assertEquals(0, 1);;
	}

	public String receive(Socket socket) {
		try {
			DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			String dataString = in.readUTF();
			return dataString;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void send(String dataString, Socket socket) {
		OutputStream outputStream = null;
		try {
			outputStream = socket.getOutputStream();
			DataOutputStream out = new DataOutputStream(outputStream);
			System.out.println("Sending messages to the server");
			out.writeUTF(dataString);
			System.out.println("Closing socket and terminating program.");
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
