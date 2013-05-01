/** This is simple bulk api wrapper class for upsert opeartion for migration tool.
 *
 * @author yucheng.wang
 * @since 04/30/2013
 */

package com.saleforce.service.bulk;


import java.io.*;
import java.util.*;

import com.sforce.async.*;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

public class Upsert{

	public Upsert(){
	}	

	/**
     * function that creates a Bulk API job for upsert operation
     * @param sobjectType 
     * @param userName
     * @param password
     * @param csvFileName
     * @return void 
     * @throws AsyncApiException
     * @throws ConnectionException
     * @throws IOException
     */
	public void runCSV(String sobjectType, String userName, String password, String csvFileName) throws AsyncApiException, ConnectionException, IOException{
		// log in process	
		BulkConnection connection = getBulkConnection(userName, password);
		// create batch job
		JobInfo job = createJob(sobjectType,connection);
		// get batch job info and implement upsert
		List<BatchInfo> batchInfoList = createBatchesFromCSVFile(connection, job, csvFileName);
		// close job
		closeJob(connection, job.getId());
		// check result	
		// checkResults(connection, job, batchInfoList);
	}
	/**
     * Create the BulkConection used to call web service
     * @param username
     * @param userpassword
     * @return BulkConnenction
     * @throws ConnectionException
     * @throws AsyncApiExceptioon
     */	

}

