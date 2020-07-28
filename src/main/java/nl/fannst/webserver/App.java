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

package nl.fannst.webserver;

import nl.fannst.webserver.Logger;
import nl.fannst.webserver.http.*;

public class App 
{
	static Logger console = new Logger("App", Logger.Level.INFO);

	/**
	 * Application entry
	 *
	 * @param args the command line arguments
	 */
	public static void main(String[] args)
	{
		// Prints some stuff to the console
		console.log("Fannst Webserver by Luke A.C.A. Rieff");
		console.log("This is an Java educational project, I'm learning and "
			+ "do not know what I'm doing !", Logger.Level.WARN);

		// Creates the HTTPServer and catches exceptions
		try
		{
			HTTPServer server = new HTTPServer();
			server.start();
		} catch (Exception e)
		{
			console.log("An exception occured: " + e.getMessage(),
				Logger.Level.FATAL);
		}
	}
}
