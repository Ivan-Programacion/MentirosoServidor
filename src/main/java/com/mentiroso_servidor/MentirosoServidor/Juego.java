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

	@Override
	public String toString() {
		return "Juego [listaPartida=" + listaPartida + "]";
	}
}
