package com.TodoListKotlin.TodoList.repository

import com.TodoListKotlin.TodoList.model.CheckIn
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Repository
interface CheckInRepository : JpaRepository<CheckIn, Long> {
    fun existsByHabitoIdAndData(habitoId: Long, data: LocalDate): Boolean

    fun findAllByHabitoUsuarioUsernameAndData(username: String, data: LocalDate): List<CheckIn>

    fun findAllByHabitoUsuarioUsername(username: String): List<CheckIn>

    fun countByHabitoUsuarioUsername(username: String): Long

    @Transactional
    fun deleteByHabitoIdAndData(habitoId: Long, data: LocalDate)

    @Transactional
    fun deleteAllByHabitoId(habitoId: Long)
}
