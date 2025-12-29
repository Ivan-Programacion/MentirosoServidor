package com.mentiroso_servidor.MentirosoServidor;

import java.util.List;

public class Jugada {
	
	private String tipoJugada;
	private List<String> valoresJugada;
	private Jugador jugador;
	
	public Jugada(String tipoJugada, List<String> valoresJugada, Jugador jugador) {
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

	public List<String> getValoresJugada() {
		return valoresJugada;
	}

	public void setValoresJugada(List<String> valoresJugada) {
		this.valoresJugada = valoresJugada;
	}

	public Jugador getJugador() {
		return jugador;
	}

	public void setJugador(Jugador jugador) {
		this.jugador = jugador;
	}
	
	
	
	
	

}
