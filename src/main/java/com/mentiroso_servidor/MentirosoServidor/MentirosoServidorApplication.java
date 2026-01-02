package com.mentiroso_servidor.MentirosoServidor;

import java.util.ArrayList;
import java.util.Random;
import java.util.Map.Entry;

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
					for (Jugador jugador : partida.getJugadores()) {
						if (partida.getIdActual() == jugador.getId()) {
							mensaje = "-1:" + jugador.getNombre();

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

				/*
				 * Antes de meter el tipo y los valores tiene que comprobar una cosa, los
				 * valores que te pasa el usuario son diferentes ahora, ahora si el usuario dice
				 * pareja, solo te manda un numero del cual se supone que es la pareja. Por lo
				 * cual el servidor en la lista valores, en vez de tener dos ("6", "6") ahora
				 * tiene uno ("6"), entonce stiene que cambiar que en vez de verificar dos
				 * numeros en los valores, verifciar el mismo pero duplicado. mas de lo mismo
				 * con el trio, solo se manda un numero, en vez de mandarte "6", "6", "6", se te
				 * manda solo un numero ("6"), y ya con ese numero triplicado tendras que comprobar si
				 * el usuario lo tiene en sus "cartasMano" o no.
				 */

				// Creamos la jugada ejecutada por el jugador
				Jugada jugada = new Jugada(tipo, listaValores, jugadorActual);

				// Habría que validar

				// Seteamos la ultima jugada
				partida.setUltimaJugada(jugada);

				// Pasamos al siguiente turno y comprobamos si el jugador existe o no
				cambioTurno(idJugador, partida);
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
		// Si es verdad, eliminamos al jugador actual y devolvemos t (de true)
		// Si es mentira, eliminamos al jugador acusado y devolvemos f (de false)
		if (mentira) {
			for (Jugador jugador : partida.getJugadores()) {
				if (idJugador == jugador.getId()) {
					partida.getJugadores().remove(jugador);
					cambioTurno(idJugador, partida);
					return "t";
				}
			}
		}
		partida.getJugadores().remove(jugadorAcusado);
		cambioTurno(idJugador, partida);
		return "f";
	}

	// MÉTODOS NO MAPPEADOS

	private void cambioTurno(int idJugador, Partida partida) {
		int contadorTurno = 1;
		boolean jugadorEncontrado = false;
		while (!jugadorEncontrado) {
			int siguienteTurno = (idJugador % partida.getJugadores().size()) + contadorTurno;
			for (Jugador jugador : partida.getJugadores()) {
				if (jugador.getId() == siguienteTurno) {
					jugadorEncontrado = true;
					partida.setIdActual(siguienteTurno);
				}
			}
			if (!jugadorEncontrado)
				contadorTurno++;
		}
		System.err.println("ID ACTUAL: " + partida.getIdActual()); // PRUEBA --------------------------
		// Cambiamos la ronda en caso de ser el último jugador
		if (idJugador == partida.getJugadores().size())
			partida.setRondas(partida.getRondas() + 1);
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
			for (String cartaMano : manoJugador) {
				if (cartaJugada.equals(cartaMano)) {
					cartaMano = "0";
					contadorCoincidencia++;
					break; // Se hace break para que no siga buscando
				}
			}
		}
		// Comparamos contador de coincidencias con el número de cartas que ha jugado
		// (valores jugada)
		return contadorCoincidencia == cartasJugada ? true : false;
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
