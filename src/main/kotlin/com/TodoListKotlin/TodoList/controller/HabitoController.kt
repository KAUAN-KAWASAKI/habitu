package com.TodoListKotlin.TodoList.controller

import com.TodoListKotlin.TodoList.model.Habito
import com.TodoListKotlin.TodoList.model.CheckIn
import com.TodoListKotlin.TodoList.repository.CheckInRepository
import com.TodoListKotlin.TodoList.repository.HabitoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
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

    /**
     * ROTA PRINCIPAL (/home)
     * Carrega todos os hábitos e disponibiliza-os ao template HTML.
     */
    @GetMapping("/home")
    fun abrirHome(model: Model): String {
        model.addAttribute("habitos", repositorio.findAll())
        model.addAttribute("novoHabito", Habito())
        model.addAttribute(
            "feitosHoje",
            checkInRepositorio.findAllByData(LocalDate.now()).map { it.habito!!.id }.toSet()
        )
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
     */
    @PostMapping("/cadastrar")
    fun cadastrarHabito(habito: Habito): String {
        repositorio.save(habito)
        return "redirect:/home"
    }

    /**
     * ROTA DE CHECK-IN (POST /checkin/{id})
     * Se o hábito já foi feito hoje, desmarca; senão, marca.
     */
    @PostMapping("/checkin/{id}")
    fun alternarCheckIn(@PathVariable("id") id: Long): String {
        val hoje = LocalDate.now()
        if (checkInRepositorio.existsByHabitoIdAndData(id, hoje)) {
            checkInRepositorio.deleteByHabitoIdAndData(id, hoje)
        } else {
            val habito = repositorio.findById(id).orElse(null)
            if (habito != null) checkInRepositorio.save(CheckIn(habito = habito, data = hoje))
        }
        return "redirect:/home"
    }

    /**
     * ROTA DE EXCLUSÃO (/excluir/{id})
     */
    @GetMapping("/excluir/{id}")
    fun excluirHabito(@PathVariable("id") id: Long): String {
        checkInRepositorio.deleteAllByHabitoId(id)
        repositorio.deleteById(id)
        return "redirect:/home"
    }
}
