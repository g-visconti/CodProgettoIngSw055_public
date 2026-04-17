package dao;

import java.awt.Component;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

import org.json.JSONArray;
import org.json.JSONObject;
import org.postgresql.util.PGobject;

import model.dto.RicercaDTO;
import model.dto.StoricoAgenteDTO;
import model.dto.StoricoClienteDTO;
import model.entity.Filtri;
import model.entity.Immobile;
import model.entity.ImmobileInAffitto;
import model.entity.ImmobileInVendita;

/**
 * Data Access Object per la gestione degli immobili nel sistema.
 * Fornisce metodi per interagire con le tabelle "Immobile", "ImmobileInAffitto"
 * e "ImmobileInVendita" del database, gestendo tutte le operazioni CRUD.
 *
 * <p>Questa classe gestisce:
 * <ul>
 *   <li>Caricamento di nuovi immobili in affitto e vendita</li>
 *   <li>Recupero degli immobili con filtri avanzati</li>
 *   <li>Gestione dello storico offerte per clienti e agenti</li>
 *   <li>Recupero di immobili specifici per ID</li>
 *   <li>Conversione di immagini in formato Base64 per il database</li>
 * </ul>
 *
 * <p>Nota: Utilizza JSONB per memorizzare filtri e immagini nel database PostgreSQL,
 * permettendo una struttura flessibile e query avanzate sui campi JSON.
 *
 * @author IngSW2425_055 Team
 * @see Immobile
 * @see ImmobileInAffitto
 * @see ImmobileInVendita
 * @see Connection
 */
public class ImmobileDAO {
	private Connection connection;

	/**
	 * Costruttore per ImmobileDAO.
	 * Inizializza il DAO con una connessione al database.
	 *
	 * @param connection Connessione al database da utilizzare per tutte le operazioni
	 * @throws IllegalArgumentException Se la connessione è null
	 */
	public ImmobileDAO(Connection connection) {
		this.connection = connection;
	}

	/**
	 * Carica un immobile in affitto nel database.
	 *
	 * <p>Questo metodo inserisce prima i dati generali dell'immobile nella tabella "Immobile",
	 * poi i dati specifici per l'affitto nella tabella "ImmobileInAffitto".
	 * Le immagini vengono convertite in Base64 e memorizzate come JSONB.
	 *
	 * @param immobile Oggetto ImmobileInAffitto contenente tutti i dati dell'immobile
	 * @throws SQLException Se si verifica un errore durante l'inserimento nel database
	 * @throws IllegalArgumentException Se l'immobile è null o contiene dati non validi
	 * @see ImmobileInAffitto
	 */
	public void caricaImmobileInAffitto(ImmobileInAffitto immobile) throws SQLException {
		final String query1 = "INSERT INTO \"Immobile\" (titolo, indirizzo, localita, dimensione, descrizione, tipologia, filtri, immagini, \"agenteAssociato\") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING \"idImmobile\"";

		try (PreparedStatement stmt = connection.prepareStatement(query1)) {
			stmt.setString(1, immobile.getTitolo());
			stmt.setString(2, immobile.getIndirizzo());
			stmt.setString(3, immobile.getLocalita());
			stmt.setInt(4, immobile.getDimensione());
			stmt.setString(5, immobile.getDescrizione());
			stmt.setString(6, immobile.getTipologia());

			// Filtri JSON
			final PGobject jsonObject = new PGobject();
			jsonObject.setType("jsonb");
			jsonObject.setValue(immobile.getFiltriAsJson().toString());
			stmt.setObject(7, jsonObject);

			// Immagini in Base64 come JSONB
			final List<String> immaginiBase64 = new ArrayList<>();
			if (immobile.getImmagini() != null) {
				immobile.getImmagini().forEach(img -> immaginiBase64.add(Base64.getEncoder().encodeToString(img)));
			}
			final PGobject immaginiJson = new PGobject();
			immaginiJson.setType("jsonb");
			immaginiJson.setValue(new org.json.JSONArray(immaginiBase64).toString());
			stmt.setObject(8, immaginiJson);

			// agenteAssociato
			stmt.setString(9, immobile.getAgenteAssociato());

			// Esegui inserimento e recupera id generato
			final ResultSet rs = stmt.executeQuery();
			int generatedId = -1;
			if (rs.next()) {
				generatedId = rs.getInt("idImmobile");
			}
			rs.close();
			// Inserimento specifico per affitto
			final String query2 = "INSERT INTO \"ImmobileInAffitto\" (\"idImmobile\", \"prezzoMensile\") VALUES (?, ?)";
			try (PreparedStatement stmt2 = connection.prepareStatement(query2)) {
				stmt2.setInt(1, generatedId);
				stmt2.setDouble(2, immobile.getPrezzoMensile());
				stmt2.executeUpdate();
			}
		}
	}

