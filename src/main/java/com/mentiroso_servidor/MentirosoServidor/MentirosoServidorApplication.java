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
				else if (partida.getJugadores().size() == 5)
					return "-3";
				else {
					Jugador jugador = new Jugador(nombre, partida.numJugadores() + 1);
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
					System.out.println(juego.toString()); // PRUEBA
															// --------------------------------------------------------------
					return jugadores + cartasJugador; // Devolvemos el valor a la función para que no siga buscando más
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
					mensaje = "0:" + numJugadores + "," + partida.getRondas();
				} else {
					for (Jugador jugador : partida.getJugadores()) {
						if (partida.getIdActual() == jugador.getId()) {
							mensaje ="-1:" + jugador.getNombre();

						}
					}
				}
			}
		}
		return mensaje;

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
