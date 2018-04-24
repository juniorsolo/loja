package br.com.alura.loja;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.junit.Assert;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;

import br.com.alura.loja.modelo.Carrinho;

public class ClienteTest {
	
	@Test
	public void testaConexaoComServidorfunciona() {
		
		Client cliente= ClientBuilder.newClient();
		//WebTarget target =  cliente.target("http://www.mocky.io/");
		//String conteudo = target.path("v2/5ade8c292f0000650056de47").request().get(String.class);
		WebTarget target =  cliente.target("http://localhost:8080");
		String conteudo = target.path("/carrinhos").request().get(String.class);
		Carrinho c = (Carrinho) new XStream().fromXML(conteudo);
		Assert.assertEquals("Rua Vergueiro 3185, 8 andar", c.getRua());
		
	}
}
