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
import java.io.*;
import java.net.*;
import nl.fannst.webserver.Logger;
import nl.fannst.webserver.http.HTTPCommand;
import nl.fannst.webserver.http.HTTPResponse;
import nl.fannst.webserver.http.exceptions.HTTPSyntaxException;
import nl.fannst.webserver.http.HTTPProperties;

public class HTTPHandler extends Thread
{
	private final Socket h_Client;
	private final Logger console;

	public HTTPHandler(Socket h_Client)
	{
		// Sets the socket
		this.h_Client = h_Client;

		// Creates the logger
		this.console = new Logger("HTTPHandler:" 
			+ this.h_Client.getInetAddress().toString().substring(1), Logger.Level.DEBUG);
		this.console.log("Thread assigned");
	}

	@Override
	public void run()
	{
		try {
			// Creates the reader for the client socket
			BufferedReader reader = new BufferedReader(new InputStreamReader(
				this.h_Client.getInputStream()));

			// Starts finite loop for handling the commands
			while (true)
			{
				// Reads the message untill '\r\n\r\n' is reached
				// - and we know that the client is requesting response
				String message = "";
				while (true)
				{
					// Reads the new line, and throws exception
					// - if something goes wrong
					String temp = reader.readLine();
					if (message == null)
						throw new Exception("Could not read data");

					// Appends the temp with the newline characters, so we later can
					// - compare it to check if there are newline chars
					message += temp;
					message += "\r\n";

					// Checks if we need to quit
					if (message.length() > 4)
						if (message.substring(message.length() - 4).equals("\r\n\r\n"))
						{
							// Gets the substring of the message to remove the last
							// - two newline indicators
							message = message.substring(0, message.length() - 4);
							break;
						}
				}

				// Prints the debug message
				this.console.log("C->" + message);

				// Parses the HTTPCommand
				HTTPCommand command = new HTTPCommand(message);
				command.print(this.console);

				HTTPResponse response = new HTTPResponse(this.h_Client);
				switch (command.c_Method)
				{
					case GET -> {
						response
							.zip()
							.code(404)
							.url(command.c_URL)
							.sendHTML("./html/404.html");
						this.h_Client.close();
					}
				}
			}
		} catch (HTTPSyntaxException e)
		{
			this.console.log("Syntax exception: " + e.getMessage(), 
				Logger.Level.ERROR);
		} catch (Exception e)
		{
			this.console.log("An exception occured: " + e.getMessage(), 
				Logger.Level.ERROR);
		}

		// Closes the socket
		try {
			this.h_Client.close();
		} catch (IOException e)
		{
			this.console.log("Could not close socket: " + e.getMessage(), 
				Logger.Level.ERROR);
		}

		// Closes the connection and prints it to the console
		// - debug only off course
		this.console.log("Thread closed");
	}
}