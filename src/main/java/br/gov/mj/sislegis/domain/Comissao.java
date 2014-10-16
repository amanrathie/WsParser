package br.gov.mj.sislegis.domain;


public class Comissao {
	
	private String id;
	private String sigla;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSigla() {
		return sigla;
	}
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getId() 
				+ " - " + this.getSigla() + "\n");

		return sb.toString();
	}
}
