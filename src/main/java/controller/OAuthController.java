package controller;

import javax.swing.JOptionPane;

import model.entity.Account;
import service.OAuthService;

/**
 * Controller per la gestione dell'autenticazione OAuth tramite provider esterni.
 * Questa classe gestisce il flusso di login OAuth, integrando l'autenticazione
 * con provider come Facebook e Google con il sistema di account interno.
 *
 * <p>La classe gestisce:
 * <ul>
 *   <li>Autenticazione utenti tramite token OAuth
 *   <li>Registrazione automatica di nuovi utenti OAuth
 *   <li>Validazione dei token OAuth
 *   <li>Estrazione informazioni account dai token
 *   <li>Integrazione con il sistema di account esistente
 * </ul>
 *
 * @author IngSW2425_055 Team
 * @see OAuthService
 * @see AccessController
 * @see Account
 */
public class OAuthController {

	private final OAuthService oauthService;
	private final AccessController accessController;

	/**
	 * Costruttore di default che inizializza i servizi OAuth e Access.
	 *
	 * <p>Il costruttore crea nuove istanze di:
	 * <ul>
	 *   <li>{@link OAuthService} - per gestire l'autenticazione OAuth
	 *   <li>{@link AccessController} - per integrare con il sistema di account
	 * </ul>
	 */
	public OAuthController() {
		oauthService = new OAuthService();
		accessController = new AccessController();
	}

	/**
	 * Gestisce il login OAuth completo tramite un token di provider esterno.
	 *
	 * <p>Il metodo esegue il seguente flusso:
	 * <ul>
	 *   <li>Valida il token OAuth
	 *   <li>Processa l'autenticazione con il provider specificato
	 *   <li>Registra o aggiorna l'utente nel database (ruolo sempre "Cliente")
	 *   <li>Logga l'accesso
	 *   <li>Restituisce l'email dell'utente per la sessione
	 * </ul>
	 *
	 * @param token Token OAuth ottenuto dal provider (Facebook, Google, etc.)
	 * @param provider Nome del provider ("facebook", "google", etc.)
	 * @return Email dell'utente autenticato, o null in caso di errore
	 *
	 * @throws IllegalArgumentException Se il token è null o vuoto
	 * @throws RuntimeException Se si verificano errori durante l'autenticazione
	 *
	 * @see OAuthService#processOAuthLogin(String, String)
	 * @see AccessController#registraNuovoUtente(String, String, String)
	 */
	public String handleOAuthLogin(String token, String provider) {
		if (token == null || token.isEmpty()) {
			showMessage("Token non valido", "Errore", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		try {
			// 1. Processa l'autenticazione OAuth
			final Account account = oauthService.processOAuthLogin(token, provider);

			if (account == null) {
				showMessage("Autenticazione con " + provider + " fallita", "Errore", JOptionPane.ERROR_MESSAGE);
				return null;
			}

			final String email = account.getEmail();
			if (email == null || email.isEmpty()) {
				showMessage("Impossibile ottenere l'email da " + provider, "Errore", JOptionPane.ERROR_MESSAGE);
				return null;
			}

			// 2. Registra o aggiorna l'utente nel database
			// Il ruolo è sempre "Cliente" per OAuth
			final String ruolo = "Cliente";

			try {
				accessController.registraNuovoUtente(email, token, ruolo);
			} catch (Exception e) {
				// Se l'utente esiste già, potrebbe essere un login normale
				System.out.println("Utente già registrato o errore di registrazione: " + e.getMessage());
				// Proseguiamo comunque con il login
			}

			// 3. Log dell'accesso
			logAccesso(email, provider);

			return email;

		} catch (Exception e) {
			e.printStackTrace();
			showMessage("Errore durante l'autenticazione: " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}

	/**
	 * Valida un token OAuth verificando la sua integrità e estraendo l'email.
	 *
	 * <p>Il metodo utilizza il servizio OAuth per estrarre l'email dal token
	 * e verificare che sia un token valido emesso dal provider specificato.
	 *
	 * @param token Token OAuth da validare
	 * @param provider Nome del provider che ha emesso il token
	 * @return true se il token è valido e contiene un'email, false altrimenti
	 *
	 * @see OAuthService#extractEmailFromToken(String, String)
	 */
	public boolean validateToken(String token, String provider) {
		try {
			final String email = oauthService.extractEmailFromToken(token, provider);
			return email != null && !email.isEmpty();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Estrae e restituisce un oggetto Account da un token OAuth.
	 *
	 * <p>Il metodo utilizza il servizio OAuth per processare il token
	 * e creare un oggetto Account con le informazioni estratte
	 * (email, nome, cognome, etc.).
	 *
	 * @param token Token OAuth da processare
	 * @param provider Nome del provider che ha emesso il token
	 * @return Oggetto Account con le informazioni estratte, o null in caso di errore
	 *
	 * @see OAuthService#processOAuthLogin(String, String)
	 */
	public Account getAccountFromToken(String token, String provider) {
		return oauthService.processOAuthLogin(token, provider);
	}

	/**
	 * Mostra un messaggio all'utente tramite dialog.
	 *
	 * <p>Metodo privato di utilità per standardizzare la visualizzazione
	 * dei messaggi di errore/informazione.
	 *
	 * @param message Testo del messaggio
	 * @param title Titolo della finestra
	 * @param messageType Tipo di messaggio (JOptionPane.ERROR_MESSAGE, etc.)
	 */
	private void showMessage(String message, String title, int messageType) {
		JOptionPane.showMessageDialog(null, message, title, messageType);
	}

	/**
	 * Logga un accesso OAuth sul console.
	 *
	 * <p>Metodo privato di utilità per tracciare gli accessi OAuth
	 * a fini di debugging e monitoraggio.
	 *
	 * @param email Email dell'utente che ha effettuato l'accesso
	 * @param provider Provider OAuth utilizzato
	 */
	private void logAccesso(String email, String provider) {
		System.out.println("[LOG] Accesso OAuth: " + email + " via " + provider);
	}
}