package it.reply.sytel.gup.common.sdp.bean;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
public class InputPagamenti implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty(value="numeroFatture", required=true)
	private int numeroFatture;

	@JsonProperty(value="fatture", required=true)
	private List<FatturaDaSaldare> fatture;

	public int getNumeroFatture() {
		return numeroFatture;
	}

	public void setNumeroFatture(int numeroFatture) {
		this.numeroFatture = numeroFatture;
	}

	public List<FatturaDaSaldare> getFatture() {
		return fatture;
	}

	public void setFatture(List<FatturaDaSaldare> fatture) {
		this.fatture = fatture;
	}

	@Override
	public String toString() {
		return "SdpRequest [numeroFatture=" + numeroFatture + ", fatture=" + fatture + "]";
	}
	
	
	public String toJsonString() {
		StringBuffer json = new StringBuffer("{");
		
		json.append("\"numeroFatture\" : ").append(getNumeroFatture()).append(",");
		json.append("\"fatture\" : [");
		
		for (FatturaDaSaldare i : this.getFatture()) {
			
			json.append("{");
			json.append("\"idFattura\" : \"").append(i.getIdFattura()).append("\",");
			json.append("\"dataEmissione\" : \"").append(i.getDataEmissione()).append("\",");
			json.append("\"importoPagato\" : ").append(i.getImportoPagato()).append(",");
			json.append("\"autPagamento\" : \"").append(i.getAutPagamento()).append("\"");
			json.append("},");
		}
		if(this.getFatture().size()>0)
			json.deleteCharAt(json.length()-1);
		json.append("]");
		json.append("}");
		
		return json.toString();
	}
}
