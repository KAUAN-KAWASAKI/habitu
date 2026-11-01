package com.TodoListKotlin.TodoList.repository

import com.TodoListKotlin.TodoList.model.Habito
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Repositório responsável pelas operações de Habito no Banco de Dados.
 * JpaRepository<Habito, Long> fornece save(), findAll(), findById() e deleteById().
 */
@Repository
interface HabitoRepository : JpaRepository<Habito, Long>
