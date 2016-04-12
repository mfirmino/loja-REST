package br.com.alura.loja;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;

import br.com.alura.loja.modelo.Produto;
import br.com.alura.loja.modelo.Projeto;

public class ProjetoTest {
	
	private HttpServer server;
	
	@Before
	public void iniciaServidor(){
		server = Servidor.inicializaServidor();
	}
	
	@After
	public void encerraServidor(){
		Servidor.encerraServidor(server);
	}
	
	@Test
	public void testeDeAcessoAoProjetos(){
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080");
		String conteudo = target.path("/projetos/1").request().get(String.class);
		Projeto projeto = ((Projeto) new XStream().fromXML(conteudo));
		Assert.assertEquals(2014, projeto.getAnoDeInicio());
	}
	
	@Test
	public void testeDeInsercaoProjeto(){
		Projeto projeto = new Projeto(3l, "REST", 2016);
        String xml = projeto.toXML();
        
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080");
		
		Entity<String> entity = Entity.entity(xml, MediaType.APPLICATION_XML);
		
		Response response = target.path("/projetos").request().post(entity);
        Assert.assertEquals(201, response.getStatus());
        
        String location = response.getHeaderString("Location");
        String conteudo = client.target(location).request().get(String.class);
        Assert.assertTrue(conteudo.contains("REST"));
	}

}
