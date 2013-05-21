/**
 * The main log handler, During startup a subclass called SuperStartupLogHandler is used,
 * but it's a small variation on this class.
 *
 *
 * @author yucheng.wang
 */

package lib.logging;


import java.io.IOException; 
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date; 
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TODO: finish Test Module also
// import com.salesforce.service.test.TestAccesible;

import com.salesforce.service.lib.log.loglogger.SuperLogLogger;
import com.salesforce.service.lib.log.logwriter.LogWriter;
import com.salesforce.service.lib.log.output.StdOutLogOutput;

public class SuperLogHandler extends Handler{
	
	private static final Pattern REGEX_TIME_FORMAT = Pattern.compile("\\{([^}]+)\\}");
	private LogWriter logWriter = null;	
	private boolean 	rollLogFiles;
	private boolean   useGMT;

	private String basepath = null;
	private boolean needsInit = true;	
	private UTCTimeFetcher timeFetcher = new DefaultUTCTimeFetcher();
	private long nextRolltime = 0L;
	private SuperLogLogger logger = null;

	/**
   * Locks to protect the state of the handler
   *
   */	
	private final ReentrantReadWriteLock.WriteLock writeLock;
	final ReentrantReadWriteLock.ReadLock readLock;

	public SuperLogHandler(){
		final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
		writeLock = lock.writeLock();
		readLock  = lock.readLock();
		initErrorLogger();
		setLevel(Level.OFF);
		initFormatter();
	}

}



















