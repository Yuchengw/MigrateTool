/**
 * This is simple bulk api wrapper class for delete operation for  * sync tool.
 *
 *  @author yucheng.wang
 *  @since 05/15/2013
 */

package com.salesforce.service.bulk;


import java.io.*;
import java.util.*;
import java.util.logging.Logger;

import com.salesforce.service.lib.log.SuperLog;

import com.sforce.async.*;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

public class Delete{
	
	private static Logger LOGGER = null;
	
	public Delete(){
	}

	public Delete(String loglevel){
		LOGGER = SuperLog.open(Delete.class, loglevel);
	}

	/**
     * function that creates a Bulk API job for delete operation
     * @param sobjectType
     * @param userName
     * @param password
     * @return void
     * @throws AsyncApiException
     * @throws ConnectionException
     * @throws IOException
     *
     */
	 public void runCSV(String sobjectType, String userName, String password) throws AsyncApiException, IOException, ConnectionException, InterruptedException{
		// log in process	
		BulkConnection connection = getBulkConnection(userName, password);
		// create batch job	
		JobInfo job = createJob(sobjectType, connection);
		// get batch info
		//List<BatchInfo> batchInfoList = createBatches(connection, job, sobjectType);	
		closeJob(connection, job.getId());
		//awaitCompletion(connection, job, batchInfoList);
	 }

	/**
     * Create the BulkConnection used to call web service
     * @param username 
     * @param userpassword
     * @return BulkConnection
     * @throws ConnctionException
     * @throws AsyncApiException
     *
     */
	public BulkConnection getBulkConnection(String username, String password) throws ConnectionException, AsyncApiException{
		
		ConnectorConfig partnerConfig = new ConnectorConfig();
		partnerConfig.setUsername(username);
		partnerConfig.setPassword(password);
		partnerConfig.setAuthEndpoint("https://login.salesforce.com/services/Soap/u/27.0");
		// create login session
		new PartnerConnection(partnerConfig);
		ConnectorConfig config = new ConnectorConfig();	
		config.setSessionId(partnerConfig.getSessionId());
		String soapEndpoint = partnerConfig.getServiceEndpoint();
		String apiVersion = "27.0";
		String restEndpoint = soapEndpoint.substring(0, soapEndpoint.indexOf("Soap/")) + "async/" + apiVersion;
		config.setRestEndpoint(restEndpoint);
		config.setCompression(true);
		config.setTraceMessage(true);
		BulkConnection connection = new BulkConnection(config);
		return connection;
	}

	/**
     * Create a new delete job using bulk api
     * @param sobject
     * @param bulkconnection
     * @param 
     * @return jobinfo
     * @throws AsyncApiException
     *
     */
	 private JobInfo createJob(String sobjectType,  BulkConnection connection) throws AsyncApiException{
		JobInfo job = new JobInfo();
		job.setObject(sobjectType);	
		job.setOperation(OperationEnum.delete);
		//job.setContentType(ContentType.CSV);
		job = connection.createJob(job);
		//System.out.println(job);
		LOGGER.info(job.toString());
		return job;	
	 }
	/**
     * After all batches finish, close the job
     * @param connection
     * @param JobInfo
     * @param AsyncApiException
     */
	 private void closeJob(BulkConnection connection, String jobId) throws AsyncApiException{
		
		JobInfo job = new JobInfo();
		job.setId(jobId);
		job.setState(JobStateEnum.Closed);
		connection.updateJob(job);
	}

}