	/**
	 * Carica un immobile in vendita nel database.
	 *
	 * <p>Questo metodo inserisce prima i dati generali dell'immobile nella tabella "Immobile",
	 * poi i dati specifici per la vendita nella tabella "ImmobileInVendita".
	 * Le immagini vengono convertite in Base64 e memorizzate come JSONB.
	 *
	 * @param immobile Oggetto ImmobileInVendita contenente tutti i dati dell'immobile
	 * @throws SQLException Se si verifica un errore durante l'inserimento nel database
	 * @throws IllegalArgumentException Se l'immobile è null o contiene dati non validi
	 * @see ImmobileInVendita
	 */
	public void caricaImmobileInVendita(ImmobileInVendita immobile) throws SQLException {
		final String query1 = "INSERT INTO \"Immobile\" (titolo, indirizzo, localita, dimensione, descrizione, tipologia, filtri, immagini, \"agenteAssociato\") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING \"idImmobile\"";

		try (PreparedStatement stmt = connection.prepareStatement(query1)) {
			stmt.setString(1, immobile.getTitolo());
			stmt.setString(2, immobile.getIndirizzo());
			stmt.setString(3, immobile.getLocalita());
			stmt.setInt(4, immobile.getDimensione());
			stmt.setString(5, immobile.getDescrizione());
			stmt.setString(6, immobile.getTipologia());

			// Filtri JSONB
			final PGobject jsonObject = new PGobject();
			jsonObject.setType("jsonb");
			jsonObject.setValue(immobile.getFiltriAsJson().toString());
			stmt.setObject(7, jsonObject);

			// Immagini in Base64 come JSONB
			final List<String> immaginiBase64 = new ArrayList<>();
			if (immobile.getImmagini() != null) {
				immobile.getImmagini().forEach(img -> immaginiBase64.add(Base64.getEncoder().encodeToString(img)));
			}
			final PGobject immaginiJson = new PGobject();
			immaginiJson.setType("jsonb");
			immaginiJson.setValue(new org.json.JSONArray(immaginiBase64).toString());
			stmt.setObject(8, immaginiJson);

			// agenteAssociato
			stmt.setString(9, immobile.getAgenteAssociato());

			// Esegui inserimento e recupera id generato
			final ResultSet rs = stmt.executeQuery();
			int generatedId = -1;
			if (rs.next()) {
				generatedId = rs.getInt("idImmobile");
			}
			rs.close();
			// Inserimento specifico per vendita
			final String query2 = "INSERT INTO \"ImmobileInVendita\" (\"idImmobile\", \"prezzoTotale\") VALUES (?, ?)";
			try (PreparedStatement stmt2 = connection.prepareStatement(query2)) {
				stmt2.setInt(1, generatedId);
				stmt2.setDouble(2, immobile.getPrezzoTotale());
				stmt2.executeUpdate();
			}
		}
	}

