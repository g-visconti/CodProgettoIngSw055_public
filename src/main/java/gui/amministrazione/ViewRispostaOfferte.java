package gui.amministrazione;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import controller.OfferteController;
import model.entity.OffertaIniziale;
import util.GuiUtils;

/**
 * Finestra per la gestione delle risposte alle offerte immobiliari.
 * Questa interfaccia permette all'agente immobiliare di rispondere alle offerte
 * dei clienti con tre diverse opzioni: accettazione, rifiuto o controproposta.
 *
 * <p>La finestra si adatta dinamicamente in base allo stato dell'offerta:
 * <ul>
 *   <li>Per offerte "In attesa": mostra tutte e tre le opzioni (Accetta, Rifiuta, Controproposta)
 *   <li>Per offerte già valutate: mostra solo l'opzione di controproposta per nuove negoziazioni
 * </ul>
 *
 * @author IngSW2425_055 Team
 * @see OfferteController
 * @see OffertaIniziale
 */
public class ViewRispostaOfferte extends JFrame {
	private static final long serialVersionUID = 1L;
	private final JPanel contentPane;
	private final JTextField txtControproposta;

	/**
	 * Costruttore della finestra di risposta alle offerte.
	 * Inizializza l'interfaccia grafica e configura i componenti in base allo stato dell'offerta.
	 *
	 * <p>La finestra mostra:
	 * <ul>
	 *   <li>Bottoni per accettare o rifiutare l'offerta (solo se in stato "In attesa")
	 *   <li>Campo di testo per inserire una controproposta
	 *   <li>Validazione dell'importo della controproposta
	 * </ul>
	 *
	 * @param idOfferta ID univoco dell'offerta a cui rispondere
	 * @param idAgente ID dell'agente immobiliare che sta rispondendo all'offerta
	 *
	 * @throws IllegalArgumentException Se l'offerta non esiste o non è recuperabile
	 *
	 * @see OfferteController#getOffertaById(long)
	 * @see OfferteController#inserisciRispostaOfferta(long, String, String, Double)
	 */
	public ViewRispostaOfferte(long idOfferta, String idAgente) {
		// Imposta l'icona di DietiEstates25 alla finestra in uso
		GuiUtils.setIconaFinestra(this);
		setTitle("DietiEstates25 - Rispondi alla proposta del cliente");

		setType(Type.POPUP);
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 595, 297);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(240, 240, 240));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		final JLabel lblDescrizione = new JLabel("Scegliere un'opzione sull'offerta proposta");
		lblDescrizione.setHorizontalAlignment(SwingConstants.CENTER);
		lblDescrizione.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblDescrizione.setBounds(47, 21, 483, 24);
		contentPane.add(lblDescrizione);

		txtControproposta = new JTextField();
		txtControproposta.setBounds(189, 188, 207, 24);
		contentPane.add(txtControproposta);
		txtControproposta.setColumns(10);

		final JLabel lblProposta = new JLabel("Controproposta: €");
		lblProposta.setHorizontalAlignment(SwingConstants.RIGHT);
		lblProposta.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblProposta.setBounds(47, 191, 132, 19);
		contentPane.add(lblProposta);

		final JLabel lblOppureProponiUna = new JLabel("oppure proporre una nuova offerta");
		lblOppureProponiUna.setHorizontalAlignment(SwingConstants.CENTER);
		lblOppureProponiUna.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblOppureProponiUna.setBounds(51, 146, 483, 24);
		contentPane.add(lblOppureProponiUna);

		final JButton btnAccetta = new JButton("Accetta");
		btnAccetta.setForeground(Color.WHITE);
		btnAccetta.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 11));
		btnAccetta.setBackground(Color.GREEN);

		final JButton btnRifiuta = new JButton("Rifiuta");
		btnRifiuta.setForeground(Color.WHITE);
		btnRifiuta.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 11));
		btnRifiuta.setBackground(Color.RED);

		final JButton btnControproposta = new JButton("Controproposta");
		getRootPane().setDefaultButton(btnControproposta);
		btnControproposta.setForeground(new Color(255, 255, 255));
		btnControproposta.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 11));
		btnControproposta.setBackground(SystemColor.textHighlight);

		final OfferteController controller = new OfferteController();
		final OffertaIniziale offerta = controller.getOffertaById(idOfferta);

		boolean isOffertaGiàValutata = false;
		if (offerta != null) {
			final String stato = offerta.getStato();
			isOffertaGiàValutata = stato != null && !stato.trim().equalsIgnoreCase("In attesa");
		}

		// aggiorna la gui in base allo stato
		if (isOffertaGiàValutata) {
			// Offerta già valutata: mostra solo la controproposta
			lblDescrizione.setText("Fai una nuova proposta al cliente");
			lblOppureProponiUna.setText("Inserisci il nuovo importo proposto");

			// Nascondi i bottoni Accetta e Rifiuta
			btnAccetta.setVisible(false);
			btnRifiuta.setVisible(false);

			// Sposta su la sezione della controproposta
			lblOppureProponiUna.setBounds(51, 72, 483, 24); // Più in alto
			lblProposta.setBounds(47, 111, 132, 19);
			txtControproposta.setBounds(189, 108, 207, 24);
			btnControproposta.setBounds(427, 108, 131, 23);

			// Cambia testo del bottone
			btnControproposta.setText("Invia proposta");
		}

		// L'agente accetta la proposta del cliente
		btnAccetta.addMouseListener(new MouseAdapter() {
			/**
			 * Gestisce il click sul pulsante "Accetta".
			 * Mostra una finestra di conferma e, in caso positivo,
			 * invia la risposta di accettazione al sistema.
			 *
			 * <p>Dopo l'operazione:
			 * <ul>
			 *   <li>Mostra un messaggio di successo o errore
			 *   <li>Chiude la finestra in caso di successo
			 * </ul>
			 *
			 * @param e L'evento del mouse che ha triggerato l'azione
			 *
			 * @see OfferteController#inserisciRispostaOfferta(long, String, String, Double)
			 */
			@Override
			public void mouseClicked(MouseEvent e) {
				final int conferma = JOptionPane.showConfirmDialog(null,
						"Sei sicuro di voler accettare questa offerta?", "Conferma accettazione",
						JOptionPane.YES_NO_OPTION);

				if (conferma == JOptionPane.YES_OPTION) {
					final OfferteController controller = new OfferteController();
					final boolean successo = controller.inserisciRispostaOfferta(idOfferta, idAgente, "Accettata",
							null);

					if (successo) {
						JOptionPane.showMessageDialog(null, "Offerta accettata con successo!");
						dispose();
					} else {
						JOptionPane.showMessageDialog(null, "Errore durante l'accettazione dell'offerta.");
					}
				}
			}
		});

		btnAccetta.setBounds(147, 72, 131, 23);
		contentPane.add(btnAccetta);

		// L'agente rifiuta l'offerta proposta dal cliente
		btnRifiuta.addMouseListener(new MouseAdapter() {
			/**
			 * Gestisce il click sul pulsante "Rifiuta".
			 * Mostra una finestra di conferma e, in caso positivo,
			 * invia la risposta di rifiuto al sistema.
			 *
			 * <p>Dopo l'operazione:
			 * <ul>
			 *   <li>Mostra un messaggio di successo o errore
			 *   <li>Chiude la finestra in caso di successo
			 * </ul>
			 *
			 * @param e L'evento del mouse che ha triggerato l'azione
			 *
			 * @see OfferteController#inserisciRispostaOfferta(long, String, String, Double)
			 */
			@Override
			public void mouseClicked(MouseEvent e) {
				final int conferma = JOptionPane.showConfirmDialog(null,
						"Sei sicuro di voler rifiutare questa offerta?", "Conferma rifiuto", JOptionPane.YES_NO_OPTION);

				if (conferma == JOptionPane.YES_OPTION) {
					final OfferteController controller = new OfferteController();
					final boolean successo = controller.inserisciRispostaOfferta(idOfferta, idAgente, "Rifiutata",
							null);

					if (successo) {
						JOptionPane.showMessageDialog(null, "Offerta rifiutata con successo!");
						dispose();
					} else {
						JOptionPane.showMessageDialog(null, "Errore durante il rifiuto dell'offerta.");
					}
				}
			}
		});

		btnRifiuta.setBounds(314, 72, 131, 23);
		contentPane.add(btnRifiuta);

		// Controproposta dell'agente
		btnControproposta.addMouseListener(new MouseAdapter() {
			/**
			 * Gestisce il click sul pulsante "Controproposta".
			 * Valida l'importo inserito e, se valido, invia una controproposta al sistema.
			 *
			 * <p>Il metodo esegue le seguenti operazioni:
			 * <ol>
			 *   <li>Converte il testo in numero double
			 *   <li>Recupera l'importo dell'offerta iniziale
			 *   <li>Valida che la controproposta sia positiva e superiore all'offerta iniziale
			 *   <li>Invia la controproposta al sistema
			 * </ol>
			 *
			 * @param e L'evento del mouse che ha triggerato l'azione
			 *
			 * @throws NumberFormatException Se il valore inserito non è un numero valido
			 *
			 * @see OfferteController#isValidControproposta(double, double)
			 * @see OfferteController#inserisciRispostaOfferta(long, String, String, Double)
			 */
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					double controproposta = Double.parseDouble(txtControproposta.getText());

					OffertaIniziale offerta = controller.getOffertaById(idOfferta);
					double offertaIniziale = offerta.getImportoProposto();

					OfferteController controller = new OfferteController();
					if (!controller.isValidControproposta(controproposta, offertaIniziale)) {
						JOptionPane.showMessageDialog(null,
								"La controproposta deve essere positiva e superiore all'offerta iniziale.");
						return;
					}

					boolean successo = controller.inserisciRispostaOfferta(
							idOfferta,
							idAgente,
							"Controproposta",
							controproposta
							);

					if (successo) {
						JOptionPane.showMessageDialog(null, "Controproposta inviata con successo!");
						dispose();
					} else {
						JOptionPane.showMessageDialog(null, "Errore durante l'invio della controproposta.");
					}

				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "Inserisci un valore numerico valido per la controproposta.");
				}
			}
		});

		btnControproposta.setBounds(427, 188, 131, 23);
		contentPane.add(btnControproposta);
	}
}