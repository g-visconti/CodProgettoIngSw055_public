package gui.amministrazione;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.json.JSONObject;

import controller.AccountController;
import controller.ImmobileController;
import model.entity.Immobile;
import model.entity.ImmobileInAffitto;
import model.entity.ImmobileInVendita;
import util.GuiUtils;

/**
 * Finestra per il caricamento di un nuovo immobile nel sistema.
 * Questa interfaccia permette all'agente immobiliare di inserire tutti i dati
 * necessari per pubblicare un annuncio di vendita o affitto.
 *
 * <p>La finestra include campi per:
 * <ul>
 *   <li>Informazioni base (titolo, indirizzo, località)
 *   <li>Caratteristiche tecniche (dimensione, locali, bagni)
 *   <li>Servizi aggiuntivi (ascensore, portineria, climatizzazione)
 *   <li>Upload di immagini
 *   <li>Prezzo e tipologia (vendita/affitto)
 * </ul>
 *
 * @author IngSW2425_055 Team
 * @see ImmobileController
 * @see Immobile
 */
public class ViewCaricaImmobile extends JFrame {

	private static final long serialVersionUID = 1L;
	// attributi
	private final JPanel contentPane;
	private final JTextArea descrizioneField;
	private final JTextField titoloField;
	private final JTextField localitaField;
	private final JTextField dimensioneField;
	private final JTextField indirizzoField;
	private final JTextField prezzoField;
	private final List<byte[]> immaginiCaricate = new ArrayList<>();

	// costruttore

	/**
	 * Costruttore della finestra di caricamento immobile.
	 * Inizializza tutti i componenti grafici e imposta i listener degli eventi.
	 *
	 * @param emailAgente Email dell'agente immobiliare che sta caricando l'immobile.
	 *                    Viene utilizzata per associare l'immobile all'agente corretto.
	 * @throws IllegalArgumentException Se l'email dell'agente è null o vuota
	 */
	public ViewCaricaImmobile(String emailAgente) {
		super("DietiEstates25 - Schermata di caricamento immobile");

		setResizable(false);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 584, 630);

