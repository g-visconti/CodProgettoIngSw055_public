package gui.utenza;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import auth.CognitoAuthService;
import auth.CognitoAuthServiceImpl;
import controller.AccessController;
import gui.ViewAccesso;
import util.GuiUtils;

/**
 * Finestra per la registrazione di un nuovo utente cliente nel sistema. Questa
 * interfaccia permette di completare il processo di registrazione inserendo i
 * dati personali dopo aver validato l'email tramite codice OTP.
 *
 * <p>
 * La finestra è divisa in due aree:
 * <ul>
 * <li>Area sinistra: logo e nome dell'applicazione
 * <li>Area destra: form di registrazione con validazione in tempo reale
 * </ul>
 *
 * <p>
 * Validazioni incluse:
 * <ul>
 * <li>Nome e cognome: solo lettere e spazi
 * <li>Telefono: solo numeri (massimo 15 cifre)
 * <li>Città e indirizzo: solo lettere e spazi
 * <li>CAP: solo numeri (massimo 5 cifre)
 * <li>Password: minimo 6 caratteri, almeno un numero, conferma corrispondente
 * </ul>
 *
 * @author IngSW2425_055 Team
 * @see CognitoAuthService
 * @see CognitoAuthServiceImpl#registerUser(String, String, String)
 * @see AccessController#registraNuovoCliente(String, String, String, String,
 *      String, String, String, String, String)
 * @see AccessController#isValidPassword(String, String)
 * @see ViewAccesso
 * @see ViewDashboard
 */
public class ViewRegistrazione extends JFrame {
	private static final long serialVersionUID = 1L;
	private final CognitoAuthService authService;
	private JPanel contentPane;
	private JTextField txtNome;
	private JTextField txtCognome;
	private JTextField txtTelefono;
	private JTextField txtCitta;
	private JTextField txtIndirizzo;
	private JPasswordField txtConfirmPass;
	private JPasswordField txtPass;
	private JPanel panelRegistrazione;
	private JLabel lblTitolo;
	private JLabel lblDietiEstatesMini;
	private JLabel lblDieti;
	private JTextField txtCap;
	private JLabel lblCognomeError;
	private JLabel lblCittaError;
	private JLabel lblTelefonoError;
	private JLabel lblCapError;
	private JLabel lblIndirizzoError;
	private String ruolo = "Cliente";

	/**
	 * Costruttore della finestra di registrazione. Inizializza l'interfaccia
	 * grafica per la registrazione di un nuovo cliente con l'email già validata
	 * tramite OTP.
	 *
	 * <p>
	 * La finestra include:
	 * <ul>
	 * <li>Form per l'inserimento dei dati personali
	 * <li>Validazione in tempo reale dei campi
	 * <li>Controlli di validità della password
	 * <li>Integrazione con AWS Cognito per la registrazione
	 * <li>Pulsanti per registrarsi o tornare alla schermata di accesso
	 * </ul>
	 *
	 * @param emailUtente Email dell'utente già validata tramite OTP Viene
	 *                    utilizzata come username per la registrazione in Cognito
	 *
	 * @throws IllegalArgumentException Se l'email è null o vuota
	 *
	 * @see CognitoAuthServiceImpl#registerUser(String, String, String)
	 * @see AccessController#registraNuovoCliente(String, String, String, String,
	 *      String, String, String, String, String)
	 */
	public ViewRegistrazione(String emailUtente) {
		authService = new CognitoAuthServiceImpl();

		setTitle("DietiEstates25 - Registrati");
		// Imposta l'icona di DietiEstates25 alla finestra in uso
		GuiUtils.setIconaFinestra(this);

		setResizable(false);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 818, 618);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		panelRegistrazione = new JPanel();
		panelRegistrazione.setBackground(new Color(245, 245, 245));
		panelRegistrazione.setBounds(359, 0, 443, 579);
		contentPane.add(panelRegistrazione);
		panelRegistrazione.setLayout(null);
		SwingUtilities.invokeLater(() -> requestFocusInWindow());

		URL pathlogo2 = getClass().getClassLoader().getResource("images/DietiEstatesLogomid.png");

		txtCitta = new JTextField();
		txtCitta.setFont(new Font("Tahoma", Font.BOLD, 11));
		txtCitta.setBounds(247, 149, 133, 25);
		panelRegistrazione.add(txtCitta);
		txtCitta.setText("Città");
		txtCitta.setColumns(10);
		labelClicked(txtCitta, "Città");

