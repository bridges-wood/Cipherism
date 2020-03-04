package com.polytonic.main;

import com.polytonic.cipher.InitialiseStaticResources;

/**
 * Class that manages the initialisation of all resources necessary for the
 * operation of the server.
 * 
 * @author Max Wood
 */
public class ServerInit {

	public static void main(String[] args) {
		//InitialiseStaticResources.uInit(true, true, true, true, true);
		com.polytonic.comms.Server.main(new String[0]);
	}

}
