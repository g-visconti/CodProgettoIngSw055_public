package gui.amministrazione;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.net.URL;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import controller.ImmobileController;
import controller.OfferteController;
import model.entity.Immobile;
import model.entity.ImmobileInAffitto;
import model.entity.ImmobileInVendita;
import model.entity.OffertaIniziale;
import util.GuiUtils;

/**
 * Finestra per la visualizzazione dei dettagli di un'offerta proposta da un cliente.
 * Questa interfaccia permette all'agente immobiliare di visualizzare tutte le informazioni
 * relative a un'offerta ricevuta, inclusi i dettagli dell'immobile e la proposta economica.
 *
 * <p>La finestra include:
 * <ul>
 *   <li>Galleria immagini dell'immobile con navigazione</li>
 *   <li>Dettagli tecnici dell'immobile (dimensioni, locali, servizi)</li>
 *   <li>Descrizione completa dell'immobile</li>
 *   <li>Pannello con la proposta economica del cliente</li>
 *   <li>Integrazione con Google Maps per la localizzazione</li>
 *   <li>Pulsante per rispondere alla proposta o fare una controfferta</li>
 * </ul>
 *
 * @author IngSW2425_055 Team
 * @see OffertaIniziale
 * @see Immobile
 * @see ViewRispostaOfferte
 */
public class ViewOffertaProposta extends JFrame {
	private static final long serialVersionUID = 1L;
	// attributi
	private JPanel contentPane;
	private JTextArea descrizioneField;
	private List<byte[]> immagini;
	private int indiceFotoCorrente = 0;
	private OffertaIniziale offertaCorrente;
	private boolean isInAttesa;

	// costruttore

