package com.mateusz.todos.kotlin_sboot_todos_backend.models

import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import jakarta.validation.constraints.Future
import jakarta.validation.constraints.Size
import java.time.LocalDate

@Entity
class Todo {
    @Id
    @GeneratedValue
    var id: Int? = null

    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    @JsonIgnore
    var user: User? = null

    @get:JsonGetter("username")
    val username: String?
        get() = user!!.username

    var description: @Size(min = 5) String? = null

    var targetDate: @Future LocalDate? = null // LocalDate recommended over Date

    var done: Boolean = false

    constructor(id: Int?, user: User?, description: String?, targetDate: LocalDate?, done: Boolean) {
        this.id = id
        this.user = user
        this.description = description
        this.targetDate = targetDate
        this.done = done
    }

    constructor()

    override fun toString(): String {
        return ("Todo [id=" + id + ", user=" + user + ", description=" + description + ", targetDate=" + targetDate
                + ", done=" + done + "]")
    }
}