package gui.utenza;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import controller.AccountController;
import controller.OfferteController;
import util.GuiUtils;

/**
 * Finestra per la visualizzazione dello storico delle offerte proposte da un cliente.
 * Questa interfaccia permette al cliente di consultare tutte le offerte precedentemente
 * inviate, visualizzarne lo stato (accettata, rifiutata, in attesa, controproposta)
 * e interagire con le controproposte degli agenti.
 *
 * <p>La finestra è divisa in due sezioni principali:
 * <ul>
 *   <li>Pannello comandi: contiene titolo, descrizione, pulsante di navigazione e informazioni utente</li>
 *   <li>Tabella risultati: visualizza tutte le offerte con dettagli e stato di avanzamento</li>
 * </ul>
 *
 * <p>Interazioni disponibili:
 * <ul>
 *   <li>Visualizzazione dello stato dell'offerta</li>
 *   <li>Apertura della finestra di gestione controproposte per le offerte in tale stato</li>
 *   <li>Navigazione indietro verso la dashboard</li>
 * </ul>
 *
 * @author IngSW2425_055 Team
 * @see OfferteController
 * @see ViewContropropostaAgente
 */
public class ViewStoricoCliente extends JFrame {

	private static final long serialVersionUID = 1L;
	private final JTable tableStoricoOfferte;
	private final JPanel contentPane;

	/**
	 * Costruttore della finestra di visualizzazione storico cliente.
	 * Inizializza tutti i componenti grafici, configura il layout e carica i dati
	 * delle offerte proposte dal cliente specificato.
	 *
	 * <p>La finestra viene impostata a schermo intero e divisa in due sezioni:
	 * un pannello superiore con comandi e informazioni, e un pannello inferiore
	 * con la tabella delle offerte.
	 *
	 * @param emailUtente Email del cliente di cui visualizzare lo storico offerte.
	 *                    Utilizzata per recuperare i dati dal database e per
	 *                    visualizzare l'identità dell'utente connesso.
	 * @throws IllegalArgumentException Se l'email dell'utente è null o vuota
	 */
	public ViewStoricoCliente(String emailUtente) {
		// Imposta l'icona di DietiEstates25 alla finestra in uso
		GuiUtils.setIconaFinestra(this);
		setTitle("DietiEstates25 - Storico cliente");

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1248, 946);

		// Imposta la finestra a schermo intero
		setExtendedState(JFrame.MAXIMIZED_BOTH);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		final SpringLayout sl_contentPane = new SpringLayout();
		contentPane.setLayout(sl_contentPane);

		// PANNELLO COMANDI
		final JPanel panelComandi = new JPanel();
		sl_contentPane.putConstraint(SpringLayout.NORTH, panelComandi, 0, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, panelComandi, 0, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, panelComandi, 134, SpringLayout.NORTH, contentPane);
		panelComandi.setBackground(new Color(255, 255, 255));
		contentPane.add(panelComandi);
		final SpringLayout sl_panelComandi = new SpringLayout();
		panelComandi.setLayout(sl_panelComandi);

		final JLabel lblTitolo = new JLabel("DietiEstates25");
		sl_panelComandi.putConstraint(SpringLayout.NORTH, lblTitolo, 47, SpringLayout.NORTH, panelComandi);
		sl_panelComandi.putConstraint(SpringLayout.WEST, lblTitolo, 489, SpringLayout.WEST, panelComandi);
		sl_panelComandi.putConstraint(SpringLayout.EAST, lblTitolo, -490, SpringLayout.EAST, panelComandi);
		lblTitolo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitolo.setForeground(new Color(27, 99, 142));
		lblTitolo.setFont(new Font("Tahoma", Font.BOLD, 30));
		panelComandi.add(lblTitolo);

