package util;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Renderer personalizzato per celle di tabella che applica una colorazione alternata alle righe.
 * Estende {@link DefaultTableCellRenderer} per fornire un'alternanza visiva tra righe pari e dispari,
 * migliorando la leggibilità dei dati in tabelle di grandi dimensioni.
 *
 * <p>Questa classe offre due costruttori:
 * <ul>
 *   <li>Un costruttore di default che utilizza colori predefiniti (bianco e grigio chiaro)</li>
 *   <li>Un costruttore che accetta colori personalizzati per le righe pari e dispari</li>
 * </ul>
 *
 * <p>La colorazione alternata viene applicata solo alle righe non selezionate.
 * Le righe selezionate mantengono il colore di selezione standard della tabella.
 *
 * @author IngSW2425_055 Team
 * @see DefaultTableCellRenderer
 * @see JTable
 */
@SuppressWarnings("serial")
public class ColorazioneAlternataRenderer extends DefaultTableCellRenderer {

	private final Color coloreRighePari;
	private final Color coloreRigheDispari;

	/**
	 * Costruttore che inizializza il renderer con colori predefiniti.
	 * Le righe pari saranno bianche, le righe dispari grigio chiaro.
	 *
	 * @see Color#WHITE
	 */
	public ColorazioneAlternataRenderer() {
		coloreRighePari = Color.WHITE;
		coloreRigheDispari = new Color(248, 248, 248); // Grigio
	}

	/**
	 * Costruttore che inizializza il renderer con colori personalizzati.
	 *
	 * @param coloreRighePari Il colore da applicare alle righe pari (indice 0, 2, 4, ...)
	 * @param coloreRigheDispari Il colore da applicare alle righe dispari (indice 1, 3, 5, ...)
	 * @throws NullPointerException Se uno dei colori forniti è null
	 */
	public ColorazioneAlternataRenderer(Color coloreRighePari, Color coloreRigheDispari) {
		this.coloreRighePari = coloreRighePari;
		this.coloreRigheDispari = coloreRigheDispari;
	}

	/**
	 * Restituisce il componente utilizzato per disegnare la cella della tabella.
	 * Applica la colorazione alternata alle righe non selezionate, mantenendo
	 * il colore di selezione standard per le righe selezionate.
	 *
	 * @param table La tabella a cui appartiene la cella
	 * @param value Il valore della cella
	 * @param isSelected True se la cella è selezionata
	 * @param hasFocus True se la cella ha il focus
	 * @param row L'indice della riga (0-based)
	 * @param column L'indice della colonna (0-based)
	 * @return Il componente configurato con la colorazione appropriata
	 *
	 * @see DefaultTableCellRenderer#getTableCellRendererComponent(JTable, Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		final Component componente = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
				column);

		// Applica colorazione alternata solo se la riga non è selezionata
		if (!isSelected) {
			if (row % 2 == 0) {
				componente.setBackground(coloreRighePari);
			} else {
				componente.setBackground(coloreRigheDispari);
			}
		} else {
			componente.setBackground(table.getSelectionBackground());
		}

		return componente;
	}
}