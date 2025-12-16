package com.mentiroso_servidor.MentirosoServidor;

import java.util.ArrayList;

public class Juego {
	private ArrayList<Partida> listaPartida;

	public Juego() {
		listaPartida = new ArrayList<>();
	}

	public ArrayList<Partida> getListaPartida() {
		return listaPartida;
	}


	public void setListaPartida(ArrayList<Partida> listaPartida) {
		this.listaPartida = listaPartida;
	}

	public int numeroPartidas() {
		return listaPartida.size();
	}

	public void addPartida(Partida nuevaPartida) {
		listaPartida.add(nuevaPartida);
	}
	
	@Override
	public String toString() {
		return "Juego [listaPartida=" + listaPartida + "]";
	}
}
