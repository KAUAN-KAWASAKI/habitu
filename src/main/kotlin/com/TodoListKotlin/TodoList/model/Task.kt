package com.TodoListKotlin.TodoList.model

import jakarta.persistence.*

/**
 * Representa a Entidade 'Task' que será mapeada diretamente para uma tabela no Banco de Dados.
 * A anotação @Entity informa ao Spring Data JPA/Hibernate que esta classe é uma tabela.
 */
@Entity
@Table(name = "tb_tasks") // Define explicitamente o nome da tabela no Banco de Dados
class Task(

    /**
     * Identificador único de cada tarefa (Chave Primária).
     * @Id: Define o campo como ID.
     * @GeneratedValue: Garante que o ID será gerado automaticamente pelo Banco de Dados (Auto Increment).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    /**
     * Título ou descrição da tarefa inserida pelo utilizador.
     */
    var title: String = "",

    /**
     * Estado da tarefa:
     * - false: Pendente (Padrão ao criar uma nova tarefa)
     * - true: Concluída
     */
    var completed: Boolean = false
)