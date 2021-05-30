package it.polito.tdp.artsmia.model;

import java.util.Comparator;

public class ComparatorPesoA implements Comparator<Adiacenza> {

	@Override
	public int compare(Adiacenza o1, Adiacenza o2) {
		// TODO Auto-generated method stub
		return o2.getPeso().compareTo(o1.getPeso());
	}

}
