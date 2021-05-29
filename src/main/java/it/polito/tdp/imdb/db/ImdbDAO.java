package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Adiacenti;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public List<Actor> listAllActors(){
		String sql = "SELECT * FROM actors";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				
				result.add(actor);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Director> listAllDirectors(){
		String sql = "SELECT * FROM directors";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				result.add(director);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void setActors(Map<Integer, Actor> idMap, String genere) {
		String sql = "SELECT DISTINCT a.id, first_name, last_name, gender "
				+ "FROM actors a, roles r, movies_genres m "
				+ "WHERE a.id = r.actor_id AND r.movie_id = m.movie_id "
				+ "AND genre = ?";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genere);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("a.id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				if(!idMap.containsKey(actor.getId())) {
					idMap.put(actor.getId(), actor);
				}
				
			}
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public List<Adiacenti> getArchi(Map<Integer,Actor> mappa, String genere){
		String sql = "SELECT a1.id, a2.id, COUNT(m1.movie_id) as peso "
				+ "FROM actors a1, roles r1, movies_genres m1, actors a2, roles r2, movies_genres m2 "
				+ "WHERE a1.id = r1.actor_id AND r1.movie_id = m1.movie_id "
				+ "AND m1.genre = ? "
				+ "AND a2.id = r2.actor_id AND r2.movie_id = m2.movie_id AND m2.genre = m1.genre "
				+ "AND m1.movie_id = m2.movie_id AND a1.id > a2.id "
				+ "GROUP BY a1.id, a2.id";
		List<Adiacenti> result = new ArrayList<>();
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genere);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor a1 = mappa.get(res.getInt("a1.id"));
				Actor a2 = mappa.get(res.getInt("a2.id"));
				if(a1 != null && a2 != null) {
					result.add(new Adiacenti(a1,a2,res.getInt("peso")));
				}
				
			}
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
		
	}
	
	public List<String> getGeneri(){
		String sql = "SELECT DISTINCT genre "
				+ "FROM movies_genres "
				+ "ORDER BY genre";
		
		List<String> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.add(res.getString("genre"));

				
			}
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	
	
}
