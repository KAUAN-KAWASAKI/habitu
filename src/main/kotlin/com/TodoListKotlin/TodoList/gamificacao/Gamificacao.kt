package com.TodoListKotlin.TodoList.gamificacao

import java.time.LocalDate

/**
 * Regras de XP e nível, calculadas a partir do total de check-ins (sem Spring, fácil de testar).
 */
object Gamificacao {
    const val XP_POR_CHECKIN = 10
    const val XP_POR_NIVEL = 100

    fun xpTotal(totalCheckIns: Long): Long = totalCheckIns * XP_POR_CHECKIN

    fun nivel(xp: Long): Long = xp / XP_POR_NIVEL + 1

    fun xpNoNivel(xp: Long): Long = xp % XP_POR_NIVEL

    /**
     * Dias seguidos em que o hábito foi feito, contando de hoje para trás.
     * Se hoje ainda não foi feito, a contagem parte de ontem (o dia ainda não acabou).
     */
    fun sequencia(datas: Set<LocalDate>, hoje: LocalDate): Int {
        var dia = if (hoje in datas) hoje else hoje.minusDays(1)
        var total = 0
        while (dia in datas) {
            total++
            dia = dia.minusDays(1)
        }
        return total
    }
}
