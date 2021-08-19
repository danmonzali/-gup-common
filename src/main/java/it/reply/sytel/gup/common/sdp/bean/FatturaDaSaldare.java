package it.reply.sytel.gup.common.sdp.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FatturaDaSaldare implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty(value="idFattura", required=true)
	private String idFattura;

	@JsonProperty(value="dataEmissione", required=true)
	private String dataEmissione;
	
	@JsonProperty(value="importoPagato", required=true)
	private String importoPagato;
	
	@JsonProperty(value="autPagamento", required=true)
	private String autPagamento;

	public String getIdFattura() {
		return idFattura;
	}

	public void setIdFattura(String idFattura) {
		this.idFattura = idFattura;
	}

	public String getDataEmissione() {
		return dataEmissione;
	}

	public void setDataEmissione(String dataEmissione) {
		this.dataEmissione = dataEmissione;
	}

	public String getImportoPagato() {
		return importoPagato;
	}

	public void setImportoPagato(String importoPagato) {
		this.importoPagato = importoPagato;
	}

	public String getAutPagamento() {
		return autPagamento;
	}

	public void setAutPagamento(String autPagamento) {
		this.autPagamento = autPagamento;
	}

	@Override
	public String toString() {
		return "Fatture [idFattura=" + idFattura + ", dataEmissione=" + dataEmissione + ", importoPagato=" + importoPagato + ", autPagamento=" + autPagamento + "]";
	}
	
}
