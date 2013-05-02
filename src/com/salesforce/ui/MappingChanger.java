/*
 * This is another upper-level mapping exchanger for change the mapping fields name for sobject
 *
 *
 *
 *
 */

package com.salesforce.ui;


import com.salesforce.ui.Runner;
import com.salesforce.service.MappingBean;

import java.io.File;
import java.io.*;
import java.util.ArrayList;


public class MappingChanger{

	private MappingBean mb;
	private ArrayList<String> fromorglist;	
	private ArrayList<String> toorglist;

	public MappingChanger(){
		System.out.println("***************Start Chang*******************");
	}

	/**
     * Function that check if mapping file exsit, and start changing 
     * @param filename for change
	 * @param mappingbean
     * @return void
     * @throws FileNotFoundException
     * @throws IOException
     */
	public void change(String filename, MappingBean mb) throws FileNotFoundException, IOException{
		File changingfile = new File(filename);
		if(!changingfile.exists()){
			throw new FileNotFoundException("****" + filename + "**** is not found");
		}
		if(mb == null){
			System.out.println("MappingBean is Null");
			return;
		}
		fromorglist = (ArrayList<String>)mb.getFromList().clone();
		toorglist   = (ArrayList<String>)mb.getToList().clone();
		changeImpl(changingfile, filename, fromorglist, toorglist);
	}
	/**
     * Function that implements the core function that change exsiting csv file 
	 * @param filehandler
     * @param filename 
     * @param fromorglist
	 * @param toorglist
	 * @throws IOException
     * @return void
     */
	public void changeImpl(File infile,String filename, ArrayList<String> fromorglist, ArrayList<String> toorglist) throws IOException{
		FileInputStream instream = new FileInputStream(infile);
		BufferedReader rdr = new BufferedReader(new InputStreamReader(instream));
		File outfile = new File(filename + ".tmp");
		FileOutputStream outstream = new FileOutputStream(outfile);	
		StringBuilder fieldsname = new StringBuilder();
		ArrayList<String> checklist = new ArrayList<String>();
		// yeah, it could be a buggy place, let's see 
		for(int i = 0; i < 1; i++){
			fieldsname.append(rdr.readLine());
		}
		String[] line = fieldsname.toString().split(",");
		for(int i = 0; i < line.length; i++){
			System.out.println("  " + line[i]);
			checklist.add(line[i]);	
		}
		String newline = null;
		if(isEqual(checklist,fromorglist)){
			newline = createCSV(checklist);
		}
		String strline = null;
		outstream.write(newline.getBytes("UTF-8"));
		while((strline = rdr.readLine()) != null){
			outstream.write(strline.getBytes("UTF-8"));
		}
		instream.close();
		outstream.flush();
		outstream.close();

		if(!infile.delete() || !outfile.renameTo(infile)){
			System.out.println("INFO:++++++++++++++++++++++File operation error");
		}
	}
	/**
     * a small helper function that check if two arraylist are equal
     * @param list1 
     * @param list2
	 * @return boolean 
     *
     */
	public boolean isEqual(ArrayList<String> list1, ArrayList<String> list2){
		if(list1 == null || list2 == null || list1.size() != list2.size()){
			return false;
		}
		for(int i = 0; i < list1.size(); i++){
			if(!list1.get(i).equals(list2.get(i))) return false;
		}		
		return true;	
	}
	/**
     * another helper function that convert a string array to a csv line
     * @param arraylist of string
     * @return csv string
     *
     */
	public String createCSV(ArrayList<String> list){
	   String newstr = null;
	   for(int i = 0; i< list.size(); i++){
			if( ++i == list.size()){
				newstr = newstr + list.get(i) + "\n";	
				break;
			}
			newstr = newstr + list.get(i) + ",";	
	   }
	   return newstr;
	}		

} 

