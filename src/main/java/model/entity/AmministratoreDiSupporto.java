package model.entity;

/**
 * Classe che rappresenta un amministratore di supporto all'interno del sistema.
 * Estende la classe {@link AgenteImmobiliare} e rappresenta un amministratore
 * con privilegi di supporto tecnico o amministrativo all'interno di un'agenzia.
 *
 * <p>Questa classe viene utilizzata per:
 * <ul>
 *   <li>Fornire supporto tecnico agli agenti dell'agenzia
 *   <li>Gestire problemi tecnici o burocratici relativi agli immobili
 *   <li>Assistere nella moderazione dei contenuti pubblicati
 *   <li>Accedere a funzionalità di amministrazione limitate all'agenzia
 * </ul>
 *
 * <p>L'amministratore di supporto ha privilegi superiori a quelli di un
 * normale agente immobiliare ma inferiori a quelli di un amministratore
 * di sistema, con focus specifico sulle attività di supporto.
 *
 * @author IngSW2425_055 Team
 * @see AgenteImmobiliare
 */
public class AmministratoreDiSupporto extends AgenteImmobiliare {

	/**
	 * Costruttore per la creazione di un amministratore di supporto.
	 * Richiama il costruttore della superclasse {@link AgenteImmobiliare}
	 * impostando automaticamente il ruolo a "Supporto".
	 *
	 * @param id        Identificativo univoco dell'account
	 * @param email     Email dell'amministratore di supporto
	 * @param password  Password per l'autenticazione
	 * @param nome      Nome dell'amministratore
	 * @param cognome   Cognome dell'amministratore
	 * @param citta     Città di residenza
	 * @param telefono  Numero di telefono
	 * @param cap       Codice di Avviamento Postale
	 * @param indirizzo Indirizzo di residenza
	 * @param ruolo     Parametro non utilizzato (il ruolo è forzato a "Supporto")
	 * @param agenzia   Nome o identificativo dell'agenzia di appartenenza
	 */
	public AmministratoreDiSupporto(String id, String email, String password, String nome, String cognome, String citta,
			String telefono, String cap, String indirizzo, String ruolo, String agenzia) {
		super(id, email, password, nome, cognome, citta, telefono, cap, indirizzo, "Supporto", agenzia);
	}
}