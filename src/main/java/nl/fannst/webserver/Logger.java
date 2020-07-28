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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Logger
{
	private String l_Prefix;
	private Level l_Level;

	public static final String _clear = "\033[0m";
	public static final String _foregroundRed = "\033[31m";
	public static final String _foregroundGreen = "\033[32m";
	public static final String _foregroundYellow = "\033[33m";
	public static final String _foregroundBlue = "\033[34m";

	private static DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	
	private static Level levelFrom = Level.DEBUG;

	// ==== The level of the current log entry ====
	public enum Level
	{
		DEBUG(0), INFO(1), WARN(2), ERROR(3), FATAL(4);

		public final int l_Value;

		/**
		 * Default constructor for the level
		 *
		 * @param l_Value the int val
		 */
		private Level(int l_Value)
		{
			this.l_Value = l_Value;
		}
	};

	/**
	 * Sets the new required level
	 *
	 * @param Level the new required level
	 * @return void
	 */
	public static void setLevelFrom(Level level)
	{
		levelFrom = level;
	}

	/**
	 * Default empty constructor
	 */
	public Logger()
	{
		this("Unset", Level.INFO);
	}

	/**
	 * Default constructor for the logger
	 *
	 * @param l_Prefix the prefix for the logger
	 * @param l_Level the level for the logger
	 */
	public Logger(String l_Prefix, Level l_Level) {
		this.l_Prefix = l_Prefix;
		this.l_Level = l_Level;
	}

	/**
	 * Sets the level of the logger
	 *
	 * @param l_Level the new level, that will be set
	 */
	public void setLevel(Level l_Level)
	{
		this.l_Level = l_Level;
	}

	/**
	 * Sets the new prefix of the logger
	 *
	 * @param l_Prefix the new prefix
	 */
	public void setPrefix(String l_Prefix)
	{
		this.l_Prefix = l_Prefix;
	}

	/**
	 * Logs with an temp level, which will be restored after method call
	 *
	 * @param message the message which will be displayed
	 * @param level the temp level
	 */
	public void log(String message, Level level)
	{
		Level old = this.l_Level;
		this.l_Level = level;
		
		this.log(message);

		this.l_Level = old;
	}

	/**
	 * Logs an message to the console
	 *
	 * @param message the message
	 */
	public void log(String message)
	{
		// Checks if the current level should be printed to the console
		// - if not just break, else create the string builder
		if (this.l_Level.l_Value < levelFrom.l_Value)
			return;
		StringBuilder result = new StringBuilder();

		// Appends the current date, so we can look back in the history
		// - and have a clear view of what happened
		Date now = Calendar.getInstance().getTime();
		result.append(dateFormat.format(now)).append(' ');

		// Appends the logger level with the colors, to make
		// - the console message more readable
		switch (this.l_Level) {
			case DEBUG -> {
				result.append(_foregroundGreen).append("(DEBUG@").append(this.l_Prefix).append(')');
			}
			case INFO -> {
				result.append(_foregroundBlue).append("(INFO@").append(this.l_Prefix).append(')');
			}
			case WARN -> {
				result.append(_foregroundYellow).append("(WARN@").append(this.l_Prefix).append(')');
			}
			case ERROR -> {
				result.append(_foregroundRed).append("(ERROR@").append(this.l_Prefix).append(')');
			}
			case FATAL -> {
				result.append(_foregroundRed).append("(FATAL@").append(this.l_Prefix).append(')');
			}
		}

		// Clears the previous added colors, appends the colon, and then
		// - the message, then we print it
		result.append(_clear).append(" : ").append(message);
		System.out.println(result);
	}
}
