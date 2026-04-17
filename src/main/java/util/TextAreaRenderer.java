package util;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

/**
 * Renderer personalizzato per celle di tabella che visualizza testo multilinea.
 * Estende {@link JTextArea} per fornire supporto al wrapping automatico del testo
 * e alla visualizzazione di contenuti testuali lunghi in celle di tabella.
 *
 * <p>Caratteristiche principali:
 * <ul>
 *   <li>Supporto per testo multilinea con wrapping automatico</li>
 *   <li>Colorazione alternata delle righe (bianco/grigio chiaro)</li>
 *   <li>Bordi interni per una migliore leggibilità</li>
 *   <li>Adattamento dinamico alle dimensioni della cella</li>
 * </ul>
 *
 * @author IngSW2425_055 Team
 * @see JTextArea
 * @see TableCellRenderer
 * @see JTable
 */
@SuppressWarnings("serial")
public class TextAreaRenderer extends JTextArea implements TableCellRenderer {

	/**
	 * Costruttore che inizializza il renderer con le configurazioni di default.
	 * Configura il componente per il wrapping del testo, bordi e visualizzazione non editabile.
	 *
	 * @see JTextArea#setLineWrap(boolean)
	 * @see JTextArea#setWrapStyleWord(boolean)
	 */
	public TextAreaRenderer() {
		setLineWrap(true);
		setWrapStyleWord(true);
		setOpaque(true);
		setBorder(BorderFactory.createEmptyBorder(8, 5, 8, 5));
		setEditable(false);
	}

	/**
	 * Restituisce il componente utilizzato per disegnare la cella della tabella.
	 * Configura il testo, i colori e le dimensioni in base allo stato della cella.
	 *
	 * @param table La tabella a cui appartiene la cella
	 * @param value Il valore della cella da visualizzare
	 * @param isSelected True se la cella è selezionata
	 * @param hasFocus True se la cella ha il focus
	 * @param row L'indice della riga (0-based)
	 * @param column L'indice della colonna (0-based)
	 * @return Questo componente JTextArea configurato per la visualizzazione
	 *
	 * @throws NullPointerException Se la tabella è null
	 * @see TableCellRenderer#getTableCellRendererComponent(JTable, Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		setText(value == null ? "" : value.toString());

		if (!isSelected) {
			if (row % 2 == 0) {
				setBackground(Color.WHITE);
			} else {
				setBackground(new Color(248, 248, 248));
			}
		} else {
			setBackground(table.getSelectionBackground());
			setForeground(table.getSelectionForeground());
		}

		if (!isSelected) {
			setForeground(table.getForeground());
		}

		setFont(table.getFont());

		// Imposta dimensioni per il wrapping
		final int width = table.getColumnModel().getColumn(column).getWidth() - 10;
		setSize(width, table.getRowHeight());

		return this;
	}
}