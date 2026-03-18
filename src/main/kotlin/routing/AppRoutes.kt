package routing

import di.AppContainer
import io.ktor.server.application.Application
import io.ktor.server.routing.routing


fun Application.configureRouting() {
    AppContainer.authController.configure(this)
    AppContainer.userController.configure(this)
}