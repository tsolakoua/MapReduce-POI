package pack.MapWorker;

import android.app.Activity;
//import android.content.Intent;
import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.*;
import java.lang.Math.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



import java.util.*;

import java.net.*;


public class Activity2 extends Activity{
	
	  Socket WorkerSoc=null;
	  
	  DataInputStream in = null;
	  DataOutputStream out = null;
	
	  TextView viewtext;
	  TextView viewtext2;
	  
	  private List <Place> elements;
	  
	  String clientPlace;
	  double x;
	  double y;
	  
	  public void onCreate(Bundle savedInstanceState) {
	    	
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.main);
	        

	        
	 ////////////////////         Connection   management        //////////////////////////////////
	        viewtext= (TextView)findViewById(R.id.Viewtext);
	        viewtext2= (TextView)findViewById(R.id.Viewtext2);
	        
	        
	        
	        
	        Button button = (Button)findViewById(R.id.button1);
	        
	        Thread myThread = new Thread(){
	       // button.setOnClickListener(new View.OnClickListener() { // we define what happens when we click the button
				
				//public void onClick(View v) {
				public void run(){
			    try{
		            WorkerSoc = new Socket("192.168.83.1",3333);
		            System.out.println("");
		            out = new DataOutputStream(WorkerSoc.getOutputStream());
		            in = new DataInputStream(WorkerSoc.getInputStream());
		            
		            
		        }catch(IOException io){ 
		        	
		        	System.out.println("Not connected with server");
		            System.exit(1);	
		        }
		        
		        
		    	
				// ..................          ..........................................
				
				try{
				    out.writeUTF("MapWorker"); // type of this emulator!!!!
				
					out.writeUTF("I can't write a place");
					
					while(true){
					    String FileName= in.readUTF();
					    System.out.println(FileName);
					
					    String message= in.readUTF();
					    System.out.println(message);
					    
					    // map procedure takes place 
					    
					    readKMLFIle(FileName);
					    MessageAnalysis(message);
					    
					    
					    
//////////////////////////     In this place starts the algorithm for the MAP function  /////////////////////					    
					    double distance=10000; // a random distance!!!! 
					    int position=-1;
					    for(int i=0;i<elements.size();i++){ // we find the place with type=clientplace and the smallest  distance
					    	
					    	if(clientPlace.compareTo(elements.get(i).getType())==0){
					    	    double distance2 = Math.abs(x-elements.get(i).getLatitude())+Math.abs(y-elements.get(i).getLongtitude());
					            if(distance2<distance){
					            	distance=distance2;
					            	position=i;
					            }
					            
					    	}
					    }
//////////////////////////////////////// End of map ////////////////////////////////////////////////////////////
					    /*
					     *  now we have the best choice for our client in position i of elements(list) ,
					     *  with the distance computed.
					     */
					    
					    // WE 'll  sent to the server our answer in the String form:  "place_name distance"
					    
					    String answer = elements.get(position).getName();
					    answer = answer.concat("@");
					    answer = answer.concat(Double.toString(distance));
					    answer = answer.concat("@");
					    answer = answer.concat(Double.toString(elements.get(position).getLatitude()));
					    answer = answer.concat("@");
					    answer = answer.concat(Double.toString(elements.get(position).getLongtitude()));
					    out.writeUTF(answer);
					}
				}catch(IOException io){
					System.out.println("not sent");
					System.exit(2);
				} /*catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				
				// ...................        ..............................................
				try{
					in.close();
					out.close();
					WorkerSoc.close();
				}catch(IOException io){ 
					System.out.println("unsuccesful Termination");
					System.exit(3);
				}
				      
				         
				//}//onClick
				}//run
				
			//}); // end of click definition
	       };//thread
	 ///////////////////////////////////////////////////////////////////////////////////////////////      
	        myThread.start();
	  }//onCreate
	  
	  public void MessageAnalysis(String message){
		  
		  StringTokenizer toks = new StringTokenizer(message);
		  
		  clientPlace = toks.nextToken();
		  x = Double.parseDouble(toks.nextToken());
		  y = Double.parseDouble(toks.nextToken());
	  }
	
	 public void readKMLFIle(String name){
		 
		 //////////////////// Reading place1.kml  //////////////////////////////////
	        try {
	        	
	            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	            DocumentBuilder db = dbf.newDocumentBuilder();
	            
	            Document doc = db.parse(getAssets().open(name));  ///////////////// BREAKPOINT
	            
	            doc.getDocumentElement().normalize();

	            NodeList nodeLst = doc.getElementsByTagName("Placemark");
	            int size = nodeLst.getLength();
	            Place[] results = new Place[size];


	            for (int s = 0; s < nodeLst.getLength(); s++) {
	               

	                Node fstNode = nodeLst.item(s);
	         

	                if (fstNode.getNodeType() == Node.ELEMENT_NODE) {


	                    Element fstElmnt = (Element) fstNode;
	                    NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("name");
	                    Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
	                    NodeList fstNm = fstNmElmnt.getChildNodes();
	              
	                    String title = ((Node) fstNm.item(0)).getNodeValue();
	          
	                    NodeList lstNmElmntLst = fstElmnt.getElementsByTagName("coordinates");
	                    Element lstNmElmnt = (Element) lstNmElmntLst.item(0);
	                    NodeList lstNm = lstNmElmnt.getChildNodes();
	          
	                    String body = ((Node) lstNm.item(0)).getNodeValue();
	                    results[s] = new Place(title, body);
	                }

	            }
	            elements = new  ArrayList(); 
	            for (int i = 0; i < results.length; i++) {
	               elements.add(results[i]);


	            }
	   
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	 }

}
