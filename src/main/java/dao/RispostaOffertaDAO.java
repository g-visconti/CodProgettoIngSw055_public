package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.entity.RispostaOfferta;

/**
 * Data Access Object per la gestione delle risposte alle offerte nel sistema.
 * Fornisce metodi per interagire con la tabella "RispostaOfferta" del database,
 * gestendo tutte le operazioni CRUD relative alle risposte degli agenti alle offerte dei clienti.
 *
 * <p>Questa classe gestisce:
 * <ul>
 *   <li>Inserimento di nuove risposte alle offerte</li>
 *   <li>Recupero di risposte per offerta specifica</li>
 *   <li>Gestione delle risposte attive e disattivazione di quelle precedenti</li>
 *   <li>Recupero di dettagli completi delle risposte con informazioni dell'agente</li>
 *   <li>Gestione delle controproposte di prezzo</li>
 * </ul>
 *
 * <p>Le risposte alle offerte rappresentano le risposte degli agenti immobiliari
 * alle offerte iniziali dei clienti, che possono essere accettazioni, rifiuti
 * o controproposte con nuovi importi.
 *
 * @author IngSW2425_055 Team
 * @see RispostaOfferta
 * @see Connection
 */
public class RispostaOffertaDAO {
	private final Connection connection;

	/**
	 * Costruttore per RispostaOffertaDAO.
	 * Inizializza il DAO con una connessione al database.
	 *
	 * @param connection Connessione al database da utilizzare per tutte le operazioni
	 * @throws IllegalArgumentException Se la connessione è null
	 */
	public RispostaOffertaDAO(Connection connection) {
		this.connection = connection;
	}

