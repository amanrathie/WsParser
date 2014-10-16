package br.gov.mj.sislegis.parser.camara;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.gov.mj.sislegis.domain.Proposicao;

import com.thoughtworks.xstream.XStream;

public class ParserPautaCamara {

	public static void main(String[] args) throws Exception {
		ParserPautaCamara parser = new ParserPautaCamara();

		// TODO: Informação que vem do filtro
		Integer idComissao = 2003;
		String datIni = "20130603";
		String datFim = "20130607";

		System.out.println(parser.getProposicoes(idComissao, datIni, datFim)
				.toString());
	}

	public List<Proposicao> getProposicoes(Integer idComissao, String datIni,
			String datFim) throws Exception {
		String wsURL = "http://www.camara.gov.br/SitCamaraWS/Orgaos.asmx/ObterPauta?IDOrgao="
				+ idComissao + "&datIni=" + datIni + "&datFim=" + datFim;
		URL url = new URL(wsURL);

		XStream xstream = new XStream();
		PautaBean pauta = new PautaBean();

		config(xstream);

		ignoreFields(xstream);

		xstream.fromXML(url, pauta);

		List<Proposicao> proposicoes = new ArrayList<Proposicao>();

		for (ReuniaoBean reuniao : pauta.getReunioes()) {
			proposicoes.addAll(reuniao.getProposicoes());
		}

		return proposicoes;
	}

	private void config(XStream xstream) {
		xstream.alias("pauta", PautaBean.class);
		xstream.alias("reuniao", ReuniaoBean.class);
		xstream.alias("proposicao", Proposicao.class);

		// Utilizamos o implicit quando os filhos já tem os dados que queremos
		// buscar. Ou seja, não tem um pai e v�rios filhos do mesmo tipo.
		xstream.addImplicitCollection(PautaBean.class, "reunioes");
		xstream.aliasAttribute(PautaBean.class, "orgao", "orgao");
		xstream.aliasAttribute(PautaBean.class, "dataInicial", "dataInicial");
		xstream.aliasAttribute(PautaBean.class, "dataFinal", "dataFinal");
	}

	// Ignora o que não precisa de parse
	private void ignoreFields(XStream xstream) {
		xstream.omitField(ReuniaoBean.class, "codReuniao");
		xstream.omitField(ReuniaoBean.class, "data");
		xstream.omitField(ReuniaoBean.class, "horario");
		xstream.omitField(ReuniaoBean.class, "local");
		xstream.omitField(ReuniaoBean.class, "estado");
		xstream.omitField(ReuniaoBean.class, "tipo");
		xstream.omitField(ReuniaoBean.class, "tituloReuniao");
		xstream.omitField(ReuniaoBean.class, "objeto");

		xstream.omitField(Proposicao.class, "resultado");
	}
}

class PautaBean {

	private String orgao;
	private String dataInicial;
	private String dataFinal;

	private List<ReuniaoBean> reunioes;

	public String getOrgao() {
		return orgao;
	}

	public void setOrgao(String orgao) {
		this.orgao = orgao;
	}

	public String getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(String dataInicial) {
		this.dataInicial = dataInicial;
	}

	public String getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(String dataFinal) {
		this.dataFinal = dataFinal;
	}

	public List<ReuniaoBean> getReunioes() {
		return reunioes;
	}

	public void setReunioes(List<ReuniaoBean> reunioes) {
		this.reunioes = reunioes;
	}

}

class ReuniaoBean {

	private List<Proposicao> proposicoes;

	public List<Proposicao> getProposicoes() {
		return proposicoes;
	}

	public void setProposicoes(List<Proposicao> proposicoes) {
		this.proposicoes = proposicoes;
	}
}
