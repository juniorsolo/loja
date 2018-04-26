package br.com.alura.loja;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import br.com.alura.loja.modelo.Carrinho;
import br.com.alura.loja.modelo.Produto;

public class ClienteTest {
	
	private HttpServer server;
	Client cliente= ClientBuilder.newClient();
	private WebTarget target =  cliente.target("http://localhost:8080");
	
	@Before
	public void iniciaServer() {
		server = Servidor.iniciaServidor();
	}
	
	@After
	public void paraServidor() {
		server.stop();
	}
	
	@Test
	public void testaConexaoComServidorfunciona() {
		
		//WebTarget target =  cliente.target("http://www.mocky.io/");
		//String conteudo = target.path("v2/5ade8c292f0000650056de47").request().get(String.class);
		String conteudo = target.path("/carrinhos/1").request().get(String.class);
		
		//Carrinho c = (Carrinho) new XStream().fromXML(conteudo); usado quando o serviço aceitava .XML
		Carrinho c =  new Gson().fromJson(conteudo, Carrinho.class);
		assertEquals("Rua Vergueiro 3185, 8 andar", c.getRua());
		
	}
	
	@Test
	public void testaAdicionaCarrinhos() {
		Carrinho c = new Carrinho();
		c.adiciona(new Produto(112, "mesa 4 cadeiras", 385.88, 1));
		c.adiciona(new Produto(451, "Lavadora", 1115.96, 1));
		c.adiciona(new Produto(97, "TV Sansung 42 Pol.", 1850.50, 1));
		c.setRua("Paranaiba 457");
		c.setCidade("Embu das artes");
		
		String json = new Gson().toJson(c);
		Entity<String> entity = Entity.entity(json, MediaType.APPLICATION_JSON);
		
		Response response = target.path("/carrinhos").request().post(entity);
		assertEquals(201, response.getStatus());
		
		String location = response.getHeaderString("Location");
		String resposta = cliente.target(location).request().get(String.class);
		
		assertTrue(resposta.contains("Lavadora"));
		
	}
}
