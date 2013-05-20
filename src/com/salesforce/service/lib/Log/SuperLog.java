/**
 * Super log template
 *
 * author yucheng.wang
 * 
 */


package com.salesforce.service.lib.log;


import java.util.Calendar;
import java.util.logging.Logger;


public class SuperLog{

	private SuperLog(){
		throw new UnsupportedOperationException();
	}
	//TODO: finish the warper methods 
 		
  /**
   * Gets the class name for logger
   */
	public static String getLoggerName(Class cl){
		return "Super." + cl.getName();
	}

	/**
   * Returns the logger for a class adjusted for SUPER hierachy
   */
	public static Logger open(Class cl){
		try{
			return Logger.getLogger(getLoggerName(cl));
		}catch(RuntimeException e){
			e.printStackTrace();
			// no need to rethrow here, test later
			//throw e;
		}catch(Error e){
			e.printStackTrace();
		}
	}	

	/**
   * Utility method to format the given timestamp either as PST or GMT
   * depending on whether logging is configured to log in GMT
   */
	public static String formatTimestamp(long millis){
		Calendar c = Calendar.getInstance(SuperLogTimeZones.getLogTimeZone());
		c.setTimeInMillis(millis);
		return formatTimestamp(c);
	}

	// try to suppressWarnings
	public static String formatTimestamp(Calendar c){
		return formatTimestamp(c,true);
	}

	public static String formatTimestamp(Calendar c, boolean includeMillis){
		StringBuilder buf = new StringBuilder(32);
		buf.append(c.get(Calendar.YEAR));
		buf.append(lpad(c.get(Calendar.MONTH) + 1, 2));
		buf.append(lpad(c.get(Calendar.DAY_OF_MONTH),2));
		buf.append(lpad(c.get(Calendar.HOUR_OF_DAY),2));
		buf.append(lpad(c.get(Calendar.MINUTE),2));
    buf.append(lpad(c.get(Calendar.SECOND),2));
		buf.append(".");
		if(includeMillis){
			buf.append(lpad(c.get(Calendar.MILLISECOND), 3));
		}	
		return buf.toString();
	}
	
	/**
     * Pads <code>val</code> with 0s so that is len chars long.
     * Only works for len less than or equal to 3.
     */
	public static String lpad(int val, int len){
		if(val < 10){
			return (len == 3 ? "00" : "0") + Integer.toString(val);
		}
		if(len == 3 && val < 100){
			return "0" + Integer.toString(val);
		}
		return Integer.toString(val);
	}
	
	public static String getLogTimeString(String name, long start){
		StringBuilder buf = new StringBuilder(100);	
		buf.append(name);
		buf.append(": ");
		buf.append(System.currentTimeMills() - start);
		buf.append("ms");
		return buf.toString();
	}
}


