package model.entity;

/**
 * Classe che rappresenta un agente immobiliare all'interno del sistema.
 * Estende la classe {@link Account} aggiungendo l'informazione specifica
 * dell'agenzia di appartenenza.
 *
 * <p>Questa specializzazione di Account viene utilizzata per:
 * <ul>
 *   <li>Identificare gli utenti con privilegi di gestione immobili
 *   <li>Associare gli immobili caricati all'agenzia di riferimento
 *   <li>Filtrare e ricercare agenti in base all'agenzia
 *   <li>Gestire le informazioni professionali specifiche dell'agente
 * </ul>
 *
 * @author IngSW2425_055 Team
 * @see Account
 */
public class AgenteImmobiliare extends Account {

	private String agenzia;

	/**
	 * Costruttore completo per la creazione di un agente immobiliare.
	 * Richiama il costruttore della superclasse {@link Account} e inizializza
	 * l'attributo specifico dell'agenzia.
	 *
	 * @param id        Identificativo univoco dell'account (ereditato da Account)
	 * @param email     Email dell'agente
	 * @param password  Password per l'autenticazione
	 * @param nome      Nome dell'agente
	 * @param cognome   Cognome dell'agente
	 * @param citta     Città di residenza
	 * @param telefono  Numero di telefono
	 * @param cap       Codice di Avviamento Postale
	 * @param indirizzo Indirizzo di residenza
	 * @param ruolo     Ruolo nel sistema (tipicamente "Agente")
	 * @param agenzia   Nome o identificativo dell'agenzia immobiliare di appartenenza
	 */
	public AgenteImmobiliare(String id, String email, String password, String nome, String cognome, String citta,
			String telefono, String cap, String indirizzo, String ruolo, String agenzia) {
		super(id, email, password, nome, cognome, citta, telefono, cap, indirizzo, ruolo);
		this.agenzia = agenzia;
	}

	// Getter e setter

	/**
	 * Restituisce il nome o identificativo dell'agenzia immobiliare
	 * a cui l'agente è associato.
	 *
	 * @return Nome dell'agenzia di appartenenza
	 */
	public String getAgenzia() {
		return agenzia;
	}

	/**
	 * Imposta o modifica l'agenzia di appartenenza dell'agente immobiliare.
	 * Utile per trasferimenti o cambi di agenzia.
	 *
	 * @param agenzia Nuova agenzia di appartenenza
	 */
	public void setAgenzia(String agenzia) {
		this.agenzia = agenzia;
	}
}