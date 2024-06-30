package ru.lab1.db

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import ru.lab1.db.tables.TableAnimals
import ru.lab1.db.tables.TableUsers
import ru.lab1.db.tables.TableVetExams
import ru.lab1.domain.Animal
import ru.lab1.domain.ConfidenceLevel
import ru.lab1.domain.Family
import ru.lab1.domain.Gender
import ru.lab1.domain.Role
import ru.lab1.domain.User
import ru.lab1.utils.localDateTimeToUnix
import java.time.LocalDateTime
import kotlin.random.Random

class MyAnimalsDB(dbUrl: String, salt: String) : AbstractDB(salt) {
    private val database =
        Database.connect(dbUrl, setupConnection = { connection ->
            connection.createStatement().executeUpdate("PRAGMA foreign_keys = ON")
        })

    init {
        animals =
            transaction(database) {
                TableAnimals.selectAll().map {
                    Animal(
                        it[TableAnimals.family],
                        it[TableAnimals.type],
                        it[TableAnimals.name],
                        it[TableAnimals.gender],
                        it[TableAnimals.dateOfBirth],
                        it[TableAnimals.certaintyDateOfBirth],
                        it[TableAnimals.description],
                        getExamsByAnimalId(it[TableAnimals.id].value),
                        it[TableAnimals.datetimeOfAdding],
                        it[TableAnimals.id].value,
                        it[TableAnimals.userId].value,
                    )
                }
            }.toMutableList()

        users =
            transaction(database) {
                TableUsers.select(where = TableUsers.isDeleted eq 0).map {
                    User(
                        it[TableUsers.name],
                        it[TableUsers.password],
                        it[TableUsers.date],
                        it[TableUsers.role],
                        it[TableUsers.id].value,
                    )
                }
            }.toMutableList()
    }

    // add
    fun addUserWithSQL(user: User) {
        val hashedPassword = hashPassword(user.password)

        val id =
            transaction {
                TableUsers.insertAndGetId {
                    it[name] = user.name
                    it[password] = hashedPassword
                    it[date] = user.date
                    it[role] = user.role
                }.value
            }

        super.addUser(user, id)
    }

    private fun addAnimalWithSQL(animal: Animal) {
        val id =
            transaction {
                TableAnimals.insertAndGetId {
                    it[name] = animal.name
                    it[family] = animal.family
                    it[type] = animal.type
                    it[gender] = animal.gender
                    it[dateOfBirth] = animal.dateOfBirthUnix
                    it[certaintyDateOfBirth] = animal.certaintyDateOfBirth
                    it[description] = animal.description
                    it[datetimeOfAdding] = animal.datetimeOfAddingUnix
                    it[userId] = animal.userId
                }.value
            }

        super.addAnimal(animal, id)
    }

    fun addAnimalAndGetIdWithSQL(animal: Animal): Int {
        addAnimalWithSQL(animal)
        return animals.last().id
    }

    @Throws(IllegalArgumentException::class)
    fun addExamWithSQL(
        animalId: Int,
        clinicName: String,
        doctor: String,
        examinationResult: String,
        conclusion: String,
    ) {
        getAnimalById(animalId)
            ?: throw IllegalArgumentException("Животное с указанным id не найдено")

        val dateOfAdding = localDateTimeToUnix(LocalDateTime.now())

        val id =
            transaction {
                TableVetExams.insertAndGetId {
                    it[date] = dateOfAdding
                    it[TableVetExams.clinicName] = clinicName
                    it[TableVetExams.doctor] = doctor
                    it[TableVetExams.examinationResult] = examinationResult
                    it[TableVetExams.conclusion] = conclusion
                    it[TableVetExams.animalId] = animalId
                }.value
            }

        super.addExam(animalId, clinicName, doctor, examinationResult, conclusion, id)
    }
    // add

    // set
    @Throws(IllegalArgumentException::class)
    fun setUserRoleWithSQL(
        id: Int,
        role: Role,
    ) {
        super.setUserRole(id, role)

        transaction {
            TableUsers.update({ TableUsers.id eq id }) {
                it[TableUsers.role] = role
            }
        }
    }

    @Throws(IllegalArgumentException::class)
    fun setAnimalWithSQL(animal: Animal) {
        super.setAnimal(animal)

        transaction {
            TableAnimals.update({ TableAnimals.id eq animal.id }) {
                it[name] = animal.name
                it[family] = animal.family
                it[type] = animal.type
                it[gender] = animal.gender
                it[dateOfBirth] = animal.dateOfBirthUnix
                it[certaintyDateOfBirth] = animal.certaintyDateOfBirth
                it[description] = animal.description
            }
        }
    }
    // set

    // delete
    @Throws(IllegalArgumentException::class)
    fun deleteUserWithSQL(id: Int) {
        super.deleteUser(id)

        transaction {
            TableUsers.update({ TableUsers.id eq id }) {
                it[isDeleted] = 1
            }
        }
    }

