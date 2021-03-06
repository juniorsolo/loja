package br.com.alura.loja.resource;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import br.com.alura.loja.dao.CarrinhoDAO;
import br.com.alura.loja.modelo.Carrinho;
import br.com.alura.loja.modelo.Produto;

@Path("carrinhos")
public class CarrinhoResource {
	
//	@Path("{id}")
//	@GET
//	@Produces(MediaType.APPLICATION_XML)
//	public String busca(@PathParam("id") Long id ) {
//		Carrinho carrinho = new CarrinhoDAO().busca(id);
//		return carrinho.getXML();
//	}
//	
	@Path("{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String buscaJson(@PathParam("id") Long id ) {
		Carrinho carrinho = new CarrinhoDAO().busca(id);
		return carrinho.getJson();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response adiciona(String conteudo) {
		Carrinho carrinho = new Gson().fromJson(conteudo, Carrinho.class);
		new CarrinhoDAO().adiciona(carrinho);		
		System.out.println(carrinho.getRua());
		URI uri = URI.create("/carrinhos/" +  carrinho.getId());
		return Response.created(uri).build();
	}
	
	@Path("{id}/produtos/{produtoId}")
	@DELETE
	public Response removeProduto(@PathParam("id") long id, @PathParam("produtoId") long produtoId) {
		try {
			Carrinho carrinho = new CarrinhoDAO().busca(id);
			carrinho.remove(produtoId);
			return Response.ok().build();
		}catch (Exception e) {
			System.err.println(e.getMessage());
			return Response.serverError().build();
		}
	}
	
	// put usado para trocar uma representação inteira ou parte dela. 
	// nesse exemplo usando a uri para identificar.
	@Path("{id}/produtos/{produtoId}/quantidade")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response alteraProduto(String conteudo, @PathParam("id") long id, @PathParam("produtoId") long produtoId) {
		Carrinho carrinho = new CarrinhoDAO().busca(id);
		Produto produto = new Gson().fromJson(conteudo, Produto.class);
		carrinho.trocaQuantidade(produto);
		return Response.ok().build();
	}
}
