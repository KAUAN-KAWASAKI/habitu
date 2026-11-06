package com.TodoListKotlin.TodoList.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler

/**
 * Configurações de segurança da aplicação.
 */
@Configuration
class SecurityConfig {

    /**
     * Codificador de senhas BCrypt: transforma a senha em hash e compara senhas com hashes.
     */
    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    /**
     * Por enquanto libera todas as rotas, para o site continuar funcionando.
     * A restrição de acesso (login) entra no próximo passo.
     */
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        // Carrega o token CSRF logo no início da requisição (e não só quando o Thymeleaf o pede),
        // senão a página quebra no meio ao tentar criar a sessão depois de a resposta começar a ser enviada.
        val csrfHandler = CsrfTokenRequestAttributeHandler().apply { setCsrfRequestAttributeName(null) }

        http
            .authorizeHttpRequests { it.anyRequest().permitAll() }
            .csrf { it.csrfTokenRequestHandler(csrfHandler) }
        return http.build()
    }
}
