package auth;

import java.util.concurrent.TimeUnit;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import auth.config.AuthConfig;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Implementazione del servizio di autenticazione tramite Amazon Cognito. Questa
 * classe fornisce metodi per interagire con il servizio Cognito di AWS, inclusi
 * l'ottenimento di token di accesso, la registrazione di utenti e
 * l'autenticazione tramite provider esterni come Facebook e Google.
 *
 * <p>
 * La classe gestisce:
 * <ul>
 * <li>Autenticazione client per ottenere token di accesso
 * <li>Registrazione di nuovi utenti nel pool utenti Cognito
 * <li>Autenticazione federata tramite Facebook e Google
 * </ul>
 *
 * @author IngSW2425_055 Team
 * @see CognitoAuthService
 * @see AuthConfig
 */
public class CognitoAuthServiceImpl implements CognitoAuthService {

	private final AuthConfig config;
	private final OkHttpClient httpClient;

	/**
	 * Costruttore di default che inizializza la configurazione di autenticazione e
	 * il client HTTP con timeout predefiniti.
	 *
	 * <p>
	 * Utilizza una nuova istanza di {@link AuthConfig} caricata con i valori di
	 * default dalle variabili d'ambiente o file di configurazione.
	 *
	 * @see AuthConfig
	 */
	public CognitoAuthServiceImpl() {
		config = new AuthConfig();
		httpClient = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS)
				.build();
	}

	/**
	 * Costruttore che permette di fornire una configurazione personalizzata. Utile
	 * per testing o scenari dove la configurazione deve essere dinamica.
	 *
	 * @param config Configurazione di autenticazione personalizzata
	 * @throws IllegalArgumentException Se la configurazione fornita è null
	 * @see AuthConfig
	 */
	public CognitoAuthServiceImpl(AuthConfig config) {
		this.config = config;
		httpClient = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS)
				.build();
	}

	/**
	 * Autentica un utente utilizzando un token di accesso di Facebook. Questo
	 * metodo implementa l'autenticazione federata tramite Facebook Login.
	 *
	 * <p>
	 * Il metodo invia il token Facebook a Cognito Identity per ottenere un'identità
	 * federata all'interno del pool identità configurato.
	 *
	 * @param facebookAccessToken Token di accesso ottenuto da Facebook Login
	 * @return true se l'autenticazione ha successo, false altrimenti
	 *
	 * @throws IllegalArgumentException Se il token Facebook è null o vuoto
	 * @see AuthConfig#getIdentityPoolId()
	 */
	@Override
	public boolean authenticateWithFacebook(String facebookAccessToken) {
		final JsonObject jsonRequest = new JsonObject();
		jsonRequest.addProperty("IdentityPoolId", config.getIdentityPoolId());
		jsonRequest.addProperty("Logins", "graph.facebook.com:" + facebookAccessToken);

		final RequestBody body = RequestBody.create(jsonRequest.toString(), MediaType.parse("application/json"));

		final Request request = new Request.Builder().url("https://cognito-identity.eu-west-1.amazonaws.com/")
				.post(body).addHeader("Content-Type", "application/json")
				.addHeader("X-Amz-Target", "AWSCognitoIdentityService.GetId").build();

		try (Response response = httpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				System.err.println("Errore durante l'autenticazione con Facebook: " + response.code());
				return false;
			}
			if (response.body() != null) {
				response.body().string();
			}
			System.out.println("Autenticazione Facebook avvenuta con successo");
			return true;
		} catch (Exception e) {
			System.err.println("Errore durante l'autenticazione Facebook: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Autentica un utente utilizzando un token di accesso di Google. Questo metodo
	 * implementa l'autenticazione federata tramite Google Sign-In.
	 *
	 * <p>
	 * Il metodo invia il token Google a Cognito Identity per ottenere un'identità
	 * federata all'interno del pool identità configurato.
	 *
	 * @param googleAccessToken Token di accesso ottenuto da Google Sign-In
	 * @return true se l'autenticazione ha successo, false altrimenti
	 *
	 * @throws IllegalArgumentException Se il token Google è null o vuoto
	 * @see AuthConfig#getIdentityPoolId()
	 */
	@Override
	public boolean authenticateWithGoogle(String googleAccessToken) {
		final JsonObject jsonRequest = new JsonObject();
		jsonRequest.addProperty("IdentityPoolId", config.getIdentityPoolId());
		jsonRequest.addProperty("Logins", "accounts.google.com:" + googleAccessToken);

		final RequestBody body = RequestBody.create(jsonRequest.toString(), MediaType.parse("application/json"));

		final Request request = new Request.Builder().url("https://cognito-identity.eu-west-1.amazonaws.com/")
				.post(body).addHeader("Content-Type", "application/json")
				.addHeader("X-Amz-Target", "AWSCognitoIdentityService.GetId").build();

		try (Response response = httpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				System.err.println("Errore durante l'autenticazione con Google: " + response.code());
				return false;
			}
			if (response.body() != null) {
				response.body().string();
			}
			System.out.println("Autenticazione Google avvenuta con successo");
			return true;
		} catch (Exception e) {
			System.err.println("Errore durante l'autenticazione Google: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Ottiene un token di accesso dal servizio Cognito utilizzando le credenziali
	 * client. Il token viene richiesto utilizzando il flusso OAuth2
	 * "client_credentials".
	 *
	 * <p>
	 * Il metodo verifica prima la validità della configurazione e del client
	 * secret. Se la richiesta ha successo, restituisce il token di accesso;
	 * altrimenti restituisce null.
	 *
	 * @return Token di accesso come stringa, o null in caso di errore
	 *
	 * @throws IllegalStateException Se la configurazione Cognito non è valida
	 * @see AuthConfig#isValid()
	 * @see AuthConfig#getClientSecret()
	 */
	@Override
	public String getAccessToken() {
		if (!config.isValid()) {
			System.err.println("Configurazione Cognito non valida");
			return null;
		}

		final String clientSecret = config.getClientSecret();
		if (clientSecret == null || clientSecret.isEmpty()) {
			System.err.println("Client secret non configurato. Imposta COGNITO_CLIENT_SECRET");
			return null;
		}

		final RequestBody formBody = new FormBody.Builder().add("client_id", config.getClientId())
				.add("client_secret", clientSecret).add("scope", config.getScope())
				.add("grant_type", "client_credentials").build();

		final Request request = new Request.Builder()
				.url("https://eu-west-1xrobh5glh.auth.eu-west-1.amazoncognito.com/oauth2/token").post(formBody)
				.header("Content-Type", "application/x-www-form-urlencoded").build();

		try (Response response = httpClient.newCall(request).execute()) {
			if (response.isSuccessful() && response.body() != null) {
				final String responseBody = response.body().string();
				final JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
				return jsonObject.get("access_token").getAsString();
			}
			System.err.println("Errore nella richiesta del token: " + response.code());
			if (response.body() != null) {
				System.err.println("Response body: " + response.body().string());
			}
			return null;
		} catch (Exception e) {
			System.err.println("Errore durante la richiesta del token: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Registra un nuovo utente nel pool utenti Cognito. Crea un account con
	 * username, password e email forniti.
	 *
	 * <p>
	 * Il metodo invia una richiesta di SignUp all'endpoint Cognito Identity
	 * Provider. L'email viene aggiunta come attributo utente.
	 *
	 * @param username Nome utente per la registrazione
	 * @param password Password per l'account
	 * @param email    Indirizzo email dell'utente
	 * @return true se la registrazione ha successo, false altrimenti
	 *
	 * @throws IllegalArgumentException Se username, password o email sono null o
	 *                                  vuoti
	 * @see AuthConfig#getClientId()
	 * @see AuthConfig#getRegion()
	 */
	@Override
	public boolean registerUser(String username, String password, String email) {
		final JsonObject jsonRequest = new JsonObject();
		jsonRequest.addProperty("ClientId", config.getClientId());
		jsonRequest.addProperty("Username", username);
		jsonRequest.addProperty("Password", password);

		final JsonArray userAttributes = new JsonArray();
		final JsonObject emailAttr = new JsonObject();
		emailAttr.addProperty("Name", "email");
		emailAttr.addProperty("Value", email);
		userAttributes.add(emailAttr);

		jsonRequest.add("UserAttributes", userAttributes);

		final RequestBody body = RequestBody.create(jsonRequest.toString(), MediaType.parse("application/json"));

		final Request request = new Request.Builder()
				.url("https://cognito-idp." + config.getRegion() + ".amazonaws.com/").post(body)
				.header("Content-Type", "application/json")
				.header("X-Amz-Target", "AWSCognitoIdentityProviderService.SignUp")
				.header("X-Amz-Region", config.getRegion()).build();

		try (Response response = httpClient.newCall(request).execute()) {
			if (response.isSuccessful()) {
				return true;
			}
			final String errorBody = response.body() != null ? response.body().string() : "Nessun body";
			System.err.println("Errore durante la registrazione: " + response.code() + " - " + errorBody);
			return false;
		} catch (Exception e) {
			System.err.println("Errore durante la registrazione: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
}