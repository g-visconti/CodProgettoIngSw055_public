package gui.amministrazione;

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
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import controller.AccountController;
import controller.ImmobileController;
import database.ConnessioneDatabase;
import gui.ViewAccesso;
import gui.ViewFiltri;
import gui.ViewInfoAccount;
import gui.ViewModificaPassword;
import gui.utenza.ViewImmobile;
import model.dto.RicercaDTO;
import model.entity.Filtri;
import util.GuiUtils;
import util.InputUtils;

/**
 * Dashboard principale dell'applicazione DietiEstates25.
 * Questa finestra rappresenta l'interfaccia centrale dopo il login,
 * mostrando funzionalità diverse in base al ruolo dell'utente (Admin, Supporto, Agente).
 *
 * <p>La dashboard include:
 * <ul>
 *   <li>Area di ricerca con filtri avanzati</li>
 *   <li>Tabella dei risultati degli immobili</li>
 *   <li>Funzionalità specifiche per ruolo (aggiunta utenti, caricamento immobili, ecc.)</li>
 *   <li>Menu utente per la gestione del profilo</li>
 *   <li>Pulsanti per offerte proposte e caricamento immobili</li>
 * </ul>
 *
 * @author IngSW2425_055 Team
 * @see AccountController
 * @see ImmobileController
 * @see ViewFiltri
 */
public class ViewDashboardDietiEstates extends JFrame {

	private static final long serialVersionUID = 1L;
	// attributi
	private String idAgente = null;
	private final String emailAgente;
	private String ruoloDietiEstates = "non definito";
	private JTextField campoRicerca;
	private String campoPieno;
	private final String campoVuoto = "";
	private JTable tableRisultati;
	private JLabel lblLogout;
	private JLabel lblRisultati;
	private ViewFiltri viewFiltri;
	private JComboBox<String> comboBoxAppartamento;
	private JComboBox<String> comboBoxFiltraImmobili;
	private int numeroRisultatiTrovati;
	private JLabel lblEmailAccesso;

	// costruttore

	/**
	 * Costruttore della dashboard principale.
	 * Inizializza l'interfaccia grafica e configura i componenti in base al ruolo dell'utente.
	 *
	 * <p>La dashboard viene configurata dinamicamente in base al ruolo dell'utente:
	 * <ul>
	 *   <li><b>Admin</b>: Accesso completo a tutte le funzionalità, inclusa l'aggiunta di altri admin</li>
	 *   <li><b>Supporto</b>: Può aggiungere agenti immobiliari</li>
	 *   <li><b>Agente</b>: Accesso alle funzionalità base e gestione dei propri immobili</li>
	 * </ul>
	 *
	 * @param emailAgente Email dell'utente che accede alla dashboard.
	 *                    Utilizzata per determinare il ruolo e personalizzare l'interfaccia.
	 * @throws SQLException Se si verificano errori nel recupero delle informazioni dal database
	 */
	public ViewDashboardDietiEstates(String emailAgente) {
		// Imposta l'icona di DietiEstates25 alla finestra in uso
		GuiUtils.setIconaFinestra(this);

		setResizable(true);
		Preferences.userNodeForPackage(ViewFiltri.class);

		this.emailAgente = emailAgente;

		// Opzioni per le comboBox
		final String[] opAppartamento = { "Vendita", "Affitto" };
		final String[] opFiltraImmobili = { "Tutti i risultati", "I miei immobili" };

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 1382, 794);

		// Imposta la finestra a schermo intero
		setExtendedState(JFrame.MAXIMIZED_BOTH);

