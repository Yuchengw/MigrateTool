/**
 * Super log for time zone
 *
 * author yucheng.wang 
 */

package com.saleforce.service.lib.log;


import java.util.TimeZone; 


public final class SuperLogTimeZones{

	private SuperLogTimeZones(){
		throw new UnsupportedOperationException();
	}
	
	public static final TimeZone LOCAL_TIME = TimeZone.getTimeZone("America/Los_Angeles");
	public static final TimeZone GMT = TimeZone.getTimeZone("GMT");

	private static volatile TimeZone logTimeZone = LOCAL_TIME;

	public static TimeZone getLogTimeZone(){
		return logTimeZone;
	}

	public static void setLogTimeZoneToGmt(){
		logTimeZone = GMT;
	}
}


