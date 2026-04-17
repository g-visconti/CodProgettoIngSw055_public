package start;

import gui.ViewAccesso;

/**
 * Classe principale di avvio dell'applicazione DietiEstates25. Contiene il
 * metodo {@code main} che funge da punto di ingresso per l'applicazione e
 * gestisce la configurazione iniziale dell'interfaccia grafica.
 *
 * <p>
 * Questa classe fornisce diverse modalità di avvio per facilitare lo sviluppo e
 * il testing:
 * <ul>
 * <li>Accesso generico: schermata di login standard per nuovi utenti</li>
 * <li>Accesso diretto con ruoli predefiniti: Admin, Supporto, Agente,
 * Utente</li>
 * </ul>
 *
 * <p>
 * Modalità di selezione: La variabile {@code x} determina quale schermata viene
 * aperta all'avvio:
 * <ul>
 * <li>Valori 1-5: accesso diretto con account specifico</li>
 * <li>Altri valori: schermata di login standard</li>
 * </ul>
 *
 * @author IngSW2425_055 Team
 * @see ViewAccesso
 */
public class Main {

	/**
	 * Metodo principale di avvio dell'applicazione DietiEstates25. Gestisce
	 * l'inizializzazione dell'interfaccia grafica in base alla modalità selezionata
	 * per facilitare sviluppo, testing e dimostrazioni.
	 *
	 * <p>
	 * Il metodo utilizza un intero {@code x} per determinare il comportamento:
	 * <ul>
	 * <li>Se {@code x} è tra 1 e 5: apre direttamente la dashboard corrispondente
	 * all'account preconfigurato</li>
	 * <li>Altrimenti: apre la schermata di accesso standard per nuovo login</li>
	 * </ul>
	 *
	 * @param args Argomenti da riga di comando (non utilizzati nell'applicazione
	 *             corrente)
	 * @throws Exception Se si verifica un errore durante l'inizializzazione delle
	 *                   finestre grafiche
	 */
	public static void main(String[] args) {
		final ViewAccesso frame = new ViewAccesso();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

}