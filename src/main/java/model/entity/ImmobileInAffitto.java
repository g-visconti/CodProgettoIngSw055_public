package model.entity;

import org.json.JSONObject;

/**
 * Classe che rappresenta un immobile disponibile per l'affitto.
 * Estende la classe {@link Immobile} aggiungendo l'informazione specifica
 * del prezzo mensile di affitto.
 *
 * <p>Questa specializzazione di Immobile viene utilizzata per:
 * <ul>
 *   <li>Differenziare gli immobili in affitto da quelli in vendita
 *   <li>Gestire specifiche regole e calcoli relativi all'affitto
 *   <li>Filtrare gli immobili in base alla disponibilità per affitto
 *   <li>Visualizzare il prezzo mensile anziché il prezzo di vendita
 * </ul>
 *
 * <p>Il prezzo mensile rappresenta il canone di affitto richiesto
 * per l'utilizzo dell'immobile per un periodo di un mese.
 *
 * @author IngSW2425_055 Team
 * @see Immobile
 * @see ImmobileInVendita
 */
public class ImmobileInAffitto extends Immobile {

	private double prezzoMensile;

	/**
	 * Costruttore completo per la creazione di un immobile in affitto.
	 * Richiama il costruttore della superclasse {@link Immobile} e inizializza
	 * l'attributo specifico del prezzo mensile.
	 *
	 * @param titolo            Titolo dell'annuncio dell'immobile
	 * @param indirizzo         Indirizzo completo dell'immobile
	 * @param localita          Località/città dell'immobile
	 * @param dimensione        Superficie in metri quadrati
	 * @param descrizione       Descrizione dettagliata dell'immobile
	 * @param tipologia         Tipologia (dovrebbe essere "Affitto")
	 * @param filtri            Oggetto JSON contenente le caratteristiche tecniche
	 * @param agenteAssociato   ID dell'agente immobiliare responsabile
	 * @param prezzoMensile     Prezzo mensile di affitto in euro
	 */
	public ImmobileInAffitto(String titolo, String indirizzo, String localita, int dimensione, String descrizione,
			String tipologia, JSONObject filtri, String agenteAssociato,
			double prezzoMensile) {
		super(titolo, indirizzo, localita, dimensione, descrizione, tipologia, filtri, agenteAssociato);
		this.prezzoMensile = prezzoMensile;
	}

	/**
	 * Costruttore vuoto per la creazione di un immobile in affitto senza parametri.
	 * Inizializza tutti gli attributi con valori di default.
	 * Utile per l'inizializzazione tramite setter o deserializzazione.
	 */
	public ImmobileInAffitto() {
		super("", "", "", 0, "", "", null, null);
		prezzoMensile = 0;
	}

	/**
	 * Restituisce il prezzo mensile di affitto dell'immobile.
	 *
	 * @return Prezzo mensile in euro
	 */
	public double getPrezzoMensile() {
		return prezzoMensile;
	}

	/**
	 * Imposta il prezzo mensile di affitto dell'immobile.
	 *
	 * @param prezzoMensile Nuovo prezzo mensile in euro
	 */
	public void setPrezzoMensile(double prezzoMensile) {
		this.prezzoMensile = prezzoMensile;
	}
}