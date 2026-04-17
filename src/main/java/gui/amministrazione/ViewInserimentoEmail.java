package gui.amministrazione;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import util.GuiUtils;

/**
 * Finestra per l'inserimento dell'email di lavoro di un nuovo utente. Questa
 * interfaccia permette agli amministratori di inserire l'email di un nuovo
 * agente immobiliare o amministratore di supporto da registrare nel sistema.
 *
 * <p>
 * La finestra include:
 * <ul>
 * <li>Campo per l'inserimento dell'email di lavoro</li>
 * <li>Validazione del formato email</li>
 * <li>Gestione del testo placeholder nel campo di input</li>
 * <li>Pulsante per procedere alla registrazione completa</li>
 * </ul>
 *
 * <p>
 * In base al tipo di inserimento selezionato, l'utente viene reindirizzato alla
 * finestra appropriata per completare la registrazione.
 *
 * @author IngSW2425_055 Team
 * @see ViewRegistraSupporto
 * @see ViewRegistraAgente
 * @see TipoInserimento
 */
public class ViewInserimentoEmail extends JFrame {
	/**
	 * Enumerazione che definisce il tipo di utente da registrare nel sistema.
	 * Utilizzata per determinare quale interfaccia di registrazione mostrare dopo
	 * l'inserimento dell'email.
	 *
	 * <p>
	 * I valori possibili sono:
	 * <ul>
	 * <li><b>SUPPORTO</b>: Amministratore di supporto con privilegi limitati</li>
	 * <li><b>AGENTE</b>: Agente immobiliare con funzionalità di gestione
	 * immobili</li>
	 * </ul>
	 *
	 * @see ViewInserimentoEmail
	 */
	public enum TipoInserimento {
		/** Tipo per registrare un nuovo amministratore di supporto */
		SUPPORTO,

		/** Tipo per registrare un nuovo agente immobiliare */
		AGENTE
	}

	private static final long serialVersionUID = 1L;
	// attributi
	private final JPanel contentPane;
	private final JPanel panel;
	private final TipoInserimento tipoInserimento;
	private final JTextField txtEmail;

	// costruttore

	private String campoPieno = "E-mail";

	// metodi

	/**
	 * Costruttore della finestra di inserimento email. Inizializza l'interfaccia
	 * grafica e configura i componenti per l'inserimento dell'email di lavoro di un
	 * nuovo utente.
	 *
	 * <p>
	 * La finestra viene configurata in base al tipo di utente da registrare:
	 * <ul>
	 * <li><b>SUPPORTO</b>: Per registrare un nuovo amministratore di supporto</li>
	 * <li><b>AGENTE</b>: Per registrare un nuovo agente immobiliare</li>
	 * </ul>
	 *
	 * @param agenzia Nome dell'agenzia a cui associare il nuovo utente
	 * @param tipo    Tipo di utente da registrare (SUPPORTO o AGENTE)
	 *
	 * @see TipoInserimento
	 * @see GuiUtils#setIconaFinestra(JFrame)
	 */
	public ViewInserimentoEmail(String agenzia, TipoInserimento tipo) {
		setTitle("DietiEstates25 - Inserimento dell'e-mail di lavoro");
		// Imposta l'icona di DietiEstates25 alla finestra in uso
		GuiUtils.setIconaFinestra(this);

		tipoInserimento = tipo;

		setResizable(false);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 643, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		panel = new JPanel();
		panel.setBackground(SystemColor.menu);
		panel.setBounds(0, 0, 631, 265);
		contentPane.add(panel);
		panel.setLayout(null);
		SwingUtilities.invokeLater(() -> requestFocusInWindow());

		txtEmail = new JTextField();
		txtEmail.setText("E-mail");
		txtEmail.setCaretColor(Color.DARK_GRAY);
		txtEmail.setDisabledTextColor(Color.DARK_GRAY);

		txtEmail.setBounds(155, 101, 320, 22);
		panel.add(txtEmail);

		// se premo sul campo e-mail
		txtEmail.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				txtEmail.setText("");
			}
		});

		// se premo fuori il campo e-mail
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (txtEmail.getText().trim().isEmpty()) {
					txtEmail.setText("E-mail");
				}
			}
		});

		// se scrivo sul campo e-mail
		txtEmail.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				campoPieno = txtEmail.getText();
				if (campoPieno.equals("E-mail")) {
					txtEmail.setText("");
				}
			}
		});

		final JLabel lblErroreEmail = new JLabel("");
		lblErroreEmail.setHorizontalAlignment(SwingConstants.CENTER);
		lblErroreEmail.setForeground(Color.RED);
		lblErroreEmail.setBounds(76, 125, 478, 22);
		panel.add(lblErroreEmail);

		// Bottone Procedi
		final JButton btnProcedi = new JButton("Procedi");
		getRootPane().setDefaultButton(btnProcedi);
		btnProcedi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final String email = txtEmail.getText().trim();

				// Controllo se l'email è vuota o contiene solo il placeholder
				if (email.isEmpty() || email.equals("E-mail")) {
					lblErroreEmail.setText("Inserire un indirizzo email valido.");
					return;
				}

				// Espressione regolare per validare il formato email con estensioni specifiche
				final String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+(it|com)$";

				if (!email.matches(emailRegex)) {
					lblErroreEmail.setText("Formato email non valido. (esempio: nome@dominio.it)");
					return;
				}

				// Se la validazione passa
				lblErroreEmail.setText("");

				SwingUtilities.invokeLater(() -> {
					if (tipoInserimento == TipoInserimento.SUPPORTO) {
						final ViewRegistraSupporto view = new ViewRegistraSupporto(email, agenzia);
						view.setLocationRelativeTo(null);
						view.setVisible(true);
					} else if (tipoInserimento == TipoInserimento.AGENTE) {
						final ViewRegistraAgente view = new ViewRegistraAgente(email, agenzia);
						view.setLocationRelativeTo(null);
						view.setVisible(true);
					}
				});
				dispose();
			}
		});
		btnProcedi.setFocusable(false);
		btnProcedi.setForeground(Color.WHITE);
		btnProcedi.setBackground(SystemColor.textHighlight);
		btnProcedi.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 13));
		btnProcedi.setBounds(215, 191, 200, 25);
		panel.add(btnProcedi);

		final JLabel lblDescrizione = new JLabel("Inserisci l'e-mail di lavoro dell'utente");
		lblDescrizione.setHorizontalAlignment(SwingConstants.CENTER);
		lblDescrizione.setFont(new Font("Segoe UI", Font.BOLD, 18));
		lblDescrizione.setBounds(0, 36, 631, 33);
		panel.add(lblDescrizione);

	}

	/**
	 * Configura il comportamento di un campo di testo con testo placeholder. Quando
	 * il campo riceve il focus, il testo placeholder viene rimosso. Quando perde il
	 * focus e il campo è vuoto, il testo placeholder viene ripristinato.
	 *
	 * <p>
	 * Questo metodo fornisce un'interfaccia utente più intuitiva per i campi di
	 * input che richiedono un testo di esempio per guidare l'utente.
	 *
	 * @param field Campo di testo da configurare
	 * @param text  Testo placeholder da visualizzare quando il campo è vuoto
	 *
	 * @see FocusAdapter
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