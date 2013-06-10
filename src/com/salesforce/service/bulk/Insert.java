/** This is a simple bulkapi for insert operaition for migration tool.
 *
 * @author yucheng.wang
 * @since 04/13/2013
 */ 

package com.salesforce.service.bulk;


import java.io.*;
import java.util.*;
import java.util.logging.Logger;

import com.sforce.async.*;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

import com.salesforce.service.lib.SuperLog;

public class Insert{
		
	private String Logger LOGGER = null;	
	public Insert(){
	}

	public Insert(String loglevel){
		LOGGER = SuperLog.open(Insert.class,loglevel);	
	}
	/** 
	 * function that creates a BULK API job and uploads batches for a CSV file, TODO: should also create a XML file version

	 * @param sobjectType, userName, user password,  target csv file name
	 * @return void
   * @throws AsyncApiException, ConnectionException, IOException
	 *
   */	
	public void runCSV(String sobjectType, String userName, String password, String csvFileName)
				throws AsyncApiException, ConnectionException, IOException{
		// log in process	
		BulkConnection connection = getBulkConnection(userName, password);			
		// create batch job
		JobInfo job = createJob(sobjectType, connection);
		// get batch job info
		List<BatchInfo>  batchInfoList = createBatchesFromCSVFile(connection, job, csvFileName);
		// close job
		closeJob(connection, job.getId());
		awaitCompletion(connection,job,batchInfoList);
		// check result
		//checkResults(connection, job, batchInfoList);
	}
	
	/**
	 * Create the BulkConnection used to call web service 
   * @param  username;userpassword
   * @return BulkConnection
	 * @throws ConnectionException;AsyncApiException
   *
   */
	public BulkConnection getBulkConnection(String username, String password) 
								throws ConnectionException, AsyncApiException{
		ConnectorConfig partnerConfig = new ConnectorConfig();	

		partnerConfig.setUsername(username);
		partnerConfig.setPassword(password);
		partnerConfig.setAuthEndpoint("https://login.salesforce.com/services/Soap/u/27.0");
	
		//creating connection using user name and user password and stores session id in partnerConfig
		new PartnerConnection(partnerConfig);	
		//after successful log in, use valid session stored in the ConnectConfig instance to initialize a 		  //a BulkConnection
		ConnectorConfig config = new ConnectorConfig();
		config.setSessionId(partnerConfig.getSessionId());
		//construct the valid SOAP uri for bulk operation by using partnerConfig
		String soapEndpoint = partnerConfig.getServiceEndpoint();	
		String apiVersion = "27.0";
		String restEndpoint = soapEndpoint.substring(0, soapEndpoint.indexOf("Soap/"))  
							  + "async/" + apiVersion;
		config.setRestEndpoint(restEndpoint);	
		//This should only be false when doing debugging
		config.setCompression(true);
		//Set this to true to see HTTP requests and responses on stdout
		config.setTraceMessage(true);
		//Use Rest config to establish BULK connection	
		BulkConnection connection = new BulkConnection(config);	
		return connection;
	} 

	/**
     * Create a new job using Bulk API
     * @param sobject;bulkconnection,
     * @return JobInfo for the new job
	   * @throws AsyncApiException
     *
     */
	private JobInfo createJob(String sobjectType, BulkConnection connection)
													throws AsyncApiException{
		JobInfo job = new JobInfo();
		job.setObject(sobjectType);
		// chose operation 
		job.setOperation(OperationEnum.insert);
		job.setContentType(ContentType.CSV);
		job.setExternalIdFieldName("Ext_ID_c");
		job = connection.createJob(job);
		System.out.println(job);
		return job;
	}	

	/**
     * Create and upload batches using a CSV file
	 * The file into the appropriate size batch files (1,000 to 10,000 is reasonable)
	 * @param connection to set up bulk connection
	 * @param jobInfo job associated with new batches
     * @param csvFileNamea The source CSV file for batch data
	 * @return bacthInfos for the job
     * @throws IOException, AsyncApiException
     */		
	private List<BatchInfo> createBatchesFromCSVFile(BulkConnection connection, JobInfo jobInfo, 
				String csvFileName) throws IOException, AsyncApiException{
	
		List<BatchInfo> batchInfos = new ArrayList<BatchInfo>();			
		BufferedReader rdr = new BufferedReader(new InputStreamReader(new FileInputStream(csvFileName)));
		
		//read CSV header row, for mapping
		byte[] headerBytes = (rdr.readLine() + "\n").getBytes("UTF-8");
		File tmpFile = File.createTempFile("bulkAPIInsert", ".csv");
			
		//split the batches records
		split(rdr, tmpFile,headerBytes,batchInfos,connection,jobInfo);
		return batchInfos;			
	}

