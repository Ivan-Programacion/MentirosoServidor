package com.mentiroso_servidor.MentirosoServidor;

import java.util.ArrayList;
import java.util.HashMap;

public class Partida {
	private int id;
	private int idJugadorActual;
	private int rondas;
	private HashMap<String, ArrayList<String>[]> baraja;
	private ArrayList<Jugador> jugadores;
}
