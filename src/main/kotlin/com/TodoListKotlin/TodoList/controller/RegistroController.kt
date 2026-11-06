package com.TodoListKotlin.TodoList.controller

import com.TodoListKotlin.TodoList.model.Usuario
import com.TodoListKotlin.TodoList.repository.UsuarioRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

/**
 * Cadastro de novos usuários. A senha é guardada apenas como hash BCrypt.
 */
@Controller
class RegistroController(
    private val usuarioRepositorio: UsuarioRepository,
    private val passwordEncoder: PasswordEncoder
) {

    /**
     * ROTA DE CADASTRO (GET /registrar): mostra o formulário.
     */
    @GetMapping("/registrar")
    fun formulario(): String = "registrar"

    /**
     * ROTA DE CADASTRO (POST /registrar): valida, gera o hash da senha e salva o usuário.
     */
    @PostMapping("/registrar")
    fun registrar(
        @RequestParam username: String,
        @RequestParam senha: String,
        model: Model
    ): String {
        val nome = username.trim()

        val erro = when {
            nome.length < 3 || nome.length > 30 -> "O nome de usuário precisa ter de 3 a 30 caracteres."
            senha.length < 6 -> "A senha precisa ter pelo menos 6 caracteres."
            // O BCrypt só aceita até 72 bytes (letras com acento ocupam 2 bytes).
            senha.toByteArray().size > 72 -> "A senha pode ter no máximo 72 caracteres."
            usuarioRepositorio.findByUsername(nome) != null -> "Esse nome de usuário já está em uso."
            else -> null
        }
        if (erro != null) return mostrarErro(model, nome, erro)

        try {
            usuarioRepositorio.save(Usuario(username = nome, senhaHash = passwordEncoder.encode(senha)!!))
        } catch (e: DataIntegrityViolationException) {
            // Outro cadastro com o mesmo nome entrou entre a consulta e o save (a coluna é unique).
            return mostrarErro(model, nome, "Esse nome de usuário já está em uso.")
        }
        return "redirect:/registrar?sucesso"
    }

    private fun mostrarErro(model: Model, username: String, erro: String): String {
        model.addAttribute("erro", erro)
        model.addAttribute("username", username)
        return "registrar"
    }
}
