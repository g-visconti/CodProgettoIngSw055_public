package auth;

/**
 * Interfaccia per il servizio di autenticazione tramite AWS Cognito in DietiEstates25.
 * Definisce le operazioni necessarie per gestire l'autenticazione degli utenti
 * attraverso il servizio di identità di Amazon Web Services.
 *
 * <p>Questa interfaccia astrae le operazioni di:
 * <ul>
 *   <li>Autenticazione tradizionale con username/password</li>
 *   <li>Autenticazione OAuth tramite provider esterni (Facebook, Google)</li>
 *   <li>Registrazione di nuovi utenti</li>
 *   <li>Gestione dei token di accesso</li>
 * </ul>
 *
 * <p>Implementazioni tipiche:
 * <ul>
 *   <li>{@link auth.CognitoAuthServiceImpl}: Implementazione concreta che interagisce
 *       direttamente con AWS Cognito</li>
 *   <li>Implementazioni mock per testing e sviluppo</li>
 * </ul>
 *
 * <p>Integrazione con il sistema:
 * <ul>
 *   <li>Utilizzata da {@link service.OAuthService} per l'autenticazione OAuth</li>
 *   <li>Integrata con i controller per la gestione degli accessi</li>
 *   <li>Fornisce un'astrazione per possibili cambiamenti nel provider di autenticazione</li>
 * </ul>
 *
 * @author IngSW2425_055 Team
 * @see auth.CognitoAuthServiceImpl
 * @see service.OAuthService
 */
public interface CognitoAuthService {

	/**
	 * Ottiene un token di accesso valido per l'API da AWS Cognito.
	 * Il token può essere utilizzato per autenticare richieste successive
	 * ai servizi protetti dell'applicazione.
	 *
	 * <p>Il token ottenuto:
	 * <ul>
	 *   <li>Ha una durata limitata (tipicamente 1 ora)</li>
	 *   <li>Deve essere rinnovato prima della scadenza</li>
	 *   <li>Contiene claim JWT con informazioni sull'utente</li>
	 *   <li>Può essere utilizzato per autorizzare richieste API</li>
	 * </ul>
	 *
	 * @return Token di accesso JWT valido per l'API, o {@code null} in caso di errore
	 *         durante il recupero, credenziali non valide o problemi di connessione
	 *         con AWS Cognito.
	 */
	String getAccessToken();

	/**
	 * Registra un nuovo utente nel sistema di identità AWS Cognito.
	 * Crea un profilo utente con le credenziali fornite e invia una richiesta
	 * di conferma all'indirizzo email specificato.
	 *
	 * <p>Il processo di registrazione:
	 * <ol>
	 *   <li>Valida le credenziali fornite</li>
	 *   <li>Invia la richiesta di registrazione a AWS Cognito</li>
	 *   <li>Invia un'email di conferma all'utente (se configurato)</li>
	 *   <li>Crea l'utente nello stato "UNCONFIRMED" fino a conferma</li>
	 * </ol>
	 *
	 * @param username Nome utente univoco per l'identificazione nel sistema.
	 *                 Deve rispettare le policy di Cognito per i nomi utente.
	 * @param password Password per l'account, deve rispettare le policy di complessità
	 *                 configurate in AWS Cognito (tipicamente: lunghezza minima,
	 *                 caratteri speciali, etc.)
	 * @param email Indirizzo email dell'utente, utilizzato per la comunicazione
	 *              e per il recupero password.
	 * @return {@code true} se la registrazione è stata avviata con successo,
	 *         {@code false} se si verifica un errore durante la registrazione
	 *         (es. utente già esistente, email duplicata, credenziali non valide).
	 * @throws IllegalArgumentException Se uno dei parametri è null o vuoto,
	 *                                  o non rispetta i formati richiesti.
	 */
	boolean registerUser(String username, String password, String email);

	/**
	 * Autentica un utente utilizzando un token di accesso OAuth di Facebook.
	 * Integra l'autenticazione Facebook con AWS Cognito per consentire il login
	 * sociale nell'applicazione.
	 *
	 * <p>Il processo di autenticazione:
	 * <ol>
	 *   <li>Valida il token Facebook con l'API di Facebook</li>
	 *   <li>Se valido, crea o recupera l'identità corrispondente in AWS Cognito</li>
	 *   <li>Ottiene un token Cognito per l'utente autenticato</li>
	 *   <li>Mantiene la sessione per le successive richieste</li>
	 * </ol>
	 *
	 * @param facebookAccessToken Token di accesso OAuth valido ottenuto da Facebook,
	 *                            tipicamente tramite il flusso di autorizzazione
	 *                            OAuth del Facebook SDK.
	 * @return {@code true} se l'autenticazione è riuscita e l'utente è stato
	 *         autenticato con successo in AWS Cognito, {@code false} se il token
	 *         è invalido, scaduto, o si verifica un errore durante l'autenticazione.
	 * @throws IllegalArgumentException Se il token è null o vuoto.
	 */
	boolean authenticateWithFacebook(String facebookAccessToken);

	/**
	 * Autentica un utente utilizzando un token di accesso OAuth di Google.
	 * Integra l'autenticazione Google con AWS Cognito per consentire il login
	 * sociale nell'applicazione.
	 *
	 * <p>Il processo di autenticazione:
	 * <ol>
	 *   <li>Valida il token Google con l'API di Google</li>
	 *   <li>Se valido, crea o recupera l'identità corrispondente in AWS Cognito</li>
	 *   <li>Ottiene un token Cognito per l'utente autenticato</li>
	 *   <li>Mantiene la sessione per le successive richieste</li>
	 * </ol>
	 *
	 * @param googleAccessToken Token di accesso OAuth valido ottenuto da Google,
	 *                          tipicamente tramite il flusso di autorizzazione
	 *                          OAuth di Google Sign-In.
	 * @return {@code true} se l'autenticazione è riuscita e l'utente è stato
	 *         autenticato con successo in AWS Cognito, {@code false} se il token
	 *         è invalido, scaduto, o si verifica un errore durante l'autenticazione.
	 * @throws IllegalArgumentException Se il token è null o vuoto.
	 */
	boolean authenticateWithGoogle(String googleAccessToken);

}