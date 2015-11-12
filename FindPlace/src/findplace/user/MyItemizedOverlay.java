package findplace.user;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class MyItemizedOverlay extends ItemizedOverlay<OverlayItem>{

	private ArrayList<OverlayItem> overlayItemList = new ArrayList<OverlayItem>();
 private Context context;
	public MyItemizedOverlay(Drawable marker,Context context) {
			super(boundCenterBottom(marker));
			// TODO Auto-generated constructor stub
this.context=context;
			populate();
		}

	public void addItem(GeoPoint p, String title, String snippet){
			OverlayItem newItem = new OverlayItem(p, title, snippet);
			overlayItemList.add(newItem);
			populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		// TODO Auto-generated method stub
		return overlayItemList.get(i);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return overlayItemList.size();
	}
	public void removeItem(int i){
		overlayItemList.remove(i);
        populate();
    }

	@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		// TODO Auto-generated method stub
		super.draw(canvas, mapView, shadow);
		//boundCenterBottom(marker);
		}
	@Override
	protected boolean onTap(int index) {
		OverlayItem item = overlayItemList.get(index);
			      AlertDialog.Builder dialog = new AlertDialog.Builder(context);
			      dialog.setTitle(item.getTitle());
			      dialog.setMessage(item.getSnippet());
			      dialog.show();
		
        return(true);
	}

	}