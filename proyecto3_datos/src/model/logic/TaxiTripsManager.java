package model.logic;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Properties;

import com.google.gson.*;

import API.ITaxiTripsManager;
import model.data_structures.*;
import model.utils.ComparatorCaminos;
import model.utils.OrdenatorP;
import model.utils.OrdenatorP.Ordenamientos;
import model.vo.CaminosSinPeaje;
import model.vo.InformacionArcoServicios;
import model.vo.InformacionVertice;

public class TaxiTripsManager implements ITaxiTripsManager
{
	//--------------------------------
	//CONSTANTES
	//--------------------------------
	public static final String BODY1 = " <!DOCTYPE html>\n" + 
			"<html>\n" + 
			"  <head>\n" + 
			"    <title>Mapa</title>\n" + 
			"    <meta name=\"viewport\" content=\"initial-scale=1.0\">\n" + 
			"    <meta charset=\"utf-8\">\n" + 
			"    <style>\n" + 
			"      /* Always set the map height explicitly to define the size of the div\n" + 
			"       * element that contains the map. */\n" + 
			"      #map {\n" + 
			"        height: 100%;\n" + 
			"      }\n" + 
			"      /* Optional: Makes the sample page fill the window. */\n" + 
			"      html, body \n" + 
			"      {\n" + 
			"        height: 100%;\n" + 
			"        margin: 0;\n" + 
			"        padding: 0;\n" + 
			"      }\n" + 
			"    </style> \n" + 
			"  </head> \n" + 
			"  <body> \n" + 
			"    <div id=\"map\"></div> \n" + 
			"    <script> \n";

	public static final String BODY2 = "</script> \n" +
			"<script src=\"https://maps.googleapis.com/maps/api/js?key=AIzaSyCezLelBYGF40NF_TauXSX0NGVrnGKGeVU&callback=initMap\" \n" +
			"async defer></script>\n"+
			"</body>\n" +
			"</html>";

	//--------------------------------
	//ATRIBUTOS
	//--------------------------------

	private DiGraph<String, InformacionVertice, InformacionArcoServicios> graph ;	

	private ArrayListT<String> colours;

	private ArrayListNotComparable<InformacionVertice> infoCsv;

	private ArrayListNotComparable<Componente> listaDeComponentes;

	private KosarajuSharir k;

	private SeparateChainingHashST<Integer, Vertex<String, InformacionVertice, InformacionArcoServicios>> registerVertices;

	private SeparateChainingHashST<String, Edge<String, InformacionArcoServicios>> registEdges;

	private int numComponentes;

	private int numServicios;


