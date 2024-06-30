package ru.lab1.web.validation

typealias FormErrors = MutableMap<String, String>

abstract class Validator {
    protected val errors: FormErrors = mutableMapOf()

    abstract fun validation()

    fun isValid(): Boolean = errors.isEmpty()

    fun getErrorMsg(errorName: String) = errors[errorName]

    fun getErrorVal(errorName: String) = !errors.contains(errorName)
}
