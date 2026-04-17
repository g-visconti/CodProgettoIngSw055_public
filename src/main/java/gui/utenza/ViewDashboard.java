package gui.utenza;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import controller.AccountController;
import controller.ImmobileController;
import database.ConnessioneDatabase;
import gui.ViewAccesso;
import gui.ViewFiltri;
import gui.ViewInfoAccount;
import gui.ViewModificaPassword;
import model.dto.RicercaDTO;
import model.entity.Filtri;
import util.GuiUtils;
import util.InputUtils;

/**
 * Finestra principale della dashboard per l'utente cliente.
 * Questa interfaccia rappresenta la schermata principale dopo il login,
 * permettendo al cliente di cercare immobili, gestire il proprio account
 * e visualizzare lo storico delle proprie offerte.
 *
 * <p>La dashboard è divisa in due aree principali:
 * <ul>
 *   <li>Area di ricerca: contiene campo di testo, filtri, tipologia immobile e comandi utente
 *   <li>Area risultati: mostra gli immobili trovati in una tabella interattiva
 * </ul>
 *
 * <p>Funzionalità incluse:
 * <ul>
 *   <li>Ricerca testuale per via, zona o parola chiave
 *   <li>Filtri avanzati per affinare la ricerca
 *   <li>Visualizzazione dettagliata degli immobili
 *   <li>Gestione profilo utente (visualizzazione info, modifica password)
 *   <li>Accesso allo storico delle offerte proposte
 *   <li>Logout sicuro dal sistema
 * </ul>
 *
 * @author IngSW2425_055 Team
 * @see ViewImmobile
 * @see ViewStoricoCliente
 * @see ViewInfoAccount
 * @see ViewModificaPassword
 */
public class ViewDashboard extends JFrame {

	private static final long serialVersionUID = 1L;
	private String idCliente = null;
	private final String emailCliente;
	private JTextField campoRicerca;
	private String campoPieno;
	private final String campoVuoto = "";
	private JTable tableRisultati;
	private JLabel lblLogout;
	private JLabel lblRisultati;
	private ViewFiltri viewFiltri;
	private JComboBox<String> comboBoxAppartamento;
	private int numeroRisultatiTrovati;

	/**
	 * Costruttore della dashboard cliente.
	 * Inizializza l'interfaccia grafica a schermo intero e configura tutti i componenti
	 * necessari per la ricerca e visualizzazione degli immobili.
	 *
	 * <p>La dashboard viene configurata con:
	 * <ul>
	 *   <li>Finestra a schermo intero con layout responsive
	 *   <li>Pannello di ricerca con campo testuale, filtri e comandi utente
	 *   <li>Pannello risultati con tabella interattiva
	 *   <li>Menu contestuale per la gestione del profilo
	 *   <li>Listener per la gestione degli eventi
	 * </ul>
	 *
	 * @param emailCliente Email del cliente che ha effettuato l'accesso.
	 *                     Viene utilizzata per personalizzare la dashboard e recuperare i dati utente.
	 *
	 * @throws IllegalArgumentException Se l'email del cliente è null o vuota
	 *
	 * @see GuiUtils#setIconaFinestra(JFrame)
	 * @see AccountController#getIdAccountByEmail(String)
	 */
	public ViewDashboard(String emailCliente) {
		// Imposta l'icona di DietiEstates25 alla finestra in uso
		GuiUtils.setIconaFinestra(this);
		setTitle("DietiEstates25 - Dashboard per l'utente");

		setResizable(true);
		Preferences.userNodeForPackage(ViewFiltri.class);

		this.emailCliente = emailCliente;

		// Opzioni per le comboBox
		final String[] opAppartamento = { "Vendita", "Affitto" };

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 1267, 805);

		// Imposta la finestra a schermo intero
		setExtendedState(JFrame.MAXIMIZED_BOTH);

