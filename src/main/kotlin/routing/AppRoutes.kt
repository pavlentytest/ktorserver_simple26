package routing

import di.AppContainer
import io.github.smiley4.ktoropenapi.openApi
import io.github.smiley4.ktorredoc.redoc
//import io.github.smiley4.ktorswaggerui.routing.swaggerUI
import io.ktor.server.application.Application
import io.ktor.server.response.respond
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing


fun Application.configureRouting() {
    routing {
        route("api.json") {
            openApi()
        }
        // Роут с интерфейсом ReDoc
        route("docs") {
            redoc("/api.json") { // указываем на роут выше
                pageTitle = "My API Docs"
            }
        }
        get("/test") {
            call.respond(mapOf("message" to "Это открытый маршрут!"))
        }
    }
    AppContainer.authController.configure(this)
    AppContainer.userController.configure(this)
}