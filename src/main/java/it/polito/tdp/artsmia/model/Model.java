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
	public List<Adiacenza> getConnesso(String categoria)
	{
		List<Adiacenza> OK=new ArrayList<Adiacenza>();
		for(Adiacenza a:this.dao.getAdiacenze(idMap, categoria))
		{
			OK.add(a);
		}
		Collections.sort(OK, new ComparatorNExp());
		return OK;
		
	}
	public List<Artist> getListaBest(Artist a)
	{
		this.listaBest=new ArrayList<Artist>();
		List<Artist> parziale=new ArrayList<Artist>();
		parziale.add(a);
		ricorsione(parziale,-1);
		return this.listaBest;
		
	}
	public List<Artist> artisti()
	{
		List<Artist> arti=new ArrayList<Artist>(this.grafo.vertexSet());
		return arti;
	}
	private void ricorsione(List<Artist> parziale, int pesoI) {
		Artist ultimo=parziale.get(parziale.size()-1);
		
		for(Artist a:Graphs.neighborListOf(this.grafo, ultimo))
		{
			int peso=0;
			if(pesoI==-1)
			{
				if(!parziale.contains(a))
				{
					parziale.add(a);
					peso=(int) this.grafo.getEdgeWeight(this.grafo.getEdge(ultimo, a));
					ricorsione(parziale,peso);
					parziale.remove(a);
				}
			}else{
				int pesoA=(int) this.grafo.getEdgeWeight(this.grafo.getEdge(ultimo, a));
				if(pesoA==peso)
				{
					if(!parziale.contains(a))
					{
						parziale.add(a);
						ricorsione(parziale,peso);
						parziale.remove(a);
					}
				}
			}
		}
		
		if(parziale.size()>this.listaBest.size())
		{
			this.listaBest=new ArrayList<Artist>(parziale);
			return;
		}
		
	}

	public int getArchi() 
	{
		return this.grafo.edgeSet().size();
	}
	public int getVertici()
	{
		return this.grafo.vertexSet().size();
	}
	
	public List<String> categorie()
	{
		return this.dao.getCategorie();
	}
}
