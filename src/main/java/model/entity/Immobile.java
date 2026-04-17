package model.entity;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.json.JSONObject;

/**
 * Classe che rappresenta un immobile all'interno del sistema immobiliare.
 * Contiene tutte le informazioni relative a una proprietà in vendita o affitto,
 * incluse immagini, caratteristiche tecniche e metadati.
 *
 * <p>Questa classe è la base per le specializzazioni {@link ImmobileInAffitto}
 * e {@link ImmobileInVendita} e viene utilizzata per:
 * <ul>
 *   <li>Memorizzare i dati degli immobili nel database
 *   <li>Visualizzare gli annunci nelle interfacce utente
 *   <li>Filtrare e cercare immobili in base alle caratteristiche
 *   <li>Gestire le immagini associate all'immobile
 *   <li>Associare l'immobile all'agente responsabile
 * </ul>
 *
 * <p>Le immagini sono gestite come array di byte per facilitare
 * la memorizzazione nel database, con metodi di utilità per la
 * conversione in/da Base64 per la visualizzazione web.
 *
 * @author IngSW2425_055 Team
 * @see ImmobileInAffitto
 * @see ImmobileInVendita
 * @see JSONObject
 */
public class Immobile {

	private List<byte[]> foto;
	private long id;
	private String titolo;
	private String indirizzo;
	private String localita;
	private int dimensione;
	private String descrizione;
	private String tipologia;
	// Campi estratti dal JSON "filtri"
	private int numeroLocali;
	private int numeroBagni;
	private int piano;
	private boolean ascensore;
	private boolean climatizzazione;
	private boolean portineria;
	private boolean postoAuto;
	private String agenteAssociato;

	// Costruttori, getter e setter

	/**
	 * Costruttore principale per la creazione di un immobile con tutti i dati necessari.
	 * Estrae automaticamente i filtri dall'oggetto JSON e li assegna ai campi corrispondenti.
	 *
	 * @param titolo            Titolo dell'annuncio dell'immobile
	 * @param indirizzo         Indirizzo completo dell'immobile
	 * @param localita          Località/città dell'immobile
	 * @param dimensione        Superficie in metri quadrati
	 * @param descrizione       Descrizione dettagliata dell'immobile
	 * @param tipologia         Tipologia ("Vendita" o "Affitto")
	 * @param filtri            Oggetto JSON contenente le caratteristiche tecniche
	 * @param agenteAssociato   ID dell'agente immobiliare responsabile
	 */
	public Immobile(String titolo, String indirizzo, String localita, int dimensione, String descrizione,
			String tipologia, JSONObject filtri, String agenteAssociato) {
		setTitolo(titolo);
		setIndirizzo(indirizzo);
		setLocalita(localita);
		setDimensione(dimensione);
		setFiltriFromJson(filtri);
		setTipologia(tipologia);
		setDescrizione(descrizione);
		setAgenteAssociato(agenteAssociato);
	}

	/**
	 * Costruttore vuoto per la creazione di un immobile senza parametri iniziali.
	 * Utile per l'inizializzazione tramite setter o framework di mapping.
	 */
	public Immobile() {
	}

	/**
	 * Imposta la lista delle immagini associate all'immobile.
	 * Ogni immagine è rappresentata come array di byte.
	 *
	 * @param immagini Lista di array di byte rappresentanti le immagini
	 */
	public void setImmagini(List<byte[]> immagini) {
		foto = immagini;
	}

	/**
	 * Restituisce la lista delle immagini associate all'immobile.
	 *
	 * @return Lista di array di byte contenenti le immagini
	 */
	public List<byte[]> getImmagini() {
		return foto;
	}

	/**
	 * Restituisce la prima immagine dell'immobile codificata in Base64.
	 * Utilizzata principalmente come icona/anteprima nelle liste.
	 *
	 * @return Stringa Base64 della prima immagine, o null se non ci sono immagini
	 */
	public String getIcon() {
		if (foto != null && !foto.isEmpty()) {
			final byte[] img = foto.get(0);
			return Base64.getEncoder().encodeToString(img);
		}
		return null;
	}

