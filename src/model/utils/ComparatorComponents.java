package model.utils;

public class ComparatorComponents implements ComparatorT<Integer>
{

	@Override
	public int compare(Integer objI, Integer objD) 
	{
		int compare = objI.intValue() - objD.intValue();
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
