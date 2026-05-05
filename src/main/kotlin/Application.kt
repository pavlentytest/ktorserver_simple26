
import data.database.CountryTable
import data.database.DatabaseFactory
import data.database.UserTable
import di.appModule
import io.github.smiley4.ktoropenapi.OpenApi
import io.github.smiley4.ktoropenapi.config.AuthScheme
import io.github.smiley4.ktoropenapi.config.AuthType
import io.github.smiley4.ktoropenapi.config.descriptors.type
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import plugins.configureAuthentication
import plugins.configureCallLogging
import plugins.configureContentNegotiation
import plugins.configureStatusPages
import routing.configureRouting
import security.PasswordHasher

fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        module()
    }.start(wait = true)
}

fun Application.module() {
    install(OpenApi) {
        info {
            title = "My API"
            version = "1.0.0"
        }
        server {
            url = "http://localhost:8080"
            description = "Локальный сервер"
        }
        security {
            securityScheme("MyJwtAuth") {
                type = AuthType.HTTP
                scheme = AuthScheme.BEARER
                bearerFormat = "JWT"
            }
        }
    }
    DatabaseFactory.init()
    transaction {
        if (UserTable.selectAll().where { UserTable.username eq "admin" }.empty()) {
            UserTable.insert {
                it[username] = "admin"
                it[passwordHash] = PasswordHasher.hash("secret123")
                it[role] = "admin"
            }
        }
    }
    transaction {
        CountryTable.insert {
            it[userId] = 1
            it[name] = "Россия"
            it[code] = "RU"
        }
        CountryTable.insert {
            it[userId] = 1
            it[name] = "США"
            it[code] = "US"
        }
        CountryTable.insert {
            it[userId] = 1
            it[name] = "Германия"
            it[code] = "DE"
        }
        CountryTable.insert {
            it[userId] = 1
            it[name] = "Франция"
            it[code] = "FR"
        }
    }
    appModule()
    configureContentNegotiation()
    configureCallLogging()
    configureStatusPages()
    configureAuthentication()
    configureRouting()
}