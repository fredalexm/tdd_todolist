package com.generation.todolist.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import com.generation.todolist.repository.TarefaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.todolist.model.Tarefa;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TarefaControllerTest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private TarefaRepository tarefaRepository;
	
	@Test
	@DisplayName("Criar nova Tarefa")
	public void deveCriarNovaTarefa() throws Exception {

		Tarefa tarefa = new Tarefa(0L, "Tarefa 01", "Tarefa numero 1", "João", LocalDate.now(), true);
		
		HttpEntity<Tarefa> corpoRequisicao = new HttpEntity<Tarefa>(tarefa);
		
		ResponseEntity<Tarefa> resposta = testRestTemplate
				.exchange("/tarefas", HttpMethod.POST, corpoRequisicao, Tarefa.class);

		assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
		assertEquals(corpoRequisicao.getBody().getNome(), resposta.getBody().getNome());
		
	}

	@Test
	@DisplayName("Listar uma Tarefa Específica")
	public void deveListarApenasUmaTarefa() {

		Tarefa buscaTarefa = tarefaRepository.save(new Tarefa(0L, "Tarefa 02", "Tarefa numero 2",
				"Maria", LocalDate.now(), true));

		ResponseEntity<String> resposta = testRestTemplate
				.exchange("/tarefas/" + buscaTarefa.getId(), HttpMethod.GET, null, String.class);

		assertEquals(HttpStatus.OK, resposta.getStatusCode());

	}

	@Test
	@DisplayName("Listar todas as tarefas")
	public void deveListarTodasAsTarefas(){

		tarefaRepository.save(new Tarefa(
				0l, "Tarefa 03", "Tarefa numero 03", "Mariazinha", LocalDate.now(),false));

		ResponseEntity<String> resposta = testRestTemplate
				.exchange("/tarefas", HttpMethod.GET, null, String.class);

		assertEquals(HttpStatus.OK, resposta.getStatusCode());
	}

	@Test
	@DisplayName("Listar todas as Tarefas que contem um Nome Especifico")
	public void deveListarTodasAsTarefasComNomeEspecifico(){

		tarefaRepository.save(new Tarefa(
				0l, "Tarefa 04", "Tarefa numero 04", "Mariana", LocalDate.now(),true));

		ResponseEntity<String> resposta = testRestTemplate
				.exchange("/tarefas/nome/Tarefa 04", HttpMethod.GET, null, String.class);

		assertEquals(HttpStatus.OK, resposta.getStatusCode());
	}

	@Test
	@DisplayName("Atualizar uma tarefa especifica")
	public void deveAtualizarUmaTarefa(){

		Tarefa buscarTarefa = tarefaRepository.save(new Tarefa(
				0l, "Tarefa 05", "Tarefa numero 05", "Carlos", LocalDate.now(),true));

		Tarefa atualizarTarefa = tarefaRepository.save(new Tarefa(
				buscarTarefa.getId(), "Tarefa 05- Up", "Tarefa numero 05 - Up", "Carlos", LocalDate.now(),false));

		HttpEntity<Tarefa> corpoRequisicao = new HttpEntity<Tarefa>(atualizarTarefa);

		ResponseEntity<Tarefa> corpoResposta = testRestTemplate
				.withBasicAuth("root", "root")
				.exchange("/tarefas", HttpMethod.POST, corpoRequisicao, Tarefa.class);

		assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());
		assertEquals(corpoRequisicao.getBody().getNome(), corpoResposta.getBody().getNome());
	}

	@Test
	@DisplayName("Apagar uma Tarefa")
	public void deveApagarUmaTarefa(){
		Tarefa buscarTarefa = tarefaRepository.save(new Tarefa(
				0l, "Tarefa 06", "Tarefa numero 06", "Joao", LocalDate.now(),true));

		ResponseEntity<String> resposta = testRestTemplate
				.exchange("/tarefas/"+ buscarTarefa.getId(), HttpMethod.DELETE, null, String.class);

		assertEquals(HttpStatus.NO_CONTENT, resposta.getStatusCode());
	}

}
