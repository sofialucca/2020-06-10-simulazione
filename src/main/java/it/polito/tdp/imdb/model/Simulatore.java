package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.imdb.model.Evento.EventType;

public class Simulatore {

	private Graph<Actor, DefaultWeightedEdge> grafo;
	private PriorityQueue<Evento> queue;
	
	//valori fissi
	private int nGiorni;
	
	//varianti
	private List<Actor> nonIntervistati;
	
	//output
	private int NPAUSE;
	private List<Actor> INTERVISTATI;
	
	public void init(Graph<Actor, DefaultWeightedEdge> grafo, int n) {
		this.grafo = grafo;
		nGiorni = n;
		queue = new PriorityQueue<>();
		
		nonIntervistati = new ArrayList<>(grafo.vertexSet());
		
		NPAUSE = 0;
		INTERVISTATI = new ArrayList<>();
		
		Actor a = this.getAttoreCasuale();
		queue.add(new Evento(1, a, EventType.INTERVISTA));

		
	}
	
	public void run() {
		Evento e;
		String gender = null;
		do {
			e = queue.poll();
			Actor a = e.getActor();
			int gg = e.getGiorno();
			
			switch(e.getTipo()) {
				case INTERVISTA:
					INTERVISTATI.add(a);
					nonIntervistati.remove(a);
					if(a.getGender().equals(gender)) {
						double prob = Math.random();
						if(prob <= 0.9) {
							queue.add(new Evento(gg+1,null, EventType.PAUSA));
							break;
						}
					}
					double prob = Math.random();
					Actor nuovoAttore = null;
					if(prob <= 0.6) {
						nuovoAttore = getAttoreCasuale();
					}else {
						List<Actor> colleghi = ottieniColleghi(a);
						if(!colleghi.isEmpty()) {
							int indice = (int) Math.random()*colleghi.size();
							nuovoAttore = this.nonIntervistati.get(indice);								
						}else {
							nuovoAttore = getAttoreCasuale();
						}
					}
					queue.add(new Evento(gg+1, nuovoAttore, EventType.INTERVISTA));
					gender = a.getGender();
					break;
				case PAUSA:
					this.NPAUSE ++;
					queue.add(new Evento(gg + 1, this.getAttoreCasuale(), EventType.INTERVISTA));
					gender = null;
					break;
					
			}
		}while(e.getGiorno() < nGiorni);
	}

	private List<Actor> ottieniColleghi(Actor a) {
		List<Actor> result = new ArrayList<>();
		double pesoMax = 0;
		for(Actor ac: Graphs.neighborListOf(grafo, a)) {
			DefaultWeightedEdge e = grafo.getEdge(a, ac);
			double nuovoPeso = grafo.getEdgeWeight(e);
			if( nuovoPeso >= pesoMax) {
				if(nuovoPeso != pesoMax) {
					pesoMax = nuovoPeso;
					result = new ArrayList<>();
				}
					result.add(ac);
			}
		}
		return result;
	}
	
	public Actor getAttoreCasuale() {
		int indice = (int) (Math.random()*this.nonIntervistati.size());
		return this.nonIntervistati.get(indice);
	}
	
	public int getPause() {
		return NPAUSE;
	}
	
	public List<Actor> getIntervistati(){
		return INTERVISTATI;
	}
}
