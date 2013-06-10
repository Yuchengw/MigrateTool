/**
 * This is the upper-level interface program that deals with the xml mapping file
 *
 *
 * Author yucheng wang
 * Date 04/30/2013
 * 
 */

package com.salesforce.ui;


import com.salesforce.ui.Runner;
import com.salesforce.service.MappingBean;
import com.salesforce.service.lib.log.SuperLog;

import java.io.File;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class MappingParser{

	private MappingBean mb;
	private Logger LOGGER = null;

	public MappingParser(){
		//for future multi-thread development
		mb = new MappingBean();
	}	

	public MappingParser(String loglevel){
		LOGGER = SuperLog.open(MappingParser.class,loglevel);
		LOGGER.info("***************************Start Mapping***************************");
		mb = new MappingBean();
	}
	
	/**
     * Function that check if config file exists
     *
     */
	public MappingBean parse() throws FileNotFoundException{
		File mappingfile = new File("mapping.xml");
		if(!mappingfile.exists()){
			LOGGER.severe("mapping.xml is not in product directory.");
			throw new FileNotFoundException("mapping.xml is not in current directory");	
		}
		return parseImpl(mappingfile);
	}

	/**
     * Implementation Function that parse xml file construct mapping Bean
     *
     * @param File 
     * @return MappingBean
     */
	public MappingBean parseImpl(File mappingfile){
		try{
		    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		    Document doc = dBuilder.parse(mappingfile);
		    doc.getDocumentElement().normalize();
		    mb.setRoot(doc.getDocumentElement().getNodeName());
		    //Extract vlaues from mapping file
		    setFromOrg(mb,doc);
		    setToOrg(mb,doc);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		    return mb;
	}
	
	public void setFromOrg(MappingBean mb, Document doc){
		NodeList nodes = doc.getElementsByTagName("fromorg").item(0).getChildNodes();
		ArrayList<String> mbfromlist = mb.getFromList();
		Node an;
		for( int i = 0; i < nodes.getLength(); i++){		
			Node node = nodes.item(i);
		    if(node.getNodeType() == Node.ELEMENT_NODE){
				NodeList nl2 = node.getChildNodes();
				for(int k = 0; k < nl2.getLength(); k++){
					an = nl2.item(k);
					if(node.getNodeName() == "fusername"){
						mb.setFromOrgUserName(an.getNodeValue());
					}else if(node.getNodeName() == "fuserpassword"){
						mb.setFromOrgUserPassword(an.getNodeValue());
					}else if(node.getNodeName() == "fobject"){
						mb.setFromOrgObject(an.getNodeValue());
					}else if(node.getNodeName() == "where"){
						mb.setFromOrgWhere(an.getNodeValue());
					}else{
		    	   	 	mbfromlist.add(an.getNodeValue());
					}
				}
		    }
    	 }	
	}

	public void setToOrg(MappingBean mb, Document doc){
		NodeList nodes = doc.getElementsByTagName("toorg").item(0).getChildNodes();
		ArrayList<String> mbtolist = mb.getToList();	
		Node an;
		if( mbtolist == null) {
			//System.out.println("to list is null");
			return;
		}
		for(int i = 0; i < nodes.getLength(); i++){
			Node node = nodes.item(i);
			if(node.getNodeType() == Node.ELEMENT_NODE){
				NodeList nl2 = node.getChildNodes();
				for( int k = 0; k <nl2.getLength();k++){	
					 an = nl2.item(k);
				     if(node.getNodeName() == "tusername"){
				     	mb.setToOrgUserName(an.getNodeValue());
				     }else if(node.getNodeName() == "tuserpassword"){
				     	mb.setToOrgUserPassword(an.getNodeValue());
					 }else if(node.getNodeName() == "tobject"){
						mb.setToOrgObject(an.getNodeValue());
					 }else if(node.getNodeName() == "externalId"){
						mb.setToOrgExternalId(an.getNodeValue());
				     }else{
				     	mbtolist.add(an.getNodeValue());
				     }
				}
			}
		}
	}
	
	public MappingBean getMappingBean(){
		return mb;
	}	
	
}
	

