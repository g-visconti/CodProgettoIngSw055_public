package controllerTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controller.OfferteController;

/**
 * Classe di test per {@link OfferteController}.
 * Contiene test unitari per la validazione delle controproposte di offerta.
 *
 * <p>I test coprono i seguenti scenari di validazione:
 * <ul>
 *   <li>Controproposte superiori all'offerta iniziale</li>
 *   <li>Controproposte inferiori o uguali all'offerta iniziale</li>
 *   <li>Valori negativi o zero</li>
 * </ul>
 *
 * @author IngSW2425_055 Team
 * @see OfferteController
 */
public class OfferteControllerTest {

	private OfferteController controller;

	/**
	 * Configura l'ambiente di test prima di ogni metodo di test.
	 * Inizializza una nuova istanza di {@link OfferteController}.
	 */
	@BeforeEach
	void setUp() {
		controller = new OfferteController();
	}

	/**
	 * Test per verificare che una controproposta valida sia accettata.
	 * Una controproposta è valida se è maggiore dell'offerta iniziale.
	 */
	@Test
	void contropropostaValida() {
		// Arrange / Let
		double controproposta = 1200.0;
		double offertaIniziale = 1000.0;

		// Act
		boolean result = controller.isValidControproposta(controproposta, offertaIniziale);

		// Assert
		assertTrue(result);
	}

	/**
	 * Test per verificare che una controproposta minore dell'offerta iniziale
	 * sia considerata non valida.
	 */
	@Test
	void contropropostaMinoreOffertaIniziale_nonValida() {
		// Arrange / Let
		double controproposta = 800.0;
		double offertaIniziale = 1000.0;

		// Act
		boolean result = controller.isValidControproposta(controproposta, offertaIniziale);

		// Assert
		assertFalse(result);
	}

	/**
	 * Test per verificare che una controproposta uguale all'offerta iniziale
	 * sia considerata non valida.
	 */
	@Test
	void contropropostaUgualeOffertaIniziale_nonValida() {
		// Arrange / Let
		double controproposta = 1000.0;
		double offertaIniziale = 1000.0;

		// Act
		boolean result = controller.isValidControproposta(controproposta, offertaIniziale);

		// Assert
		assertFalse(result);
	}

	/**
	 * Test per verificare che una controproposta negativa sia considerata non valida.
	 */
	@Test
	void contropropostaNegativa_nonValida() {
		// Arrange / Let
		double controproposta = -500.0;
		double offertaIniziale = 1000.0;

		// Act
		boolean result = controller.isValidControproposta(controproposta, offertaIniziale);

		// Assert
		assertFalse(result);
	}

	/**
	 * Test per verificare che una controproposta uguale a zero sia considerata non valida.
	 */
	@Test
	void contropropostaZero_nonValida() {
		// Arrange / Let
		double controproposta = 0;
		double offertaIniziale = 1000.0;

		// Act
		boolean result = controller.isValidControproposta(controproposta, offertaIniziale);

		// Assert
		assertFalse(result);
	}
}