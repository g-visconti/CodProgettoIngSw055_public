package model.dto;

import model.entity.Filtri;

/**
 * Data Transfer Object (DTO) per i parametri di ricerca degli immobili.
 * Questa classe rappresenta tutti i criteri di ricerca che possono essere specificati
 * per filtrare gli annunci immobiliari nel sistema.
 *
 * <p>Il DTO include sia i parametri di ricerca di base (query testuale, tipologia)
 * che i filtri avanzati, oltre a informazioni sul tipo di ricerca e l'utente che
 * la sta effettuando.
 *
 * @author IngSW2425_055 Team
 * @see Filtri
 */
public class RicercaDTO {

	private String queryRicerca;
	private String tipologiaImmobile; // "Affitto" o "Vendita"
	private Filtri filtri;
	private String emailUtente; // Email dell'utente che effettua la ricerca (agente o cliente)
	private TipoRicerca tipoRicerca; // Flag per ricerca da agente o cliente

	/**
	 * Costruttore vuoto per RicercaDTO.
	 * Inizializza un oggetto con tutti i campi a null.
	 */
	public RicercaDTO() {
	}

	/**
	 * Costruttore completo per RicercaDTO.
	 * Inizializza tutti i parametri di ricerca con i valori forniti.
	 *
	 * @param queryRicerca Query testuale per la ricerca (es. "Milano centro")
	 * @param tipologiaImmobile Tipologia dell'immobile ("Affitto" o "Vendita")
	 * @param filtri Oggetto {@link Filtri} contenente i filtri avanzati di ricerca
	 * @param emailUtente Email dell'utente che effettua la ricerca
	 * @param tipoRicerca Tipo di ricerca specificato dall'enum {@link TipoRicerca}
	 */
	public RicercaDTO(String queryRicerca, String tipologiaImmobile, Filtri filtri, String emailUtente,
			TipoRicerca tipoRicerca) {
		this.queryRicerca = queryRicerca;
		this.tipologiaImmobile = tipologiaImmobile;
		this.filtri = filtri;
		this.emailUtente = emailUtente;
		this.tipoRicerca = tipoRicerca;
	}

	/**
	 * Restituisce la query testuale di ricerca.
	 *
	 * @return Query di ricerca testuale
	 */
	public String getQueryRicerca() {
		return queryRicerca;
	}

	/**
	 * Imposta la query testuale di ricerca.
	 *
	 * @param queryRicerca Query di ricerca testuale da impostare
	 */
	public void setQueryRicerca(String queryRicerca) {
		this.queryRicerca = queryRicerca;
	}

	/**
	 * Restituisce la tipologia dell'immobile cercato.
	 *
	 * @return Tipologia dell'immobile ("Affitto" o "Vendita")
	 */
	public String getTipologiaImmobile() {
		return tipologiaImmobile;
	}

	/**
	 * Imposta la tipologia dell'immobile cercato.
	 *
	 * @param tipologiaImmobile Tipologia dell'immobile da impostare ("Affitto" o "Vendita")
	 */
	public void setTipologiaImmobile(String tipologiaImmobile) {
		this.tipologiaImmobile = tipologiaImmobile;
	}

	/**
	 * Restituisce l'oggetto contenente i filtri avanzati di ricerca.
	 *
	 * @return Oggetto {@link Filtri} con i criteri di filtraggio
	 */
	public Filtri getFiltri() {
		return filtri;
	}

	/**
	 * Imposta l'oggetto contenente i filtri avanzati di ricerca.
	 *
	 * @param filtri Oggetto {@link Filtri} da impostare
	 */
	public void setFiltri(Filtri filtri) {
		this.filtri = filtri;
	}

	/**
	 * Restituisce l'email dell'utente che effettua la ricerca.
	 *
	 * @return Email dell'utente ricercatore
	 */
	public String getEmailUtente() {
		return emailUtente;
	}

	/**
	 * Imposta l'email dell'utente che effettua la ricerca.
	 *
	 * @param emailUtente Email dell'utente ricercatore da impostare
	 */
	public void setEmailUtente(String emailUtente) {
		this.emailUtente = emailUtente;
	}

	/**
	 * Restituisce il tipo di ricerca specificato.
	 *
	 * @return Tipo di ricerca secondo l'enum {@link TipoRicerca}
	 */
	public TipoRicerca getTipoRicerca() {
		return tipoRicerca;
	}

	/**
	 * Imposta il tipo di ricerca.
	 *
	 * @param tipoRicerca Tipo di ricerca da impostare secondo l'enum {@link TipoRicerca}
	 */
	public void setTipoRicerca(TipoRicerca tipoRicerca) {
		this.tipoRicerca = tipoRicerca;
	}

	/**
	 * Verifica se la ricerca corrente è una ricerca per agente.
	 * Una ricerca è considerata "per agente" quando il tipo è {@code I_MIEI_IMMOBILI}
	 * e l'email dell'utente è presente e non vuota.
	 *
	 * @return {@code true} se la ricerca è per agente, {@code false} altrimenti
	 */
	public boolean isRicercaPerAgente() {
		return TipoRicerca.I_MIEI_IMMOBILI.equals(tipoRicerca) && emailUtente != null && !emailUtente.isEmpty();
	}

	/**
	 * Enumerazione che definisce i tipi di ricerca disponibili nel sistema.
	 * Specifica se la ricerca deve restituire tutti gli immobili disponibili
	 * o solo quelli associati all'utente corrente.
	 */
	public enum TipoRicerca {
		/**
		 * Ricerca che restituisce tutti gli immobili che corrispondono ai criteri,
		 * indipendentemente dall'associazione con l'utente.
		 */
		TUTTI_I_RISULTATI,

		/**
		 * Ricche che restituisce solo gli immobili associati all'agente
		 * specificato nell'email utente.
		 */
		I_MIEI_IMMOBILI
	}
}