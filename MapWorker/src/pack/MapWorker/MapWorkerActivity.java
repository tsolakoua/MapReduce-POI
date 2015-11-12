package pack.MapWorker;

import android.app.Activity;
import android.content.Intent;
//import android.media.MediaPlayer;
import android.os.Bundle;

//import java.net.*;


public class MapWorkerActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main2);
        
     
        
        Thread myThread = new Thread(){
        	
          public void run(){
        	try{
        		int timer=0;
        		while(timer<5000){
        			sleep(400);
        			timer=timer+500;
        		}
        		
        		// go to my Activity2.java
        		startActivity(new Intent("pack.MapWorker.CLEARSCREEN")); 
        		
        	}
        	catch(InterruptedException e){
        		e.printStackTrace();
          	}
        	
          }//run
           
       
        };
        
        myThread.start();
        
    }//onCreate

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
    
    
}//MapWorkerActivity