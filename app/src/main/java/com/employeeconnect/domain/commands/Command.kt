package com.employeeconnect.domain.commands

interface Command<out T> {
    fun execute(): T
}