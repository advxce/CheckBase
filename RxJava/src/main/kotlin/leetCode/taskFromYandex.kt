package leetCode

fun main() {
    maxOnes(listOf(1, 1, 1, 1, 1, 1, 1, 1))
}

fun maxOnes(list: List<Int>) {

    val tempList = list
    // 11101101111101111
    val listCount = mutableListOf<Int>()
    var count = 0
    for (i in list.indices) {

        if (list[i] != 0 && i != list.size - 1) {
            count++
        } else if (i == list.size - 1) {
            count++
            listCount.add(count)
            count = 0
        } else {
            listCount.add(count)
            count = 0
        }

        println(count)
    }

    println("max: "+listCount.max())
}