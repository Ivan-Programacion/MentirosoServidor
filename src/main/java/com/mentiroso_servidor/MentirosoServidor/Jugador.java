package com.mentiroso_servidor.MentirosoServidor;

import java.util.ArrayList;

public class Jugador {
	private int id;
	private String nombre;
	private ArrayList<String> mano;
	private boolean mentiroso;

	public Jugador(String nombre, int id) {
		this.nombre = nombre;
		this.id = id;
		this.mano = new ArrayList<>();
		mentiroso = false;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public ArrayList<String> getMano() {
		return mano;
	}

	public void setMano(ArrayList<String> mano) {
		this.mano = mano;
	}

	public boolean isMentiroso() {
		return mentiroso;
	}

	public void setMentiroso(boolean mentiroso) {
		this.mentiroso = mentiroso;
	}

	@Override
	public String toString() {
		return "Jugador [id=" + id + ", nombre=" + nombre + ", mano=" + mano + "]";
	}

}
