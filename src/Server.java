import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

	public static void main(String[] args) {
		new Server().startServer();
	}

	public void startServer() {
		final ExecutorService clientProcessingPool = Executors.newCachedThreadPool(); // Creates our pool of threads to
																						// process incoming text from
																						// clients.
		Runnable serverTask = new Runnable() {

			@Override
			public void run() {
				try {
					@SuppressWarnings("resource") // Because the server should always be up, the socket is never closed
													// programmatically.
					ServerSocket serverSocket = new ServerSocket(8000);
					System.out.println("Waiting for clients to connect...");
					while (true) {
						Socket clientSocket = serverSocket.accept();
						// If any task comes through, a thread is automatically assigned to it.
						clientProcessingPool.submit(new ClientTask(clientSocket));
					}// need host and port, we want to connect to the ServerSocket at port 8000
				} catch (IOException e) {
					System.err.println("Unable to process client request");
					e.printStackTrace();
				}
			}
		};
		Thread serverThread = new Thread(serverTask);
		serverThread.start();
	}

	private class ClientTask implements Runnable {
		private final Socket clientSocket;

		private ClientTask(Socket clientSocket) {
			this.clientSocket = clientSocket;
		}

		@Override
		public void run() {
			System.out.println("Got a client !");
			String toSend = receive(clientSocket);
			System.out.println("Data from Client: " + toSend);
			try {
				send(toSend, clientSocket);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/**
		 * Sends a string over a specified socket, back to the client that submitted the
		 * request.
		 * 
		 * @param dataString The string to be sent.
		 * @param socket     The socket of the client.
		 * @throws IOException
		 */
		public void send(String dataString, Socket socket) throws IOException {
			OutputStream outputStream = socket.getOutputStream();
			DataOutputStream out = new DataOutputStream(outputStream); // Creates an output stream to the other end of
																		// the socket.
			System.out.println("Sending messages to the client");
			out.writeUTF(dataString); // Writes data to the client.
			System.out.println("Closing socket and terminating program.");
			out.flush(); // Cleans buffer.
		}

		/**
		 * Receives string data from the given client socket.
		 * 
		 * @param socket The clients socket.
		 * @return The string sent by the client.
		 */
		public String receive(Socket socket) {
			try {
				DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream())); // Streams
																											// the data
																											// in from
																											// the
																											// client.
				String dataString = in.readUTF();
				return dataString;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

}
