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

import java.util.HashMap;
import nl.fannst.webserver.http.HTTPHeader;
import nl.fannst.webserver.Logger;
import nl.fannst.webserver.http.exceptions.HTTPSyntaxException;

public class HTTPCommand
{
	private Method c_Method;
	private String c_URL;
	private Version c_Version;
	private HashMap<String, String> c_Headers;

	public enum Version
	{
		HTTP1_1, HTTP1_0
	}

	public enum Method
	{
		GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS, CONNECT, PATCH
	};

	/**
	 * Prints the HTTPCommand
	 *
	 * @param console the console to log to
	 */
	public void print(Logger console)
	{
		// Prints the command itself
		console.log("[HTTPCommand]: ", Logger.Level.DEBUG);
		console.log("- Version: " + this.c_Version.name(), Logger.Level.DEBUG);
		console.log("- Method: " + this.c_Method.name(), Logger.Level.DEBUG);
		console.log("- URL: " + this.c_URL, Logger.Level.DEBUG);

		// Prints the headers
		console.log("- Headers:", Logger.Level.DEBUG);
		for (String key : this.c_Headers.keySet())
		{
			console.log("\t- '" + key + "': '" + this.c_Headers.get(key) + "'", Logger.Level.DEBUG);
		}
	}

	/**
	 * The parsing constructor for the HTTP command
	 *
	 * @param raw the raw string
	 * @throws HTTPSyntaxException
	 */
	public HTTPCommand(String raw) throws HTTPSyntaxException
	{
		this.parse(raw);
	}

	/**
	 * Parses an HTTP command
	 *
	 * @param raw the raw command
	 */
	public void parse(String raw) throws HTTPSyntaxException
	{
		// ==================================================
		// Parses the head
		//
		// Parses the head of the command, so the method
		// - path and http version
		//
		// Example: 'GET / HTTP/1.1'
		// ==================================================

		// Checks if the length is valid
		if (raw.indexOf(" ") == -1)
			throw new HTTPSyntaxException("Invalid head");

		// Gets the index to split to
		int splitTo = raw.indexOf("\r\n");
		if (splitTo == -1) splitTo = raw.length();

		// Splits the head into segments
		String[] headSegments = raw.substring(0, splitTo).split(" ");
		if (headSegments.length < 3)
			throw new HTTPSyntaxException("Invalid head");

		// Gets the method
		switch (headSegments[0])
		{
			case "GET" -> {
				this.c_Method = Method.GET;
			}
			case "HEAD" -> {
				this.c_Method = Method.HEAD;
			}
			case "POST" -> {
				this.c_Method = Method.POST;
			}
			case "PUT" -> {
				this.c_Method = Method.PUT;
			}
			case "DELETE" -> {
				this.c_Method = Method.DELETE;
			}
			case "TRACE" -> {
				this.c_Method = Method.TRACE;
			}
			case "OPTIONS" -> {
				this.c_Method = Method.OPTIONS;
			}
			case "CONNECT" -> {
				this.c_Method = Method.CONNECT;
			}
			case "PATCH" -> {
				this.c_Method = Method.PATCH;
			}
			default -> {
				throw new HTTPSyntaxException("Invalid method");
			}
		}

		// Stores the url
		this.c_URL = headSegments[1];

		// Parses the method
		switch (headSegments[2])
		{
			case "HTTP/1.1" -> {
				this.c_Version = Version.HTTP1_1;
			}
			case "HTTP/1.0" -> {
				this.c_Version = Version.HTTP1_0;
			}
			default -> {
				throw new HTTPSyntaxException("Invalid version");
			}
		}

		// ==================================================
		// Parses the headers
		//
		// Parses the request headers such as the
		// - host, accept-language etc
		// ==================================================

		// Parses the headers
		this.c_Headers = HTTPHeader.parseHeaders(raw);
	}
}