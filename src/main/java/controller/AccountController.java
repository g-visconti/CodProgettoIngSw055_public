package controller;

import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import dao.AccountDAO;
import dao.AmministratoreDAO;
import dao.OffertaInizialeDAO;
import dao.RispostaOffertaDAO;
import database.ConnessioneDatabase;
import model.dto.AccountInfoDTO;
import model.entity.Account;
import model.entity.RispostaOfferta;

/**
 * Controller per la gestione degli account utente e operazioni correlate.
 * Questa classe fornisce metodi per recuperare informazioni account,
 * gestire l'autenticazione, modificare password e gestire risposte alle offerte.
 *
 * <p>La classe gestisce:
 * <ul>
 *   <li>Recupero informazioni profilo utente
 *   <li>Conversione email in ID account
 *   <li>Gestione ruoli utente
 *   <li>Modifica password
 *   <li>Inserimento risposte a offerte immobiliari
 * </ul>
 *
 * @author IngSW2425_055 Team
 * @see AccountDAO
 * @see AccountInfoDTO
 * @see RispostaOfferta
 */
public class AccountController {

	/**
	 * Costruttore di default per l'AccountController.
	 */
	public AccountController() {
	}

	/**
	 * Converte un indirizzo email nel corrispondente ID account.
	 *
	 * <p>Il metodo ricerca l'ID account associato all'email fornita.
	 * Utile per operazioni che richiedono l'identificativo univoco dell'account.
	 *
	 * @param email Email dell'account da convertire
	 * @return ID dell'account corrispondente all'email, o null se non trovato
	 * @throws SQLException Se si verifica un errore durante l'accesso al database
	 * @see AccountDAO#getId(String)
	 */
	public String emailToId(String email) throws SQLException {
		final Connection connAWS = ConnessioneDatabase.getInstance().getConnection();
		final AccountDAO accountDAO = new AccountDAO(connAWS);
		return accountDAO.getId(email);
	}

	/**
	 * Recupera l'ID account associato a un'email specifica.
	 *
	 * <p>Simile a {@link #emailToId(String)}, ma con nome più esplicito
	 * per chiarezza nel codice chiamante.
	 *
	 * @param email Email dell'account
	 * @return ID dell'account, o null se non trovato
	 * @throws SQLException Se si verifica un errore durante l'accesso al database
	 * @see AccountDAO#getIdAccountByEmail(String)
	 */
	public String getIdAccountByEmail(String email) throws SQLException {
		final Connection connAWS = ConnessioneDatabase.getInstance().getConnection();
		final AccountDAO accountDAO = new AccountDAO(connAWS);
		return accountDAO.getIdAccountByEmail(email);
	}

	/**
	 * Recupera le informazioni complete del profilo utente attualmente in sessione.
	 *
	 * <p>Il metodo raccoglie tutte le informazioni anagrafiche e di account
	 * associate all'email dell'utente loggato.
	 *
	 * @param emailUtente Email dell'utente di cui recuperare il profilo
	 * @return DTO contenente tutte le informazioni del profilo, o null se non trovato
	 * @throws SQLException Se si verifica un errore durante l'accesso al database
	 * @see AccountInfoDTO
	 * @see AccountDAO#getInfoProfiloDAO(String)
	 */
	public AccountInfoDTO getInfoProfilo(String emailUtente) throws SQLException {
		try (Connection connAWS = ConnessioneDatabase.getInstance().getConnection()) {
			final AccountDAO accountDAO = new AccountDAO(connAWS);
			return accountDAO.getInfoProfiloDAO(emailUtente);
		}
	}

	/**
	 * Recupera il ruolo di un utente in base alla sua email.
	 *
	 * <p>Il ruolo determina i permessi e le funzionalità disponibili
	 * per l'utente nel sistema (es. "admin", "agente", "cliente").
	 *
	 * @param email Email dell'utente
	 * @return Il ruolo dell'utente, o null se non trovato
	 * @throws SQLException Se si verifica un errore durante l'accesso al database
	 * @see AccountDAO#getRuoloByEmail(String)
	 */
	public String getRuoloByEmail(String email) throws SQLException {
		final Connection connAWS = ConnessioneDatabase.getInstance().getConnection();
		final AccountDAO accountDAO = new AccountDAO(connAWS);
		return accountDAO.getRuoloByEmail(email);
	}