	/**
	 * Inserisce una nuova risposta a un'offerta nel database.
	 *
	 * <p>Questo metodo salva la risposta di un agente a un'offerta iniziale di un cliente.
	 * Gestisce correttamente il valore null per l'importo della controproposta quando non applicabile.
	 * La risposta viene memorizzata come attiva di default.
	 *
	 * @param risposta Oggetto RispostaOfferta contenente tutti i dati della risposta
	 * @return true se l'inserimento è avvenuto con successo, false altrimenti
	 * @throws SQLException Se si verifica un errore durante l'inserimento nel database
	 * @throws IllegalArgumentException Se la risposta è null o contiene dati non validi
	 * @see RispostaOfferta
	 */
	public boolean inserisciRispostaOfferta(RispostaOfferta risposta) throws SQLException {
		final String query = "INSERT INTO \"RispostaOfferta\" (\"offertaInizialeAssociata\", \"agenteAssociato\", \"tipoRisposta\", \"importoControproposta\", \"dataRisposta\", \"attiva\") VALUES (?, ?, ?, ?, ?, ?)";
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setLong(1, risposta.getOffertaInizialeAssociata());
			stmt.setString(2, risposta.getAgenteAssociato());
			stmt.setString(3, risposta.getTipoRisposta());

			// Gestione del valore null per importoControproposta
			if (risposta.getImportoControproposta() != null) {
				stmt.setDouble(4, risposta.getImportoControproposta());
			} else {
				stmt.setNull(4, java.sql.Types.DOUBLE);
			}

			stmt.setTimestamp(5, java.sql.Timestamp.valueOf(risposta.getDataRisposta()));
			stmt.setBoolean(6, risposta.getAttiva());

			final int rowsInserted = stmt.executeUpdate();
			return rowsInserted > 0;
		}
	}

	/**
	 * Recupera tutte le risposte associate a una specifica offerta iniziale.
	 *
	 * <p>Utile per visualizzare lo storico completo delle risposte a un'offerta,
	 * incluse quelle non più attive. Le risposte sono ordinate per data decrescente.
	 *
	 * @param offertaInizialeAssociata ID dell'offerta iniziale di cui recuperare le risposte
	 * @return Lista di RispostaOfferta associate all'offerta, ordinate per data decrescente
	 * @throws SQLException Se si verifica un errore durante l'accesso al database
	 * @see RispostaOfferta
	 */
	public List<RispostaOfferta> getRisposteByOfferta(Long offertaInizialeAssociata) throws SQLException {
		final List<RispostaOfferta> risposte = new ArrayList<>();
		final String query = "SELECT * FROM \"RispostaOfferta\" WHERE \"offertaInizialeAssociata\" = ? ORDER BY \"dataRisposta\" DESC";
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setLong(1, offertaInizialeAssociata);
			final ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				final RispostaOfferta risposta = new RispostaOfferta();
				risposta.setIdRisposta(rs.getLong("idRisposta"));
				risposta.setOffertaInizialeAssociata(rs.getLong("offertaInizialeAssociata"));
				risposta.setAgenteAssociato(rs.getString("agenteAssociato"));
				risposta.setTipoRisposta(rs.getString("tipoRisposta"));

				final double importo = rs.getDouble("importoControproposta");
				risposta.setImportoControproposta(rs.wasNull() ? null : importo);

				risposta.setDataRisposta(rs.getTimestamp("dataRisposta").toLocalDateTime());
				risposta.setAttiva(rs.getBoolean("attiva"));
				risposte.add(risposta);
			}
		}
		return risposte;
	}

	/**
	 * Recupera la risposta attiva corrente per una specifica offerta iniziale.
	 *
	 * <p>Poiché solo una risposta può essere attiva per ogni offerta in un dato momento,
	 * questo metodo restituisce la risposta attualmente valida per la negoziazione.
	 *
	 * @param offertaInizialeAssociata ID dell'offerta iniziale di cui recuperare la risposta attiva
	 * @return RispostaOfferta attiva per l'offerta specificata, null se nessuna risposta attiva
	 * @throws SQLException Se si verifica un errore durante l'accesso al database
	 * @see RispostaOfferta
	 */
	public RispostaOfferta getRispostaAttivaByOfferta(Long offertaInizialeAssociata) throws SQLException {
		final String query = "SELECT * FROM \"RispostaOfferta\" WHERE \"offertaInizialeAssociata\" = ? AND \"attiva\" = true";
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setLong(1, offertaInizialeAssociata);
			final ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				final RispostaOfferta risposta = new RispostaOfferta();
				risposta.setIdRisposta(rs.getLong("idRisposta"));
				risposta.setOffertaInizialeAssociata(rs.getLong("offertaInizialeAssociata"));
				risposta.setAgenteAssociato(rs.getString("agenteAssociato"));
				risposta.setTipoRisposta(rs.getString("tipoRisposta"));

				final double importo = rs.getDouble("importoControproposta");
				risposta.setImportoControproposta(rs.wasNull() ? null : importo);

				risposta.setDataRisposta(rs.getTimestamp("dataRisposta").toLocalDateTime());
				risposta.setAttiva(rs.getBoolean("attiva"));
				return risposta;
			}
		}
		return null;
	}

	/**
	 * Disattiva tutte le risposte precedenti per una specifica offerta iniziale.
	 *
	 * <p>Questo metodo viene chiamato quando viene creata una nuova risposta
	 * per assicurare che solo una risposta per offerta sia attiva alla volta.
	 * Mantiene lo storico delle risposte precedenti ma le marca come non attive.
	 *
	 * @param offertaInizialeAssociata ID dell'offerta iniziale di cui disattivare le risposte precedenti
	 * @return true se l'operazione è stata eseguita, false in caso di errore
	 * @throws SQLException Se si verifica un errore durante l'aggiornamento nel database
	 */
	public boolean disattivaRispostePrecedenti(Long offertaInizialeAssociata) throws SQLException {
		final String query = "UPDATE \"RispostaOfferta\" SET \"attiva\" = false WHERE \"offertaInizialeAssociata\" = ?";
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setLong(1, offertaInizialeAssociata);
			final int rowsUpdated = stmt.executeUpdate();
			return rowsUpdated >= 0;
		}
	}

	/**
	 * Recupera i dettagli completi della risposta attiva per un'offerta, inclusi i dati dell'agente.
	 *
	 * <p>Questo metodo esegue una JOIN con la tabella Account per recuperare
	 * anche il nome e cognome dell'agente che ha fornito la risposta.
	 *
	 * @param offertaInizialeAssociata ID dell'offerta iniziale di cui recuperare i dettagli della risposta attiva
	 * @return RispostaOfferta completa con informazioni dell'agente, null se nessuna risposta attiva
	 * @throws SQLException Se si verifica un errore durante l'accesso al database
	 * @see RispostaOfferta
	 */
	public RispostaOfferta getDettagliRispostaAttiva(Long offertaInizialeAssociata) throws SQLException {
		final String query = "SELECT r.*, a.nome, a.cognome " + "FROM \"RispostaOfferta\" r "
				+ "JOIN \"Account\" a ON r.\"agenteAssociato\" = a.\"idAccount\" "
				+ "WHERE r.\"offertaInizialeAssociata\" = ? AND r.\"attiva\" = true";

		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setLong(1, offertaInizialeAssociata);
			final ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				final RispostaOfferta risposta = new RispostaOfferta();
				risposta.setIdRisposta(rs.getLong("idRisposta"));
				risposta.setOffertaInizialeAssociata(rs.getLong("offertaInizialeAssociata"));
				risposta.setAgenteAssociato(rs.getString("agenteAssociato"));
				risposta.setTipoRisposta(rs.getString("tipoRisposta"));

				final double importo = rs.getDouble("importoControproposta");
				risposta.setImportoControproposta(rs.wasNull() ? null : importo);

				risposta.setDataRisposta(rs.getTimestamp("dataRisposta").toLocalDateTime());
				risposta.setAttiva(rs.getBoolean("attiva"));
				risposta.setNomeAgente(rs.getString("nome"));
				risposta.setCognomeAgente(rs.getString("cognome"));

				return risposta;
			}
		}
		return null;
	}

	/**
	 * Recupera i dettagli della controproposta attiva per un'offerta specifica di un cliente.
	 *
	 * <p>Questo metodo restituisce un array di stringhe contenente i dettagli
	 * della controproposta attiva, inclusi nome e cognome dell'agente,
	 * data della risposta, importo della controproposta e tipo di risposta.
	 *
	 * @param idOfferta ID dell'offerta di cui recuperare la controproposta
	 * @param idCliente ID del cliente che ha fatto l'offerta (per verifica di sicurezza)
	 * @return Array di stringhe con i dettagli della controproposta, null se non trovata
	 * @throws SQLException Se si verifica un errore durante l'accesso al database
	 */
	public String[] getContropropostaByOffertaCliente(Long idOfferta, String idCliente) throws SQLException {
		final String query = "SELECT a.nome, a.cognome, r.\"dataRisposta\", r.\"importoControproposta\", r.\"tipoRisposta\" "
				+ "FROM \"OffertaIniziale\" o "
				+ "JOIN \"RispostaOfferta\" r ON o.\"idOfferta\" = r.\"offertaInizialeAssociata\" "
				+ "JOIN \"Account\" a ON a.\"idAccount\" = r.\"agenteAssociato\" "
				+ "WHERE o.\"idOfferta\" = ? AND o.\"clienteAssociato\" = ? AND r.\"attiva\" = true";

		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setLong(1, idOfferta);
			stmt.setString(2, idCliente);
			final ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				final String[] result = new String[5];
				result[0] = rs.getString("nome");
				result[1] = rs.getString("cognome");
				result[2] = rs.getTimestamp("dataRisposta").toString();

				final double importo = rs.getDouble("importoControproposta");
				result[3] = rs.wasNull() ? "N/A" : String.valueOf(importo);

				result[4] = rs.getString("tipoRisposta");
				return result;
			} else {
				return null;
			}
		}
	}
}