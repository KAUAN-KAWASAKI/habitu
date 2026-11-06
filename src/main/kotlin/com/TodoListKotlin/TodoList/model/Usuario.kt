package com.TodoListKotlin.TodoList.model

import jakarta.persistence.*

/**
 * Representa a Entidade 'Usuario' mapeada para uma tabela no Banco de Dados.
 */
@Entity
@Table(name = "tb_usuarios")
class Usuario(

    /**
     * Identificador único de cada usuário (Chave Primária), gerado pelo Banco (Auto Increment).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    /**
     * Nome de usuário: não pode se repetir (unique) nem ficar vazio no banco (nullable = false).
     */
    @Column(unique = true, nullable = false)
    var username: String = "",

    /**
     * Senha já embaralhada (hash BCrypt). Nunca guardamos a senha original.
     */
    var senhaHash: String = ""
)