	/**
	 * Imposta l'icona dell'immobile da una stringa Base64.
	 * Crea una nuova lista di immagini con questa come prima immagine.
	 *
	 * @param base64 Stringa Base64 rappresentante l'immagine icona
	 */
	public void setIcon(String base64) {
		if (base64 != null && !base64.isEmpty()) {
			final byte[] imageBytes = Base64.getDecoder().decode(base64);
			foto = new ArrayList<>();
			foto.add(imageBytes);
		}
	}

	/**
	 * Restituisce il titolo dell'annuncio dell'immobile.
	 *
	 * @return Titolo dell'immobile
	 */
	public String getTitolo() {
		return titolo;
	}

	/**
	 * Imposta il titolo dell'annuncio dell'immobile.
	 *
	 * @param titolo Nuovo titolo per l'immobile
	 */
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	/**
	 * Restituisce l'indirizzo completo dell'immobile.
	 *
	 * @return Indirizzo dell'immobile
	 */
	public String getIndirizzo() {
		return indirizzo;
	}

	/**
	 * Imposta l'indirizzo completo dell'immobile.
	 *
	 * @param indirizzo Nuovo indirizzo dell'immobile
	 */
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	/**
	 * Restituisce la località (città) dell'immobile.
	 *
	 * @return Località dell'immobile
	 */
	public String getLocalita() {
		return localita;
	}

	/**
	 * Imposta la località (città) dell'immobile.
	 *
	 * @param localita Nuova località dell'immobile
	 */
	public void setLocalita(String localita) {
		this.localita = localita;
	}

	/**
	 * Restituisce la dimensione (superficie) dell'immobile in metri quadrati.
	 *
	 * @return Superficie in m²
	 */
	public int getDimensione() {
		return dimensione;
	}

	/**
	 * Imposta la dimensione (superficie) dell'immobile in metri quadrati.
	 *
	 * @param dimensione Nuova superficie in mq
	 */
	public void setDimensione(int dimensione) {
		this.dimensione = dimensione;
	}

	/**
	 * Restituisce la descrizione dettagliata dell'immobile.
	 *
	 * @return Descrizione dell'immobile
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * Imposta la descrizione dettagliata dell'immobile.
	 *
	 * @param descrizione Nuova descrizione dell'immobile
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * Restituisce la tipologia dell'immobile ("Vendita" o "Affitto").
	 *
	 * @return Tipologia dell'immobile
	 */
	public String getTipologia() {
		return tipologia;
	}

	/**
	 * Imposta la tipologia dell'immobile.
	 *
	 * @param tipologia Nuova tipologia ("Vendita" o "Affitto")
	 */
	public void setTipologia(String tipologia) {
		this.tipologia = tipologia;
	}

	/**
	 * Restituisce l'ID univoco dell'immobile nel database.
	 *
	 * @return ID dell'immobile
	 */
	public long getId() {
		return id;
	}

	/**
	 * Imposta l'ID univoco dell'immobile.
	 *
	 * @param id Nuovo ID dell'immobile
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Restituisce il numero di locali dell'immobile.
	 *
	 * @return Numero di locali
	 */
	public int getNumeroLocali() {
		return numeroLocali;
	}

	/**
	 * Imposta il numero di locali dell'immobile.
	 *
	 * @param numeroLocali Nuovo numero di locali
	 */
	public void setNumeroLocali(int numeroLocali) {
		this.numeroLocali = numeroLocali;
	}

	/**
	 * Restituisce il numero di bagni dell'immobile.
	 *
	 * @return Numero di bagni
	 */
	public int getNumeroBagni() {
		return numeroBagni;
	}

	/**
	 * Imposta il numero di bagni dell'immobile.
	 *
	 * @param numeroBagni Nuovo numero di bagni
	 */
	public void setNumeroBagni(int numeroBagni) {
		this.numeroBagni = numeroBagni;
	}

	/**
	 * Restituisce il piano dell'immobile.
	 * Valore -1 indica che il piano non è specificato.
	 *
	 * @return Piano dell'immobile
	 */
	public int getPiano() {
		return piano;
	}

	/**
	 * Imposta il piano dell'immobile.
	 *
	 * @param piano Nuovo piano (-1 per non specificato)
	 */
	public void setPiano(int piano) {
		this.piano = piano;
	}

