/**
 * All the fine object should be Tool, that will be returned as the final object for the end user
 *
 * 
 */


package com.salesforce.factory;

import java.io.*;

import com.sforce.async.*;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;

public interface Tool {
	public String getToolInfo();
	public void   startRun() throws ConnectionException, IOException, AsyncApiException, InterruptedException;
}
