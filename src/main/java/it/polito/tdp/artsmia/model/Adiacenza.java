package it.polito.tdp.artsmia.model;

public class Adiacenza {
	
	Artist a1;
	Artist a2;
	Integer peso;
	public Adiacenza(Artist a1, Artist a2, Integer peso) {
		super();
		this.a1 = a1;
		this.a2 = a2;
		this.peso = peso;
	}
	public Artist getA1() {
		return a1;
	}
	public void setA1(Artist a1) {
		this.a1 = a1;
	}
	public Artist getA2() {
		return a2;
	}
	public void setA2(Artist a2) {
		this.a2 = a2;
	}
	public Integer getPeso() {
		return peso;
	}
	public void setPeso(Integer peso) {
		this.peso = peso;
	}
	@Override
	public String toString() {
		return a1.getNome() + " - " + a2.getNome() + " peso= " + peso;
	}
	

	
}