	/**
	 * Recupera i dettagli completi di un agente immobiliare tramite il suo ID account.
	 *
	 * <p>Il metodo restituisce un oggetto Account contenente tutte le informazioni
	 * anagrafiche e professionali dell'agente.
	 *
	 * @param idAccount ID dell'account agente
	 * @return Oggetto Account con i dettagli dell'agente, o null se non trovato
	 * @see Account
	 * @see AccountDAO#getAccountDettagli(String)
	 */
	public Account recuperaDettagliAgente(String idAccount) {
		try {
			final Connection connAWS = ConnessioneDatabase.getInstance().getConnection();
			final AccountDAO accountDAO = new AccountDAO(connAWS);
			return accountDAO.getAccountDettagli(idAccount);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Aggiorna la password di un account utente.
	 *
	 * <p>Il metodo verifica che la nuova password e la conferma corrispondano,
	 * applica l'hashing e aggiorna il record nel database.
	 *
	 * @param emailAssociata Email dell'account di cui modificare la password
	 * @param pass Nuova password
	 * @param confermaPass Conferma della nuova password
	 * @return true se l'aggiornamento ha successo, false altrimenti
	 * @throws SQLException Se si verifica un errore durante l'accesso al database
	 * @see AccountDAO#cambiaPassword(String, String, String)
	 */
	public boolean updatePassword(String emailAssociata, String pass, String confermaPass) throws SQLException {
		final Connection connAWS = ConnessioneDatabase.getInstance().getConnection();
		final AccountDAO accountDAO = new AccountDAO(connAWS);

		return accountDAO.cambiaPassword(emailAssociata, pass, confermaPass);
	}

	/**
	 * Recupera l'agenzia di appartenenza di un utente (agente o amministratore).
	 *
	 * <p>Il metodo è utile per determinare a quale agenzia immobiliare
	 * appartiene un agente o amministratore di supporto.
	 *
	 * @param email Email dell'utente
	 * @return Nome dell'agenzia, o null se non trovato o non applicabile
	 * @see AmministratoreDAO#getAgenziaByEmail(String)
	 */
	public String getAgenzia(String email) {
		String agenzia = null;
		try {
			final Connection connAWS = ConnessioneDatabase.getInstance().getConnection();
			final AmministratoreDAO adminDAO = new AmministratoreDAO(connAWS);
			agenzia = adminDAO.getAgenziaByEmail(email);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return agenzia;
	}

	/**
	 * Inserisce una risposta a un'offerta immobiliare da parte di un agente.
	 *
	 * <p>Il metodo gestisce l'intero flusso di risposta a un'offerta:
	 * <ul>
	 *   <li>Recupera i dati dell'agente
	 *   <li>Disattiva risposte precedenti alla stessa offerta
	 *   <li>Inserisce la nuova risposta
	 *   <li>Aggiorna lo stato dell'offerta
	 * </ul>
	 *
	 * @param idOfferta ID dell'offerta a cui rispondere
	 * @param idAgente ID dell'agente che risponde
	 * @param tipoRisposta Tipo di risposta ("accettata", "rifiutata", "controproposta")
	 * @param importoControproposta Importo della controproposta (solo se tipoRisposta = "controproposta")
	 * @return true se l'inserimento ha successo, false altrimenti
	 * @throws SQLException Se si verifica un errore durante l'accesso al database
	 * @see RispostaOfferta
	 * @see RispostaOffertaDAO
	 * @see OffertaInizialeDAO
	 */
	public boolean inserisciRispostaOfferta(long idOfferta, String idAgente, String tipoRisposta,
			Double importoControproposta) {
		try {
			final Connection connAWS = ConnessioneDatabase.getInstance().getConnection();
			final RispostaOffertaDAO rispostaDAO = new RispostaOffertaDAO(connAWS);

			// Recupera nome e cognome dell'agente
			final AccountDAO accountDAO = new AccountDAO(connAWS);
			final Account agente = accountDAO.getAccountById(idAgente);

			if (agente == null) {
				JOptionPane.showMessageDialog(null, "Agente non trovato!");
				return false;
			}

			// Disattiva risposte precedenti
			rispostaDAO.disattivaRispostePrecedenti(idOfferta);

			// Crea e inserisci nuova risposta
			final RispostaOfferta risposta = new RispostaOfferta(idOfferta, idAgente, agente.getNome(),
					agente.getCognome(),
					tipoRisposta, importoControproposta);

			final boolean successo = rispostaDAO.inserisciRispostaOfferta(risposta);

			// Se l'inserimento è riuscito, aggiorna lo stato dell'offerta
			if (successo) {
				final OffertaInizialeDAO offertaDAO = new OffertaInizialeDAO(connAWS);
				offertaDAO.aggiornaStatoOfferta(idOfferta, tipoRisposta);
			}

			return successo;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}