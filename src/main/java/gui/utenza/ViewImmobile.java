package gui.utenza;

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

import controller.AccountController;
import controller.ImmobileController;
import model.entity.Account;
import model.entity.AgenteImmobiliare;
import model.entity.Immobile;
import model.entity.ImmobileInAffitto;
import model.entity.ImmobileInVendita;
import util.GuiUtils;

/**
 * Finestra per la visualizzazione dettagliata di un immobile.
 * Questa interfaccia mostra tutte le informazioni complete di un immobile,
 * incluse immagini, descrizione, caratteristiche tecniche e dati dell'agente associato.
 *
 * <p>La finestra è suddivisa in diverse aree:
 * <ul>
 *   <li>Visualizzatore immagini con navigazione tra foto multiple
 *   <li>Dettagli tecnici dell'immobile (dimensioni, locali, servizi)
 *   <li>Descrizione testuale completa
 *   <li>Pannello per proporre un'offerta o contattare l'agente
 *   <li>Integrazione con Google Maps per visualizzare la posizione
 *   <li>Informazioni di contatto dell'agente immobiliare
 * </ul>
 *
 * <p>Funzionalità interattive:
 * <ul>
 *   <li>Click sulle immagini per navigare tra le foto disponibili
 *   <li>Click sulla mappa per aprire la posizione in Google Maps
 *   <li>Pulsante per proporre un'offerta all'agente
 *   <li>Visualizzazione dinamica del prezzo in base alla tipologia (affitto/vendita)
 * </ul>
 *
 * @author IngSW2425_055 Team
 * @see ImmobileController#recuperaDettagli(long)
 * @see AccountController#recuperaDettagliAgente(String)
 * @see ViewOffertaIniziale
 * @see GuiUtils#caricaImmagineInEtichetta(JLabel, byte[], int, int)
 */
public class ViewImmobile extends JFrame {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPane;
	private final JTextArea descrizioneField;
	private List<byte[]> immagini;
	private int indiceFotoCorrente = 0;

