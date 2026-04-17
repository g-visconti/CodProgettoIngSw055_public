package service;

import java.io.IOException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import auth.CognitoAuthService;
import auth.CognitoAuthServiceImpl;
import model.entity.Account;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Servizio per la gestione dell'autenticazione OAuth tramite provider esterni
 * (Facebook e Google) in DietiEstates25.
 *
 * <p>Questa classe coordina il processo completo di autenticazione OAuth:
 * <ol>
 *   <li>Autenticazione con AWS Cognito per la gestione delle identità</li>
 *   <li>Recupero delle informazioni utente dai provider OAuth</li>
 *   <li>Creazione di oggetti {@link Account} con i dati ottenuti</li>
 *   <li>Estrazione dell'email dai token OAuth</li>
 * </ol>
 *
 * <p>Provider supportati:
 * <ul>
 *   <li>Facebook: utilizza Graph API per recuperare email, nome e cognome</li>
 *   <li>Google: utilizza Google OAuth2 UserInfo endpoint</li>
 * </ul>
 *
 * <p>Architettura:
 * <ul>
 *   <li>Delega l'autenticazione a {@link CognitoAuthService}</li>
 *   <li>Utilizza {@link OkHttpClient} per chiamate HTTP ai provider</li>
 *   <li>Parsing JSON con {@link com.google.gson.Gson}</li>
 * </ul>
 *
 * @author IngSW2425_055 Team
 * @see CognitoAuthService
 * @see Account
 * @see OkHttpClient
 */
public class OAuthService {

	private final CognitoAuthService cognitoAuthService;
	private final OkHttpClient httpClient;

	/**
	 * Costruttore predefinito che inizializza il servizio OAuth
	 * con l'implementazione standard di {@link CognitoAuthService}.
	 * Crea una nuova istanza di {@link OkHttpClient} per le chiamate HTTP.
	 */
	public OAuthService() {
		cognitoAuthService = new CognitoAuthServiceImpl();
		httpClient = new OkHttpClient();
	}

	/**
	 * Costruttore che permette l'iniezione di un'istanza personalizzata
	 * di {@link CognitoAuthService} per facilitare il testing e l'estensibilità.
	 *
	 * @param cognitoAuthService Implementazione di {@link CognitoAuthService}
	 *                           da utilizzare per l'autenticazione Cognito.
	 * @throws IllegalArgumentException Se cognitoAuthService è null
	 */
	public OAuthService(CognitoAuthService cognitoAuthService) {
		this.cognitoAuthService = cognitoAuthService;
		httpClient = new OkHttpClient();
	}

	/**
	 * Processa il login OAuth completo per un utente.
	 * Combina autenticazione Cognito e recupero informazioni utente dal provider.
	 *
	 * <p>Il metodo esegue le seguenti operazioni:
	 * <ol>
	 *   <li>Valida il token OAuth ricevuto</li>
	 *   <li>Autentica l'utente con AWS Cognito tramite il provider specificato</li>
	 *   <li>Se l'autenticazione Cognito ha successo, recupera i dati utente dal provider</li>
	 *   <li>Crea e restituisce un oggetto {@link Account} con i dati ottenuti</li>
	 * </ol>
	 *
	 * @param token Token OAuth ricevuto dal provider (Facebook o Google)
	 * @param provider Nome del provider OAuth ("facebook" o "google", case-insensitive)
	 * @return Oggetto {@link Account} contenente i dati dell'utente autenticato,
	 *         o {@code null} se il token è invalido, l'autenticazione fallisce
	 *         o si verifica un errore durante il recupero dei dati.
	 * @throws IllegalArgumentException Se il provider non è supportato
	 */
	public Account processOAuthLogin(String token, String provider) {
		if (token == null || token.isEmpty()) {
			return null;
		}

		// 1. Autentica con Cognito
		final boolean cognitoSuccess = authenticateWithCognito(token, provider);
		if (!cognitoSuccess) {
			return null;
		}

		// 2. Recupera i dati dell'utente dal provider
		return getUserInfoFromProvider(token, provider);
	}

	/**
	 * Estrae l'email dell'utente dal token OAuth senza eseguire l'autenticazione completa.
	 * Utile per operazioni che richiedono solo l'identificazione dell'utente.
	 *
	 * <p>Il metodo:
	 * <ul>
	 *   <li>Contatta l'API del provider per recuperare le informazioni dell'utente</li>
	 *   <li>Estrae l'email dalla risposta JSON</li>
	 *   <li>Restituisce l'email o {@code null} in caso di errore</li>
	 * </ul>
	 *
	 * @param token Token OAuth valido del provider
	 * @param provider Nome del provider OAuth ("facebook" o "google", case-insensitive)
	 * @return Email dell'utente associata al token OAuth, o {@code null} se
	 *         non è possibile recuperarla o il token è invalido.
	 * @throws IllegalArgumentException Se il provider non è supportato
	 */
	public String extractEmailFromToken(String token, String provider) {
		final Account account = getUserInfoFromProvider(token, provider);
		return account != null ? account.getEmail() : null;
	}