	/**
	 * Costruttore della finestra di visualizzazione offerta.
	 * Inizializza l'interfaccia con tutti i dettagli dell'offerta e dell'immobile associato.
	 *
	 * <p>La finestra viene popolata dinamicamente recuperando:
	 * <ul>
	 *   <li>Le informazioni dell'offerta dal database</li>
	 *   <li>I dettagli completi dell'immobile associato</li>
	 *   <li>Le immagini dell'immobile</li>
	 *   <li>Le informazioni del cliente che ha fatto la proposta</li>
	 * </ul>
	 *
	 * @param idOfferta ID dell'offerta da visualizzare
	 * @param emailAgente Email dell'agente che sta visualizzando l'offerta
	 *
	 * @throws IllegalArgumentException Se l'offerta non viene trovata nel database
	 * @see OfferteController#getOffertaById(long)
	 * @see ImmobileController#recuperaDettagli(long)
	 */
	public ViewOffertaProposta(long idOfferta, String emailAgente) {
		// Inizializza il controller per recuperare l'offerta
		OfferteController offerteController = new OfferteController();
		offertaCorrente = offerteController.getOffertaById(idOfferta);

		if (offertaCorrente == null) {
			JOptionPane.showMessageDialog(this,
					"Offerta non trovata!",
					"Errore",
					JOptionPane.ERROR_MESSAGE);
			dispose();
			return;
		}

		long idImmobile = offertaCorrente.getImmobileAssociato();

		setTitle("DietiEstates25 - Dettagli dell'immobile selezionato");

		// Imposta l'icona di DietiEstates25 alla finestra in uso
		GuiUtils.setIconaFinestra(this);

		// finestra grande standard (dimensione fissa)
		this.setSize(1159, 697);
		setResizable(false);

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		contentPane = new JPanel();

		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblFoto = new JLabel("New label");
		lblFoto.setOpaque(true);
		lblFoto.setBackground(Color.GRAY);
		lblFoto.setBounds(10, 11, 690, 289);
		contentPane.add(lblFoto);

		JLabel lblFotoPlus = new JLabel("+ x foto");
		lblFotoPlus.setForeground(new Color(255, 255, 255));
		lblFotoPlus.setHorizontalAlignment(SwingConstants.CENTER);
		lblFotoPlus.setBackground(new Color(30, 144, 255));
		lblFotoPlus.setOpaque(true);
		lblFotoPlus.setBounds(701, 11, 66, 289);
		contentPane.add(lblFotoPlus);

		URL pathDEimage = this.getClass().getClassLoader().getResource("images/immobiletest.png");
		lblFoto.setIcon(new ImageIcon(pathDEimage));

		JLabel lblTitolo = new JLabel("Tipo, Via XXXX, Quartiere, Città");
		lblTitolo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitolo.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 17));
		lblTitolo.setForeground(new Color(45, 45, 45));
		lblTitolo.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		lblTitolo.setBounds(10, 325, 723, 30);
		contentPane.add(lblTitolo);

		JLabel lblMaps = new JLabel("");
		lblMaps.setBackground(SystemColor.text);
		lblMaps.setOpaque(true);
		lblMaps.setBounds(854, 432, 277, 132);
		contentPane.add(lblMaps);

		// interazione con API Places
		lblMaps.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblMaps.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String testoCompleto = lblTitolo.getText();

				String[] parole = testoCompleto.split(" ", 2);
				String indirizzo = parole.length > 1 ? parole[1] : testoCompleto;

				String url = "https://www.google.com/maps/search/?api=1&query=" + indirizzo.replace(" ", "+");

				try {
					Desktop.getDesktop().browse(new URI(url));
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "Errore nell'apertura del browser");
				}


			}
		});


		URL pathDEimage1 = this.getClass().getClassLoader().getResource("images/mapslogo.png");
		lblMaps.setIcon(new ImageIcon(pathDEimage1));

		JLabel lblDescrizionePosizione = new JLabel("Controlla la posizione dell'immobile");
		lblDescrizionePosizione.setHorizontalAlignment(SwingConstants.CENTER);
		lblDescrizionePosizione.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblDescrizionePosizione.setBounds(854, 403, 277, 23);
		contentPane.add(lblDescrizionePosizione);

		JPanel panelProponiOfferta = new JPanel();
		panelProponiOfferta.setBackground(new Color(240, 248, 255));
		panelProponiOfferta.setBounds(854, 57, 277, 205);
		contentPane.add(panelProponiOfferta);
		panelProponiOfferta.setLayout(null);

		JButton btnRispondiCliente = new JButton("Rispondi al cliente");
		btnRispondiCliente.setFocusable(false);
		btnRispondiCliente.setForeground(Color.WHITE);

		// Recupera il nome del cliente che ha fatto l'offerta
		String nomeCliente = offerteController.getClienteByOffertaId(idOfferta);

		JLabel lblDescrizioneValoreProposta = new JLabel("Il cliente propone:");
		lblDescrizioneValoreProposta.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblDescrizioneValoreProposta.setHorizontalAlignment(SwingConstants.CENTER);
		lblDescrizioneValoreProposta.setBounds(49, 41, 179, 21);
		panelProponiOfferta.add(lblDescrizioneValoreProposta);

		// Modifica le label per mostrare le informazioni del cliente
		lblDescrizioneValoreProposta.setText("Proposta del cliente:");
		if (nomeCliente != null && !nomeCliente.isEmpty()) {
			lblDescrizioneValoreProposta.setText("Proposta di: " + nomeCliente);
		}

		JLabel lblValoreProposta = new JLabel("valore proposta");
		lblValoreProposta.setHorizontalAlignment(SwingConstants.CENTER);
		lblValoreProposta.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblValoreProposta.setBounds(40, 73, 196, 21);
		panelProponiOfferta.add(lblValoreProposta);

		if (offertaCorrente != null) {
			double importoProposto = offertaCorrente.getImportoProposto();
			String importoFormattato = String.format("€ %,.2f", importoProposto);
			lblValoreProposta.setText(importoFormattato);

			String stato = offertaCorrente.getStato();
			isInAttesa = false;

			if (stato != null) {
				stato = stato.trim();

				// Controlla vari modi in cui potrebbe essere scritto
				isInAttesa = stato.equalsIgnoreCase("In attesa") ||
						stato.equalsIgnoreCase("IN_ATTESA") ||
						stato.equalsIgnoreCase("IN ATTESA");
			}

			if (isInAttesa) {
				// OFFERTA IN ATTESA: bottone rosso per rispondere
				btnRispondiCliente.setText("Rispondi al cliente");
				btnRispondiCliente.setBackground(new Color(204, 0, 0));
				btnRispondiCliente.setEnabled(true);
				System.out.println("DEBUG - Bottone configurato: 'Rispondi al cliente' (abilitato)");
			} else {
				// OFFERTA GIÀ VALUTATA: bottone blu per nuova proposta
				btnRispondiCliente.setText("Proponi nuova offerta");
				btnRispondiCliente.setBackground(new Color(30, 144, 255));
				btnRispondiCliente.setEnabled(true);
				System.out.println("DEBUG - Bottone configurato: 'Proponi nuova offerta' (abilitato)");
			}
		}

		btnRispondiCliente.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ViewRispostaOfferte guiOfferta = new ViewRispostaOfferte(idOfferta, emailAgente);
				guiOfferta.setLocationRelativeTo(null);
				guiOfferta.setVisible(true);
			}
		});

		btnRispondiCliente.setBackground(new Color(204, 0, 0));
		btnRispondiCliente.setBounds(36, 131, 204, 37);
		panelProponiOfferta.add(btnRispondiCliente);

		JPanel panelDettagliImmobile = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				int arc = 20;
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				// Colore azzurro tenue (sfondo)
				Color bgColor = new Color(220, 240, 255);
				// Colore blu bordo
				Color borderColor = new Color(30, 144, 255);

				// Disegna lo sfondo arrotondato
				g2.setColor(bgColor);
				g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);

				// Disegna il bordo arrotondato
				g2.setColor(borderColor);
				g2.setStroke(new BasicStroke(2));
				g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
			}
		};

		panelDettagliImmobile.setBounds(10, 352, 578, 113);
		contentPane.add(panelDettagliImmobile);
		panelDettagliImmobile.setLayout(null);

		JLabel lblNumLocali = new JLabel("n. locali:");
		lblNumLocali.setHorizontalAlignment(SwingConstants.LEFT);
		lblNumLocali.setBounds(156, 11, 126, 30);
		panelDettagliImmobile.add(lblNumLocali);
		lblNumLocali.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblNumLocali.setForeground(new Color(45, 45, 45)); // grigio scuro, più soft del nero
		lblNumLocali.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

		JLabel lblDimensione = new JLabel("dimensione:");
		lblDimensione.setHorizontalTextPosition(SwingConstants.LEFT);
		lblDimensione.setHorizontalAlignment(SwingConstants.LEFT);
		lblDimensione.setBounds(0, 11, 168, 30);
		panelDettagliImmobile.add(lblDimensione);
		lblDimensione.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblDimensione.setForeground(new Color(45, 45, 45)); // grigio scuro, più soft del nero
		lblDimensione.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

		JLabel lblNumPiano = new JLabel("piano:");
		lblNumPiano.setBounds(310, 11, 150, 30);
		panelDettagliImmobile.add(lblNumPiano);
		lblNumPiano.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblNumPiano.setForeground(new Color(45, 45, 45)); // grigio scuro, più soft del nero
		lblNumPiano.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

		JLabel lblNumBagni = new JLabel("n. bagni:");
		lblNumBagni.setBounds(455, 11, 123, 30);
		panelDettagliImmobile.add(lblNumBagni);
		lblNumBagni.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblNumBagni.setForeground(SystemColor.inactiveCaptionText); // grigio scuro, più soft del nero
		lblNumBagni.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

		JLabel lblAscensore = new JLabel("ascensore:");
		lblAscensore.setBounds(53, 65, 150, 30);
		panelDettagliImmobile.add(lblAscensore);
		lblAscensore.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblAscensore.setForeground(new Color(45, 45, 45)); // grigio scuro, più soft del nero
		lblAscensore.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

		JLabel lblPortineria = new JLabel("portineria:");
		lblPortineria.setBounds(229, 65, 144, 30);
		panelDettagliImmobile.add(lblPortineria);
		lblPortineria.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblPortineria.setForeground(new Color(45, 45, 45)); // grigio scuro, più soft del nero
		lblPortineria.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

		JLabel lblRiscaldamento = new JLabel("riscaldamento:");
		lblRiscaldamento.setBounds(384, 65, 189, 30);
		panelDettagliImmobile.add(lblRiscaldamento);
		lblRiscaldamento.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblRiscaldamento.setForeground(new Color(45, 45, 45)); // grigio scuro, più soft del nero
		lblRiscaldamento.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

		JLabel lblDescrizioneImmobile = new JLabel("Descrizione");
		lblDescrizioneImmobile.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblDescrizioneImmobile.setBounds(10, 482, 101, 23);
		contentPane.add(lblDescrizioneImmobile);

		JPanel panelDescrizione = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				int arc = 20; // raggio per gli angoli arrotondati
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				// Colore azzurro tenue (sfondo)
				Color bgColor = new Color(220, 240, 255);
				// Colore blu bordo
				Color borderColor = new Color(30, 144, 255);

				// Disegna lo sfondo arrotondato
				g2.setColor(bgColor);
				g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);

				// Disegna il bordo arrotondato
				g2.setColor(borderColor);
				g2.setStroke(new BasicStroke(2));
				g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
			}
		};

		panelDescrizione.setBounds(10, 505, 757, 141);
		panelDescrizione.setLayout(null);
		contentPane.add(panelDescrizione);

		descrizioneField = new JTextArea();
		descrizioneField.setBounds(5, 5, 747, 131);
		panelDescrizione.add(descrizioneField);

		descrizioneField.setLineWrap(true);
		descrizioneField.setWrapStyleWord(true);
		descrizioneField.setFont(new Font("Arial", Font.PLAIN, 16));
		descrizioneField.setEditable(false);
		descrizioneField.setEnabled(false);
		descrizioneField.setDisabledTextColor(Color.BLACK);
		descrizioneField.setOpaque(false); // Rende trasparente
		descrizioneField.setBackground(new Color(0, 0, 0, 0));
		descrizioneField.setBorder(BorderFactory.createEmptyBorder());

		JPanel panelPrezzoImmobile = new JPanel() {
			/**
			 * Serial version UID per garantire la compatibilità della serializzazione.
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Disegna un pannello con sfondo arrotondato e bordo colorato.
			 * Utilizzato per visualizzare il prezzo dell'immobile con un design distintivo.
			 *
			 * @param g Contesto grafico per il disegno
			 * @see Graphics2D
			 * @see RenderingHints#KEY_ANTIALIASING
			 */
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				int arc = 20; // raggio angoli arrotondati
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				// Colore interno
				Color bgColor = new Color(200, 60, 60);

				// Colore bordo
				Color borderColor = new Color(180, 0, 0);

				// Disegna sfondo arrotondato
				g2.setColor(bgColor);
				g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);

				// Disegna bordo arrotondato
				g2.setColor(borderColor);
				g2.setStroke(new BasicStroke(2));
				g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
			}
		};

		panelPrezzoImmobile.setBounds(598, 360, 169, 92);
		contentPane.add(panelPrezzoImmobile);
		panelPrezzoImmobile.setLayout(null);

		JLabel lblPrezzo = new JLabel("Prezzo");
		lblPrezzo.setHorizontalAlignment(SwingConstants.CENTER);
		lblPrezzo.setForeground(Color.WHITE);
		lblPrezzo.setBounds(10, 11, 149, 70);
		panelPrezzoImmobile.add(lblPrezzo);
		lblPrezzo.setFont(new Font("Segoe UI", Font.BOLD, 16));

		ImmobileController controllerImmobile = new ImmobileController();
		Immobile immobile = controllerImmobile.recuperaDettagli(idImmobile);

		if (immobile != null) {
			// Popola campi
			String titoloCompleto = immobile.getTitolo() + ", " + immobile.getIndirizzo() + ", "
					+ immobile.getLocalita();
			lblTitolo.setText(titoloCompleto);

			lblNumLocali.setText("n.locali:  " + immobile.getNumeroLocali());
			lblDimensione.setText("dimensione:  " + immobile.getDimensione() + " m²");
			lblNumBagni.setText("n.bagni:  " + immobile.getNumeroBagni());
			lblNumPiano.setText("piano:  " + immobile.getPiano());
			lblAscensore.setText("ascensore: " + (immobile.isAscensore() ? "Sì" : "No"));
			lblRiscaldamento.setText("riscaldamento: " + (immobile.getClimatizzazione() ? "Sì" : "No"));
			lblPortineria.setText("portineria: " + (immobile.isPortineria() ? "Sì" : "No"));
			descrizioneField.setText(immobile.getDescrizione());

			if (immobile instanceof ImmobileInAffitto) {
				double prezzoMensile = ((ImmobileInAffitto) immobile).getPrezzoMensile();
				String prezzoFormattato = String.format("%,.0f", prezzoMensile).replace(",", ".");
				lblPrezzo.setText("<html><div style='text-align: center;'>Prezzo Mensile: <br><span style='font-size:16px; font-weight:bold;'>€ " +
						prezzoFormattato + "</span></div></html>");
			} else if (immobile instanceof ImmobileInVendita) {
				double prezzoTotale = ((ImmobileInVendita) immobile).getPrezzoTotale();
				String prezzoFormattato = String.format("%,.0f", prezzoTotale).replace(",", ".");
				lblPrezzo.setText("<html><div style='text-align: center;'>Prezzo Totale: <br><span style='font-size:16px; font-weight:bold;'>€ " +
						prezzoFormattato + "</span></div></html>");
			} else {
				lblPrezzo.setText("<html><div style='text-align: center;'>Prezzo: Non disponibile</div></html>");
			}

			// Visualizza immagini
			immagini = immobile.getImmagini();
			indiceFotoCorrente = 0;

			GuiUtils.caricaImmagineInEtichetta(lblFoto,
					(immagini != null && !immagini.isEmpty()) ? immagini.get(0) : null,
							lblFoto.getWidth(),
							lblFoto.getHeight()
					);

			// Configura il contatore immagini
			if (immagini != null && immagini.size() > 1) {
				lblFotoPlus.setText("+" + (immagini.size() - 1) + " foto");
			} else {
				lblFotoPlus.setText("");
			}

		} else {
			lblTitolo.setText("Immobile non trovato");
			lblFoto.setText("Nessuna immagine disponibile");
			lblFotoPlus.setText("");
		}

		/**
		 * Listener per la navigazione tra le immagini dell'immobile.
		 * Permette all'utente di cliccare sull'indicatore "+x foto" per
		 * passare alla prossima immagine nella galleria.
		 *
		 * @see MouseAdapter
		 * @see GuiUtils#caricaImmagineInEtichetta(JLabel, byte[], int, int)
		 */
		lblFotoPlus.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (immagini != null && immagini.size() > 1) {
					// Passa alla prossima immagine
					indiceFotoCorrente = (indiceFotoCorrente + 1) % immagini.size();

					// Usa GuiUtils per caricare la nuova immagine
					GuiUtils.caricaImmagineInEtichetta(lblFoto,
							immagini.get(indiceFotoCorrente),
							lblFoto.getWidth(),
							lblFoto.getHeight()
							);

					// Aggiorna il contatore
					if (immagini.size() > 1) {
						lblFotoPlus.setText("+" + (immagini.size() - 1) + " foto");
					}
				}
			}
		});
	}
}