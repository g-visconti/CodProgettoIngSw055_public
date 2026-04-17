package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import model.entity.OffertaIniziale;

/**
 * Data Access Object per la gestione delle offerte iniziali nel sistema.
 * Fornisce metodi per interagire con la tabella "OffertaIniziale" del database,
 * gestendo tutte le operazioni CRUD relative alle offerte fatte dai clienti.
 *
 * <p>Questa classe gestisce:
 * <ul>
 *   <li>Inserimento di nuove offerte iniziali</li>
 *   <li>Recupero di offerte per cliente o immobile</li>
 *   <li>Aggiornamento dello stato delle offerte</li>
 *   <li>Ricerca di offerte specifiche per ID</li>
 * </ul>
 *
 * <p>Le offerte iniziali rappresentano le proposte di acquisto o affitto
 * fatte dai clienti sugli immobili pubblicati nel sistema.
 *
 * @author IngSW2425_055 Team
 * @see OffertaIniziale
 * @see Connection
 */
public class OffertaInizialeDAO {
	private final Connection connection;

	/**
	 * Costruttore per OffertaInizialeDAO.
	 * Inizializza il DAO con una connessione al database.
	 *
	 * @param connection Connessione al database da utilizzare per tutte le operazioni
	 * @throws IllegalArgumentException Se la connessione è null
	 */
	public OffertaInizialeDAO(Connection connection) {
		this.connection = connection;
	}

