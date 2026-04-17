package database;

import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

/**
 * La classe ConnessioneDatabase gestisce la connessione al database PostgreSQL
 * utilizzando il pattern Singleton per garantire una singola istanza di connessione
 * in tutta l'applicazione DietiEstates25.
 *
 * <p>Questa classe fornisce:
 * <ul>
 *   <li>Gestione centralizzata della connessione al database AWS RDS</li>
 *   <li>Implementazione del pattern Singleton per evitare connessioni multiple</li>
 *   <li>Gestione robusta degli errori di connessione con messaggi utente appropriati</li>
 *   <li>Logging dettagliato delle operazioni di connessione</li>
 *   <li>Riconnessione automatica in caso di connessione chiusa</li>
 * </ul>
 *
 * <p>Configurazione del database:
 * <ul>
 *   <li>Host: database AWS RDS in us-east-1</li>
 *   <li>Driver: PostgreSQL JDBC</li>
 *   <li>Database: DietiEstates25DB</li>
 *   <li>Credenziali: memorizzate nella classe (da considerare per sicurezza in produzione)</li>
 * </ul>
 *
 * @author IngSW2425_055 Team
 * @see Connection
 * @see DriverManager
 * @see Logger
 */
public class ConnessioneDatabase {

	// Attributi
	private static ConnessioneDatabase instance;
	private static final String DRIVER = "org.postgresql.Driver";
	// Logger per la gestione degli errori
	private static final Logger logger = Logger.getLogger(ConnessioneDatabase.class.getName());
	private final String nome = "postgres";
	private final String password = "PostgresDB";
	private final String url = "jdbc:postgresql://database-1.cshockuiujqi.us-east-1.rds.amazonaws.com:5432/DietiEstates25DB";
	// Connessione
	private Connection connection;

	/**
	 * Costruttore privato per il pattern Singleton.
	 * Inizializza la connessione al database PostgreSQL su AWS RDS e gestisce
	 * eventuali errori con messaggi appropriati all'utente.
	 *
	 * <p>Il costruttore esegue le seguenti operazioni:
	 * <ol>
	 *   <li>Carica il driver JDBC PostgreSQL</li>
	 *   <li>Stabilisce la connessione con le credenziali configurate</li>
	 *   <li>Gestisce diversi tipi di errori con messaggi utente specifici</li>
	 *   <li>Logga il risultato dell'operazione</li>
	 * </ol>
	 *
	 * @throws SQLException Se si verifica un errore durante la connessione al database.
	 *                      L'errore viene loggato e viene mostrato un messaggio all'utente
	 *                      in base al tipo di problema riscontrato.
	 * @throws ClassNotFoundException Se il driver PostgreSQL non è disponibile nel classpath
	 */
	// Costruttore privato
	private ConnessioneDatabase() throws SQLException {
		try {
			// Carica il driver PostgreSQL
			Class.forName(DRIVER);

			// Crea la connessione
			connection = DriverManager.getConnection(url, nome, password);
			logger.info("Connessione al database avvenuta con successo.");

		} catch (ClassNotFoundException e) {
			logger.log(Level.SEVERE, "Driver PostgreSQL non trovato.", e);
			JOptionPane.showMessageDialog(null,
					"Errore interno: driver PostgreSQL non trovato.\nContattare l'amministratore del sistema.",
					"Errore di connessione", JOptionPane.ERROR_MESSAGE);
			throw new SQLException("Errore durante il caricamento del driver", e);

		} catch (SQLException e) {
			// Controlla se la causa è UnknownHostException (nessuna rete o host non valido)
			if (e.getCause() instanceof UnknownHostException) {
				logger.log(Level.SEVERE, "Host del database non raggiungibile.", e);
				JOptionPane.showMessageDialog(null,
						"Attenzione: il database al momento non è raggiungibile.\n"
								+ "Verifica la connessione di rete e riprova.",
								"Connessione non disponibile", JOptionPane.WARNING_MESSAGE);
			} else {
				logger.log(Level.SEVERE, "Errore di connessione al database.", e);
				JOptionPane.showMessageDialog(null,
						"Si è verificato un errore durante la connessione al database.\n"
								+ "Controlla la connessione Internet o riprova più tardi.",
								"Errore di connessione", JOptionPane.ERROR_MESSAGE);
			}
			throw e; // rilancia per permettere gestione a livello superiore, se serve
		}
	}

	/**
	 * Restituisce l'oggetto {@link Connection} attualmente attivo per il database.
	 * La connessione viene gestita internamente dalla classe e non dovrebbe essere
	 * chiusa direttamente dai chiamanti.
	 *
	 * @return La connessione attiva al database PostgreSQL.
	 * @throws IllegalStateException Se la connessione non è stata inizializzata
	 *                               (dovrebbe essere gestita da {@link #getInstance()})
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * Restituisce l'istanza Singleton della connessione al database.
	 * Se l'istanza non esiste o la connessione è chiusa, ne crea una nuova.
	 *
	 * <p>Questo metodo implementa il pattern Singleton con lazy initialization
	 * e gestisce automaticamente la riconnessione in caso di connessione chiusa.
	 *
	 * @return L'istanza Singleton di {@link ConnessioneDatabase}.
	 * @throws SQLException Se non riesce a creare una nuova connessione quando necessario.
	 *                      L'errore può essere causato da problemi di rete, credenziali
	 *                      errate o database non raggiungibile.
	 */
	public static ConnessioneDatabase getInstance() throws SQLException {
		if (instance == null) {
			instance = new ConnessioneDatabase();
		} else if (instance.connection == null || instance.connection.isClosed()) {
			instance = new ConnessioneDatabase();
		}
		return instance;
	}

	/**
	 * Chiude la connessione al database in modo sicuro.
	 * Questo metodo dovrebbe essere chiamato quando l'applicazione viene terminata
	 * o quando la connessione non è più necessaria per un periodo prolungato.
	 *
	 * <p>Il metodo:
	 * <ul>
	 *   <li>Verifica che la connessione esista e non sia già chiusa</li>
	 *   <li>Chiude la connessione in modo sicuro</li>
	 *   <li>Logga l'esito dell'operazione</li>
	 *   <li>Gestisce silenziosamente eventuali errori di chiusura</li>
	 * </ul>
	 *
	 * <p>Note di sicurezza:
	 * <ul>
	 *   <li>Dopo la chiusura, {@link #getInstance()} creerà una nuova connessione quando necessario</li>
	 *   <li>In un ambiente multi-thread, la chiusura dovrebbe essere coordinata</li>
	 * </ul>
	 */
	public void closeConnection() {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
				logger.info("Connessione al database chiusa correttamente.");
			}
		} catch (SQLException e) {
			logger.log(Level.WARNING, "Errore durante la chiusura della connessione.", e);
		}
	}
}