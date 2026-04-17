package model.entity;

/**
 * Classe che rappresenta un account utente nel sistema.
 * Contiene tutte le informazioni personali e di autenticazione necessarie
 * per la gestione degli utenti, compresi clienti, agenti e amministratori.
 *
 * <p>Questa entità viene utilizzata per:
 * <ul>
 *   <li>Autenticazione e autorizzazione (login/registrazione)
 *   <li>Gestione del profilo utente
 *   <li>Associazione di immobili agli agenti
 *   <li>Identificazione univoca degli utenti nel sistema
 * </ul>
 *
 * @author IngSW2425_055 Team
 */
public class Account {
	private String idAccount;
	private String email;
	private String nome;
	private String cognome;
	private String password;
	private String citta;
	private String telefono;
	private String cap;
	private String indirizzo;
	private String ruolo;

	/**
	 * Costruttore vuoto per la creazione di un account senza parametri iniziali.
	 * Utile per l'inizializzazione tramite setter o framework di mapping.
	 */
	public Account() {
	}

	/**
	 * Costruttore per la creazione di un account con le credenziali di login.
	 * Utilizzato principalmente durante il processo di autenticazione.
	 *
	 * @param email    Email dell'utente, utilizzata come identificativo di login
	 * @param password Password in chiaro (da hashare prima del salvataggio)
	 * @param ruolo    Ruolo dell'utente nel sistema (es. "Cliente", "Agente", "Amministratore")
	 */
	public Account(String email, String password, String ruolo) {
		this.email = email;
		this.password = password;
		this.ruolo = ruolo;
	}

	/**
	 * Costruttore completo per la registrazione o l'aggiornamento di un account.
	 * Include tutti i campi necessari per la creazione di un profilo utente completo.
	 *
	 * @param id        Identificativo univoco dell'account (generalmente generato dal database)
	 * @param email     Email dell'utente
	 * @param password  Password in chiaro
	 * @param nome      Nome dell'utente
	 * @param cognome   Cognome dell'utente
	 * @param citta     Città di residenza
	 * @param telefono  Numero di telefono
	 * @param cap       Codice di Avviamento Postale
	 * @param indirizzo Indirizzo completo di residenza
	 * @param ruolo     Ruolo nel sistema
	 */
	public Account(String id, String email, String password, String nome, String cognome, String citta, String telefono,
			String cap, String indirizzo, String ruolo) {
		setIdAccount(id);
		this.email = email;
		this.password = password;
		this.nome = nome;
		this.cognome = cognome;
		this.citta = citta;
		this.telefono = telefono;
		this.cap = cap;
		this.indirizzo = indirizzo;
		this.ruolo = ruolo;
	}

	/**
	 * Costruttore per la creazione di un oggetto Account senza password.
	 * Utilizzato quando si visualizzano o manipolano dati del profilo senza
	 * esporre le informazioni sensibili di autenticazione.
	 *
	 * @param id        Identificativo univoco dell'account
	 * @param email     Email dell'utente
	 * @param nome      Nome dell'utente
	 * @param cognome   Cognome dell'utente
	 * @param citta     Città di residenza
	 * @param telefono  Numero di telefono
	 * @param cap       Codice di Avviamento Postale
	 * @param indirizzo Indirizzo completo di residenza
	 * @param ruolo     Ruolo nel sistema
	 */
	public Account(String id, String email, String nome, String cognome, String citta, String telefono, String cap,
			String indirizzo, String ruolo) {
		setIdAccount(id);
		this.email = email;
		this.nome = nome;
		this.cognome = cognome;
		this.citta = citta;
		this.telefono = telefono;
		this.cap = cap;
		this.indirizzo = indirizzo;
		this.ruolo = ruolo;
	}

	// Getter e setter

	/**
	 * Restituisce l'email associata all'account.
	 *
	 * @return Email dell'utente
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Imposta l'email dell'account.
	 *
	 * @param email Nuova email da associare all'account
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Restituisce il nome dell'utente.
	 *
	 * @return Nome dell'utente
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * Imposta il nome dell'utente.
	 *
	 * @param nome Nuovo nome dell'utente
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * Restituisce il cognome dell'utente.
	 *
	 * @return Cognome dell'utente
	 */
	public String getCognome() {
		return cognome;
	}

	/**
	 * Imposta il cognome dell'utente.
	 *
	 * @param cognome Nuovo cognome dell'utente
	 */
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	/**
	 * Restituisce la password dell'account.
	 * <b>Nota:</b> in un sistema di produzione, la password dovrebbe essere sempre hashata
	 * e questo metodo potrebbe restituire solo un hash.
	 *
	 * @return Password dell'account (in chiaro o hashata)
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Imposta la password dell'account.
	 * <b>Nota:</b> si consiglia di hashare la password prima di chiamare questo metodo.
	 *
	 * @param password Nuova password (in chiaro o hashata)
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Restituisce la città di residenza dell'utente.
	 *
	 * @return Città di residenza
	 */
	public String getCitta() {
		return citta;
	}

	/**
	 * Imposta la città di residenza dell'utente.
	 *
	 * @param citta Nuova città di residenza
	 */
	public void setCitta(String citta) {
		this.citta = citta;
	}

	/**
	 * Restituisce il numero di telefono dell'utente.
	 *
	 * @return Numero di telefono
	 */
	public String getTelefono() {
		return telefono;
	}

	/**
	 * Imposta il numero di telefono dell'utente.
	 *
	 * @param telefono Nuovo numero di telefono
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	/**
	 * Restituisce il CAP (Codice di Avviamento Postale) dell'utente.
	 *
	 * @return CAP di residenza
	 */
	public String getCap() {
		return cap;
	}

	/**
	 * Imposta il CAP dell'utente.
	 *
	 * @param cap Nuovo CAP di residenza
	 */
	public void setCap(String cap) {
		this.cap = cap;
	}

	/**
	 * Restituisce l'indirizzo completo di residenza.
	 *
	 * @return Indirizzo di residenza
	 */
	public String getIndirizzo() {
		return indirizzo;
	}

	/**
	 * Imposta l'indirizzo di residenza.
	 *
	 * @param indirizzo Nuovo indirizzo di residenza
	 */
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	/**
	 * Restituisce il ruolo dell'utente nel sistema.
	 * I ruoli tipici includono: "Cliente", "Agente", "Amministratore".
	 *
	 * @return Ruolo dell'utente
	 */
	public String getRuolo() {
		return ruolo;
	}

	/**
	 * Imposta il ruolo dell'utente nel sistema.
	 *
	 * @param ruolo Nuovo ruolo dell'utente
	 */
	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}

	/**
	 * Restituisce l'identificativo univoco dell'account.
	 * Questo ID è generalmente generato dal database e utilizzato come chiave primaria.
	 *
	 * @return ID univoco dell'account
	 */
	public String getIdAccount() {
		return idAccount;
	}

	/**
	 * Imposta l'identificativo univoco dell'account.
	 * Utilizzato principalmente durante il caricamento dei dati dal database.
	 *
	 * @param idAccount Nuovo ID dell'account
	 */
	public void setIdAccount(String idAccount) {
		this.idAccount = idAccount;
	}
}