package gui;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.net.URL;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import controller.AccessController;
import controller.OAuthController;
import gui.utenza.ViewDashboard;
import gui.utenza.ViewRegistrazione;
import util.GuiUtils;

/**
 * Finestra principale di accesso all'applicazione DietiEstates25.
 * Fornisce diverse modalità di autenticazione per gli utenti:
 * <ul>
 *   <li>Accesso tramite email e password (con redirect alla finestra password)</li>
 *   <li>Registrazione di nuovi utenti</li>
 *   <li>Autenticazione OAuth tramite provider esterni (Google e Facebook)</li>
 * </ul>
 *
 * <p>La finestra è divisa in due sezioni principali:
 * <ul>
 *   <li>Pannello sinistro: contiene logo e titolo dell'applicazione</li>
 *   <li>Pannello destro: contiene tutti i controlli per l'autenticazione</li>
 * </ul>
 *
 * <p>Flussi supportati:
 * <ul>
 *   <li>Accesso con email esistente → {@link ViewAccessoConPassword}</li>
 *   <li>Registrazione nuova email → {@link ViewRegistrazione}</li>
 *   <li>OAuth Google/Facebook → acquisizione token via browser e clipboard</li>
 *   <li>Accesso riuscito → {@link ViewDashboard}</li>
 * </ul>
 *
 * @author IngSW2425_055 Team
 * @see AccessController
 * @see OAuthController
 * @see ViewAccessoConPassword
 * @see ViewRegistrazione
 * @see ViewDashboard
 */
public class ViewAccesso extends JFrame {

	private static final long serialVersionUID = 1L;
	private final OAuthController oauthController;
	private JPanel contentPane;
	private JTextField txtEmail;
	private JTextField txtAccediORegistrati;
	private JTextField txtOppure;


