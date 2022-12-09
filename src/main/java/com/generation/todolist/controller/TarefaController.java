package com.generation.todolist.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.generation.todolist.model.Tarefa;
import com.generation.todolist.repository.TarefaRepository;

import java.util.Optional;

@RestController
@RequestMapping("/tarefas")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TarefaController {

	@Autowired
	private TarefaRepository tarefaRepository;
	
	@PostMapping
	public ResponseEntity<Tarefa> post(@Valid @RequestBody Tarefa tarefa){
		return ResponseEntity.status(HttpStatus.CREATED).body(tarefaRepository.save(tarefa));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Tarefa> getById(@PathVariable Long id) {
		return tarefaRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.notFound().build());
	}



	
}

