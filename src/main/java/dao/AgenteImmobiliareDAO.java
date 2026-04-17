package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import model.entity.AgenteImmobiliare;

/**
 * Data Access Object per la gestione degli agenti immobiliari nel sistema.
 * Fornisce metodi per interagire con la tabella "AgenteImmobiliare" del database,
 * specializzata per le operazioni specifiche degli agenti immobiliari.
 *
 * <p>Questa classe gestisce:
 * <ul>
 *   <li>Inserimento di nuovi agenti immobiliari</li>
 *   <li>Associazione tra account e agenzia</li>
 * </ul>
 *
 * <p>Nota: Questa classe opera in combinazione con AccountDAO per gestire
 * le informazioni complete dell'agente (dati personali + informazioni professionali).
 *
 * @author IngSW2425_055 Team
 * @see AgenteImmobiliare
 * @see AccountDAO
 * @see Connection
 */
public class AgenteImmobiliareDAO {
	private final Connection conn;

	/**
	 * Costruttore per AgenteImmobiliareDAO.
	 * Inizializza il DAO con una connessione al database.
	 *
	 * @param conn Connessione al database da utilizzare per tutte le operazioni
	 * @throws IllegalArgumentException Se la connessione è null
	 */
	public AgenteImmobiliareDAO(Connection conn) {
		this.conn = conn;
	}

	/**
	 * Inserisce un nuovo agente immobiliare nel database.
	 *
	 * <p>Questo metodo salva le informazioni specifiche dell'agente (agenzia di appartenenza)
	 * nella tabella dedicata. L'account deve essere già stato creato nella tabella "Account"
	 * poiché questo metodo utilizza l'ID dell'account come chiave esterna.
	 *
	 * @param agente Oggetto AgenteImmobiliare contenente i dati professionali dell'agente
	 * @throws SQLException Se si verifica un errore durante l'inserimento nel database
	 * @throws IllegalArgumentException Se l'agente è null o non contiene dati validi
	 * @see AgenteImmobiliare
	 */
	public void insertAgente(AgenteImmobiliare agente) throws SQLException {
		final String sql = "INSERT INTO \"AgenteImmobiliare\" (id, agenzia) VALUES (?, ?)";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, agente.getIdAccount()); // ID generato dal DB in Account
			ps.setString(2, agente.getAgenzia());
			ps.executeUpdate();
		}
	}
}