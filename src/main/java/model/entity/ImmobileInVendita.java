package model.entity;

import org.json.JSONObject;

/**
 * Classe che rappresenta un immobile disponibile per la vendita.
 * Estende la classe {@link Immobile} aggiungendo l'informazione specifica
 * del prezzo totale di vendita.
 *
 * <p>Questa specializzazione di Immobile viene utilizzata per:
 * <ul>
 *   <li>Differenziare gli immobili in vendita da quelli in affitto
 *   <li>Gestire specifiche regole e calcoli relativi alla vendita
 *   <li>Filtrare gli immobili in base alla disponibilità per la vendita
 *   <li>Visualizzare il prezzo totale anziché il canone mensile
 * </ul>
 *
 * <p>Il prezzo totale rappresenta il valore complessivo di vendita
 * dell'immobile, comprensivo di tutte le spese accessorie incluse
 * nell'offerta.
 *
 * @author IngSW2425_055 Team
 * @see Immobile
 * @see ImmobileInAffitto
 */
public class ImmobileInVendita extends Immobile {

	private int prezzoTotale;

	/**
	 * Costruttore completo per la creazione di un immobile in vendita.
	 * Richiama il costruttore della superclasse {@link Immobile} e inizializza
	 * l'attributo specifico del prezzo totale.
	 *
	 * @param titolo            Titolo dell'annuncio dell'immobile
	 * @param indirizzo         Indirizzo completo dell'immobile
	 * @param localita          Località/città dell'immobile
	 * @param dimensione        Superficie in metri quadrati
	 * @param descrizione       Descrizione dettagliata dell'immobile
	 * @param tipologia         Tipologia (dovrebbe essere "Vendita")
	 * @param filtri            Oggetto JSON contenente le caratteristiche tecniche
	 * @param agenteAssociato   ID dell'agente immobiliare responsabile
	 * @param prezzoTotale      Prezzo totale di vendita in euro
	 */
	public ImmobileInVendita(String titolo, String indirizzo, String localita, int dimensione, String descrizione,
			String tipologia, JSONObject filtri, String agenteAssociato,
			int prezzoTotale) {
		super(titolo, indirizzo, localita, dimensione, descrizione, tipologia, filtri, agenteAssociato);
		this.prezzoTotale = prezzoTotale;
	}

	/**
	 * Costruttore vuoto per la creazione di un immobile in vendita senza parametri.
	 * Inizializza tutti gli attributi con valori di default.
	 * Utile per l'inizializzazione tramite setter o deserializzazione.
	 */
	public ImmobileInVendita() {
		super("", "", "", 0, "", "", null, null);
		prezzoTotale = 0;
	}

	/**
	 * Restituisce il prezzo totale di vendita dell'immobile.
	 *
	 * @return Prezzo totale in euro
	 */
	public int getPrezzoTotale() {
		return prezzoTotale;
	}

	/**
	 * Imposta il prezzo totale di vendita dell'immobile.
	 *
	 * @param prezzoTotale Nuovo prezzo totale in euro
	 */
	public void setPrezzoTotale(int prezzoTotale) {
		this.prezzoTotale = prezzoTotale;
	}
}