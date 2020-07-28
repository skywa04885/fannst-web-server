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

public class HTTPProperties
{
	public enum Method
	{
		GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS, CONNECT, PATCH
	};

	public enum Version
	{
		HTTP1_1, HTTP1_0
	}

	public static String responseCodeString(int code)
	{
		switch (code)
		{
			case 200: return "OK";
			case 404: return "PAGE NOT FOUND";
			default: return "INVALID CODE";
		}
	}

	public static String versionString(Version ver)
	{
		switch (ver)
		{
			case HTTP1_1: return "HTTP/1.1";
			default: case HTTP1_0: return "HTTP/1.0";
		}
	}
}