	/**
	 * Inserisce una nuova offerta iniziale nel database.
	 *
	 * <p>Questo metodo salva una proposta di offerta fatta da un cliente
	 * per un immobile specifico. L'offerta viene memorizzata con stato
	 * iniziale "In attesa" in attesa di valutazione da parte dell'agente.
	 *
	 * @param offerta Oggetto OffertaIniziale contenente tutti i dati dell'offerta
	 * @return true se l'inserimento è avvenuto con successo, false altrimenti
	 * @throws SQLException Se si verifica un errore durante l'inserimento nel database
	 * @throws IllegalArgumentException Se l'offerta è null o contiene dati non validi
	 * @see OffertaIniziale
	 */
	public boolean inserisciOffertaIniziale(OffertaIniziale offerta) throws SQLException {
		final String query = "INSERT INTO \"OffertaIniziale\" (\"importoProposto\", \"clienteAssociato\", \"immobileAssociato\", \"dataOfferta\", \"stato\") VALUES (?, ?, ?, ?, ?)";

		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setDouble(1, offerta.getImportoProposto());
			stmt.setString(2, offerta.getClienteAssociato());
			stmt.setLong(3, offerta.getImmobileAssociato());
			stmt.setTimestamp(4, Timestamp.valueOf(offerta.getDataOfferta()));
			stmt.setString(5, offerta.getStato());

			final int rowsInserted = stmt.executeUpdate();
			return rowsInserted > 0;
		}
	}

	/**
	 * Recupera tutte le offerte iniziali fatte da un cliente specifico.
	 *
	 * <p>Utile per visualizzare lo storico delle offerte di un cliente
	 * o per permettere al cliente di tracciare le proprie proposte.
	 *
	 * @param clienteAssociato ID del cliente di cui recuperare le offerte
	 * @return Lista di OffertaIniziale fatte dal cliente, ordinate per data decrescente
	 * @throws SQLException Se si verifica un errore durante l'accesso al database
	 * @see OffertaIniziale
	 */
	public List<OffertaIniziale> getOfferteByCliente(String clienteAssociato) throws SQLException {
		final List<OffertaIniziale> offerte = new ArrayList<>();
		final String query = "SELECT * FROM \"OffertaIniziale\" WHERE \"clienteAssociato\" = ? ORDER BY \"dataOfferta\" DESC";

		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, clienteAssociato);
			final ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				final OffertaIniziale offerta = new OffertaIniziale(rs.getDouble("importoProposto"),
						rs.getString("clienteAssociato"), rs.getLong("immobileAssociato"));
				offerta.setIdOfferta(rs.getLong("idOfferta"));
				offerta.setDataOfferta(rs.getTimestamp("dataOfferta").toLocalDateTime());
				offerta.setStato(rs.getString("stato"));
				offerte.add(offerta);
			}
		}
		return offerte;
	}

	/**
	 * Recupera tutte le offerte iniziali ricevute per un immobile specifico.
	 *
	 * <p>Utile per permettere all'agente immobiliare di visualizzare
	 * tutte le proposte ricevute per un immobile che gestisce.
	 *
	 * @param immobileAssociato ID dell'immobile di cui recuperare le offerte
	 * @return Lista di OffertaIniziale ricevute per l'immobile, ordinate per data decrescente
	 * @throws SQLException Se si verifica un errore durante l'accesso al database
	 * @see OffertaIniziale
	 */
	public List<OffertaIniziale> getOfferteByImmobile(Long immobileAssociato) throws SQLException {
		final List<OffertaIniziale> offerte = new ArrayList<>();
		final String query = "SELECT * FROM \"OffertaIniziale\" WHERE \"immobileAssociato\" = ? ORDER BY \"dataOfferta\" DESC";

		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setLong(1, immobileAssociato);
			final ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				final OffertaIniziale offerta = new OffertaIniziale(rs.getDouble("importoProposto"),
						rs.getString("clienteAssociato"), rs.getLong("immobileAssociato"));
				offerta.setIdOfferta(rs.getLong("idOfferta"));
				offerta.setDataOfferta(rs.getTimestamp("dataOfferta").toLocalDateTime());
				offerta.setStato(rs.getString("stato"));
				offerte.add(offerta);
			}
		}
		return offerte;
	}

	/**
	 * Aggiorna lo stato di una offerta iniziale.
	 *
	 * <p>Permette all'agente immobiliare di modificare lo stato di un'offerta,
	 * ad esempio da "In attesa" a "Accettata" o "Rifiutata".
	 *
	 * @param idOfferta ID dell'offerta da aggiornare
	 * @param nuovoStato Nuovo stato da assegnare all'offerta
	 * @return true se l'aggiornamento è avvenuto con successo, false altrimenti
	 * @throws SQLException Se si verifica un errore durante l'aggiornamento nel database
	 */
	public boolean aggiornaStatoOfferta(Long idOfferta, String nuovoStato) throws SQLException {
		final String query = "UPDATE \"OffertaIniziale\" SET \"stato\" = ? WHERE \"idOfferta\" = ?";

		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, nuovoStato);
			stmt.setLong(2, idOfferta);

			final int rowsUpdated = stmt.executeUpdate();
			return rowsUpdated > 0;
		}
	}

	/**
	 * Recupera un'offerta iniziale specifica basandosi sul suo ID.
	 *
	 * <p>Utile per ottenere tutti i dettagli di una singola offerta,
	 * ad esempio per la visualizzazione dettagliata o per operazioni di modifica.
	 *
	 * @param idOfferta ID dell'offerta da recuperare
	 * @return Oggetto OffertaIniziale completo, null se non trovato
	 * @throws SQLException Se si verifica un errore durante l'accesso al database
	 * @see OffertaIniziale
	 */
	public OffertaIniziale getOffertaById(long idOfferta) throws SQLException {
		final String query = "SELECT * FROM \"OffertaIniziale\" WHERE \"idOfferta\" = ?";

		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setLong(1, idOfferta);
			final ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				final OffertaIniziale offerta = new OffertaIniziale(rs.getDouble("importoProposto"),
						rs.getString("clienteAssociato"), rs.getLong("immobileAssociato"));
				offerta.setIdOfferta(rs.getLong("idOfferta"));
				offerta.setDataOfferta(rs.getTimestamp("dataOfferta").toLocalDateTime());
				offerta.setStato(rs.getString("stato"));
				return offerta;
			}
		}
		return null;
	}

	/**
	 * Recupera l'ID del cliente associato a una specifica offerta.
	 *
	 * <p>Metodo utile per ottenere rapidamente l'identificativo del cliente
	 * che ha fatto una determinata offerta, senza dover recuperare tutti i dati.
	 *
	 * @param idOfferta ID dell'offerta di cui recuperare il cliente
	 * @return ID del cliente associato all'offerta, null se non trovato
	 * @throws SQLException Se si verifica un errore durante l'accesso al database
	 */
	public String getClienteByOffertaId(long idOfferta) throws SQLException {
		final String query = "SELECT \"clienteAssociato\" FROM \"OffertaIniziale\" WHERE \"idOfferta\" = ?";

		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setLong(1, idOfferta);
			final ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return rs.getString("clienteAssociato");
			}
		}
		return null;
	}
}