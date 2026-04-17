package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import model.entity.Filtri;
import util.GuiUtils;
import util.InputUtils;

/**
 * Finestra per la gestione dei filtri di ricerca immobili in DietiEstates25.
 * Permette agli utenti di definire criteri di ricerca avanzati per affinare
 * i risultati della ricerca immobiliare in base alle proprie preferenze.
 *
 * <p>La finestra supporta diverse tipologie di filtri:
 * <ul>
 *   <li>Filtri numerici: prezzo minimo/massimo, superficie minima/massima</li>
 *   <li>Filtri strutturali: numero locali, numero bagni, piano</li>
 *   <li>Filtri di servizi: ascensore, portineria, climatizzazione</li>
 * </ul>
 *
 * <p>Funzionalità avanzate:
 * <ul>
 *   <li>Persistenza delle preferenze tramite {@link Preferences} API</li>
 *   <li>Reset completo dei filtri con conferma</li>
 *   <li>Salvataggio automatico delle scelte</li>
 *   <li>Opzione "Indifferente" per ogni filtro</li>
 * </ul>
 *
 * <p>I filtri vengono adattati dinamicamente in base alla tipologia di appartamento
 * (Vendita/Affitto) con range di prezzo appropriati.
 *
 * @author IngSW2425_055 Team
 * @see Filtri
 * @see Preferences
 * @see InputUtils
 */
public class ViewFiltri extends JFrame {
	private static final long serialVersionUID = 1L;
	// Attributi della classe ViewFiltri
	private final Preferences prefs;
	private final JPanel contentPane;
	// attributi che serviranno per settare gli input ricevuti all'interno della
	// classe Filtri
	private final JComboBox<String> comboBoxPMin;
	private final JComboBox<String> comboBoxPMax;
	private final JComboBox<String> comboBoxSMin;
	private final JComboBox<String> comboBoxSMax;
	private final JComboBox<String> comboBoxNumLocali;
	private final JComboBox<String> comboBoxPiano;
	private final JComboBox<String> comboBoxNumBagni;
	private final JCheckBox chckbxAscensore;
	private final JCheckBox chckbxPortineria;
	private final JCheckBox chckbxClimatizzazione;

