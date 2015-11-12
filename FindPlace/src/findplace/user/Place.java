package findplace.user;
import java.io.Serializable;
import java.util.StringTokenizer;

public class Place implements Serializable{
    private String type;
    private String latLong;
    private String name;
    double lat,lng;
    
    public Place(String n, String coords)
    {
       
        latLong = coords;
        name = n;
        StringTokenizer st = new StringTokenizer(name);
        type = st.nextToken();
        if(type.equals("Fast")) type+=" Food";
        
        
        
        StringTokenizer stringTokenizer = new StringTokenizer(latLong);
        int i = 0;
        while (stringTokenizer.hasMoreTokens()) {
            if (i == 0) {
                String lt = stringTokenizer.nextToken();
                 lat = Double.parseDouble(lt);
                System.out.println(lat);

                i++;
            } else {
                String lg = stringTokenizer.nextToken();
                 lng = Double.parseDouble(lg);
                System.out.println(lng);
            }
    }
    }
    public Place(String name,String Type,double lat,double lng)
    {
    	type=Type;
    	this.name=name;
    	this.lat=lat;
    	this.lng=lng;
    }
    
    public String getType()
    {
        return this.type;
    }
    
    public String getCoords()
    {
        return this.latLong;
    }
   
    public String getName( )
    {
        return this.name;
    }
    
    public double getLongtitude()
    {
        return this.lng;
        
    }
    
    public double getLatitude()
    {
        return this.lat;
        
    }
    
    
   
     
    
    
}
