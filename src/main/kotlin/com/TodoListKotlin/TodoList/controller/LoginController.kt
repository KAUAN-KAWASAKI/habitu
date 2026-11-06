package com.TodoListKotlin.TodoList.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

/**
 * Página de login. A verificação da senha em si é feita pelo Spring Security.
 */
@Controller
class LoginController {

    /**
     * ROTA DE LOGIN (GET /login): mostra o formulário.
     */
    @GetMapping("/login")
    fun login(): String = "login"
}
