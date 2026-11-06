package com.TodoListKotlin.TodoList.model

import jakarta.persistence.*
import java.time.LocalDate

/**
 * Representa a Entidade 'Habito' mapeada para uma tabela no Banco de Dados.
 */
@Entity
@Table(name = "tb_habitos") // Define explicitamente o nome da tabela no Banco de Dados
class Habito(

    /**
     * Identificador único de cada hábito (Chave Primária), gerado pelo Banco (Auto Increment).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    /**
     * Nome do hábito inserido pelo utilizador.
     */
    var nome: String = "",

    /**
     * Dono do hábito. Cada usuário só enxerga e mexe nos próprios hábitos.
     */
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    var usuario: Usuario? = null,

    /**
     * Data de criação do hábito.
     */
    var criadoEm: LocalDate = LocalDate.now()
)
