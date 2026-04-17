// AccountInfoDTO.java
package model.dto;

/**
 * Data Transfer Object (DTO) per le informazioni di un account.
 * Questa classe rappresenta un trasferimento di dati contenente tutte le informazioni
 * principali di un account, utilizzata per passare i dati tra i vari livelli dell'applicazione
 * senza esporre direttamente l'entità del modello.
 *
 * <p>Il DTO contiene tutte le informazioni personali dell'utente come nome, cognome,
 * email, indirizzo, telefono e ruolo, oltre all'identificativo univoco dell'account.
 *
 * @author IngSW2425_055 Team
 */
public class AccountInfoDTO {
	private final String idAccount;
	private final String email;
	private final String nome;
	private final String cognome;
	private final String telefono;
	private final String citta;
	private final String indirizzo;
	private final String cap;
	private final String ruolo;

	/**
	 * Costruttore completo per AccountInfoDTO.
	 * Inizializza tutte le proprietà dell'account con i valori forniti.
	 *
	 * @param idAccount Identificativo univoco dell'account
	 * @param email Email associata all'account
	 * @param nome Nome dell'utente
	 * @param cognome Cognome dell'utente
	 * @param telefono Numero di telefono dell'utente
	 * @param citta Città di residenza dell'utente
	 * @param indirizzo Indirizzo di residenza dell'utente
	 * @param cap Codice di avviamento postale
	 * @param ruolo Ruolo dell'utente nel sistema (es. "cliente", "agente", "amministratore")
	 */
	public AccountInfoDTO(String idAccount, String email, String nome, String cognome, String telefono, String citta,
			String indirizzo, String cap, String ruolo) {
		this.idAccount = idAccount;
		this.email = email;
		this.nome = nome;
		this.cognome = cognome;
		this.telefono = telefono;
		this.citta = citta;
		this.indirizzo = indirizzo;
		this.cap = cap;
		this.ruolo = ruolo;
	}

	/**
	 * Restituisce l'identificativo univoco dell'account.
	 *
	 * @return ID dell'account
	 */
	public String getIdAccount() {
		return idAccount;
	}

	/**
	 * Restituisce l'email associata all'account.
	 *
	 * @return Email dell'account
	 */
	public String getEmail() {
		return email;
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
	 * Restituisce il cognome dell'utente.
	 *
	 * @return Cognome dell'utente
	 */
	public String getCognome() {
		return cognome;
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
	 * Restituisce la città di residenza dell'utente.
	 *
	 * @return Città di residenza
	 */
	public String getCitta() {
		return citta;
	}

	/**
	 * Restituisce l'indirizzo di residenza dell'utente.
	 *
	 * @return Indirizzo di residenza
	 */
	public String getIndirizzo() {
		return indirizzo;
	}

	/**
	 * Restituisce il codice di avviamento postale.
	 *
	 * @return CAP
	 */
	public String getCap() {
		return cap;
	}

	/**
	 * Restituisce il ruolo dell'utente nel sistema.
	 *
	 * @return Ruolo dell'utente (es. "cliente", "agente", "amministratore")
	 */
	public String getRuolo() {
		return ruolo;
	}

	/**
	 * Restituisce il nome completo dell'utente, formato da nome e cognome concatenati.
	 *
	 * @return Nome e cognome dell'utente separati da uno spazio
	 */
	public String getNomeCompleto() {
		return nome + " " + cognome;
	}
}