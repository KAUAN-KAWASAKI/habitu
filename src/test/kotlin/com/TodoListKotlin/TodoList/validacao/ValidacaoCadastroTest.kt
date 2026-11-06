package com.TodoListKotlin.TodoList.validacao

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ValidacaoCadastroTest {

    @Test
    fun `nome e senha validos passam`() {
        assertNull(ValidacaoCadastro.validar("kauan", "segredo123"))
    }

    @Test
    fun `nome precisa ter de 3 a 30 caracteres`() {
        assertNotNull(ValidacaoCadastro.validar("ab", "segredo123"))
        assertNull(ValidacaoCadastro.validar("abc", "segredo123"))
        assertNull(ValidacaoCadastro.validar("a".repeat(30), "segredo123"))
        assertNotNull(ValidacaoCadastro.validar("a".repeat(31), "segredo123"))
    }

    @Test
    fun `senha precisa ter pelo menos 6 caracteres`() {
        assertNotNull(ValidacaoCadastro.validar("kauan", "12345"))
        assertNull(ValidacaoCadastro.validar("kauan", "123456"))
    }

    @Test
    fun `senha de ate 72 bytes passa e acima disso nao`() {
        assertNull(ValidacaoCadastro.validar("kauan", "a".repeat(72)))
        assertNotNull(ValidacaoCadastro.validar("kauan", "a".repeat(73)))
    }

    @Test
    fun `o limite da senha conta bytes e nao caracteres`() {
        // 37 letras "é" são 37 caracteres, mas 74 bytes em UTF-8.
        val acentuada = "é".repeat(37)
        assertEquals("A senha pode ter no máximo 72 caracteres.", ValidacaoCadastro.validar("kauan", acentuada))
        assertNull(ValidacaoCadastro.validar("kauan", "é".repeat(36)))
    }
}