	/**
	 * Verifica se l'immobile dispone di ascensore.
	 *
	 * @return true se ha ascensore, false altrimenti
	 */
	public boolean isAscensore() {
		return ascensore;
	}

	/**
	 * Imposta la presenza dell'ascensore nell'immobile.
	 *
	 * @param ascensore true se ha ascensore, false altrimenti
	 */
	public void setAscensore(boolean ascensore) {
		this.ascensore = ascensore;
	}

	/**
	 * Verifica se l'immobile dispone di climatizzazione.
	 *
	 * @return true se ha climatizzazione, false altrimenti
	 */
	public boolean getClimatizzazione() {
		return climatizzazione;
	}

	/**
	 * Imposta la presenza della climatizzazione nell'immobile.
	 *
	 * @param climatizzazione true se ha climatizzazione, false altrimenti
	 */
	public void setClimatizzazione(boolean climatizzazione) {
		this.climatizzazione = climatizzazione;
	}

	/**
	 * Verifica se l'immobile dispone di portineria.
	 *
	 * @return true se ha portineria, false altrimenti
	 */
	public boolean isPortineria() {
		return portineria;
	}

	/**
	 * Imposta la presenza della portineria nell'immobile.
	 *
	 * @param portineria true se ha portineria, false altrimenti
	 */
	public void setPortineria(boolean portineria) {
		this.portineria = portineria;
	}

	/**
	 * Verifica se l'immobile dispone di posto auto.
	 *
	 * @return true se ha posto auto, false altrimenti
	 */
	public boolean isPostoauto() {
		return postoAuto;
	}

	/**
	 * Imposta la presenza del posto auto nell'immobile.
	 *
	 * @param postoauto true se ha posto auto, false altrimenti
	 */
	public void setPostoauto(boolean postoauto) {
		postoAuto = postoauto;
	}

	/**
	 * Converte le caratteristiche tecniche dell'immobile in un oggetto JSON.
	 * Utilizzato per serializzare i dati per il database o l'API.
	 *
	 * @return Oggetto JSON contenente tutti i filtri/characteristiche
	 */
	public JSONObject getFiltriAsJson() {
		final JSONObject filtriJson = new JSONObject();
		filtriJson.put("numeroLocali", numeroLocali);
		filtriJson.put("numeroBagni", numeroBagni);
		filtriJson.put("piano", piano);
		filtriJson.put("ascensore", ascensore);
		filtriJson.put("portineria", portineria);
		filtriJson.put("riscaldamento", climatizzazione);
		filtriJson.put("postoAuto", postoAuto);
		return filtriJson;
	}

	/**
	 * Estrae e imposta le caratteristiche tecniche da un oggetto JSON.
	 * I campi mancanti nel JSON mantengono i valori di default.
	 *
	 * @param filtri Oggetto JSON contenente le caratteristiche tecniche
	 */
	public void setFiltriFromJson(JSONObject filtri) {
		if (filtri == null) {
			filtri = new JSONObject();
		}

		if (filtri.has("numeroLocali")) {
			numeroLocali = filtri.getInt("numeroLocali");
		}

		if (filtri.has("numeroBagni")) {
			numeroBagni = filtri.getInt("numeroBagni");
		}

		if (filtri.has("piano")) {
			piano = filtri.getInt("piano");
		}

		if (filtri.has("ascensore")) {
			ascensore = filtri.getBoolean("ascensore");
		}

		if (filtri.has("portineria")) {
			portineria = filtri.getBoolean("portineria");
		}

		if (filtri.has("riscaldamento")) {
			climatizzazione = filtri.getBoolean("riscaldamento");
		}

		if (filtri.has("postoAuto")) {
			postoAuto = filtri.getBoolean("postoAuto");
		}
	}

	/**
	 * Restituisce l'ID dell'agente immobiliare associato all'immobile.
	 *
	 * @return ID dell'agente responsabile
	 */
	public String getAgenteAssociato() {
		return agenteAssociato;
	}

	/**
	 * Imposta l'ID dell'agente immobiliare associato all'immobile.
	 *
	 * @param agenteAssociato ID del nuovo agente responsabile
	 */
	public void setAgenteAssociato(String agenteAssociato) {
		this.agenteAssociato = agenteAssociato;
	}
}