package it.polito.tdp.artsmia.model;

public class Artist {

	Integer artistID;
	String nome;
	public Artist(Integer artistID, String nome) {
		super();
		this.artistID = artistID;
		this.nome = nome;
	}
	public Integer getArtistID() {
		return artistID;
	}
	public void setArtistID(Integer artistID) {
		this.artistID = artistID;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((artistID == null) ? 0 : artistID.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Artist other = (Artist) obj;
		if (artistID == null) {
			if (other.artistID != null)
				return false;
		} else if (!artistID.equals(other.artistID))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return nome +" - "+artistID;
	}
	
	
}
