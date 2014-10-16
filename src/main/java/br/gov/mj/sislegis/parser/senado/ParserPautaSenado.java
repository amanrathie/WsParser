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
		xstream.alias("Partes", ListaPartes.class);
		xstream.alias("Parte", ParteBean.class);
		xstream.alias("Itens", ListaItens.class);
		xstream.alias("Item", ItemBean.class);
		
		xstream.addImplicitCollection(ListaPartes.class, "partes");
		xstream.aliasField("Partes", ReuniaoBean.class, "partes");
		xstream.aliasField("Codigo", ReuniaoBean.class, "codigo");
		
		xstream.aliasField("Itens", ParteBean.class, "itens");
		xstream.addImplicitCollection(ListaItens.class, "itens");		
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
		
		xstream.omitField(ParteBean.class, "Codigo");
		xstream.omitField(ParteBean.class, "NumOrdem");
		xstream.omitField(ParteBean.class, "CodigoTipo");
		xstream.omitField(ParteBean.class, "Tipo");
		xstream.omitField(ParteBean.class, "NomeFantasia");
		xstream.omitField(ParteBean.class, "ExisteItemTerminativo");
		xstream.omitField(ParteBean.class, "Eventos");
		
		xstream.omitField(ItemBean.class, "Codigo");
		xstream.omitField(ItemBean.class, "SeqOrdemPauta");
		xstream.omitField(ItemBean.class, "Nome");
		xstream.omitField(ItemBean.class, "NomeFormatadoComOrdem");
		xstream.omitField(ItemBean.class, "ItemTerminativo");
		xstream.omitField(ItemBean.class, "TipoPauta");
		xstream.omitField(ItemBean.class, "Categoria");
		xstream.omitField(ItemBean.class, "Relatorio");
		xstream.omitField(ItemBean.class, "Observacao");
		xstream.omitField(ItemBean.class, "Resultado");
		
		xstream.omitField(MateriaBean.class, "DescricaoSubtipo");
		xstream.omitField(MateriaBean.class, "ExplicacaoEmenta");
		xstream.omitField(MateriaBean.class, "Indexacao");
		xstream.omitField(MateriaBean.class, "SiglaCasaIniciadora");
		xstream.omitField(MateriaBean.class, "SiglaCasaLeitura");
		xstream.omitField(MateriaBean.class, "Prazos");
	}
}

class ListaReunioes {
	protected List<ReuniaoBean> reunioes;

	public List<ReuniaoBean> getReunioes() {
		return reunioes;
	}
}

class ReuniaoBean {
	protected Integer codigo;
	protected ListaPartes partes;
	
	public Integer getCodigo() {
		return codigo;
	}
}

class ListaPartes {
	protected List<ParteBean> partes;
}

class ParteBean {
	protected ListaItens itens;
}

class ListaItens {
	protected List<ItemBean> itens;
}

class ItemBean {
	protected MateriaBean materia;
	
}

class MateriaBean {
	protected Integer codigo;
	protected String subtipo;
	protected String numero;
	protected String ano;
	protected String ementa;
	
}

