package br.gov.mj.sislegis.domain;


public class Proposicao {
	
	private Integer idProposicao;
	private String sigla;
	private String ementa;
	private String tipo;
	private Integer numero;
	private Integer ano;
	private String autor;
	
	public Integer getIdProposicao() {
		return idProposicao;
	}
	public void setIdProposicao(Integer idProposicao) {
		this.idProposicao = idProposicao;
	}
	public String getSigla() {
		return sigla;
	}
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
	public String getEmenta() {
		return ementa;
	}
	public void setEmenta(String ementa) {
		this.ementa = ementa;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public Integer getNumero() {
		return numero;
	}
	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	public Integer getAno() {
		return ano;
	}
	public void setAno(Integer ano) {
		this.ano = ano;
	}
	public String getAutor() {
		return autor;
	}
	public void setAutor(String autor) {
		this.autor = autor;
	}
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getIdProposicao() 
				+ " - " + this.getSigla()
				+ " - " + this.getEmenta()
				+ " - " + this.getTipo()
				+ " - " + this.getNumero()
				+ " - " + this.getAno()
				+ " - " + this.getAutor() + "\n");

		return sb.toString();
	}
}
