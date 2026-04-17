package controllerTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controller.AccessController;

/**
 * Classe di test per {@link AccessController}.
 * Contiene test unitari per i metodi di validazione dell'email e della password.
 *
 * <p>I test coprono i seguenti scenari:
 * <ul>
 *   <li>Validazione email con vari formati e condizioni</li>
 *   <li>Validazione password con criteri di sicurezza</li>
 *   <li>Verifica della coerenza tra password e conferma</li>
 * </ul>
 *
 * @author IngSW2425_055 Team
 * @see AccessController
 */
public class AccessControllerTest {

	private AccessController controller;

	/**
	 * Configura l'ambiente di test prima di ogni metodo di test.
	 * Inizializza una nuova istanza di {@link AccessController}.
	 */
	@BeforeEach
	void setUp() {
		// Arrange / Let
		controller = new AccessController();
	}

	/* =========================
       TEST isValidEmail
       ========================= */

	/**
	 * Test per verificare che una email nulla sia considerata non valida.
	 * Caso di test per input nullo.
	 */
	@Test
	void emailNulla_nonValida() {
		// Arrange / Let
		String email = null;

		// Act
		boolean result = controller.isValidEmail(email, false);

		// Assert
		assertFalse(result);
	}

	/**
	 * Test per verificare che una email vuota sia consentita quando il flag lo permette.
	 * Verifica che il parametro {@code allowEmpty} funzioni correttamente.
	 */
	@Test
	void emailVuotaConsentita() {
		// Arrange / Let
		String email = "";

		// Act
		boolean result = controller.isValidEmail(email, true);

		// Assert
		assertTrue(result);
	}

	/**
	 * Test per verificare che un email con formato corretto sia considerata valida.
	 * Caso di test positivo per formato standard.
	 */
	@Test
	void emailFormatoCorretto_valida() {
		// Arrange / Let
		String email = "utente@mail.com";

		// Act
		boolean result = controller.isValidEmail(email, false);

		// Assert
		assertTrue(result);
	}

	/**
	 * Test per verificare che un email con formato errato sia considerata non valida.
	 * Verifica la rilevazione di formati email non validi.
	 */
	@Test
	void emailFormatoErrato_nonValida() {
		// Arrange / Let
		String email = "user@ma il.com";

		// Act
		boolean result = controller.isValidEmail(email, false);

		// Assert
		assertFalse(result);
	}

	/* #########################
       TEST isValidPassword
       ######################### */

	/**
	 * Test per verificare che una password nulla sia considerata non valida.
	 * Caso di test per input nullo.
	 */
	@Test
	void passwordNulla_nonValida() {
		// Arrange / Let
		String password = null;
		String conferma = null;

		// Act
		boolean result = controller.isValidPassword(password, conferma);

		// Assert
		assertFalse(result);
	}

	/**
	 * Test per verificare che una password diversa dalla conferma sia considerata non valida.
	 * Verifica la coerenza tra password e conferma.
	 */
	@Test
	void passwordDiversaDaConferma_nonValida() {
		// Arrange / Let
		String password = "abc123";
		String conferma = "abc124";

		// Act
		boolean result = controller.isValidPassword(password, conferma);

		// Assert
		assertFalse(result);
	}

	/**
	 * Test per verificare che una password valida sia correttamente accettata.
	 * Caso di test positivo con password che soddisfa tutti i criteri.
	 */
	@Test
	void passwordValida() {
		// Arrange / Let
		String password = "abc123";
		String conferma = "abc123";

		// Act
		boolean result = controller.isValidPassword(password, conferma);

		// Assert
		assertTrue(result);
	}

	/**
	 * Test per verificare che una password senza numeri sia considerata non valida.
	 * Verifica il requisito di presenza di almeno un carattere numerico.
	 */
	@Test
	void passwordSenzaNumeri_nonValida() {
		// Arrange / Let
		String password = "abcdef";
		String conferma = "abcdef";

		// Act
		boolean result = controller.isValidPassword(password, conferma);

		// Assert
		assertFalse(result);
	}
}