		final JPanel dashboard = new JPanel();
		dashboard.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 11));
		dashboard.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(dashboard);
		dashboard.setLayout(null);
		final SpringLayout sl_dashboard = new SpringLayout();
		dashboard.setLayout(sl_dashboard);

		// Definisco la dashborad da visualizzare in base al ruolo di amministrazione
		final AccountController controller = new AccountController();
		try {
			ruoloDietiEstates = controller.getRuoloByEmail(emailAgente);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Non è stato possibile recuperare il ruolo", "Errore",
					JOptionPane.INFORMATION_MESSAGE);
			e.printStackTrace();
		}

		// Crea il pannello di ricerca comune a tutti i ruoli
		final JPanel ricerca = createRicercaPanel(emailAgente, opAppartamento, opFiltraImmobili, sl_dashboard, dashboard);

		// Configurazioni specifiche per ruolo
		switch (ruoloDietiEstates) {
		case "Admin", "Supporto" -> // Per Admin e Supporto aggiungiamo il menu per aggiungere utenti
		setupAdminSupportoUI(ricerca, emailAgente, ruoloDietiEstates);
		case "Agente" -> // Per Agente configuriamo l'action listener specifico
		setupAgenteUI(ricerca, emailAgente);
		default -> dispose();
		}

		// Pannello per i risultati della ricerca
		setupRisultatiPanel(emailAgente, sl_dashboard, dashboard);
	}

	// metodi

	/**
	 * Crea e configura il pannello di ricerca principale.
	 * Questo pannello contiene tutti i controlli per eseguire ricerche di immobili:
	 * campo di testo, combo box per tipologia, filtri e pulsante di ricerca.
	 *
	 * <p>Il pannello include:
	 * <ul>
	 *   <li>Campo di ricerca con testo placeholder</li>
	 *   <li>Combo box per selezionare tipologia (Vendita/Affitto)</li>
	 *   <li>Combo box per filtrare risultati (Tutti/I miei immobili)</li>
	 *   <li>Icona per filtri avanzati</li>
	 *   <li>Pulsante di esecuzione ricerca</li>
	 *   <li>Informazioni utente e logout</li>
	 * </ul>
	 *
	 * @param emailAgente Email dell'utente per personalizzare le etichette
	 * @param opAppartamento Array di opzioni per la combo box di tipologia
	 * @param opFiltraImmobili Array di opzioni per il filtraggio risultati
	 * @param sl_dashboard Layout della dashboard principale
	 * @param dashboard Pannello contenitore principale
	 * @return JPanel configurato per la ricerca
	 *
	 * @see GuiUtils#setIconaLabel(JLabel, String)
	 * @see GuiUtils#setIconaButton(JButton, String)
	 */
	private JPanel createRicercaPanel(String emailAgente, String[] opAppartamento, String[] opFiltraImmobili,
			SpringLayout sl_dashboard, JPanel dashboard) {
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
		sl_ricerca.putConstraint(SpringLayout.EAST, campoRicerca, -355, SpringLayout.EAST, ricerca);
		sl_ricerca.putConstraint(SpringLayout.HORIZONTAL_CENTER, campoRicerca, 0, SpringLayout.HORIZONTAL_CENTER,
				ricerca);
		sl_ricerca.putConstraint(SpringLayout.VERTICAL_CENTER, campoRicerca, 0, SpringLayout.VERTICAL_CENTER, ricerca);
		ricerca.setLayout(sl_ricerca);

		// ComboBox appartamento
		comboBoxAppartamento = new JComboBox<>(opAppartamento);
		sl_ricerca.putConstraint(SpringLayout.WEST, comboBoxAppartamento, 248, SpringLayout.WEST, ricerca);
		sl_ricerca.putConstraint(SpringLayout.EAST, comboBoxAppartamento, -23, SpringLayout.WEST, campoRicerca);
		sl_ricerca.putConstraint(SpringLayout.SOUTH, campoRicerca, 0, SpringLayout.SOUTH, comboBoxAppartamento);
		sl_ricerca.putConstraint(SpringLayout.SOUTH, comboBoxAppartamento, -70, SpringLayout.SOUTH, ricerca);
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
		lblLogout.setToolTipText("Clicca per uscire da DietiEstates25");
		ricerca.add(lblLogout);
		GuiUtils.setIconaLabel(lblLogout, "Logout");

		// Bottone ricerca
		final JButton btnEseguiRicerca = new JButton();
		GuiUtils.setIconaButton(btnEseguiRicerca, "Search");
		btnEseguiRicerca.setBorderPainted(false);
		btnEseguiRicerca.setFocusPainted(false);
		btnEseguiRicerca.setContentAreaFilled(false);

		sl_ricerca.putConstraint(SpringLayout.SOUTH, btnEseguiRicerca, -70, SpringLayout.SOUTH, ricerca);
		sl_ricerca.putConstraint(SpringLayout.EAST, btnEseguiRicerca, -255, SpringLayout.EAST, ricerca);

		getRootPane().setDefaultButton(btnEseguiRicerca);
		btnEseguiRicerca.setToolTipText("Clicca per iniziare una ricerca");
		btnEseguiRicerca.setBorderPainted(false);

		ricerca.add(btnEseguiRicerca);

		// Label utente
		final JLabel lblUser = new JLabel("");
		sl_ricerca.putConstraint(SpringLayout.SOUTH, lblLogout, 0, SpringLayout.SOUTH, lblUser);
		sl_ricerca.putConstraint(SpringLayout.NORTH, lblUser, 8, SpringLayout.NORTH, ricerca);
		sl_ricerca.putConstraint(SpringLayout.NORTH, lblLogout, 0, SpringLayout.NORTH, lblUser);
		sl_ricerca.putConstraint(SpringLayout.WEST, lblLogout, 11, SpringLayout.EAST, lblUser);
		lblUser.setToolTipText("Clicca per vedere altre ozioni sul profilo");
		ricerca.add(lblUser);

		// Menu utente
		setupUserMenu(lblUser, emailAgente);
		GuiUtils.setIconaLabel(lblUser, "User");

		// Titolo
		final JLabel lblTitolo = new JLabel("DietiEstates25");
		sl_ricerca.putConstraint(SpringLayout.NORTH, lblTitolo, 0, SpringLayout.NORTH, ricerca);
		sl_ricerca.putConstraint(SpringLayout.WEST, lblTitolo, 10, SpringLayout.WEST, ricerca);
		sl_ricerca.putConstraint(SpringLayout.SOUTH, lblTitolo, -145, SpringLayout.SOUTH, ricerca);
		sl_ricerca.putConstraint(SpringLayout.EAST, lblTitolo, -1008, SpringLayout.EAST, ricerca);
		lblTitolo.setForeground(new Color(27, 99, 142));
		lblTitolo.setHorizontalAlignment(SwingConstants.LEFT);
		lblTitolo.setFont(new Font("Tahoma", Font.BOLD, 30));
		ricerca.add(lblTitolo);

		// Labels benvenuto e email
		setupBenvenutoLabels(ricerca, sl_ricerca, lblTitolo, emailAgente, btnEseguiRicerca);

		// Filtri
		final JLabel lblFiltri = new JLabel();
		sl_ricerca.putConstraint(SpringLayout.WEST, lblFiltri, 23, SpringLayout.EAST, campoRicerca);
		sl_ricerca.putConstraint(SpringLayout.EAST, lblFiltri, -302, SpringLayout.EAST, ricerca);
		sl_ricerca.putConstraint(SpringLayout.WEST, btnEseguiRicerca, 17, SpringLayout.EAST, lblFiltri);
		sl_ricerca.putConstraint(SpringLayout.NORTH, lblFiltri, 50, SpringLayout.SOUTH, getLblEmailAccesso());
		sl_ricerca.putConstraint(SpringLayout.SOUTH, lblFiltri, -70, SpringLayout.SOUTH, ricerca);
		lblFiltri.setToolTipText("Clicca per impostare preferenze aggiuntive, poi esegui la ricerca");

		lblFiltri.addMouseListener(new MouseAdapter() {
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

		// Combobox per il filtraggio degli immobili
		comboBoxFiltraImmobili = new JComboBox<>(opFiltraImmobili);
		sl_ricerca.putConstraint(SpringLayout.NORTH, comboBoxFiltraImmobili, 0, SpringLayout.NORTH,
				comboBoxAppartamento);
		sl_ricerca.putConstraint(SpringLayout.WEST, comboBoxFiltraImmobili, 84, SpringLayout.WEST, ricerca);
		sl_ricerca.putConstraint(SpringLayout.SOUTH, comboBoxFiltraImmobili, 0, SpringLayout.SOUTH,
				comboBoxAppartamento);
		sl_ricerca.putConstraint(SpringLayout.EAST, comboBoxFiltraImmobili, -40, SpringLayout.WEST,
				comboBoxAppartamento);
		comboBoxFiltraImmobili.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 11));
		comboBoxFiltraImmobili.setFocusable(false);
		comboBoxFiltraImmobili.setForeground(new Color(0, 0, 51));
		comboBoxFiltraImmobili.setBackground(new Color(255, 255, 255));
		comboBoxFiltraImmobili.setToolTipText("Filtra o ricerca tutti gli immobili");
		ricerca.add(comboBoxFiltraImmobili);

		return ricerca;
	}

	/**
	 * Configura l'interfaccia utente per i ruoli Admin e Supporto.
	 * Aggiunge le funzionalità specifiche per questi ruoli, come la possibilità
	 * di aggiungere nuovi utenti al sistema.
	 *
	 * <p>Le differenze tra Admin e Supporto:
	 * <ul>
	 *   <li><b>Admin</b>: Può aggiungere sia amministratori di supporto che agenti</li>
	 *   <li><b>Supporto</b>: Può aggiungere solo agenti immobiliari</li>
	 * </ul>
	 *
	 * @param ricerca Pannello di ricerca dove aggiungere le funzionalità
	 * @param emailAgente Email dell'utente corrente
	 * @param ruolo Ruolo dell'utente (Admin o Supporto)
	 *
	 * @see ViewInserimentoEmail
	 * @see AccountController#getAgenzia(String)
	 */
	private void setupAdminSupportoUI(JPanel ricerca, String emailAgente, String ruolo) {
		final SpringLayout sl_ricerca = (SpringLayout) ricerca.getLayout();

		// Menu per aggiungere utenti (solo per Admin e Supporto)
		final JPopupMenu menuAggiungiUtente = new JPopupMenu();

		// Solo gli Admin possono aggiungere altri admin di supporto
		if ("Admin".equals(ruolo)) {
			setTitle("DietiEstates25 - Dashboard per l'Admin");

			final JMenuItem amministratoreDiSupporto = new JMenuItem("Aggiungi un nuovo amministratore di supporto");
			menuAggiungiUtente.add(amministratoreDiSupporto);

			amministratoreDiSupporto.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							final AccountController con2 = new AccountController();
							final String agenzia = con2.getAgenzia(emailAgente);

							final ViewInserimentoEmail view = new ViewInserimentoEmail(agenzia,
									ViewInserimentoEmail.TipoInserimento.SUPPORTO);
							view.setLocationRelativeTo(null);
							view.setVisible(true);
						}
					});
				}
			});
		} else {
			setTitle("DietiEstates25 - Dashboard per l'agente di supporto");
		}

		// Sia Admin che Supporto possono aggiungere agenti immobiliari
		final JMenuItem agente = new JMenuItem("Aggiungi un nuovo agente immobiliare");
		menuAggiungiUtente.add(agente);

		agente.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						final AccountController con1 = new AccountController();
						final String agenzia = con1.getAgenzia(emailAgente);

						final ViewInserimentoEmail view = new ViewInserimentoEmail(agenzia,
								ViewInserimentoEmail.TipoInserimento.AGENTE);
						view.setLocationRelativeTo(null);
						view.setVisible(true);
					}
				});
			}
		});

		final JLabel lblAggiungiUtente = new JLabel();
		sl_ricerca.putConstraint(SpringLayout.EAST, lblAggiungiUtente, -95, SpringLayout.EAST, ricerca);
		// Trova lblUser per impostare i constraint relativi
		final JLabel lblUser = findLabelByTooltip(ricerca, "Clicca per vedere altre ozioni sul profilo");
		if (lblUser != null) {
			sl_ricerca.putConstraint(SpringLayout.WEST, lblUser, 6, SpringLayout.EAST, lblAggiungiUtente);
		}
		sl_ricerca.putConstraint(SpringLayout.NORTH, lblAggiungiUtente, 11, SpringLayout.NORTH, ricerca);

		// Trova lblBenvenuto per impostare i constraint relativi
		final JLabel lblBenvenuto = findLabelByText(ricerca, "Accesso effettuato con:");
		if (lblBenvenuto != null) {
			sl_ricerca.putConstraint(SpringLayout.EAST, lblBenvenuto, -49, SpringLayout.WEST, lblAggiungiUtente);
		}

		lblAggiungiUtente.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mousePressed(java.awt.event.MouseEvent evt) {
				if (evt.getButton() == java.awt.event.MouseEvent.BUTTON1) {
					menuAggiungiUtente.show(lblAggiungiUtente, evt.getX(), evt.getY());
				}
			}
		});

		// Aggiorna il tooltip in base al ruolo
		final String tooltip;
		if ("Admin".equals(ruolo)) {
			tooltip = "Clicca per aggiungere un nuovo agente o amministratore di supporto";
		} else {
			tooltip = "Clicca per aggiungere un nuovo agente immobiliare";
		}
		lblAggiungiUtente.setToolTipText(tooltip);

		ricerca.add(lblAggiungiUtente);
		GuiUtils.setIconaLabel(lblAggiungiUtente, "AddUser");

		// Configura il listener del bottone ricerca per Admin/Supporto
		final JButton btnEseguiRicerca = findButtonByTooltip(ricerca, "Clicca per iniziare una ricerca");
		if (btnEseguiRicerca != null) {
			btnEseguiRicerca.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ricercaImmobili();
				}
			});
		}
	}

	/**
	 * Configura l'interfaccia utente per il ruolo Agente.
	 * Personalizza la dashboard con le funzionalità specifiche per gli agenti immobiliari,
	 * come il caricamento di nuovi immobili e la visualizzazione delle offerte proposte.
	 *
	 * @param ricerca Pannello di ricerca da configurare
	 * @param emailAgente Email dell'agente corrente
	 *
	 * @see #ricercaImmobili()
	 * @see #ottieniIdAccount(String)
	 */
	private void setupAgenteUI(JPanel ricerca, String emailAgente) {
		setTitle("DietiEstates25 - Dashboard per l'Agente immobiliare");

		final SpringLayout sl_ricerca = (SpringLayout) ricerca.getLayout();

		// Configurazioni specifiche per Agente
		final JLabel lblUser = findLabelByTooltip(ricerca, "Clicca per vedere altre ozioni sul profilo");
		if (lblUser != null) {
			sl_ricerca.putConstraint(SpringLayout.EAST, lblUser, -89, SpringLayout.EAST, ricerca);
		}

		final JLabel lblBenvenuto = findLabelByText(ricerca, "Accesso effettuato con:");
		if (lblBenvenuto != null) {
			sl_ricerca.putConstraint(SpringLayout.EAST, lblBenvenuto, -144, SpringLayout.EAST, ricerca);
		}

		// Configura il listener del bottone ricerca per Agente
		final JButton btnEseguiRicerca = findButtonByTooltip(ricerca, "Clicca per iniziare una ricerca");
		if (btnEseguiRicerca != null) {
			btnEseguiRicerca.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ottieniIdAccount(emailAgente);
					ViewDashboardDietiEstates.this.ricercaImmobili();
				}
			});
		}
	}

	/**
	 * Configura il pannello per la visualizzazione dei risultati della ricerca.
	 * Questo pannello contiene una tabella per mostrare gli immobili trovati
	 * e pulsanti per azioni specifiche (caricamento immobili, visualizzazione offerte).
	 *
	 * <p>Il pannello include:
	 * <ul>
	 *   <li>Tabella scrollabile con i risultati</li>
	 *   <li>Etichetta con il conteggio dei risultati</li>
	 *   <li>Pulsante "Carica immobile" (solo per agenti)</li>
	 *   <li>Pulsante "Vedi offerte proposte"</li>
	 * </ul>
	 *
	 * @param emailAgente Email dell'utente per configurare i listener
	 * @param sl_dashboard Layout della dashboard principale
	 * @param dashboard Pannello contenitore principale
	 *
	 * @see ViewImmobile
	 * @see ViewCaricaImmobile
	 * @see ViewStoricoAgente
	 */
	private void setupRisultatiPanel(String emailAgente, SpringLayout sl_dashboard, JPanel dashboard) {
		// Pannello per i risultati della ricerca
		final JPanel risultatiDaRicerca = new JPanel();
		sl_dashboard.putConstraint(SpringLayout.NORTH, risultatiDaRicerca, 190, SpringLayout.NORTH, dashboard);
		sl_dashboard.putConstraint(SpringLayout.WEST, risultatiDaRicerca, 0, SpringLayout.WEST, dashboard);
		sl_dashboard.putConstraint(SpringLayout.SOUTH, risultatiDaRicerca, 0, SpringLayout.SOUTH, dashboard);
		sl_dashboard.putConstraint(SpringLayout.EAST, risultatiDaRicerca, 0, SpringLayout.EAST, dashboard);
		risultatiDaRicerca.setBackground(new Color(50, 133, 177));
		dashboard.add(risultatiDaRicerca);
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
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					ottieniIdAccount(emailAgente);

					final int row = tableRisultati.rowAtPoint(e.getPoint());
					if (row >= 0) {
						final long idImmobile = (Long) tableRisultati.getValueAt(row, 0);
						final ViewImmobile finestra = new ViewImmobile(idImmobile, idAgente);
						finestra.setLocationRelativeTo(null);
						finestra.setVisible(true);
					}
				}
			}
		});

		scrollPane.setViewportView(tableRisultati);

		lblRisultati = new JLabel("Avvia una ricerca per vedere i risultati");
		sl_risultatiDaRicerca.putConstraint(SpringLayout.NORTH, lblRisultati, 5, SpringLayout.NORTH,
				risultatiDaRicerca);
		sl_risultatiDaRicerca.putConstraint(SpringLayout.WEST, lblRisultati, 20, SpringLayout.WEST, risultatiDaRicerca);
		sl_risultatiDaRicerca.putConstraint(SpringLayout.SOUTH, lblRisultati, -10, SpringLayout.NORTH, scrollPane);
		lblRisultati.setHorizontalAlignment(SwingConstants.LEFT);
		lblRisultati.setForeground(Color.WHITE);
		lblRisultati.setFont(new Font("Tahoma", Font.BOLD, 20));
		risultatiDaRicerca.add(lblRisultati);

		// Vedi Offerte Proposte
		final JButton btnVediOfferteProposte = new JButton("Vedi offerte proposte");
		btnVediOfferteProposte.setFocusable(false);
		sl_risultatiDaRicerca.putConstraint(SpringLayout.NORTH, btnVediOfferteProposte, 8, SpringLayout.NORTH,
				lblRisultati);
		sl_risultatiDaRicerca.putConstraint(SpringLayout.EAST, btnVediOfferteProposte, -42, SpringLayout.EAST,
				risultatiDaRicerca);
		btnVediOfferteProposte.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final ViewStoricoAgente viewOfferte = new ViewStoricoAgente(emailAgente);
				viewOfferte.setLocationRelativeTo(null);
				viewOfferte.setVisible(true);
			}
		});

		btnVediOfferteProposte.setToolTipText("Clicca per visualizzare tutte le offerte proposte");
		btnVediOfferteProposte.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 11));
		btnVediOfferteProposte.setBackground(Color.WHITE);
		risultatiDaRicerca.add(btnVediOfferteProposte);

		// Carica un immobile
		final JButton btnCaricaImmobile = new JButton("Carica immobile");
		sl_risultatiDaRicerca.putConstraint(SpringLayout.NORTH, btnCaricaImmobile, 8, SpringLayout.NORTH, lblRisultati);
		sl_risultatiDaRicerca.putConstraint(SpringLayout.EAST, btnCaricaImmobile, -18, SpringLayout.WEST,
				btnVediOfferteProposte);
		btnCaricaImmobile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				final ViewCaricaImmobile viewCaricaImmobile = new ViewCaricaImmobile(emailAgente);
				viewCaricaImmobile.setLocationRelativeTo(null);
				viewCaricaImmobile.setVisible(true);
			}
		});

		btnCaricaImmobile.setToolTipText("Clicca per inserire un nuovo immobile");
		btnCaricaImmobile.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 11));
		btnCaricaImmobile.setBackground(Color.WHITE);
		risultatiDaRicerca.add(btnCaricaImmobile);
	}

	/**
	 * Configura i listener per il campo di ricerca.
	 * Gestisce il comportamento del campo di testo quando viene cliccato o quando
	 * l'utente inizia a digitare, mostrando/nascondendo il testo placeholder.
	 *
	 * @param ricerca Pannello di ricerca a cui aggiungere i listener
	 *
	 * @see KeyAdapter
	 * @see MouseAdapter
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
	 * Configura il listener per l'etichetta di logout.
	 * Gestisce il processo di logout dell'utente, chiudendo la connessione
	 * al database e tornando alla schermata di accesso.
	 *
	 * @param logoutLabel Etichetta a cui associare il listener di logout
	 *
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
	 * Configura il menu contestuale per l'utente.
	 * Crea un popup menu che appare cliccando sull'icona utente,
	 * offrendo opzioni per visualizzare informazioni account e modificare la password.
	 *
	 * @param userLabel Etichetta dell'utente a cui associare il menu
	 * @param emailAgente Email dell'utente per passare alle view di gestione
	 *
	 * @see ViewInfoAccount
	 * @see ViewModificaPassword
	 */
	private void setupUserMenu(JLabel userLabel, String emailAgente) {
		final JPopupMenu menuUtente = new JPopupMenu();
		final JMenuItem visualizzaInfoAccount = new JMenuItem("Visualizza informazioni sull'account");
		final JMenuItem modificaPassword = new JMenuItem("Modifica password");

		menuUtente.add(visualizzaInfoAccount);
		menuUtente.add(modificaPassword);

		visualizzaInfoAccount.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final ViewInfoAccount viewAccount = new ViewInfoAccount(emailAgente);
				viewAccount.setLocationRelativeTo(null);
				viewAccount.setVisible(true);
			}
		});

		modificaPassword.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final ViewModificaPassword viewModificaPassword = new ViewModificaPassword(emailAgente);
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
	 * Configura le etichette di benvenuto e email nel pannello di ricerca.
	 * Mostra l'email dell'utente corrente e un messaggio di benvenuto personalizzato.
	 *
	 * @param ricerca Pannello di ricerca dove aggiungere le etichette
	 * @param sl_ricerca Layout del pannello di ricerca
	 * @param lblTitolo Etichetta del titolo per il posizionamento relativo
	 * @param emailAgente Email da visualizzare
	 * @param btnEseguiRicerca Pulsante di ricerca per il posizionamento relativo
	 */
	private void setupBenvenutoLabels(JPanel ricerca, SpringLayout sl_ricerca, JLabel lblTitolo, String emailAgente,
			JButton btnEseguiRicerca) {
		final JLabel lblBenvenuto = new JLabel("Accesso effettuato con:");
		sl_ricerca.putConstraint(SpringLayout.NORTH, lblBenvenuto, 11, SpringLayout.NORTH, ricerca);
		sl_ricerca.putConstraint(SpringLayout.WEST, lblBenvenuto, 601, SpringLayout.EAST, lblTitolo);
		lblBenvenuto.setHorizontalAlignment(SwingConstants.RIGHT);
		lblBenvenuto.setVerticalAlignment(SwingConstants.TOP);
		ricerca.add(lblBenvenuto);

		lblEmailAccesso = new JLabel("<email>");
		sl_ricerca.putConstraint(SpringLayout.NORTH, btnEseguiRicerca, 50, SpringLayout.SOUTH, lblEmailAccesso);
		sl_ricerca.putConstraint(SpringLayout.NORTH, campoRicerca, 50, SpringLayout.SOUTH, lblEmailAccesso);
		sl_ricerca.putConstraint(SpringLayout.NORTH, comboBoxAppartamento, 50, SpringLayout.SOUTH, lblEmailAccesso);
		sl_ricerca.putConstraint(SpringLayout.NORTH, lblEmailAccesso, 1, SpringLayout.SOUTH, lblBenvenuto);
		sl_ricerca.putConstraint(SpringLayout.WEST, lblEmailAccesso, 532, SpringLayout.EAST, lblTitolo);

		// Trova lblUser per impostare il constraint SOUTH
		final JLabel lblUser = findLabelByTooltip(ricerca, "Clicca per vedere altre ozioni sul profilo");
		if (lblUser != null) {
			sl_ricerca.putConstraint(SpringLayout.SOUTH, lblEmailAccesso, 0, SpringLayout.SOUTH, lblUser);
		}

		sl_ricerca.putConstraint(SpringLayout.EAST, lblEmailAccesso, 0, SpringLayout.EAST, lblBenvenuto);
		lblEmailAccesso.setHorizontalAlignment(SwingConstants.RIGHT);
		lblEmailAccesso.setText(emailAgente + " ");
		lblEmailAccesso.setFont(new Font("Yu Gothic UI Semibold", Font.ITALIC, 11));
		ricerca.add(lblEmailAccesso);
	}

	/**
	 * Restituisce l'etichetta contenente l'email dell'utente corrente.
	 *
	 * @return JLabel con l'email dell'utente
	 */
	private JLabel getLblEmailAccesso() {
		return lblEmailAccesso;
	}

	// Metodi helper per trovare componenti

	/**
	 * Cerca un'etichetta nel pannello specificato in base al testo del tooltip.
	 *
	 * @param panel Pannello in cui cercare
	 * @param tooltip Testo del tooltip da cercare
	 * @return JLabel trovata o null se non trovata
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
	 * Cerca un'etichetta nel pannello specificato in base al testo.
	 *
	 * @param panel Pannello in cui cercare
	 * @param text Testo da cercare
	 * @return JLabel trovata o null se non trovata
	 */
	private JLabel findLabelByText(JPanel panel, String text) {
		for (Component comp : panel.getComponents()) {
			if (comp instanceof JLabel label) {
				if (text.equals(label.getText())) {
					return label;
				}
			}
		}
		return null;
	}

	/**
	 * Cerca un pulsante nel pannello specificato in base al testo del tooltip.
	 *
	 * @param panel Pannello in cui cercare
	 * @param tooltip Testo del tooltip da cercare
	 * @return JButton trovato o null se non trovato
	 */
	private JButton findButtonByTooltip(JPanel panel, String tooltip) {
		for (Component comp : panel.getComponents()) {
			if (comp instanceof JButton button) {
				if (tooltip.equals(button.getToolTipText())) {
					return button;
				}
			}
		}
		return null;
	}

	/**
	 * Recupera l'ID account associato all'email specificata.
	 * Il risultato viene memorizzato nella variabile d'istanza idAgente
	 * per utilizzi successivi nell'applicazione.
	 *
	 * @param emailAgente Email dell'agente di cui recuperare l'ID
	 * @throws SQLException Se si verificano errori nel recupero dal database
	 *
	 * @see AccountController#getIdAccountByEmail(String)
	 */
	private void ottieniIdAccount(String emailAgente) {
		if (idAgente == null) {
			final AccountController controller = new AccountController();
			try {
				idAgente = controller.getIdAccountByEmail(emailAgente);
			} catch (SQLException ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(this, "Errore nel recupero dell'ID account: " + ex.getMessage(), "Errore",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Esegue una ricerca di immobili in base ai criteri selezionati.
	 * Recupera i parametri dai controlli dell'interfaccia, crea un DTO di ricerca
	 * e popola la tabella dei risultati con gli immobili trovati.
	 *
	 * <p>La ricerca considera:
	 * <ul>
	 *   <li>Testo inserito nel campo di ricerca</li>
	 *   <li>Tipologia selezionata (Vendita/Affitto)</li>
	 *   <li>Filtri avanzati impostati</li>
	 *   <li>Tipo di ricerca (Tutti i risultati/I miei immobili)</li>
	 * </ul>
	 *
	 * @throws IllegalArgumentException Se il campo di ricerca è vuoto o contiene il testo placeholder
	 *
	 * @see ImmobileController#riempiTableRisultati(JTable, RicercaDTO)
	 * @see InputUtils#capitalizzaParole(String)
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

		// Creazione del RicercaDTO
		final RicercaDTO ricercaDTO = new RicercaDTO();
		ricercaDTO.setQueryRicerca(campoPieno);
		ricercaDTO.setEmailUtente(emailAgente);
		ricercaDTO.setTipologiaImmobile(tipologiaAppartamento);
		ricercaDTO.setFiltri(filtri);
		ricercaDTO.setEmailUtente(emailAgente); // Email dell'agente corrente

		// Determina il tipo di ricerca in base alla combobox
		final String selezioneFiltro = (String) comboBoxFiltraImmobili.getSelectedItem();
		if ("I miei immobili".equals(selezioneFiltro)) {
			ricercaDTO.setTipoRicerca(RicercaDTO.TipoRicerca.I_MIEI_IMMOBILI);
		} else {
			ricercaDTO.setTipoRicerca(RicercaDTO.TipoRicerca.TUTTI_I_RISULTATI);
		}

		// Chiamata al controller con il DTO
		numeroRisultatiTrovati = controller.riempiTableRisultati(tableRisultati, ricercaDTO);
		lblRisultati.setText("Immobili trovati: " + numeroRisultatiTrovati);
	}

	/**
	 * Gestisce il comportamento del campo di ricerca quando viene cliccato o inizia la digitazione.
	 * Rimuove il testo placeholder se presente.
	 */
	private void setCampoDiTestoIn() {
		campoPieno = campoRicerca.getText();
		if (campoPieno.equals("Cerca scrivendo una via, una zona o una parola chiave")) {
			campoRicerca.setText(campoVuoto);
			campoRicerca.setFont(new Font("Yu Gothic UI Semibold", Font.CENTER_BASELINE, 11));
		}
	}

	/**
	 * Gestisce il comportamento del campo di ricerca quando perde il focus.
	 * Ripristina il testo placeholder se il campo è vuoto.
	 */
	private void setCampoDiTestoOut() {
		campoPieno = campoRicerca.getText();
		if (campoPieno.equals(campoVuoto)) {
			campoRicerca.setText("Cerca scrivendo una via, una zona o una parola chiave");
			campoRicerca.setFont(new Font("Yu Gothic UI Semibold", Font.ITALIC, 11));
		}
	}
}