package data.repository

import data.database.UserTable
import domain.model.User
import domain.repository.UserRepository
import org.jetbrains.exposed.sql.insertAndGetId

import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction


class UserRepositoryImpl : UserRepository {

    override suspend fun findByUsername(username: String): User? = newSuspendedTransaction {
        UserTable.selectAll().where { UserTable.username eq username }
            .map {
                User(
                    id = it[UserTable.id].value,
                    username = it[UserTable.username],
                    role = it[UserTable.role]
                )
            }
            .firstOrNull()
    }

    override suspend fun createUser(username: String, passwordHash: String, role: String): Int = newSuspendedTransaction {
        UserTable.insertAndGetId {
            it[UserTable.username] = username
            it[UserTable.passwordHash] = passwordHash
            it[UserTable.role] = role
        }.value
    }

    override suspend fun findById(id: Int): User? = newSuspendedTransaction {
        UserTable.selectAll().where { UserTable.id eq id }
            .map {
                User(
                    id = it[UserTable.id].value,
                    username = it[UserTable.username],
                    role = it[UserTable.role]
                )
            }
            .firstOrNull()
    }
}