package model.utils;

import model.vo.CaminosSinPeaje;

public class ComparatorCaminos implements ComparatorT<CaminosSinPeaje>
{

	@Override
	public int compare(CaminosSinPeaje objI, CaminosSinPeaje objD)
	{
		// TODO Auto-generated method stub
		return objI.compareTo(objD);
	}

}
