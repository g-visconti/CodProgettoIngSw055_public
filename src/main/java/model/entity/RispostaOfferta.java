package model.entity;

import java.time.LocalDateTime;

/**
 * Classe che rappresenta una risposta ad un'offerta iniziale da parte di un agente immobiliare.
 * Contiene tutte le informazioni relative alla risposta dell'agente, inclusa l'eventuale controproposta.
 *
 * <p>Questa classe viene utilizzata per:
 * <ul>
 *   <li>Registrare le risposte degli agenti alle offerte dei clienti
 *   <li>Gestire il flusso di negoziazione tra cliente e agente
 *   <li>Tracciare le controproposte economiche
 *   <li>Mantenere una cronologia completa delle trattative
 * </ul>
 *
 * <p>Le risposte possono essere di tre tipi:
 * <ol>
 *   <li><b>Accettata</b>: l'agente accetta l'offerta del cliente
 *   <li><b>Rifiutata</b>: l'agente rifiuta l'offerta del cliente
 *   <li><b>Controproposta</b>: l'agente propone un importo diverso
 * </ol>
 *
 * @author IngSW2425_055 Team
 * @see OffertaIniziale
 * @see AgenteImmobiliare
 */
public class RispostaOfferta {

	/**
	 * Identificativo univoco della risposta nel database.
	 */
	private Long idRisposta;

	/**
	 * ID dell'offerta iniziale a cui questa risposta si riferisce.
	 */
	private Long offertaInizialeAssociata;

	/**
	 * ID dell'agente immobiliare che ha fornito la risposta.
	 */
	private String agenteAssociato;

	/**
	 * Nome dell'agente che ha fornito la risposta.
	 * Utilizzato per la visualizzazione senza dover recuperare l'oggetto Agente completo.
	 */
	private String nomeAgente;

	/**
	 * Cognome dell'agente che ha fornito la risposta.
	 * Utilizzato per la visualizzazione senza dover recuperare l'oggetto Agente completo.
	 */
	private String cognomeAgente;

	/**
	 * Tipo di risposta fornita dall'agente.
	 * Valori possibili: "Accettata", "Rifiutata", "Controproposta".
	 */
	private String tipoRisposta;

	/**
	 * Importo della controproposta (solo se tipoRisposta = "Controproposta").
	 * Per altri tipi di risposta, questo campo è null.
	 */
	private Double importoControproposta;

	/**
	 * Data e ora in cui la risposta è stata fornita.
	 */
	private LocalDateTime dataRisposta;

	/**
	 * Flag che indica se la risposta è ancora attiva/valida.
	 * Una risposta può essere disattivata se superata da eventi successivi.
	 */
	private Boolean attiva;

	/**
	 * Costruttore principale per la creazione di una nuova risposta ad un'offerta.
	 * Imposta automaticamente la data corrente e lo stato iniziale ad attiva=true.
	 *
	 * @param offertaInizialeAssociata ID dell'offerta iniziale a cui si risponde
	 * @param agenteAssociato          ID dell'agente che fornisce la risposta
	 * @param nomeAgente               Nome dell'agente (per visualizzazione)
	 * @param cognomeAgente            Cognome dell'agente (per visualizzazione)
	 * @param tipoRisposta             Tipo di risposta ("Accettata", "Rifiutata", "Controproposta")
	 * @param importoControproposta    Importo della controproposta (solo per tipo "Controproposta")
	 */
	public RispostaOfferta(Long offertaInizialeAssociata, String agenteAssociato, String nomeAgente,
			String cognomeAgente, String tipoRisposta, Double importoControproposta) {
		this.offertaInizialeAssociata = offertaInizialeAssociata;
		this.agenteAssociato = agenteAssociato;
		setNomeAgente(nomeAgente);
		setCognomeAgente(cognomeAgente);
		this.tipoRisposta = tipoRisposta;
		this.importoControproposta = importoControproposta;
		dataRisposta = LocalDateTime.now().withNano(0);
		attiva = true; // Stato iniziale predefinito
	}

	/**
	 * Costruttore vuoto per la creazione di una risposta senza parametri iniziali.
	 * Utile per l'inizializzazione tramite setter o deserializzazione.
	 */
	public RispostaOfferta() {
	}

	// Getter e Setter

