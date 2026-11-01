package com.TodoListKotlin.TodoList.gamificacao

import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GamificacaoTest {
    private val hoje = LocalDate.of(2026, 11, 10)

    private fun dias(vararg atras: Long): Set<LocalDate> = atras.map { hoje.minusDays(it) }.toSet()

    @Test
    fun `xp total e dez por check-in`() {
        assertEquals(30L, Gamificacao.xpTotal(3))
        assertEquals(0L, Gamificacao.xpTotal(0))
    }

    @Test
    fun `nivel nas bordas`() {
        assertEquals(1L, Gamificacao.nivel(0))
        assertEquals(1L, Gamificacao.nivel(99))
        assertEquals(2L, Gamificacao.nivel(100))
        assertEquals(5L, Gamificacao.nivel(400))
    }

    @Test
    fun `xp no nivel e o resto da divisao por cem`() {
        assertEquals(0L, Gamificacao.xpNoNivel(0))
        assertEquals(99L, Gamificacao.xpNoNivel(99))
        assertEquals(0L, Gamificacao.xpNoNivel(100))
        assertEquals(30L, Gamificacao.xpNoNivel(130))
    }

    @Test
    fun `sequencia conta de hoje para tras`() {
        assertEquals(3, Gamificacao.sequencia(dias(0, 1, 2), hoje))
    }

    @Test
    fun `sequencia sem hoje conta a partir de ontem`() {
        assertEquals(2, Gamificacao.sequencia(dias(1, 2), hoje))
    }

    @Test
    fun `sequencia quebrada para no dia faltante`() {
        assertEquals(2, Gamificacao.sequencia(dias(0, 1, 3), hoje))
    }

    @Test
    fun `sequencia com so um dia antigo e zero`() {
        assertEquals(0, Gamificacao.sequencia(dias(5), hoje))
    }

    @Test
    fun `sequencia vazia e zero`() {
        assertEquals(0, Gamificacao.sequencia(emptySet(), hoje))
    }

    @Test
    fun `sem progresso nenhuma conquista e desbloqueada`() {
        assertTrue(Gamificacao.conquistas(0, 0, 1).none { it.desbloqueada })
    }

    @Test
    fun `um check-in desbloqueia so o primeiro passo`() {
        val desbloqueadas = Gamificacao.conquistas(1, 0, 1).filter { it.desbloqueada }
        assertEquals(listOf("Primeiro passo"), desbloqueadas.map { it.nome })
    }

    @Test
    fun `conquistas de ritmo, sequencia e nivel`() {
        fun nomes(total: Long, seq: Int, nivel: Long) =
            Gamificacao.conquistas(total, seq, nivel).filter { it.desbloqueada }.map { it.nome }

        assertTrue("Pegando o ritmo" in nomes(10, 0, 1))
        assertFalse("Pegando o ritmo" in nomes(9, 0, 1))
        assertTrue("Semana de fogo" in nomes(0, 7, 1))
        assertFalse("Mês imbatível" in nomes(0, 29, 1))
        assertTrue("Mês imbatível" in nomes(0, 30, 1))
        assertTrue("Nível 5" in nomes(0, 0, 5))
    }
}
