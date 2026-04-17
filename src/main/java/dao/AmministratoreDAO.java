package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data Access Object per la gestione degli amministratori di agenzia nel sistema.
 * Fornisce metodi per interagire con le tabelle relative agli amministratori,
 * con particolare attenzione al recupero delle informazioni sull'agenzia associata.
 *
 * <p>Questa classe gestisce:
 * <ul>
 *   <li>Recupero dell'agenzia associata a un amministratore</li>
 *   <li>Ricerca tramite email dell'amministratore</li>
 * </ul>
 *
 * <p>Nota: Questa classe opera principalmente con la tabella "AmministratoreAgenzia"
 * in combinazione con la tabella "Account" per ottenere informazioni complete.
 *
 * @author IngSW2425_055 Team
 * @see Connection
 * @see ResultSet
 */
public class AmministratoreDAO {
	private final Connection conn;

	/**
	 * Costruttore per AmministratoreDAO.
	 * Inizializza il DAO con una connessione al database.
	 *
	 * @param conn Connessione al database da utilizzare per tutte le operazioni
	 * @throws IllegalArgumentException Se la connessione è null
	 */
	public AmministratoreDAO(Connection conn) {
		this.conn = conn;
	}

	/**
	 * Recupera l'agenzia associata a un amministratore basandosi sulla sua email.
	 *
	 * <p>Questo metodo esegue una query JOIN tra le tabelle "AmministratoreAgenzia"
	 * e "Account" per trovare l'agenzia gestita dall'amministratore specificato.
	 * Utile per determinare il contesto operativo di un amministratore quando
	 * accede al sistema.
	 *
	 * @param emailAdmin Email dell'amministratore di cui recuperare l'agenzia
	 * @return Nome dell'agenzia associata all'amministratore, null se non trovato
	 *         o se l'utente non è un amministratore di agenzia
	 * @throws SQLException Se si verifica un errore durante l'accesso al database
	 *         (gestita internamente e stampata nello stack trace)
	 */
	public String getAgenziaByEmail(String emailAdmin) {
		String agenzia = null;
		final String sql = """
				    SELECT aa."agenzia"
				    FROM "AmministratoreAgenzia" aa
				    JOIN "Account" acc ON aa.id = acc."idAccount"
				    WHERE acc."email" = ?
				""";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, emailAdmin);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					agenzia = rs.getString("agenzia");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return agenzia;
	}
}