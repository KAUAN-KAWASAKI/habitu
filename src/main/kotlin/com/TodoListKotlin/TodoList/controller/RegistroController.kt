package com.TodoListKotlin.TodoList.controller

import com.TodoListKotlin.TodoList.model.Usuario
import com.TodoListKotlin.TodoList.repository.UsuarioRepository
import com.TodoListKotlin.TodoList.validacao.ValidacaoCadastro
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

        val erro = ValidacaoCadastro.validar(nome, senha)
            ?: if (usuarioRepositorio.findByUsername(nome) != null) "Esse nome de usuário já está em uso." else null
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
