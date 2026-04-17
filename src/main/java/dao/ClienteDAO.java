package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import model.entity.Cliente;

/**
 * Data Access Object per la gestione dei clienti nel sistema.
 * Fornisce metodi per interagire con la tabella "Cliente" del database,
 * specializzata per le operazioni specifiche dei clienti.
 *
 * <p>Questa classe gestisce:
 * <ul>
 *   <li>Inserimento di nuovi clienti</li>
 *   <li>Associazione tra account generale e ruolo cliente</li>
 * </ul>
 *
 * <p>Nota: Questa classe opera in combinazione con AccountDAO per gestire
 * le informazioni complete del cliente. La tabella "Cliente" contiene
 * informazioni aggiuntive specifiche per il ruolo di cliente oltre a quelle
 * già presenti nella tabella "Account".
 *
 * @author IngSW2425_055 Team
 * @see Cliente
 * @see AccountDAO
 * @see Connection
 */
public class ClienteDAO {
	private final Connection connection;

	/**
	 * Costruttore per ClienteDAO.
	 * Inizializza il DAO con una connessione al database.
	 *
	 * @param connection Connessione al database da utilizzare per tutte le operazioni
	 * @throws IllegalArgumentException Se la connessione è null
	 */
	public ClienteDAO(Connection connection) {
		this.connection = connection;
	}

	/**
	 * Inserisce un nuovo cliente nel database.
	 *
	 * <p>Questo metodo salva le informazioni specifiche del cliente nella
	 * tabella dedicata "Cliente". L'account deve essere già stato creato
	 * nella tabella "Account" poiché questo metodo utilizza l'ID dell'account
	 * come chiave esterna.
	 *
	 * <p>La tabella "Cliente" contiene principalmente l'associazione tra
	 * l'ID dell'account e il ruolo cliente, permettendo di estendere le
	 * funzionalità specifiche per i clienti in futuro.
	 *
	 * @param cliente Oggetto Cliente contenente i dati specifici del cliente
	 * @throws SQLException Se si verifica un errore durante l'inserimento nel database
	 * @throws IllegalArgumentException Se l'oggetto cliente è null o non contiene dati validi
	 * @see Cliente
	 */
	public void insertCliente(Cliente cliente) throws SQLException {
		final String query = "INSERT INTO \"Cliente\" (\"id\", \"email\") VALUES (?, ?)";
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, cliente.getIdAccount());
			stmt.setString(2, cliente.getEmail());
			stmt.executeUpdate();
		}
	}

}