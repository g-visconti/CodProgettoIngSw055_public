package model.entity;

/**
 * Classe che rappresenta un amministratore di agenzia all'interno del sistema.
 * Estende la classe {@link AmministratoreDiSupporto} e rappresenta un amministratore
 * con privilegi limitati a una specifica agenzia immobiliare.
 *
 * <p>Questa classe viene utilizzata per:
 * <ul>
 *   <li>Gestire gli account degli agenti appartenenti alla propria agenzia
 *   <li>Monitorare e moderare gli immobili pubblicati dall'agenzia
 *   <li>Accedere a statistiche e report relativi all'agenzia
 *   <li>Configurare impostazioni specifiche dell'agenzia
 * </ul>
 *
 * <p>L'amministratore di agenzia ha privilegi amministrativi circoscritti
 * alla propria struttura, a differenza dell'amministratore di sistema che
 * ha accesso globale.
 *
 * @author IngSW2425_055 Team
 * @see AmministratoreDiSupporto
 */
public class AmministratoreAgenzia extends AmministratoreDiSupporto {

	/**
	 * Costruttore per la creazione di un amministratore di agenzia.
	 * Richiama il costruttore della superclasse {@link AmministratoreDiSupporto}
	 * impostando automaticamente il ruolo a "Admin".
	 *
	 * @param id        Identificativo univoco dell'account
	 * @param email     Email dell'amministratore
	 * @param password  Password per l'autenticazione
	 * @param nome      Nome dell'amministratore
	 * @param cognome   Cognome dell'amministratore
	 * @param citta     Città di residenza
	 * @param telefono  Numero di telefono
	 * @param cap       Codice di Avviamento Postale
	 * @param indirizzo Indirizzo di residenza
	 * @param ruolo     Parametro non utilizzato (il ruolo è forzato a "Admin")
	 * @param agenzia   Nome o identificativo dell'agenzia di cui è amministratore
	 */
	public AmministratoreAgenzia(String id, String email, String password, String nome, String cognome, String citta,
			String telefono, String cap, String indirizzo, String ruolo, String agenzia) {
		super(id, email, password, nome, cognome, citta, telefono, cap, indirizzo, "Admin", agenzia);
	}
}