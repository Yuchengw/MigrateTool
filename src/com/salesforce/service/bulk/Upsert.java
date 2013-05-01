/** This is simple bulk api wrapper class for upsert opeartion for migration tool.
 *
 * @author yucheng.wang
 * @since 04/30/2013
 */

package com.salesforce.service.bulk;


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
	public BulkConnection getBulkConnection(String username, String password) throws ConnectionException, AsyncApiException{

		ConnectorConfig partnerConfig = new ConnectorConfig();
		partnerConfig.setUsername(username);		
		partnerConfig.setPassword(password);
		partnerConfig.setAuthEndpoint("https://login.salesforce.com/services/Soap/u/27.0");
				
		// create login session
		new PartnerConnection(partnerConfig);
		ConnectorConfig config = new ConnectorConfig();
		config.setSessionId(partnerConfig.getSessionId());
		// construct the valid SOAP uri for bulk operation by usering partnerConfig
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
     * Create a new upsert job using bulk api
     * @param sobject
	 * @param bulkconnection
	 * @return jobinfo
	 * @throws AsyncApiException
     *
     */
	 private JobInfo createJob(String sobjectType, BulkConnection connection) throws AsyncApiException{
		JobInfo job = new JobInfo();
		job.setObject(sobjectType);
		job.setOperation(OperationEnum.upsert);
		job.setContentType(ContentType.CSV);
		job.setExternalIdFieldName("First__c");
		job = connection.createJob(job);
		System.out.println(job);
		return job;
	}

	/**
     * Create and upsert batches using a CSV file
     * split the file into appropriate batch size (1,000 to 10,000 is reasonable)
	 * @param connection 
     * @param jobInfo
	 * @param csvFileName
	 * @return batchInfos
     * @throws IOException
     * @throws AsyncApiException
     *
     */
	private List<BatchInfo> createBatchesFromCSVFile(BulkConnection connection, JobInfo jobInfo, String csvFileName) throws IOException, AsyncApiException{
		
		List<BatchInfo> batchInfos = new ArrayList<BatchInfo>();
		BufferedReader rdr = new BufferedReader(new InputStreamReader(new FileInputStream(csvFileName)));
		
		byte[] headerBytes = (rdr.readLine() + "\n").getBytes("UTF-8");
		File tmpFile = File.createTempFile("bulkAPIUpsert", ".csv");
		
		split(rdr, tmpFile, headerBytes,batchInfos, connection,jobInfo);
		return batchInfos;
	}
	/**
     * Split the batch records into small batches for better performance
     * @param buffer 
     * @param headerBytes
     * @param batchInfos	
     * @param connection
     * @param jobinfo
     * @return void
     * @throws IOException
     * @throws AsyncApiException
     */
	 private void split(BufferedReader rdr, File tmpFile, byte[] headerBytes, List<BatchInfo> batchInfos, BulkConnection connection, JobInfo jobInfo) throws IOException, AsyncApiException{
		try{
			FileOutputStream tmpOut = new FileOutputStream(tmpFile);
			int maxBytesPerBatch = 10000000;
			int maxRowsPerBatch = 10000;
			int currentBytes = 0;
			int currentLines = 0;
			String nextLine;
			
			while((nextLine = rdr.readLine()) != null){
				byte[] bytes = (nextLine + "\n").getBytes("UTF-8");
				// create new batch when batch size limit is reached
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
			if(currentLines > 1){
				createBatch(tmpOut, tmpFile, batchInfos, connection, jobInfo);
			}
		}finally{
			tmpFile.delete();
		}
		
	 }
	/**
     * Create a batch by uploading file in the local machine, this also close the output stream
     * @param tmpOUt
     * @param tmpFile
     * @param batchInfos
     * @param batchInfos
     * @param connection
     * @param jobInfo
     * @return void
     * @throws IOException
     * @throws AsyncApiException
     */
	private void createBatch(FileOutputStream tmpOut, File tmpFile, List<BatchInfo> batchInfos, BulkConnection connection, JobInfo jobInfo) throws IOException, AsyncApiException{
		
		tmpOut.flush();
		tmpOut.close();
		FileInputStream tmpInputStream = new FileInputStream(tmpFile);
		try{
			BatchInfo batchInfo = connection.createBatchFromStream(jobInfo,tmpInputStream);
			System.out.println(batchInfo);
			batchInfos.add(batchInfo);
		}finally{
			tmpInputStream.close();
		}
	}
	/**
     * After all batches finish, close the job
     * @param connection
     * @param JobInfo
     * @throws AsyncApiException
     *
     */
	 private void closeJob(BulkConnection connection, String jobId) throws AsyncApiException{
		
		JobInfo job = new JobInfo();
		job.setId(jobId);
		job.setState(JobStateEnum.Closed);
		connection.updateJob(job);
	}
	/**
     * A batch may take some time to complete depending on the size of the data set
	 * @param bonnection
     * @param jobinfo
     * @param batchinfolist
	 * @return void
     * @throws AsyncApiException
     */
	private void awaitCompletion(BulkConnection connection, JobInfo job, List<BatchInfo> batchInfoList) throws AsyncApiException{
		long sleepTime = 0L;
		Set<String> incomplete = new HashSet<String>();	
		for(BatchInfo bi : batchInfoList){
			incomplete.add(bi.getId());
		}
		while(!incomplete.isEmpty()){
			try{
				Thread.sleep(sleepTime);
			}catch(InterruptedException e){
				System.out.println(e);	
            }
			System.out.println("Awating results... " + incomplete.size());	
			sleepTime = 10000L;
			BatchInfo[] statusList = connection.getBatchInfoList(job.getId()).getBatchInfo();
			for(BatchInfo b : statusList){
				if(b.getState() == BatchStateEnum.Completed || b.getState() == BatchStateEnum.Failed){
					if(incomplete.remove(b.getId())){
						System.out.println("Batch Status:\n" + b);
					}
				}
			}
		}
	}
	/**
     * Get the resutls  of the operation and checks for errors
     * @param BulkConnection
	 * @param jobInfo
     * @param BatchInfos
     * @return void
     * @throws AsyncApiException
	 * @throws IOException
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












































