package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import model.entity.AmministratoreDiSupporto;

/**
 * Data Access Object per la gestione degli amministratori di supporto nel sistema.
 * Fornisce metodi per interagire con la tabella "AmministratoreDiSupporto" del database,
 * specializzata per le operazioni specifiche degli amministratori di supporto.
 *
 * <p>Questa classe gestisce:
 * <ul>
 *   <li>Inserimento di nuovi amministratori di supporto</li>
 *   <li>Associazione tra account e agenzia per il ruolo di supporto</li>
 * </ul>
 *
 * <p>Nota: Questa classe opera in combinazione con AccountDAO per gestire
 * le informazioni complete dell'amministratore di supporto (dati personali + informazioni professionali).
 * Gli amministratori di supporto hanno privilegi limitati rispetto agli amministratori di agenzia.
 *
 * @author IngSW2425_055 Team
 * @see AmministratoreDiSupporto
 * @see AccountDAO
 * @see Connection
 */
public class AmministratoreDiSupportoDAO {
	private final Connection conn;

	/**
	 * Costruttore per AmministratoreDiSupportoDAO.
	 * Inizializza il DAO con una connessione al database.
	 *
	 * @param conn Connessione al database da utilizzare per tutte le operazioni
	 * @throws IllegalArgumentException Se la connessione è null
	 */
	public AmministratoreDiSupportoDAO(Connection conn) {
		this.conn = conn;
	}

	/**
	 * Inserisce un nuovo amministratore di supporto nel database.
	 *
	 * <p>Questo metodo salva le informazioni specifiche dell'amministratore di supporto
	 * (agenzia di appartenenza) nella tabella dedicata. L'account deve essere già stato
	 * creato nella tabella "Account" poiché questo metodo utilizza l'ID dell'account
	 * come chiave esterna.
	 *
	 * @param supporto Oggetto AmministratoreDiSupporto contenente i dati professionali
	 *                 dell'amministratore di supporto
	 * @throws SQLException Se si verifica un errore durante l'inserimento nel database
	 * @throws IllegalArgumentException Se l'oggetto supporto è null o non contiene dati validi
	 * @see AmministratoreDiSupporto
	 */
	public void insertSupporto(AmministratoreDiSupporto supporto) throws SQLException {
		final String sql = "INSERT INTO \"AmministratoreDiSupporto\" (id, agenzia) VALUES (?, ?)";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, supporto.getIdAccount());
			ps.setString(2, supporto.getAgenzia());
			ps.executeUpdate();
		}
	}

}