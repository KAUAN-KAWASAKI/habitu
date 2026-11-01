package com.TodoListKotlin.TodoList.repository

import com.TodoListKotlin.TodoList.model.Task
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Interface do Repositório responsável por gerir as operações no Banco de Dados.
 * Ao herdar de JpaRepository<Task, Long>, o Spring concede automaticamente métodos como:
 * - save(): Inserir ou Atualizar registros
 * - findAll(): Listar todos os registros
 * - findById(): Buscar um registro específico pelo ID
 * - deleteById(): Remover um registro pelo ID
 */
@Repository
interface TaskRepository : JpaRepository<Task, Long>