		txtPass = new JPasswordField();
		txtPass.setFont(new Font("Tahoma", Font.BOLD, 11));
		txtPass.setBounds(83, 252, 133, 25);
		panelRegistrazione.add(txtPass);
		txtPass.addMouseListener(new MouseAdapter() {
			/**
			 * Gestisce il click sul campo password per svuotarlo quando contiene il
			 * placeholder.
			 *
			 * @param e L'evento del mouse che ha triggerato lo svuotamento
			 */
			@Override
			public void mouseClicked(MouseEvent e) {
				txtPass.setText("");
			}
		});
		txtPass.setVerifyInputWhenFocusTarget(false);
		txtPass.setToolTipText("");
		txtPass.setText("******");

		txtConfirmPass = new JPasswordField();
		txtConfirmPass.setFont(new Font("Tahoma", Font.BOLD, 11));
		txtConfirmPass.setBounds(247, 252, 133, 25);
		panelRegistrazione.add(txtConfirmPass);
		txtConfirmPass.setText("******");
		txtConfirmPass.setVerifyInputWhenFocusTarget(false);
		txtConfirmPass.addMouseListener(new MouseAdapter() {
			/**
			 * Gestisce il click sul campo conferma password per svuotarlo quando contiene
			 * il placeholder.
			 *
			 * @param e L'evento del mouse che ha triggerato lo svuotamento
			 */
			@Override
			public void mouseClicked(MouseEvent e) {
				txtConfirmPass.setText("");
			}
		});
		txtConfirmPass.setToolTipText("");

		txtIndirizzo = new JTextField();
		txtIndirizzo.setFont(new Font("Tahoma", Font.BOLD, 11));
		txtIndirizzo.setBounds(83, 201, 133, 25);
		panelRegistrazione.add(txtIndirizzo);
		txtIndirizzo.setText("Indirizzo");
		txtIndirizzo.setColumns(10);
		labelClicked(txtIndirizzo, "Indirizzo");

		txtCognome = new JTextField();
		txtCognome.setFont(new Font("Tahoma", Font.BOLD, 11));
		txtCognome.setBounds(247, 92, 133, 25);
		panelRegistrazione.add(txtCognome);
		txtCognome.setText("Cognome");
		txtCognome.setColumns(10);
		labelClicked(txtCognome, "Cognome");

		JLabel lblPassDimError = new JLabel("Inserire una password di almeno 6 caratteri.");
		lblPassDimError.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPassDimError.setBounds(83, 288, 371, 20);
		panelRegistrazione.add(lblPassDimError);
		lblPassDimError.setHorizontalAlignment(SwingConstants.LEFT);

		JLabel lblPassNumError = new JLabel("La password deve contenere almeno un numero.");
		lblPassNumError.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPassNumError.setBounds(83, 319, 371, 20);
		panelRegistrazione.add(lblPassNumError);
		lblPassNumError.setHorizontalAlignment(SwingConstants.LEFT);

		JLabel lblPassConfError = new JLabel("Conferma la password.");
		lblPassConfError.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPassConfError.setHorizontalAlignment(SwingConstants.LEFT);
		lblPassConfError.setBounds(83, 350, 133, 20);
		panelRegistrazione.add(lblPassConfError);

		txtNome = new JTextField();
		txtNome.setCaretColor(new Color(0, 0, 51));
		txtNome.setFont(new Font("Tahoma", Font.BOLD, 11));
		txtNome.setBounds(83, 92, 133, 25);
		panelRegistrazione.add(txtNome);
		txtNome.setText("Nome");
		txtNome.setColumns(10);
		labelClicked(txtNome, "Nome");

		txtTelefono = new JTextField();
		txtTelefono.setFont(new Font("Tahoma", Font.BOLD, 11));
		txtTelefono.setBounds(83, 149, 133, 25);
		panelRegistrazione.add(txtTelefono);
		txtTelefono.setText("Telefono");
		txtTelefono.setColumns(10);
		labelClicked(txtTelefono, "Telefono");

		lblTitolo = new JLabel("Riempire i seguenti campi:");
		lblTitolo.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblTitolo.setBounds(83, 29, 341, 41);
		panelRegistrazione.add(lblTitolo);

		txtCap = new JTextField();
		txtCap.setFont(new Font("Tahoma", Font.BOLD, 11));
		txtCap.setText("CAP");
		txtCap.setBounds(247, 201, 68, 25);
		panelRegistrazione.add(txtCap);
		txtCap.setColumns(10);
		labelClicked(txtCap, "CAP");

		JLabel lblNameError = new JLabel("Nome non valido");
		lblNameError.setForeground(new Color(255, 0, 0));
		lblNameError.setVisible(false);
		lblNameError.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblNameError.setBounds(83, 117, 133, 14);
		panelRegistrazione.add(lblNameError);