		final JLabel lblDescrizione = new JLabel("Di seguito è riportato l'elenco delle sue offerte proposte");
		sl_panelComandi.putConstraint(SpringLayout.SOUTH, lblTitolo, -13, SpringLayout.NORTH, lblDescrizione);
		sl_panelComandi.putConstraint(SpringLayout.NORTH, lblDescrizione, 97, SpringLayout.NORTH, panelComandi);
		sl_panelComandi.putConstraint(SpringLayout.SOUTH, lblDescrizione, -8, SpringLayout.SOUTH, panelComandi);
		sl_panelComandi.putConstraint(SpringLayout.WEST, lblDescrizione, 220, SpringLayout.WEST, panelComandi);
		sl_panelComandi.putConstraint(SpringLayout.EAST, lblDescrizione, -220, SpringLayout.EAST, panelComandi);
		lblDescrizione.setHorizontalAlignment(SwingConstants.CENTER);
		lblDescrizione.setFont(new Font("Tahoma", Font.PLAIN, 18));
		panelComandi.add(lblDescrizione);

		final JButton btnTornaIndietro = new JButton();
		GuiUtils.setIconaButton(btnTornaIndietro, "Back");
		sl_panelComandi.putConstraint(SpringLayout.NORTH, btnTornaIndietro, 11, SpringLayout.NORTH, panelComandi);
		sl_panelComandi.putConstraint(SpringLayout.WEST, btnTornaIndietro, 10, SpringLayout.WEST, panelComandi);
		sl_panelComandi.putConstraint(SpringLayout.SOUTH, btnTornaIndietro, 39, SpringLayout.NORTH, panelComandi);
		sl_panelComandi.putConstraint(SpringLayout.EAST, btnTornaIndietro, 38, SpringLayout.WEST, panelComandi);
		btnTornaIndietro.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnTornaIndietro.setToolTipText("Clicca qui per tornare alla dashboard");
		btnTornaIndietro.setBorderPainted(false);
		panelComandi.add(btnTornaIndietro);

		final JLabel lblBenvenuto = new JLabel("Accesso effettuato con:");
		sl_panelComandi.putConstraint(SpringLayout.NORTH, lblBenvenuto, 0, SpringLayout.NORTH, btnTornaIndietro);
		sl_panelComandi.putConstraint(SpringLayout.WEST, lblBenvenuto, 951, SpringLayout.EAST, btnTornaIndietro);
		sl_panelComandi.putConstraint(SpringLayout.EAST, lblBenvenuto, -10, SpringLayout.EAST, panelComandi);
		lblBenvenuto.setVerticalAlignment(SwingConstants.TOP);
		lblBenvenuto.setHorizontalAlignment(SwingConstants.RIGHT);
		panelComandi.add(lblBenvenuto);

		final JLabel lblEmailAccesso = new JLabel(emailUtente + " ");
		sl_panelComandi.putConstraint(SpringLayout.SOUTH, lblEmailAccesso, -52, SpringLayout.NORTH, lblDescrizione);
		sl_panelComandi.putConstraint(SpringLayout.NORTH, lblEmailAccesso, 6, SpringLayout.SOUTH, lblBenvenuto);
		sl_panelComandi.putConstraint(SpringLayout.WEST, lblEmailAccesso, 951, SpringLayout.EAST, btnTornaIndietro);
		sl_panelComandi.putConstraint(SpringLayout.EAST, lblEmailAccesso, -10, SpringLayout.EAST, panelComandi);
		lblEmailAccesso.setHorizontalAlignment(SwingConstants.RIGHT);
		lblEmailAccesso.setFont(new Font("Yu Gothic UI Semibold", Font.ITALIC, 11));
		panelComandi.add(lblEmailAccesso);

		// TABELLA RISULTATI
		final JPanel panelRisultati = new JPanel();
		sl_contentPane.putConstraint(SpringLayout.EAST, panelComandi, 0, SpringLayout.EAST, panelRisultati);
		sl_contentPane.putConstraint(SpringLayout.NORTH, panelRisultati, 134, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, panelRisultati, 0, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, panelRisultati, 0, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, panelRisultati, 0, SpringLayout.EAST, contentPane);
		panelRisultati.setBackground(new Color(50, 133, 177));
		contentPane.add(panelRisultati);
		final SpringLayout sl_panelRisultati = new SpringLayout();
		panelRisultati.setLayout(sl_panelRisultati);

		final JScrollPane scrollPane = new JScrollPane();
		sl_panelRisultati.putConstraint(SpringLayout.NORTH, scrollPane, 18, SpringLayout.NORTH, panelRisultati);
		sl_panelRisultati.putConstraint(SpringLayout.WEST, scrollPane, 10, SpringLayout.WEST, panelRisultati);
		sl_panelRisultati.putConstraint(SpringLayout.SOUTH, scrollPane, -10, SpringLayout.SOUTH, panelRisultati);
		sl_panelRisultati.putConstraint(SpringLayout.EAST, scrollPane, -10, SpringLayout.EAST, panelRisultati);
		scrollPane.setBackground(Color.WHITE);
		panelRisultati.add(scrollPane);

