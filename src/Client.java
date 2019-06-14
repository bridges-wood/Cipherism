import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;

public class Client {

	public static void main(String[] args) throws IOException {
		// We need a host and port, we want to connect to the ServerSocket at port 8000
		Socket socket = null;
		try {
			socket = new Socket("localhost", 8000);
		} catch (ConnectException e) {
			System.out.println("Could not connect to the socket.");
		}
		if (!socket.equals(null)) {
			System.out.println("Client is connected!");
			send("test", socket);
			String returned = receive(socket);
			System.out.println("Received '" + returned + "' from the server.");

			System.out.println("Closing socket and terminating program.");
			socket.close();
		}
	}

	public static String receive(Socket socket) {
		try {
			DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			String dataString = in.readUTF();
			return dataString;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void send(String dataString, Socket socket) throws IOException {
		OutputStream outputStream = socket.getOutputStream();
		DataOutputStream out = new DataOutputStream(outputStream);
		System.out.println("Sending messages to the client");
		out.writeUTF(dataString);
		System.out.println("Closing socket and terminating program.");
		out.flush();
	}
}
