package com.TodoListKotlin.TodoList.model

import jakarta.persistence.*
import java.time.LocalDate

/**
 * Registra que um hábito foi feito em um determinado dia.
 * A restrição única impede dois check-ins do mesmo hábito no mesmo dia.
 */
@Entity
@Table(
    name = "tb_checkins",
    uniqueConstraints = [UniqueConstraint(columnNames = ["habito_id", "data"])]
)
class CheckIn(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne(optional = false)
    @JoinColumn(name = "habito_id")
    var habito: Habito? = null,

    var data: LocalDate = LocalDate.now()
)
