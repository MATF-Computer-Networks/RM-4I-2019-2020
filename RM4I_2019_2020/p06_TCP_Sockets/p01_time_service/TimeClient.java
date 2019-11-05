package p01_time_service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

class TimeClient {

	// https://tf.nist.gov/tf-cgi/servers.cgi
	// We are using TIME protocol here (port 37)
	
	public static void main(String[] args) {

		String hostname = "time.nist.gov";
		
		// The time protocol sets the epoch at 1900, the Java Date class works 
		// with 1970 as the epoch start. So we need to convert manually.
		
		// One option: use this magic number
		// long onlyForMagicians = 2208988800L;
		
		// Second option: calculate it by hand
		// 		Take timezone into account - returned value corresponds to GMT time
		TimeZone gmt = TimeZone.getTimeZone("GMT");
		// 		Create a calendar
		Calendar epoch1900 = Calendar.getInstance(gmt);
		//		Set it to a point in time
		epoch1900.set(1900, Calendar.JANUARY, 1, 0, 0, 0);
		//		Get number of milliseconds since that point in time
		long epoch1900ms = epoch1900.getTimeInMillis();
		//		Do same for the other point
		Calendar epoch1970 = Calendar.getInstance(gmt);
		epoch1970.set(1970, Calendar.JANUARY, 1, 0, 0, 0);
		long epoch1970ms = epoch1970.getTimeInMillis();
		//		Calculate the difference
		long epochDifferenceInMilliseconds = epoch1970ms - epoch1900ms;
		long epochDifferenceInSeconds = epochDifferenceInMilliseconds / 1000;

		// Prepare to receive data from server
		try (Socket sock = new Socket(hostname, 37)) {
			InputStream raw = new BufferedInputStream(sock.getInputStream());
			
			// We receive byte by byte. The end result is an "unsigned int"
			// representing number of seconds elapsed since 1900. Each time we
			// read we shift the current result by one byte to the left and do
			// bitwise OR with the new byte. For example:
			//   1st iteration: We receive 0xAB. Our current result is 0x00000000.
			//   			    Bitwise OR gives: 0x000000AB
			//   2nd iteration: We receive 0xCD. Our current result is 0x000000AB.
			//   				Shift: 0x0000AB00 -> OR: 0x0000ABCD
			//   3rd iteration: We receive 0xEF. Our current result is 0x0000ABCD.
			//   				Shift: 0x00ABCD00 -> OR: 0x00ABCDEF
			//   4th iteration: We receive 0x12. Our current result is 0x00ABCDEF.
			//   				Shift: 0xABCDEF00 -> OR: 0xABCDEF12
			//
			// ( btw almost wrote 0xGH and made up new hex system like 007 @ 0:25:
			// https://www.youtube.com/watch?v=aApTVqeGJMw )

			// We have to use `long` here because if we use `int` it is treated as
			// signed - the value from server represents an `unsigned int` but since
			// Java does not have unsigned types, during automatic cast to long the
			// value might be treated as a negative number, which will interfere with
			// our operations later.
			long secondsSince1900 = 0;

			for (int i = 0; i < 4; i++)
				secondsSince1900 = (secondsSince1900 << 8) | raw.read();

			// Now we can calculate current time
			long secondsSince1970 = secondsSince1900 - epochDifferenceInSeconds;
			long millisecondsSince1970 = secondsSince1970 * 1000;
			
			Date now = new Date(millisecondsSince1970);

			System.out.println("It is " + now + " at " + hostname);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

}
