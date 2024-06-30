package ru.lab1.db

import ru.lab1.domain.Animal
import ru.lab1.domain.Family
import ru.lab1.domain.Gender
import ru.lab1.domain.Role
import ru.lab1.domain.User
import ru.lab1.domain.VetExamResult
import ru.lab1.utils.localDateTimeToUnix
import java.security.MessageDigest
import java.time.LocalDateTime

abstract class AbstractDB(private val salt: String) {
    protected var animals: MutableList<Animal> = mutableListOf()
    protected var users: MutableList<User> = mutableListOf()

    protected fun hashPassword(password: String): String {
        val saltedPassword = password + salt
        val md = MessageDigest.getInstance("SHA-256")
        val hashBytes = md.digest(saltedPassword.toByteArray())
        return hashBytes.joinToString("") { "%02x".format(it) }
    }

    fun checkPassword(
        userName: String,
        password: String,
    ): Boolean {
        val hashPassword = hashPassword(password)
        val user = getUserByName(userName)
        return user != null && user.password == hashPassword
    }

    fun filter(
        name: String?,
        type: String?,
        families: List<Family?>,
        genders: List<Gender?>,
    ): List<Animal> {
        val genderAllNull = genders.all { it == null }
        val familiesAllNull = families.all { it == null }

        return animals.filter { animal ->
            name?.let { animal.name.contains(name, ignoreCase = true) } ?: true &&
                type?.let { animal.type.contains(type, ignoreCase = true) } ?: true &&
                (animal.family in families || familiesAllNull) &&
                (animal.gender in genders || genderAllNull)
        }
    }

    // get
    fun getAllUsers(): List<User> {
        return users
    }

    fun getAllAnimals(): List<Animal> {
        return animals
    }

    fun getUserById(id: Int): User? {
        return users.find { user: User -> user.id == id }
    }

    fun getUserByName(name: String): User? {
        return users.find { user: User -> user.name == name }
    }

    fun getAnimalById(id: Int): Animal? {
        return animals.find { animal: Animal -> animal.id == id }
    }

    protected fun getExamsByAnimalId(id: Int): MutableList<VetExamResult> {
        val animal = getAnimalById(id)
        if (animal != null) {
            return animal.getVetExamResults()
        }
        return mutableListOf()
    }
    // get

    fun addAnimalAndGetId(
        animal: Animal,
        id: Int = 0,
    ): Int {
        addAnimal(animal, id)
        return animals.last().id
    }

    // add
    fun addUser(
        user: User,
        id: Int = 0,
    ) {
        val hashedPassword = hashPassword(user.password)
        users.add(user.copy(id = id, password = hashedPassword))
    }

    fun addAnimal(
        animal: Animal,
        id: Int = 0,
    ) {
        animals.add(animal.copy(id = id))
    }

    @Throws(IllegalArgumentException::class)
    fun addExam(
        animalId: Int,
        clinicName: String,
        doctor: String,
        examinationResult: String,
        conclusion: String,
        id: Int = 0,
    ) {
        val animal =
            getAnimalById(animalId)
                ?: throw IllegalArgumentException("Животное с указанным id не найдено")

        val dateOfAdding = localDateTimeToUnix(LocalDateTime.now())

        animal.getVetExamResults().add(
            VetExamResult(
                dateOfAdding,
                clinicName,
                doctor,
                examinationResult,
                conclusion,
                id,
            ),
        )
    }
    // add

    // set
    @JvmName("setAnimalsList")
    fun setAnimals(animals: MutableList<Animal>) {
        this.animals = animals
    }

    @JvmName("setUsersList")
    fun setUsers(users: MutableList<User>) {
        this.users = users
    }

    fun setUserRole(
        id: Int,
        role: Role,
    ) {
        val user =
            getUserById(id)
                ?: throw IllegalArgumentException("Пользователь с указанным id не найден")

        val indexOldUser = users.indexOf(user)
        users[indexOldUser] = user.copy(role = role)
    }

    fun setAnimal(animal: Animal) {
        val oldAnimal =
            getAnimalById(animal.id)
                ?: throw IllegalArgumentException("Животное с указанным id не найдено")

        val indexOldAnimal = animals.indexOf(oldAnimal)
        animals[indexOldAnimal] = animal
    }
    // set

    // delete
    @Throws(IllegalArgumentException::class)
    fun deleteUser(id: Int) {
        val toDeleteUser =
            getUserById(id)
                ?: throw IllegalArgumentException("Пользователь с указанным id не найден")

        users.remove(toDeleteUser)
    }

    @Throws(IllegalArgumentException::class)
    fun deleteAnimal(id: Int) {
        val toDeleteAnimal =
            getAnimalById(id)
                ?: throw IllegalArgumentException("Животное с указанным id не найдено")

        animals.remove(toDeleteAnimal)
    }

    @Throws(IllegalArgumentException::class)
    fun deleteExam(
        idExam: Int,
        idAnimal: Int,
    ) {
        val animal =
            animals.find { animal: Animal -> animal.id == idAnimal }
                ?: throw IllegalArgumentException("Животное с указанным id не найдено")

        val examToDelete =
            animal.getVetExamResults().find { vetExamResult -> vetExamResult.id == idExam }
                ?: throw IllegalArgumentException("Анализ с указанным id не найден")

        animal.getVetExamResults().remove(examToDelete)
    }
    // delete

    fun clear() {
        animals.clear()
        users.clear()
    }
}
