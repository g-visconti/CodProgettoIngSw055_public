package util;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

/**
 * Classe di utilità per operazioni comuni sulle tabelle Swing (JTable).
 * Fornisce metodi statici per la manipolazione delle colonne, formattazione
 * di dati e configurazione dell'aspetto delle tabelle.
 *
 * <p>Questa classe è progettata come utility class e non può essere istanziata.
 * Le funzionalità principali includono:
 * <ul>
 *   <li>Gestione della visibilità e dimensione delle colonne</li>
 *   <li>Formattazione di prezzi in formato italiano</li>
 *   <li>Formattazione di date secondo lo standard italiano</li>
 * </ul>
 *
 * @author IngSW2425_055 Team
 * @see JTable
 * @see TableColumn
 * @see NumberFormat
 */
public class TableUtils {

	/**
	 * Costruttore privato per prevenire l'istanziazione.
	 * Questa è una utility class con solo metodi statici.
	 *
	 * @throws IllegalStateException Se si tenta di istanziare la classe
	 */
	private TableUtils() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * Nasconde completamente una colonna della tabella impostando tutte le sue dimensioni a zero.
	 * Utile per colonne che contengono dati tecnici (es: ID) che non devono essere visualizzati.
	 *
	 * @param tabella La tabella contenente la colonna da nascondere
	 * @param indice L'indice della colonna da nascondere (0-based)
	 * @throws IllegalArgumentException Se l'indice è fuori dai limiti della tabella
	 * @throws NullPointerException Se la tabella è null
	 * @see TableColumn
	 */
	public static void nascondiColonna(JTable tabella, int indice) {
		final TableColumn colonna = tabella.getColumnModel().getColumn(indice);
		colonna.setMinWidth(0);
		colonna.setMaxWidth(0);
		colonna.setWidth(0);
		colonna.setPreferredWidth(0);
	}

	/**
	 * Fissa una colonna della tabella a una larghezza specifica, rendendola non ridimensionabile.
	 * Utile per colonne con contenuto di dimensione fissa (es: icone, flag).
	 *
	 * @param tabella La tabella contenente la colonna da fissare
	 * @param indice L'indice della colonna da fissare (0-based)
	 * @param larghezza La larghezza fissa in pixel per la colonna
	 * @throws IllegalArgumentException Se l'indice è fuori dai limiti della tabella o larghezza ≤ 0
	 * @throws NullPointerException Se la tabella è null
	 * @see TableColumn#setResizable(boolean)
	 */
	public static void fissaColonna(JTable tabella, int indice, int larghezza) {
		final TableColumn colonna = tabella.getColumnModel().getColumn(indice);
		colonna.setPreferredWidth(larghezza);
		colonna.setMinWidth(larghezza);
		colonna.setMaxWidth(larghezza);
		colonna.setResizable(false);
	}

	/**
	 * Imposta la larghezza preferita di una colonna della tabella.
	 * La colonna rimane ridimensionabile dall'utente.
	 *
	 * @param tabella La tabella contenente la colonna da configurare
	 * @param indice L'indice della colonna (0-based)
	 * @param larghezza La larghezza preferita in pixel per la colonna
	 * @throws IllegalArgumentException Se l'indice è fuori dai limiti della tabella o larghezza ≤ 0
	 * @throws NullPointerException Se la tabella è null
	 * @see TableColumn#setPreferredWidth(int)
	 */
	public static void larghezzaColonna(JTable tabella, int indice, int larghezza) {
		final TableColumn colonna = tabella.getColumnModel().getColumn(indice);
		colonna.setPreferredWidth(larghezza);
	}

	/**
	 * Formatta un importo numerico come prezzo in formato italiano.
	 * Utilizza il simbolo dell'euro (€) e formattazione con separatori di migliaia.
	 *
	 * <p>Esempio:
	 * <pre>
	 * Input: 1500
	 * Output: "€ 1.500"
	 * </pre>
	 *
	 * @param importo Il valore numerico da formattare (Integer, Double, etc.)
	 * @return La stringa formattata come prezzo, oppure "€ 0" se l'importo non è numerico
	 * @see NumberFormat
	 * @see Locale#ITALY
	 */
	public static String formattaPrezzo(Object importo) {
		final NumberFormat format = NumberFormat.getNumberInstance(Locale.ITALY);
		format.setGroupingUsed(true);
		format.setMaximumFractionDigits(0);
		format.setMinimumFractionDigits(0);

		if (importo instanceof Number) {
			return "€ " + format.format(importo);
		}
		return "€ 0";
	}

	/**
	 * Formatta una data LocalDateTime nel formato italiano "gg/mm/aaaa HH:mm".
	 *
	 * <p>Esempio:
	 * <pre>
	 * Input: LocalDateTime.of(2023, 12, 25, 14, 30)
	 * Output: "25/12/2023 14:30"
	 * </pre>
	 *
	 * @param data La data da formattare
	 * @return La stringa formattata, oppure stringa vuota se la data è null
	 * @see LocalDateTime
	 * @see DateTimeFormatter
	 */
	public static String formattaData(LocalDateTime data) {
		if (data == null) {
			return "";
		} else {
			final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
			return data.format(formatter);
		}
	}

}