package it.polito.tdp.imdb.model;

public class Evento implements Comparable<Evento>{

	enum EventType{
		INTERVISTA,
		PAUSA
	}

	
	private Integer giorno;
	private Actor actor;
	private EventType tipo;
	
	public Evento(Integer giorno, Actor actor, EventType tipo) {
		super();
		this.actor = actor;
		this.tipo = tipo;

		this.giorno = giorno;
	}

	public Actor getActor() {
		return actor;
	}

	public void setActor(Actor actor) {
		this.actor = actor;
	}

	public EventType getTipo() {
		return tipo;
	}

	public void setTipo(EventType tipo) {
		this.tipo = tipo;
	}

	public Integer getGiorno() {
		return giorno;
	}

	public void setGiorno(Integer giorno) {
		this.giorno = giorno;
	}

	@Override
	public int compareTo(Evento o) {
		// TODO Auto-generated method stub
		return this.giorno.compareTo(o.giorno);
	}
	
	
}
