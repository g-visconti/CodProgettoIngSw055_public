package util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.util.Base64;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

/**
 * Renderer personalizzato per celle di tabella che contengono immagini in formato Base64.
 * Questa classe estende {@link ColorazioneAlternataRenderer} per mantenere l'alternanza
 * dei colori delle righe e fornisce la funzionalità di decodifica e visualizzazione
 * di immagini codificate in Base64.
 *
 * <p>Il renderer:
 * <ul>
 *   <li>Decodifica la stringa Base64 in un'immagine</li>
 *   <li>Ridimensiona l'immagine per adattarla alle dimensioni della cella</li>
 *   <li>Mantiene le proporzioni originali dell'immagine</li>
 *   <li>Mostra messaggi alternativi se l'immagine non è disponibile</li>
 * </ul>
 *
 * @author IngSW2425_055 Team
 * @see ColorazioneAlternataRenderer
 * @see JTable
 * @see Base64
 */
@SuppressWarnings("serial")
public class Base64ImageRenderer extends ColorazioneAlternataRenderer {

	/**
	 * Restituisce il componente utilizzato per disegnare la cella della tabella.
	 * Decodifica la stringa Base64, ridimensiona l'immagine e la visualizza nella cella.
	 * Se non è presente un'immagine valida, mostra un testo alternativo.
	 *
	 * @param table La tabella a cui appartiene la cella
	 * @param value Il valore della cella (stringa Base64 o null)
	 * @param isSelected True se la cella è selezionata
	 * @param hasFocus True se la cella ha il focus
	 * @param row L'indice della riga
	 * @param column L'indice della colonna
	 * @return Il componente JLabel configurato per visualizzare l'immagine o il testo alternativo
	 *
	 * @throws IllegalArgumentException Se il valore Base64 non è valido o non può essere decodificato
	 * @see Base64#getDecoder()
	 * @see ImageIcon
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		final JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
				column);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setVerticalAlignment(SwingConstants.CENTER);
		label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		try {
			if (value instanceof String base64 && !base64.trim().isEmpty()) {
				// Decodifica Base64
				final byte[] imageData = Base64.getDecoder().decode(base64);
				final ImageIcon originalIcon = new ImageIcon(imageData);

				// Calcola dimensioni disponibili
				final int availableWidth = table.getColumnModel().getColumn(column).getWidth() - 15;
				final int availableHeight = table.getRowHeight(row) - 15;

				// Ridimensiona mantenendo le proporzioni
				final Image originalImage = originalIcon.getImage();
				final double widthRatio = (double) availableWidth / originalIcon.getIconWidth();
				final double heightRatio = (double) availableHeight / originalIcon.getIconHeight();
				final double ratio = Math.min(widthRatio, heightRatio);

				final int newWidth = (int) (originalIcon.getIconWidth() * ratio);
				final int newHeight = (int) (originalIcon.getIconHeight() * ratio);

				final Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
				label.setIcon(new ImageIcon(scaledImage));
				label.setText("");
			} else {
				label.setIcon(null);
				label.setText("L'immobile non ha foto");
				label.setForeground(Color.GRAY);
			}
		} catch (Exception e) {
			label.setIcon(null);
			label.setText("x");
		}

		return label;
	}
}