	/**
	 * Recupera le offerte proposte da un cliente specifico.
	 *
	 * <p>Questo metodo recupera tutte le offerte iniziali fatte da un cliente,
	 * incluse le informazioni sull'immobile associato e lo stato dell'offerta.
	 * Utile per visualizzare lo storico delle offerte del cliente.
	 *
	 * @param emailUtente Email del cliente di cui recuperare le offerte
	 * @return Lista di StoricoClienteDTO contenenti le informazioni sulle offerte
	 * @throws RuntimeException Se si verifica un errore durante l'accesso al database
	 * @see StoricoClienteDTO
	 */
	public List<StoricoClienteDTO> getDatiOfferteProposte(String emailUtente) {
		final String query = "SELECT oi.\"idOfferta\" as \"idOfferta\", i.\"immagini\" as \"Foto\", i.\"tipologia\" as \"Categoria\", i.\"descrizione\" as \"Descrizione\", "
				+ "oi.\"dataOfferta\" as \"Data\", oi.\"importoProposto\" as \"Prezzo proposto\", oi.\"stato\" as \"stato\" "
				+ "FROM \"Immobile\" i "
				+ "INNER JOIN \"OffertaIniziale\" oi ON i.\"idImmobile\" = oi.\"immobileAssociato\" "
				+ "INNER JOIN \"Account\" a ON oi.\"clienteAssociato\" = a.\"idAccount\" " + "WHERE a.\"email\" = ?";

		final List<StoricoClienteDTO> risultati = new ArrayList<>();

		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, emailUtente);
			final ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				// Crea un nuovo DTO per ogni riga
				final StoricoClienteDTO dto = new StoricoClienteDTO();

				// ID Offerta
				dto.setIdOfferta(rs.getLong("idOfferta"));

				// Estrai la prima immagine dal JSON
				final String immaginiJson = rs.getString("Foto");
				String primaImmagineBase64 = "";

				if (immaginiJson != null && !immaginiJson.isBlank()) {
					try {
						final JSONArray array = new JSONArray(immaginiJson);
						if (array.length() > 0) {
							primaImmagineBase64 = array.getString(0);
						}
					} catch (Exception e) {
						// Log errore ma continua
						System.err.println("Errore parsing JSON immagini: " + e.getMessage());
					}
				}
				dto.setPrimaImmagineBase64(primaImmagineBase64);

				// Altri campi
				dto.setCategoria(rs.getString("Categoria"));
				dto.setDescrizione(rs.getString("Descrizione"));

				// Data
				final java.sql.Timestamp timestamp = rs.getTimestamp("Data");
				dto.setDataOfferta(timestamp != null ? timestamp.toLocalDateTime() : null);

				// Prezzo proposto
				final BigDecimal importoProposto = rs.getBigDecimal("Prezzo proposto");
				dto.setImportoProposto(importoProposto != null ? importoProposto : BigDecimal.ZERO);

				// Stato
				dto.setStato(rs.getString("Stato"));