		GuiUtils.setIconaFinestra(this);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);


		// immagine dell'immobile
		final JLabel lblFoto = new JLabel("");
		lblFoto.setOpaque(true);
		lblFoto.setBackground(Color.WHITE);
		lblFoto.setBounds(8, 10, 132, 157);
		contentPane.add(lblFoto);

		GuiUtils.setIconaLabel(lblFoto, "camera");

		final JLabel lblNuovaFoto = new JLabel("+ foto");
		lblNuovaFoto.setBounds(140, 10, 46, 157);
		contentPane.add(lblNuovaFoto);

		lblFoto.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				caricaFoto(lblFoto, lblNuovaFoto);
			}
		});


		// text field
		titoloField = new JTextField();
		titoloField.setBounds(259, 78, 101, 20);
		titoloField.setColumns(10);
		contentPane.add(titoloField);

		localitaField = new JTextField();
		localitaField.setBounds(259, 148, 101, 20);
		localitaField.setColumns(10);
		contentPane.add(localitaField);

		dimensioneField = new JTextField();
		dimensioneField.setBounds(259, 205, 73, 19);
		dimensioneField.setColumns(10);
		contentPane.add(dimensioneField);

		indirizzoField = new JTextField();
		indirizzoField.setBounds(380, 148, 103, 20);
		indirizzoField.setColumns(10);
		contentPane.add(indirizzoField);

		prezzoField = new JTextField();
		prezzoField.setBounds(259, 314, 86, 20);
		prezzoField.setColumns(10);
		contentPane.add(prezzoField);

		descrizioneField = new JTextArea();
		descrizioneField.setLineWrap(true);
		descrizioneField.setWrapStyleWord(true);

		final JScrollPane scrollPane = new JScrollPane(descrizioneField);
		scrollPane.setBounds(81, 394, 419, 106);
		contentPane.add(scrollPane);


		// label
		final JLabel lblTitolo = new JLabel("Titolo");
		lblTitolo.setBounds(260, 47, 85, 20);
		contentPane.add(lblTitolo);

		final JLabel lblTipologia = new JLabel("Tipologia");
		lblTipologia.setBounds(380, 50, 73, 14);
		contentPane.add(lblTipologia);

		final JLabel lblLocalita = new JLabel("Località");
		lblLocalita.setBounds(259, 120, 69, 17);
		contentPane.add(lblLocalita);

		final JLabel lblIndirizzo = new JLabel("Indirizzo");
		lblIndirizzo.setBounds(380, 120, 101, 17);
		contentPane.add(lblIndirizzo);

		final JLabel lblDimensione = new JLabel("Dimensione");
		lblDimensione.setBounds(259, 179, 73, 14);
		contentPane.add(lblDimensione);

		final JLabel lblMq = new JLabel("mq");
		lblMq.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblMq.setBounds(333, 207, 18, 17);
		contentPane.add(lblMq);

		final JLabel lblPiano = new JLabel("Piano");
		lblPiano.setBounds(380, 179, 73, 14);
		contentPane.add(lblPiano);

		final JLabel lblLocali = new JLabel("Locali");
		lblLocali.setBounds(259, 237, 46, 14);
		contentPane.add(lblLocali);

		final JLabel lblBagni = new JLabel("Bagni");
		lblBagni.setBounds(380, 237, 63, 14);
		contentPane.add(lblBagni);

		final JLabel lblPrezzo = new JLabel("Prezzo");
		lblPrezzo.setBounds(259, 289, 69, 14);
		contentPane.add(lblPrezzo);

		final JLabel lblEuro = new JLabel("€");
		lblEuro.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblEuro.setBounds(349, 317, 69, 17);
		contentPane.add(lblEuro);

		final JLabel lblDescrizione = new JLabel("Descrizione");
		lblDescrizione.setHorizontalAlignment(SwingConstants.CENTER);
		lblDescrizione.setBounds(229, 370, 109, 20);
		contentPane.add(lblDescrizione);


		// check box
		final JCheckBox ascensoreChckbx = new JCheckBox("Ascensore");
		ascensoreChckbx.setBounds(18, 204, 97, 23);
		contentPane.add(ascensoreChckbx);

		final JCheckBox portineriaChckbx = new JCheckBox("Portineria");
		portineriaChckbx.setBounds(117, 205, 97, 20);
		contentPane.add(portineriaChckbx);

		final JCheckBox climatizzazioneChckbx = new JCheckBox("Climatizzazione");
		climatizzazioneChckbx.setBounds(17, 242, 140, 23);
		contentPane.add(climatizzazioneChckbx);


		// combo box
		final JComboBox<String> tipologiaComboBox = new JComboBox<>();
		tipologiaComboBox.setFont(new Font("Tahoma", Font.PLAIN, 11));
		tipologiaComboBox.setModel(new DefaultComboBoxModel<>(new String[] { "-", "Vendita", "Affitto" }));
		tipologiaComboBox.setBounds(380, 78, 103, 20);
		contentPane.add(tipologiaComboBox);

		final String[] piani = { "-", "0", "1", "2", "3", "4", "5", "6", "7" };
		final JComboBox<String> pianoComboBox = new JComboBox<>(piani);
		pianoComboBox.setFont(new Font("Tahoma", Font.PLAIN, 11));
		pianoComboBox.setBounds(380, 205, 103, 19);
		contentPane.add(pianoComboBox);


		// spinner
		final JSpinner localiSpinner = new JSpinner();
		localiSpinner.setFont(new Font("Tahoma", Font.PLAIN, 11));
		localiSpinner.setBounds(259, 261, 101, 20);
		contentPane.add(localiSpinner);

		final JSpinner bagniSpinner = new JSpinner();
		bagniSpinner.setFont(new Font("Tahoma", Font.PLAIN, 11));
		bagniSpinner.setBounds(382, 262, 101, 20);
		contentPane.add(bagniSpinner);


		// Carica i dati inseriti nel form
		final JButton btnCarica = new JButton("Carica");
		btnCarica.setFocusable(false);
		btnCarica.setForeground(Color.WHITE);
		btnCarica.setBackground(new Color(255, 0, 51));
		btnCarica.setBounds(223, 547, 121, 25);

		btnCarica.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				caricaImmobile(emailAgente, tipologiaComboBox, pianoComboBox, ascensoreChckbx, portineriaChckbx, climatizzazioneChckbx, bagniSpinner, localiSpinner);
			}
		});

		contentPane.add(btnCarica);
	}


	// metodi

	/**
	 * Permette all'utente di selezionare una o più immagini dal file system
	 * e le carica nella lista delle immagini dell'immobile.
	 *
	 * <p>Le immagini vengono convertite in array di byte per essere memorizzate
	 * nel database. Viene mostrata un'anteprima della prima immagine selezionata.
	 *
	 * @param lblFoto JLabel dove verrà visualizzata l'anteprima della prima immagine
	 * @param lblNuovaFoto JLabel che mostra il conteggio delle immagini aggiuntive
	 * @throws IOException Se si verifica un errore durante la lettura dei file
	 * @see JFileChooser
	 */
	private void caricaFoto(JLabel lblFoto, JLabel lblNuovaFoto) {
		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(true);
		fileChooser.setFileFilter(new FileNameExtensionFilter("Immagini", "jpg", "png", "jpeg"));

		final int result = fileChooser.showOpenDialog(null);

		if (result == JFileChooser.APPROVE_OPTION) {
			final File[] files = fileChooser.getSelectedFiles();
			immaginiCaricate.clear();

			for (File file : files) {
				try {
					final byte[] bytes = Files.readAllBytes(file.toPath());
					immaginiCaricate.add(bytes);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}

			if (!immaginiCaricate.isEmpty()) {
				final ImageIcon icon = new ImageIcon(immaginiCaricate.get(0));
				final Image img = icon.getImage().getScaledInstance(lblFoto.getWidth(), lblFoto.getHeight(),
						Image.SCALE_SMOOTH);

				lblFoto.setIcon(new ImageIcon(img));
				lblFoto.setText("");
			}

			if (immaginiCaricate.size() > 1) {
				lblNuovaFoto.setText("+" + (immaginiCaricate.size() - 1) + " foto");
			} else {
				lblNuovaFoto.setText("+ foto");
			}
		}
	}

	/**
	 * Valida e carica i dati dell'immobile nel sistema.
	 *
	 * <p>Questo metodo raccoglie tutti i dati inseriti dall'utente, li valida,
	 * crea un oggetto Immobile appropriato (ImmobileInAffitto o ImmobileInVendita)
	 * e lo salva nel database attraverso il controller.
	 *
	 * @param emailAgente Email dell'agente che carica l'immobile
	 * @param tipologiaComboBox ComboBox per la selezione della tipologia (Vendita/Affitto)
	 * @param pianoComboBox ComboBox per la selezione del piano
	 * @param ascensoreChckbx CheckBox per l'ascensore
	 * @param portineriaChckbx CheckBox per la portineria
	 * @param climatizzazioneChckbx CheckBox per la climatizzazione
	 * @param bagniSpinner Spinner per il numero di bagni
	 * @param localiSpinner Spinner per il numero di locali
	 *
	 * @throws NumberFormatException Se i campi numerici contengono valori non validi
	 * @throws IllegalArgumentException Se la validazione dei dati fallisce
	 *
	 * @see ImmobileController#validaImmobile(String, String, String, String, String, int, int, int)
	 * @see ImmobileController#caricaImmobile(Immobile)
	 */
	private void caricaImmobile(
			String emailAgente,
			JComboBox<String> tipologiaComboBox,
			JComboBox<String> pianoComboBox,
			JCheckBox ascensoreChckbx,
			JCheckBox portineriaChckbx,
			JCheckBox climatizzazioneChckbx,
			JSpinner bagniSpinner,
			JSpinner localiSpinner
			) {
		try {
			ImmobileController con = new ImmobileController();

			String titolo = titoloField.getText();
			String indirizzo = indirizzoField.getText();
			String localita = localitaField.getText();
			String descrizione = descrizioneField.getText();
			String tipologia = (String) tipologiaComboBox.getSelectedItem();

			int dimensione;
			try {
				dimensione = Integer.parseInt(dimensioneField.getText().trim());
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(null, "Inserisci un numero valido per la dimensione");
				return;
			}

			int prezzo;
			try {
				prezzo = Integer.parseInt(prezzoField.getText().trim());
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(null, "Inserisci un numero valido per il prezzo");
				return;
			}

			String pianoStr = pianoComboBox.getSelectedItem().toString();
			int piano = pianoStr.equals("-") ? -1 : Integer.parseInt(pianoStr);

			int bagni = (Integer) bagniSpinner.getValue();
			int locali = (Integer) localiSpinner.getValue();

			try {
				con.validaImmobile(titolo, indirizzo, localita, descrizione, tipologia, dimensione, prezzo, piano);
			} catch (IllegalArgumentException ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage());
				return;
			}

			JSONObject filtri = new JSONObject();
			filtri.put("ascensore", ascensoreChckbx.isSelected());
			filtri.put("portineria", portineriaChckbx.isSelected());
			filtri.put("climatizzazione", climatizzazioneChckbx.isSelected());
			filtri.put("piano", piano);
			filtri.put("numeroBagni", bagni);
			filtri.put("numeroLocali", locali);

			AccountController controller = new AccountController();
			String idAgenteAssociato = controller.getIdAccountByEmail(emailAgente);

			if ("undef".equals(idAgenteAssociato)) {
				JOptionPane.showMessageDialog(null, "Agente non trovato, impossibile associare immobile.");
				return;
			}

			Immobile immobile;
			if ("Affitto".equalsIgnoreCase(tipologia)) {
				immobile = new ImmobileInAffitto(titolo, indirizzo, localita, dimensione,
						descrizione, tipologia, filtri, idAgenteAssociato, prezzo);

			} else if ("Vendita".equalsIgnoreCase(tipologia)) {
				immobile = new ImmobileInVendita(titolo, indirizzo, localita, dimensione,
						descrizione, tipologia, filtri, idAgenteAssociato, prezzo);

			} else {
				JOptionPane.showMessageDialog(null, "Tipologia non valida");
				return;
			}

			immobile.setImmagini(immaginiCaricate);

			boolean successo = con.caricaImmobile(immobile);

			if (successo) {
				JOptionPane.showMessageDialog(null, "Immobile caricato con successo");
			} else {
				JOptionPane.showMessageDialog(null, "Errore nel caricamento dell'immobile");
			}

			// chiusura della finestra ViewCaricaImmobile
			dispose();

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Errore generico durante il caricamento dell'immobile");
		}
	}
}
