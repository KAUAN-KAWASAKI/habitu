package com.TodoListKotlin.TodoList.validacao

/**
 * Regras de nome de usuário e senha no cadastro (sem Spring, fácil de testar).
 */
object ValidacaoCadastro {
    const val USERNAME_MIN = 3
    const val USERNAME_MAX = 30
    const val SENHA_MIN = 6

    // O BCrypt só aceita até 72 bytes (letras com acento ocupam 2 bytes).
    const val SENHA_MAX_BYTES = 72

    /**
     * Devolve a mensagem de erro, ou null se o nome e a senha são válidos.
     * O nome deve chegar já sem espaços nas pontas (trim).
     */
    fun validar(username: String, senha: String): String? = when {
        username.length < USERNAME_MIN || username.length > USERNAME_MAX ->
            "O nome de usuário precisa ter de $USERNAME_MIN a $USERNAME_MAX caracteres."
        senha.length < SENHA_MIN -> "A senha precisa ter pelo menos $SENHA_MIN caracteres."
        senha.toByteArray().size > SENHA_MAX_BYTES -> "A senha pode ter no máximo $SENHA_MAX_BYTES caracteres."
        else -> null
    }
}
