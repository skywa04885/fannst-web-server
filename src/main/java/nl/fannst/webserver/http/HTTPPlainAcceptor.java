/*
	Copyright [2020] [Luke A.C.A. Rieff]
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	http://www.apache.org/licenses/LICENSE-2.0
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/

package nl.fannst.webserver.http;

import java.lang.Thread;
import java.net.*;
import java.io.*;
import nl.fannst.webserver.Logger;
import nl.fannst.webserver.http.HTTPHandler;

public class HTTPPlainAcceptor extends Thread
{
	private final ServerSocket a_ServerSocket;
	private final Logger console;

	public HTTPPlainAcceptor(ServerSocket a_ServerSocket)
	{
		// Sets the veriables
		this.a_ServerSocket = a_ServerSocket;

		// Creates the logger and prints it to the console
		this.console = new Logger("PlainAcceptor", Logger.Level.INFO);
		this.console.log("Plain acceptor started");
	}

	@Override
	public void run()
	{
		while (true)
		{
			try {
				// Accepts the client, and prints an the client to the logger
				// - so it looks hacker-ish
				Socket client = this.a_ServerSocket.accept();
				this.console.log("Client " 
					+ client.getRemoteSocketAddress().toString().substring(1) 
					+ " accepted !");

				// Sets the socket timeout, so it will not block forever
				client.setSoTimeout(6000); // 6 Seconds

				// Starts the HTTPHandler thread
				Thread handler = new HTTPHandler(client);
				handler.start();
			} catch (IOException e)
			{
				this.console.log("Could not accept client !", Logger.Level.ERROR);
			}
		}
	}
}