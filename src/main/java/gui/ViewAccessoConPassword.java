package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import controller.AccessController;
import controller.AccountController;
import gui.amministrazione.ViewDashboardDietiEstates;
import gui.utenza.ViewDashboard;
import util.GuiUtils;

/**
 * Finestra per l'autenticazione tramite email e password in DietiEstates25.
 * Questa interfaccia permette agli utenti già registrati di accedere al sistema
 * inserendo la propria password dopo aver specificato l'email nella schermata precedente.
 *
 * <p>La finestra è divisa in due sezioni principali:
 * <ul>
 *   <li>Pannello sinistro: contiene logo e titolo dell'applicazione</li>
 *   <li>Pannello destro: contiene il campo password, l'email confermata e i pulsanti di controllo</li>
 * </ul>
 *
 * <p>Caratteristiche del campo password:
 * <ul>
 *   <li>Placeholder "******" che scompare all'interazione</li>
 *   <li>Gestione intelligente del focus e del contenuto</li>
 *   <li>Reset automatico quando si clicca fuori dal campo se vuoto</li>
 * </ul>
 *
 * <p>Flussi di autenticazione in base al ruolo:
 * <ul>
 *   <li>Cliente: redirezione a {@link ViewDashboard}</li>
 *   <li>Agente/Supporto/Admin: redirezione a {@link ViewDashboardDietiEstates}</li>
 * </ul>
 *
 * @author IngSW2425_055 Team
 * @see AccessController
 * @see AccountController
 * @see ViewAccesso
 * @see ViewDashboard
 * @see ViewDashboardDietiEstates
 */
public class ViewAccessoConPassword extends JFrame {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPane;
	private final JTextField txtAccediORegistrati;
	private String campoPieno = "******";
	private final String campoVuoto = "";
	private final JPasswordField passwordField;

	/**
	 * Costruttore della finestra di accesso con password.
	 * Inizializza tutti i componenti grafici e configura i listener per la gestione
	 * dell'autenticazione tramite password.
	 *
	 * <p>La finestra mostra l'email precedentemente inserita dall'utente e richiede
	 * l'inserimento della password corrispondente per completare l'accesso.
	 *
	 * @param emailInserita Email dell'utente che sta tentando l'accesso.
	 *                      Viene visualizzata nella finestra per conferma e
	 *                      utilizzata per la verifica delle credenziali.
	 * @throws IllegalArgumentException Se l'email è null o vuota
	 */
	public ViewAccessoConPassword(String emailInserita) {
		// Imposta l'icona di DietiEstates25 alla finestra in uso
		GuiUtils.setIconaFinestra(this);

		setTitle("DietiEstates25 - Accedi con email e password");
		setResizable(false);

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 818, 618);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		final JPanel panelLogo = new JPanel();
		panelLogo.setBackground(new Color(255, 255, 255));
		panelLogo.setBounds(0, 0, 450, 593);
		contentPane.add(panelLogo);
		panelLogo.setLayout(null);

		final JLabel lblLogoDieti = new JLabel("New label");
		lblLogoDieti.setBounds(95, 176, 259, 249);
		panelLogo.add(lblLogoDieti);
		lblLogoDieti.setOpaque(true);

		final URL pathlogo = getClass().getClassLoader().getResource("images/DietiEstatesLogo.png");
		lblLogoDieti.setIcon(new ImageIcon(pathlogo));

		final JLabel lblDieti = new JLabel("DietiEstates25");
		lblDieti.setBounds(2, 39, 445, 32);
		panelLogo.add(lblDieti);
		lblDieti.setHorizontalAlignment(SwingConstants.CENTER);
		lblDieti.setForeground(new Color(27, 99, 142));
		lblDieti.setFont(new Font("Tahoma", Font.BOLD, 30));

		final JPanel panelAccesso = new JPanel();

		panelAccesso.setBackground(new Color(245, 245, 245));
		panelAccesso.setBounds(449, 0, 367, 593);
		contentPane.add(panelAccesso);
		panelAccesso.setLayout(null);

		txtAccediORegistrati = new JTextField();
		txtAccediORegistrati.setOpaque(false);
		txtAccediORegistrati.setFocusable(false);
		txtAccediORegistrati.setEditable(false);
		txtAccediORegistrati.setForeground(new Color(0, 0, 51));
		txtAccediORegistrati.setFont(new Font("Tahoma", Font.BOLD, 18));
		txtAccediORegistrati.setBorder(new EmptyBorder(0, 0, 0, 0));
		txtAccediORegistrati.setHorizontalAlignment(SwingConstants.CENTER);
		txtAccediORegistrati.setText("Accedi con email");
		txtAccediORegistrati.setBounds(11, 36, 344, 39);
		panelAccesso.add(txtAccediORegistrati);
		txtAccediORegistrati.setColumns(10);

