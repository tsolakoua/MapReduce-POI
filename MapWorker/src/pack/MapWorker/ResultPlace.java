package pack.MapWorker;

import java.io.Serializable;

public class ResultPlace implements Serializable{

    private String name;
    private String type;
    private Place place;
    private String intermadiateKey;
    private int time;
    private int distance;

    public ResultPlace(Place place, String i, int tm, int d) {
        this.place=place;
        intermadiateKey = i;
        time = tm;
        distance = d;

    }
     public Place getPlace()
     {
    	return this.place;
     } 
     public String getName( )
    {
        return this.name;
    }     
    
    public String getType()
    {
        return this.type;
    }
    
     public String getKey( )
    {
        return this.intermadiateKey;
    }  
    
    public int getDistance()
    {
        return this.distance;
    }
    
     public int getTime()
    {
        return this.time;
    }
   
   
}
