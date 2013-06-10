/**
 * This is a simple bulkapi for query operation		for migration tool
 *
 * @author yucheng.wang
 * @since  04/13/2013
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


public class Query{
	
	private static Logger LOGGER = null;

	public Query(){
	}
	
	public Query(String loglevel){
			LOGGER = SuperLog.open(Query.class,loglevel);
	}

	/**
   * Main routine that creates a BULK API job nad downloads the fields of custome object into a CSV file. TODO: should also create a XML file version
   * @param sobjectType
   * @param userName
   * @param userpassword, 
   * @return void
   * @throws AsyncApiException
   * @throws ConnectionException 
   * @throws IOException
   */
	public void runCSV(String sobjectType, String userName, String password, ArrayList<String> orgfields) throws AsyncApiException,IOException,ConnectionException, InterruptedException{
		// log in process
		BulkConnection connection = getBulkConnection(userName, password);
		// create batch job	
		JobInfo job = createJob(sobjectType, connection);
		// get batch job info and save records into a temp csv fileo		
		List<BatchInfo> batchInfoList = createBatchesToCSVFile(connection, job, sobjectType,null,orgfields);
		// close job
		closeJob(connection, job.getId());
		awaitCompletion(connection,job,batchInfoList);
		// check result	
		//checkResults(connection, job, batchInfoList);
	}
	
	/**
   * overload
   */	
	public void runCSV(String sobjectType, String userName, String password, ArrayList<String> orgfields, String wherestatement) throws AsyncApiException,IOException,ConnectionException, InterruptedException{
		// log in process
		BulkConnection connection = getBulkConnection(userName, password);
		// create batch job	
		JobInfo job = createJob(sobjectType, connection);
		// get batch job info and save records into a temp csv fileo		
		List<BatchInfo> batchInfoList = createBatchesToCSVFile(connection, job, sobjectType,wherestatement,orgfields);
		// close job
		closeJob(connection, job.getId());
		awaitCompletion(connection,job,batchInfoList);
		// check result	
		//checkResults(connection, job, batchInfoList);
	}
		
  /**
   * Create the BulkConnection used to call web service
   * @param username;userpassword
   * @return ConnectionException;AsyncApiException
   */
	public BulkConnection getBulkConnection(String username, String password) throws ConnectionException, AsyncApiException{
		ConnectorConfig partnerConfig = new ConnectorConfig();
		
		partnerConfig.setUsername(username);	
		partnerConfig.setPassword(password);
		partnerConfig.setAuthEndpoint("https://login.salesforce.com/services/Soap/u/27.0");
		// creating connection using user name and uuser password and stores session id in partner Config	
		new PartnerConnection(partnerConfig);
		// after successful log in, user valid session is stored in the ConnectConfig instance to initialize a Bulk Connection
		ConnectorConfig config = new ConnectorConfig();
		config.setSessionId(partnerConfig.getSessionId());
		// construct the valid SOAP uri for bulk operation by using partnerConfig
		String soapEndpoint = partnerConfig.getServiceEndpoint();
		String apiVersion = "27.0";
		String restEndpoint = soapEndpoint.substring(0,soapEndpoint.indexOf("Soap/")) + "async/" + apiVersion;	
		config.setRestEndpoint(restEndpoint);		
		// This should only be false when doing debugging
		config.setCompression(true);
		// Set this to true to see HTTP requests and responses on stdout	
		//config.setTraceMessage(true);	
		//config.setTraceFile("traceLogs.txt");
		BulkConnection connection = new BulkConnection(config);
		return connection;
	}
		
  /**
   * Create a new job using Bulk API
   * @param sobject;bulkconnection	 
   * @return JobInfo for the new job
   * @throws AsyncApiException
   *
   */
		private JobInfo createJob(String sobjectType, BulkConnection connection) throws AsyncApiException{
		JobInfo job = new JobInfo();
		job.setObject(sobjectType);
		// chose query operation
		job.setOperation(OperationEnum.query);
		job.setContentType(ContentType.CSV);
		job.setConcurrencyMode(ConcurrencyMode.Parallel);
		job = connection.createJob(job);
		//System.out.println(job);
		LOGGER.info(job.toString());	
		return job;
	}
	
  /**
   * Create batches for writing a CSV file
   * The file into the appropriate size batch
   * files (1,000 to 10,000 is reasonable) 
   * @param connection 
   * @param jobInfo
   * @param sobjectType
   * @param sql  where clause statment
   * @param orgfields select items
   * @return batchInfos for the job
   * @throws IOException;AsyncApiException
   *
   */
 	private List<BatchInfo> createBatchesToCSVFile(BulkConnection connection, JobInfo jobInfo, String sobjectType, String sql, ArrayList<String> orgfields) throws IOException, AsyncApiException, InterruptedException{
		List<BatchInfo> batchInfos = new ArrayList<BatchInfo>();		
		//try{
			File tmpFile= new File("query.csv"); 
			if(tmpFile.exists()) tmpFile.delete();
			    tmpFile.createNewFile();
			    FileOutputStream tmpOut = new FileOutputStream(tmpFile,true);	
			    // construct the query 
				StringBuilder query = new StringBuilder();
				
				if(sql == null && orgfields != null && orgfields.size() != 0){
				    query.append("SELECT ");
				    for(int i = 0; i < orgfields.size(); i++){
				    	if(i == orgfields.size() - 1) query.append(orgfields.get(i) + " ");
				    	else query.append(orgfields.get(i) + ", ");	
				    }
				    query.append("FROM ");
				    query.append(sobjectType);
				// construct sql select statement with where clause
				}else if(sql != null && orgfields != null){
				    query.append("SELECT ");
				    for(int i = 0; i < orgfields.size(); i++){
				    	if(i == orgfields.size() - 1) query.append(orgfields.get(i) + " ");
				    	else query.append(orgfields.get(i) + ", ");	
				    }
				    query.append("FROM ");
				    query.append(sobjectType +" ");
					query.append("WHERE ");
					query.append(sql);
					//System.out.println("***************query message: " + query);
					LOGGER.info("*********************query message: " + query);
				}else{
					//System.out.println("could not construct sql statement....");
					LOGGER.info("could not construct sql statement....");
				}
			    String[] res = null;
			    ByteArrayInputStream bout = new ByteArrayInputStream(query.toString().getBytes());	
			    BatchInfo info = connection.createBatchFromStream(jobInfo,bout);
			    batchInfos.add(info);
				//TODO: add feature to reminder if there is a timeout
				for(int i = 0; i < 1000; i++ ){
					Thread.sleep(i == 0 ? 10 * 1000 : 10 * 1000); //10 sec
					info = connection.getBatchInfo(jobInfo.getId(),info.getId());
					batchInfos.add(info);
					if(info.getState() == BatchStateEnum.Completed){
						// get stream id
						res = connection.getQueryResultList(jobInfo.getId(), info.getId()).getResult();	
						break;
					}else if(info.getState() == BatchStateEnum.Failed){
						//System.out.println("---------------Query failed-------");
						LOGGER.info("-------------------Query failed--------------");
					}else{
						//System.out.println("----------------Query waiting-----");
						LOGGER.info("-------------------Query waiting-----------");	
					}
				}
			  if(res != null){
			  for(String resultId : res){
			   	 InputStream inputStream = connection.getQueryResultStream(jobInfo.getId(),info.getId(),resultId);
			   	 StringBuilder sb = new StringBuilder();
			     BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			     String read;
			     while ((read = br.readLine()) != null) {
			 	     sb.append(read + "\n");
			     }
			 	 	 tmpOut.write(sb.toString().getBytes("UTF-8"));
			     br.close();
			  }
			  }
			tmpOut.flush();
			tmpOut.close();
		return batchInfos;
	}

	 	
		/**
     * After all batches have been added to job, close the job
	   * @param BulkConnection JobId
		 * @return void
     * @throws AsyncApiException
     */
		private void closeJob(BulkConnection connection, String jobId) throws AsyncApiException{
			JobInfo job = new JobInfo();	
			job.setId(jobId);
			job.setState(JobStateEnum.Closed);
			connection.updateJob(job);
		}	

   private void awaitCompletion(BulkConnection connection, JobInfo job,
               List<BatchInfo> batchInfoList) throws AsyncApiException{
 
     long sleepTime = 0L;
     Set<String> incomplete = new HashSet<String>();
     for(BatchInfo bi : batchInfoList){
       incomplete.add(bi.getId());
     }
 
     while( !incomplete.isEmpty()){
       try{
         Thread.sleep(sleepTime);
       }catch(InterruptedException e){}
         //System.out.println("Awaiting results... " + incomplete.size());
		 LOGGER.info("Awaiting results..." + incomplete.size());
         sleepTime = 10000L;
         BatchInfo[] statusList = connection.getBatchInfoList(job.getId()).getBatchInfo();
       for(BatchInfo b : statusList){
         if(b.getState()  == BatchStateEnum.Completed || b.getState() ==
                             BatchStateEnum.Failed){
           if(incomplete.remove(b.getId())){
             //System.out.println("Batch STATUS:\n" + b);
						 LOGGER.info("Batch Status:\n" + b);	
           }
         }
       }
     }
   }
			
	/**
     * Get the resutls of the operation and checks for errors
     * @param BulkConnection;JobInfo;ListofBatchInfos
	 * @return void
     * @throws AsyncApiException;IOException
     */
		private void checkResults(BulkConnection connection, JobInfo job, List<BatchInfo> batchInfoList) throws AsyncApiException, IOException		{
			for(BatchInfo b : batchInfoList){
				CSVReader rdr = new CSVReader(connection.getBatchResultStream(job.getId(), b.getId()));	
				List<String> resultHeader = rdr.nextRecord();
				int resultCols = resultHeader.size();
				List<String> row;
				while((row = rdr.nextRecord()) != null){
				Map<String,String> resultInfo = new HashMap<String,String>();
				for(int i = 0;  i < resultCols; i++){
					resultInfo.put(resultHeader.get(i), row.get(i));
				}
				boolean success = Boolean.valueOf(resultInfo.get("Success"));
				boolean created = Boolean.valueOf(resultInfo.get("Created"));
				String id = resultInfo.get("Id");
				String error = resultInfo.get("Error");
				if(success && created){
					//System.out.println("Created row with id" + id);
					LOGGER.info("Created row with id " + id);
				}else if(!success){
					//System.out.println("Failed with error " + error);
					LOGGER.info("Failed with error " + error);
				}
			}
		}
	}
}

