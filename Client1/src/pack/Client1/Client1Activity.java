package pack.Client1;

import android.app.Activity;
//import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.*;
import java.net.*;




public class Client1Activity extends Activity {
    
	
	Socket ClientSoc;
	DataInputStream in = null;
	DataOutputStream out = null;
	
	TextView viewtext;
	EditText edittext;
	EditText edittext2;
	EditText edittext3;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        viewtext= (TextView)findViewById(R.id.Connection_accomplished);
        edittext= (EditText)findViewById(R.id.editText1);
        edittext2= (EditText)findViewById(R.id.editText2);
        edittext3= (EditText)findViewById(R.id.editText3);
        
        Button button = (Button)findViewById(R.id.button1);
        
        
        button.setOnClickListener(new View.OnClickListener() { // we define what happens when we click the button
			
			public void onClick(View v) {
				
		    try{
	            ClientSoc = new Socket("192.168.1.63",4455);
	            System.out.println("");
	            out = new DataOutputStream(ClientSoc.getOutputStream());
	            in = new DataInputStream(ClientSoc.getInputStream());
	            
	            
	        }catch(IOException io){ 
	        	
	        	System.out.println("Not connected with server");
	            System.exit(1);	
	        }
	        
	        
	    	
			
			
			try{
			    out.writeUTF("Client");
			    
			    String message=edittext.getText().toString();
			    message=message.concat(" ");
			    message=message.concat(edittext2.getText().toString());
			    message=message.concat(" ");
			    message=message.concat(edittext3.getText().toString());
				System.out.println(message);
				
				out.writeUTF(message);
				
				String income= in.readUTF();
				
				viewtext.setText(income);
			}catch(IOException io){
				System.out.println("not sent");
				System.exit(2);
			} /*catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
			
			try{
				in.close();
				out.close();
				ClientSoc.close();
			}catch(IOException io){ 
				System.out.println("unsuccesful Termination");
				System.exit(3);
			}
			      
			         
			}
			
		}); // end of click definition
        
      
        
    }// onCreate

        
}
