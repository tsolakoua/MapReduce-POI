package findplace.user;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import android.R.drawable;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class FindPlaceActivity extends MapActivity implements OnClickListener{
	private MapView map;
	private MapController mapcontroller;
	private String provider;
	private Location location;
	private LocationManager locationManager;
	private TextView text;
	private Button search;
	private EditText editText;
	private Spinner spinner1;
	 CustomOnItemSelectedListener onitemlistener;
	MyItemizedOverlay myItemizedOverlay;
	Socket socket = null;
    DataOutputStream oos = null;
    DataInputStream ois = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
         text=(TextView) findViewById(R.id.textView1);
         search=(Button) findViewById(R.id.button1);
         search.setOnClickListener(this);
         editText=(EditText) findViewById(R.id.editText1);
         spinner1 = (Spinner) findViewById(R.id.spinner1);
       
        initializeMap();
       // text.setText("created");
        try {
			connect("10.15.5.141",4455);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}                            
    } 

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	public void initializeMap()
	{
		map=(MapView) findViewById(R.id.mapview);
        map.setBuiltInZoomControls(true);
        mapcontroller=map.getController();
        mapcontroller.setZoom(12);
        map.displayZoomControls(true);
        provider=LocationManager.GPS_PROVIDER;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		location = locationManager.getLastKnownLocation(provider);
		// The minimum time (in miliseconds) the system will wait until checking if the location changed
		int minTime = 600;
		// The minimum distance (in meters) traveled until you will be notified
		float minDistance = 15;
		// Create a new instance of the location listener
		
/*		Criteria criteria = new Criteria();
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setSpeedRequired(false);
		// Get the best provider from the criteria specified, and false to say it can turn the provider on if it isn't already
		String bestProvider = locationManager.getBestProvider(criteria, false);          */
		// Request location updates
		if(location!=null)  
    	 mapcontroller.animateTo(new GeoPoint((int)(location.getLatitude()* 1E6),(int)(location.getLongitude()*1E6)));
		//////////////////////////////////////////////
		 Drawable marker=getResources().getDrawable(android.R.drawable.star_big_on);
		 int markerWidth = marker.getIntrinsicWidth();
	     int markerHeight = marker.getIntrinsicHeight();
	     marker.setBounds(0, markerHeight, markerWidth, 0);
	     myItemizedOverlay = new MyItemizedOverlay(marker,this);
	     map.getOverlays().add(myItemizedOverlay);
/*	     GeoPoint myPoint1 = new GeoPoint(0*1000000, 0*1000000);
	     myItemizedOverlay.addItem(myPoint1, "myPoint1", "myPoint1");
	     GeoPoint myPoint2 = new GeoPoint(50*1000000, 50*1000000);
	     myItemizedOverlay.addItem(myPoint2, "myPoint2", "myPoint2");

*/		
	     onitemlistener= new CustomOnItemSelectedListener(map,myItemizedOverlay,text);
	     spinner1.setOnItemSelectedListener(onitemlistener);
	     MyLocationListener myLocListener = new MyLocationListener(map,onitemlistener);
		locationManager.requestLocationUpdates(provider, minTime, minDistance, myLocListener);

	}
	public void findLocation(String locationName)
	{
		text.setText("searching for: "+locationName);
		Geocoder geoCoder = new Geocoder(this);  
        try {
            List<Address> addresses = geoCoder.getFromLocationName(locationName, 5);
            if (addresses.size() > 0) {
            	animateToResult(addresses.get(0).getLatitude(),addresses.get(0).getLongitude(),addresses.get(0).getFeatureName(),"");
            }    
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERROR");
        }
		
	}
	public void connect(String ip,int port) throws Exception
    {
	
    	socket=new Socket(ip,port);
    	
    	oos = new DataOutputStream(socket.getOutputStream());
    	ois = new DataInputStream(socket.getInputStream());
		onitemlistener.setStreams(oos,ois,location.getLatitude(),location.getLongitude());
		
	}
    public void send(String obj) throws Exception
    {
    	oos.writeUTF(obj);
    	String result =ois.readUTF();
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

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if( search.getId() == ((Button)arg0).getId() ){
				//findLocation(editText.getText().toString());
			findLocation(editText.getText().toString());
		}
	}
	public void animateTo(double lat,double lng)
	{
		GeoPoint p = new GeoPoint(
                (int) (lat * 1E6), 
                (int) (lng * 1E6));
        mapcontroller.animateTo(p);
        map.invalidate();
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
