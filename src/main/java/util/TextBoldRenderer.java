package util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

/**
 * Renderer personalizzato per celle di tabella che visualizza testo in grassetto
 * con opzioni di allineamento e colore personalizzati.
 * Estende {@link ColorazioneAlternataRenderer} per mantenere la colorazione alternata
 * delle righe mentre applica formattazioni speciali al testo.
 *
 * <p>Caratteristiche principali:
 * <ul>
 *   <li>Testo in grassetto con dimensione del font personalizzabile</li>
 *   <li>Opzione per allineamento centrato o a sinistra</li>
 *   <li>Colore del testo personalizzabile</li>
 *   <li>Mantenimento della colorazione alternata delle righe</li>
 * </ul>
 *
 * @author IngSW2425_055 Team
 * @see ColorazioneAlternataRenderer
 * @see JTable
 * @see Font
 */
@SuppressWarnings("serial")
public class TextBoldRenderer extends ColorazioneAlternataRenderer {

	private final boolean center;
	private final Color textColor;

	/**
	 * Costruttore che inizializza il renderer con opzioni di allineamento e colore.
	 *
	 * @param center True per allineare il testo al centro, false per allineare a sinistra
	 * @param textColor Il colore del testo da applicare (null per utilizzare il colore di default)
	 * @see SwingConstants
	 */
	public TextBoldRenderer(boolean center, Color textColor) {
		this.center = center;
		this.textColor = textColor;
		setOpaque(true);
	}

	/**
	 * Restituisce il componente utilizzato per disegnare la cella della tabella.
	 * Applica formattazione in grassetto, allineamento e colore personalizzato al testo.
	 *
	 * @param table La tabella a cui appartiene la cella
	 * @param value Il valore della cella da visualizzare
	 * @param isSelected True se la cella è selezionata
	 * @param hasFocus True se la cella ha il focus
	 * @param row L'indice della riga (0-based)
	 * @param column L'indice della colonna (0-based)
	 * @return Il componente JLabel configurato con la formattazione specificata
	 *
	 * @throws NullPointerException Se la tabella è null
	 * @see ColorazioneAlternataRenderer#getTableCellRendererComponent(JTable, Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		final JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
				column);

		// Imposta font in grassetto con dimensione 14
		label.setFont(label.getFont().deriveFont(Font.BOLD, 14f));

		// Configura allineamento
		if (center) {
			label.setHorizontalAlignment(SwingConstants.CENTER);
		} else {
			label.setHorizontalAlignment(SwingConstants.LEFT);
		}

		// Applica colore testo personalizzato (solo se non selezionato)
		if (!isSelected && textColor != null) {
			label.setForeground(textColor);
		}

		return label;
	}
}