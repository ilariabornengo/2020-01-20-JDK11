package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {

	ArtsmiaDAO dao;
	Graph<Artist,DefaultWeightedEdge> grafo;
	Map<Integer,Artist> idMap;
	private List<Artist> best;
	
	public Model()
	{
		this.dao=new ArtsmiaDAO();
	}
	
	public void creaGrafo(String ruolo)
	{
		this.grafo=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.idMap=new HashMap<Integer,Artist>();
		
		//aggiungo i vertici
		this.dao.getVertici(idMap, ruolo);
		Graphs.addAllVertices(this.grafo, idMap.values());
		
		//aggiungo gli archi
		for (Adiacenza a:this.dao.getAdiacenze(idMap, ruolo))
		{
			if(this.grafo.containsVertex(a.getA1())&& this.grafo.containsVertex(a.getA2()))
			{
				Graphs.addEdge(this.grafo, a.getA1(), a.getA2(), a.getPeso());
			}
		}
		System.out.println("GRAFO CREATO");
		System.out.println("# VERTICI: "+this.grafo.vertexSet().size());
		System.out.println("# ARCHI: "+this.grafo.edgeSet().size());
		
	}
	
	public List<Adiacenza> getArtistiConnessi(String ruolo)
	{
		List<Adiacenza> artistiConnessi=new ArrayList<Adiacenza>(this.dao.getAdiacenze(idMap,ruolo));
		Collections.sort(artistiConnessi, new ComparatorPesoA());
		return artistiConnessi;
	}
	public List<Artist> trovaPercorso(Integer id)
	{
		this.best=new ArrayList<Artist>();
		List<Artist> parziale=new ArrayList<Artist>();
		Artist a=null;
		boolean trovato=false;
		for(Artist ar:this.grafo.vertexSet())
		{
			if(ar.getArtistID()==id)
			{
				a=ar;
			}
		}
		parziale.add(a);
		//lancio la ricorsione
		//con -1 perchè non ho ancora livello
		ricorsione(parziale,-1);
		return best;
		
	}
	
	private void ricorsione(List<Artist> parziale, int peso) {
		//devo iterare sui vicini dell'ultimo elemento
		Artist ultimo=parziale.get(parziale.size()-1);
		//ottengo i vicini
		List<Artist> vicini=Graphs.neighborListOf(this.grafo, ultimo);
		
		//nel primo caso basta che non sia già in parziale
		for(Artist vicino:vicini )
		{
			if(!parziale.contains(vicino)&& peso==-1)
			{	//nel primo caso basta che non sia già in parziale, aggiungo il primo elemento
				parziale.add(vicino);
				ricorsione(parziale,(int)this.grafo.getEdgeWeight(this.grafo.getEdge(ultimo, vicino)));
				parziale.remove(vicino);
			}
			else if(!parziale.contains(vicino) && this.grafo.getEdgeWeight(this.grafo.getEdge(ultimo, vicino))==peso)
			{
				//basta che non sia contenuto in parziale e che il peso sia lo stesso
				parziale.add(vicino);
				ricorsione(parziale,peso);
				parziale.remove(vicino);
			}
		}
		if(parziale.size()>best.size())
		{
			this.best=new ArrayList<>(parziale);
		}
		
	}
	
	public boolean contieneGrafo(Integer i)
	{
		Artist a=null;
		for(Artist ar:this.grafo.vertexSet())
		{
			if(ar.getArtistID()==i)
			{
				ar=a;
				return true;
			}
		}
		return false;
	}

	public Graph<Artist, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}

	public void setGrafo(Graph<Artist, DefaultWeightedEdge> grafo) {
		this.grafo = grafo;
	}

	public int getVertici()
	{
		return this.grafo.vertexSet().size();
	}
	
	public int getArchi()
	{
		return this.grafo.edgeSet().size();
	}
	
	public List<String> getRuoli()
	{
		List<String> ruoli=new ArrayList<String>(this.dao.listRuoli());
		Collections.sort(ruoli, new comparatorAlfaR());
		return ruoli;
	}

	

	public ArtsmiaDAO getDao() {
		return dao;
	}

	
	
}
