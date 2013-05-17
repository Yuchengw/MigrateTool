/**
 * This is a upper-level interface program that deals with the xml mapping file
 *
 *
 *
 */

package com.salesforce.ui;


import com.salesforce.ui.Runner;
import com.salesforce.service.QueryBean;

import java.io.File;
import java.io.*;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class QueryParser{

	private QueryBean qb;
	
	public QueryParser(){
		System.out.println("********************Start Query Parsing*********************");	
		qb = new QueryBean();
	}

	/**
     * Function that check if config file exists
     *
     *
     */
	public QueryBean parse() throws FileNotFoundException{
		File queryFile = new File("query.xml");
		if(!queryFile.exists()){
			throw new FileNotFoundException("config.xml is not in current directory");
		}
		return parseImpl(queryFile);
	}
		
	/**
     * Implementation function that parse xml file construct query bean
     *
     * @param File
	 * @return MappingBean 
     */
	public QueryBean parseImpl(File queryfile){
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(queryfile);
			doc.getDocumentElement().normalize();
			System.out.println("##############################" + doc.getDocumentElement().getNodeName());
			qb.setRoot(doc.getDocumentElement().getNodeName());
			setQueryOrg(qb,doc);
		}catch(Exception ex){
			ex.printStackTrace();
		}
			return qb;
	}
	/**
     * function that get information from config file to qury bean
     *  
     * @param QueryBean
     * @param Document doc
     * @return void
     */
	public void setQueryOrg(QueryBean bean, Document doc){
		NodeList nodes = doc.getElementsByTagName("queryorg").item(0).getChildNodes();				
		ArrayList<String> qblist = bean.getQueryList();
		Node an;	
		for(int i = 0; i < nodes.getLength(); i++){
			Node node = nodes.item(i);
			if(node.getNodeType() == Node.ELEMENT_NODE){
				NodeList nl2 = node.getChildNodes();
				for(int k = 0; k < nl2.getLength(); k++){
					an = nl2.item(k);
					if(node.getNodeName() == "qusername"){
						bean.setQueryOrgUserName(an.getNodeValue());
					}else if(node.getNodeName() == "quserpassword"){
						bean.setQueryOrgPassword(an.getNodeValue());	
					}else if(node.getNodeName() == "qobject"){
						bean.setQueryOrgObject(an.getNodeValue());
					}else if(node.getNodeName() == "where"){
						bean.setQueryOrgWhere(an.getNodeValue());
					}else{
						qblist.add(an.getNodeValue());
					}
				}
			}	
		}
	}
 
	public QueryBean getQueryBean(){
		return qb;
	}
}
