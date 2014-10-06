package com.mailroom.common;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Custom Logger Class for logging Errors and Exceptions <br>
 * Will put them into a database located in logs
 * @author James sitzja@grizzlies.adams.edu
 */
public class Logger
{
	/**
	 * Create Statement for Error File <br>
	 * Could make a single database file but this seems better to have one per
	 * day
	 */
	private final static String create = "CREATE TABLE IF NOT EXISTS Error(error_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,error_message VARCHAR(100) NOT NULL,error_stacktrace TEXT NOT NULL)";

	/**
	 * Logs data from Exception
	 * @param ex Exception caught
	 * @return status of logging success
	 */
	public static boolean log(Exception ex)
	{
		File dir = new File("./Logs");

		if (!dir.exists())
		{
			dir.mkdir();
		}

		boolean retVal = true;

		Date d = new Date();
		String sDate = new SimpleDateFormat("yyyy-MM-dd").format(d).toString();
		String fileName = "./Logs/" + sDate + ".err";
		File f = new File(fileName);

		try
		{
			Class.forName("org.sqlite.JDBC");
		}
		catch (ClassNotFoundException e)
		{
			System.err.println("Logging Error");
			e.printStackTrace();
			retVal = false;
		}

		if (!f.exists())
		{
			try
			{
				f.createNewFile();

				Connection con = DriverManager.getConnection("jdbc:sqlite:"
						+ fileName);
				Statement stmnt = con.createStatement();

				stmnt.execute(create);

				stmnt.close();
				con.close();
			}
			catch (IOException | SQLException e)
			{
				System.err.println("Logging Error");
				e.printStackTrace();
				retVal = false;
			}
		}

		try
		{
			Connection con = DriverManager.getConnection("jdbc:sqlite:"
					+ fileName);
			java.sql.PreparedStatement stmnt = con
					.prepareStatement("insert into Error(error_message, error_stacktrace) values(?,?)");

			stmnt.setString(1, ex.getMessage());
			Writer stackTrace = new StringWriter();
			PrintWriter pWriter = new PrintWriter(stackTrace);
			ex.printStackTrace(pWriter);
			stmnt.setString(2, stackTrace.toString());

			if (!stmnt.execute())
			{
				retVal = false;
			}
			pWriter.close();
			stackTrace.close();
			stmnt.close();
			con.close();
		}
		catch (SQLException e)
		{
			System.err.println("Logging Error");
			e.printStackTrace();
			retVal = false;
		}
		catch (IOException e)
		{
			System.err.println("Logging Erorr");
			e.printStackTrace();
			retVal = false;
		}

		return retVal;
	}
}
