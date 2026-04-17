package controller;

import java.awt.Color;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import dao.ImmobileDAO;
import database.ConnessioneDatabase;
import model.dto.RicercaDTO;
import model.entity.Immobile;
import model.entity.ImmobileInAffitto;
import model.entity.ImmobileInVendita;
import util.Base64ImageRenderer;
import util.TableUtils;
import util.TextAreaRenderer;
import util.TextBoldRenderer;

/**
 * Controller per la gestione degli immobili nel sistema.
 * Questa classe fornisce metodi per caricare, recuperare, visualizzare
 * e validare gli immobili, sia in vendita che in affitto.
 *
 * <p>La classe gestisce:
 * <ul>
 *   <li>Caricamento di nuovi immobili nel database
 *   <li>Recupero dettagli immobili tramite ID
 *   <li>Popolamento di tabelle con risultati di ricerca
 *   <li>Validazione dei dati immobili prima del caricamento
 * </ul>
 *
 * @author IngSW2425_055 Team
 * @see ImmobileDAO
 * @see Immobile
 * @see ImmobileInAffitto
 * @see ImmobileInVendita
 * @see RicercaDTO
 */
public class ImmobileController {

	/**
	 * Costruttore di default per l'ImmobileController.
	 */
	public ImmobileController() {
	}

