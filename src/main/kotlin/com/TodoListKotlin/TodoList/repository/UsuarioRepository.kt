package com.TodoListKotlin.TodoList.repository

import com.TodoListKotlin.TodoList.model.Usuario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Repositório responsável pelas operações de Usuario no Banco de Dados.
 * O Spring cria a consulta de findByUsername sozinho a partir do nome do método.
 */
@Repository
interface UsuarioRepository : JpaRepository<Usuario, Long> {
    fun findByUsername(username: String): Usuario?
}
