package com.mentiroso_servidor.MentirosoServidor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Partida {
	private int id;
	private int rondas;
	private HashMap<String, ArrayList<String>> baraja;
	private ArrayList<Jugador> jugadores;
	private int idActual;
	private Jugada ultimaJugada;
	private Jugador ultimoJugador;

	public Partida(int id) {
		this.id = id;
		rondas = 1;
		baraja = new HashMap<>();
		jugadores = new ArrayList<>();
		idActual = 1;
	}

	public void crearBaraja() {
		String[] listaCartas = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K" };
		ArrayList<String> cartasP = new ArrayList<>(Arrays.asList(listaCartas));
		ArrayList<String> cartasT = new ArrayList<>(Arrays.asList(listaCartas));
		ArrayList<String> cartasC = new ArrayList<>(Arrays.asList(listaCartas));
		ArrayList<String> cartasD = new ArrayList<>(Arrays.asList(listaCartas));
		baraja.put("picas", cartasP);
		baraja.put("treboles", cartasT);
		baraja.put("corazones", cartasC);
		baraja.put("diamantes", cartasD);
	}

	public int numJugadores() {
		return jugadores.size();
	}

	public void addJugador(Jugador j) {
		jugadores.add(j);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getIdActual() {
		return idActual;
	}

	public void setIdActual(int idActual) {
		this.idActual = idActual;
	}

	public Jugada getUltimaJugada() {
		return ultimaJugada;
	}

	public void setUltimaJugada(Jugada ultimaJugada) {
		this.ultimaJugada = ultimaJugada;
	}

	public Jugador getUltimoJugador() {
		return ultimoJugador;
	}

	public void setUltimoJugador(Jugador ultimoJugador) {
		this.ultimoJugador = ultimoJugador;
	}

	@Override
	public String toString() {
		return "Partida [id=" + id + ", rondas=" + rondas + ", baraja=" + baraja + ", jugadores=" + jugadores + "]";
	}
}
