package model.entity;

/**
 * Classe che rappresenta un insieme di filtri di ricerca per gli immobili.
 * Contiene tutti i criteri di ricerca che un utente può applicare per
 * filtrare i risultati della ricerca immobiliare.
 *
 * <p>Questa classe viene utilizzata per:
 * <ul>
 *   <li>Raccogliere i criteri di ricerca inseriti dall'utente nell'interfaccia
 *   <li>Passare i parametri di filtro al controller per la ricerca nel database
 *   <li>Serializzare i criteri di ricerca per salvare preferenze o ricerche salvate
 *   <li>Validare e normalizzare i valori dei filtri prima dell'esecuzione della query
 * </ul>
 *
 * <p>Tutti gli attributi sono pubblici per facilitare l'accesso diretto,
 * in quanto questa classe funge principalmente da DTO (Data Transfer Object).
 *
 * @author IngSW2425_055 Team
 */
public class Filtri {

	// Attributi pubblici per accesso diretto

	/**
	 * Prezzo minimo dell'immobile (in euro).
	 * Valore null indica che non è stato impostato alcun limite minimo.
	 */
	public Integer prezzoMin;

	/**
	 * Prezzo massimo dell'immobile (in euro).
	 * Valore null indica che non è stato impostato alcun limite massimo.
	 */
	public Integer prezzoMax;

	/**
	 * Superficie minima dell'immobile (in metri quadrati).
	 * Valore null indica che non è stato impostato alcun limite minimo.
	 */
	public Integer superficieMin;

	/**
	 * Superficie massima dell'immobile (in metri quadrati).
	 * Valore null indica che non è stato impostato alcun limite massimo.
	 */
	public Integer superficieMax;

	/**
	 * Piano dell'immobile.
	 * Può essere un numero come stringa (es. "2"), un intervallo (es. "1-3")
	 * o un valore speciale come "ultimo" o "terra".
	 * Valore null indica che non è stato impostato alcun filtro sul piano.
	 */
	public String piano;

	/**
	 * Numero minimo di locali dell'immobile.
	 * Valore null indica che non è stato impostato alcun filtro sui locali.
	 */
	public Integer numLocali;

	/**
	 * Numero minimo di bagni dell'immobile.
	 * Valore null indica che non è stato impostato alcun filtro sui bagni.
	 */
	public Integer numBagni;

	/**
	 * Presenza dell'ascensore nell'immobile.
	 * true = deve avere ascensore, false = non deve avere ascensore, null = non filtrato
	 */
	public Boolean ascensore;

	/**
	 * Presenza della portineria nell'immobile.
	 * true = deve avere portineria, false = non deve avere portineria, null = non filtrato
	 */
	public Boolean portineria;

	/**
	 * Presenza della climatizzazione nell'immobile.
	 * true = deve avere climatizzazione, false = non deve avere climatizzazione, null = non filtrato
	 */
	public Boolean climatizzazione;

	/**
	 * Costruttore vuoto per la creazione di un oggetto Filtri senza parametri.
	 * Utile per l'inizializzazione step-by-step tramite assegnazione diretta degli attributi.
	 * Tutti gli attributi rimangono null, indicando che nessun filtro è attivo.
	 */
	public Filtri() {
	}

	/**
	 * Costruttore completo per la creazione di un oggetto Filtri con tutti i criteri.
	 * Utilizzato quando si conoscono tutti i valori dei filtri da applicare.
	 *
	 * @param prezzoMin         Prezzo minimo dell'immobile (in euro), null per nessun limite
	 * @param prezzoMax         Prezzo massimo dell'immobile (in euro), null per nessun limite
	 * @param superficieMin     Superficie minima (in m²), null per nessun limite
	 * @param superficieMax     Superficie massima (in m²), null per nessun limite
	 * @param piano             Piano dell'immobile, null per nessun filtro
	 * @param numLocali         Numero minimo di locali, null per nessun filtro
	 * @param numBagni          Numero minimo di bagni, null per nessun filtro
	 * @param ascensore         Presenza ascensore, null per non filtrato
	 * @param portineria        Presenza portineria, null per non filtrato
	 * @param climatizzazione   Presenza climatizzazione, null per non filtrato
	 */
	public Filtri(Integer prezzoMin, Integer prezzoMax, Integer superficieMin, Integer superficieMax, String piano,
			Integer numLocali, Integer numBagni, Boolean ascensore, Boolean portineria, Boolean climatizzazione) {
		this.prezzoMin = prezzoMin;
		this.prezzoMax = prezzoMax;
		this.superficieMin = superficieMin;
		this.superficieMax = superficieMax;
		this.piano = piano;
		this.numLocali = numLocali;
		this.numBagni = numBagni;
		this.ascensore = ascensore;
		this.portineria = portineria;
		this.climatizzazione = climatizzazione;
	}
}