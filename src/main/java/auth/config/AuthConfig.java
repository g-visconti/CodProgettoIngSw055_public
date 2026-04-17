package auth.config;

/**
 * Configurazione per l'autenticazione AWS Cognito.
 * Questa classe gestisce il caricamento delle credenziali e dei parametri di configurazione
 * necessari per l'integrazione con il servizio Amazon Cognito.
 *
 * <p>Le credenziali vengono caricate in ordine di priorità:
 * <ul>
 *   <li>Variabili d'ambiente (preferite)
 *   <li>Valori di default (fallback)
 * </ul>
 *
 * <p>La classe fornisce anche un metodo di validazione per verificare
 * che la configurazione sia completa e utilizzabile.
 *
 * @author IngSW2425_055 Team
 */
public class AuthConfig {

	private String region;
	private String clientId;
	private final String clientSecret;
	private final String tokenUrl;
	private final String signupUrl;
	private final String scope;
	private String identityPoolId;

	/**
	 * Costruttore di default che inizializza la configurazione di autenticazione.
	 *
	 * <p>Il costruttore tenta prima di caricare le configurazioni dalle variabili d'ambiente:
	 * <ul>
	 *   <li>{@code COGNITO_REGION} - Regione AWS per Cognito
	 *   <li>{@code COGNITO_CLIENT_ID} - ID client dell'applicazione Cognito
	 *   <li>{@code COGNITO_CLIENT_SECRET} - Secret client per l'autenticazione
	 *   <li>{@code COGNITO_IDENTITY_POOL_ID} - ID del pool identità Cognito
	 * </ul>
	 *
	 * <p>Se le variabili d'ambiente non sono impostate, vengono utilizzati valori di default.
	 * Gli URL per token e signup vengono costruiti dinamicamente in base alla regione.
	 */
	public AuthConfig() {
		region = System.getenv("COGNITO_REGION");
		clientId = System.getenv("COGNITO_CLIENT_ID");
		clientSecret = System.getenv("COGNITO_CLIENT_SECRET");
		identityPoolId = System.getenv("COGNITO_IDENTITY_POOL_ID");

		if (region == null) {
			region = "eu-west-1";
		}
		if (clientId == null) {
			clientId = "cuvf5rcnt2n3riofl9os0ja63";
		}
		if (identityPoolId == null) {
			identityPoolId = "eu-west-1:710dcdd9-c2f3-4039-baf0-7768cb3d4c02";
		}

		// Costruisci URL dinamicamente
		tokenUrl = String.format("https://cognito-idp.%s.amazonaws.com/", region);
		signupUrl = String.format("https://%s.auth.%s.amazoncognito.com/signup", "eu-west-1xrobh5glh", region);
		scope = "default-m2m-resource-server-2eb6f9/read";
	}

	/**
	 * Restituisce la regione AWS configurata per Cognito.
	 *
	 * @return La regione AWS (es. "eu-west-1")
	 */
	public String getRegion() {
		return region;
	}

	/**
	 * Restituisce l'ID client dell'applicazione Cognito.
	 *
	 * @return L'ID client dell'applicazione
	 */
	public String getClientId() {
		return clientId;
	}

	/**
	 * Restituisce il client secret per l'autenticazione OAuth2.
	 *
	 * @return Il client secret, o null se non configurato
	 */
	public String getClientSecret() {
		return clientSecret;
	}

	/**
	 * Restituisce l'URL dell'endpoint per l'ottenimento dei token.
	 *
	 * @return L'URL completo dell'endpoint token
	 */
	public String getTokenUrl() {
		return tokenUrl;
	}

	/**
	 * Restituisce l'URL dell'endpoint per la registrazione utenti.
	 *
	 * @return L'URL completo dell'endpoint signup
	 */
	public String getSignupUrl() {
		return signupUrl;
	}

	/**
	 * Restituisce lo scope OAuth2 configurato per le richieste di token.
	 *
	 * @return Lo scope OAuth2
	 */
	public String getScope() {
		return scope;
	}

	/**
	 * Restituisce l'ID del pool identità Cognito per l'autenticazione federata.
	 *
	 * @return L'ID del pool identità
	 */
	public String getIdentityPoolId() {
		return identityPoolId;
	}

	/**
	 * Verifica se la configurazione è valida e completa.
	 *
	 * <p>Una configurazione è considerata valida se:
	 * <ul>
	 *   <li>La regione non è null o vuota
	 *   <li>L'ID client non è null o vuoto
	 *   <li>L'URL del token non è null o vuoto
	 * </ul>
	 *
	 * @return true se la configurazione è valida, false altrimenti
	 */
	public boolean isValid() {
		return region != null && !region.isEmpty() && clientId != null && !clientId.isEmpty() && tokenUrl != null
				&& !tokenUrl.isEmpty();
	}
}