	/**
     * Split the batch records into small batches for better performance. 
     * @param  Buffered Original file, Temp File
     * @return void
	 * 
     */
	private void split(BufferedReader rdr, File tmpFile, byte[] headerBytes, 
									List<BatchInfo> batchInfos, BulkConnection connection,
									JobInfo jobInfo)
									 throws IOException, AsyncApiException{
    try{
		FileOutputStream tmpOut =  new FileOutputStream(tmpFile);
		int maxBytesPerBatch = 10000000; //10 millions bytes per batch 
		int maxRowsPerBatch = 10000;//10 thousands rows per batch
		int currentBytes = 0;
		int currentLines = 0;
		String nextLine;
	
		while((nextLine = rdr.readLine()) != null){
			byte[] bytes = (nextLine + "\n").getBytes("UTF-8");
			
			//Create new batch when our batch size limit is reached
			if(currentBytes + bytes.length > maxBytesPerBatch || currentLines > maxRowsPerBatch){
				createBatch(tmpOut, tmpFile, batchInfos, connection, jobInfo);
		        currentBytes = 0;
				currentLines = 0;	
			}
			// create new batch file
			if(currentBytes == 0){
				tmpOut = new FileOutputStream(tmpFile);
				tmpOut.write(headerBytes);
				currentBytes = headerBytes.length;
				currentLines = 1;
			}	
			tmpOut.write(bytes);
			currentBytes += bytes.length;
			currentLines++;
		}
		//dealing with any remaining date 
		if(currentLines > 1){
			createBatch(tmpOut, tmpFile, batchInfos, connection, jobInfo);
		}
	  }finally{
	   	    tmpFile.delete();
	   }	
	}	
	
	
	/**
     * Create a batch by uploading file in the local machine, This close the output Stream
     *
     * @param tmpOut 
	 *            The output stream used to write the file data for a single batch
	 * @param tmpFile
	 *            The file associated with the above stream
     * @param batchInfos
	 *            The batch info for the newly created batch is added to this list
	 * @param connection
	 *            The BulkConnection used to create the new batch
	 * @param jobInfo
     *            The JobInfo associated with the new batch
     *
     * @return void
	 * @throws IOException, AynsApiException
     */
	private void createBatch(FileOutputStream tmpOut, File tmpFile, List<BatchInfo> batchInfos, 
							 BulkConnection connection, JobInfo jobInfo) 
							 throws IOException, AsyncApiException{
		tmpOut.flush();
		tmpOut.close();
		FileInputStream tmpInputStream = new FileInputStream(tmpFile);
		try{
			BatchInfo batchInfo = connection.createBatchFromStream(jobInfo, tmpInputStream);
			System.out.println(batchInfo);
			batchInfos.add(batchInfo);
		} finally{
			tmpInputStream.close();
		}
	}

	/**
     * After all batches have been added to job, close the job
     * @param Bulk Connection, JobId
     * @return void
     * @throws AsyncApiException
     *
     */
	private void closeJob(BulkConnection connection, String jobId)
										throws AsyncApiException{
		JobInfo job = new JobInfo();
		job.setId(jobId);	
		job.setState(JobStateEnum.Closed);
		connection.updateJob(job);
	}

	/**
     * A batch may take some time to complete depending on the size of the data set. 
     * @param BulkConnection, Job Information, BatchInfo List
	 * @return void
	 * @throws AsyncApiException
     *
     */
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
			System.out.println("Awaiting results... " + incomplete.size());
			sleepTime = 10000L;
			BatchInfo[] statusList = connection.getBatchInfoList(job.getId()).getBatchInfo();
			for(BatchInfo b : statusList){
				if(b.getState()  == BatchStateEnum.Completed || b.getState() == 
														BatchStateEnum.Failed){
					if(incomplete.remove(b.getId())){
						System.out.println("Batch STATUS:\n" + b);
					}
				}
			}
		}
	}																		
		
	/**
     * Get the resutls  of the operation and checks for errors
     * @param BulkConnection, JobInfo, List of BatchInfos
     * @return void
     * @throws AsyncApiException, IOException
     *
     */
	private  void checkResults(BulkConnection connection, JobInfo job, List<BatchInfo> batchInfoList)
										throws AsyncApiException, IOException{
		for(BatchInfo b : batchInfoList){
			CSVReader rdr = new CSVReader(connection.getBatchResultStream(job.getId(), b.getId()));
			List<String> resultHeader = rdr.nextRecord();
			int resultCols = resultHeader.size();

			List<String> row;
			while((row = rdr.nextRecord()) != null){
				Map<String,String> resultInfo = new HashMap<String,String>();
				for(int i = 0; i < resultCols; i++){
					resultInfo.put(resultHeader.get(i), row.get(i));
				}
				boolean success = Boolean.valueOf(resultInfo.get("Success"));
				boolean created = Boolean.valueOf(resultInfo.get("Created"));
				String id = resultInfo.get("Id");
				String error = resultInfo.get("Error");
				if(success && created){
					System.out.println("Created row with id " + id);
				}else if(!success){
					System.out.println("Failed with error " + error);
				}
			}
		}
	}
} 
















