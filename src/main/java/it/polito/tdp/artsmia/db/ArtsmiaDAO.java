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
	
	public List<String> getCategorie() {
			
			String sql = "SELECT DISTINCT a.role as r "
					+ "FROM authorship a "
					+ "ORDER BY a.role ";
			List<String> result = new ArrayList<>();
			Connection conn = DBConnect.getConnection();
	
			try {
				PreparedStatement st = conn.prepareStatement(sql);
				ResultSet res = st.executeQuery();
				while (res.next()) {
					
					result.add(res.getString("r"));
				}
				conn.close();
				return result;
				
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}


	public void getVertici(Map<Integer,Artist> idMap,String categoria) {
		
		String sql = "SELECT distinct ar.artist_id AS id, ar.name AS nome "
				+ "FROM authorship a, artists ar "
				+ "WHERE a.artist_id=ar.artist_id "
				+ "AND a.role=? " ;
		
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
		
		String sql = "SELECT a1.artist_id AS id1, a2.artist_id AS id2, count(eo1.exhibition_id) AS peso "
				+ "FROM objects o1, objects o2, exhibition_objects eo1,exhibition_objects eo2, authorship a1, authorship a2 "
				+ "WHERE o1.object_id=eo1.object_id AND a1.object_id=o1.object_id "
				+ "AND o2.object_id=eo2.object_id AND a2.object_id=o2.object_id "
				+ "AND a1.artist_id> a2.artist_id "
				+ "AND eo1.exhibition_id=eo2.exhibition_id "
				+ "AND a1.role=a2.role "
				+ "AND a1.role=? "
				+ "GROUP BY a1.artist_id,a2.artist_id " ;
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
