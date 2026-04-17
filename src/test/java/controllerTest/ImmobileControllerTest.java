package controllerTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controller.ImmobileController;

/**
 * Classe di test per {@link ImmobileController}.
 * Contiene test unitari per il metodo di validazione degli immobili.
 *
 * <p>I test sono organizzati in classi di equivalenza che coprono:
 * <ul>
 *   <li>Input validi completi</li>
 *   <li>Campi testuali vuoti o non validi</li>
 *   <li>Tipologie non valide</li>
 *   <li>Valori numerici fuori range</li>
 *   <li>Piani non validi</li>
 * </ul>
 *
 * @author IngSW2425_055 Team
 * @see ImmobileController
 */
public class ImmobileControllerTest {

	private ImmobileController controller;

	/**
	 * Configura l'ambiente di test prima di ogni metodo di test.
	 * Inizializza una nuova istanza di {@link ImmobileController}.
	 */
	@BeforeEach
	void setUp() {
		controller = new ImmobileController();
	}

	/**
	 * Classe di equivalenza: INPUT VALIDO
	 * Test per verificare che un input completo e corretto non generi eccezioni.
	 */
	@Test
	void testInputValido() {
		assertDoesNotThrow(() ->
		controller.validaImmobile(
				"Casa",
				"Via Roma 10",
				"Milano",
				"Appartamento luminoso",
				"Vendita",
				80,
				200000,
				2
				)
				);
	}

	/**
	 * Classe di equivalenza: CAMPI TESTUALI NON VALIDI
	 * Test per verificare che un campo testuale vuoto generi un'eccezione.
	 * Caso rappresentativo: titolo vuoto.
	 */
	@Test
	void testCampoTestualeVuoto() {
		assertThrows(IllegalArgumentException.class, () ->
		controller.validaImmobile(
				"",                // titolo non valido
				"Via Roma 10",
				"Milano",
				"Descrizione",
				"Affitto",
				70,
				1000,
				1
				)
				);
	}

	/**
	 * Classe di equivalenza: TIPOLOGIA NON VALIDA
	 * Test per verificare che una tipologia non valida generi un'eccezione.
	 */
	@Test
	void testTipologiaNonValida() {
		assertThrows(IllegalArgumentException.class, () ->
		controller.validaImmobile(
				"Casa",
				"Via Roma",
				"Milano",
				"Descrizione",
				"-",               // tipologia non valida
				70,
				1000,
				1
				)
				);
	}

	/**
	 * Classe di equivalenza: VALORI NUMERICI NON VALIDI
	 * Test per verificare che valori numerici fuori range generino un'eccezione.
	 * Caso rappresentativo: dimensione <= 0.
	 */
	@Test
	void testValoriNumericiNonValidi() {
		assertThrows(IllegalArgumentException.class, () ->
		controller.validaImmobile(
				"Casa",
				"Via Roma",
				"Milano",
				"Descrizione",
				"Vendita",
				0,                // dimensione non valida
				1000,
				1
				)
				);
	}

	/**
	 * Classe di equivalenza: PIANO NON VALIDO
	 * Test per verificare che un piano fuori range generi un'eccezione.
	 * Caso rappresentativo: piano < -1.
	 */
	@Test
	void testPianoNonValido() {
		assertThrows(IllegalArgumentException.class, () ->
		controller.validaImmobile(
				"Casa",
				"Via Roma",
				"Milano",
				"Descrizione",
				"Vendita",
				80,
				1000,
				-5               // piano non valido
				)
				);
	}
}