    @Throws(IllegalArgumentException::class)
    fun deleteAnimalWithSQL(id: Int) {
        super.deleteAnimal(id)

        transaction {
            TableAnimals.deleteWhere {
                TableAnimals.id eq id
            }
        }
    }

    @Throws(IllegalArgumentException::class)
    fun deleteExamWithSQL(
        idExam: Int,
        idAnimal: Int,
    ) {
        super.deleteExam(idExam, idAnimal)

        transaction {
            TableVetExams.deleteWhere {
                (TableVetExams.id eq idExam) and (animalId eq idAnimal)
            }
        }
    }
    // delete

    // these functions clear a database, use with caution

    fun randomFillDb(count: Int = 10000) {
        clear()

        // animals
        val names =
            listOf(
                "Олег", "Они", "Стеша", "Олег",
                "Владивосток", "Сьюзи", "Барсик",
                "Кеша", "Дени", "Дружок", "Хмурый",
                "Подводник", "Бурёнка", "Зефир", "Рыжик",
            )

        val types =
            listOf(
                "Кенгуру", "Орёл", "Дельфин", "Капибара",
                "Шимпанзе", "Зебра", "Жаворонок", "Корова",
                "Варан", "Пантера", "Осёл", "Медведь", "Удав",
                "Кролик", "Жираф", "Лев", "Пингвин", "Летучая мышь",
                "Тигр", "Фламинго", "Кот", "Крокодил",
            )

        // 22.11.2018 19:00 = 1700679600 unix
        val dates: MutableList<Long?> = mutableListOf()
        for (i in 0..30) {
            val newItem: Int = 1542913200 + i * 3600 * 30
            dates.add(newItem.toLong())
        }
        dates.add(null)

        val descriptions = listOf("Описание...", null)

        // exams

        // 22.11.2022 19:00 = 1669143600 unix
        val datesAfter: MutableList<Long> = mutableListOf()
        for (i in 0..30) {
            val newItem: Int = 1669143600 + i * 3600 * 7
            datesAfter.add(newItem.toLong())
        }

        val clinics =
            listOf(
                "Заботливая лапа", "Ветеринарное здоровье", "Счастливый питомец", "ЗооГарант",
                "Любимец в порядке", "Пушистый доктор", "ВетКомфорт", "Хвостик и лапка", "ВетКлиника 24/7",
                "Питомец на первом месте", "Ветеринарная гармония", "Дружба на все лапы",
                "ВетЛайф", "Пушистое спасение", "Семейная клиника для питомцев", "Забота о перьях",
            )

        val doctors =
            listOf(
                "Иванов Иван Иванович",
                "Петрова Анна Сергеевна",
                "Смирнов Алексей Владимирович",
                "Козлова Елена Дмитриевна",
                "Морозов Артем Николаевич",
                "Соловьева Наталья Васильевна",
                "Кузнецов Владимир Александрович",
                "Николаева Людмила Петровна",
                "Васильев Игорь Анатольевич",
                "Полякова Мария Викторовна",
                "Карпов Дмитрий Сергеевич",
                "Григорьева Ольга Александровна",
                "Андреев Павел Анатольевич",
                "Орлова Анастасия Игоревна",
                "Сергеев Александр Дмитриевич",
                "Тихонова Екатерина Станиславовна",
                "Федоров Артур Николаевич",
                "Медведева Ирина Владимировна",
                "Белов Алексей Викторович",
                "Соколова Татьяна Игоревна",
            )

        for (animalCount in 1..count) {
            transaction {
                TableAnimals.insert {
                    it[name] = names.random()
                    it[family] = Family.entries.toTypedArray().random()
                    it[type] = types.random()
                    it[gender] = Gender.entries.toTypedArray().random()
                    it[dateOfBirth] = dates.random()
                    it[certaintyDateOfBirth] = ConfidenceLevel.entries.toTypedArray().random()
                    it[description] = descriptions.random()
                    it[datetimeOfAdding] = datesAfter.random()
                }
            }

            for (i in 0..<Random.nextInt(0, 5)) {
                transaction {
                    TableVetExams.insert {
                        it[date] = datesAfter.random()
                        it[clinicName] = clinics.random()
                        it[doctor] = doctors.random()
                        it[examinationResult] = "Результаты анализов..."
                        it[conclusion] = "Заключение..."
                        it[animalId] = animalCount
                    }
                }
            }
        }
    }

    fun clearWithSQL() {
        transaction(database) {
            TableAnimals.deleteAll()

            // Сбрасываем автоинкрементное значение
            exec("DELETE FROM sqlite_sequence WHERE name='TableVetExams'")
            exec("DELETE FROM sqlite_sequence WHERE name='TableAnimals'")
            exec("DELETE FROM sqlite_sequence WHERE name='TableUsers'")
        }

        super.clear()
    }
}
