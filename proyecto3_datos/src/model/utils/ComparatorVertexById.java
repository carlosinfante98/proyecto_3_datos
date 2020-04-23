package model.utils;

import model.data_structures.Vertex;

@SuppressWarnings("rawtypes")
public class ComparatorVertexById implements ComparatorT<Vertex>{

	

		@Override
		public int compare(Vertex objI, Vertex objD) 
		{
			int compare = objI.getIdComponent() - objD.getIdComponent();
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
