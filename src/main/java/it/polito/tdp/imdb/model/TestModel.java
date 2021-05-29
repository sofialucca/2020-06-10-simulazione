package it.polito.tdp.imdb.model;

public class TestModel {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Model m = new Model();
		
		m.creaGrafo("Horror");
		
		for(Actor a : m.getActors()) {
			System.out.println(a);			
		}

	}

}
