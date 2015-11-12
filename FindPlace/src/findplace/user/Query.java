package findplace.user;

import java.io.Serializable;

public class Query implements Serializable {
	private double lat;
	private double lng;
	private String searchFor;
	
	public Query(double lat,double lng,String search)
	{
		this.lat=lat;
		this.lng=lng;
		searchFor=search;
	}
	public double getLat()
	{
		return this.lat;
	}
	public double getLng()
	{
		return this.lng;
	}
	public String getSearch()
	{
		return this.searchFor;
	}

}