		lblCognomeError = new JLabel("Cognome non valido");
		lblCognomeError.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblCognomeError.setVisible(false);
		lblCognomeError.setForeground(new Color(255, 0, 0));
		lblCognomeError.setBounds(247, 117, 133, 14);
		panelRegistrazione.add(lblCognomeError);

		lblTelefonoError = new JLabel("Telefono non valido");
		lblTelefonoError.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblTelefonoError.setForeground(new Color(255, 0, 0));
		lblTelefonoError.setVisible(false);
		lblTelefonoError.setBounds(83, 174, 133, 14);
		panelRegistrazione.add(lblTelefonoError);

		lblCapError = new JLabel("CAP non valido");
		lblCapError.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblCapError.setForeground(new Color(255, 0, 0));
		lblCapError.setVisible(false);
		lblCapError.setBounds(248, 224, 133, 14);
		panelRegistrazione.add(lblCapError);

		lblCittaError = new JLabel("Citt\u00E0 non valida");
		lblCittaError.setVisible(false);
		lblCittaError.setForeground(new Color(255, 0, 0));
		lblCittaError.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblCittaError.setBounds(247, 174, 133, 14);
		panelRegistrazione.add(lblCittaError);

		lblIndirizzoError = new JLabel("Indirizzo non valido");
		lblIndirizzoError.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblIndirizzoError.setForeground(new Color(255, 0, 0));
		lblIndirizzoError.setVisible(false);
		lblIndirizzoError.setBounds(83, 224, 134, 14);
		panelRegistrazione.add(lblIndirizzoError);

