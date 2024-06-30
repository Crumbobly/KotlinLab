package ru.lab1.domain

data class Permissions(val role: Role) {
    val canAddModDelHisAnimal: Boolean
    val canAddModDelAllAnimal: Boolean

    val canAddModDelVetHisExam: Boolean
    val canAddModDelVetAllExam: Boolean

    val canSeeListUser: Boolean
    val canAddDelUser: Boolean

    init {

        when (role) {
            Role.ADMIN -> {
                canAddModDelHisAnimal = true
                canAddModDelAllAnimal = true
                canAddModDelVetHisExam = true
                canAddModDelVetAllExam = true
                canSeeListUser = true
                canAddDelUser = true
            }
            Role.MODERATOR -> {
                canAddModDelHisAnimal = true
                canAddModDelAllAnimal = true
                canAddModDelVetHisExam = true
                canAddModDelVetAllExam = true
                canSeeListUser = true
                canAddDelUser = false
            }
            Role.WORKER -> {
                canAddModDelHisAnimal = true
                canAddModDelAllAnimal = false
                canAddModDelVetHisExam = false
                canAddModDelVetAllExam = false
                canSeeListUser = false
                canAddDelUser = false
            }
            Role.VET -> {
                canAddModDelHisAnimal = false
                canAddModDelAllAnimal = false
                canAddModDelVetHisExam = true
                canAddModDelVetAllExam = false
                canSeeListUser = false
                canAddDelUser = false
            }
            Role.AUTHUSER -> {
                canAddModDelHisAnimal = false
                canAddModDelAllAnimal = false
                canAddModDelVetHisExam = false
                canAddModDelVetAllExam = false
                canSeeListUser = false
                canAddDelUser = false
            }
            Role.NOAUTHUSER -> {
                canAddModDelHisAnimal = false
                canAddModDelAllAnimal = false
                canAddModDelVetHisExam = false
                canAddModDelVetAllExam = false
                canSeeListUser = false
                canAddDelUser = false
            }
        }
    }
}
