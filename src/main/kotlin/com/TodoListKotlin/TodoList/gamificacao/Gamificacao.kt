package com.TodoListKotlin.TodoList.gamificacao

/**
 * Regras de XP e nível, calculadas a partir do total de check-ins (sem Spring, fácil de testar).
 */
object Gamificacao {
    const val XP_POR_CHECKIN = 10
    const val XP_POR_NIVEL = 100

    fun xpTotal(totalCheckIns: Long): Long = totalCheckIns * XP_POR_CHECKIN

    fun nivel(xp: Long): Long = xp / XP_POR_NIVEL + 1

    fun xpNoNivel(xp: Long): Long = xp % XP_POR_NIVEL
}
