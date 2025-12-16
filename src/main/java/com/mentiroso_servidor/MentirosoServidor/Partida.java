package com.mentiroso_servidor.MentirosoServidor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Partida {
	private int id;
	private int idJugadorActual;
	private int rondas;
	private HashMap<String, ArrayList<String>> baraja;
	private ArrayList<Jugador> jugadores;

	public Partida(int id, int idJugadorActual) {
		this.id = id;
		this.idJugadorActual = idJugadorActual;
		rondas = 1;
		baraja = new HashMap<>();
		jugadores = new ArrayList<>();
	}

	public void crearBaraja() {
		String[] listaCartas = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
		ArrayList<String> cartas = new ArrayList<>(Arrays.asList(listaCartas));
		baraja.put("picas", cartas);
		baraja.put("treboles", cartas);
		baraja.put("corazones", cartas);
		baraja.put("diamantes", cartas);
	}

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

	public HashMap<String, ArrayList<String>> getBaraja() {
		return baraja;
	}

	public void setBaraja(HashMap<String, ArrayList<String>> baraja) {
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
