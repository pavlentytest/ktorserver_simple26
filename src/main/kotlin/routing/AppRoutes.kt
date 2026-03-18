package routing

import di.AppContainer
import io.ktor.server.application.Application
import io.ktor.server.routing.*


fun Application.configureRouting() {
    val greetingController = AppContainer.greetingController
    greetingController.configureRoutes(this)
    println("Маршруты настроены")
}