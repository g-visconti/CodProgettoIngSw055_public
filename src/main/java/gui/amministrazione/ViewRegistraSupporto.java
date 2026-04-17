package gui.amministrazione;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
import util.GuiUtils;

/**
 * Finestra per la registrazione di un nuovo amministratore di supporto nel
 * sistema. Questa interfaccia permette agli amministratori con privilegi
 * elevati di inserire tutti i dati necessari per creare un nuovo account
 * amministratore di supporto.
 *
 * <p>
 * La finestra è strutturalmente simile a {@link ViewRegistraAgente} ma con
 * alcune differenze:
 * <ul>
 * <li>Titolo e descrizioni specifiche per amministratori di supporto</li>
 * <li>Ruolo predefinito impostato a "Supporto" invece di "Agente"</li>
 * <li>Chiamata al metodo specifico per la registrazione supporto</li>
 * </ul>
 *
 * <p>
 * La validazione include:
 * <ul>
 * <li>Controllo formato campi testuali con espressioni regolari</li>
 * <li>Validazione numerica per telefono e CAP</li>
 * <li>Validazione password con controlli avanzati</li>
 * <li>Registrazione su AWS Cognito per l'autenticazione</li>
 * <li>Salvataggio nel database di AWS tramite AccessController</li>
 * </ul>
 *
 * @author IngSW2425_055 Team
 * @see ViewRegistraAgente
 * @see CognitoAuthService
 * @see AccessController
 */
public class ViewRegistraSupporto extends JFrame {

	private static final long serialVersionUID = 1L;
	private final CognitoAuthService authService;
	private final JPanel contentPane;
	private final JTextField txtNome;
	private final JTextField txtCognome;
	private final JTextField txtTelefono;
	private final JTextField txtCitta;
	private final JTextField txtIndirizzo;
	private final JPasswordField txtConfirmPass;
	private final JPasswordField txtPass;
	private final JPanel panel;
	private final JLabel lblNuovoAmmDiSupporto;
	private final JTextField txtCap;
	private final JLabel lblCognomeError;
	private final JLabel lblCittaError;
	private final JLabel lblTelefonoError;
	private final JLabel lblCapError;
	private final JLabel lblIndirizzoError;
	private final JLabel lblEmail;
	private final String ruolo = "Supporto";

	/**
	 * Costruttore della finestra di registrazione amministratore di supporto.
	 * Inizializza tutti i componenti grafici e configura i listener per la
	 * validazione in tempo reale dei dati inseriti.
	 *
	 * <p>
	 * Questa interfaccia è accessibile solo agli amministratori principali e
	 * consente di creare nuovi account con privilegi di supporto, che potranno a
	 * loro volta aggiungere agenti immobiliari ma non altri amministratori.
	 *
	 * <p>
	 * Il processo di registrazione combina:
	 * <ul>
	 * <li>Autenticazione tramite AWS Cognito</li>
	 * <li>Salvataggio dati anagrafici nel database</li>
	 * <li>Associazione automatica all'agenzia dell'amministratore creatore</li>
	 * </ul>
	 *
	 * @param emailSupporto Email di lavoro del nuovo amministratore di supporto
	 * @param agenzia       Nome dell'agenzia a cui associare il nuovo
	 *                      amministratore
	 *
	 * @see CognitoAuthServiceImpl#registerUser(String, String, String)
	 * @see AccessController#registraNuovoSupporto(String, String, String, String,
	 *      String, String, String, String, String, String)
	 * @see GuiUtils#setIconaFinestra(JFrame)
	 */
	public ViewRegistraSupporto(String emailSupporto, String agenzia) {
		authService = new CognitoAuthServiceImpl();

		setTitle("DietiEstates25 - Registra un nuovo amministratore di supporto");
		setResizable(false);

		// Imposta l'icona di DietiEstates25 alla finestra in uso
		GuiUtils.setIconaFinestra(this);

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 459, 618);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		panel = new JPanel();
		panel.setBackground(SystemColor.menu);
		panel.setBounds(0, 0, 443, 579);
		contentPane.add(panel);
		panel.setLayout(null);
		SwingUtilities.invokeLater(() -> requestFocusInWindow());

		txtCitta = new JTextField();
		txtCitta.setFont(new Font("Tahoma", Font.BOLD, 11));
		txtCitta.setBounds(225, 142, 133, 25);
		panel.add(txtCitta);
		txtCitta.setText("Citt\u00E0");
		txtCitta.setColumns(10);
		labelClicked(txtCitta, "Città");