		final JPanel dashboard = new JPanel();
		dashboard.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 11));
		dashboard.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(dashboard);
		dashboard.setLayout(null);
		final SpringLayout sl_dashboard = new SpringLayout();
		dashboard.setLayout(sl_dashboard);

		createRicercaPanel(emailCliente, opAppartamento, sl_dashboard, dashboard);

		// Pannello per i risultati della ricerca
		setupRisultatiPanel(emailCliente, sl_dashboard, dashboard);
	}

	// METODI

	/**
	 * Crea e configura il pannello di ricerca della dashboard.
	 * Questo pannello contiene tutti i controlli per effettuare ricerche di immobili:
	 * campo di testo, comboBox per tipologia, pulsanti filtri e gestione utente.
	 *
	 * @param emailCliente Email del cliente per personalizzare l'interfaccia
	 * @param opAppartamento Array delle opzioni di tipologia immobile (Vendita/Affitto)
	 * @param sl_dashboard Layout della dashboard principale
	 * @param dashboard Pannello contenitore principale
	 *
	 * @return Il pannello di ricerca configurato
	 *
	 * @see #setupCampoRicercaListeners(JPanel)
	 * @see #setupLogoutListener(JLabel)
	 * @see #setupUserMenu(JLabel, String)
	 */
	private JPanel createRicercaPanel(String emailCliente, String[] opAppartamento, SpringLayout sl_dashboard,
			JPanel dashboard) {
		final JPanel ricerca = new JPanel();
		sl_dashboard.putConstraint(SpringLayout.NORTH, ricerca, 0, SpringLayout.NORTH, dashboard);
		sl_dashboard.putConstraint(SpringLayout.WEST, ricerca, 0, SpringLayout.WEST, dashboard);
		sl_dashboard.putConstraint(SpringLayout.SOUTH, ricerca, 189, SpringLayout.NORTH, dashboard);
		sl_dashboard.putConstraint(SpringLayout.EAST, ricerca, 0, SpringLayout.EAST, dashboard);
		ricerca.setAlignmentY(Component.TOP_ALIGNMENT);
		ricerca.setAlignmentX(Component.RIGHT_ALIGNMENT);
		ricerca.setName("");
		ricerca.setBackground(new Color(255, 255, 255));
		dashboard.add(ricerca);

		// Campo di ricerca
		campoRicerca = new JTextField();
		campoRicerca.setText("Cerca scrivendo una via, una zona o una parola chiave");
		campoRicerca.setFont(new Font("Yu Gothic UI Semibold", Font.ITALIC, 11));
		campoRicerca.setColumns(10);

		// Listeners per il campo di ricerca
		setupCampoRicercaListeners(ricerca);

		final SpringLayout sl_ricerca = new SpringLayout();
		sl_ricerca.putConstraint(SpringLayout.SOUTH, campoRicerca, -72, SpringLayout.SOUTH, ricerca);
		sl_ricerca.putConstraint(SpringLayout.EAST, campoRicerca, -355, SpringLayout.EAST, ricerca);
		sl_ricerca.putConstraint(SpringLayout.HORIZONTAL_CENTER, campoRicerca, 0, SpringLayout.HORIZONTAL_CENTER,
				ricerca);
		sl_ricerca.putConstraint(SpringLayout.VERTICAL_CENTER, campoRicerca, 0, SpringLayout.VERTICAL_CENTER, ricerca);
		ricerca.setLayout(sl_ricerca);

		// ComboBox appartamento
		comboBoxAppartamento = new JComboBox<>(opAppartamento);
		sl_ricerca.putConstraint(SpringLayout.WEST, comboBoxAppartamento, 253, SpringLayout.WEST, ricerca);
		sl_ricerca.putConstraint(SpringLayout.EAST, comboBoxAppartamento, -18, SpringLayout.WEST, campoRicerca);
		sl_ricerca.putConstraint(SpringLayout.NORTH, campoRicerca, 0, SpringLayout.NORTH, comboBoxAppartamento);
		sl_ricerca.putConstraint(SpringLayout.NORTH, comboBoxAppartamento, 79, SpringLayout.NORTH, ricerca);
		sl_ricerca.putConstraint(SpringLayout.SOUTH, comboBoxAppartamento, -79, SpringLayout.SOUTH, ricerca);
		comboBoxAppartamento.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 11));
		comboBoxAppartamento.setFocusable(false);
		comboBoxAppartamento.setForeground(new Color(0, 0, 51));
		comboBoxAppartamento.setBackground(new Color(255, 255, 255));
		comboBoxAppartamento.setToolTipText("Seleziona la tipologia di appartamento");

		ricerca.add(comboBoxAppartamento);
		ricerca.add(campoRicerca);

		// Logout
		lblLogout = new JLabel();
		setupLogoutListener(lblLogout);
		sl_ricerca.putConstraint(SpringLayout.NORTH, lblLogout, 13, SpringLayout.NORTH, ricerca);
		sl_ricerca.putConstraint(SpringLayout.SOUTH, lblLogout, -145, SpringLayout.SOUTH, ricerca);
		sl_ricerca.putConstraint(SpringLayout.EAST, lblLogout, -10, SpringLayout.EAST, ricerca);
		lblLogout.setToolTipText("Clicca per uscire da DietiEstates25");
		ricerca.add(lblLogout);
		GuiUtils.setIconaLabel(lblLogout, "Logout");

		// Bottone ricerca
		final JButton btnEseguiRicerca = new JButton();
		GuiUtils.setIconaButton(btnEseguiRicerca, "Search");
		btnEseguiRicerca.setBorderPainted(false);
		btnEseguiRicerca.setFocusPainted(false);
		btnEseguiRicerca.setContentAreaFilled(false);

		sl_ricerca.putConstraint(SpringLayout.NORTH, btnEseguiRicerca, 0, SpringLayout.NORTH, comboBoxAppartamento);
		sl_ricerca.putConstraint(SpringLayout.SOUTH, btnEseguiRicerca, 0, SpringLayout.SOUTH, comboBoxAppartamento);
		sl_ricerca.putConstraint(SpringLayout.EAST, btnEseguiRicerca, -255, SpringLayout.EAST, ricerca);
		sl_ricerca.putConstraint(SpringLayout.WEST, lblLogout, 215, SpringLayout.EAST, btnEseguiRicerca);

		getRootPane().setDefaultButton(btnEseguiRicerca);
		btnEseguiRicerca.setToolTipText("Clicca per iniziare una ricerca");
		btnEseguiRicerca.setBorderPainted(false);

		btnEseguiRicerca.addActionListener(new ActionListener() {
			/**
			 * Gestisce il click sul pulsante di ricerca.
			 * Avvia la ricerca degli immobili in base ai criteri selezionati
			 * e popola la tabella dei risultati.
			 *
			 * @param e L'evento dell'azione che ha triggerato la ricerca
			 *
			 * @see #ricercaImmobili()
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				ricercaImmobili();
			}
		});

		ricerca.add(btnEseguiRicerca);

		// Label utente
		final JLabel lblUser = new JLabel();
		sl_ricerca.putConstraint(SpringLayout.NORTH, lblUser, 0, SpringLayout.NORTH, lblLogout);
		sl_ricerca.putConstraint(SpringLayout.SOUTH, lblUser, -145, SpringLayout.SOUTH, ricerca);
		sl_ricerca.putConstraint(SpringLayout.EAST, lblUser, -49, SpringLayout.EAST, lblLogout);
		lblUser.setToolTipText("Clicca per vedere altre ozioni sul profilo");
		ricerca.add(lblUser);

		// Menu utente
		setupUserMenu(lblUser, emailCliente);
		GuiUtils.setIconaLabel(lblUser, "User");

		// Titolo
		final JLabel lblTitolo = new JLabel("DietiEstates25");
		sl_ricerca.putConstraint(SpringLayout.SOUTH, lblUser, 0, SpringLayout.SOUTH, lblTitolo);
		sl_ricerca.putConstraint(SpringLayout.NORTH, lblTitolo, 0, SpringLayout.NORTH, ricerca);
		sl_ricerca.putConstraint(SpringLayout.WEST, lblTitolo, 10, SpringLayout.WEST, ricerca);
		sl_ricerca.putConstraint(SpringLayout.SOUTH, lblTitolo, -145, SpringLayout.SOUTH, ricerca);
		sl_ricerca.putConstraint(SpringLayout.EAST, lblTitolo, -1008, SpringLayout.EAST, ricerca);
		lblTitolo.setForeground(new Color(27, 99, 142));
		lblTitolo.setHorizontalAlignment(SwingConstants.LEFT);
		lblTitolo.setFont(new Font("Tahoma", Font.BOLD, 30));
		ricerca.add(lblTitolo);

		// Labels benvenuto e email
		setupBenvenutoLabels(ricerca, sl_ricerca, lblTitolo, emailCliente);

		// Filtri
		final JLabel lblFiltri = new JLabel();
		sl_ricerca.putConstraint(SpringLayout.WEST, btnEseguiRicerca, 17, SpringLayout.EAST, lblFiltri);
		sl_ricerca.putConstraint(SpringLayout.NORTH, lblFiltri, 79, SpringLayout.NORTH, ricerca);
		sl_ricerca.putConstraint(SpringLayout.SOUTH, lblFiltri, -79, SpringLayout.SOUTH, ricerca);
		sl_ricerca.putConstraint(SpringLayout.WEST, lblFiltri, 23, SpringLayout.EAST, campoRicerca);
		sl_ricerca.putConstraint(SpringLayout.EAST, lblFiltri, -302, SpringLayout.EAST, ricerca);
		lblFiltri.setToolTipText("Clicca per impostare preferenze aggiuntive, poi esegui la ricerca");

		lblFiltri.addMouseListener(new MouseAdapter() {
			/**
			 * Gestisce il click sull'icona dei filtri.
			 * Apre la finestra di selezione filtri avanzati.
			 *
			 * @param e L'evento del mouse che ha triggerato l'apertura dei filtri
			 *
			 * @see ViewFiltri
			 */
			@Override
			public void mouseClicked(MouseEvent e) {
				final String tipologiaAppartamento = (String) comboBoxAppartamento.getSelectedItem();
				viewFiltri = new ViewFiltri(tipologiaAppartamento);
				viewFiltri.setLocationRelativeTo(null);
				viewFiltri.setVisible(true);
			}
		});
		ricerca.add(lblFiltri);
		GuiUtils.setIconaLabel(lblFiltri, "Tune");

		return ricerca;
	}

	/**
	 * Crea e configura il pannello dei risultati della ricerca.
	 * Questo pannello contiene la tabella per visualizzare gli immobili trovati
	 * e i controlli per navigare nello storico delle offerte.
	 *
	 * @param emailCliente Email del cliente per il recupero dell'ID account
	 * @param sl_dashboard Layout della dashboard principale
	 * @param dashboard Pannello contenitore principale
	 *
	 * @see #ottieniIdAccount(String)
	 * @see ViewStoricoCliente
	 */
	private void setupRisultatiPanel(String emailCliente, SpringLayout sl_dashboard, JPanel dashboard) {
		final JLayeredPane layeredPane = new JLayeredPane();
		sl_dashboard.putConstraint(SpringLayout.NORTH, layeredPane, 190, SpringLayout.NORTH, dashboard);
		sl_dashboard.putConstraint(SpringLayout.WEST, layeredPane, 0, SpringLayout.WEST, dashboard);
		sl_dashboard.putConstraint(SpringLayout.SOUTH, layeredPane, 0, SpringLayout.SOUTH, dashboard);
		sl_dashboard.putConstraint(SpringLayout.EAST, layeredPane, 0, SpringLayout.EAST, dashboard);
		dashboard.add(layeredPane);
		final SpringLayout sl_layeredPane = new SpringLayout();
		layeredPane.setLayout(sl_layeredPane);

		final JPanel risultatiDaRicerca = new JPanel();
		sl_layeredPane.putConstraint(SpringLayout.NORTH, risultatiDaRicerca, 0, SpringLayout.NORTH, layeredPane);
		sl_layeredPane.putConstraint(SpringLayout.WEST, risultatiDaRicerca, 0, SpringLayout.WEST, layeredPane);
		sl_layeredPane.putConstraint(SpringLayout.SOUTH, risultatiDaRicerca, 0, SpringLayout.SOUTH, layeredPane);
		sl_layeredPane.putConstraint(SpringLayout.EAST, risultatiDaRicerca, 0, SpringLayout.EAST, layeredPane);
		risultatiDaRicerca.setBackground(new Color(50, 133, 177));
		layeredPane.add(risultatiDaRicerca);
		final SpringLayout sl_risultatiDaRicerca = new SpringLayout();
		risultatiDaRicerca.setLayout(sl_risultatiDaRicerca);

		final JScrollPane scrollPane = new JScrollPane();
		sl_risultatiDaRicerca.putConstraint(SpringLayout.NORTH, scrollPane, 47, SpringLayout.NORTH, risultatiDaRicerca);
		sl_risultatiDaRicerca.putConstraint(SpringLayout.WEST, scrollPane, 10, SpringLayout.WEST, risultatiDaRicerca);
		sl_risultatiDaRicerca.putConstraint(SpringLayout.SOUTH, scrollPane, -10, SpringLayout.SOUTH,
				risultatiDaRicerca);
		sl_risultatiDaRicerca.putConstraint(SpringLayout.EAST, scrollPane, -10, SpringLayout.EAST, risultatiDaRicerca);
		scrollPane.getViewport().setBackground(new Color(255, 255, 255));
		scrollPane.setBorder(null);
		risultatiDaRicerca.add(scrollPane);

		// Crea la JTable
		tableRisultati = new JTable();
		tableRisultati.setShowVerticalLines(false);
		tableRisultati.setShowGrid(false);
		tableRisultati.setRowHeight(100);
		tableRisultati.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableRisultati.setSelectionBackground(new Color(226, 226, 226));

		// Mouse listener per la tabella
		tableRisultati.addMouseListener(new MouseAdapter() {
			/**
			 * Gestisce il click su una riga della tabella risultati.
			 * Recupera l'ID dell'immobile selezionato e apre la vista dettagliata.
			 *
			 * @param e L'evento del mouse che ha triggerato la selezione
			 *
			 * @see #ottieniIdAccount(String)
			 * @see ViewImmobile
			 */
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					ottieniIdAccount(emailCliente);

					final int row = tableRisultati.rowAtPoint(e.getPoint());
					if (row >= 0) {
						final long idImmobile = (Long) tableRisultati.getValueAt(row, 0);
						final ViewImmobile finestra = new ViewImmobile(idImmobile, idCliente);
						finestra.setLocationRelativeTo(null);
						finestra.setVisible(true);
					}
				}
			}
		});

		scrollPane.setViewportView(tableRisultati);

		lblRisultati = new JLabel("Avvia una ricerca per visualizzare i risultati");
		sl_risultatiDaRicerca.putConstraint(SpringLayout.NORTH, lblRisultati, 5, SpringLayout.NORTH,
				risultatiDaRicerca);
		sl_risultatiDaRicerca.putConstraint(SpringLayout.WEST, lblRisultati, 20, SpringLayout.WEST, risultatiDaRicerca);
		sl_risultatiDaRicerca.putConstraint(SpringLayout.SOUTH, lblRisultati, -10, SpringLayout.NORTH, scrollPane);
		sl_risultatiDaRicerca.putConstraint(SpringLayout.EAST, lblRisultati, 483, SpringLayout.WEST,
				risultatiDaRicerca);
		lblRisultati.setHorizontalAlignment(SwingConstants.LEFT);
		lblRisultati.setForeground(Color.WHITE);
		lblRisultati.setFont(new Font("Tahoma", Font.BOLD, 20));
		risultatiDaRicerca.add(lblRisultati);

		// Vedi Offerte Proposte
		final JButton btnStoricoMieOfferte = new JButton("Storico delle mie offerte");
		sl_risultatiDaRicerca.putConstraint(SpringLayout.NORTH, btnStoricoMieOfferte, 13, SpringLayout.NORTH,
				risultatiDaRicerca);
		sl_risultatiDaRicerca.putConstraint(SpringLayout.EAST, btnStoricoMieOfferte, 0, SpringLayout.EAST, scrollPane);
		btnStoricoMieOfferte.addActionListener(new ActionListener() {
			/**
			 * Gestisce il click sul pulsante "Storico delle mie offerte".
			 * Apre la finestra dello storico delle offerte proposte dal cliente.
			 *
			 * @param e L'evento dell'azione che ha triggerato l'apertura dello storico
			 *
			 * @see ViewStoricoCliente
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				final ViewStoricoCliente viewOfferte = new ViewStoricoCliente(emailCliente);
				viewOfferte.setLocationRelativeTo(null);
				viewOfferte.setVisible(true);
			}
		});

		btnStoricoMieOfferte.setToolTipText("Clicca per visualizzare tutte le offerte proposte");
		btnStoricoMieOfferte.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 11));
		btnStoricoMieOfferte.setBackground(Color.WHITE);
		risultatiDaRicerca.add(btnStoricoMieOfferte);
	}


	/**
	 * Configura i listener per il campo di ricerca testuale.
	 * Gestisce il comportamento del placeholder text quando il campo riceve o perde il focus.
	 *
	 * @param ricerca Pannello contenitore del campo di ricerca
	 *
	 * @see #setCampoDiTestoIn()
	 * @see #setCampoDiTestoOut()
	 */
	private void setupCampoRicercaListeners(JPanel ricerca) {
		campoRicerca.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				setCampoDiTestoIn();
			}
		});

		campoRicerca.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setCampoDiTestoIn();
			}
		});

		ricerca.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setCampoDiTestoOut();
			}
		});
	}

	/**
	 * Configura il listener per l'icona di logout.
	 * Gestisce la disconnessione sicura dall'applicazione con conferma utente.
	 *
	 * @param logoutLabel Label dell'icona di logout a cui associare il listener
	 *
	 * @see ConnessioneDatabase#getInstance()
	 * @see ConnessioneDatabase#closeConnection()
	 * @see ViewAccesso
	 */
	private void setupLogoutListener(JLabel logoutLabel) {
		logoutLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				final int scelta = JOptionPane.showConfirmDialog(null, "Sei sicuro di voler fare logout?",
						"Conferma logout", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

				if (scelta == JOptionPane.YES_OPTION) {
					try {
						ConnessioneDatabase.getInstance().closeConnection();
						JOptionPane.showMessageDialog(null, "Disconnessione avvenuta con successo", "Avviso",
								JOptionPane.INFORMATION_MESSAGE);
					} catch (SQLException ex) {
						JOptionPane.showMessageDialog(null,
								"Errore durante la chiusura della connessione: " + ex.getMessage(), "Errore",
								JOptionPane.ERROR_MESSAGE);
						return;
					}

					final ViewAccesso viewAccesso = new ViewAccesso();
					viewAccesso.setLocationRelativeTo(null);
					viewAccesso.setVisible(true);
					dispose();
				}
			}
		});
	}

	/**
	 * Configura il menu contestuale per la gestione del profilo utente.
	 * Aggiunge le opzioni per visualizzare le informazioni account e modificare la password.
	 *
	 * @param userLabel Label dell'icona utente a cui associare il menu
	 * @param emailCliente Email del cliente per passare alle viste di gestione account
	 *
	 * @see ViewInfoAccount
	 * @see ViewModificaPassword
	 */
	private void setupUserMenu(JLabel userLabel, String emailCliente) {
		final JPopupMenu menuUtente = new JPopupMenu();
		final JMenuItem visualizzaInfoAccount = new JMenuItem("Visualizza informazioni sull'account");
		final JMenuItem modificaPassword = new JMenuItem("Modifica password");

		menuUtente.add(visualizzaInfoAccount);
		menuUtente.add(modificaPassword);

		visualizzaInfoAccount.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final ViewInfoAccount viewAccount = new ViewInfoAccount(emailCliente);
				viewAccount.setLocationRelativeTo(null);
				viewAccount.setVisible(true);
			}
		});

		modificaPassword.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final ViewModificaPassword viewModificaPassword = new ViewModificaPassword(emailCliente);
				viewModificaPassword.setLocationRelativeTo(null);
				viewModificaPassword.setVisible(true);
			}
		});

		userLabel.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mousePressed(java.awt.event.MouseEvent evt) {
				if (evt.getButton() == java.awt.event.MouseEvent.BUTTON1) {
					menuUtente.show(userLabel, evt.getX(), evt.getY());
				}
			}
		});
	}

	/**
	 * Configura le etichette di benvenuto e email nell'intestazione.
	 * Mostra l'email del cliente che ha effettuato l'accesso.
	 *
	 * @param ricerca Pannello di ricerca contenente le etichette
	 * @param sl_ricerca Layout del pannello di ricerca
	 * @param lblTitolo Label del titolo per il posizionamento relativo
	 * @param emailCliente Email da visualizzare nell'intestazione
	 */
	private void setupBenvenutoLabels(JPanel ricerca, SpringLayout sl_ricerca, JLabel lblTitolo, String emailCliente) {
		final JLabel lblBenvenuto = new JLabel("Accesso effettuato con:");
		sl_ricerca.putConstraint(SpringLayout.NORTH, lblBenvenuto, 0, SpringLayout.NORTH, lblLogout);
		sl_ricerca.putConstraint(SpringLayout.WEST, lblBenvenuto, 739, SpringLayout.EAST, lblTitolo);
		sl_ricerca.putConstraint(SpringLayout.EAST, lblBenvenuto, -27, SpringLayout.WEST,
				findLabelByTooltip(ricerca, "Clicca per vedere altre ozioni sul profilo"));
		lblBenvenuto.setHorizontalAlignment(SwingConstants.RIGHT);
		lblBenvenuto.setVerticalAlignment(SwingConstants.TOP);
		ricerca.add(lblBenvenuto);

		final JLabel lblEmailAccesso = new JLabel("<email>");
		sl_ricerca.putConstraint(SpringLayout.WEST, lblEmailAccesso, 685, SpringLayout.EAST, lblTitolo);
		sl_ricerca.putConstraint(SpringLayout.SOUTH, lblEmailAccesso, 0, SpringLayout.SOUTH, lblLogout);
		sl_ricerca.putConstraint(SpringLayout.EAST, lblEmailAccesso, 0, SpringLayout.EAST, lblBenvenuto);
		lblEmailAccesso.setHorizontalAlignment(SwingConstants.RIGHT);
		lblEmailAccesso.setText(emailCliente + " ");
		lblEmailAccesso.setFont(new Font("Yu Gothic UI Semibold", Font.ITALIC, 11));
		ricerca.add(lblEmailAccesso);
	}

	/**
	 * Trova una label all'interno di un pannello in base al suo testo di tooltip.
	 * Metodo di utilità per la ricerca di componenti nell'interfaccia.
	 *
	 * @param panel Pannello in cui cercare la label
	 * @param tooltip Testo del tooltip da cercare
	 *
	 * @return La JLabel trovata, o null se non trovata
	 */
	private JLabel findLabelByTooltip(JPanel panel, String tooltip) {
		for (Component comp : panel.getComponents()) {
			if (comp instanceof JLabel label) {
				if (tooltip.equals(label.getToolTipText())) {
					return label;
				}
			}
		}
		return null;
	}

	/**
	 * Recupera l'ID account del cliente dal database.
	 * Questo metodo viene chiamato la prima volta che è necessario l'ID cliente
	 * e lo memorizza in cache per utilizzi successivi.
	 *
	 * @param emailAgente Email del cliente per il recupero dell'ID
	 *
	 * @throws SQLException Se si verifica un errore durante l'accesso al database
	 *
	 * @see AccountController#getIdAccountByEmail(String)
	 */
	private void ottieniIdAccount(String emailAgente) {
		if (idCliente == null) {
			final AccountController controller = new AccountController();
			try {
				idCliente = controller.getIdAccountByEmail(emailAgente);
			} catch (SQLException ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(this, "Errore nel recupero dell'ID account: " + ex.getMessage(), "Errore",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Esegue la ricerca degli immobili in base ai criteri selezionati.
	 * Questo metodo raccoglie tutti i parametri di ricerca (testo, tipologia, filtri)
	 * e popola la tabella dei risultati tramite il controller.
	 *
	 * <p>La ricerca include:
	 * <ul>
	 *   <li>Validazione del campo di testo (non può essere vuoto o placeholder)
	 *   <li>Capitalizzazione delle parole per normalizzare l'input
	 *   <li>Recupero dei filtri selezionati
	 *   <li>Creazione del DTO di ricerca
	 *   <li>Aggiornamento del conteggio risultati
	 * </ul>
	 *
	 * @throws IllegalArgumentException Se il campo di ricerca è vuoto o contiene solo il placeholder
	 *
	 * @see InputUtils#capitalizzaParole(String)
	 * @see ImmobileController#riempiTableRisultati(JTable, RicercaDTO)
	 * @see RicercaDTO
	 */
	private void ricercaImmobili() {
		final ImmobileController controller = new ImmobileController();
		final String tipologiaAppartamento = (String) comboBoxAppartamento.getSelectedItem();
		campoPieno = campoRicerca.getText();

		if (campoPieno.equals("Cerca scrivendo una via, una zona o una parola chiave")
				|| campoPieno.equals(campoVuoto)) {
			JOptionPane.showMessageDialog(null, "Scrivere qualcosa prima di iniziare la ricerca!", "Attenzione",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		campoPieno = InputUtils.capitalizzaParole(campoPieno);
		final ViewFiltri viewFiltri = new ViewFiltri(tipologiaAppartamento);
		final Filtri filtri = viewFiltri.getFiltriSelezionati();

		// Creazione del RicercaDTO per cliente
		final RicercaDTO ricercaDTO = new RicercaDTO();
		ricercaDTO.setQueryRicerca(campoPieno);
		ricercaDTO.setEmailUtente(emailCliente);
		ricercaDTO.setTipologiaImmobile(tipologiaAppartamento);
		ricercaDTO.setFiltri(filtri);
		ricercaDTO.setEmailUtente(emailCliente); // Email del cliente
		/* La ricerca degli immobili di un cliente mostra
		 * sempre tutti i risultati di ricerca */
		ricercaDTO.setTipoRicerca(RicercaDTO.TipoRicerca.TUTTI_I_RISULTATI);

		numeroRisultatiTrovati = controller.riempiTableRisultati(tableRisultati, ricercaDTO);
		lblRisultati.setText("Immobili trovati: " + numeroRisultatiTrovati);
	}

	/**
	 * Gestisce il comportamento del campo di testo quando riceve il focus.
	 * Rimuove il placeholder text e cambia lo stile del font.
	 */
	private void setCampoDiTestoIn() {
		campoPieno = campoRicerca.getText();
		if (campoPieno.equals("Cerca scrivendo una via, una zona o una parola chiave")) {
			campoRicerca.setText(campoVuoto);
			campoRicerca.setFont(new Font("Yu Gothic UI Semibold", Font.CENTER_BASELINE, 11));
		}
	}

	/**
	 * Gestisce il comportamento del campo di testo quando perde il focus.
	 * Ripristina il placeholder text se il campo è vuoto.
	 */
	private void setCampoDiTestoOut() {
		campoPieno = campoRicerca.getText();
		if (campoPieno.equals(campoVuoto)) {
			campoRicerca.setText("Cerca scrivendo una via, una zona o una parola chiave");
			campoRicerca.setFont(new Font("Yu Gothic UI Semibold", Font.ITALIC, 11));
		}
	}
}