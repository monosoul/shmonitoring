package dev.monosoul.shmonitoring.model

sealed class Validated<T> {
    abstract fun throwIfInvalid()
    class Valid<T>(val value: T) : Validated<T>() {
        override fun throwIfInvalid() = Unit
    }

    class Invalid<T>(val errors: List<String>) : Validated<T>() {
        constructor(vararg errors: String) : this(errors.toList())

        override fun throwIfInvalid() {
            throw IllegalArgumentException(errors.joinToString("\n"))
        }
    }
}
