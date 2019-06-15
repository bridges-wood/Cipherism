import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	public static InetAddress server;

	public static void main(String[] args) throws IOException {
		// We need a host and port, we want to connect to the ServerSocket at port 8000
		Socket socket = null;
		initServer();
		try {
			socket = new Socket(server, 8000); // Note, don't post out through the in.
		} catch (ConnectException e) {
			System.out.println("Could not connect to the socket.");
		}
		if (!socket.equals(null)) {
			System.out.println("Client is connected!");
			send("changed text", socket);
			String returned = receive(socket);
			System.out.println("Received '" + returned + "' from the server.");
			socket.getOutputStream().flush();
			socket.getInputStream().close();
			System.out.println("Closing socket and terminating client.");
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

	public static void initServer() {
		try {
			server = InetAddress.getByName("192.168.1.166");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}
