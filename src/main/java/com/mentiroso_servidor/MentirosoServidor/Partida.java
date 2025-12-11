package com.mentiroso_servidor.MentirosoServidor;

import java.util.ArrayList;
import java.util.HashMap;

public class Partida {
	private int id;
	private int idJugadorActual;
	private int rondas;
	private HashMap<String, ArrayList<String>[]> baraja;
	private ArrayList<Jugador> jugadores;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdJugadorActual() {
		return idJugadorActual;
	}

	public void setIdJugadorActual(int idJugadorActual) {
		this.idJugadorActual = idJugadorActual;
	}

	public int getRondas() {
		return rondas;
	}

	public void setRondas(int rondas) {
		this.rondas = rondas;
	}

	public HashMap<String, ArrayList<String>[]> getBaraja() {
		return baraja;
	}

	public void setBaraja(HashMap<String, ArrayList<String>[]> baraja) {
		this.baraja = baraja;
	}

	public ArrayList<Jugador> getJugadores() {
		return jugadores;
	}

	public void setJugadores(ArrayList<Jugador> jugadores) {
		this.jugadores = jugadores;
	}

	@Override
	public String toString() {
		return "Partida [id=" + id + ", idJugadorActual=" + idJugadorActual + ", rondas=" + rondas + ", baraja="
				+ baraja + ", jugadores=" + jugadores + "]";
	}
}