				risultati.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore nel recupero storico offerte per: " + emailUtente, e);
		}

		return risultati;
	}

	/**
	 * Recupera le offerte ricevute da un agente immobiliare.
	 *
	 * <p>Questo metodo recupera tutte le offerte iniziali fatte sui immobili
	 * gestiti da un agente specifico. Utile per permettere all'agente di
	 * valutare e gestire le offerte ricevute.
	 *
	 * @param emailAgente Email dell'agente di cui recuperare le offerte ricevute
	 * @return Lista di StoricoAgenteDTO contenenti le informazioni sulle offerte
	 * @throws SQLException Se si verifica un errore durante l'accesso al database
	 * @see StoricoAgenteDTO
	 */
	public List<StoricoAgenteDTO> getDatiOfferteRicevuteAgente(String emailAgente) throws SQLException {
		final List<StoricoAgenteDTO> offerte = new ArrayList<>();

		final String query = "SELECT " + "    oi.\"idOfferta\" as \"idOfferta\", "
				+ "    i.\"immagini\"->>0 as \"primaImmagine\", " + "    i.\"tipologia\" as \"categoria\", "
				+ "    i.\"descrizione\" as \"descrizione\", " + "    oi.\"dataOfferta\" as \"dataOfferta\", "
				+ "    oi.\"importoProposto\" as \"importoProposto\", " + "    CASE "
				+ "        WHEN oi.\"stato\" = 'In attesa' THEN 'In attesa' " + "        ELSE 'Valutato' "
				+ "    END as \"stato\" " + "FROM \"Immobile\" i "
				+ "INNER JOIN \"OffertaIniziale\" oi ON i.\"idImmobile\" = oi.\"immobileAssociato\" "
				+ "INNER JOIN \"Account\" a ON i.\"agenteAssociato\" = a.\"idAccount\" " + "WHERE a.\"email\" = ? "
				+ "ORDER BY oi.\"dataOfferta\" DESC";

		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, emailAgente);

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					final StoricoAgenteDTO offerta = new StoricoAgenteDTO(rs.getLong("idOfferta"),
							rs.getString("primaImmagine"), // Base64
							rs.getString("categoria"), rs.getString("descrizione"),
							rs.getTimestamp("dataOfferta").toLocalDateTime(), rs.getBigDecimal("importoProposto"),
							rs.getString("stato"));
					offerte.add(offerta);
				}
			}
		}

		return offerte;
	}

	/**
	 * Recupera un immobile completo dal database basandosi sul suo ID.
	 *
	 * <p>Questo metodo recupera tutti i dati dell'immobile, inclusi filtri,
	 * immagini e prezzo specifico (mensile per affitto, totale per vendita).
	 * Le immagini vengono decodificate da Base64 a byte array.
	 *
	 * @param idimmobile ID dell'immobile da recuperare
	 * @return Oggetto Immobile (o sottoclasse appropriata) con tutti i dati,
	 *         null se l'immobile non viene trovato
	 * @throws SQLException Se si verifica un errore durante l'accesso al database
	 * @see Immobile
	 * @see ImmobileInAffitto
	 * @see ImmobileInVendita
	 */
	public Immobile getImmobileById(long idimmobile) throws SQLException {
		final String sqlBase = "SELECT * FROM \"Immobile\" WHERE \"idImmobile\" = ?";
		Immobile immobile = null;

		try (PreparedStatement stmt = connection.prepareStatement(sqlBase)) {
			stmt.setLong(1, idimmobile);
			final ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				final String tipo = rs.getString("tipologia"); // tipo: "Affitto" o "Vendita"

				if ("Affitto".equalsIgnoreCase(tipo)) {
					immobile = new ImmobileInAffitto();
				} else if ("Vendita".equalsIgnoreCase(tipo)) {
					immobile = new ImmobileInVendita();
				} else {
					immobile = new Immobile(); // fallback
				}

				immobile.setId(rs.getLong("idImmobile"));
				immobile.setTitolo(rs.getString("titolo"));
				immobile.setIndirizzo(rs.getString("indirizzo"));
				immobile.setDimensione(rs.getInt("dimensione"));
				immobile.setDescrizione(rs.getString("descrizione"));
				immobile.setLocalita(rs.getString("localita"));
				immobile.setTipologia(rs.getString("tipologia"));
				immobile.setAgenteAssociato(rs.getString("agenteAssociato"));
				final String filtriJsonString = rs.getString("filtri");
				if (filtriJsonString != null && !filtriJsonString.isEmpty()) {
					final JSONObject filtri = new JSONObject(filtriJsonString);
					immobile.setFiltriFromJson(filtri);
				}

				// Recupero immagini da JSONB (array di stringhe Base64)
				final String immaginiJson = rs.getString("immagini");
				if (immaginiJson != null && !immaginiJson.isEmpty()) {
					final JSONArray jsonArray = new JSONArray(immaginiJson);
					final List<byte[]> immaginiBytes = new ArrayList<>();
					for (int i = 0; i < jsonArray.length(); i++) {
						final String base64img = jsonArray.getString(i);
						final byte[] bytes = Base64.getDecoder().decode(base64img);
						immaginiBytes.add(bytes);
					}
					immobile.setImmagini(immaginiBytes);
				} else {
					immobile.setImmagini(new ArrayList<>());
				}

				// Recupera prezzo specifico
				if (immobile instanceof ImmobileInAffitto) {
					final String sqlPrezzoAffitto = "SELECT \"prezzoMensile\" FROM \"ImmobileInAffitto\" WHERE \"idImmobile\" = ?";
					try (PreparedStatement psPrezzo = connection.prepareStatement(sqlPrezzoAffitto)) {
						psPrezzo.setLong(1, idimmobile);
						final ResultSet rsPrezzo = psPrezzo.executeQuery();
						if (rsPrezzo.next()) {
							((ImmobileInAffitto) immobile).setPrezzoMensile(rsPrezzo.getInt("prezzoMensile"));
						}
					}
				} else if (immobile instanceof ImmobileInVendita) {
					final String sqlPrezzoVendita = "SELECT \"prezzoTotale\" FROM \"ImmobileInVendita\" WHERE \"idImmobile\" = ?";
					try (PreparedStatement psPrezzo = connection.prepareStatement(sqlPrezzoVendita)) {
						psPrezzo.setLong(1, idimmobile);
						final ResultSet rsPrezzo = psPrezzo.executeQuery();
						if (rsPrezzo.next()) {
							((ImmobileInVendita) immobile).setPrezzoTotale(rsPrezzo.getInt("prezzoTotale"));
						}
					}
				}
			}
		}
		return immobile;
	}

	/**
	 * Costruisce la query base per la ricerca di immobili.
	 *
	 * <p>Metodo privato che costruisce la parte iniziale della query SQL
	 * basandosi sulla tipologia (Affitto/Vendita) e sul tipo di ricerca.
	 *
	 * @param tipologia Tipologia dell'immobile ("Affitto" o "Vendita")
	 * @param ricercaDTO DTO contenente i criteri di ricerca
	 * @return Stringa SQL con la query base
	 * @see RicercaDTO
	 */
	private String costruisciQueryBase(String tipologia, RicercaDTO ricercaDTO) {
		String query = "";

		if ("Affitto".equals(tipologia)) {
			query = "SELECT i.\"idImmobile\", i.\"immagini\" \"Immagini\", i.\"titolo\" \"Tipologia\", "
					+ "i.\"descrizione\" \"Descrizione\", a.\"prezzoMensile\" \"Prezzo Mensile\" "
					+ "FROM \"Immobile\" i JOIN \"ImmobileInAffitto\" a " + "ON i.\"idImmobile\" = a.\"idImmobile\" "
					+ "WHERE (i.\"titolo\" ILIKE '%' || ? || '%' OR " + "i.\"localita\" ILIKE '%' || ? || '%' OR "
					+ "i.\"descrizione\" ILIKE '%' || ? || '%') " + "AND i.\"tipologia\" = ? ";
		} else {
			query = "SELECT i.\"idImmobile\", i.\"immagini\" \"Immagini\", i.\"titolo\" \"Tipologia\", "
					+ "i.\"descrizione\" \"Descrizione\", v.\"prezzoTotale\" \"Prezzo Totale\" "
					+ "FROM \"Immobile\" i JOIN \"ImmobileInVendita\" v " + "ON i.\"idImmobile\" = v.\"idImmobile\" "
					+ "WHERE (i.\"titolo\" ILIKE '%' || ? || '%' OR " + "i.\"localita\" ILIKE '%' || ? || '%' OR "
					+ "i.\"descrizione\" ILIKE '%' || ? || '%') " + "AND i.\"tipologia\" = ? ";
		}

		if (ricercaDTO.getTipoRicerca() == RicercaDTO.TipoRicerca.I_MIEI_IMMOBILI && ricercaDTO.getEmailUtente() != null
				&& !ricercaDTO.getEmailUtente().isEmpty()) {
			query += " AND i.\"agenteAssociato\" = (SELECT \"idAccount\" FROM \"Account\" WHERE \"email\" = ?) ";
		}

		return query;
	}

	/**
	 * Aggiunge i filtri alla query SQL esistente.
	 *
	 * <p>Metodo privato che estende una query SQL aggiungendo le condizioni
	 * di filtro basate sull'oggetto Filtri fornito.
	 *
	 * @param query Query SQL base a cui aggiungere i filtri
	 * @param filtri Oggetto contenente i criteri di filtro
	 * @param tipologia Tipologia dell'immobile ("Affitto" o "Vendita")
	 * @return Query SQL estesa con le condizioni di filtro
	 * @see Filtri
	 */
	private String aggiungiFiltriQuery(String query, Filtri filtri, String tipologia) {
		if (filtri == null) {
			return query;
		}

		if (filtri.prezzoMin != null) {
			if ("Affitto".equals(tipologia)) {
				query += " AND a.\"prezzoMensile\" >= ?";
			} else {
				query += " AND v.\"prezzoTotale\" >= ?";
			}
		}
		if (filtri.prezzoMax != null) {
			if ("Affitto".equals(tipologia)) {
				query += " AND a.\"prezzoMensile\" <= ?";
			} else {
				query += " AND v.\"prezzoTotale\" <= ?";
			}
		}
		if (filtri.superficieMin != null) {
			query += " AND i.\"dimensione\" >= ?";
		}
		if (filtri.superficieMax != null) {
			query += " AND i.\"dimensione\" <= ?";
		}
		if (filtri.piano != null && !filtri.piano.equals("Indifferente")) {
			query += " AND (i.\"filtri\"->>'piano')::int = ?";
		}
		if (filtri.numLocali != null) {
			query += " AND (i.\"filtri\"->>'numeroLocali')::int = ?";
		}
		if (filtri.numBagni != null) {
			query += " AND (i.\"filtri\"->>'numeroBagni')::int = ?";
		}
		if (filtri.ascensore != null) {
			query += " AND (i.\"filtri\"->>'ascensore')::boolean = ?";
		}
		if (filtri.portineria != null) {
			query += " AND (i.\"filtri\"->>'portineria')::boolean = ?";
		}
		if (filtri.climatizzazione != null) {
			query += " AND (i.\"filtri\"->>'climatizzazione')::boolean = ?";
		}

		return query;
	}

	/**
	 * Aggiunge i parametri dei filtri allo PreparedStatement.
	 *
	 * <p>Metodo privato che imposta i valori dei parametri per i filtri
	 * nello PreparedStatement, gestendo tutti i tipi di filtro supportati.
	 *
	 * @param ps PreparedStatement a cui aggiungere i parametri
	 * @param filtri Oggetto contenente i valori dei filtri
	 * @param index Indice corrente per l'impostazione dei parametri
	 * @return Nuovo indice dopo aver aggiunto tutti i parametri
	 * @throws SQLException Se si verifica un errore durante l'impostazione dei parametri
	 * @see Filtri
	 */
	private int aggiungiParametriFiltri(PreparedStatement ps, Filtri filtri, int index) throws SQLException {
		if (filtri == null) {
			return index;
		}

		if (filtri.prezzoMin != null) {
			ps.setInt(index, filtri.prezzoMin);
			index++;
		}
		if (filtri.prezzoMax != null) {
			ps.setInt(index, filtri.prezzoMax);
			index++;
		}
		if (filtri.superficieMin != null) {
			ps.setInt(index, filtri.superficieMin);
			index++;
		}
		if (filtri.superficieMax != null) {
			ps.setInt(index, filtri.superficieMax);
			index++;
		}
		if (filtri.piano != null && !filtri.piano.equals("Indifferente")) {
			ps.setInt(index, Integer.parseInt(filtri.piano));
			index++;
		}
		if (filtri.numLocali != null) {
			ps.setInt(index, filtri.numLocali);
			index++;
		}
		if (filtri.numBagni != null) {
			ps.setInt(index, filtri.numBagni);
			index++;
		}
		if (filtri.ascensore != null) {
			ps.setBoolean(index, filtri.ascensore);
			index++;
		}
		if (filtri.portineria != null) {
			ps.setBoolean(index, filtri.portineria);
			index++;
		}
		if (filtri.climatizzazione != null) {
			ps.setBoolean(index, filtri.climatizzazione);
			index++;
		}

		return index;
	}

	/**
	 * Recupera gli immobili in affitto in base ai criteri di ricerca specificati.
	 *
	 * <p>Questo metodo esegue una query complessa con filtri avanzati per trovare
	 * immobili in affitto che corrispondono ai criteri specificati nel RicercaDTO.
	 * Le immagini vengono decodificate da Base64 a byte array.
	 *
	 * @param ricercaDTO DTO contenente tutti i criteri di ricerca e filtri
	 * @return Lista di ImmobileInAffitto che soddisfano i criteri di ricerca,
	 *         lista vuota se nessun immobile viene trovato o in caso di errore
	 * @see ImmobileInAffitto
	 * @see RicercaDTO
	 */
	public List<ImmobileInAffitto> getImmobiliAffitto(RicercaDTO ricercaDTO) {
		final List<ImmobileInAffitto> immobili = new ArrayList<>();

		String query = costruisciQueryBase("Affitto", ricercaDTO);
		query = aggiungiFiltriQuery(query, ricercaDTO.getFiltri(), "Affitto");
		query += " ORDER BY i.\"idImmobile\" DESC ";

		try (PreparedStatement ps = connection.prepareStatement(query)) {
			int index = 1;

			// Parametri di ricerca base
			ps.setString(index, ricercaDTO.getQueryRicerca());
			index++;
			ps.setString(index, ricercaDTO.getQueryRicerca());
			index++;
			ps.setString(index, ricercaDTO.getQueryRicerca());
			index++;
			ps.setString(index, ricercaDTO.getTipologiaImmobile());
			index++;

			// Parametro per filtro agente
			if (ricercaDTO.getTipoRicerca() == RicercaDTO.TipoRicerca.I_MIEI_IMMOBILI
					&& ricercaDTO.getEmailUtente() != null && !ricercaDTO.getEmailUtente().isEmpty()) {
				ps.setString(index, ricercaDTO.getEmailUtente());
				index++;
			}

			index = aggiungiParametriFiltri(ps, ricercaDTO.getFiltri(), index);

			final ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				final ImmobileInAffitto imm = new ImmobileInAffitto();
				imm.setId(rs.getInt(1));

				final String immaginiJson = rs.getString(2);
				if (immaginiJson != null && !immaginiJson.isEmpty()) {
					final JSONArray jsonArray = new JSONArray(immaginiJson);
					final List<byte[]> immagini = new ArrayList<>();
					for (int i = 0; i < jsonArray.length(); i++) {
						final String base64img = jsonArray.getString(i);
						final byte[] imgBytes = Base64.getDecoder().decode(base64img);
						immagini.add(imgBytes);
					}
					imm.setImmagini(immagini);
					if (!immagini.isEmpty()) {
						imm.setIcon(jsonArray.getString(0));
					}
				}

				imm.setTitolo(rs.getString(3));
				imm.setDescrizione(rs.getString(4));
				imm.setPrezzoMensile(rs.getInt(5));
				immobili.add(imm);
			}

			return immobili;

		} catch (SQLException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	/**
	 * Recupera gli immobili in vendita in base ai criteri di ricerca specificati.
	 *
	 * <p>Questo metodo esegue una query complessa con filtri avanzati per trovare
	 * immobili in vendita che corrispondono ai criteri specificati nel RicercaDTO.
	 * Le immagini vengono decodificate da Base64 a byte array.
	 *
	 * @param ricercaDTO DTO contenente tutti i criteri di ricerca e filtri
	 * @return Lista di ImmobileInVendita che soddisfano i criteri di ricerca,
	 *         lista vuota se nessun immobile viene trovato o in caso di errore
	 * @see ImmobileInVendita
	 * @see RicercaDTO
	 */
	public List<ImmobileInVendita> getImmobiliVendita(RicercaDTO ricercaDTO) {
		final List<ImmobileInVendita> immobili = new ArrayList<>();

		String query = costruisciQueryBase("Vendita", ricercaDTO);
		query = aggiungiFiltriQuery(query, ricercaDTO.getFiltri(), "Vendita");
		query += " ORDER BY i.\"idImmobile\" DESC ";

		try (PreparedStatement ps = connection.prepareStatement(query)) {
			int index = 1;

			// Parametri di ricerca base
			ps.setString(index, ricercaDTO.getQueryRicerca());
			index++;
			ps.setString(index, ricercaDTO.getQueryRicerca());
			index++;
			ps.setString(index, ricercaDTO.getQueryRicerca());
			index++;
			ps.setString(index, ricercaDTO.getTipologiaImmobile());
			index++;

			// Parametro per filtro agente
			if (ricercaDTO.getTipoRicerca() == RicercaDTO.TipoRicerca.I_MIEI_IMMOBILI
					&& ricercaDTO.getEmailUtente() != null && !ricercaDTO.getEmailUtente().isEmpty()) {
				ps.setString(index, ricercaDTO.getEmailUtente());
				index++;
			}

			index = aggiungiParametriFiltri(ps, ricercaDTO.getFiltri(), index);

			final ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				final ImmobileInVendita imm = new ImmobileInVendita();
				imm.setId(rs.getInt(1));

				final String immaginiJson = rs.getString(2);
				if (immaginiJson != null && !immaginiJson.isEmpty()) {
					final JSONArray jsonArray = new JSONArray(immaginiJson);
					final List<byte[]> immagini = new ArrayList<>();
					for (int i = 0; i < jsonArray.length(); i++) {
						final String base64img = jsonArray.getString(i);
						final byte[] imgBytes = Base64.getDecoder().decode(base64img);
						immagini.add(imgBytes);
					}
					imm.setImmagini(immagini);
					if (!immagini.isEmpty()) {
						imm.setIcon(jsonArray.getString(0));
					}
				}

				imm.setTitolo(rs.getString(3));
				imm.setDescrizione(rs.getString(4));
				imm.setPrezzoTotale(rs.getInt(5));
				immobili.add(imm);
			}

			return immobili;

		} catch (SQLException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	/**
	 * Renderer personalizzato per celle di tabella che contengono testo multilinea.
	 *
	 * <p>Classe interna che estende JTextArea per fornire un renderer di celle
	 * che gestisca correttamente il testo multilinea con word wrapping.
	 * Utilizzata per visualizzare descrizioni lunghe nelle tabelle di immobili.
	 *
	 * @author IngSW2425_055 Team
	 * @see JTextArea
	 * @see TableCellRenderer
	 */
	@SuppressWarnings("serial")
	static class TextAreaRenderer extends JTextArea implements TableCellRenderer {

		/**
		 * Costruttore per TextAreaRenderer.
		 * Configura il componente per il wrapping di testo e parole.
		 */
		public TextAreaRenderer() {
			setLineWrap(true);
			setWrapStyleWord(true);
			setOpaque(true);
		}

		/**
		 * Restituisce il componente configurato per il rendering della cella.
		 *
		 * @param table Tabella a cui appartiene la cella
		 * @param value Valore della cella
		 * @param isSelected true se la cella è selezionata
		 * @param hasFocus true se la cella ha il focus
		 * @param row Indice della riga
		 * @param column Indice della colonna
		 * @return Componente configurato per il rendering
		 */
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			setText(value == null ? "" : value.toString());
			if (isSelected) {
				setBackground(table.getSelectionBackground());
				setForeground(table.getSelectionForeground());
			} else {
				setBackground(table.getBackground());
				setForeground(table.getForeground());
			}
			setFont(table.getFont());
			return this;
		}
	}
}