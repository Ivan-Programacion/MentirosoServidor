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
		juego.addPartida(partida);
		partida.addJugador(jugador);
		String cartasJugador = String.join(",", jugador.getMano());
		String respuestaServidor = numeroJugador + "," + cartasJugador;
		return respuestaServidor;

	}

	public void repartirCartas(Partida p, Jugador j) {
		Random random = new Random();

		int contador = 0;
		while (contador < 5) {

			String[] listaPalos = { "picas", "treboles", "corazones", "diamantes" };
			int paloAleatorio = random.nextInt(4);
			String paloEscogido = listaPalos[paloAleatorio];

			for (Entry<String, ArrayList<String>> entry : p.getBaraja().entrySet()) {
				String clave = entry.getKey();

				if (clave.equals(paloEscogido)) {
					int numeroAleatorio = random.nextInt(entry.getValue().size());

					String valor = entry.getValue().get(numeroAleatorio);
					if (!valor.equals("0")) {
						j.getMano().add(valor);
						entry.getValue().set(numeroAleatorio, "0");
						contador++;
					}
				}
			}
		}
	}

}
