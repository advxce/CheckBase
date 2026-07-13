package TechMeSkilsTasks

import io.reactivex.rxjava3.core.Observable


data class Student(val name: String, val id: Int)

fun main() {

    val studentList = mutableListOf<Student>()
    repeat(4){ id->
        repeat(4){ name->
            studentList.add(Student("Student $name", name))
        }
    }
    studentList.forEach{println(it)}
    println("result")

    val observable = Observable.fromIterable(studentList)
        .distinct {student-> student.id
        }

    observable.subscribe{
        println(it)
    }
}