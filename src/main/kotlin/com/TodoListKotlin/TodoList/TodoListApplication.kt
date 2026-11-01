package com.TodoListKotlin.TodoList

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * Anotação principal do Spring Boot.
 * Inicializa a auto-configuração, a varredura de componentes (Controllers, Repositories)
 * e o servidor web embutido (Tomcat).
 */
@SpringBootApplication
class TodoListApplication

/**
 * Função main: Ponto de entrada padrão em aplicações Kotlin/Java.
 */
fun main(args: Array<String>) {
	runApplication<TodoListApplication>(*args)
}