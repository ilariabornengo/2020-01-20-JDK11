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
	Map<Integer,Artist> idMap;
	Graph<Artist,DefaultWeightedEdge> grafo;
	List<Artist> listaBest;
	
	public Model()
	{
		this.dao=new ArtsmiaDAO();
	}
	
	public List<String> getCategorie()
	{
		return this.dao.getRuoli();
	}
	public void creaGrafo(String categoria)
	{
		this.idMap=new HashMap<Integer,Artist>();
		this.grafo=new SimpleWeightedGraph<Artist,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//aggiungo i vertici
		this.dao.getVertici(idMap, categoria);
		Graphs.addAllVertices(this.grafo, this.idMap.values());
		
		//aggiungo gli archi
		for(Adiacenza a:this.dao.getAdiacenze(idMap, categoria))
		{
			if(this.grafo.vertexSet().contains(a.getA1()) && this.grafo.vertexSet().contains(a.getA2()))
			{
				Graphs.addEdge(this.grafo, a.getA1(), a.getA2(), a.getPeso());
			}
		}
	}
	
	public List<Adiacenza> liste(String categoria)
	{
		List<Adiacenza> lista=new ArrayList<Adiacenza> ();
		for(Adiacenza a:this.dao.getAdiacenze(idMap, categoria))
		{
			lista.add(a);
		}
		Collections.sort(lista, new ComparatorPesoA());
		return lista;
	}
	public int getArchi()
	{
		return this.grafo.edgeSet().size();
	}
	public int getVertici()
	{
		return this.grafo.vertexSet().size();
	}
	
	public List<Artist> percorsoMassimo(int idA)
	{
		Artist a=null;
		for(Artist ar:this.grafo.vertexSet())
		{
			if(ar.getArtistID()==idA)
			{
				a=ar;
			}
		}
		this.listaBest=new ArrayList<Artist>();
		List<Artist> parziale=new ArrayList<Artist>();
		parziale.add(a);
		ricorsione(parziale,-1);
		return this.listaBest;
		
	}

	private void ricorsione(List<Artist> parziale,int peso) {
		Artist ultimo=parziale.get(parziale.size()-1);
		
		for(Artist ar:Graphs.neighborListOf(this.grafo, ultimo))
		{
			// caso iniziale, devo settare il peso
			if(!parziale.contains(ar) && peso==-1)
			{
				parziale.add(ar);
				peso=(int) this.grafo.getEdgeWeight(this.grafo.getEdge(ultimo, ar));
				ricorsione(parziale,peso);
				parziale.remove(ar);
			}else if(!parziale.contains(ar) && this.grafo.getEdgeWeight(this.grafo.getEdge(ultimo, ar))==peso)
			{
				parziale.add(ar);
				ricorsione(parziale,peso);
				parziale.remove(ar);
			}
		}
		if(parziale.size()>this.listaBest.size())
		{
			this.listaBest=new ArrayList<Artist>(parziale);
		}
	}
}