		// Crea il modello dati della tabella
		final DefaultTableModel model = new DefaultTableModel(new Object[][] {},
				new String[] { "Foto", "Categoria", "Descrizione", "Data", "Prezzo proposto", "Stato" }) {
			@Override
			public Class<?> getColumnClass(int columnIndex) {
				return switch (columnIndex) {
				case 0 -> String.class; // Base64 immagini
				case 1, 2, 3, 4, 5 -> String.class;
				default -> Object.class;
				};
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		// Crea e configura la JTable
		tableStoricoOfferte = new JTable(model);
		tableStoricoOfferte.setRowHeight(100);
		tableStoricoOfferte.setShowGrid(false);
		tableStoricoOfferte.setShowVerticalLines(false);
		tableStoricoOfferte.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableStoricoOfferte.setSelectionBackground(new Color(226, 226, 226));

		// Configura larghezze colonne
		final int[] preferredWidths = { 100, 100, 400, 120, 120, 80 };
		for (int i = 0; i < preferredWidths.length; i++) {
			final TableColumn col = tableStoricoOfferte.getColumnModel().getColumn(i);
			col.setPreferredWidth(preferredWidths[i]);
			col.setResizable(false);
		}

		// Inserisci la tabella nello scroll pane
		scrollPane.setViewportView(tableStoricoOfferte);

		// Popola la tabella con i dati tramite il controller
		final OfferteController controller = new OfferteController();
		controller.riempiTableOfferteProposte(tableStoricoOfferte, emailUtente);

		/**
		 * Gestisce la selezione di una riga nella tabella delle offerte.
		 * A seconda dello stato dell'offerta selezionata, mostra messaggi informativi
		 * o apre la finestra di gestione controproposte.
		 *
		 * <p>Azioni in base allo stato:
		 * <ul>
		 *   <li>"Accettata": mostra messaggio di conferma</li>
		 *   <li>"Rifiutata": mostra messaggio di rifiuto</li>
		 *   <li>"In attesa": mostra messaggio di attesa</li>
		 *   <li>"Controproposta": apre {@link ViewContropropostaAgente}</li>
		 * </ul>
		 *
		 * @throws SQLException Se si verifica un errore durante il recupero dell'ID cliente
		 */
		tableStoricoOfferte.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				final int selectedRow = tableStoricoOfferte.getSelectedRow();
				if (selectedRow != -1) {
					final String stato = (String) tableStoricoOfferte.getValueAt(selectedRow, 6);

					switch (stato) {
					case "Accettata" -> JOptionPane.showMessageDialog(null, "L'agente ha accettato la sua proposta",
							"Messaggio informativo", JOptionPane.INFORMATION_MESSAGE);
					case "Rifiutata" -> JOptionPane.showMessageDialog(null, "Mi dispiace, la sua proposta è stata rifiutata",
							"Messaggio informativo", JOptionPane.INFORMATION_MESSAGE);
					case "In attesa" -> JOptionPane.showMessageDialog(null,
							"La proposta è ancora in attesa di essere valutata dall'agente",
							"Messaggio informativo", JOptionPane.INFORMATION_MESSAGE);
					case "Controproposta" -> {
						final Long idOfferta = (Long) tableStoricoOfferte.getValueAt(selectedRow, 0);
						String idCliente = "undef";
						try {
							final AccountController controller1 = new AccountController();
							idCliente = controller1.emailToId(emailUtente);
						} catch (SQLException e1) {
							JOptionPane.showMessageDialog(null, "Non è stato possibile recuperare l'id del cliente",
									"Errore", JOptionPane.INFORMATION_MESSAGE);
							e1.printStackTrace();
						}
						final ViewContropropostaAgente viewControproposta = new ViewContropropostaAgente(idOfferta,
								idCliente);
						viewControproposta.setLocationRelativeTo(null);
						viewControproposta.setVisible(true);
					}
					}
				}
			}
		});

	}
}