	/**
	 * Carica un immobile nel database, distinguendo tra affitto e vendita.
	 *
	 * <p>Il metodo determina la tipologia dell'immobile e chiama il metodo
	 * appropriato del DAO per l'inserimento nel database corretto.
	 *
	 * @param imm L'immobile da caricare (deve essere di tipo ImmobileInAffitto o ImmobileInVendita)
	 * @return true se il caricamento ha successo, false in caso di errore
	 *
	 * @throws ClassCastException Se l'immobile non è di un tipo valido per la sua tipologia
	 * @throws IllegalArgumentException Se la tipologia dell'immobile non è supportata
	 * @throws SQLException Se si verifica un errore durante l'accesso al database
	 *
	 * @see ImmobileDAO#caricaImmobileInAffitto(ImmobileInAffitto)
	 * @see ImmobileDAO#caricaImmobileInVendita(ImmobileInVendita)
	 */
	public boolean caricaImmobile(Immobile imm) {
		try {
			final Connection connAWS = ConnessioneDatabase.getInstance().getConnection();
			final ImmobileDAO immobileDao = new ImmobileDAO(connAWS);

			final String tipo = imm.getTipologia().toLowerCase();

			switch (tipo) {
			case "affitto" -> immobileDao.caricaImmobileInAffitto((ImmobileInAffitto) imm);
			case "vendita" -> immobileDao.caricaImmobileInVendita((ImmobileInVendita) imm);
			default -> throw new IllegalArgumentException("Tipologia non valida: " + tipo);
			}

			return true;

		} catch (SQLException | IllegalArgumentException | ClassCastException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Recupera i dettagli completi di un immobile dal database tramite il suo ID.
	 *
	 * <p>Il metodo restituisce un oggetto Immobile contenente tutte le informazioni,
	 * incluse immagini e caratteristiche dell'immobile.
	 *
	 * @param idImmobile ID univoco dell'immobile da recuperare
	 * @return Oggetto Immobile con tutti i dettagli, o null se non trovato
	 *
	 * @throws SQLException Se si verifica un errore durante l'accesso al database
	 * @see ImmobileDAO#getImmobileById(long)
	 */
	public Immobile recuperaDettagli(long idImmobile) {
		try {
			final Connection connAWS = ConnessioneDatabase.getInstance().getConnection();
			final ImmobileDAO immobileDAO = new ImmobileDAO(connAWS);
			return immobileDAO.getImmobileById(idImmobile);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Popola una JTable con i risultati della ricerca immobili.
	 *
	 * <p>Il metodo esegue una ricerca in base ai criteri specificati nel DTO
	 * e popola la tabella con i risultati, applicando formattazione e renderer specifici.
	 *
	 * <p>La tabella include:
	 * <ul>
	 *   <li>Anteprima immagine (con renderer Base64)
	 *   <li>Titolo dell'annuncio (in grassetto)
	 *   <li>Descrizione (con TextArea per il wrapping)
	 *   <li>Prezzo (formattato in base alla tipologia)
	 * </ul>
	 *
	 * @param tableRisultati La JTable da popolare con i risultati
	 * @param ricercaDTO DTO contenente i criteri di ricerca
	 * @return Il numero di righe inserite nella tabella
	 *
	 * @throws SQLException Se si verifica un errore durante l'accesso al database
	 * @see ImmobileDAO#getImmobiliAffitto(RicercaDTO)
	 * @see ImmobileDAO#getImmobiliVendita(RicercaDTO)
	 * @see TableUtils
	 * @see Base64ImageRenderer
	 * @see TextBoldRenderer
	 * @see TextAreaRenderer
	 */
	public int riempiTableRisultati(JTable tableRisultati, RicercaDTO ricercaDTO) {
		try {
			final Connection connAWS = ConnessioneDatabase.getInstance().getConnection();
			final ImmobileDAO immobileDAO = new ImmobileDAO(connAWS);

			final String[] colonne = { "ID", "Immagini", "Titolo dell'annuncio", "Descrizione",
					"Vendita".equals(ricercaDTO.getTipologiaImmobile()) ? "Prezzo Totale" : "Prezzo Mensile" };

			final DefaultTableModel model = new DefaultTableModel(colonne, 0) {
				@Override
				public Class<?> getColumnClass(int columnIndex) {
					return switch (columnIndex) {
					case 0 -> Integer.class;
					case 1 -> String.class;
					case 2 -> String.class;
					case 3 -> String.class;
					case 4 -> String.class;
					default -> Object.class;
					};
				}

				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};

			if (ricercaDTO.getTipologiaImmobile().equals("Affitto")) {
				immobileDAO.getImmobiliAffitto(ricercaDTO).forEach(imm -> {
					final String base64 = imm.getIcon();
					model.addRow(new Object[] { imm.getId(), base64, imm.getTitolo(), imm.getDescrizione(),
							TableUtils.formattaPrezzo(imm.getPrezzoMensile()) });
				});
			} else if (ricercaDTO.getTipologiaImmobile().equals("Vendita")) {
				immobileDAO.getImmobiliVendita(ricercaDTO).forEach(imm -> {
					final String base64 = imm.getIcon();
					model.addRow(new Object[] { imm.getId(), base64, imm.getTitolo(), imm.getDescrizione(),
							TableUtils.formattaPrezzo(imm.getPrezzoTotale()) });
				});
			}

			tableRisultati.setModel(model);

			// Ordinamento colonne fissato
			tableRisultati.getTableHeader().setReorderingAllowed(false);

			// Altezza di ogni riga
			tableRisultati.setRowHeight(160);

			// Larghezza di ogni singola colonna
			TableUtils.nascondiColonna(tableRisultati, 0); // Nascondi ID dell'immobile
			TableUtils.fissaColonna(tableRisultati, 1, 180); // Immagine
			TableUtils.larghezzaColonna(tableRisultati, 2, 170); // Titolo
			TableUtils.larghezzaColonna(tableRisultati, 3, 450); // Descrizione
			TableUtils.fissaColonna(tableRisultati, 4, 100); // Prezzo

			// Renderer
			tableRisultati.getColumnModel().getColumn(1).setCellRenderer(new Base64ImageRenderer());
			tableRisultati.getColumnModel().getColumn(2)
			.setCellRenderer(new TextBoldRenderer(false, new Color(50, 133, 177)));
			tableRisultati.getColumnModel().getColumn(3).setCellRenderer(new TextAreaRenderer());
			tableRisultati.getColumnModel().getColumn(4)
			.setCellRenderer(new TextBoldRenderer(true, new Color(0, 0, 0)));

			return model.getRowCount();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * Valida i dati di un immobile prima del caricamento nel sistema.
	 *
	 * <p>Il metodo verifica che tutti i campi obbligatori siano presenti
	 * e contengano valori validi.
	 *
	 * <p>Vengono controllati:
	 * <ul>
	 *   <li>Titolo (non vuoto)
	 *   <li>Indirizzo (non vuoto)
	 *   <li>Località (non vuota)
	 *   <li>Descrizione (non vuota)
	 *   <li>Tipologia (valida, non "-")
	 *   <li>Dimensione (positiva)
	 *   <li>Prezzo (positivo)
	 *   <li>Piano (>= -1, dove -1 indica "non specificato")
	 * </ul>
	 *
	 * @param titolo Titolo dell'annuncio
	 * @param indirizzo Indirizzo dell'immobile
	 * @param localita Località dell'immobile
	 * @param descrizione Descrizione dettagliata
	 * @param tipologia Tipologia ("Affitto" o "Vendita")
	 * @param dimensione Dimensione in mq
	 * @param prezzo Prezzo (mensile per affitto, totale per vendita)
	 * @param piano Piano dell'immobile (-1 se non specificato)
	 *
	 * @throws IllegalArgumentException Se uno dei parametri non supera la validazione
	 */
	public void validaImmobile(
			String titolo,
			String indirizzo,
			String localita,
			String descrizione,
			String tipologia,
			int dimensione,
			int prezzo,
			int piano
			) {

		if (titolo == null || titolo.isBlank()) {
			throw new IllegalArgumentException("Titolo mancante");
		}

		if (indirizzo == null || indirizzo.isBlank()) {
			throw new IllegalArgumentException("Indirizzo mancante");
		}

		if (localita == null || localita.isBlank()) {
			throw new IllegalArgumentException("Località mancante");
		}

		if (descrizione == null || descrizione.isBlank()) {
			throw new IllegalArgumentException("Descrizione mancante");
		}

		if (tipologia == null || "-".equals(tipologia)) {
			throw new IllegalArgumentException("Tipologia non valida");
		}

		if (dimensione <= 0) {
			throw new IllegalArgumentException("Dimensione non valida");
		}

		if (prezzo <= 0) {
			throw new IllegalArgumentException("Prezzo non valido");
		}

		if (piano < -1) {
			throw new IllegalArgumentException("Piano non valido");
		}
	}
}