package com.TodoListKotlin.TodoList.controller

import com.TodoListKotlin.TodoList.model.Habito
import com.TodoListKotlin.TodoList.gamificacao.Gamificacao
import com.TodoListKotlin.TodoList.model.CheckIn
import com.TodoListKotlin.TodoList.repository.CheckInRepository
import com.TodoListKotlin.TodoList.repository.HabitoRepository
import com.TodoListKotlin.TodoList.repository.UsuarioRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.server.ResponseStatusException
import java.security.Principal
import java.time.LocalDate

/**
 * Processa requisições HTTP e retorna páginas HTML renderizadas pelo Thymeleaf.
 */
@Controller
class HabitoController {

    @Autowired
    lateinit var repositorio: HabitoRepository

    @Autowired
    lateinit var checkInRepositorio: CheckInRepository

    @Autowired
    lateinit var usuarioRepositorio: UsuarioRepository

    /**
     * ROTA PRINCIPAL (/home)
     * Carrega todos os hábitos e disponibiliza-os ao template HTML.
     */
    @GetMapping("/home")
    fun abrirHome(model: Model, principal: Principal): String {
        model.addAttribute("usuarioLogado", principal.name)
        val habitos = repositorio.findAllByUsuarioUsername(principal.name)
        model.addAttribute("habitos", habitos)
        model.addAttribute("novoHabito", Habito())
        model.addAttribute(
            "feitosHoje",
            checkInRepositorio.findAllByHabitoUsuarioUsernameAndData(principal.name, LocalDate.now()).map { it.habito!!.id }.toHashSet()
        )

        val checkIns = checkInRepositorio.findAllByHabitoUsuarioUsername(principal.name)
        val datasPorHabito = checkIns
            .groupBy { it.habito!!.id }
            .mapValues { entry -> entry.value.map { it.data }.toSet() }
        val sequencias = habitos.associate {
            it.id to Gamificacao.sequencia(datasPorHabito[it.id] ?: emptySet(), LocalDate.now())
        }
        model.addAttribute("sequencias", sequencias)

        val totalCheckIns = checkIns.size.toLong()
        val xp = Gamificacao.xpTotal(totalCheckIns)
        model.addAttribute("xp", xp)
        model.addAttribute("nivel", Gamificacao.nivel(xp))
        model.addAttribute("xpNoNivel", Gamificacao.xpNoNivel(xp))

        val nivel = Gamificacao.nivel(xp)
        val maiorSequencia = sequencias.values.maxOrNull() ?: 0
        model.addAttribute("conquistas", Gamificacao.conquistas(totalCheckIns, maiorSequencia, nivel))
        return "tasks"
    }

    /**
     * ROTA RAIZ (/) - redireciona para /home.
     */
    @GetMapping("/")
    fun redirecionarParaHome(): String {
        return "redirect:/home"
    }

    /**
     * ROTA DE CADASTRO (/cadastrar)
     * Recebe só o nome (e não um Habito inteiro) para ninguém definir id ou dono pelo formulário.
     */
    @PostMapping("/cadastrar")
    fun cadastrarHabito(@RequestParam nome: String, principal: Principal): String {
        val nomeLimpo = nome.trim()
        if (nomeLimpo.isNotEmpty()) {
            val dono = usuarioRepositorio.findByUsername(principal.name)
            repositorio.save(Habito(nome = nomeLimpo.take(80), usuario = dono))
        }
        return "redirect:/home"
    }

    /**
     * ROTA DE CHECK-IN (POST /checkin/{id})
     * Se o hábito já foi feito hoje, desmarca; senão, marca.
     */
    @PostMapping("/checkin/{id}")
    fun alternarCheckIn(@PathVariable("id") id: Long, principal: Principal): String {
        val habito = habitoDoUsuario(id, principal)
        val hoje = LocalDate.now()
        if (checkInRepositorio.existsByHabitoIdAndData(habito.id, hoje)) {
            checkInRepositorio.deleteByHabitoIdAndData(habito.id, hoje)
        } else {
            checkInRepositorio.save(CheckIn(habito = habito, data = hoje))
        }
        return "redirect:/home"
    }

    /**
     * ROTA DE EXCLUSÃO (/excluir/{id})
     */
    @PostMapping("/excluir/{id}")
    fun excluirHabito(@PathVariable("id") id: Long, principal: Principal): String {
        val habito = habitoDoUsuario(id, principal)
        checkInRepositorio.deleteAllByHabitoId(habito.id)
        repositorio.delete(habito)
        return "redirect:/home"
    }

    /**
     * Devolve o hábito só se for do usuário logado. Se for de outra pessoa (ou não existir),
     * responde 404, sem revelar que o id existe.
     */
    private fun habitoDoUsuario(id: Long, principal: Principal): Habito =
        repositorio.findByIdAndUsuarioUsername(id, principal.name)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
}
