package com.TodoListKotlin.TodoList.controller

import com.TodoListKotlin.TodoList.model.Task
import com.TodoListKotlin.TodoList.repository.TaskRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

/**
 * Anotação @Controller indica ao Spring que esta classe processará requisições HTTP
 * e retornará páginas HTML renderizadas pelo Thymeleaf.
 */
@Controller
class TaskController {

    /**
     * @Autowired: Realiza a injeção de dependência automática do Repositório.
     * 'lateinit var' avisa ao Kotlin que o repositório será inicializado pelo Spring antes de qualquer uso.
     */
    @Autowired
    lateinit var repositorio: TaskRepository

    /**
     * ROTA PRINCIPAL (/home)
     * Carrega todas as tarefas salvas e disponibiliza os objetos para o template HTML.
     */
    @GetMapping("/home")
    fun abrirHome(model: Model): String {
        // 1. Procura todas as tarefas registadas no Banco de Dados
        val tasks = repositorio.findAll()

        // 2. Instancia um objeto vazio para ser preenchido pelo formulário de criação
        val task = Task()

        // 3. Adiciona os atributos ao objeto 'Model' que é enviado para a visualização Thymeleaf
        model.addAttribute("tasks", tasks)
        model.addAttribute("newTask", task)

        // 4. Retorna o nome do ficheiro HTML localizado em 'resources/templates/tasks.html'
        return "tasks"
    }

    /**
     * ROTA RAIZ (/)
     * Redireciona acessos à raiz da aplicação (localhost:8080/) para a rota /home.
     */
    @GetMapping("/")
    fun redirecionarParaHome(): String {
        return "redirect:/home"
    }

    /**
     * ROTA DE CADASTRO (/cadastrar)
     * Recebe os dados da tarefa enviados pelo formulário do HTML via método POST.
     */
    @PostMapping("/cadastrar")
    fun cadastrarTask(task: Task): String {
        // O método .save() grava a nova tarefa no Banco de Dados
        repositorio.save(task)

        // Redireciona o navegador de volta para a tela inicial atualizada
        return "redirect:/home"
    }

    /**
     * ROTA DE ALTERNÂNCIA DE STATUS (/toggle/{id})
     * Recebe o ID da tarefa, inverte o valor do campo 'completed' e guarda a alteração.
     */
    @GetMapping("/toggle/{id}")
    fun alternarStatusTask(@PathVariable("id") id: Long): String {
        // Busca a tarefa pelo ID no banco; se não encontrar, retorna nulo
        val task = repositorio.findById(id).orElse(null)

        if (task != null) {
            // Inverte o estado booleano (se estava false vira true e vice-versa)
            task.completed = !task.completed
            // Guarda a tarefa atualizada no Banco de Dados
            repositorio.save(task)
        }

        return "redirect:/home"
    }

    /**
     * ROTA DE EXCLUSÃO (/excluir/{id})
     * Remove a tarefa correspondente ao ID passado como parâmetro na URL.
     */
    @GetMapping("/excluir/{id}")
    fun excluirTask(@PathVariable("id") id: Long): String {
        // Executa a eliminação do registro pelo ID
        repositorio.deleteById(id)

        return "redirect:/home"
    }
}