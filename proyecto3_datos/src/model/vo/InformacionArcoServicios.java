package model.vo;


public class InformacionArcoServicios 
{
	private double miles;
	private double money;
	private int seconds;
	private int numeroDeServiciosPeaje;
	private int numServicios;
	
	public InformacionArcoServicios(double pMiles, double pMoney, int pSeconds, int pNumeroPeaje)
	{
		miles = pMiles;
		money = pMoney;
		seconds = pSeconds;
		numeroDeServiciosPeaje = pNumeroPeaje;
		numServicios = 0;
	}
	
	public double getMiles()
	{
		return miles;
	}
	
	public int getNumServicios()
	{
		return numServicios;
	}

	public int getNumeroDeServiciosPeaje()
	{
		return numeroDeServiciosPeaje;
	}

	public void setNumeroDeServiciosPeaje(int numeroDeServiciosPeaje) 
	{
		this.numeroDeServiciosPeaje = numeroDeServiciosPeaje;
	}
	
	public void addService(double pMiles, double pMoney, int pSeconds, int pNumServPeaje)
	{
		miles += pMiles;
		money += pMoney;
		seconds += pSeconds;
		numeroDeServiciosPeaje += pNumServPeaje;
		numServicios++;
	}

	public double getMoney() 
	{
		return money;
	}

	public void setMoney(double money)
    {
		this.money = money;
	}

	public int getSeconds() 
	{
		return seconds;
	}

	public void setSeconds(int seconds) 
	{
		this.seconds = seconds;
	}
	
	public double seleccionarCriterio(int crit)
	{
		if(crit == 0)
		{
			return miles;
		}
		else if(crit == 1)
		{
			return money;
		}
		else if(crit == 2)
		{
			return seconds;
		}
		else
		{
			return numeroDeServiciosPeaje;
		}
	}
	
}
