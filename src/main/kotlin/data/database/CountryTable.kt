package data.database

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object CountryTable : Table("countries") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id").references(
        ref = UserTable.id,
        onDelete = ReferenceOption.RESTRICT,
        onUpdate = ReferenceOption.RESTRICT
    )
    val name = varchar("name", 100)
    val code = varchar("code", 3)
    val visitedAt = long("visited_at").clientDefault { System.currentTimeMillis() }

    override val primaryKey = PrimaryKey(id)
}