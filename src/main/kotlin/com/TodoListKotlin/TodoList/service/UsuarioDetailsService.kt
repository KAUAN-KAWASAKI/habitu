package com.TodoListKotlin.TodoList.service

import com.TodoListKotlin.TodoList.repository.UsuarioRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

/**
 * Ensina o Spring Security a buscar o usuário no banco na hora do login.
 * Ele devolve o hash guardado e o próprio Spring compara com a senha digitada (matches).
 */
@Service
class UsuarioDetailsService(
    private val usuarioRepositorio: UsuarioRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val usuario = usuarioRepositorio.findByUsername(username)
            ?: throw UsernameNotFoundException("Usuário não encontrado")

        return User.withUsername(usuario.username)
            .password(usuario.senhaHash)
            .authorities("ROLE_USER")
            .build()
    }
}
