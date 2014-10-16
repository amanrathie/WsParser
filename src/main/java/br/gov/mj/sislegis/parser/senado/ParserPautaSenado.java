package br.gov.mj.sislegis.parser.senado;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.gov.mj.sislegis.domain.Proposicao;

import com.thoughtworks.xstream.XStream;

public class ParserPautaSenado {
	
	public static void main(String[] args) throws Exception {
		ParserPautaSenado parser = new ParserPautaSenado();
		
		// TODO: Informação que vem do filtro
		String siglaComissao = "CAE";
		String datIni = "20140801";
		
		System.out.println(parser.getProposicoes(siglaComissao, datIni).toString());
	}
	
	public List<Proposicao> getProposicoes(String siglaComissao, String datIni) throws Exception {
		List<Proposicao> proposicoes = new ArrayList<Proposicao>();
			
		XStream xstreamReuniao = new XStream();
		
		configReuniao(xstreamReuniao);
		
		ignoreFieldsReuniao(xstreamReuniao);

		for (ReuniaoBean bean : getReunioes(siglaComissao, datIni)) {
			String wsURLReuniao = "http://legis.senado.leg.br/dadosabertos/reuniao/"+bean.getCodigo();
			URL url = new URL(wsURLReuniao);
			ReuniaoBean reuniao = new ReuniaoBean();
			
			xstreamReuniao.fromXML(url, reuniao);
		}
		
		return proposicoes;
	}
	
	private List<ReuniaoBean> getReunioes(String siglaComissao, String datIni) throws Exception {
		String wsURL = "http://legis.senado.leg.br/dadosabertos/agenda/"+datIni+"?colegiado="+siglaComissao;
		URL url = new URL(wsURL);
		
		XStream xstreamAgenda = new XStream();
		ListaReunioes reunioes = new ListaReunioes();
		
		configAgenda(xstreamAgenda);
		
		ignoreFieldsAgenda(xstreamAgenda);

		xstreamAgenda.fromXML(url, reunioes);
	
		return reunioes.getReunioes();
	}
	
	private void configAgenda(XStream xstream) {
		xstream.alias("Reunioes", ListaReunioes.class);
		xstream.alias("Reuniao", ReuniaoBean.class);
		
		xstream.addImplicitCollection(ListaReunioes.class, "reunioes");
		
		xstream.aliasField("Codigo", ReuniaoBean.class, "codigo");
	}
	
	// Ignora o que não precisa de parse
	private void ignoreFieldsAgenda(XStream xstream) {
		xstream.omitField(ReuniaoBean.class, "Tipo");
		xstream.omitField(ReuniaoBean.class, "Data");
		xstream.omitField(ReuniaoBean.class, "Hora");
		xstream.omitField(ReuniaoBean.class, "Situacao");
		xstream.omitField(ReuniaoBean.class, "Local");
		xstream.omitField(ReuniaoBean.class, "ExisteItemTerminativo");
		
		xstream.omitField(ReuniaoBean.class, "Comissoes");
		xstream.omitField(ReuniaoBean.class, "Partes");
	}
	
	/**
	 * TODO: Montar parsing das proposicoes (materias)
	 */
	private void configReuniao(XStream xstream) {
		xstream.alias("Reuniao", ReuniaoBean.class);
		xstream.aliasField("Codigo", ReuniaoBean.class, "codigo");
	}
	
	// Ignora o que não precisa de parse
	private void ignoreFieldsReuniao(XStream xstream) {
		xstream.omitField(ReuniaoBean.class, "Data");
		xstream.omitField(ReuniaoBean.class, "Hora");
		xstream.omitField(ReuniaoBean.class, "Tipo");
		xstream.omitField(ReuniaoBean.class, "Situacao");
		xstream.omitField(ReuniaoBean.class, "Realizada");
		xstream.omitField(ReuniaoBean.class, "Local");
		
		xstream.omitField(ReuniaoBean.class, "Comissoes");
	}
}

class ListaReunioes {
	
	private List<ReuniaoBean> reunioes;

	public List<ReuniaoBean> getReunioes() {
		return reunioes;
	}

	public void setReunioes(List<ReuniaoBean> reunioes) {
		this.reunioes = reunioes;
	}

}

class ReuniaoBean {
	
	private Integer codigo;

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
}
