package com.mateusz.spring_boot_todos.models;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;

@Entity
public class Todo {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    @JsonIgnore
    private User user;

    @JsonGetter("username")
    public String getUsername() {
        return this.user.getUsername();
    }

    @Size(min = 5)
    private String description;

    @Future
    private LocalDate targetDate;// LocalDate recommended over Date

    private boolean done;

    public Todo(Integer id, User user, String description, LocalDate targetDate, boolean done) {
        this.id = id;
        this.user = user;
        this.description = description;
        this.targetDate = targetDate;
        this.done = done;
    }

    public Todo() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(LocalDate targetDate) {
        this.targetDate = targetDate;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @Override
    public String toString() {
        return "Todo [id=" + id + ", user=" + user + ", description=" + description + ", targetDate=" + targetDate
                + ", done=" + done + "]";
    }

}