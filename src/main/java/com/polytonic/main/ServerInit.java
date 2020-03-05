package com.polytonic.main;

/**
 * Class that manages the initialisation of all resources necessary for the
 * operation of the server.
 * 
 * @author Max Wood
 */
public class ServerInit {

	public static void main(String[] args) {
		// InitialiseStaticResources.uInit(true, true, true, true, true);
		System.out.println("Initialising server.");
		com.polytonic.comms.Server.main(new String[0]);
	}

}