		/**
		 * Campo di testo per l'inserimento della password con placeholder dinamico.
		 * Il campo gestisce automaticamente la visualizzazione del placeholder "******"
		 * che scompare quando l'utente inizia a digitare o clicca sul campo.
		 */
		passwordField = new JPasswordField();
		// se inizio a digitare dei tasti il campo si resetta scrivendo il nuovo testo
		passwordField.addKeyListener(new KeyAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void keyTyped(KeyEvent e) {
				campoPieno = passwordField.getText();
				if (campoPieno.equals("******")) {
					passwordField.setText(campoVuoto);
				}
			}
		});
		passwordField.setVerifyInputWhenFocusTarget(false);
		passwordField.setToolTipText("Inserire la password di accesso");
		passwordField.setText("******");
		passwordField.setFont(new Font("Tahoma", Font.BOLD, 11));
		passwordField.setBounds(81, 255, 205, 20);
		panelAccesso.add(passwordField);

		// se premo sul campo di ricerca
		passwordField.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void mouseClicked(MouseEvent e) {
				campoPieno = passwordField.getText();
				if (campoPieno.equals("******")) {
					passwordField.setText(campoVuoto);
				}
			}
		});

		// se premo altrove nel campo di ricerca
		panelAccesso.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void mouseClicked(MouseEvent e) {
				campoPieno = passwordField.getText();
				if (campoPieno.equals(campoVuoto)) {
					passwordField.setText("******");
				}
			}
		});

		final JLabel lblInserireLaPassword = new JLabel("Inserire la password di accesso");
		lblInserireLaPassword.setHorizontalAlignment(SwingConstants.CENTER);
		lblInserireLaPassword.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblInserireLaPassword.setBounds(44, 224, 278, 20);
		panelAccesso.add(lblInserireLaPassword);

		final JLabel lblEmail = new JLabel("Email inserita:");
		lblEmail.setHorizontalAlignment(SwingConstants.CENTER);
		lblEmail.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblEmail.setBounds(44, 88, 99, 20);
		panelAccesso.add(lblEmail);

		final JLabel lblEmailAttuale = new JLabel("<email>");
		if (!(emailInserita.equals("Email"))) {
			lblEmailAttuale.setText(emailInserita);
		}
		lblEmailAttuale.setHorizontalAlignment(SwingConstants.CENTER);
		lblEmailAttuale.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblEmailAttuale.setBounds(144, 88, 178, 20);
		panelAccesso.add(lblEmailAttuale);

		/**
		 * Pulsante per tornare alla schermata di accesso principale.
		 * Chiude la finestra corrente e riapre {@link ViewAccesso} per permettere
		 * all'utente di inserire un'email diversa o utilizzare altri metodi di accesso.
		 */
		final JButton btnTornaIndietro = new JButton("Torna indietro");
		btnTornaIndietro.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				final ViewAccesso frame = new ViewAccesso();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
				dispose();
			}
		});

		/**
		 * Pulsante per confermare l'accesso con le credenziali inserite.
		 * Verifica la correttezza della password tramite {@link AccessController}
		 * e in base al ruolo dell'utente reindirizza alla dashboard appropriata.
		 */
		final JButton btnAccedi = new JButton("Accedi");
		// Bottone predefinito alla pressione del tasto Enter
		getRootPane().setDefaultButton(btnAccedi);

		btnAccedi.addActionListener(new ActionListener() {
			/**
			 * Gestisce il processo di autenticazione quando viene premuto il pulsante "Accedi".
			 *
			 * <p>Il metodo:
			 * <ol>
			 *   <li>Recupera la password inserita dall'utente</li>
			 *   <li>Verifica le credenziali tramite {@link AccessController#checkCredenziali(String, String)}</li>
			 *   <li>Se le credenziali sono corrette, determina il ruolo dell'utente tramite {@link AccountController#getRuoloByEmail(String)}</li>
			 *   <li>Reindirizza alla dashboard appropriata in base al ruolo:
			 *     <ul>
			 *       <li>Cliente: {@link ViewDashboard}</li>
			 *       <li>Agente/Supporto/Admin: {@link ViewDashboardDietiEstates}</li>
			 *     </ul>
			 *   </li>
			 *   <li>Mostra messaggi di errore in caso di password errata o ruolo non riconosciuto</li>
			 * </ol>
			 *
			 * @param e Evento dell'azione del pulsante
			 * @throws SQLException Se si verifica un errore durante l'accesso al database
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				final AccessController con = new AccessController();
				@SuppressWarnings("deprecation")
				final String passwordInserita = passwordField.getText();
				try {
					if (con.checkCredenziali(emailInserita, passwordInserita)) {
						// Guardo com'è strutturato l'id associato alla email, uso un metodo nel
						// controller
						final AccountController controller = new AccountController();
						final String ruolo = controller.getRuoloByEmail(emailInserita);

						switch (ruolo) {
						case "Cliente" -> {
							final ViewDashboard viewDashboard = new ViewDashboard(emailInserita);
							viewDashboard.setLocationRelativeTo(null);
							viewDashboard.setVisible(true);
							dispose();
						}
						case "Agente", "Supporto", "Admin" -> {
							final ViewDashboardDietiEstates viewDashboardDietiEstates = new ViewDashboardDietiEstates(
									emailInserita);
							viewDashboardDietiEstates.setLocationRelativeTo(null);
							viewDashboardDietiEstates.setVisible(true);
							dispose();
						}
						default -> JOptionPane.showMessageDialog(null, "Ruolo non riconosciuto", "Errore",
								JOptionPane.INFORMATION_MESSAGE);
						}

					} else {
						// password errata
						JOptionPane.showMessageDialog(null, "La password inserita non è corretta, riprova!",
								"Errore inserimento dei dati", JOptionPane.INFORMATION_MESSAGE);
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}

			}
		});

		btnAccedi.setFocusable(false);
		btnAccedi.setForeground(Color.WHITE);
		btnAccedi.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 11));
		btnAccedi.setBackground(SystemColor.textHighlight);
		btnAccedi.setBounds(208, 382, 114, 23);
		panelAccesso.add(btnAccedi);

		btnTornaIndietro.setForeground(Color.WHITE);
		btnTornaIndietro.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 11));
		btnTornaIndietro.setFocusable(false);
		btnTornaIndietro.setBackground(SystemColor.textHighlight);
		btnTornaIndietro.setBounds(53, 382, 114, 23);
		panelAccesso.add(btnTornaIndietro);
	}
}