		JButton btnRegistrati = new JButton("Registrati");
		btnRegistrati.addActionListener(new ActionListener() {
			/**
			 * Gestisce il click sul pulsante "Registrati". Valida tutti i campi del form e,
			 * se validi, procede con:
			 * <ol>
			 * <li>Registrazione utente in AWS Cognito
			 * <li>Salvataggio dati cliente nel database locale
			 * <li>Apertura della dashboard cliente
			 * </ol>
			 *
			 * <p>
			 * Validazioni eseguite:
			 * <ul>
			 * <li>Nome e cognome: solo lettere e spazi
			 * <li>Telefono: solo numeri (max 15 cifre)
			 * <li>Città: solo lettere e spazi
			 * <li>Indirizzo: solo lettere e spazi
			 * <li>CAP: solo numeri (max 5 cifre)
			 * <li>Password: minimo 6 caratteri, almeno un numero, conferma corrispondente
			 * </ul>
			 *
			 * @param e L'evento dell'azione che ha triggerato la registrazione
			 *
			 * @see CognitoAuthServiceImpl#registerUser(String, String, String)
			 * @see AccessController#registraNuovoCliente(String, String, String, String,
			 *      String, String, String, String, String)
			 * @see AccessController#isValidPassword(String, String)
			 */
			@Override
			public void actionPerformed(ActionEvent e) {

				String nome = txtNome.getText();
				if (!nome.matches("[a-zA-ZÀ-ÿ\\s]+")) {
					lblNameError.setVisible(true);
					txtNome.setText("Nome");
				} else {
					lblNameError.setVisible(false);
				}

				String cognome = txtCognome.getText();
				if (!cognome.matches("[a-zA-ZÀ-ÿ\\s]+")) {
					lblCognomeError.setVisible(true);
					txtCognome.setText("Cognome");
				} else {
					lblCognomeError.setVisible(false);
				}

				String citta = txtCitta.getText();
				if (!citta.matches("[a-zA-ZÀ-ÿ\\s]+")) {
					lblCittaError.setVisible(true);
					txtCitta.setText("Citt�");
				} else {
					lblCittaError.setVisible(false);
				}

				String telefono = txtTelefono.getText().trim();

				if (!telefono.matches("\\d{1,15}")) {

					lblTelefonoError.setVisible(true);
					txtTelefono.setText("Telefono");
				} else {
					lblTelefonoError.setVisible(false);
				}

				String cap = txtCap.getText().trim();

				if (!cap.matches("\\d{1,5}")) {

					lblCapError.setVisible(true);
					txtCap.setText("CAP");
				} else {
					lblCapError.setVisible(false);
				}

				String indirizzo = txtIndirizzo.getText();
				if (!indirizzo.matches("[a-zA-ZÀ-ÿ\\s\\d]+")) {
					lblIndirizzoError.setVisible(true);
					txtIndirizzo.setText("Indirizzo");
				} else {
					lblIndirizzoError.setVisible(false);
				}

				char[] passwordChar = txtPass.getPassword();
				char[] confermaChar = txtConfirmPass.getPassword();

				String passwordUtente = new String(passwordChar);
				String confermaPassword = new String(confermaChar);

				AccessController controller = new AccessController();

				if (!controller.isValidPassword(passwordUtente, confermaPassword)) {
					JOptionPane.showMessageDialog(null,
							"Password non valida:\n- minimo 6 caratteri\n- almeno un numero\n- le password devono coincidere",
							"Errore password", JOptionPane.ERROR_MESSAGE);
					return;
				}

				boolean success = authService.registerUser(emailUtente, passwordUtente, emailUtente);

				if (success) {
					AccessController cont1 = new AccessController();
					cont1.registraNuovoCliente(emailUtente, passwordUtente, nome, cognome, citta, telefono, cap,
							indirizzo, ruolo);
					ViewDashboard viewDashboard = new ViewDashboard(emailUtente);
					viewDashboard.setVisible(true);
					dispose();
				} else {
					JOptionPane.showMessageDialog(null, "La registrazione � fallita. Riprova con i dati corretti.",
							"Errore nella registrazione", JOptionPane.ERROR_MESSAGE);
				}

				ViewDashboard schermata = new ViewDashboard(emailUtente);
				schermata.setVisible(true);

			}
		});
		btnRegistrati.setFocusable(false);
		btnRegistrati.setForeground(Color.WHITE);
		btnRegistrati.setBackground(SystemColor.textHighlight);
		btnRegistrati.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 11));
		btnRegistrati.setBounds(247, 409, 133, 25);
		panelRegistrazione.add(btnRegistrati);

		JButton btnTornaIndietro = new JButton("Torna indietro");
		btnTornaIndietro.setForeground(Color.WHITE);
		btnTornaIndietro.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 11));
		btnTornaIndietro.setFocusable(false);
		btnTornaIndietro.setBackground(SystemColor.textHighlight);
		btnTornaIndietro.setBounds(82, 409, 134, 25);
		panelRegistrazione.add(btnTornaIndietro);

		btnTornaIndietro.addMouseListener(new MouseAdapter() {
			/**
			 * Gestisce il click sul pulsante "Torna indietro". Chiude la finestra di
			 * registrazione e ritorna alla schermata di accesso.
			 *
			 * @param e L'evento del mouse che ha triggerato il ritorno
			 *
			 * @see ViewAccesso
			 */
			@Override
			public void mouseClicked(MouseEvent e) {
				ViewAccesso frame = new ViewAccesso();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
				dispose();
			}
		});

		JPanel panelLogo = new JPanel();
		panelLogo.setBackground(Color.WHITE);
		panelLogo.setBounds(0, 0, 356, 579);
		contentPane.add(panelLogo);
		panelLogo.setLayout(null);

		lblDietiEstatesMini = new JLabel("");
		lblDietiEstatesMini.setBounds(83, 286, 190, 174);
		panelLogo.add(lblDietiEstatesMini);
		lblDietiEstatesMini.setIcon(new ImageIcon(pathlogo2));

		lblDieti = new JLabel("DietiEstates25");
		lblDieti.setHorizontalAlignment(SwingConstants.CENTER);
		lblDieti.setFont(new Font("Tahoma", Font.BOLD, 30));
		lblDieti.setForeground(new Color(27, 99, 142));
		lblDieti.setBounds(5, 54, 345, 56);
		panelLogo.add(lblDieti);
	}

	/**
	 * Configura il comportamento di placeholder per un campo di testo. Il testo
	 * viene mostrato quando il campo è vuoto e scompare quando il campo riceve il
	 * focus.
	 *
	 * @param field Il campo di testo da configurare
	 * @param text  Il testo del placeholder da mostrare
	 *
	 * @see FocusAdapter
	 */
	public void labelClicked(JTextField field, String text) {
		field.setText(text);

		field.addFocusListener(new FocusAdapter() {
			/**
			 * Gestisce l'evento di focus guadagnato. Se il campo contiene il testo del
			 * placeholder, lo svuota.
			 *
			 * @param e L'evento di focus che indica che il campo ha ricevuto il focus
			 */
			@Override
			public void focusGained(FocusEvent e) {
				if (field.getText().equals(text)) {
					field.setText("");
				}
			}

			/**
			 * Gestisce l'evento di focus perso. Se il campo è vuoto, ripristina il testo
			 * del placeholder.
			 *
			 * @param e L'evento di focus che indica che il campo ha perso il focus
			 */
			@Override
			public void focusLost(FocusEvent e) {
				if (field.getText().trim().isEmpty()) {
					field.setText(text);
				}
			}
		});
	}
}