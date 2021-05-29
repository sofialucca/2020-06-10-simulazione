package it.polito.tdp.imdb.model;

public class TestModel {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Model m = new Model();
		
		m.creaGrafo("Horror");
		
		m.setSim(10);
		System.out.println(m.getPause());
		System.out.println(m.getIntervistati());


	}

}