	/**
	 * Restituisce l'identificativo univoco della risposta.
	 *
	 * @return ID della risposta
	 */
	public Long getIdRisposta() {
		return idRisposta;
	}

	/**
	 * Imposta l'identificativo univoco della risposta.
	 *
	 * @param idRisposta Nuovo ID della risposta
	 */
	public void setIdRisposta(Long idRisposta) {
		this.idRisposta = idRisposta;
	}

	/**
	 * Restituisce l'ID dell'offerta iniziale associata a questa risposta.
	 *
	 * @return ID dell'offerta iniziale
	 */
	public Long getOffertaInizialeAssociata() {
		return offertaInizialeAssociata;
	}

	/**
	 * Imposta l'ID dell'offerta iniziale associata a questa risposta.
	 *
	 * @param offertaInizialeAssociata Nuovo ID dell'offerta iniziale
	 */
	public void setOffertaInizialeAssociata(Long offertaInizialeAssociata) {
		this.offertaInizialeAssociata = offertaInizialeAssociata;
	}

	/**
	 * Restituisce l'ID dell'agente che ha fornito la risposta.
	 *
	 * @return ID dell'agente
	 */
	public String getAgenteAssociato() {
		return agenteAssociato;
	}

	/**
	 * Imposta l'ID dell'agente che ha fornito la risposta.
	 *
	 * @param agenteAssociato Nuovo ID dell'agente
	 */
	public void setAgenteAssociato(String agenteAssociato) {
		this.agenteAssociato = agenteAssociato;
	}

	/**
	 * Restituisce il nome dell'agente che ha fornito la risposta.
	 *
	 * @return Nome dell'agente
	 */
	public String getNomeAgente() {
		return nomeAgente;
	}

	/**
	 * Imposta il nome dell'agente che ha fornito la risposta.
	 *
	 * @param nomeAgente Nuovo nome dell'agente
	 */
	public void setNomeAgente(String nomeAgente) {
		this.nomeAgente = nomeAgente;
	}

	/**
	 * Restituisce il cognome dell'agente che ha fornito la risposta.
	 *
	 * @return Cognome dell'agente
	 */
	public String getCognomeAgente() {
		return cognomeAgente;
	}

	/**
	 * Imposta il cognome dell'agente che ha fornito la risposta.
	 *
	 * @param cognomeAgente Nuovo cognome dell'agente
	 */
	public void setCognomeAgente(String cognomeAgente) {
		this.cognomeAgente = cognomeAgente;
	}

	/**
	 * Restituisce il tipo di risposta fornita.
	 *
	 * @return Tipo di risposta ("Accettata", "Rifiutata", "Controproposta")
	 */
	public String getTipoRisposta() {
		return tipoRisposta;
	}

	/**
	 * Imposta il tipo di risposta.
	 *
	 * @param tipoRisposta Nuovo tipo di risposta
	 */
	public void setTipoRisposta(String tipoRisposta) {
		this.tipoRisposta = tipoRisposta;
	}

	/**
	 * Restituisce l'importo della controproposta (se presente).
	 *
	 * @return Importo della controproposta, o null se non è una controproposta
	 */
	public Double getImportoControproposta() {
		return importoControproposta;
	}

	/**
	 * Imposta l'importo della controproposta.
	 *
	 * @param importoControproposta Nuovo importo della controproposta
	 */
	public void setImportoControproposta(Double importoControproposta) {
		this.importoControproposta = importoControproposta;
	}

	/**
	 * Restituisce la data e ora in cui la risposta è stata fornita.
	 *
	 * @return Data e ora della risposta
	 */
	public LocalDateTime getDataRisposta() {
		return dataRisposta;
	}

	/**
	 * Imposta la data e ora in cui la risposta è stata fornita.
	 *
	 * @param dataRisposta Nuova data e ora della risposta
	 */
	public void setDataRisposta(LocalDateTime dataRisposta) {
		this.dataRisposta = dataRisposta;
	}

	/**
	 * Verifica se la risposta è ancora attiva/valida.
	 *
	 * @return true se la risposta è attiva, false altrimenti
	 */
	public Boolean getAttiva() {
		return attiva;
	}

	/**
	 * Imposta lo stato di attività della risposta.
	 *
	 * @param attiva Nuovo stato di attività
	 */
	public void setAttiva(Boolean attiva) {
		this.attiva = attiva;
	}
}