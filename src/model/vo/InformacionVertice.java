package model.vo;

import model.data_structures.ArrayListT;

public class InformacionVertice 
{
	private ArrayListT<String> incomingServices;
	
	private ArrayListT<String> outgoingServices;
	
	private double latitude;
	
	private double longitude;
	
	
	public InformacionVertice(double pLatitude, double pLongitude)
	{
		latitude = pLatitude;
		longitude = pLongitude;
		incomingServices = new ArrayListT<String>();
		outgoingServices = new ArrayListT<String>();
	}
	
	public double getLatitude()
	{
		return latitude;
	}
	
	public double getLongitude()
	{
		return longitude;
	}
	
	public void setLatitude(double pLatitude)
	{
		latitude = pLatitude;
	}
	
	public void setLongitude(double pLongitude)
	{
		longitude = pLongitude;
	}
	
	public ArrayListT<String> getIncomingServices()
	{
		return incomingServices;
	}
	
	public ArrayListT<String> getOutgoingServices()
	{
		return outgoingServices;
	}
	
}
