package util;

import java.awt.Image;
import java.awt.Window;
import java.net.URL;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 * Classe di utilità per operazioni grafiche comuni nell'interfaccia utente
 * Swing.
 */
public class GuiUtils {

	/**
	 * Carica un'immagine da un array di byte in un'etichetta JLabel.
	 */
	public static void caricaImmagineInEtichetta(JLabel etichetta, byte[] datiImmagine, int larghezza, int altezza) {
		if (datiImmagine != null && datiImmagine.length > 0) {
			try {
				final ImageIcon icona = new ImageIcon(datiImmagine);
				final Image img = icona.getImage().getScaledInstance(larghezza, altezza, Image.SCALE_SMOOTH);
				etichetta.setIcon(new ImageIcon(img));
				etichetta.setText("");
			} catch (Exception ex) {
				etichetta.setText("Immagine non valida");
				etichetta.setIcon(null);
				System.err.println("Errore caricamento immagine: " + ex.getMessage());
			}
		} else {
			etichetta.setText("Nessuna immagine");
			etichetta.setIcon(null);
		}
	}

	/**
	 * Configura una galleria di immagini su due etichette.
	 */
	public static void configuraGalleriaImmagini(JLabel etichettaPrincipale, JLabel etichettaContatore,
			List<byte[]> immagini, int indiceCorrente) {
		if (immagini != null && !immagini.isEmpty() && indiceCorrente >= 0 && indiceCorrente < immagini.size()) {
			final int larghezza = etichettaPrincipale.getWidth() > 0 ? etichettaPrincipale.getWidth() : 300;
			final int altezza = etichettaPrincipale.getHeight() > 0 ? etichettaPrincipale.getHeight() : 200;

			caricaImmagineInEtichetta(etichettaPrincipale, immagini.get(indiceCorrente), larghezza, altezza);

			if (immagini.size() > 1) {
				etichettaContatore.setText((indiceCorrente + 1) + "/" + immagini.size());
			} else {
				etichettaContatore.setText("");
			}
		} else {
			etichettaPrincipale.setText("Nessuna immagine disponibile");
			etichettaPrincipale.setIcon(null);
			etichettaContatore.setText("");
		}
	}

	/**
	 * Imposta un'icona su un pulsante JButton.
	 */
	public static void setIconaButton(JButton pulsante, String nomeIcona) {
		// usa getClass().getResource() con percorso assoluto
		final URL pathIcona = GuiUtils.class.getResource("/images/" + nomeIcona + ".png");

		if (pathIcona != null) {
			final ImageIcon iconaOriginale = new ImageIcon(pathIcona);
			final Image img = iconaOriginale.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
			pulsante.setIcon(new ImageIcon(img));
		} else {
			System.err.println("ERRORE: Icona button non trovata: /images/" + nomeIcona + ".png");
		}
	}

	/**
	 * Imposta l'icona dell'applicazione per una finestra.
	 */
	public static void setIconaFinestra(Window finestra) {
		// usa getClass().getResource() con percorso assoluto (inizia con /)
		final URL pathIcona = GuiUtils.class.getResource("/images/DietiEstatesIcona.png");
		if (pathIcona != null) {
			final ImageIcon icon = new ImageIcon(pathIcona);
			finestra.setIconImage(icon.getImage());
		} else {
			System.err.println("ERRORE: Icona finestra non trovata: /images/DietiEstatesIcona.png");
		}
	}

	/**
	 * Imposta un'icona su un'etichetta JLabel, ridimensionandola automaticamente.
	 */
	public static void setIconaLabel(JLabel etichetta, String nomeIcona) {
		// MODIFICATO: usa getClass().getResource() con percorso assoluto
		final URL pathIcona = GuiUtils.class.getResource("/images/" + nomeIcona + ".png");

		if (pathIcona != null) {
			final ImageIcon icon = new ImageIcon(pathIcona);
			// ... resto del codice invariato ...
			if (etichetta.getWidth() <= 0 || etichetta.getHeight() <= 0) {
				etichetta.setIcon(icon);

				etichetta.addComponentListener(new java.awt.event.ComponentAdapter() {
					@Override
					public void componentResized(java.awt.event.ComponentEvent e) {
						if (etichetta.getWidth() > 0 && etichetta.getHeight() > 0) {
							final Image img = icon.getImage().getScaledInstance(etichetta.getWidth(),
									etichetta.getHeight(), Image.SCALE_SMOOTH);
							etichetta.setIcon(new ImageIcon(img));
							etichetta.setText("");
							etichetta.removeComponentListener(this);
						}
					}
				});
			} else {
				final Image img = icon.getImage().getScaledInstance(etichetta.getWidth(), etichetta.getHeight(),
						Image.SCALE_SMOOTH);
				etichetta.setIcon(new ImageIcon(img));
				etichetta.setText("");
			}
		} else {
			System.err.println("ERRORE: Icona label non trovata: /images/" + nomeIcona + ".png");
			etichetta.setText("Icona non trovata");
			etichetta.setIcon(null);
		}
	}

	private GuiUtils() {
		throw new IllegalStateException("Utility class");
	}
}