		txtPass = new JPasswordField();
		txtPass.setFont(new Font("Tahoma", Font.BOLD, 11));
		txtPass.setBounds(69, 245, 133, 25);
		panel.add(txtPass);
		txtPass.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (new String(txtPass.getPassword()).equals("******")) {
					txtPass.setText("");
				}
			}
		});
		txtPass.setVerifyInputWhenFocusTarget(false);
		txtPass.setToolTipText("");
		txtPass.setText("******");

		txtConfirmPass = new JPasswordField();
		txtConfirmPass.setFont(new Font("Tahoma", Font.BOLD, 11));
		txtConfirmPass.setBounds(225, 245, 133, 25);
		panel.add(txtConfirmPass);
		txtConfirmPass.setText("******");
		txtConfirmPass.setVerifyInputWhenFocusTarget(false);
		txtConfirmPass.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (new String(txtConfirmPass.getPassword()).equals("******")) {
					txtConfirmPass.setText("");
				}
			}
		});
		txtConfirmPass.setToolTipText("");

		txtIndirizzo = new JTextField();
		txtIndirizzo.setFont(new Font("Tahoma", Font.BOLD, 11));
		txtIndirizzo.setBounds(69, 194, 133, 25);
		panel.add(txtIndirizzo);
		txtIndirizzo.setText("Indirizzo");
		txtIndirizzo.setColumns(10);
		labelClicked(txtIndirizzo, "Indirizzo");

		txtCognome = new JTextField();
		txtCognome.setFont(new Font("Tahoma", Font.BOLD, 11));
		txtCognome.setBounds(225, 85, 133, 25);
		panel.add(txtCognome);
		txtCognome.setText("Cognome");
		txtCognome.setColumns(10);
		labelClicked(txtCognome, "Cognome");

		final JLabel lblPassDimError = new JLabel("Inserire una password di almeno 6 caratteri.");
		lblPassDimError.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPassDimError.setBounds(69, 281, 371, 20);
		panel.add(lblPassDimError);
		lblPassDimError.setHorizontalAlignment(SwingConstants.LEFT);
		lblPassDimError.setVisible(false);

		final JLabel lblPassNumError = new JLabel("La password deve contenere almeno un numero.");
		lblPassNumError.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPassNumError.setBounds(69, 312, 371, 20);
		panel.add(lblPassNumError);
		lblPassNumError.setHorizontalAlignment(SwingConstants.LEFT);
		lblPassNumError.setVisible(false);

		final JLabel lblPassConfError = new JLabel("Conferma la password.");
		lblPassConfError.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPassConfError.setHorizontalAlignment(SwingConstants.LEFT);
		lblPassConfError.setBounds(69, 343, 133, 20);
		panel.add(lblPassConfError);
		lblPassConfError.setVisible(false);

		txtNome = new JTextField();
		txtNome.setCaretColor(new Color(0, 0, 51));
		txtNome.setFont(new Font("Tahoma", Font.BOLD, 11));
		txtNome.setBounds(69, 85, 133, 25);
		panel.add(txtNome);
		txtNome.setText("Nome");
		txtNome.setColumns(10);
		labelClicked(txtNome, "Nome");

		txtTelefono = new JTextField();
		txtTelefono.setFont(new Font("Tahoma", Font.BOLD, 11));
		txtTelefono.setBounds(69, 142, 133, 25);
		panel.add(txtTelefono);
		txtTelefono.setText("Telefono");
		txtTelefono.setColumns(10);
		labelClicked(txtTelefono, "Telefono");

		lblNuovoAmmDiSupporto = new JLabel("Inserire i dati del nuovo amministratore di supporto:");
		lblNuovoAmmDiSupporto.setHorizontalAlignment(SwingConstants.CENTER);
		lblNuovoAmmDiSupporto.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNuovoAmmDiSupporto.setBounds(45, 22, 353, 41);
		panel.add(lblNuovoAmmDiSupporto);

		txtCap = new JTextField();
		txtCap.setFont(new Font("Tahoma", Font.BOLD, 11));
		txtCap.setText("CAP");
		txtCap.setBounds(225, 194, 68, 25);
		panel.add(txtCap);
		txtCap.setColumns(10);
		labelClicked(txtCap, "CAP");

		final JLabel lblNameError = new JLabel("Nome non valido");
		lblNameError.setForeground(new Color(255, 0, 0));
		lblNameError.setVisible(false);
		lblNameError.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblNameError.setBounds(69, 110, 133, 14);
		panel.add(lblNameError);

		lblCognomeError = new JLabel("Cognome non valido");
		lblCognomeError.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblCognomeError.setVisible(false);
		lblCognomeError.setForeground(new Color(255, 0, 0));
		lblCognomeError.setBounds(225, 110, 133, 14);
		panel.add(lblCognomeError);

		lblTelefonoError = new JLabel("Telefono non valido");
		lblTelefonoError.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblTelefonoError.setForeground(new Color(255, 0, 0));
		lblTelefonoError.setVisible(false);
		lblTelefonoError.setBounds(69, 167, 133, 14);
		panel.add(lblTelefonoError);

		lblCapError = new JLabel("CAP non valido");
		lblCapError.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblCapError.setForeground(new Color(255, 0, 0));
		lblCapError.setVisible(false);
		lblCapError.setBounds(225, 217, 133, 14);
		panel.add(lblCapError);

		lblCittaError = new JLabel("Citt\u00E0 non valida");
		lblCittaError.setVisible(false);
		lblCittaError.setForeground(new Color(255, 0, 0));
		lblCittaError.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblCittaError.setBounds(225, 167, 133, 14);
		panel.add(lblCittaError);

		lblIndirizzoError = new JLabel("Indirizzo non valido");
		lblIndirizzoError.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblIndirizzoError.setForeground(new Color(255, 0, 0));
		lblIndirizzoError.setVisible(false);
		lblIndirizzoError.setBounds(68, 217, 134, 14);
		panel.add(lblIndirizzoError);

		// Aggiunta del campo email
		lblEmail = new JLabel("Email: " + emailSupporto);
		lblEmail.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblEmail.setBounds(69, 50, 371, 25);
		panel.add(lblEmail);

		final JButton btnRegistraSupporto = new JButton("Registra supporto");
		getRootPane().setDefaultButton(btnRegistraSupporto);
		btnRegistraSupporto.addActionListener(new ActionListener() {
			/**
			 * Gestisce il click sul pulsante di registrazione. Esegue la validazione di
			 * tutti i campi, registra l'utente su AWS Cognito, salva i dati nel database
			 * locale e reindirizza alla dashboard.
			 *
			 * <p>
			 * Processo di validazione dettagliato:
			 * <ul>
			 * <li><b>Nome e cognome</b>: Solo caratteri alfabetici, spazi e caratteri
			 * speciali consentiti (espressione: [a-zA-Z�-�\\s]+)</li>
			 * <li><b>Telefono</b>: Solo cifre numeriche, massimo 15 caratteri</li>
			 * <li><b>Città e indirizzo</b>: Solo caratteri alfabetici e spazi</li>
			 * <li><b>CAP</b>: Solo cifre numeriche, esattamente 5 caratteri</li>
			 * <li><b>Password</b>: Almeno 6 caratteri con almeno un numero</li>
			 * </ul>
			 *
			 * <p>
			 * In caso di successo della registrazione su AWS Cognito:
			 * <ol>
			 * <li>Chiama {@code AccessController.registraNuovoSupporto()} per salvare i
			 * dati localmente</li>
			 * <li>Chiude la finestra corrente</li>
			 * </ol>
			 *
			 * <p>
			 * In caso di fallimento mostra un messaggio di errore appropriato.
			 *
			 * @param e Evento di azione generato dal click sul pulsante
			 *
			 * @see CognitoAuthService#registerUser(String, String, String)
			 * @see AccessController#registraNuovoSupporto(String, String, String, String,
			 *      String, String, String, String, String, String)
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean valid = true;

				// Validazione nome
				final String nome = txtNome.getText();
				if (!nome.matches("[a-zA-ZÀ-ÿ\\s]+") || nome.equals("Nome")) {
					lblNameError.setVisible(true);
					txtNome.setText("Nome");
					valid = false;
				} else {
					lblNameError.setVisible(false);
				}

				// Validazione cognome
				final String cognome = txtCognome.getText();
				if (!cognome.matches("[a-zA-ZÀ-ÿ\\s]+") || cognome.equals("Cognome")) {
					lblCognomeError.setVisible(true);
					txtCognome.setText("Cognome");
					valid = false;
				} else {
					lblCognomeError.setVisible(false);
				}

				// Validazione città
				final String citta = txtCitta.getText();
				if (!citta.matches("[a-zA-ZÀ-ÿ\\s]+") || citta.equals("Città")) {
					lblCittaError.setVisible(true);
					txtCitta.setText("Città");
					valid = false;
				} else {
					lblCittaError.setVisible(false);
				}

				// Validazione telefono
				final String telefono = txtTelefono.getText().trim();

				// Permette numeri con o senza spazi: es. 3203407898 o 320 340 7898
				if (!telefono.matches("[\\d\\s]{1,20}") || telefono.equals("Telefono")) {
					lblTelefonoError.setVisible(true);
					txtTelefono.setText("Telefono");
					valid = false;
				} else {
					// Verifica che ci siano almeno 8-15 cifre numeriche
					String soloNumeri = telefono.replaceAll("\\D", ""); // Rimuove tutto tranne i numeri
					if (soloNumeri.length() < 8 || soloNumeri.length() > 15) {
						lblTelefonoError.setVisible(true);
						txtTelefono.setText("Telefono");
						valid = false;
					} else {
						lblTelefonoError.setVisible(false);
					}
				}

				// Validazione CAP
				final String cap = txtCap.getText().trim();
				// CAP italiano: esattamente 5 cifre, prima cifra 0-9
				if (!cap.matches("\\d{5}") || cap.equals("CAP")) {
					lblCapError.setVisible(true);
					txtCap.setText("CAP");
					valid = false;
				} else {
					lblCapError.setVisible(false);
				}

				// Validazione indirizzo
				final String indirizzo = txtIndirizzo.getText();
				if (!indirizzo.matches("[a-zA-ZÀ-ÿ\\s\\d]+") || indirizzo.equals("Indirizzo")) {
					lblIndirizzoError.setVisible(true);
					txtIndirizzo.setText("Indirizzo");
					valid = false;
				} else {
					lblIndirizzoError.setVisible(false);
				}

				// Validazione password
				final char[] passwordChar = txtPass.getPassword();
				final String passwordUtente = new String(passwordChar);
				final char[] confirmPasswordChar = txtConfirmPass.getPassword();
				final String confirmPassword = new String(confirmPasswordChar);

				// Nascondi tutti i messaggi di errore password prima di ricontrollare
				lblPassDimError.setVisible(false);
				lblPassNumError.setVisible(false);
				lblPassConfError.setVisible(false);

				// Controlla lunghezza password
				if (passwordUtente.length() < 6) {
					lblPassDimError.setVisible(true);
					valid = false;
				}

				// Controlla se password contiene almeno un numero
				if (!passwordUtente.matches(".*\\d.*")) {
					lblPassNumError.setVisible(true);
					valid = false;
				}

				// Controlla se le password corrispondono
				if (!passwordUtente.equals(confirmPassword)) {
					lblPassConfError.setVisible(true);
					valid = false;
				}

				// Controlla se la password non è quella di default
				if (passwordUtente.equals("******")) {
					lblPassDimError.setVisible(true);
					valid = false;
				}

				// Se tutti i campi sono validi, procedi con la registrazione
				if (valid) {
					final boolean success = authService.registerUser(emailSupporto, passwordUtente, emailSupporto);

					if (success) {
						final AccessController controller = new AccessController();
						controller.registraNuovoSupporto(emailSupporto, passwordUtente, nome, cognome, citta, telefono,
								cap, indirizzo, ruolo, agenzia);
						JOptionPane.showMessageDialog(null,
								"La registrazione del nuovo amministratore di supporto è avvenuta con successo!");
						dispose();
					} else {
						JOptionPane.showMessageDialog(null, "Qualcosa è andato storto, registrazione fallita.",
								"Errore nella registrazione", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					// Non chiudere la finestra se ci sono errori
					JOptionPane.showMessageDialog(null, "Correggi gli errori nei campi prima di procedere.",
							"Errore di validazione", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		btnRegistraSupporto.setFocusable(false);
		btnRegistraSupporto.setForeground(Color.WHITE);
		btnRegistraSupporto.setBackground(SystemColor.textHighlight);
		btnRegistraSupporto.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 13));
		btnRegistraSupporto.setBounds(121, 420, 200, 25);
		panel.add(btnRegistraSupporto);

		txtPass.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (txtPass.getPassword().length == 0 || new String(txtPass.getPassword()).equals("******")) {
					lblPassDimError.setVisible(false); // Nascondi quando non c'è input
					lblPassNumError.setVisible(false);
				}
			}
		});
	}

	/**
	 * Configura un campo di testo con comportamento da placeholder intelligente.
	 * Implementa il pattern "hint text" che fornisce un'esperienza utente
	 * migliorata: il testo di esempio scompare quando l'utente inizia a digitare e
	 * riappare se il campo viene lasciato vuoto.
	 *
	 * <p>
	 * Caratteristiche:
	 * <ul>
	 * <li>Testo di guida visibile quando il campo è vuoto e senza focus</li>
	 * <li>Rimozione automatica del testo di guida al focus</li>
	 * <li>Ripristino automatico se il campo viene abbandonato vuoto</li>
	 * <li>Compatibile con tutti i campi di testo dell'interfaccia</li>
	 * </ul>
	 *
	 * @param field Campo di testo da configurare
	 * @param text  Testo placeholder/guida da visualizzare
	 *
	 * @see FocusAdapter
	 * @see JTextField
	 */
	public void labelClicked(JTextField field, String text) {
		field.setText(text);

		field.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (field.getText().equals(text)) {
					field.setText("");
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (field.getText().trim().isEmpty()) {
					field.setText(text);
				}
			}
		});
	}
}