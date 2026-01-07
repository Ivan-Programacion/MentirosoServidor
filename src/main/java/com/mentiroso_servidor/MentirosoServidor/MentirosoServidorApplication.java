package com.mentiroso_servidor.MentirosoServidor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class MentirosoServidorApplication {

	private Juego juego = new Juego(); // Iniciamos el juego con el arranque del servidor

	public static void main(String[] args) {
		SpringApplication.run(MentirosoServidorApplication.class, args);

	}

	@GetMapping("/prueba")
	public String prueba() {
		return "Esto es una prueba";
	}

	@GetMapping("/conexion")
	public String conexion() {
		return "Conexión establecida";
	}

	@GetMapping("/crearPartida/{unNombre}")
	public String crearPartida(@PathVariable String unNombre) {
		int numeroPartida = juego.numeroPartidas() + 1;
		Partida partida = new Partida(numeroPartida);
		partida.crearBaraja();
		int numeroJugador = partida.numJugadores() + 1;
		Jugador jugador = new Jugador(unNombre, numeroJugador);
		repartirCartas(partida, jugador);
		partida.addJugador(jugador);
		juego.addPartida(partida);
		String cartasJugador = String.join(",", jugador.getMano());
		String respuestaServidor = numeroJugador + ":" + cartasJugador + "," + numeroPartida;
		System.out.println(juego.toString()); // PRUEBA --------------------------------------------------------------
		return respuestaServidor;

	}

	@GetMapping("/unir/{nombre}/{idPartida}")
	public String unir(@PathVariable String nombre, @PathVariable int idPartida) {
		String mensaje = "-1";
		if (!juego.getListaPartida().isEmpty() && juego.getListaPartida() != null) {
			for (Partida partida : juego.getListaPartida()) {
				if (partida.getId() != idPartida)
					mensaje = "-1";
				else if (partida.getRondas() > 1)
					return "-2"; // Devolvemos el valor a la función para que no siga buscando más
				else if (partida.getJugadores().size() == 10)
					return "-3";
				else {
					int idJugador = partida.numJugadores() + 1;
					Jugador jugador = new Jugador(nombre, idJugador);
					repartirCartas(partida, jugador);
					partida.addJugador(jugador);
					String cartasJugador = String.join(",", jugador.getMano());
					String jugadores = "";
					for (int i = 0; i < partida.getJugadores().size(); i++) {
						if (i == partida.getJugadores().size() - 1)
							jugadores += partida.getJugadores().get(i).getNombre() + ":";
						else
							jugadores += partida.getJugadores().get(i).getNombre() + ",";
					}
					System.out.println(juego.toString()); // PRUEBA ------------------------------------------------
					return jugadores + cartasJugador + ":" + idJugador; // Devolvemos el valor a la función para que no
																		// siga buscando más
				}
			}
		}
		return mensaje;
	}

	@GetMapping("/comprobarTurno/{id}/{idPartida}")
	private String comprobarTurno(@PathVariable int id, @PathVariable int idPartida) {
		String mensaje = null;
		for (Partida partida : juego.getListaPartida()) {
			if (partida.getId() == idPartida) {
				if (id == partida.getIdActual()) {
					int numJugadores = partida.getJugadores().size();
					// Hay que pasar la jugada del jugador anterior
					String jugadorAnterior = " ";
					String tipoJugada = " ";
					String valoresJugada = " ";
					// Si no hay jugador anterior, se pasa en blanco
					if (partida.getUltimaJugada() != null) {
						jugadorAnterior = partida.getUltimaJugada().getJugador().getNombre();
						tipoJugada = partida.getUltimaJugada().getTipoJugada();
						valoresJugada = "";
						for (String jugada : partida.getUltimaJugada().getValoresJugada()) {
							valoresJugada += jugada + " ";
						}
					}
					mensaje = "0:" + numJugadores + "," + partida.getRondas() + ":" + jugadorAnterior + "," + tipoJugada
							+ "," + valoresJugada;
				} else {
					// Comprobamos si el jugador existe en la partida
					boolean existeJugador = false;
					for (Jugador j : partida.getJugadores()) {
						if (j.getId() == id) {
							existeJugador = true;
						}
					}

					// Si no obtenemos resultado significa que se ha eliminado
					if (!existeJugador) {
						mensaje = "-2";
					} else {
						// En caso de que exista indicamos a quién le toca
						for (Jugador jugador : partida.getJugadores()) {
							if (partida.getIdActual() == jugador.getId()) {
								mensaje = "-1:" + jugador.getNombre();
							}
						}
					}
				}
			}
		}
		return mensaje;

	}

	@GetMapping("/jugar/{idPartida}/{idJugador}/{tipo}/{valores}")
	public String jugar(@PathVariable int idPartida, @PathVariable int idJugador, @PathVariable String tipo,
			@PathVariable String valores) {

		// Buscamos la partida
		for (Partida partida : juego.getListaPartida()) {
			if (partida.getId() == idPartida) {

				// Vemos si es el turno del jugador, si no devolvemos -1
				if (partida.getIdActual() != idJugador) {
					return "-1";
				}

				// Buscamos el jugador y lo guardamos en un objeto jugador
				Jugador jugadorActual = null;
				for (Jugador j : partida.getJugadores()) {
					if (j.getId() == idJugador)
						jugadorActual = j;
				}

				// Si no se ha guardado datos en el objeto jugador es porque no existe
				if (jugadorActual == null)
					return "-2";
				// Creamos los valores de la jugada
				ArrayList<String> listaValores = new ArrayList<>();
				for (String v : valores.split(",")) {
					listaValores.add(v);
				}
				// Si el jugador se ha guardado correctamente, sumamos en 1 los turnos jugados
				// por el jugador
				jugadorActual.setTurnosJugados(jugadorActual.getTurnosJugados() + 1);

				// Creamos la jugada ejecutada por el jugador y recuperamos la jugada anterior
				Jugada jugada = new Jugada(tipo, listaValores, jugadorActual);
				Jugada jugadaAnterior = partida.getUltimaJugada();
				// Comprobamos la jugada si es válida o no (si es mayor que la que había o no).
				// Si no es válida, retornamos un mensaje de error
				if (!jugadaValida(jugada, jugadaAnterior))
					return "-4";
				// Seteamos la ultima jugada
				partida.setUltimaJugada(jugada);

				// Pasamos al siguiente turno y le pasamos el turno al siguiente
				cambioTurno(partida);
				return "Jugada ejecutada";
			}
		}
		return "-3"; // No se ha encontrado partifda
	}

	@GetMapping("/mentiroso/{idPartida}/{idJugador}")
	public String mentiroso(@PathVariable int idPartida, @PathVariable int idJugador) {
		// Primero hay que buscar la partida
		Partida partida = null;
		for (Partida p : juego.getListaPartida()) {
			if (p.getId() == idPartida) {
				partida = p;
			}
		}
		// Si no se sobreescribe el obejto creado es porque no existe
		if (partida == null)
			return "-1";

		// Verificamos si ha habido última jugada
		Jugada ultimaJugada = partida.getUltimaJugada();
		if (ultimaJugada == null)
			return "-2";

		// Comprobamos si mentía
		Jugador jugadorAcusado = ultimaJugada.getJugador();
		boolean mentira = comprobarMentira(ultimaJugada);
		// Si es mentira, eliminamos al jugador acusado y devolvemos t (de true)
		// Si es verdad, eliminamos al jugador actual y devolvemos f (de false)
		if (mentira) {
			partida.getJugadores().remove(jugadorAcusado);
//			System.err.println("LISTA JUGADORES TRAS ELIMINAR: " + partida.getJugadores().toString()); // PRUEBA -------
			// Reseteamos la jugada anterior
			partida.setUltimaJugada(null);
			cambioTurno(partida);
			return "t";
		} else {
			Jugador acusador = null;
			for (Jugador j : partida.getJugadores()) {
				if (j.getId() == idJugador) {
					acusador = j;
					break;
				}
			}
			if (acusador != null) {
				partida.getJugadores().remove(acusador);
				// Reseteamos la jugada anterior
				partida.setUltimaJugada(null);
				cambioTurno(partida);
			}
			return "f";
		}

	}

	// MÉTODOS NO MAPPEADOS

	private boolean jugadaValida(Jugada jugada, Jugada jugadaAnterior) {
		int valor = 1;
		HashMap<String, Integer> comprobarJugada = new HashMap<>();
		while (valor <= 6) {
			switch (valor) {
			case 1:
				comprobarJugada.put("Carta_alta", valor);
				break;
			case 2:
				comprobarJugada.put("Pareja", valor);
				break;
			case 3:
				comprobarJugada.put("Doble_pareja", valor);
				break;
			case 4:
				comprobarJugada.put("Trío", valor);
				break;
			case 5:
				comprobarJugada.put("Full_House", valor);
				break;
			case 6:
				comprobarJugada.put("Póker", valor);
				break;
			}
			valor++;
		}
		boolean respuesta = true;
		System.err.println("JUGADA -> " + jugada.getValoresJugada().toString()); // PRUEBA -----------------------
		if (jugadaAnterior == null) {
			System.err.println("JUGADA ANTERIOR NULA"); // PRUEBA -----------------------------------------------------
			respuesta = true;
		} else {
			System.err.println("JUGADA ANTERIOR -> " + jugadaAnterior.getValoresJugada().toString()); // PRUEBA
																										// ---------------
			int valorJugadaAnterior = 0;
			int valorJugada = 0;
			for (Map.Entry<String, Integer> entry : comprobarJugada.entrySet()) {
				if (jugada.getTipoJugada().equals(entry.getKey()))
					valorJugada = entry.getValue();
				if (jugadaAnterior.getTipoJugada().equals(entry.getKey()))
					valorJugadaAnterior = entry.getValue();
			}
			System.err.println("valorJugada = " + valorJugada); // PRUEBA ---------------------------------------------
			System.err.println("valorJugadaAnterior = " + valorJugadaAnterior); // PRUEBA -----------
			if (valorJugada > valorJugadaAnterior)
				respuesta = true;
			else if (valorJugada < valorJugadaAnterior)
				respuesta = false;
			else {
				ArrayList<String> valoresCartasOrdenados = new ArrayList<>();
				for (int i = 1; i <= 13; i++) {
					switch (i) {
					case 1:
						valoresCartasOrdenados.add("2");
						break;
					case 2:
						valoresCartasOrdenados.add("3");
						break;
					case 3:
						valoresCartasOrdenados.add("4");
						break;
					case 4:
						valoresCartasOrdenados.add("5");
						break;
					case 5:
						valoresCartasOrdenados.add("6");
						break;
					case 6:
						valoresCartasOrdenados.add("7");
						break;
					case 7:
						valoresCartasOrdenados.add("8");
						break;
					case 8:
						valoresCartasOrdenados.add("9");
						break;
					case 9:
						valoresCartasOrdenados.add("10");
						break;
					case 10:
						valoresCartasOrdenados.add("J");
						break;
					case 11:
						valoresCartasOrdenados.add("Q");
						break;
					case 12:
						valoresCartasOrdenados.add("K");
						break;
					case 13:
						valoresCartasOrdenados.add("A");
						break;
					}
				}
				valorJugada = 0;
				valorJugadaAnterior = 0;
				for (String cartaJugada : jugada.getValoresJugada()) {
					for (int j = 0; j < valoresCartasOrdenados.size(); j++) {
						if (cartaJugada.equals(valoresCartasOrdenados.get(j))) {
							valorJugada += j + 1;
							j = valoresCartasOrdenados.size();
						}
					}
				}

				for (String cartaJugada : jugadaAnterior.getValoresJugada()) {
					for (int j = 0; j < valoresCartasOrdenados.size(); j++) {
						if (cartaJugada.equals(valoresCartasOrdenados.get(j))) {
							valorJugadaAnterior += j + 1;
							j = valoresCartasOrdenados.size();
						}
					}
				}
				System.err.println("valorJugada = " + valorJugada); // PRUEBA-----------
				System.err.println("valorJugadaAnterior = " + valorJugadaAnterior); // PRUEBA-----------
				if (valorJugada < valorJugadaAnterior)
					respuesta = false;
			}
		}
		return respuesta;
	}

	private void cambioTurno(Partida partida) {
//		System.err.println("ID ACTUAL ANTES: " + partida.getIdActual()); // PRUEBA --------------------------
//		System.err.println("RONDA ACTUAL ANTES -> " + partida.getRondas()); // PRUEBA ----------------------
		boolean rondaTerminada = true;
		// Mientras la ronda siga activa, va a buscar jugadores que NO hayan jugado
		// todavía dicha ronda
		while (rondaTerminada) {
			boolean jugadorActualAsignado = false;
			for (Jugador jugador : partida.getJugadores()) {
				// Si hay algún jugador que todavía no ha jugado su turno en esta ronda y además
				// no se ha asignado jugador actual todavía, pasará a ser el jugador actual
				if (jugador.getTurnosJugados() < partida.getRondas() && !jugadorActualAsignado) {
					partida.setIdActual(jugador.getId());
					jugadorActualAsignado = true;
					rondaTerminada = false;
				}
			}
			// Si al buscar un jugador todos han jugado la ronda, se pasa a la siguiente
			// ronda
			if (rondaTerminada) {
				partida.setRondas(partida.getRondas() + 1);
			}
		}
//		System.err.println("ID ACTUAL DESPUÉS: " + partida.getIdActual()); // PRUEBA --------------------------
//		System.err.println("RONDA ACTUAL DESPUÉS -> " + partida.getRondas()); // PRUEBA ----------------------
	}

	// Método para saber si el jugador miente o no
	public boolean comprobarMentira(Jugada jugada) {
		// jugada.valoresJugada --> ArrayList de la jugada
		// jugada.jugador.mano --> ArrayList de la mano

		// Se crean nuevas instancias con los las listas de los valores de la jugada y
		// de la mano del jugador
		ArrayList<String> valoresJugada = new ArrayList<String>(jugada.getValoresJugada());
		ArrayList<String> manoJugador = new ArrayList<String>(jugada.getJugador().getMano());
		// Cogemos el size de los valores de la jugada
		int cartasJugada = valoresJugada.size();
		// Añadimos un contador para comparar después
		int contadorCoincidencia = 0;
		// Comprobamos, 1 a 1, si contiene los valores de la jugada en la mano del
		// jugador
		for (String cartaJugada : valoresJugada) {
			for (int i = 0; i < manoJugador.size(); i++) {
				if (cartaJugada.equals(manoJugador.get(i))) {
					manoJugador.set(i, "0");
					contadorCoincidencia++;
					break; // Se hace break para que no siga buscando
				}
			}
		}
		// Comparamos contador de coincidencias con el número de cartas que ha jugado
		// (valores jugada)
		return contadorCoincidencia == cartasJugada ? false : true;
	}

	public void repartirCartas(Partida partida, Jugador jugador) {
		Random random = new Random();
		int contador = 0;
		while (contador < 5) {
			String[] listaPalos = { "picas", "treboles", "corazones", "diamantes" };
			int paloAleatorio = random.nextInt(4);
			String paloEscogido = listaPalos[paloAleatorio];
			for (Entry<String, ArrayList<String>> entry : partida.getBaraja().entrySet()) {
				String clave = entry.getKey();
				if (clave.equals(paloEscogido)) {
					int numeroAleatorio = random.nextInt(entry.getValue().size());
					String valor = entry.getValue().get(numeroAleatorio);
					if (!valor.equals("0")) {
						jugador.getMano().add(valor);
						entry.getValue().set(numeroAleatorio, "0");
						contador++;
					}
				}
			}
		}
	}

}
