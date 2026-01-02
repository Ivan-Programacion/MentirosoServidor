package com.mentiroso_servidor.MentirosoServidor;

import java.util.ArrayList;

public class Jugada {

	private String tipoJugada;
	private ArrayList<String> valoresJugada;
	private Jugador jugador;

	public Jugada(String tipoJugada, ArrayList<String> valoresJugada, Jugador jugador) {
		this.tipoJugada = tipoJugada;
		this.valoresJugada = valoresJugada;
		this.jugador = jugador;
	}

	public String getTipoJugada() {
		return tipoJugada;
	}

	public void setTipoJugada(String tipoJugada) {
		this.tipoJugada = tipoJugada;
	}

	public ArrayList<String> getValoresJugada() {
		return valoresJugada;
	}

	public void setValoresJugada(ArrayList<String> valoresJugada) {
		this.valoresJugada = valoresJugada;
	}

	public Jugador getJugador() {
		return jugador;
	}

	public void setJugador(Jugador jugador) {
		this.jugador = jugador;
	}

}
