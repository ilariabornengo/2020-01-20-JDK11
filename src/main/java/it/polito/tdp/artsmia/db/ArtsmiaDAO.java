package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.artsmia.model.Adiacenza;
import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.Artist;
import it.polito.tdp.artsmia.model.Exhibition;

public class ArtsmiaDAO {

	public List<ArtObject> listObjects() {
		
		String sql = "SELECT * from objects";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
				
				result.add(artObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Exhibition> listExhibitions() {
		
		String sql = "SELECT * from exhibitions";
		List<Exhibition> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Exhibition exObj = new Exhibition(res.getInt("exhibition_id"), res.getString("exhibition_department"), res.getString("exhibition_title"), 
						res.getInt("begin"), res.getInt("end"));
				
				result.add(exObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

public List<String> getRuoli() {
		
		String sql = "SELECT distinct a.role as ruolo "
				+ "FROM authorship a " ;
		List<String> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				result.add(res.getString("ruolo"));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void getVertici(Map<Integer,Artist> idMap,String categoria) {
		
		String sql = "SELECT distinct a.artist_id AS id, a.name AS nome "
				+ "FROM artists a, authorship au "
				+ "WHERE au.artist_id=a.artist_id "
				+ "AND au.role=? " ;
		
		Connection conn = DBConnect.getConnection();
	
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, categoria);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(!idMap.containsKey(res.getInt("id")))
				{
					Artist a=new Artist(res.getInt("id"),res.getString("nome"));
					idMap.put(a.getArtistID(), a);
				}
				
			}
			conn.close();
		
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
	}
	

	public List<Adiacenza> getAdiacenze(Map<Integer,Artist> idMap,String categoria) {
			
			String sql = "SELECT a1.artist_id AS id1, a2.artist_id AS id2, count(distinct e1.exhibition_id) AS peso "
					+ "FROM authorship a1, authorship a2, objects o1, objects o2, exhibition_objects e1, exhibition_objects e2 "
					+ "WHERE a1.object_id=o1.object_id AND o1.object_id=e1.object_id "
					+ "AND a2.object_id=o2.object_id AND o2.object_id=e2.object_id "
					+ "AND a1.role=a2.role AND a1.role=? "
					+ "AND a1.artist_id> a2.artist_id "
					+ "AND a1.object_id!=o2.object_id "
					+ "AND e1.exhibition_id=e2.exhibition_id "
					+ "GROUP BY a1.artist_id, a2.artist_id " ;
			List<Adiacenza> result = new ArrayList<>();
			Connection conn = DBConnect.getConnection();
	
			try {
				PreparedStatement st = conn.prepareStatement(sql);
				st.setString(1, categoria);
				ResultSet res = st.executeQuery();
				while (res.next()) {
				if(idMap.containsKey(res.getInt("id1")) && idMap.containsKey(res.getInt("id2")))
				{
					Adiacenza a=new Adiacenza(idMap.get(res.getInt("id1")),idMap.get(res.getInt("id2")),res.getInt("peso"));
					result.add(a);
				}
					
				}
				conn.close();
				return result;
				
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
}