	/**
	 * Costruttore della finestra principale di accesso.
	 * Inizializza il controller OAuth e tutti i componenti grafici dell'interfaccia.
	 *
	 * <p>La finestra viene configurata con dimensioni fisse e non ridimensionabile,
	 * con layout diviso in due pannelli distinti per logo e controlli di accesso.
	 */
	public ViewAccesso() {
		oauthController = new OAuthController();

		// Impostazioni finestra
		GuiUtils.setIconaFinestra(this);
		setTitle("DietiEstates25 - Accedi o registrati");
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 818, 618);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		initPanelLogo();
		initPanelAccesso();
	}

	/**
	 * Inizializza il pannello sinistro contenente il logo e il titolo dell'applicazione.
	 *
	 * <p>Il pannello ha sfondo bianco e contiene:
	 * <ul>
	 *   <li>Titolo "DietiEstates25" centrato con colore e font personalizzati</li>
	 *   <li>Logo dell'applicazione caricato dalle risorse</li>
	 * </ul>
	 */
	private void initPanelLogo() {
		JPanel panelLogo = new JPanel();
		panelLogo.setBackground(Color.WHITE);
		panelLogo.setBounds(0, 0, 446, 593);
		panelLogo.setLayout(null);
		contentPane.add(panelLogo);

		JLabel lblTitolo = new JLabel("DietiEstates25", SwingConstants.CENTER);
		lblTitolo.setForeground(new Color(27, 99, 142));
		lblTitolo.setFont(new Font("Tahoma", Font.BOLD, 30));
		lblTitolo.setBounds(0, 39, 446, 32);
		panelLogo.add(lblTitolo);

		JLabel lblLogo = new JLabel();
		lblLogo.setBounds(93, 169, 259, 249);
		panelLogo.add(lblLogo);

		URL pathLogo = getClass().getClassLoader().getResource("images/DietiEstatesLogo.png");
		lblLogo.setIcon(new ImageIcon(pathLogo));
	}

	/**
	 * Inizializza il pannello destro contenente tutti i controlli per l'autenticazione.
	 *
	 * <p>Il pannello include:
	 * <ul>
	 *   <li>Campi di testo per istruzioni e email</li>
	 *   <li>Pulsanti per autenticazione OAuth (Google e Facebook)</li>
	 *   <li>Pulsante per login/registrazione via email</li>
	 * </ul>
	 *
	 * @see #initTextFields(JPanel)
	 * @see #initOAuthButtons(JPanel)
	 * @see #initEmailLogin(JPanel)
	 */
	private void initPanelAccesso() {
		JPanel panel = new JPanel();
		panel.setBackground(new Color(245, 245, 245));
		panel.setBounds(446, 0, 370, 593);
		panel.setLayout(null);
		contentPane.add(panel);

		initTextFields(panel);
		initOAuthButtons(panel);
		initEmailLogin(panel);
	}

	/**
	 * Crea e configura i campi di testo e le label informative nel pannello di accesso.
	 *
	 * <p>I campi includono:
	 * <ul>
	 *   <li>Titolo "Accedi o registrati con" (non editabile)</li>
	 *   <li>Campo per l'inserimento dell'email con placeholder "Email"</li>
	 *   <li>Separatore "oppure" tra metodi di autenticazione</li>
	 * </ul>
	 *
	 * @param panel Pannello contenitore dove aggiungere i componenti
	 */
	private void initTextFields(JPanel panel) {
		txtAccediORegistrati = new JTextField("Accedi o registrati con");
		txtAccediORegistrati.setBounds(13, 40, 344, 39);
		txtAccediORegistrati.setFont(new Font("Tahoma", Font.BOLD, 18));
		txtAccediORegistrati.setHorizontalAlignment(SwingConstants.CENTER);
		txtAccediORegistrati.setEditable(false);
		txtAccediORegistrati.setOpaque(false);
		txtAccediORegistrati.setBorder(null);
		panel.add(txtAccediORegistrati);

		txtEmail = new JTextField("Email");
		txtEmail.setBounds(80, 130, 205, 20);
		panel.add(txtEmail);

		txtEmail.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				txtEmail.setText("");
			}
		});

		txtOppure = new JTextField("oppure");
		txtOppure.setBounds(13, 240, 344, 20);
		txtOppure.setHorizontalAlignment(SwingConstants.CENTER);
		txtOppure.setEditable(false);
		txtOppure.setOpaque(false);
		txtOppure.setBorder(null);
		panel.add(txtOppure);
	}

	/**
	 * Crea e configura i pulsanti per l'autenticazione OAuth tramite provider esterni.
	 *
	 * <p>Vengono creati due pulsanti stilizzati come label:
	 * <ul>
	 *   <li>Facebook: con colore blu di sistema, apre il flusso di autenticazione Facebook</li>
	 *   <li>Google: con colore rosso, apre il flusso di autenticazione Google</li>
	 * </ul>
	 *
	 * <p>Entrambi i pulsanti:
	 * <ul>
	 *   <li>Aprirono il browser per l'autenticazione OAuth</li>
	 *   <li>Mostrano una finestra di conferma per incollare il token dalla clipboard</li>
	 * </ul>
	 *
	 * @param panel Pannello contenitore dove aggiungere i componenti
	 * @see #loginWithFacebook()
	 * @see #loginWithGoogle()
	 * @see #showTokenConfirmationDialog(String)
	 */
	private void initOAuthButtons(JPanel panel) {
		JLabel lblFacebook = createOAuthLabel(
				"Facebook",
				SystemColor.textHighlight,
				_ -> {
					loginWithFacebook();
					showTokenConfirmationDialog("Facebook");
				}
				);
		lblFacebook.setBounds(109, 328, 146, 23);
		panel.add(lblFacebook);

		JLabel lblGoogle = createOAuthLabel(
				"Google",
				new Color(178, 34, 34),
				_ -> {
					loginWithGoogle();
					showTokenConfirmationDialog("Google");
				}
				);
		lblGoogle.setBounds(109, 374, 146, 23);
		panel.add(lblGoogle);
	}

	/**
	 * Crea e configura il pulsante per il login/registrazione tramite email.
	 *
	 * <p>Il pulsante "Prosegui":
	 * <ul>
	 *   <li>Ha colore di evidenziazione di sistema e testo bianco</li>
	 *   <li>È impostato come pulsante predefinito (attivabile con Enter)</li>
	 *   <li>Alla pressione valida l'email e reindirizza alla schermata appropriata</li>
	 * </ul>
	 *
	 * @param panel Pannello contenitore dove aggiungere il componente
	 * @see #handleEmailLogin()
	 */
	private void initEmailLogin(JPanel panel) {
		JButton btnProsegui = new JButton("Prosegui");
		btnProsegui.setBounds(109, 161, 146, 25);
		btnProsegui.setBackground(SystemColor.textHighlight);
		btnProsegui.setForeground(Color.WHITE);
		panel.add(btnProsegui);

		btnProsegui.addActionListener(_ -> handleEmailLogin());
		getRootPane().setDefaultButton(btnProsegui);
	}


	/**
	 * Gestisce il processo di accesso o registrazione tramite email.
	 *
	 * <p>Il metodo:
	 * <ol>
	 *   <li>Recupera l'email inserita dall'utente</li>
	 *   <li>Valida il formato dell'email tramite {@link AccessController}</li>
	 *   <li>Verifica se l'email è già registrata nel sistema</li>
	 *   <li>Reindirizza a:
	 *     <ul>
	 *       <li>{@link ViewAccessoConPassword} se l'utente esiste</li>
	 *       <li>{@link ViewRegistrazione} se è un nuovo utente</li>
	 *     </ul>
	 *   </li>
	 *   <li>Chiude la finestra corrente</li>
	 * </ol>
	 *
	 * @throws SQLException Se si verifica un errore durante la verifica dell'utente nel database
	 */
	private void handleEmailLogin() {
		String email = txtEmail.getText();

		AccessController controller = new AccessController();

		if (!controller.isValidEmail(email, false)) {
			JOptionPane.showMessageDialog(this, "Indirizzo email non valido");
			return;
		}

		email = email.trim();

		try {
			if (controller.checkUtente(email)) {
				new ViewAccessoConPassword(email).setVisible(true);
			} else {
				new ViewRegistrazione(email).setVisible(true);
			}
			dispose();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Gestisce il token OAuth ricevuto da un provider esterno.
	 *
	 * <p>Il metodo:
	 * <ol>
	 *   <li>Verifica che il token non sia nullo o vuoto</li>
	 *   <li>Delega al {@link OAuthController} l'estrazione dell'email dal token</li>
	 *   <li>Se l'email è valida, apre la {@link ViewDashboard} e chiude la finestra corrente</li>
	 *   <li>Altrimenti mostra un messaggio di errore</li>
	 * </ol>
	 *
	 * @param token Token OAuth ricevuto dal provider (Google o Facebook)
	 * @param provider Nome del provider ("Google" o "Facebook")
	 */
	private void handleProviderToken(String token, String provider) {
		if (token == null || token.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Nessun token rilevato");
			return;
		}

		String email = oauthController.handleOAuthLogin(token, provider);
		if (email != null) {
			ViewDashboard dashboard = new ViewDashboard(email);
			dashboard.setLocationRelativeTo(null);
			dashboard.setVisible(true);
			dispose();
		}
	}


	/**
	 * Avvia il processo di autenticazione OAuth tramite Facebook.
	 *
	 * <p>Apre il browser predefinito con l'URL di autorizzazione Facebook che include:
	 * <ul>
	 *   <li>Client ID dell'applicazione</li>
	 *   <li>URI di redirect configurato</li>
	 *   <li>Scope richiesti (email e public_profile)</li>
	 *   <li>Tipo di risposta: token</li>
	 * </ul>
	 *
	 * @throws Exception Se si verifica un errore durante l'apertura del browser
	 */
	private void loginWithFacebook() {
		try {
			String url = "https://www.facebook.com/v11.0/dialog/oauth?"
					+ "client_id=1445081039790531"
					+ "&redirect_uri=https://manubxx.github.io/fb-callback-redirect/callbackfb"
					+ "&scope=email,public_profile"
					+ "&response_type=token";

			Desktop.getDesktop().browse(new URI(url));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Errore login Facebook");
		}
	}

	/**
	 * Avvia il processo di autenticazione OAuth tramite Google.
	 *
	 * <p>Apre il browser predefinito con l'URL di autorizzazione Google che include:
	 * <ul>
	 *   <li>Client ID dell'applicazione</li>
	 *   <li>URI di redirect configurato</li>
	 *   <li>Tipo di risposta: token</li>
	 *   <li>Scope richiesti (openid, profile, email)</li>
	 * </ul>
	 *
	 * @throws Exception Se si verifica un errore durante l'apertura del browser
	 */
	private void loginWithGoogle() {
		try {
			String url = "https://accounts.google.com/o/oauth2/v2/auth?"
					+ "client_id=1099039266131-kt4al5u1r4ldd4q64h9euh3a9pjpeu98.apps.googleusercontent.com"
					+ "&redirect_uri=https://manubxx.github.io/google-callback-redirect/callbackgoogle"
					+ "&response_type=token"
					+ "&scope=openid%20profile%20email";

			Desktop.getDesktop().browse(new URI(url));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Errore login Google");
		}
	}


	/**
	 * Mostra una finestra di dialogo per confermare l'accesso OAuth tramite token dalla clipboard.
	 *
	 * <p>Il metodo crea una finestra modale con un pulsante "Conferma Accesso" che:
	 * <ol>
	 *   <li>Recupera il contenuto della clipboard di sistema</li>
	 *   <li>Interpreta il contenuto come stringa (token OAuth)</li>
	 *   <li>Passa il token al metodo {@link #handleProviderToken(String, String)}</li>
	 *   <li>Chiude la finestra di dialogo</li>
	 * </ol>
	 *
	 * <p>Questa finestra viene mostrata dopo che l'utente ha completato l'autenticazione
	 * nel browser e ha copiato il token nella clipboard.
	 *
	 * @param provider Nome del provider ("Google" o "Facebook") per personalizzare il titolo
	 */
	private void showTokenConfirmationDialog(String provider) {
		JFrame frame = new JFrame("Conferma Accesso " + provider);
		GuiUtils.setIconaFinestra(frame);

		JButton btn = new JButton("Conferma Accesso");
		btn.addActionListener(_ -> {
			try {
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				String token = (String) clipboard.getData(DataFlavor.stringFlavor);
				handleProviderToken(token, provider);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			frame.dispose();
		});

		frame.add(btn);
		frame.setSize(350, 150);
		frame.setLocationRelativeTo(this);
		frame.setVisible(true);
	}


	/**
	 * Crea una label stilizzata come pulsante per l'autenticazione OAuth.
	 *
	 * <p>La label viene configurata con:
	 * <ul>
	 *   <li>Testo centrato e colori personalizzati</li>
	 *   <li>Bordo arrotondato con colore più scuro dello sfondo</li>
	 *   <li>Listener per il click del mouse che esegue l'azione specificata</li>
	 * </ul>
	 *
	 * @param text Testo da visualizzare sulla label (es. "Facebook", "Google")
	 * @param bg Colore di sfondo della label
	 * @param action ActionListener da eseguire quando la label viene cliccata
	 * @return JLabel configurata come pulsante OAuth
	 */
	private JLabel createOAuthLabel(String text, Color bg, ActionListener action) {
		JLabel lbl = new JLabel(text, SwingConstants.CENTER);
		lbl.setOpaque(true);
		lbl.setBackground(bg);
		lbl.setForeground(Color.WHITE);
		lbl.setBorder(new LineBorder(bg.darker(), 1, true));
		lbl.setFont(new Font("Yu Gothic UI Semibold", Font.BOLD, 11));

		lbl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				action.actionPerformed(null);
			}
		});
		return lbl;
	}
}