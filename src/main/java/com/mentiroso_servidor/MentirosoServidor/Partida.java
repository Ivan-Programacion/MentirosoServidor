package com.mentiroso_servidor.MentirosoServidor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class Partida {
	private int id;
	private int rondas;
	private HashMap<String, ArrayList<String>> baraja;
	private ArrayList<Jugador> jugadores;

	public Partida(int id) {
		this.id = id;
		rondas = 1;
		baraja = new HashMap<>();
		jugadores = new ArrayList<>();
	}

	public void crearBaraja() {
		String[] listaCartas = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K" };
		ArrayList<String> cartas = new ArrayList<>(Arrays.asList(listaCartas));
		baraja.put("picas", cartas);
		baraja.put("treboles", cartas);
		baraja.put("corazones", cartas);
		baraja.put("diamantes", cartas);
	}
	
	public int numJugadores() {
		return jugadores.size();
	}
	
	public void addJugador(Jugador j) {
		jugadores.add(j);
	}

//	public String repartirCartas() {
//		Random random = new Random();
//
//		int contador = 0;
//		String mano = "";
//		while (contador < 5) {
//
//			String[] listaPalos = { "picas", "treboles", "corazones", "diamantes" };
//			int paloAleatorio = random.nextInt(4);
//			String paloEscogido = listaPalos[paloAleatorio];
//
//			for (Entry<String, ArrayList<String>> entry : baraja.entrySet()) {
//				String clave = entry.getKey();
//
//				if (clave.equals(paloEscogido)) {
//					int numeroAleatorio = random.nextInt(entry.getValue().size());
//
//					if (!entry.getValue().get(numeroAleatorio).equals("0")) {
//						mano += entr
//					}
//				}
//			}
//			contador++;
//		}
//		return "";
//	}

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

	@Override
	public String toString() {
		return "Partida [id=" + id + ", rondas=" + rondas + ", baraja=" + baraja + ", jugadores=" + jugadores + "]";
	}
}
