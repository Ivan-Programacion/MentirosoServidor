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
					return jugadores + cartasJugador + ":" + idJugador; // Devolvemos el valor a la función para que no siga buscando más
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
					if(partida.getUltimoJugador() != null) {
						jugadorAnterior = partida.getUltimoJugador().getNombre();
						tipoJugada = partida.getUltimaJugada().getTipoJugada();
						valoresJugada = "";
						for (String jugada : partida.getUltimaJugada().getValoresJugada()) {
							valoresJugada += jugada + " ";
						}
					}
					mensaje = "0:" + numJugadores + "," + partida.getRondas() + ":" + jugadorAnterior + "," + tipoJugada + "," + valoresJugada;
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

				// Creamos la jugada ejecutada por el jugador
				Jugada jugada = new Jugada(tipo, listaValores, jugadorActual);

				// Habría que validar

				// Seteamos la ultima jugada y el ultimo jugador
				partida.setUltimaJugada(jugada);
				partida.setUltimoJugador(jugadorActual);

				// Pasamos al siguiente turno
				int siguienteTurno = (idJugador % partida.getJugadores().size()) + 1;
				partida.setIdActual(siguienteTurno);
				// Cambiamos la ronda en caso de ser el último jugador
				if (idJugador == partida.getJugadores().size())
					partida.setRondas(partida.getRondas() + 1);
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
			return "-3";

		// Comprobamos el turno
		if (partida.getIdActual() != idJugador)
			return "-1";

		// Verificamos si ha habido última jugada
		Jugada ultimaJugada = partida.getUltimaJugada();
		if (ultimaJugada == null)
			return "-2";

		// Comprobamos si mentía
		Jugador jugadorAcusado = ultimaJugada.getJugador();
		boolean mentira = comprobarMentira(ultimaJugada, jugadorAcusado);

		if (mentira) {
			partida.getJugadores().remove(jugadorAcusado);
		} else {
			// Crear metodo para eliminar al jugador que ha acusado falsamente (eliminar por
			// ID)
		}

		// Comprobamos ganador
		if (partida.getJugadores().size() == 1)
			return "El jugador:" + partida.getJugadores().get(0).getNombre() + " ha GANADO";

		// pasamos a una nueva ronda
		partida.setRondas(partida.getRondas() + 1);
		// Reiniciamos la ultima jugada
		partida.setUltimaJugada(null);

		if (mentira) {
			return "El jugador anterior MENTÍA";
		} else {
			return "El jugador anterior decía la VERDAD";
		}
	}

	// Método para saber si el jugador miente o no
	public boolean comprobarMentira(Jugada jugada, Jugador jugador) {
		// Meter un if que verifique si el jugador miente
		return false; // Esto en caso de que el jugador decía la verdad
	}

	// MÉTODOS NO MAPPEADOS

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
