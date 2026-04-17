package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

import model.dto.AccountInfoDTO;
import model.entity.Account;
import model.entity.AgenteImmobiliare;
import model.entity.AmministratoreDiSupporto;
import model.entity.Cliente;

/**
 * Data Access Object per la gestione degli account nel sistema.
 * Fornisce metodi per interagire con la tabella "Account" del database,
 * inclusa la creazione, lettura, aggiornamento e verifica degli account.
 *
 * <p>Questa classe gestisce:
 * <ul>
 *   <li>Verifica esistenza email</li>
 *   <li>Autenticazione utenti</li>
 *   <li>Creazione nuovi account</li>
 *   <li>Recupero informazioni profilo</li>
 *   <li>Gestione password</li>
 *   <li>Ricerca account per email o ID</li>
 * </ul>
 *
 * @author IngSW2425_055 Team
 * @see Account
 * @see Connection
 */
public class AccountDAO {

	private final Connection connection;

	/**
	 * Costruttore per AccountDAO.
	 * Inizializza il DAO con una connessione al database.
	 *
	 * @param connection Connessione al database da utilizzare per tutte le operazioni
	 * @throws IllegalArgumentException Se la connessione è null
	 */
	public AccountDAO(Connection connection) {
		this.connection = connection;
	}

	/**
	 * Verifica se un'email esiste già nel database.
	 *
	 * <p>Questo metodo controlla se esiste già un account con l'email specificata,
	 * utile durante la registrazione per evitare duplicati.
	 *
	 * @param email Email da verificare
	 * @return true se l'email esiste già, false altrimenti
	 * @throws SQLException Se si verifica un errore durante l'accesso al database
	 */
	public boolean emailEsiste(String email) throws SQLException {
		boolean result = false;
		final String sql = "SELECT 1 FROM \"Account\" WHERE email = ?";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setString(1, email);
			final ResultSet rs = stmt.executeQuery();
			if (rs.next()) { // true se ottengo una tupla con le credenziali passate per parametri
				result = true;
			}

			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Verifica le credenziali di accesso di un utente.
	 *
	 * <p>Controlla se esiste un account con l'email specificata e se la password
	 * corrisponde a quella memorizzata (hashata con BCrypt).
	 *
	 * @param email Email dell'account
	 * @param passwordInserita Password in chiaro inserita dall'utente
	 * @return true se le credenziali sono valide, false altrimenti
	 * @throws SQLException Se si verifica un errore durante l'accesso al database
	 * @see BCrypt#checkpw(String, String)
	 */
	public boolean checkCredenziali(String email, String passwordInserita) throws SQLException {
		final String query = "SELECT password FROM \"Account\" WHERE email = ?";
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, email);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					final String hashedPassword = rs.getString("password");
					return BCrypt.checkpw(passwordInserita, hashedPassword);
				} else {
					return false; // nessun account con questa email
				}
			}
		}
	}

	/**
	 * Crea un nuovo account nel database.
	 *
	 * <p>Inserisce un nuovo record nella tabella "Account" con i dati forniti
	 * e restituisce l'ID generato automaticamente dal database.
	 *
	 * @param account Oggetto Account contenente tutti i dati dell'utente
	 * @return ID dell'account appena creato
	 * @throws SQLException Se si verifica un errore durante l'inserimento nel database
	 * @throws IllegalArgumentException Se l'account è null o contiene dati non validi
	 */
	public String insertAccount(Account account) throws SQLException {

		final String query = "INSERT INTO \"Account\" (\"email\", \"password\", \"nome\", \"cognome\", \"citta\", \"numeroTelefono\",\"cap\", \"indirizzo\", ruolo ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement stmt = connection.prepareStatement(query, new String[] { "idAccount" })) {
			stmt.setString(1, account.getEmail());
			stmt.setString(2, account.getPassword());
			stmt.setString(3, account.getNome());
			stmt.setString(4, account.getCognome());
			stmt.setString(5, account.getCitta());
			stmt.setString(6, account.getTelefono());
			stmt.setString(7, account.getCap());
			stmt.setString(8, account.getIndirizzo());
			stmt.setString(9, account.getRuolo());

			final int affectedRows = stmt.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Creazione account fallita, nessuna riga inserita.");
			}

			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					return generatedKeys.getString(1); // ritorna idAccount generato
				} else {
					throw new SQLException("Creazione account fallita, nessun ID generato.");
				}
			}
		}
	}

	/**
	 * Crea un nuovo account per un cliente nel database.
	 *
	 * <p>Versione specializzata per oggetti Cliente che utilizza RETURNING
	 * per ottenere immediatamente l'ID generato.
	 *
	 * @param cliente Oggetto Cliente da inserire nel database
	 * @return ID dell'account appena creato
	 * @throws SQLException Se si verifica un errore durante l'inserimento nel database
	 * @throws IllegalArgumentException Se il cliente è null o contiene dati non validi
	 */
	public String insertAccount(Cliente cliente) throws SQLException {
		// Lasciamo il DB generare idAccount, quindi non lo inseriamo
		final String query = "INSERT INTO \"Account\" (\"email\", \"password\", \"nome\", \"cognome\", \"citta\", \"numeroTelefono\", \"cap\", \"indirizzo\", ruolo) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING \"idAccount\"";

		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, cliente.getEmail());
			stmt.setString(2, cliente.getPassword());
			stmt.setString(3, cliente.getNome());
			stmt.setString(4, cliente.getCognome());
			stmt.setString(5, cliente.getCitta());
			stmt.setString(6, cliente.getTelefono());
			stmt.setString(7, cliente.getCap());
			stmt.setString(8, cliente.getIndirizzo());
			stmt.setString(9, cliente.getRuolo());

			final ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				// restituiamo la string generata dal DB
				return rs.getString("idAccount");
			} else {
				throw new SQLException("Creazione account fallita, nessun ID generato.");
			}
		}
	}

	/**
	 * Recupera i dettagli completi di un account, inclusi i ruoli specializzati.
	 *
	 * <p>Questo metodo recupera non solo i dati base dell'account ma anche
	 * informazioni specifiche per AgenteImmobiliare o AmministratoreDiSupporto
	 * tramite JOIN con le rispettive tabelle.
	 *
	 * @param idAccount ID dell'account da recuperare
	 * @return Oggetto Account (o sottoclasse) con tutti i dettagli, null se non trovato
	 * @throws SQLException Se si verifica un errore durante l'accesso al database
	 * @see AgenteImmobiliare
	 * @see AmministratoreDiSupporto
	 */
	public Account getAccountDettagli(String idAccount) throws SQLException {
		final String sql = "SELECT a.\"idAccount\", a.nome, a.cognome, a.email, a.\"numeroTelefono\", "
				+ "       ag.agenzia, s.\"id\" AS supporto_id " + "FROM \"Account\" a "
				+ "LEFT JOIN \"AgenteImmobiliare\" ag ON a.\"idAccount\" = ag.id "
				+ "LEFT JOIN \"AmministratoreDiSupporto\" s ON a.\"idAccount\" = s.id " + "WHERE a.\"idAccount\" = ?";

		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setString(1, idAccount);
			final ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				final String id = rs.getString("idAccount");
				final String nome = rs.getString("nome");
				final String cognome = rs.getString("cognome");
				final String email = rs.getString("email");
				final String numeroTelefono = rs.getString("numeroTelefono");
				final String agenzia = rs.getString("agenzia");
				final String supportoId = rs.getString("supporto_id");

				System.out.println("DEBUG: id=" + id + ", nome=" + nome + ", cognome=" + cognome + ", email=" + email
						+ ", numeroTelefono=" + numeroTelefono + ", agenzia=" + agenzia + ", supporto_id="
						+ supportoId);

				if (supportoId != null) {
					return new AmministratoreDiSupporto(id, // id
							email, // email
							null, // password
							nome, // nome
							cognome, // cognome
							null, // citta
							numeroTelefono, // telefono
							null, // cap
							null, // indirizzo
							"Supporto", // ruolo
							agenzia // agenzia
							);
				} else if (agenzia != null) {
					return new AgenteImmobiliare(id, email, null, nome, cognome, null, numeroTelefono, null, null,
							"Agente", agenzia);
				} else {
					return new Account(id, email, null, nome, cognome, null, numeroTelefono, null, null, "Account");
				}

			} else {
				return null;
			}
		}
	}

	/**
	 * Recupera il ruolo di un account basato sull'email.
	 *
	 * <p>Utile per determinare i permessi e le funzionalità disponibili
	 * per un utente durante l'autenticazione e la navigazione nel sistema.
	 *
	 * @param email Email dell'account
	 * @return Ruolo dell'account (es. "Cliente", "Agente", "Supporto")
	 * @throws SQLException Se si verifica un errore durante l'accesso al database
	 * @throws SQLException Se nessun account viene trovato con l'email specificata
	 */
	public String getRuoloByEmail(String email) throws SQLException {
		final String sql = "SELECT ruolo FROM \"Account\" WHERE email = ?";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setString(1, email);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getString("ruolo");
				} else {
					throw new SQLException("Nessun account trovato con email: " + email);
				}
			}
		}
	}

	/**
	 * Recupera l'ID di un account basato sull'email.
	 *
	 * <p>Restituisce solo i primi 3 caratteri dell'ID dell'account.
	 * Se l'account non viene trovato, restituisce "undef".
	 *
	 * @param email Email dell'account
	 * @return ID abbreviato dell'account (primi 3 caratteri) o "undef" se non trovato
	 * @throws SQLException Se si verifica un errore durante l'accesso al database
	 */
	public String getId(String email) throws SQLException {
		String result = "undef";
		final String query = "SELECT SUBSTRING(\"idAccount\", 1, 3) AS \"idAccount\" FROM \"Account\" WHERE email = ?";

		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, email);
			final ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				result = rs.getString("idAccount");
			}

			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * Cambia la password di un account.
	 *
	 * <p>Aggiorna la password nel database per l'account specificato.
	 * Si presume che la validazione della nuova password sia stata effettuata
	 * prima della chiamata a questo metodo.
	 *
	 * @param emailAssociata Email dell'account di cui cambiare la password
	 * @param pass Nuova password (già hashata)
	 * @param confermaPass Conferma della nuova password (non utilizzata in questo metodo)
	 * @return true se l'aggiornamento è avvenuto con successo, false altrimenti
	 * @throws SQLException Se si verifica un errore durante l'aggiornamento del database
	 */
	public boolean cambiaPassword(String emailAssociata, String pass, String confermaPass) {
		boolean result = false;
		final String query = "UPDATE \"Account\" SET \"password\" = ? WHERE email = ?";
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, pass);
			stmt.setString(2, emailAssociata);

			final int rowsUpdated = stmt.executeUpdate();

			if (rowsUpdated > 0) {
				result = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Recupera le informazioni del profilo di un utente.
	 *
	 * <p>Restituisce un DTO contenente tutte le informazioni di base
	 * dell'account necessarie per visualizzare il profilo utente.
	 *
	 * @param emailUtente Email dell'utente di cui recuperare le informazioni
	 * @return AccountInfoDTO con i dati del profilo, null se l'utente non viene trovato
	 * @throws SQLException Se si verifica un errore durante l'accesso al database
	 * @see AccountInfoDTO
	 */
	public AccountInfoDTO getInfoProfiloDAO(String emailUtente) throws SQLException {
		final String query = "SELECT \"idAccount\", email, nome, cognome, \"numeroTelefono\", "
				+ "citta, indirizzo, cap, ruolo " + "FROM \"Account\" WHERE email = ?";

		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, emailUtente);
			final ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return new AccountInfoDTO(rs.getString("idAccount"), rs.getString("email"), rs.getString("nome"),
						rs.getString("cognome"), rs.getString("numeroTelefono"), rs.getString("citta"),
						rs.getString("indirizzo"), rs.getString("cap"), rs.getString("ruolo"));
			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * Recupera l'ID completo di un account basato sull'email.
	 *
	 * <p>Restituisce l'ID intero dell'account. Se l'account non viene trovato,
	 * restituisce "undef".
	 *
	 * @param email Email dell'account
	 * @return ID completo dell'account o "undef" se non trovato
	 * @throws SQLException Se si verifica un errore durante l'accesso al database
	 */
	public String getIdAccountByEmail(String email) throws SQLException {
		String idAccount = "undef";
		final String query = "SELECT \"idAccount\" FROM \"Account\" WHERE email = ?";

		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, email);
			final ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				idAccount = rs.getString("idAccount");
			}

			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return idAccount;
	}

	/**
	 * Recupera l'agente associato a un immobile specifico.
	 *
	 * <p>Utile per determinare quale agente è responsabile di un determinato immobile,
	 * ad esempio per visualizzare le informazioni di contatto o per la gestione delle prenotazioni.
	 *
	 * @param idImmobile ID dell'immobile
	 * @return ID dell'agente associato o "inesistente" se non trovato
	 * @throws SQLException Se si verifica un errore durante l'accesso al database
	 */
	public String getAgenteAssociato(long idImmobile) {
		String agenteAssociato = "inesistente";
		final String query = "SELECT \"agenteAssociato\" FROM \"Immobile\" WHERE \"idImmobile\" = ?";

		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setLong(1, idImmobile);
			final ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				agenteAssociato = rs.getString("agenteAssociato");
			}

			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return agenteAssociato;
	}

	/**
	 * Recupera un account completo basato sull'email.
	 *
	 * <p>Restituisce un oggetto Account con tutti i campi popolati.
	 * Utile quando sono necessarie tutte le informazioni dell'account.
	 *
	 * @param email Email dell'account da recuperare
	 * @return Oggetto Account completo, null se non trovato
	 * @throws SQLException Se si verifica un errore durante l'accesso al database
	 */
	public Account getAccountByEmail(String email) throws SQLException {
		final String query = "SELECT * FROM \"Account\" WHERE \"email\" = ?";

		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, email);
			final ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				final Account account = new Account();
				account.setIdAccount(rs.getString("idAccount"));
				account.setEmail(rs.getString("email"));
				account.setNome(rs.getString("nome"));
				account.setCognome(rs.getString("cognome"));
				account.setTelefono(rs.getString("numeroTelefono"));
				account.setCitta(rs.getString("citta"));
				account.setCap(rs.getString("cap"));
				account.setIndirizzo(rs.getString("indirizzo"));
				account.setRuolo(rs.getString("ruolo"));
				return account;
			}
		}
		return null;
	}

	/**
	 * Recupera un account completo basato sull'ID.
	 *
	 * <p>Restituisce un oggetto Account con tutti i campi popolati, inclusa la password.
	 * Utile per operazioni amministrative o di recupero dati completi.
	 *
	 * @param idAccount ID dell'account da recuperare
	 * @return Oggetto Account completo, null se non trovato
	 * @throws SQLException Se si verifica un errore durante l'accesso al database
	 */
	public Account getAccountById(String idAccount) throws SQLException {
		final String query = "SELECT * FROM \"Account\" WHERE \"idAccount\" = ?";

		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, idAccount);
			final ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				final Account account = new Account();
				account.setIdAccount(rs.getString("idAccount"));
				account.setEmail(rs.getString("email"));
				account.setNome(rs.getString("nome"));
				account.setCognome(rs.getString("cognome"));
				account.setTelefono(rs.getString("numeroTelefono"));
				account.setCitta(rs.getString("citta"));
				account.setCap(rs.getString("cap"));
				account.setIndirizzo(rs.getString("indirizzo"));
				account.setRuolo(rs.getString("ruolo"));
				account.setPassword(rs.getString("password"));

				return account;
			}
		}
		return null;
	}
}