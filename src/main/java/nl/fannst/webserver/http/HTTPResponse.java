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

import nl.fannst.webserver.http.HTTPProperties;
import java.util.zip.*;
import java.io.*;
import java.net.*;
import java.util.HashMap;

class HTTPResponse
{
	private OutputStream r_OutputStream;
	private HashMap<String, String> r_Headers;
	private Socket r_Client;

	private HTTPProperties.Version r_RequestVersion;
	private HTTPProperties.Method r_RequestMethod;
	private int r_ResponseCode;
	private String r_RequestURL;
	private boolean r_GZIP;

	public HTTPResponse(Socket r_Client) throws IOException
	{
		// Sets the variables
		this.r_Headers = new HashMap<String, String>();
		this.r_Client = r_Client;
		this.r_OutputStream = this.r_Client.getOutputStream();

		// Sets the default headers
		this.r_Headers.put("Server", "Fannst HTTP/1.0 (" + System.getProperty("os.name") + ")");

		// Sets the default response code
		this.r_ResponseCode = 200;
		this.r_RequestMethod = HTTPProperties.Method.GET;
		this.r_RequestVersion = HTTPProperties.Version.HTTP1_1;
		this.r_GZIP = false;
	}

	private static byte[] compress(String input) throws IOException
	{
		// Creates the output stream, and the gzip output stream
		// - these will store the bytes
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		GZIPOutputStream gzip = new GZIPOutputStream(outputStream);

		// Writes the input stirng as bytes, and zips them
		gzip.write(input.getBytes("utf-8"));
		gzip.flush();
		gzip.close();

		// Returns the result
		return outputStream.toByteArray();
	}

	private void sendHeaders() throws IOException
	{
		// Builds the headers
		String headers = HTTPHeader.buildHeaders(this.r_Headers);

		// Writes the headers and the newline
		this.r_OutputStream.write(headers.getBytes("utf-8"));
		this.r_OutputStream.write("\r\n".getBytes("utf-8"));
	}

	private void sendHead() throws IOException
	{
		// Builds the response
		StringBuilder result = new StringBuilder();
		result
			.append(HTTPProperties.versionString(this.r_RequestVersion))
			.append(" ")
			.append(this.r_RequestURL)
			.append(" ")
			.append(HTTPProperties.responseCodeString(this.r_ResponseCode))
			.append("\r\n");

		// Writes the response
		this.r_OutputStream.write(result.toString().getBytes("utf-8"));
	}

	private void finish() throws IOException
	{
		this.r_OutputStream.write("\r\n".getBytes("utf-8"));
		this.r_OutputStream.close();
	}

	public HTTPResponse code(int c)
	{
		this.r_ResponseCode = c;
		return this;
	}

	public HTTPResponse url(String u)
	{
		this.r_RequestURL = u;
		return this;
	}

	public HTTPResponse version(HTTPProperties.Version v)
	{
		this.r_RequestVersion = v;
		return this;
	}

	public HTTPResponse method(HTTPProperties.Method m)
	{
		this.r_RequestMethod = m;
		return this;
	}

	public HTTPResponse zip()
	{
		this.r_Headers.put("Content-Encoding", "gzip");
		this.r_GZIP = true;
		return this;
	}

	public void sendHTML(String file) throws IOException
	{
		// Sets the headers
		this.r_Headers.put("Content-Type", "text/html; charset=utf-8");

		// Sends the head
		this.sendHead();

		// Sends the headers
		this.sendHeaders();

		// Sends the file and finishes writing
		this.sendFile(file);
		this.finish();
	}

	public void sendFile(String file) throws IOException
	{
		// Opens the file input stream
		InputStream inputStream = new FileInputStream(file);

		// Checks if we need to zip
		if (this.r_GZIP)
		{
			// Reads the file, line by line
			BufferedReader inputStreamReader = new BufferedReader(
				new InputStreamReader(inputStream));
			
			String line, toZip = "";
			while ((line = inputStreamReader.readLine()) != null)
				toZip += line + "\r\n";

			// Performs the zipping, and then sends it to the client
			byte[] zipped = this.compress(toZip);
			this.r_OutputStream.write(zipped);
		} else
		{
			// Just writes the bytes to the client
			int no;
			byte[] buffer = new byte[2048];
			while ((no = inputStream.read(buffer)) > 0)
				this.r_OutputStream.write(buffer, 0, no);
		}

		// Closes the stream
		inputStream.close();
	}
}