	/**
	 * Delega l'autenticazione del token OAuth a AWS Cognito.
	 * Utilizza l'implementazione specifica del provider per validare il token.
	 *
	 * @param token Token OAuth da validare
	 * @param provider Nome del provider OAuth ("facebook" o "google", case-insensitive)
	 * @return {@code true} se l'autenticazione Cognito ha successo,
	 *         {@code false} altrimenti.
	 * @throws IllegalArgumentException Se il provider non è supportato
	 * @see CognitoAuthService#authenticateWithFacebook(String)
	 * @see CognitoAuthService#authenticateWithGoogle(String)
	 */
	private boolean authenticateWithCognito(String token, String provider) {
		return switch (provider.toLowerCase()) {
		case "facebook" -> cognitoAuthService.authenticateWithFacebook(token);
		case "google" -> cognitoAuthService.authenticateWithGoogle(token);
		default -> throw new IllegalArgumentException("Provider non supportato: " + provider);
		};
	}

	/**
	 * Recupera le informazioni dell'utente dal provider OAuth specificato.
	 * Dispatcher che chiama il metodo appropriato in base al provider.
	 *
	 * @param token Token OAuth valido del provider
	 * @param provider Nome del provider OAuth ("facebook" o "google", case-insensitive)
	 * @return Oggetto {@link Account} con i dati dell'utente, o {@code null}
	 *         se il recupero fallisce.
	 * @throws IllegalArgumentException Se il provider non è supportato
	 */
	private Account getUserInfoFromProvider(String token, String provider) {
		return switch (provider.toLowerCase()) {
		case "facebook" -> getFacebookAccount(token);
		case "google" -> getGoogleAccount(token);
		default -> throw new IllegalArgumentException("Provider non supportato: " + provider);
		};
	}

	/**
	 * Recupera le informazioni dell'utente dall'API Graph di Facebook.
	 * Contatta l'endpoint {@code /me} di Facebook Graph API per ottenere
	 * email, nome e cognome dell'utente.
	 *
	 * <p>Campi richiesti a Facebook:
	 * <ul>
	 *   <li>email: indirizzo email associato all'account Facebook</li>
	 *   <li>first_name: nome dell'utente</li>
	 *   <li>last_name: cognome dell'utente</li>
	 * </ul>
	 *
	 * <p>L'account creato avrà sempre ruolo "Cliente" e utilizzerà il token
	 * Facebook come password temporanea.
	 *
	 * @param facebookAccessToken Token di accesso OAuth di Facebook valido
	 * @return Oggetto {@link Account} con i dati dell'utente Facebook,
	 *         o {@code null} se la richiesta fallisce o la risposta è malformata.
	 * @throws IOException Se si verifica un errore di rete durante la chiamata HTTP
	 */
	private Account getFacebookAccount(String facebookAccessToken) {
		final String url = "https://graph.facebook.com/me?fields=email,first_name,last_name&access_token="
				+ facebookAccessToken;
		final Request request = new Request.Builder().url(url).get().build();

		try (Response response = httpClient.newCall(request).execute()) {
			if (response.isSuccessful() && response.body() != null) {
				final String responseBody = response.body().string();
				final JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();

				final String email = jsonObject.has("email") ? jsonObject.get("email").getAsString() : null;
				final String ruolo = "Cliente";
				final Account account = new Account(email, facebookAccessToken, ruolo);

				if (jsonObject.has("first_name")) {
					account.setNome(jsonObject.get("first_name").getAsString());
				}
				if (jsonObject.has("last_name")) {
					account.setCognome(jsonObject.get("last_name").getAsString());
				}

				return account;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Recupera le informazioni dell'utente dall'API UserInfo di Google.
	 * Contatta l'endpoint {@code /oauth2/v3/userinfo} di Google OAuth2.
	 *
	 * <p>Campi restituiti da Google:
	 * <ul>
	 *   <li>email: indirizzo email associato all'account Google</li>
	 *   <li>given_name: nome dell'utente</li>
	 *   <li>family_name: cognome dell'utente</li>
	 * </ul>
	 *
	 * <p>L'account creato avrà sempre ruolo "Cliente" e utilizzerà il token
	 * Google come password temporanea.
	 *
	 * @param googleAccessToken Token di accesso OAuth di Google valido
	 * @return Oggetto {@link Account} con i dati dell'utente Google,
	 *         o {@code null} se la richiesta fallisce o la risposta è malformata.
	 * @throws IOException Se si verifica un errore di rete durante la chiamata HTTP
	 */
	private Account getGoogleAccount(String googleAccessToken) {
		final Request request = new Request.Builder().url("https://www.googleapis.com/oauth2/v3/userinfo")
				.addHeader("Authorization", "Bearer " + googleAccessToken).get().build();

		try (Response response = httpClient.newCall(request).execute()) {
			if (response.isSuccessful() && response.body() != null) {
				final String responseBody = response.body().string();
				final JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();

				final String email = jsonObject.has("email") ? jsonObject.get("email").getAsString() : null;
				final String ruolo = "Cliente";
				final Account account = new Account(email, googleAccessToken, ruolo);

				if (jsonObject.has("given_name")) {
					account.setNome(jsonObject.get("given_name").getAsString());
				}
				if (jsonObject.has("family_name")) {
					account.setCognome(jsonObject.get("family_name").getAsString());
				}

				return account;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}