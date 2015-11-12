package findplace.user;



import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.StringTokenizer;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import android.content.Context;
import android.location.Location;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import android.widget.Toast;

public class CustomOnItemSelectedListener implements OnItemSelectedListener {
	MapView map;
	MapController mapcontroller;
	DataOutputStream oos;
	DataInputStream ois;
	MyItemizedOverlay myItemizedOverlay;
	TextView text;
	double lat,lng;
	 public CustomOnItemSelectedListener(MapView map,MyItemizedOverlay myItemizedOverlay,TextView text)
	 {
		 this.map=map;
		 mapcontroller=map.getController();
		 this.myItemizedOverlay=myItemizedOverlay;
		 this.text=text;
		 
	 }
	 public void setStreams(DataOutputStream oos,DataInputStream ois,double lat,double lng)
	 {
		 this.oos=oos;
		 this.ois=ois;
		 this.lat=lat;
		 this.lng=lng;
	 }
	 public void setLocation(double lat,double lng)
	 {
		 this.lat=lat;
		 this.lng=lng;
	 }

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
		// TODO Auto-generated method stub
		
	if(pos!=0){	
		String search=parent.getItemAtPosition(pos).toString();
		Toast.makeText(parent.getContext(), 
				"OnItemSelectedListener : " +search ,
				Toast.LENGTH_SHORT).show();
		String query=search+" "+lat+" "+lng;
		try {
			send(query,parent.getContext());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	public void send(String obj,Context cont) throws Exception
    {
    	oos.writeUTF(obj);
    	String result=ois.readUTF();
    	StringTokenizer st = new StringTokenizer(result, ",");
   	 int i=0;
   	 String name = null;
   	 double lat = 0,lng = 0,distance = 0;
		while (st.hasMoreElements()) {
			if (i==0) name=st.nextElement().toString();
			if (i==1) distance=Double.parseDouble((String) st.nextElement());
			if (i==2) lat=Double.parseDouble((String) st.nextElement());
			if (i==3) lng=Double.parseDouble((String) st.nextElement());
			i++;
		}
		animateToResult(lat,lng,name,name);
		text.setText("Distance: "+distance);
		oos.flush();
    }
	public void animateToResult(double lat,double lng,String name,String descr)
	{
		GeoPoint p = new GeoPoint(
                (int) (lat * 1E6), 
                (int) (lng * 1E6));
        mapcontroller.animateTo(p);
        addPush(p,name,descr,"result");
        map.invalidate();
	}
	public void addPush(GeoPoint point,String name,String descr,String type)
	{
		 myItemizedOverlay.addItem(point,name,descr);
	}
	

}