	/**
	 * Costruttore della finestra di dettaglio immobile.
	 * Inizializza l'interfaccia grafica con tutte le informazioni dell'immobile
	 * specificato e configura i componenti interattivi.
	 *
	 * <p>La finestra recupera e visualizza:
	 * <ul>
	 *   <li>Immagini dell'immobile con navigazione
	 *   <li>Titolo e indirizzo completo
	 *   <li>Caratteristiche tecniche (dimensioni, locali, bagni, piano, servizi)
	 *   <li>Descrizione testuale
	 *   <li>Prezzo (mensile per affitti, totale per vendite)
	 *   <li>Dati dell'agente associato (nome, cognome, email, telefono, agenzia)
	 *   <li>Integrazione con Google Maps per la localizzazione
	 * </ul>
	 *
	 * @param idImmobile ID univoco dell'immobile da visualizzare
	 * @param idCliente ID del cliente che sta visualizzando l'immobile
	 *                  (utilizzato per la proposta di offerte)
	 *
	 * @throws IllegalArgumentException Se l'ID immobile non è valido o l'immobile non esiste
	 *
	 * @see ImmobileController#recuperaDettagli(long)
	 * @see AccountController#recuperaDettagliAgente(String)
	 */
	public ViewImmobile(long idImmobile, String idCliente) {
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

		final JLabel lblFoto = new JLabel("New label");
		lblFoto.setOpaque(true);
		lblFoto.setBackground(Color.GRAY);
		lblFoto.setBounds(10, 11, 690, 289);
		contentPane.add(lblFoto);

		final JLabel lblFotoPlus = new JLabel("+ x foto");
		lblFotoPlus.setForeground(new Color(255, 255, 255));
		lblFotoPlus.setHorizontalAlignment(SwingConstants.CENTER);
		lblFotoPlus.setBackground(new Color(30, 144, 255));
		lblFotoPlus.setOpaque(true);
		lblFotoPlus.setBounds(701, 11, 66, 289);
		contentPane.add(lblFotoPlus);

		final URL pathDEimage = this.getClass().getClassLoader().getResource("images/immobiletest.png");
		lblFoto.setIcon(new ImageIcon(pathDEimage));

		final JLabel lblTitolo = new JLabel("Tipo, Via XXXX, Quartiere, Città");
		lblTitolo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitolo.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 17));
		lblTitolo.setForeground(new Color(45, 45, 45));
		lblTitolo.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		lblTitolo.setBounds(10, 325, 723, 30);
		contentPane.add(lblTitolo);

		final JLabel lblMaps = new JLabel("");
		lblMaps.setBackground(SystemColor.text);
		lblMaps.setOpaque(true);
		lblMaps.setBounds(854, 482, 277, 132);
		contentPane.add(lblMaps);

		// API Places
		lblMaps.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblMaps.addMouseListener(new MouseAdapter() {
			/**
			 * Gestisce il click sulla mappa per aprire Google Maps.
			 * Estrae l'indirizzo dal titolo dell'immobile e apre il browser
			 * con la posizione corrispondente su Google Maps.
			 *
			 * @param e L'evento del mouse che ha triggerato l'apertura della mappa
			 *
			 * @throws Exception Se si verifica un errore durante l'apertura del browser
			 *
			 * @see Desktop#getDesktop()
			 * @see Desktop#browse(URI)
			 */
			@Override
			public void mouseClicked(MouseEvent e) {
				final String testoCompleto = lblTitolo.getText();

				final String[] parole = testoCompleto.split(" ", 2);
				final String indirizzo = parole.length > 1 ? parole[1] : testoCompleto;

				final String url = "https://www.google.com/maps/search/?api=1&query=" + indirizzo.replace(" ", "+");

				try {
					Desktop.getDesktop().browse(new URI(url));
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "Errore nell'apertura del browser");
				}

			}
		});

		final URL pathDEimage1 = this.getClass().getClassLoader().getResource("images/mapslogo.png");
		lblMaps.setIcon(new ImageIcon(pathDEimage1));

		final JLabel lblDescrizionePosizione = new JLabel("Controlla la posizione dell'immobile");
		lblDescrizionePosizione.setHorizontalAlignment(SwingConstants.CENTER);
		lblDescrizionePosizione.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblDescrizionePosizione.setBounds(854, 453, 277, 23);
		contentPane.add(lblDescrizionePosizione);

		final JPanel panelProponiOfferta = new JPanel();
		panelProponiOfferta.setBackground(new Color(240, 248, 255));
		panelProponiOfferta.setBounds(854, 11, 277, 422);
		contentPane.add(panelProponiOfferta);
		panelProponiOfferta.setLayout(null);

		final JButton btnProponiOfferta = new JButton("Proponi un'offerta");
		btnProponiOfferta.setFocusable(false);
		btnProponiOfferta.setForeground(Color.WHITE);
		btnProponiOfferta.addMouseListener(new MouseAdapter() {
			/**
			 * Gestisce il click sul pulsante "Proponi un'offerta".
			 * Apre la finestra per inviare un'offerta iniziale per l'immobile corrente.
			 *
			 * @param e L'evento del mouse che ha triggerato l'apertura della finestra offerta
			 *
			 * @see ViewOffertaIniziale
			 */
			@Override
			public void mouseClicked(MouseEvent e) {
				final ViewOffertaIniziale guiOfferta = new ViewOffertaIniziale(idImmobile, idCliente);
				guiOfferta.setLocationRelativeTo(null);
				guiOfferta.setVisible(true);
			}
		});
		btnProponiOfferta.setBackground(new Color(204, 0, 0));
		btnProponiOfferta.setBounds(36, 99, 204, 37);
		panelProponiOfferta.add(btnProponiOfferta);

		final JLabel lblAltreInfo = new JLabel("Oppure contatta l'agente:");
		lblAltreInfo.setHorizontalAlignment(SwingConstants.CENTER);
		lblAltreInfo.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblAltreInfo.setBounds(40, 194, 196, 21);
		panelProponiOfferta.add(lblAltreInfo);

		final JLabel lblNomeAgente = new JLabel("Nome");
		lblNomeAgente.setBounds(14, 258, 124, 21);
		panelProponiOfferta.add(lblNomeAgente);

		final JLabel lblEmailAgente = new JLabel("email");
		lblEmailAgente.setBounds(14, 319, 253, 21);
		panelProponiOfferta.add(lblEmailAgente);

		final JLabel lblTelefonoAgente = new JLabel("Telefono");
		lblTelefonoAgente.setBounds(153, 381, 121, 21);
		panelProponiOfferta.add(lblTelefonoAgente);

		final JLabel lblAgenziaAgente = new JLabel("Agenzia");
		lblAgenziaAgente.setBounds(14, 381, 253, 21);
		panelProponiOfferta.add(lblAgenziaAgente);

		final JLabel lblDescrizioneProponiOfferta = new JLabel("Ti interessa questo immobile?");
		lblDescrizioneProponiOfferta.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblDescrizioneProponiOfferta.setHorizontalAlignment(SwingConstants.CENTER);
		lblDescrizioneProponiOfferta.setBounds(49, 41, 179, 21);
		panelProponiOfferta.add(lblDescrizioneProponiOfferta);

		final JLabel lblCognomeAgente = new JLabel("Cognome");
		lblCognomeAgente.setBounds(153, 258, 124, 21);
		panelProponiOfferta.add(lblCognomeAgente);

		final JLabel lblEmail = new JLabel("Email:");
		lblEmail.setBounds(14, 297, 253, 21);
		panelProponiOfferta.add(lblEmail);

		final JLabel lblTelefono = new JLabel("Telefono:");
		lblTelefono.setBounds(153, 359, 94, 21);
		panelProponiOfferta.add(lblTelefono);

		final JLabel lblAgenzia = new JLabel("Agenzia:");
		lblAgenzia.setBounds(14, 359, 253, 21);
		panelProponiOfferta.add(lblAgenzia);

		final JPanel panelDettagliImmobile = new JPanel() {

			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * Sovrascrive il metodo paintComponent per disegnare un pannello
			 * con angoli arrotondati e bordo colorato.
			 *
			 * @param g L'oggetto Graphics utilizzato per il disegno
			 */
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				final int arc = 20; // raggio per gli angoli arrotondati
				final Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				// Colore azzurro tenue (sfondo)
				final Color bgColor = new Color(220, 240, 255);
				// Colore blu bordo
				final Color borderColor = new Color(30, 144, 255);

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

		final JLabel lblNumLocali = new JLabel("n. locali:");
		lblNumLocali.setHorizontalAlignment(SwingConstants.LEFT);
		lblNumLocali.setBounds(156, 11, 126, 30);
		panelDettagliImmobile.add(lblNumLocali);
		lblNumLocali.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblNumLocali.setForeground(new Color(45, 45, 45));
		lblNumLocali.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

		final JLabel lblDimensione = new JLabel("dimensione:");
		lblDimensione.setHorizontalTextPosition(SwingConstants.LEFT);
		lblDimensione.setHorizontalAlignment(SwingConstants.LEFT);
		lblDimensione.setBounds(0, 11, 168, 30);
		panelDettagliImmobile.add(lblDimensione);
		lblDimensione.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblDimensione.setForeground(new Color(45, 45, 45));
		lblDimensione.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

		final JLabel lblNumPiano = new JLabel("piano:");
		lblNumPiano.setBounds(310, 11, 150, 30);
		panelDettagliImmobile.add(lblNumPiano);
		lblNumPiano.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblNumPiano.setForeground(new Color(45, 45, 45));
		lblNumPiano.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

		final JLabel lblNumBagni = new JLabel("n. bagni:");
		lblNumBagni.setBounds(455, 11, 123, 30);
		panelDettagliImmobile.add(lblNumBagni);
		lblNumBagni.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblNumBagni.setForeground(SystemColor.inactiveCaptionText);
		lblNumBagni.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

		final JLabel lblAscensore = new JLabel("ascensore:");
		lblAscensore.setBounds(53, 65, 150, 30);
		panelDettagliImmobile.add(lblAscensore);
		lblAscensore.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblAscensore.setForeground(new Color(45, 45, 45));
		lblAscensore.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

		final JLabel lblPortineria = new JLabel("portineria:");
		lblPortineria.setBounds(229, 65, 144, 30);
		panelDettagliImmobile.add(lblPortineria);
		lblPortineria.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblPortineria.setForeground(new Color(45, 45, 45));
		lblPortineria.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

		final JLabel lblRiscaldamento = new JLabel("riscaldamento:");
		lblRiscaldamento.setBounds(384, 65, 189, 30);
		panelDettagliImmobile.add(lblRiscaldamento);
		lblRiscaldamento.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblRiscaldamento.setForeground(new Color(45, 45, 45));
		lblRiscaldamento.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

		final JLabel lblDescrizioneImmobile = new JLabel("Descrizione");
		lblDescrizioneImmobile.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblDescrizioneImmobile.setBounds(10, 482, 101, 23);
		contentPane.add(lblDescrizioneImmobile);

		final JPanel panelDescrizione = new JPanel() {
			private static final long serialVersionUID = 1L;

			/**
			 * Sovrascrive il metodo paintComponent per disegnare un pannello
			 * con angoli arrotondati e bordo colorato per l'area descrizione.
			 *
			 * @param g L'oggetto Graphics utilizzato per il disegno
			 */
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				final int arc = 20; // raggio per gli angoli arrotondati
				final Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				// Colore azzurro tenue (sfondo)
				final Color bgColor = new Color(220, 240, 255);
				// Colore blu bordo
				final Color borderColor = new Color(30, 144, 255);

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

		// Configura la text area
		descrizioneField.setLineWrap(true);
		descrizioneField.setWrapStyleWord(true);
		descrizioneField.setFont(new Font("Arial", Font.PLAIN, 16));
		descrizioneField.setEditable(false);
		descrizioneField.setEnabled(false);
		descrizioneField.setDisabledTextColor(Color.BLACK);
		descrizioneField.setOpaque(false);
		descrizioneField.setBackground(new Color(0, 0, 0, 0));
		descrizioneField.setBorder(BorderFactory.createEmptyBorder());


		final JPanel panelPrezzoImmobile = new JPanel() {

			private static final long serialVersionUID = 1L;

			/**
			 * Sovrascrive il metodo paintComponent per disegnare un pannello
			 * con angoli arrotondati e bordo colorato per l'area prezzo.
			 *
			 * @param g L'oggetto Graphics utilizzato per il disegno
			 */
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				final int arc = 20; // raggio angoli arrotondati
				final Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				// Colore interno
				final Color bgColor = new Color(200, 60, 60);
				// Colore bordo
				final Color borderColor = new Color(180, 0, 0);

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

		final JLabel lblPrezzo = new JLabel("Prezzo");
		lblPrezzo.setHorizontalAlignment(SwingConstants.CENTER);
		lblPrezzo.setForeground(Color.WHITE);
		lblPrezzo.setBounds(10, 11, 149, 70);
		panelPrezzoImmobile.add(lblPrezzo);
		lblPrezzo.setFont(new Font("Segoe UI", Font.BOLD, 16));

		final ImmobileController controllerImmobile = new ImmobileController();
		final Immobile immobile = controllerImmobile.recuperaDettagli(idImmobile);

		if (immobile != null) {
			// Popola campi
			final String titoloCompleto = immobile.getTitolo() + ", " + immobile.getIndirizzo();
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
				final double prezzoMensile = ((ImmobileInAffitto) immobile).getPrezzoMensile();
				final String prezzoFormattato = String.format("%,.0f", prezzoMensile).replace(",", ".");
				lblPrezzo.setText(
						"<html><div style='text-align: center;'>Prezzo Mensile: <br><span style='font-size:16px; font-weight:bold;'>€ "
								+ prezzoFormattato + "</span></div></html>");
			} else if (immobile instanceof ImmobileInVendita) {
				final double prezzoTotale = ((ImmobileInVendita) immobile).getPrezzoTotale();
				final String prezzoFormattato = String.format("%,.0f", prezzoTotale).replace(",", ".");
				lblPrezzo.setText(
						"<html><div style='text-align: center;'>Prezzo Totale: <br><span style='font-size:16px; font-weight:bold;'>€ "
								+ prezzoFormattato + "</span></div></html>");
			} else {
				lblPrezzo.setText("<html><div style='text-align: center;'>Prezzo: Non disponibile</div></html>");
			}
			System.out.println("DEBUG: immobile.getAgenteAssociato() = " + immobile.getAgenteAssociato());

			final AccountController controllerAccount = new AccountController();
			final Account account = controllerAccount.recuperaDettagliAgente(immobile.getAgenteAssociato());
			System.out.println("DEBUG: account = " + account);

			if ((account != null) && (account != null)) {
				lblNomeAgente.setText(account.getNome());
				lblCognomeAgente.setText(account.getCognome());
				lblEmailAgente.setText(account.getEmail());

				lblTelefonoAgente.setText(account.getTelefono());

				if (account instanceof AgenteImmobiliare) {
					lblAgenziaAgente.setText(((AgenteImmobiliare) account).getAgenzia());
				} else {
					lblAgenziaAgente.setText("Agenzia: N/A");
				}
			}

			// Visualizza immagini
			immagini = immobile.getImmagini();
			indiceFotoCorrente = 0;

			// Usa il metodo di GuiUtils per caricare la prima immagine
			GuiUtils.caricaImmagineInEtichetta(lblFoto,
					(immagini != null && !immagini.isEmpty()) ? immagini.get(0) : null, lblFoto.getWidth(),
							lblFoto.getHeight());

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
		lblFotoPlus.addMouseListener(new MouseAdapter() {
			/**
			 * Gestisce il click sul contatore immagini per navigare tra le foto.
			 * Cambia l'immagine visualizzata in modo circolare tra tutte le foto disponibili.
			 *
			 * @param e L'evento del mouse che ha triggerato il cambio immagine
			 *
			 * @see GuiUtils#caricaImmagineInEtichetta(JLabel, byte[], int, int)
			 */
			@Override
			public void mouseClicked(MouseEvent e) {
				if (immagini != null && immagini.size() > 1) {
					// Passa alla prossima immagine
					indiceFotoCorrente = (indiceFotoCorrente + 1) % immagini.size();

					// Usa GuiUtils per caricare la nuova immagine
					GuiUtils.caricaImmagineInEtichetta(lblFoto, immagini.get(indiceFotoCorrente), lblFoto.getWidth(),
							lblFoto.getHeight());

					// Aggiorna il contatore
					if (immagini.size() > 1) {
						lblFotoPlus.setText("+" + (immagini.size() - 1) + " foto");
					}
				}
			}
		});

	}
}