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
import java.io.*;
import nl.fannst.webserver.http.exceptions.HTTPSyntaxException;

public class HTTPHeader
{
	public static String buildHeaders(HashMap<String, String> headers)
	{
		StringBuilder result = new StringBuilder();

		// Builds the result
		for (String key : headers.keySet())
			result.append(key).append(": ").append(headers.get(key)).append("\r\n");

		return result.toString();
	}

	public static HashMap<String, String> parseHeaders(String raw) throws HTTPSyntaxException
	{
		HashMap<String, String> headers = new HashMap<String, String>();

		try {
			BufferedReader reader = new BufferedReader(new StringReader(raw));
			String line, key, value;
			long index = 0;
			while ((line = reader.readLine()) != null)
			{
				// Checks if it is the first line, if so ignore
				// - since it is not an header, else if it is
				// - an empty line, also ignore since the request body
				// - has started then
				if (index++ == 0) continue;
				else if (line == "") break;

				// Parses the header
				int colonIndex = line.indexOf(":");
				if (colonIndex == -1)
					throw new HTTPSyntaxException("Invalid header field");

				// Gets the key and value, then we remove the non-required
				// - whitespace, so it is easier to read later on
				key = line.substring(0, colonIndex).trim()
				.replaceAll(" +", " ").toLowerCase();
				value = line.substring(colonIndex + 1).trim().replaceAll(" +", " ");

				// Pushes the header to the headers, basically the hashmap
				headers.put(key, value);
			}
		} catch (IOException e)
		{
			throw new HTTPSyntaxException(e.getMessage());
		}

		return headers;
	}
}