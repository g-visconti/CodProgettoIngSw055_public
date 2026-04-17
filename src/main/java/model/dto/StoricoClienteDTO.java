package model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) per rappresentare lo storico delle offerte proposte da un cliente.
 * Contiene dati aggregati da diverse entità: Immobile, OffertaIniziale, Account.
 *
 * <p>Questa classe fornisce una vista unificata delle offerte inviate da un cliente,
 * includendo informazioni sull'immobile, sull'offerta e sul suo stato attuale,
 * facilitando la visualizzazione dello storico personale delle proposte.
 *
 * @author IngSW2425_055 Team
 */
public class StoricoClienteDTO {

	private Long idOfferta;
	private String primaImmagineBase64;
	private String categoria; // Tipologia immobile
	private String descrizione; // Descrizione immobile
	private LocalDateTime dataOfferta; // Data della proposta
	private BigDecimal importoProposto; // Importo offerto
	private String stato; // Stato offerta (IN_ATTESA, ACCETTATA, RIFIUTATA)

	/**
	 * Costruttore vuoto per StoricoClienteDTO.
	 * Inizializza un oggetto con tutti i campi a null.
	 */
	public StoricoClienteDTO() {
	}

	/**
	 * Costruttore completo per StoricoClienteDTO.
	 * Inizializza tutte le proprietà dell'offerta storica del cliente con i valori forniti.
	 *
	 * @param idOfferta Identificativo univoco dell'offerta
	 * @param primaImmagineBase64 Prima immagine dell'immobile in formato Base64
	 * @param categoria Categoria/tipologia dell'immobile
	 * @param descrizione Descrizione completa dell'immobile
	 * @param dataOfferta Data e ora in cui è stata inviata l'offerta
	 * @param importoProposto Importo proposto dal cliente per l'immobile
	 * @param stato Stato attuale dell'offerta (IN_ATTESA, ACCETTATA, RIFIUTATA)
	 */
	public StoricoClienteDTO(Long idOfferta, String primaImmagineBase64, String categoria, String descrizione,
			LocalDateTime dataOfferta, BigDecimal importoProposto, String stato) {
		this.idOfferta = idOfferta;
		this.primaImmagineBase64 = primaImmagineBase64;
		this.categoria = categoria;
		this.descrizione = descrizione;
		this.dataOfferta = dataOfferta;
		this.importoProposto = importoProposto;
		this.stato = stato;
	}

	/**
	 * Restituisce l'identificativo univoco dell'offerta.
	 *
	 * @return ID dell'offerta
	 */
	public Long getIdOfferta() {
		return idOfferta;
	}

	/**
	 * Imposta l'identificativo univoco dell'offerta.
	 *
	 * @param idOfferta ID dell'offerta da impostare
	 */
	public void setIdOfferta(Long idOfferta) {
		this.idOfferta = idOfferta;
	}

	/**
	 * Restituisce la prima immagine dell'immobile in formato Base64.
	 *
	 * @return Immagine codificata in Base64
	 */
	public String getPrimaImmagineBase64() {
		return primaImmagineBase64;
	}

	/**
	 * Imposta la prima immagine dell'immobile in formato Base64.
	 *
	 * @param primaImmagineBase64 Immagine codificata in Base64 da impostare
	 */
	public void setPrimaImmagineBase64(String primaImmagineBase64) {
		this.primaImmagineBase64 = primaImmagineBase64;
	}

	/**
	 * Restituisce la categoria/tipologia dell'immobile.
	 *
	 * @return Categoria dell'immobile
	 */
	public String getCategoria() {
		return categoria;
	}

	/**
	 * Imposta la categoria/tipologia dell'immobile.
	 *
	 * @param categoria Categoria dell'immobile da impostare
	 */
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	/**
	 * Restituisce la descrizione completa dell'immobile.
	 *
	 * @return Descrizione dell'immobile
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * Imposta la descrizione completa dell'immobile.
	 *
	 * @param descrizione Descrizione dell'immobile da impostare
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * Restituisce la data e ora in cui è stata inviata l'offerta.
	 *
	 * @return Data e ora dell'offerta
	 */
	public LocalDateTime getDataOfferta() {
		return dataOfferta;
	}

	/**
	 * Imposta la data e ora in cui è stata inviata l'offerta.
	 *
	 * @param dataOfferta Data e ora dell'offerta da impostare
	 */
	public void setDataOfferta(LocalDateTime dataOfferta) {
		this.dataOfferta = dataOfferta;
	}

	/**
	 * Restituisce l'importo proposto dal cliente per l'immobile.
	 *
	 * @return Importo dell'offerta
	 */
	public BigDecimal getImportoProposto() {
		return importoProposto;
	}

	/**
	 * Imposta l'importo proposto dal cliente per l'immobile.
	 *
	 * @param importoProposto Importo dell'offerta da impostare
	 */
	public void setImportoProposto(BigDecimal importoProposto) {
		this.importoProposto = importoProposto;
	}

	/**
	 * Restituisce lo stato attuale dell'offerta.
	 *
	 * @return Stato dell'offerta (IN_ATTESA, ACCETTATA, RIFIUTATA)
	 */
	public String getStato() {
		return stato;
	}

	/**
	 * Imposta lo stato attuale dell'offerta.
	 *
	 * @param stato Stato dell'offerta da impostare (IN_ATTESA, ACCETTATA, RIFIUTATA)
	 */
	public void setStato(String stato) {
		this.stato = stato;
	}

	/**
	 * Verifica se l'immobile associato all'offerta ha un'immagine disponibile.
	 *
	 * @return {@code true} se è presente un'immagine Base64 non vuota, {@code false} altrimenti
	 */
	public boolean hasImmagine() {
		return primaImmagineBase64 != null && !primaImmagineBase64.trim().isEmpty();
	}

	/**
	 * Verifica se lo stato dell'offerta è "ACCETTATA".
	 *
	 * @return {@code true} se l'offerta è stata accettata, {@code false} altrimenti
	 */
	public boolean isStatoAccettata() {
		return "ACCETTATA".equalsIgnoreCase(stato);
	}

	/**
	 * Verifica se lo stato dell'offerta è "IN_ATTESA".
	 *
	 * @return {@code true} se l'offerta è in attesa di risposta, {@code false} altrimenti
	 */
	public boolean isStatoInAttesa() {
		return "IN_ATTESA".equalsIgnoreCase(stato);
	}

	/**
	 * Verifica se lo stato dell'offerta è "RIFIUTATA".
	 *
	 * @return {@code true} se l'offerta è stata rifiutata, {@code false} altrimenti
	 */
	public boolean isStatoRifiutata() {
		return "RIFIUTATA".equalsIgnoreCase(stato);
	}

	/**
	 * Restituisce una rappresentazione testuale dell'offerta.
	 * Il formato è: "Offerta #[id] [stato] - [categoria]"
	 *
	 * @return Stringa che rappresenta l'offerta
	 */
	@Override
	public String toString() {
		return String.format("Offerta #%d [%s] - %s", idOfferta, stato, categoria);
	}
}