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

import java.net.*;
import java.io.*;
import nl.fannst.webserver.Logger;
import nl.fannst.webserver.http.HTTPPlainAcceptor;

public class HTTPServer
{
	private final int s_PlainPort;
	private final int s_SecurePort;
	private final ServerSocket s_SecureServer;
	private final ServerSocket s_PlainServer;
	private final Logger console;

	/**
	 * Default empty constructor for the HTTPServer
	 *
	 * @throws IOException
	 */
	public HTTPServer() throws IOException
	{
		this(80, 443);
	}

	/**
	 * The default constructor for the HTTPServer
	 *
	 * @param s_PlainPort the port for the plain server
	 * @param s_SecurePort the port for the secure server
	 * @throws IOException
	 */
	public HTTPServer(int s_PlainPort, int s_SecurePort) throws IOException
	{
		// Sets the ports
		this.s_PlainPort = s_PlainPort;
		this.s_SecurePort = s_SecurePort;

		// Creates the sockets
		this.s_PlainServer = new ServerSocket(this.s_PlainPort);
		this.s_SecureServer = new ServerSocket(this.s_SecurePort);

		// Creates the logger and prints the initial message
		this.console = new Logger("HTTPServer", Logger.Level.INFO);
		this.console.log("Server started on = {secure: " + this.s_SecurePort 
			+ ", plain: " + this.s_PlainPort + "}");
	}

	/**
	 * Starts accepting clients, and creates the handler thread
	 *
	 * @throws Exception
	 */
	public void start() throws Exception
	{
		// Starts the plain acceptor
		Thread plainAcceptor = new HTTPPlainAcceptor(this.s_PlainServer);
		plainAcceptor.start();

		// Starts the secure acceptor
	}
}