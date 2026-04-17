package model.entity;

import java.time.LocalDateTime;

/**
 * Classe che rappresenta un'offerta iniziale fatta da un cliente per un immobile.
 * Contiene tutte le informazioni relative a una proposta di acquisto o affitto,
 * inclusi i dati del cliente, dell'immobile, l'importo proposto e lo stato della negoziazione.
 *
 * <p>Questa classe viene utilizzata per:
 * <ul>
 *   <li>Registrare le offerte dei clienti sugli immobili
 *   <li>Tracciare lo stato della negoziazione (in attesa, accettata, rifiutata)
 *   <li>Gestire la cronologia delle offerte per ogni immobile
 *   <li>Fornire dati per statistiche e report sulle negoziazioni
 * </ul>
 *
 * <p>Le offerte possono essere per immobili sia in vendita che in affitto,
 * e rappresentano il punto di partenza per una negoziazione tra cliente e agente.
 *
 * @author IngSW2425_055 Team
 * @see Immobile
 * @see Cliente
 */
public class OffertaIniziale {

	// Attributi

	/**
	 * Identificativo univoco dell'offerta nel database.
	 */
	private long idOfferta;

	/**
	 * ID dell'immobile a cui si riferisce l'offerta.
	 */
	private long immobileAssociato;

	/**
	 * ID del cliente che ha fatto l'offerta.
	 */
	private String clienteAssociato;

	/**
	 * Data e ora in cui l'offerta è stata effettuata.
	 */
	private LocalDateTime dataOfferta;

	/**
	 * Stato corrente dell'offerta.
	 * Valori possibili: "In attesa", "Accettata", "Rifiutata", "Ritirata".
	 */
	private String stato;

	/**
	 * Importo economico proposto dal cliente.
	 * Per immobili in vendita: prezzo di acquisto totale.
	 * Per immobili in affitto: canone mensile.
	 */
	private double importoProposto;

	/**
	 * Costruttore per la creazione di una nuova offerta iniziale.
	 * Imposta automaticamente la data corrente e lo stato iniziale a "In attesa".
	 *
	 * @param importoProposto   Importo economico proposto dal cliente
	 * @param clienteAssociato  ID del cliente che effettua l'offerta
	 * @param immobileAssociato ID dell'immobile a cui si riferisce l'offerta
	 */
	public OffertaIniziale(double importoProposto, String clienteAssociato, long immobileAssociato) {
		this.importoProposto = importoProposto;
		this.clienteAssociato = clienteAssociato;
		this.immobileAssociato = immobileAssociato;
		dataOfferta = LocalDateTime.now().withNano(0);
		stato = "In attesa";
	}

	// Getter

	/**
	 * Restituisce la data e ora in cui l'offerta è stata effettuata.
	 *
	 * @return Data e ora dell'offerta
	 */
	public LocalDateTime getDataOfferta() {
		return dataOfferta;
	}

	/**
	 * Restituisce l'ID del cliente che ha effettuato l'offerta.
	 *
	 * @return ID del cliente associato
	 */
	public String getClienteAssociato() {
		return clienteAssociato;
	}

	/**
	 * Restituisce l'ID dell'immobile a cui si riferisce l'offerta.
	 *
	 * @return ID dell'immobile associato
	 */
	public long getImmobileAssociato() {
		return immobileAssociato;
	}

	/**
	 * Restituisce l'identificativo univoco dell'offerta.
	 *
	 * @return ID dell'offerta
	 */
	public long getIdOfferta() {
		return idOfferta;
	}

	/**
	 * Restituisce l'importo economico proposto dal cliente.
	 *
	 * @return Importo proposto
	 */
	public double getImportoProposto() {
		return importoProposto;
	}

	/**
	 * Restituisce lo stato corrente dell'offerta.
	 *
	 * @return Stato dell'offerta (es. "In attesa", "Accettata", "Rifiutata")
	 */
	public String getStato() {
		return stato;
	}

	// Setter

	/**
	 * Imposta la data e ora dell'offerta.
	 * Utile per modifiche o correzioni sulla data di registrazione.
	 *
	 * @param dataOfferta Nuova data e ora dell'offerta
	 */
	public void setDataOfferta(LocalDateTime dataOfferta) {
		this.dataOfferta = dataOfferta;
	}

	/**
	 * Imposta l'ID del cliente associato all'offerta.
	 * Utilizzato principalmente per correzioni o trasferimenti di offerta.
	 *
	 * @param clienteAssociato Nuovo ID del cliente
	 */
	public void setClienteAssociato(String clienteAssociato) {
		this.clienteAssociato = clienteAssociato;
	}

	/**
	 * Imposta l'ID dell'immobile associato all'offerta.
	 * Utilizzato principalmente per correzioni o trasferimenti di offerta.
	 *
	 * @param immobileAssociato Nuovo ID dell'immobile
	 */
	public void setImmobileAssociato(long immobileAssociato) {
		this.immobileAssociato = immobileAssociato;
	}

	/**
	 * Imposta l'identificativo univoco dell'offerta.
	 * Generalmente chiamato dal layer di persistenza dopo l'inserimento nel database.
	 *
	 * @param idOfferta Nuovo ID dell'offerta
	 */
	public void setIdOfferta(long idOfferta) {
		this.idOfferta = idOfferta;
	}

	/**
	 * Imposta l'importo economico proposto.
	 * Può essere utilizzato per aggiornamenti o correzioni dell'importo.
	 *
	 * @param importoProposto Nuovo importo proposto
	 */
	public void setImportoProposto(double importoProposto) {
		this.importoProposto = importoProposto;
	}

	/**
	 * Imposta lo stato corrente dell'offerta.
	 * Utilizzato per aggiornare lo stato durante la negoziazione.
	 *
	 * @param stato Nuovo stato dell'offerta
	 */
	public void setStato(String stato) {
		this.stato = stato;
	}

	// Metodi (debugging)

	/**
	 * Restituisce una rappresentazione in formato stringa dell'offerta.
	 * Utile per debugging e logging.
	 *
	 * @return Stringa contenente tutti gli attributi dell'offerta
	 */
	@Override
	public String toString() {
		return "OffertaIniziale{" + "idOfferta=" + idOfferta + ", immobileAssociato=" + immobileAssociato
				+ ", clienteAssociato='" + clienteAssociato + '\'' + ", dataOfferta=" + dataOfferta + ", stato='"
				+ stato + '\'' + ", importoProposto=" + importoProposto + '}';
	}
}