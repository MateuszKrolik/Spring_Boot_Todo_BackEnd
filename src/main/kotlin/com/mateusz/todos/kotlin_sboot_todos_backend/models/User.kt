package com.mateusz.todos.kotlin_sboot_todos_backend.models

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "users")
class User {
    @Id
    var username: String? = null
    var password: String? = null
    private var enabled: Boolean = false

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    var todos: List<Todo>? = null

    constructor()

    constructor(username: String?, password: String?, enabled: Boolean, todos: List<Todo>?) {
        this.username = username
        this.password = password
        this.todos = todos
        this.enabled = enabled
    }

    override fun toString(): String {
        return ("User [username=" + username + ", password=" + password + ", enabled=" + enabled
                + ", todos=" + todos + "]")
    }
}