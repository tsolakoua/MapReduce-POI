package pack.ReduceWorker;

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


public class ReduceWorkerActivity extends Activity{
	
	  Socket WorkerSoc=null;
	  
	  DataInputStream in = null;
	  DataOutputStream out = null;
	
	  TextView viewtext;
	  TextView viewtext2;
	  
	  
	  
	  
	 
	  
	  public void onCreate(Bundle savedInstanceState) {
	    	
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.main);
	        

	        
	 ////////////////////         Connection   management        //////////////////////////////////

	        
	        
	        Thread myThread = new Thread(){
	       // button.setOnClickListener(new View.OnClickListener() { // we define what happens when we click the button
				
				//public void onClick(View v) {
				public void run(){
			    try{
		            WorkerSoc = new Socket("10.15.5.141",4455);
		            System.out.println("");
		            out = new DataOutputStream(WorkerSoc.getOutputStream());
		            in = new DataInputStream(WorkerSoc.getInputStream());
		            
		            
		        }catch(IOException io){ 
		        	
		        	System.out.println("Not connected with server");
		            System.exit(1);	
		        }
		        
		        
		    	
				// ..................          ..........................................
				
				try{
				    out.writeUTF("ReduceWorker"); // type of this emulator!!!!
				
					out.writeUTF("I can't write a place");
					
					while(true){

					    String message= in.readUTF();
					    System.out.println(message);
										  
					    System.out.println("reduce worker works well");
					    
					    String answer ="";
//////////////////////////     In this place starts the algorithm for the REDUCE function  /////////////////////					    
					   StringTokenizer tok = new StringTokenizer(message,"@");
					   
					   String name=tok.nextToken();
					   double distance = Double.parseDouble(tok.nextToken());
					   double x = Double.parseDouble(tok.nextToken());
					   double y = Double.parseDouble(tok.nextToken());
					   
					   while(tok.hasMoreTokens()){
						   
						   String name2 =tok.nextToken();
						   double distance2 = Double.parseDouble(tok.nextToken());
						   double x2 = Double.parseDouble(tok.nextToken());
						   double y2 = Double.parseDouble(tok.nextToken());
						   if(distance2<distance){
							   distance=distance2;
							   name=name2;
							   x=x2;
							   y=y2;
						   }
					   }
					    
					  
					    
//////////////////////////////////////// End of reduce ////////////////////////////////////////////////////////////
					
			           answer=answer.concat(name);
			           answer=answer.concat(",");
			           answer=answer.concat(Double.toString(distance));
			           answer=answer.concat(",");
			           answer=answer.concat(Double.toString(y)); 
			           answer=answer.concat(",");
			           answer=answer.concat(Double.toString(y)); 
					    out.writeUTF(answer);
					}
				}catch(IOException io){
					System.out.println("not sent");
					System.exit(2);
				} 
				
				// ...................        ..............................................
				try{
					in.close();
					out.close();
					WorkerSoc.close();
				}catch(IOException io){ 
					System.out.println("unsuccesful Termination");
					System.exit(3);
				}
				      
				      
			}//run
				
		
	       };//thread
	 ///////////////////////////////////////////////////////////////////////////////////////////////      
	        myThread.start();
	  }//onCreate
	  

		  
		 
	  
	
	
}
