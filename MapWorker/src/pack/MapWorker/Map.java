package pack.MapWorker;

import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

public class Map {

    int size = 70;
    double lat, lng;
    List<Place> results;
    String Cplace;
    int speed;

    public Map(List <Place>res, String s, double l, double ln,int speed) {
        lat = l;
        lng = ln;
        Cplace = s;
        results = res;
        this.speed=speed;

    }

    public ResultPlace result() {
        ResultPlace place ;
        Place temp=null;
        double minimumDistance = Double.MAX_VALUE;

        for (int i = 0; i < results.size(); i++) {
          if(Cplace.compareTo(results.get(i).getType())==0){ 
            double distance = Math.sqrt((lat - results.get(i).getLatitude()) * (lat - results.get(i).getLatitude()) + (lng - results.get(i).getLongtitude()) * (lng - results.get(i).getLongtitude()));
            if (minimumDistance > distance) {
                minimumDistance = distance;
            }// if 
            temp=results.get(i);
           }
        }//for
        int min = (int) (minimumDistance);  // minimum distance
       double t = min /speed;
        
       int time = (int)t ; // time in hours
           place=new ResultPlace(temp,"",min,time);
          return place;



    }
}