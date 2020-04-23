package model.utils;

import model.data_structures.Componente;

public class ComparatorComponente implements ComparatorT<Componente>
{

	@Override
	public int compare(Componente objI, Componente objD) 
	{
		int compare = objI.getNumeroDeVertices() - objD.getNumeroDeVertices();
		if(compare > 0)
		{
			return 1;
		}
		else if(compare < 0)
		{
			return -1;
		}
		else
		{
			return 0;
		}
		// TODO Auto-generated method stub
	}

}
