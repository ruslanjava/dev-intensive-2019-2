package ru.skillbranch.devintensive.models

class Bender(var status:Status = Status.NORMAL, var question:Question = Question.NAME) {

    fun askQuestion(): String {
        return question.question
    }

    fun listenAnswer(answer:String) : Pair<String, Triple<Int, Int, Int>> {
        if (question.answers.contains(answer)) {
            question = question.nextQuestion()
            return if (question != Question.IDLE) {
                "Отлично - ты справился\n${question.question}" to status.color
            } else {
                "Отлично - ты справился\nНа этом все, вопросов больше нет" to status.color
            }
        }

        val message = question.validationHint(answer)
        if (!message.isEmpty()) {
            return "$message\n${question.question}" to status.color
        }

        return if (status < Status.CRITICAL) {
            status = status.nextStatus()
            "Это неправильный ответ\n${question.question}" to status.color
        } else {
            status = Status.NORMAL
            question = Question.NAME
            "Это неправильный ответ. Давай все по новой\n${question.question}" to status.color
        }
    }

    enum class Status(val color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 0, 0));

        fun nextStatus() : Status {
            return if (this.ordinal < values().lastIndex) {
                values()[this.ordinal + 1]
            } else {
                values()[0]
            }
        }
    }

    enum class Question(val question:String, val answers:List<String>, val validator:Validator) {
        NAME("Как меня зовут?", listOf("Бендер", "Bender"), Validator.NAME) {
            override fun nextQuestion(): Question = PROFESSION
        },
        PROFESSION("Назови мою профессию?", listOf("сгибальщик", "bender"), Validator.PROFESSION) {
            override fun nextQuestion(): Question = MATERIAL
        },
        MATERIAL("Из чего я сделан?", listOf("металл", "дерево", "metal", "iron", "wood"), Validator.MATERIAL){
            override fun nextQuestion(): Question = BDAY
        },
        BDAY("Когда меня создали?", listOf("2993"), Validator.BDAY){
            override fun nextQuestion(): Question = SERIAL
        },
        SERIAL("Мой серийный номер?", listOf("2716057"), Validator.SERIAL) {
            override fun nextQuestion(): Question = IDLE
        },
        IDLE("На этом все, вопросов больше нет", listOf(), Validator.IDLE) {
            override fun nextQuestion(): Question = IDLE
        };

        abstract fun nextQuestion() : Question

        fun validationHint(answer:String) : String {
            if (validator.validate(answer)) {
                return ""
            }
            return validator.hint
        }

    }

    enum class Validator(val hint:String) {
        NAME("Имя должно начинаться с заглавной буквы") {
            override fun validate(answer: String): Boolean {
                return when {
                    answer.isEmpty() -> false
                    answer.get(0).isUpperCase() -> true
                    else -> false
                }
            }
        },
        PROFESSION("Профессия должна начинаться со строчной буквы") {
            override fun validate(answer: String): Boolean {
                return when {
                    answer.isEmpty() -> false
                    answer.get(0).isLowerCase() -> true
                    else -> false
                }
            }
        },
        MATERIAL("Материал не должен содержать цифр") {
            override fun validate(answer: String): Boolean {
                if (answer.isEmpty()) {
                    return true
                }
                return !answer.contains(Regex("\\d"))
            }
        },
        BDAY("Год моего рождения должен содержать только цифры") {
            override fun validate(answer: String): Boolean {
                if (answer.isEmpty()) {
                    return false
                }
                return answer.matches(Regex("^\\d+$"))
            }
        },
        SERIAL("Серийный номер содержит только цифры, и их 7") {
            override fun validate(answer: String): Boolean {
                return answer.matches(Regex("^[0-9]{7}$"))
            }
        },
        IDLE("") {
            override fun validate(answer: String): Boolean {
                return true
            }
        };

        abstract fun validate(answer : String) : Boolean

    }

}