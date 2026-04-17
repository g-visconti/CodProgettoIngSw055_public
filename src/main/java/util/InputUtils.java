package util;

import javax.swing.JComboBox;

/**
 * Classe di utilità per la validazione e conversione di input utente.
 * Fornisce metodi statici per gestire conversioni di tipi, parsing di numeri
 * e formattazione di stringhe provenienti dall'interfaccia utente.
 *
 * <p>Questa classe è progettata come utility class e non può essere istanziata.
 * Le funzionalità principali includono:
 * <ul>
 *   <li>Conversione di stringhe in valori numerici interi</li>
 *   <li>Gestione di valori selezionati da JComboBox</li>
 *   <li>Formattazione di stringhe con capitalizzazione delle parole</li>
 * </ul>
 *
 * @author IngSW2425_055 Team
 * @see JComboBox
 */
public class InputUtils {

	/**
	 * Costruttore privato per prevenire l'istanziazione.
	 * Questa è una utility class con solo metodi statici.
	 *
	 * @throws IllegalStateException Se si tenta di istanziare la classe
	 */
	private InputUtils() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * Converte una stringa di testo in un valore intero.
	 * Gestisce valori vuoti o non numerici restituendo null.
	 *
	 * @param text La stringa da convertire in intero
	 * @return L'intero corrispondente, oppure null se la stringa è vuota o non numerica
	 * @throws NumberFormatException Se la stringa contiene caratteri non numerici (gestita internamente)
	 * @see Integer#parseInt(String)
	 */
	public static Integer parseInteger(String text) {
		try {
			return text.isEmpty() ? null : Integer.parseInt(text.trim());
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/**
	 * Converte il valore selezionato in una JComboBox in un valore intero.
	 * Gestisce valori speciali come "Indifferente", stringhe vuote o null.
	 *
	 * @param combo La JComboBox da cui estrarre il valore selezionato
	 * @return L'intero corrispondente, oppure null se il valore è "Indifferente", vuoto o non numerico
	 * @throws NullPointerException Se la combo è null
	 * @see JComboBox#getSelectedItem()
	 */
	public static Integer parseComboInteger(JComboBox<String> combo) {
		final String selected = (String) combo.getSelectedItem();
		if (selected == null || selected.equalsIgnoreCase("Indifferente") || selected.isEmpty()) {
			return null;
		}
		try {
			return parseInteger(selected);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/**
	 * Capitalizza la prima lettera di ogni parola in una stringa.
	 * Le parole sono separate da uno o più spazi bianchi.
	 *
	 * <p>Esempio:
	 * <pre>
	 * Input: "casa bella vista"
	 * Output: "Casa Bella Vista"
	 * </pre>
	 *
	 * @param input La stringa da capitalizzare
	 * @return La stringa con ogni parola capitalizzata, oppure stringa vuota se l'input è null
	 * @see StringBuilder
	 * @see Character#toUpperCase(char)
	 */
	public static String capitalizzaParole(String input) {
		if (input == null) {
			return "";
		}

		// Converto l'input tutto in lettere minuscole
		// divido in stringhe rimuovendo una o più occorenze degli spazi bianchi
		final String[] parole = input.toLowerCase().split("\\s+");
		// Alternativa per costruire stringhe come concatenazione di parole
		final StringBuilder risultato = new StringBuilder();

		for (String parola : parole) {
			if (!parola.isEmpty()) {
				risultato.append(Character.toUpperCase(parola.charAt(0))).append(parola.substring(1)).append(" ");
			}
		}

		// Prima di ritornare al chiamante
		// Converto l'oggetto risultato (che è una seq. di caratteri) StringBuilder
		// in una stringa, infine rimuovo eventuali spazi bianchi ad inizio e fine stringa
		return risultato.toString().trim();
	}
}