	public TaxiTripsManager()
	{
		graph = new DiGraph<String, InformacionVertice, InformacionArcoServicios>();
		colours = new ArrayListT<String>();
		infoCsv = new ArrayListNotComparable<InformacionVertice>();
		registerVertices = new SeparateChainingHashST<Integer, Vertex<String, InformacionVertice, InformacionArcoServicios>>();
		registEdges = new SeparateChainingHashST<String,Edge<String, InformacionArcoServicios>>();
		cargarCsv();
		try 
		{
			cargarColores();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	public double getHarvesianDistance (double lat1, double lon1, double lat2, double lon2)
	{
		final int R = 6371*1000; // Radious of the earth in meters
		Double latDistance = Math.toRadians(lat2-lat1);
		Double lonDistance = Math.toRadians(lon2-lon1);
		Double a = Math.sin(latDistance/2) * Math.sin(latDistance/2) + Math.cos(Math.toRadians(lat1))
		* Math.cos(Math.toRadians(lat2)) * Math.sin(lonDistance/2) * Math.sin(lonDistance/2);
		Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		Double distance = R * c;
		return distance;
	}
	public void cargarGrafo()
	{
		JsonParser parser = new JsonParser();
		try
		{
			graph = new DiGraph<String, InformacionVertice, InformacionArcoServicios>();
			JsonArray arr= (JsonArray) parser.parse(new FileReader("./data/graphs/graphDistance-100.json"));
			//Ciclo que engloba a todos los vertices del grafo.
			for (int i = 0; arr != null && i < arr.size()-1; i++) 
			{
				JsonObject obj = (JsonObject) arr.get(i);
				//Proceso que permite obtener la informacion de los vertices y sus respectivos servicios.
				String key = obj.get("id").getAsString();	

				Double latitude = obj.get("latitude").getAsDouble();	
				Double longitude = obj.get("longitude").getAsDouble();	
				JsonArray value =  obj.get("value").getAsJsonObject().get("outgoingServices").getAsJsonArray();
				graph.addVertex(key, new InformacionVertice(latitude, longitude));
				for (int j = 0; j < value.size() ; j++)
				{
					graph.getInfoVertex(key).getOutgoingServices().add(value.get(j).getAsString());
				}

				JsonArray value2 =  obj.get("value").getAsJsonObject().get("incomingServices").getAsJsonArray();
				for (int j = 0; j < value2.size() ; j++)
				{
					graph.getInfoVertex(key).getIncomingServices().add(value2.get(j).getAsString());
				}

			}
			//Proceso que permite obtener la informacion de los arcos.
			JsonObject obj = (JsonObject) arr.get(arr.size()-1);
			JsonArray listOfEdges = obj.get("edges").getAsJsonArray();
			for (int j = 0; j < listOfEdges.size() ; j++) 
			{
				JsonObject informacion = listOfEdges.get(j).getAsJsonObject();
				int segundosPromedio = informacion.get("accumulatedSeconds").getAsInt();
				double millasPromedio = informacion.get("accumulatedMiles").getAsDouble();
				double dineroPromedio = informacion.get("accumulatedCash").getAsDouble();
				int numeroDePeajes = informacion.get("numTolls").getAsInt();
				String verticeDeLlegada = informacion.get("idArrivalVertex").getAsString();
				String verticeDeSalida = informacion.get("idDepartureVertex").getAsString();
				graph.addEdge(verticeDeSalida, verticeDeLlegada, new InformacionArcoServicios(millasPromedio, dineroPromedio, segundosPromedio, numeroDePeajes));
			}
			//Donde se asignan a cada arco su # de servicios?hmmm
			darNumServicios();
			System.out.println("numero vertices: "+graph.V());
			System.out.println("numero arcos es: "+ graph.E());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	//REQ 1
	public Vertex<String,InformacionVertice,InformacionArcoServicios> darInformacionDelVerticeMasCongestionado()
	{
		try
		{
			Vertex<String,InformacionVertice,InformacionArcoServicios> ve = null;
			int congestion = 0;
			int congestionFinal  = 0;
			//Este m�todo lo podemos realizar recorriendo la "listaDeVertices" que brinda el grafo. 
			// Si se hace as�, se deber�a ordenar esta lista por MERGE y posteriormente escoger al �ltimo elemento de este criter
			ArrayListNotComparable<Vertex<String, InformacionVertice,InformacionArcoServicios>> list = graph.getListOfVertices();
			for (int i = 0; i < list.size(); i++) 
			{
				Vertex<String, InformacionVertice,InformacionArcoServicios> v = list.get(i);
				congestion = graph.getInfoVertex(v.getId()).getIncomingServices().size() + 
						graph.getInfoVertex(v.getId()).getOutgoingServices().size();
				if(congestion > congestionFinal)
				{
					congestionFinal = congestion;
					ve = v;
				}
			}


			System.out.println("El v�rtice m�s congestionado es : "+ ve.getIdNum() +" \n Este tiene por (latitud,longitud) : "+
					ve.getId());
			System.out.println("Adem�s cuenta con " + graph.getInfoVertex(ve.getId()).getOutgoingServices().size() +" servicios de salida y con "+graph.getInfoVertex(ve.getId()).getIncomingServices().size() + " servicios de llegada");
			System.out.println();

			String[] ubication = ve.getId().split(",");
			File file = new File("./data/req1.html");
			PrintWriter writer =  new PrintWriter(file);

			writer.println(BODY1);
			writer.println("var loc = {lat:"+ ubication[0] +", lng:"+ ubication[1]+"}; \n");
			writer.println("var marker;");
			writer.println("function initMap() { \n"+
					"var map = new google.maps.Map(document.getElementById('map'), { \n"+
					"zoom: 16, \n"+
					"center: loc,\n"+
					"mapTypeId: 'terrain' \n"+
					"}); \n" );
			
			writer.println("var locl = {lat:"+ ubication[0] +", lng:"+ ubication[1]+"}; \n");

			writer.println("var circle = new google.maps.Circle({ \n"+
					"strokeColor: '#FF0000', \n"+
					"strokeOpacity: 0.6, \n"+
					"strokeWeight: 1, \n"+
					" fillColor: '" + obtenerColorAleatorio() + "',\n"+
					" fillOpacity: 0.35,\n"+
					"map: map, \n"+
					"center: locl,\n"+
					"radius: 100 \n"+
					"}); \n");

			writer.println("marker = new google.maps.Marker({\n" + 
					"          map: map,\n" + 
					"          draggable: false,\n" + 
					"          animation: google.maps.Animation.DROP,\n" + 
					"          position: {lat:"+ ubication[0] +", lng:"+ ubication[1]+"} \n" + 
					"        });\n" + 
					"        marker.addListener('click', toggleBounce);\n" + 
					"      }\n" + 
					"\n" + 
					"      function toggleBounce() {\n" + 
					"        if (marker.getAnimation() !== null) {\n" + 
					"          marker.setAnimation(null);\n" + 
					"        } else {\n" + 
					"          marker.setAnimation(google.maps.Animation.BOUNCE);\n" + 
					"        }\n" + 
					"      }");

			writer.println(BODY2);
			writer.close();

			File f = new File("data/req1.html");
			java.awt.Desktop.getDesktop().browse(f.toURI());

			return ve;


		}
		catch(Exception e)
		{
			return null;

		}
	}

	//REQ 2
	public ArrayListNotComparable<Componente> darComponentesFuertementeConectadas()
	{
		ArrayListNotComparable<Vertex<String, InformacionVertice, InformacionArcoServicios>> listaFinal = new ArrayListNotComparable<Vertex<String, InformacionVertice, InformacionArcoServicios>>();
		ArrayListNotComparable<ArrayListNotComparable<Vertex<String, InformacionVertice, InformacionArcoServicios>>> laLista = new ArrayListNotComparable<ArrayListNotComparable<Vertex<String, InformacionVertice, InformacionArcoServicios>>>();
		try
		{

			File file = new File("data/req2.html");
			PrintWriter writer =  new PrintWriter(file);
			graph.inicializar();
			k = new KosarajuSharir(graph);
			laLista = k.getMotherOfLists();
			numComponentes = laLista.size();
			int posicionConMayorVertices = 0;
			int temp = 0;
			for (int i = 0; i < laLista.size(); i++) 
			{
				if(laLista.get(i).size() > temp)
				{
					posicionConMayorVertices = i;
					temp = laLista.get(i).size();
				}
			}
			listaFinal = laLista.get(posicionConMayorVertices);
			graficarComponente(writer, listaFinal, null, k,false);

			java.awt.Desktop.getDesktop().browse(file.toURI());
		}
		catch(Exception e)
		{
			System.out.println("No pudo hacer el kosaraju");
			e.printStackTrace();
		}

		listaDeComponentes =  new ArrayListNotComparable<Componente>();
		for (int i = 0; i < laLista.size(); i++) 
		{
			listaDeComponentes.add(new Componente(laLista.get(i).size(), laLista.get(i).get(0).getIdComponent(), laLista.get(i)));
		}

		return listaDeComponentes;
		//Kosaraju Sharir.
		//Realizar la asignaci�n por colores.
	}

	//REQ 3
	public void graficar()
	{
		try
		{
			File file = new File("data/req3.html");
			PrintWriter writer =  new PrintWriter(file);
			graph.inicializar();


			graficarComponente(writer,null, listaDeComponentes, k, true);

			java.awt.Desktop.getDesktop().browse(file.toURI());

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		//M�todo m�s complejo
	}

	public void graficarComponente(PrintWriter writer, ArrayListNotComparable<Vertex<String, InformacionVertice, InformacionArcoServicios>> listaFinal,ArrayListNotComparable<Componente> listaF, KosarajuSharir k, boolean density)
	{
		if(!density)
		{
			writer.println(BODY1);
			writer.println("var loc = {lat: 41.798797104, lng: -87.708637865};"); 
			writer.println("function initMap() { \n"+
					"var map = new google.maps.Map(document.getElementById('map'), { \n"+
					"zoom: 11, \n"+
					"center: loc,\n"+
					"mapTypeId: 'terrain' \n"+
					"}); \n" );
			String colour = obtenerColorAleatorio();
			writer.println("var lineSymbol = \n"+
					" { \n"+
					"  path: google.maps.SymbolPath.FORWARD_CLOSED_ARROW \n" +
					"  }; \n " );
			for (int i = 0; i < listaFinal.size(); i++) 
			{
				Vertex<String, InformacionVertice, InformacionArcoServicios> v = listaFinal.get(i);
				String ubicacion = v.getId();
				String[] ubication = ubicacion.split(",");
				writer.println("var locl = {lat:"+ ubication[0] +", lng:"+ ubication[1]+"}; \n");
				writer.println("var circle = new google.maps.Circle({ \n"+
						"strokeColor: '#FF0000', \n"+
						"strokeOpacity: 0.6, \n"+
						"strokeWeight: 1, \n"+
						" fillColor: '" + colour + "',\n"+
						" fillOpacity: 0.35,\n"+
						"map: map, \n"+
						"center: locl,\n"+
						"radius:500 \n"+
						"}); \n");

				for (int j = 0; j < v.getOutEdges().size(); j++) 
				{
					Edge<String, InformacionArcoServicios> e = v.getOutEdges().get(j);
					String ubLlegada = e.getIdVertEnd();
					//					if(v.getIdComponent() == graph.getVertex(ubLlegada).getIdComponent() && !(v.getId().equals(ubLlegada)))
					//					{
					String[] ubi = ubLlegada.split(",");
					writer.println("var line = new google.maps.Polyline({ \n"+
							" path: [locl, {lat:"+ubi[0] +", lng:"+ ubi[1] +"}], \n"+
							" icons: [{ \n "+
							" icon: lineSymbol, \n"+
							" offset: '100%' \n"+
							" }], \n"+
							" strokeColor: '" + colour + "', \n"+
							" strokeWeight : 0.5, " +
							"map: map \n"+
							" }); \n");
					//					}
				}
			}

			writer.println("}");
			writer.println(BODY2);
			writer.close();
		}
		else
		{
			if(listaF != null)
			{
				writer.println(BODY1);
				writer.println("var loc = {lat: 41.798797104, lng: -87.708637865};"); 
				writer.println("function initMap() { \n"+
						"var map = new google.maps.Map(document.getElementById('map'), { \n"+
						"zoom: 11, \n"+
						"center: loc,\n"+
						"mapTypeId: 'terrain' \n"+
						"}); \n" );
				System.out.println(listaF.size()+"-----");
				writer.println("var lineSymbol = \n"+
						" { \n"+
						"  path: google.maps.SymbolPath.FORWARD_CLOSED_ARROW \n" +
						"  }; \n " );

				for (int i = 0; i < listaF.size(); i++) 
				{

					Componente c = listaF.get(i);
					ArrayListNotComparable<Vertex<String, InformacionVertice, InformacionArcoServicios>> listica = c.getList();
					String colour = obtenerColorAleatorio();
					for (int j = 0; j < listica.size(); j++) 
					{

						Vertex<String, InformacionVertice, InformacionArcoServicios> v = listica.get(j);

						//						if(!existsVertex(v))
						//						{
						registerVertices.put(v.getIdNum(), v);
						String[] ubication = v.getId().split(",");
						writer.println("var locl = {lat:"+ ubication[0] +", lng:"+ ubication[1]+"}; \n");

						double suma = (v.getValue().getIncomingServices().size()+v.getValue().getOutgoingServices().size());
						double radio = suma / (double) numServicios;
						radio *= 100;
						System.out.println("numServicios es "+ numServicios);
						if(radio < 0.01 )
						{
							radio *= 50000;
							writer.println("var circle = new google.maps.Circle({ \n"+
									"strokeColor: '#FF0000', \n"+
									"strokeOpacity: 0.6, \n"+
									"strokeWeight: 1, \n"+
									" fillColor: '" + colour + "',\n"+
									" fillOpacity: 0.35,\n"+
									"map: map, \n"+
									"center: locl,\n"+
									"radius:" +radio+"\n"+
									"}); \n");
						}
						else if(radio < 0.09 )
						{
							radio *= 5000;
							writer.println("var circle = new google.maps.Circle({ \n"+
									"strokeColor: '#FF0000', \n"+
									"strokeOpacity: 0.6, \n"+
									"strokeWeight: 1, \n"+
									" fillColor: '" + colour + "',\n"+
									" fillOpacity: 0.35,\n"+
									"map: map, \n"+
									"center: locl,\n"+
									"radius:" +radio+"\n"+
									"}); \n");
						}
						else if(radio < 3.0)
						{
							radio *= 700;
							writer.println("var circle = new google.maps.Circle({ \n"+
									"strokeColor: '#FF0000', \n"+
									"strokeOpacity: 0.6, \n"+
									"strokeWeight: 1, \n"+
									" fillColor: '" + colour + "',\n"+
									" fillOpacity: 0.35,\n"+
									"map: map, \n"+
									"center: locl,\n"+
									"radius:" +radio+"\n"+
									"}); \n");

						}
						else if(radio < 10.0)
						{
							radio *= 80;
							writer.println("var circle = new google.maps.Circle({ \n"+
									"strokeColor: '#FF0000', \n"+
									"strokeOpacity: 0.6, \n"+
									"strokeWeight: 1, \n"+
									" fillColor: '" + colour + "',\n"+
									" fillOpacity: 0.35,\n"+
									"map: map, \n"+
									"center: locl,\n"+
									"radius:" +radio+"\n"+
									"}); \n");
						}
						else if(radio <300.0)
						{
							radio *= 2;
							writer.println("var circle = new google.maps.Circle({ \n"+
									"strokeColor: '#FF0000', \n"+
									"strokeOpacity: 0.6, \n"+
									"strokeWeight: 1, \n"+
									" fillColor: '" + colour + "',\n"+
									" fillOpacity: 0.35,\n"+
									"map: map, \n"+
									"center: locl,\n"+
									"radius:" +radio+"\n"+
									"}); \n");
						}
						else
						{
							writer.println("var circle = new google.maps.Circle({ \n"+
									"strokeColor: '#FF0000', \n"+
									"strokeOpacity: 0.6, \n"+
									"strokeWeight: 1, \n"+
									" fillColor: '" + colour + "',\n"+
									" fillOpacity: 0.35,\n"+
									"map: map, \n"+
									"center: locl,\n"+
									"radius:" +radio+"\n"+
									"}); \n");
						}
						//						}
						for (int h = 0; h < v.getOutEdges().size(); h++) 
						{
							Edge<String, InformacionArcoServicios> e = v.getOutEdges().get(h);
							String ubLlegada =  e.getIdVertEnd();
							//							if(v.getIdComponent() == graph.getVertex(ubLlegada).getIdComponent() && !(v.getId().equals(ubLlegada)))
							//							{
							//								if(!existsEdge(e))
							//								{
							registEdges.put(e.getIdVertStart()+e.getIdVertEnd(), e);
							String[] ubi = ubLlegada.split(",");
							writer.println("var line = new google.maps.Polyline({ \n"+
									" path: [locl, {lat:"+ubi[0] +", lng:"+ ubi[1] +"}], \n"+
									" icons: [{ \n "+
									" icon: lineSymbol, \n"+
									" offset: '100%' \n"+
									" }], \n"+
									" strokeColor: '" + colour + "', \n"+
									" strokeWeight : 0.5, " +
									"map: map \n"+
									" }); \n");
							//								}
							//							}

						}
					}
				}
			}
			writer.println("}");
			writer.println(BODY2);
			writer.close();
		}
	}


	//REQ 4
	public void darInformacionCaminoMenorDistancia()
	{
		InformacionVertice info1 = obtenerInfoAleatoria();
		InformacionVertice info2 = obtenerInfoAleatoria();
		//Dijkstra 
		String s = aproximarCoordenadaAlGrafo(info1.getLatitude()+","+info1.getLongitude());
		String b = aproximarCoordenadaAlGrafo(info2.getLatitude()+","+info2.getLongitude());

		try
		{
			String colour = obtenerColorAleatorio();
			File file = new File("./data/req4.html");
			PrintWriter writer =  new PrintWriter(file);
			graph.inicializar();
			Dijkstra<String> dj = new Dijkstra<String>(graph, s, true,0);
			Iterable<Edge<String,InformacionArcoServicios>> iterPath = dj.pathTo(graph, graph.getVertex(b));
			if(iterPath != null)
			{
				Iterator<Edge<String,InformacionArcoServicios>> iter = iterPath.iterator();
				double cantidadDinero = 0.0 ;
				int segundosFinales = 0;

				writer.println(BODY1);
				writer.println("var loc = {lat: 41.798797104, lng: -87.708637865};"); 
				writer.println("var marker;");
				writer.println("function initMap() { \n"+
						"var map = new google.maps.Map(document.getElementById('map'), { \n"+
						"zoom: 11, \n"+
						"center: loc,\n"+
						"mapTypeId: 'terrain' \n"+
						"}); \n" );
				writer.println("var lineSymbol = \n"+
						" { \n"+
						"  path: google.maps.SymbolPath.FORWARD_CLOSED_ARROW \n" +
						"  }; \n " );

				System.out.println("La ruta a seguir es: ");
				System.out.println("Localizacion inicial :"+ s);
				System.out.println("Localizacion final :"+b);

				while(iter.hasNext())
				{
					Edge<String, InformacionArcoServicios> e =  iter.next();
					System.out.println("------>"+e.getIdVertStart());
					String[] inicial = e.getIdVertStart().split(",");
					String[] finall = e.getIdVertEnd().split(",");
					cantidadDinero += e.getEspecifiedElement(1);
					segundosFinales += e.getEspecifiedElement(2);

					writer.println("var locl = {lat:"+ inicial[0] +", lng:"+ inicial[1]+"}; \n");
					writer.println("var locp = {lat:"+ finall[0] +", lng:"+ finall[1]+"}; \n");

					if(e.getIdVertStart().equals(s))
					{
						writer.println("marker = new google.maps.Marker({\n" + 
								"          map: map,\n" + 
								"          draggable: false,\n" + 
								"          animation: google.maps.Animation.DROP,\n" + 
								"          label: 'I',\n" +
								"          position: locl \n" + 
								"        });");
					}
					if(e.getIdVertEnd().equals(b)) 
					{
						writer.println("marker = new google.maps.Marker({\n" + 
								"          map: map,\n" + 
								"          draggable: false,\n" + 
								"          animation: google.maps.Animation.DROP,\n" + 
								"          label: 'F',\n" +
								"          position: locp \n" + 
								"        });");
					}

					writer.println("var circle = new google.maps.Circle({ \n"+
							"strokeColor: '#FF0000', \n"+
							"strokeOpacity: 0.6, \n"+
							"strokeWeight: 1, \n"+
							" fillColor: '" + colour + "',\n"+
							" fillOpacity: 0.35,\n"+
							"map: map, \n"+
							"center: locl,\n"+
							"radius: 500 \n"+
							"}); \n");
					writer.println("var circle = new google.maps.Circle({ \n"+
							"strokeColor: '#FF0000', \n"+
							"strokeOpacity: 0.6, \n"+
							"strokeWeight: 1, \n"+
							" fillColor: '" + colour + "',\n"+
							" fillOpacity: 0.35,\n"+
							"map: map, \n"+
							"center: locp,\n"+
							"radius: 500 \n"+
							"}); \n");
					writer.println("var line = new google.maps.Polyline({ \n"+
							" path: [locl, locp], \n"+
							" icons: [{ \n "+
							" icon: lineSymbol, \n"+
							" offset: '100%' \n"+
							" }], \n"+
							" strokeColor: '" + colour + "', \n"+
							"map: map \n"+
							" }); \n");
				}
				System.out.println("------>"+b);
				writer.println("}");
				writer.println(BODY2);
				writer.close();
				System.out.println();
				System.out.println("Al camino le corresponden \n "+
						"Distancia (la menos costosa respecto a millas) : "+dj.distTo(graph.getVertex(b))+ " millas"+"\n"+
						"Segundos totales : "+segundosFinales+ " seg"+"\n"+
						"Dinero total : $"+cantidadDinero);
				System.out.println();

				java.awt.Desktop.getDesktop().browse(file.toURI());
			}
			else
			{
				System.out.println("No hay camino entre los puntos aproximados al camino. Recalculando...");
				System.out.println();
				darInformacionCaminoMenorDistancia();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("Algo fall� al hallar el camino m�s corto" + e.getMessage());
		}
	}

	//REQ 5
	public void darCaminoMasCortoYMasLargo()
	{
		InformacionVertice info1 = obtenerInfoAleatoria();
		InformacionVertice info2 = obtenerInfoAleatoria();

		//Dijkstra 
		String s = aproximarCoordenadaAlGrafo(info1.getLatitude()+","+info1.getLongitude());
		String b = aproximarCoordenadaAlGrafo(info2.getLatitude()+","+info2.getLongitude());

		try
		{
			String colorCaminoCorto = obtenerColorAleatorio();
			File file = new File("data/req5.html");
			PrintWriter writer =  new PrintWriter(file);
			graph.inicializar();

			Dijkstra<String> dj = new Dijkstra<String>(graph, s, true,2);
			Iterable<Edge<String,InformacionArcoServicios>> iterPath = dj.pathTo(graph, graph.getVertex(b));

			if(iterPath != null)
			{
				Dijkstra<String> dji = new Dijkstra<String>(graph, b, true,2);
				Iterator<Edge<String,InformacionArcoServicios>> iter = iterPath.iterator();
				double cantidadDinero = 0.0 ;
				int segundosFinales = 0;

				writer.println(BODY1);
				writer.println("var loc = {lat: 41.798797104, lng: -87.708637865};"); 
				writer.println("var marker;");
				writer.println("function initMap() { \n"+
						"var map = new google.maps.Map(document.getElementById('map'), { \n"+
						"zoom: 11, \n"+
						"center: loc,\n"+
						"mapTypeId: 'terrain' \n"+
						"}); \n" );
				writer.println("var lineSymbol = \n"+
						" { \n"+
						"  path: google.maps.SymbolPath.FORWARD_CLOSED_ARROW \n" +
						"  }; \n " );

				System.out.println("La ruta a seguir es: ");
				System.out.println("            Localizacion inicial :"+ s);
				System.out.println("            Localizacion final :"+b);

				while(iter.hasNext())
				{
					Edge<String, InformacionArcoServicios> e =  iter.next();
					System.out.println("------>"+e.getIdVertStart());
					String[] inicial = e.getIdVertStart().split(",");
					String[] finall = e.getIdVertEnd().split(",");
					cantidadDinero += e.getEspecifiedElement(1);
					segundosFinales += e.getEspecifiedElement(2);

					writer.println("var locl = {lat:"+ inicial[0] +", lng:"+ inicial[1]+"}; \n");
					writer.println("var locp = {lat:"+ finall[0] +", lng:"+ finall[1]+"}; \n");


					writer.println("var circle = new google.maps.Circle({ \n"+
							"strokeColor: '#FF0000', \n"+
							"strokeOpacity: 0.6, \n"+
							"strokeWeight: 1, \n"+
							" fillColor: '" + colorCaminoCorto + "',\n"+
							" fillOpacity: 0.35,\n"+
							"map: map, \n"+
							"center: locl,\n"+
							"radius: 500 \n"+
							"}); \n");
					writer.println("var circle = new google.maps.Circle({ \n"+
							"strokeColor: '#FF0000', \n"+
							"strokeOpacity: 0.6, \n"+
							"strokeWeight: 1, \n"+
							" fillColor: '" + colorCaminoCorto + "',\n"+
							" fillOpacity: 0.35,\n"+
							"map: map, \n"+
							"center: locp,\n"+
							"radius: 500 \n"+
							"}); \n");
					writer.println("var line = new google.maps.Polyline({ \n"+
							" path: [locl, locp], \n"+
							" icons: [{ \n "+
							" icon: lineSymbol, \n"+
							" offset: '100%' \n"+
							" }], \n"+
							" strokeColor: '" + colorCaminoCorto + "', \n"+
							"map: map \n"+
							" }); \n");

				}
				System.out.println("------>"+b);
				System.out.println();
				System.out.println("Al camino le corresponden \n "+
						"Distancia (respecto a millas) : "+dj.distTo(graph.getVertex(b))+ " millas"+"\n"+
						"Segundos totales : "+segundosFinales+ " seg"+"\n"+
						"Dinero total : $"+cantidadDinero);
				System.out.println();

				String colorCaminoLargo = obtenerColorAleatorio();
				iterPath = dji.pathTo(graph, graph.getVertex(s));
				iter = iterPath.iterator();
				cantidadDinero = 0.0 ;
				segundosFinales = 0;
				System.out.println("La ruta a seguir es: ");
				System.out.println("            Localizacion inicial :"+ b);
				System.out.println("            Localizacion final :"+s);

				while(iter.hasNext())
				{
					Edge<String, InformacionArcoServicios> e =  iter.next();
					System.out.println("------>"+e.getIdVertStart());
					String[] inicial = e.getIdVertStart().split(",");
					String[] finall = e.getIdVertEnd().split(",");
					cantidadDinero += e.getEspecifiedElement(1);
					segundosFinales += e.getEspecifiedElement(2);

					writer.println("var locl = {lat:"+ inicial[0] +", lng:"+ inicial[1]+"}; \n");
					writer.println("var locp = {lat:"+ finall[0] +", lng:"+ finall[1]+"}; \n");

					if(e.getIdVertStart().equals(b))
					{
						writer.println("marker = new google.maps.Marker({\n" + 
								"          map: map,\n" + 
								"          draggable: false,\n" + 
								"          animation: google.maps.Animation.DROP,\n" + 
								"          position: locl \n" + 
								"        });");
					}
					if(e.getIdVertEnd().equals(s)) 
					{
						writer.println("marker = new google.maps.Marker({\n" + 
								"          map: map,\n" + 
								"          draggable: false,\n" + 
								"          animation: google.maps.Animation.DROP,\n" + 
								"          position: locp \n" + 
								"        });");
					}

					writer.println("var circle = new google.maps.Circle({ \n"+
							"strokeColor: '#FF0000', \n"+
							"strokeOpacity: 0.6, \n"+
							"strokeWeight: 1, \n"+
							" fillColor: '" + colorCaminoLargo + "',\n"+
							" fillOpacity: 0.35,\n"+
							"map: map, \n"+
							"center: locl,\n"+
							"radius: 500 \n"+
							"}); \n");
					writer.println("var circle = new google.maps.Circle({ \n"+
							"strokeColor: '#FF0000', \n"+
							"strokeOpacity: 0.6, \n"+
							"strokeWeight: 1, \n"+
							" fillColor: '" + colorCaminoLargo + "',\n"+
							" fillOpacity: 0.35,\n"+
							"map: map, \n"+
							"center: locp,\n"+
							"radius: 500 \n"+
							"}); \n");
					writer.println("var line = new google.maps.Polyline({ \n"+
							" path: [locl, locp], \n"+
							" icons: [{ \n "+
							" icon: lineSymbol, \n"+
							" offset: '100%' \n"+
							" }], \n"+
							" strokeColor: '" + colorCaminoLargo + "', \n"+
							"map: map \n"+
							" }); \n");

				}
				System.out.println("------>"+s);
				writer.println("}");
				writer.println(BODY2);
				writer.close();
				System.out.println();
				System.out.println("Al camino le corresponden \n "+
						"Distancia (respecto a millas) : "+dji.distTo(graph.getVertex(s))+ " millas"+"\n"+
						"Segundos totales : "+segundosFinales+ " seg"+"\n"+
						"Dinero total : $"+cantidadDinero);
				System.out.println();

				java.awt.Desktop.getDesktop().browse(file.toURI());

			}
			else
			{
				System.out.println("No hay camino corto los puntos aproximados al camino.");
				darCaminoMasCortoYMasLargo();
			}


		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("Algo fall� al hallar el camino m�s corto" + e.getMessage());
		}
		//Dijkstra y Dijkstra a la inversa
	}

	//REQ 6
	public ArrayListNotComparable<ArrayListT<String>> darCaminosSinPeaje()
	{
		//DFS(todos los caminos posibles
		//ordenador por dinero
		//Dijkstra para obtener el mayor camino y el menor camino.

		ArrayListNotComparable<ArrayListT<String>> lista = new ArrayListNotComparable<ArrayListT<String>>();
		InformacionVertice info1 = obtenerInfoAleatoria();
		InformacionVertice info2 = obtenerInfoAleatoria();

		//Dijkstra 
		String s = aproximarCoordenadaAlGrafo(info1.getLatitude()+","+info1.getLongitude());
		String b = aproximarCoordenadaAlGrafo(info2.getLatitude()+","+info2.getLongitude());

		try 
		{
			ArrayListNotComparable<Queue<Edge<String,InformacionArcoServicios>>> colaDeColas = new ArrayListNotComparable<Queue<Edge<String,InformacionArcoServicios>>>();
			ArrayListNotComparable<ArrayListNotComparable<Vertex<String, InformacionVertice, InformacionArcoServicios>>> listaDeCaminos = graph.encontrarCaminos(s, b);
			if(listaDeCaminos != null)
			{
				for (int i = 0; i < listaDeCaminos.size(); i++) 
				{
					ArrayListNotComparable<Vertex<String, InformacionVertice, InformacionArcoServicios>> subLista = listaDeCaminos.get(i); //coger un camino
					Queue<Edge<String,InformacionArcoServicios>> vertices = new Queue<Edge<String,InformacionArcoServicios>>();

					boolean camino = false;
					for(int j= 0; j < subLista.size()-1 && !camino ; j++ )
					{
						if(graph.getInfoEdge(subLista.get(j).getId(), subLista.get(j+1).getId()).getNumeroDeServiciosPeaje() == 0)
						{
							vertices.enqueue(graph.getEdge(subLista.get(j).getId(), subLista.get(j+1).getId()));
						}
						else
						{
							camino = true;
							///aqui entra si el camino no cumple con la condicion de que no tenga peaje.
						}
					}
					if(!camino)
						colaDeColas.add(vertices); 
				}
				if(colaDeColas.size() != 0)
				{

					ArrayListT<CaminosSinPeaje> sumaCaminosCostos = new ArrayListT<CaminosSinPeaje>();
					double tempo = 0;
					//ponle el template.
					for (int i = 0; i < colaDeColas.size(); i++) 
					{
						Queue<Edge<String,InformacionArcoServicios>> cola = colaDeColas.get(i);

						for (Edge<String,InformacionArcoServicios> e : cola ) 
						{
							tempo += e.getElement().getMoney();
						}
						sumaCaminosCostos.add(new CaminosSinPeaje("Dinero", tempo, cola)); // a�adimos la sumatoria en costos del camino (i)
					}
					OrdenatorP<CaminosSinPeaje> ordenador = new OrdenatorP<CaminosSinPeaje>();
					ComparatorCaminos comparador = new ComparatorCaminos();

					ordenador.ordenar(Ordenamientos.MERGE, false, comparador, sumaCaminosCostos);

					System.out.println("Los caminos ordenados por costos se ven de la siguiente forma : ");

					for (int i = 0; i < sumaCaminosCostos.size(); i++) 
					{
						System.out.println("------------------------------------------------");
						CaminosSinPeaje csp = sumaCaminosCostos.get(i);
						System.out.println("                  CAMINO "+ i +" con $ "+ csp.darSumaCamino());
						for (Edge<String, InformacionArcoServicios> edge : csp.darCamino()) 
						{
							System.out.println("Desde "+ edge.getIdVertStart() + " hasta "+ edge.getIdVertEnd());
						}
					}

					//GRAFICAR TEMPORAL

					//Temp es la que graficaremos.
					ArrayListT<CaminosSinPeaje> sumaCaminoSegundos = new ArrayListT<CaminosSinPeaje>();
					double sumaSec = 0;
					for (int i = 0; i < colaDeColas.size(); i++) 
					{
						Queue<Edge<String,InformacionArcoServicios>> cola = colaDeColas.get(i);

						for (Edge<String,InformacionArcoServicios> e : cola ) 
						{
							sumaSec += e.getElement().getSeconds();
						}
						sumaCaminoSegundos.add(new CaminosSinPeaje("Segundos", sumaSec, cola)); // a�adimos la sumatoria en costos del camino (i)

					}
					ordenador.ordenar(Ordenamientos.MERGE, true, comparador, sumaCaminoSegundos);

					System.out.println("Los caminos ordenador por segundos se ven de la siguiente forma ");

					for (int i = 0; i < sumaCaminoSegundos.size(); i++) 
					{
						System.out.println("------------------------------------------------");
						CaminosSinPeaje csp = sumaCaminoSegundos.get(i);
						System.out.println("                  CAMINO "+ i +" con  "+ csp.darSumaCamino()+" seg");
						for (Edge<String, InformacionArcoServicios> edge : csp.darCamino()) 
						{
							System.out.println("Desde "+ edge.getIdVertStart() + " hasta "+ edge.getIdVertEnd());
						}
					}
					//GRAFICAR TEMP

					String colorSegundos = colours.get(17);// VERDEEEE
					String colorMoney = colours.get(48); //NARANJA

					File file = new File("data/req6.html");
					PrintWriter writer =  new PrintWriter(file);

					//se grafica sumaCaminoSegundos

					writer.println(BODY1);
					writer.println("var loc = {lat: 41.798797104, lng: -87.708637865};"); 
					writer.println("var marker;");
					writer.println("function initMap() { \n"+
							"var map = new google.maps.Map(document.getElementById('map'), { \n"+
							"zoom: 11, \n"+
							"center: loc,\n"+
							"mapTypeId: 'terrain' \n"+
							"}); \n" );
					writer.println("var lineSymbol = \n"+
							" { \n"+
							"  path: google.maps.SymbolPath.FORWARD_CLOSED_ARROW \n" +
							"  }; \n " );

					Queue<Edge<String,InformacionArcoServicios>> caminoUno = sumaCaminoSegundos.get(0).darCamino(); //no tiene sentido, que mierda.
					Queue<Edge<String,InformacionArcoServicios>> caminoDos = sumaCaminosCostos.get(0).darCamino();
					
					
					for (Edge<String, InformacionArcoServicios> edge : caminoUno) 
					{
						String[] inicial = edge.getIdVertStart().split(",");
						String[] finall = edge.getIdVertEnd().split(",");

						writer.println("var locl = {lat:"+ inicial[0] +", lng:"+ inicial[1]+"}; \n");
						writer.println("var locp = {lat:"+ finall[0] +", lng:"+ finall[1]+"}; \n");

						
						writer.println("var circle = new google.maps.Circle({ \n"+
								"strokeColor: '#FF0000', \n"+
								"strokeOpacity: 0.6, \n"+
								"strokeWeight: 1, \n"+
								" fillColor: '" + colorSegundos + "',\n"+
								" fillOpacity: 0.35,\n"+
								"map: map, \n"+
								"center: locp,\n"+
								"radius: 500 \n"+
								"}); \n");

						writer.println("var circle = new google.maps.Circle({ \n"+
								"strokeColor: '#FF0000', \n"+
								"strokeOpacity: 0.6, \n"+
								"strokeWeight: 1, \n"+
								" fillColor: '" + colorSegundos + "',\n"+
								" fillOpacity: 0.35,\n"+
								"map: map, \n"+
								"center: locl,\n"+
								"radius: 500 \n"+
								"}); \n");

						writer.println("var line = new google.maps.Polyline({ \n"+
								" path: [locl, locp], \n"+
								" icons: [{ \n "+
								" icon: lineSymbol, \n"+
								" offset: '100%' \n"+
								" }], \n"+
								" strokeColor: '" + colorSegundos + "', \n"+
								"map: map \n"+
								" }); \n");
					}
					for (Edge<String, InformacionArcoServicios> edge : caminoDos) 
					{
						String[] inicial = edge.getIdVertStart().split(",");
						String[] finall = edge.getIdVertEnd().split(",");

						writer.println("var locl = {lat:"+ inicial[0] +", lng:"+ inicial[1]+"}; \n");
						writer.println("var locp = {lat:"+ finall[0] +", lng:"+ finall[1]+"}; \n");
						
						if(edge.getIdVertStart().equals(s))
							writer.println("marker = new google.maps.Marker({\n" + 
								"          map: map,\n" + 
								"          draggable: false,\n" + 
								"          animation: google.maps.Animation.DROP,\n" + 
								"          label: 'I',\n" +
								"          position: locl \n" + 
								"        });");
						if(edge.getIdVertEnd().equals(b))
							writer.println("marker = new google.maps.Marker({\n" + 
								"          map: map,\n" + 
								"          draggable: false,\n" + 
								"          animation: google.maps.Animation.DROP,\n" + 
								"          label: 'F',\n" +
								"          position: locp \n" + 
								"        });");
						
						writer.println("var circle = new google.maps.Circle({ \n"+
								"strokeColor: '#FF0000', \n"+
								"strokeOpacity: 0.6, \n"+
								"strokeWeight: 1, \n"+
								" fillColor: '" + colorMoney + "',\n"+
								" fillOpacity: 0.35,\n"+
								"map: map, \n"+
								"center: locp,\n"+
								"radius: 500 \n"+
								"}); \n");

						writer.println("var circle = new google.maps.Circle({ \n"+
								"strokeColor: '#FF0000', \n"+
								"strokeOpacity: 0.6, \n"+
								"strokeWeight: 1, \n"+
								" fillColor: '" + colorMoney + "',\n"+
								" fillOpacity: 0.35,\n"+
								"map: map, \n"+
								"center: locl,\n"+
								"radius: 500 \n"+
								"}); \n");

						writer.println("var line = new google.maps.Polyline({ \n"+
								" path: [locl, locp], \n"+
								" icons: [{ \n "+
								" icon: lineSymbol, \n"+
								" offset: '100%' \n"+
								" }], \n"+
								" strokeColor: '" + colorMoney + "', \n"+
								"map: map \n"+
								" }); \n");
					}
					writer.println("}");
					writer.println(BODY2);
					writer.close();
					
					System.out.println("El camino con el menor valor de segundos es : verde");
					System.out.println("El camino con el mayor valor de costo es : naranja");

					java.awt.Desktop.getDesktop().browse(file.toURI());
				} 
				else
				{
					darCaminosSinPeaje();
				}
			}
			else
			{
				darCaminosSinPeaje();
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}

		return lista;
	}

	public void cargarColores() throws Exception
	{
		Properties datos = new Properties();

		try
		{
			FileInputStream fis = new FileInputStream(new File("data/colores.properties"));
			datos.load(fis);
			fis.close();
			int i = 0;
			boolean acabo = false;
			while(!acabo)
			{
				colours.add(datos.getProperty("" + i));
				if(i == 55)
				{
					acabo = true;
				}
				i++;
			}
		}
		catch(Exception e)
		{
			throw new Exception("Error al cargar archivo");
		}
	}

	public String obtenerColorAleatorio()
	{
		int pos = (int)(Math.random()*100)%(colours.size()+1);
		return colours.get(pos);
	}

	public String aproximarCoordenadaAlGrafo(String coordenada)
	{
		//Valor por defecto.
		InformacionVertice infor = new InformacionVertice(-2, -2);
		String[] coordinate =  coordenada.split(",");
		double latitud = Double.parseDouble(coordinate[0]);
		double longitud = Double.parseDouble(coordinate[1]);


		ArrayListNotComparable<InformacionVertice> array = graph.vertices();
		double temp = Double.MAX_VALUE;
		for (int i = 0; i < array.size(); i++) 
		{
			InformacionVertice info = array.get(i);
			double dis= getHarvesianDistance(info.getLatitude(), info.getLongitude(), latitud, longitud);
			if( dis <= temp)
			{
				temp = dis;
				infor =  new InformacionVertice(info.getLatitude(), info.getLongitude());

			}

		}

		return infor.getLatitude()+","+infor.getLongitude();
	}

	public void cargarCsv() 
	{
		// TNODE_ID ; PRE_DIR ; STREET_NAM ; STREET_TYP ; PRE_DIR ; ONEWAY_DIR ; the_geom
		try 
		{
			FileReader fr = new FileReader(new File("data/Chicago Streets.csv"));
			BufferedReader br = new BufferedReader(fr);
			br.readLine();
			String l = br.readLine();

			String posicionesCalle[];
			double latitude = 0;
			double longitude = 0;

			while(l != null && !l.contains("MULTILINESTRING EMPTY"))
			{
				String[] arreglo = l.split(";");

				posicionesCalle = new String[arreglo.length-6];
				for (int i = 6; i < arreglo.length; i++) 
				{
					if(i > 6)
					{
						arreglo[i] = arreglo[i].substring(1, arreglo[i].length());
					}
					posicionesCalle[i-6] = arreglo[i];
					latitude = Double.parseDouble(posicionesCalle[i-6].split(" ")[1]);
					longitude = Double.parseDouble(posicionesCalle[i-6].split(" ")[0]);
					infoCsv.add(new InformacionVertice(latitude, longitude));
				}

				l = br.readLine();
			}


			fr.close();
			br.close();

		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	public InformacionVertice obtenerInfoAleatoria()
	{
		int pos = (int)(Math.random()*100)%(infoCsv.size()+1);
		return infoCsv.get(pos);
	}

	public boolean existsVertex(Vertex<String, InformacionVertice, InformacionArcoServicios> v)
	{
		return registerVertices.get(v.getIdNum()) != null;
	}

	public boolean existsEdge(Edge<String, InformacionArcoServicios> v)
	{
		return registEdges.get(v.getIdVertStart()+v.getIdVertEnd()) != null;
	}

	public int darNumComponentes()
	{
		return numComponentes;
	}

	public void darNumServicios()
	{
		ArrayListNotComparable<Vertex<String, InformacionVertice, InformacionArcoServicios>> vertices = graph.getListOfVertices();
		int contador = 0;

		for (int i = 0; i < vertices.size(); i++) 
		{
			InformacionVertice info = vertices.get(i).getValue();
			contador += info.getOutgoingServices().size() + info.getIncomingServices().size();
		}
		numServicios = contador;
	}



}