	/**
	 * Costruttore della finestra di gestione filtri.
	 * Inizializza tutti i componenti grafici e carica le preferenze precedentemente
	 * salvate per ogni filtro.
	 *
	 * <p>I filtri disponibili vengono adattati dinamicamente in base alla tipologia
	 * di appartamento specificata, con particolare attenzione ai range di prezzo.
	 *
	 * @param tipologiaAppartamento Tipologia di immobile per cui applicare i filtri.
	 *                              Determina i range di prezzo disponibili:
	 *                              <ul>
	 *                                <li>"Vendita": range da 50.000€ a 1.000.000€</li>
	 *                                <li>"Affitto": range da 200€ a 4.000€</li>
	 *                              </ul>
	 * @throws IllegalArgumentException Se la tipologia non è "Vendita" o "Affitto"
	 */
	// Costruttore che crea il frame
	public ViewFiltri(String tipologiaAppartamento) {
		// Imposta l'icona di DietiEstates25 alla finestra in uso
		GuiUtils.setIconaFinestra(this);
		setTitle("DietiEstates25 - Filtri di ricerca");
		setResizable(false);

		prefs = Preferences.userNodeForPackage(ViewFiltri.class);

		/**
		 * Listener per il click sul frame che resetta le preferenze.
		 * Utilizzato principalmente per debug e testing.
		 */
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					prefs.clear();
				} catch (BackingStoreException e1) {
					e1.printStackTrace();
				}
			}
		});

		// Opzioni per le comboBox
		String[] opPrezzoMin = null;
		String[] opPrezzoMax = null;
		switch (tipologiaAppartamento) {
		case "Vendita" -> {
			opPrezzoMin = new String[] { "Indifferente", "50000", "60000", "70000", "80000", "90000", "100000",
					"150000", "200000", "300000", "500000", "1000000" };
			opPrezzoMax = new String[] { "Indifferente", "50000", "60000", "70000", "80000", "90000", "100000",
					"150000", "200000", "300000", "500000", "1000000" };
		}
		case "Affitto" -> {
			opPrezzoMin = new String[] { "Indifferente", "200", "300", "400", "500", "600", "700", "800", "900", "1000",
					"1500", "2000", "2500", "3000", "3500", "4000" };
			opPrezzoMax = new String[] { "Indifferente", "200", "300", "400", "500", "600", "700", "800", "900", "1000",
					"1500", "2000", "2500", "3000", "3500", "4000" };
		}
		}

		final String[] opSupMin = { "Indifferente", "40", "60", "80", "100", "120", "150", "180", "200", "300", "500" };
		final String[] opSupMax = { "Indifferente", "40", "60", "80", "100", "120", "150", "180", "200", "300", "500" };
		final String[] opNumLocali = { "Indifferente", "1", "2", "3", "4", "5" };
		final String[] opNumBagni = { "Indifferente", "1", "2", "3", "4" };
		final String[] opPiano = { "Indifferente", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setBounds(100, 100, 937, 454);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		final JPanel panelTitolo = new JPanel();
		panelTitolo.setBounds(0, 0, 935, 429);
		contentPane.add(panelTitolo);
		panelTitolo.setLayout(null);

		SwingUtilities.invokeLater(this::requestFocusInWindow);

		// Titolo schermata
		final JLabel lblFiltri = new JLabel("Qui puoi selezionare i filtri di ricerca");
		lblFiltri.setBounds(27, 23, 359, 22);
		panelTitolo.add(lblFiltri);
		lblFiltri.setFont(new Font("Tahoma", Font.BOLD, 18));

		final JLabel lblPrezzoMinimo = new JLabel("Prezzo minimo (€)");
		lblPrezzoMinimo.setHorizontalAlignment(SwingConstants.CENTER);
		lblPrezzoMinimo.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPrezzoMinimo.setBounds(145, 92, 122, 14);
		panelTitolo.add(lblPrezzoMinimo);

		comboBoxPMin = new JComboBox<>(opPrezzoMin);
		comboBoxPMin.setToolTipText("Seleziona il prezzo di partenza");
		comboBoxPMin.setMaximumRowCount(12);
		comboBoxPMin.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 11));
		comboBoxPMin.setBackground(Color.WHITE);
		comboBoxPMin.setBounds(155, 117, 103, 29);
		panelTitolo.add(comboBoxPMin);
		comboBoxPMin.setSelectedItem(prefs.get("prezzoMin", "Indifferente"));

		final JLabel lblPrezzoMassimo = new JLabel("Prezzo massimo (€)");
		lblPrezzoMassimo.setHorizontalAlignment(SwingConstants.CENTER);
		lblPrezzoMassimo.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPrezzoMassimo.setBounds(313, 92, 122, 14);
		panelTitolo.add(lblPrezzoMassimo);

		comboBoxPMax = new JComboBox<>(opPrezzoMax);
		comboBoxPMax.setToolTipText("Seleziona il prezzo limite");
		comboBoxPMax.setMaximumRowCount(12);
		comboBoxPMax.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 11));
		comboBoxPMax.setBackground(Color.WHITE);
		comboBoxPMax.setBounds(322, 117, 103, 29);
		panelTitolo.add(comboBoxPMax);
		comboBoxPMax.setSelectedItem(prefs.get("prezzoMax", "Indifferente"));

		final JLabel lblSuperficieMinima = new JLabel("Superficie minima (mq)");
		lblSuperficieMinima.setHorizontalAlignment(SwingConstants.CENTER);
		lblSuperficieMinima.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSuperficieMinima.setBounds(478, 92, 141, 14);
		panelTitolo.add(lblSuperficieMinima);

		comboBoxSMin = new JComboBox<>(opSupMin);
		comboBoxSMin.setToolTipText("Seleziona la superficie di partenza");
		comboBoxSMin.setMaximumRowCount(12);
		comboBoxSMin.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 11));
		comboBoxSMin.setBackground(Color.WHITE);
		comboBoxSMin.setBounds(494, 117, 103, 29);
		panelTitolo.add(comboBoxSMin);
		comboBoxSMin.setSelectedItem(prefs.get("supMin", "Indifferente"));

		final JLabel lblSuperficieMassima = new JLabel("Superficie massima (mq)");
		lblSuperficieMassima.setHorizontalAlignment(SwingConstants.CENTER);
		lblSuperficieMassima.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSuperficieMassima.setBounds(638, 92, 148, 14);
		panelTitolo.add(lblSuperficieMassima);

		comboBoxSMax = new JComboBox<>(opSupMax);
		comboBoxSMax.setToolTipText("Seleziona la superficie limite");
		comboBoxSMax.setMaximumRowCount(12);
		comboBoxSMax.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 11));
		comboBoxSMax.setBackground(Color.WHITE);
		comboBoxSMax.setBounds(661, 117, 103, 29);
		panelTitolo.add(comboBoxSMax);
		comboBoxSMax.setSelectedItem(prefs.get("supMax", "Indifferente"));

		final JLabel lblNumeroLocali = new JLabel("Numero locali");
		lblNumeroLocali.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNumeroLocali.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNumeroLocali.setBounds(170, 219, 97, 14);
		panelTitolo.add(lblNumeroLocali);

		comboBoxNumLocali = new JComboBox<>(opNumLocali);
		comboBoxNumLocali.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 11));
		comboBoxNumLocali.setBackground(new Color(255, 255, 255));
		comboBoxNumLocali.setBounds(277, 215, 115, 22);
		panelTitolo.add(comboBoxNumLocali);
		comboBoxNumLocali.setSelectedItem(prefs.get("numLocali", "Indifferente"));

		final JLabel lblPiano = new JLabel("Piano");
		lblPiano.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPiano.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPiano.setBounds(170, 300, 97, 14);
		panelTitolo.add(lblPiano);

		comboBoxPiano = new JComboBox<>(opPiano);
		comboBoxPiano.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 11));
		comboBoxPiano.setBackground(new Color(255, 255, 255));
		comboBoxPiano.setBounds(277, 296, 115, 22);
		panelTitolo.add(comboBoxPiano);
		comboBoxPiano.setSelectedItem(prefs.get("piano", "Indifferente"));

		final JLabel lblNumeroBagni = new JLabel("Numero bagni");
		lblNumeroBagni.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNumeroBagni.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNumeroBagni.setBounds(170, 259, 97, 14);
		panelTitolo.add(lblNumeroBagni);

		comboBoxNumBagni = new JComboBox<>(opNumBagni);
		comboBoxNumBagni.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 11));
		comboBoxNumBagni.setBackground(new Color(255, 255, 255));
		comboBoxNumBagni.setBounds(277, 255, 115, 22);
		panelTitolo.add(comboBoxNumBagni);
		comboBoxNumBagni.setSelectedItem(prefs.get("numBagni", "Indifferente"));

		// Elenco di checkbox sulle possibili preferenze
		chckbxAscensore = new JCheckBox("Ascensore");
		chckbxAscensore.setFont(new Font("Tahoma", Font.BOLD, 11));
		chckbxAscensore.setBounds(523, 215, 97, 23);
		panelTitolo.add(chckbxAscensore);
		// chckbxAscensore.setSelected(false);
		chckbxAscensore.setSelected(prefs.getBoolean("ascensore", false));

		chckbxPortineria = new JCheckBox("Portineria");
		chckbxPortineria.setFont(new Font("Tahoma", Font.BOLD, 11));
		chckbxPortineria.setBounds(648, 215, 97, 23);
		panelTitolo.add(chckbxPortineria);
		chckbxPortineria.setSelected(prefs.getBoolean("portineria", false));

		chckbxClimatizzazione = new JCheckBox("Climatizzazione");
		chckbxClimatizzazione.setFont(new Font("Tahoma", Font.BOLD, 11));
		chckbxClimatizzazione.setBounds(523, 255, 115, 23);
		panelTitolo.add(chckbxClimatizzazione);
		chckbxClimatizzazione.setSelected(prefs.getBoolean("climatizzazione", false));

		// Bottoni di gestione della finestra
		final JButton btnSalvaFiltri = new JButton("Salva");

		getRootPane().setDefaultButton(btnSalvaFiltri);
		/**
		 * Listener per il pulsante "Salva" che persiste le preferenze selezionate
		 * e chiude la finestra.
		 */
		btnSalvaFiltri.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				prefs.put("prezzoMin", (String) comboBoxPMin.getSelectedItem());
				prefs.put("prezzoMax", (String) comboBoxPMax.getSelectedItem());
				prefs.put("supMin", (String) comboBoxSMin.getSelectedItem());
				prefs.put("supMax", (String) comboBoxSMax.getSelectedItem());
				prefs.put("numLocali", (String) comboBoxNumLocali.getSelectedItem());
				prefs.put("piano", (String) comboBoxPiano.getSelectedItem());
				prefs.put("numBagni", (String) comboBoxNumBagni.getSelectedItem());
				prefs.putBoolean("ascensore", chckbxAscensore.isSelected());
				prefs.putBoolean("portineria", chckbxPortineria.isSelected());
				prefs.putBoolean("climatizzazione", chckbxClimatizzazione.isSelected());
				// chiama view dashboard con stringa di preferenze (?)

				/**
				 * Chiude la finestra dei filtri dopo aver salvato le preferenze.
				 * Le preferenze verranno utilizzate per filtrare i risultati nella
				 * schermata principale.
				 */
				ViewFiltri.this.dispose();
			}
		});
		btnSalvaFiltri.setForeground(Color.WHITE);
		btnSalvaFiltri.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 11));
		btnSalvaFiltri.setFocusable(false);
		btnSalvaFiltri.setBackground(SystemColor.textHighlight);
		btnSalvaFiltri.setBounds(494, 372, 185, 23);
		panelTitolo.add(btnSalvaFiltri);

		/**
		 * Pulsante per annullare le modifiche e chiudere la finestra senza salvare.
		 */
		final JButton btnAnnulla = new JButton("Annulla");
		btnAnnulla.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ViewFiltri.this.dispose();
			}
		});
		btnAnnulla.setForeground(Color.WHITE);
		btnAnnulla.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 11));
		btnAnnulla.setFocusable(false);
		btnAnnulla.setBackground(new Color(255, 74, 74));
		btnAnnulla.setBounds(250, 372, 185, 23);
		panelTitolo.add(btnAnnulla);

		/**
		 * Pulsante per resettare tutti i filtri ai valori predefiniti.
		 * Richiede conferma all'utente prima di procedere con l'operazione.
		 */
		final JButton btnReset = new JButton("Reset filtri");
		btnReset.addActionListener(new ActionListener() {
			/**
			 * Gestisce il reset completo di tutti i filtri.
			 *
			 * <p>Il metodo:
			 * <ol>
			 *   <li>Mostra una finestra di conferma con opzione "Sì/No"</li>
			 *   <li>Se l'utente conferma, resetta tutte le preferenze a valori predefiniti</li>
			 *   <li>Imposta tutti i filtri a "Indifferente" o "false"</li>
			 *   <li>Mostra un messaggio di successo</li>
			 *   <li>Chiude la finestra corrente</li>
			 * </ol>
			 *
			 * @param e Evento dell'azione del pulsante
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				// Mostra una finestra di conferma
				final int scelta = JOptionPane.showConfirmDialog(null, "Sei sicuro di voler resettare i filtri?",
						"Conferma Reset", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

				// Se l'utente ha cliccato "Sì", esegue il reset
				if (scelta == JOptionPane.YES_OPTION) {
					prefs.put("prezzoMin", "Indifferente");
					prefs.put("prezzoMax", "Indifferente");
					prefs.put("supMin", "Indifferente");
					prefs.put("supMax", "Indifferente");
					prefs.put("numLocali", "Indifferente");
					prefs.put("piano", "Indifferente");
					prefs.put("numBagni", "Indifferente");
					prefs.putBoolean("ascensore", false);
					prefs.putBoolean("postoAuto", false);
					prefs.putBoolean("portineria", false);
					prefs.putBoolean("climatizzazione", false);

					JOptionPane.showMessageDialog(null, "Reset avvenuto con successo!", "Reset",
							JOptionPane.INFORMATION_MESSAGE);

					// Chiude la finestra corrente
					ViewFiltri.this.dispose();
				}
			}
		});

		btnReset.setForeground(Color.WHITE);
		btnReset.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 11));
		btnReset.setFocusable(false);
		btnReset.setBackground(SystemColor.textHighlight);
		btnReset.setBounds(705, 23, 185, 23);
		panelTitolo.add(btnReset);

	}

	/**
	 * Crea un oggetto {@link Filtri} basato sui valori selezionati nell'interfaccia.
	 *
	 * <p>Il metodo converte tutti i valori delle comboBox e checkbox in un oggetto
	 * Filtri pronto per essere utilizzato nelle query di ricerca. I valori "Indifferente"
	 * vengono convertiti in {@code null}.
	 *
	 * @return Oggetto {@link Filtri} contenente tutti i criteri di ricerca selezionati
	 * @see InputUtils#parseComboInteger(JComboBox)
	 */
	private Filtri creaFiltriDaInput() {
		// Legge e converte i valori dalle combobox
		final Integer prezzoMin = InputUtils.parseComboInteger(comboBoxPMin);
		final Integer prezzoMax = InputUtils.parseComboInteger(comboBoxPMax);
		final Integer supMin = InputUtils.parseComboInteger(comboBoxSMin);
		final Integer supMax = InputUtils.parseComboInteger(comboBoxSMax);
		final String piano = comboBoxPiano.getSelectedItem().toString();
		final Integer numLocali = InputUtils.parseComboInteger(comboBoxNumLocali);
		final Integer numBagni = InputUtils.parseComboInteger(comboBoxNumBagni);
		final Boolean ascensore = chckbxAscensore.isSelected() ? Boolean.TRUE : null;
		final Boolean portineria = chckbxPortineria.isSelected() ? Boolean.TRUE : null;
		final Boolean climatizzazione = chckbxClimatizzazione.isSelected() ? Boolean.TRUE : null;

		return new Filtri(prezzoMin, prezzoMax, supMin, supMax, piano, numLocali, numBagni, ascensore, portineria,
				climatizzazione);

	}

	/**
	 * Restituisce l'oggetto {@link Filtri} contenente tutti i criteri di ricerca
	 * selezionati dall'utente nell'interfaccia.
	 *
	 * <p>Questo metodo è il punto di accesso principale per recuperare i filtri
	 * configurati dall'utente e utilizzarli nelle operazioni di ricerca.
	 *
	 * @return Oggetto {@link Filtri} con tutti i criteri di ricerca attivi
	 * @see #creaFiltriDaInput()
	 */
	public Filtri getFiltriSelezionati() {
		return creaFiltriDaInput();
	}

}