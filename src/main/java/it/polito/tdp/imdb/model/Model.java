package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {

	private Graph<Actor, DefaultWeightedEdge> grafo ;
	private ImdbDAO dao;
	private Map<Integer, Actor> idMap;
	private Map<Actor, Actor> predecessore;
	
	public Model() {
		dao = new ImdbDAO();
	}
	
	public void creaGrafo(String genere) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		idMap = new HashMap<>();
		
		//vertici
		dao.setActors(idMap, genere);
		Graphs.addAllVertices(grafo, idMap.values());
		
		//archi
		for(Adiacenti a : dao.getArchi(idMap, genere)) {
			Graphs.addEdge(grafo, a.getA1(), a.getA2(), a.getPeso());
		}
		
		System.out.println("GRAFO CREATO:");
		System.out.println("# vertici: " + grafo.vertexSet().size());
		System.out.println("#archi: " + grafo.edgeSet().size());
	}
	
	public List<String> getGeneri(){
		return dao.getGeneri();
	}
	
	public List<Actor> getAttoriRaggiungibili(Actor partenza){
		
		BreadthFirstIterator<Actor,DefaultWeightedEdge> bfv = new BreadthFirstIterator<>(this.grafo,partenza);
		
		this.predecessore = new HashMap<>();
		this.predecessore.put(partenza, null);

		bfv.addTraversalListener(new TraversalListener<Actor, DefaultWeightedEdge>(){
			
			@Override
			public void connectedComponentFinished(ConnectedComponentTraversalEvent e) {
			}

			@Override
			public void connectedComponentStarted(ConnectedComponentTraversalEvent e) {
			}

			@Override
			public void edgeTraversed(EdgeTraversalEvent<DefaultWeightedEdge> e) {
				DefaultWeightedEdge arco = e.getEdge();
				Actor a = grafo.getEdgeSource(arco);
				Actor b = grafo.getEdgeTarget(arco);
				if(predecessore.containsKey(b) && !predecessore.containsKey(a)) {
					predecessore.put(a, b);
				}else if(predecessore.containsKey(a) && !predecessore.containsKey(b)){
					predecessore.put(b, a);
				}
			}

			@Override
			public void vertexTraversed(VertexTraversalEvent<Actor> e) {	
			}

			@Override
			public void vertexFinished(VertexTraversalEvent<Actor> e) {
			}
			
		});
		
		List<Actor> result = new ArrayList<>();
		
		while(bfv.hasNext()) {
			Actor a = bfv.next();
			result.add(a);
		}
		result.remove(partenza);
		Collections.sort(result);
		return result;
	}
	
	public int getEdgeSize() {
		return grafo.edgeSet().size();
	}
	
	public List<Actor> getActors(){
		List<Actor> attori = new ArrayList<>(grafo.vertexSet());
		Collections.sort(attori);
		return attori;
	}
}
