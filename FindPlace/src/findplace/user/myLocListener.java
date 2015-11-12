package findplace.user;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

 class MyLocationListener implements LocationListener
{
	 MapView map;
	 MapController mapcontroller;
	 CustomOnItemSelectedListener onitemlistener;
	 public MyLocationListener(MapView map,CustomOnItemSelectedListener onitemlistener)
	 {
		  this.map=map;
		  mapcontroller=map.getController();
		  this.onitemlistener=onitemlistener;
	 }
   @Override
   public void onLocationChanged(Location loc)
   {
      if (loc != null)
      {
         // Do something knowing the location changed by the distance you requested
    	  mapcontroller.animateTo(new GeoPoint((int)(loc.getLatitude()* 1E6),(int)(loc.getLongitude()*1E6)));
    	  mapcontroller.setZoom(5);
    	  onitemlistener.setLocation(loc.getLatitude(),loc.getLongitude());
      }
   }

   @Override
   public void onProviderDisabled(String arg0)
   {
      // Do something here if you would like to know when the provider is disabled by the user
   }

   @Override
   public void onProviderEnabled(String arg0)
   {
      // Do something here if you would like to know when the provider is enabled by the user
   }

   @Override
   public void onStatusChanged(String arg0, int arg1, Bundle arg2)
   {
      // Do something here if you would like to know when the provider status changes
   }
}