package model.entity;

/**
 * Classe che rappresenta un cliente all'interno del sistema immobiliare.
 * Estende la classe {@link Account} e rappresenta un utente con il ruolo di cliente,
 * che può cercare, visualizzare e salvare preferenze sugli immobili.
 *
 * <p>Questa classe viene utilizzata per:
 * <ul>
 *   <li>Gestire l'autenticazione e il profilo dei clienti
 *   <li>Tracciare le preferenze di ricerca immobiliare
 *   <li>Gestire le prenotazioni di visite agli immobili
 *   <li>Salvare gli immobili preferiti (preferiti/wishlist)
 * </ul>
 *
 * <p>Il cliente è l'utente finale che utilizza il sistema per trovare
 * immobili in affitto o in vendita, a differenza degli agenti che
 * gestiscono le inserzioni.
 *
 * @author IngSW2425_055 Team
 * @see Account
 */
public class Cliente extends Account {

	/**
	 * Costruttore completo per la creazione di un cliente.
	 * Utilizzato principalmente durante la registrazione o l'aggiornamento
	 * del profilo quando sono disponibili tutte le informazioni.
	 *
	 * @param id        Identificativo univoco dell'account
	 * @param email     Email del cliente
	 * @param password  Password per l'autenticazione
	 * @param nome      Nome del cliente
	 * @param cognome   Cognome del cliente
	 * @param citta     Città di residenza
	 * @param telefono  Numero di telefono
	 * @param cap       Codice di Avviamento Postale
	 * @param indirizzo Indirizzo di residenza
	 * @param ruolo     Ruolo nel sistema (tipicamente "Cliente")
	 */
	public Cliente(String id, String email, String password, String nome, String cognome, String citta, String telefono,
			String cap, String indirizzo, String ruolo) {
		super(id, email, password, nome, cognome, citta, telefono, cap, indirizzo, ruolo);
	}

	/**
	 * Costruttore per la creazione di un cliente con le sole credenziali di base.
	 * Utilizzato principalmente durante il login o la registrazione iniziale
	 * quando non sono ancora disponibili tutte le informazioni personali.
	 *
	 * @param email     Email del cliente, utilizzata come identificativo di login
	 * @param password  Password per l'autenticazione
	 * @param ruolo     Ruolo nel sistema (tipicamente "Cliente")
	 */
	public Cliente(String email, String password, String ruolo) {
		super(email, password, ruolo);
	}
}