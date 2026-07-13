package leetCode

fun main(){
    val array = intArrayOf(1,2,2,5,6,4,4)
//    twoSum(array, 4).forEach { print("$it ") }
//    twoSumDefault(array, 4).forEach { print("$it ") }
    twoSumMap(array,6).forEach(::println)
}

fun twoSum(nums: IntArray, target: Int): IntArray {
    val indicesArray = IntArray(2)
    var middle = nums.size/2
    var left = 0
    var right = nums.size-1
    while (left<=middle){
        if(nums[left] + nums[right] == target){
            indicesArray[0] = left
            indicesArray[1] = right
//            println(indicesArray)
            println("0 : ${indicesArray[0]}")
            println("1 : ${indicesArray[1]}")
            return indicesArray
        }
        else if(nums[left] + nums[right] != target && right>middle){
            right--
        }
        else if(right == middle && nums[left] + nums[right] != target){
            left++
            right = nums.size-1

        }
        else{
            left++
        }
    }

    println("left:$left,right:$right")
//        println(indicesArray)

    println("0 : ${indicesArray[0]}")
    println("1 : ${indicesArray[1]}")
     return indicesArray
}

fun twoSumDefault(nums: IntArray, target: Int): IntArray{
    val indicesArray = IntArray(2)
    for(i in 0 until nums.size){
        for(j in 0 until nums.size){
            if(j != i && nums[i] + nums[j] == target){
                indicesArray[0] = i
                indicesArray[1] = j
                return indicesArray
            }
        }
    }
    return indicesArray
}


fun twoSumMap(nums: IntArray, target: Int): IntArray{
    val seen = HashMap<Int, Int>()
    for(i in nums.indices){
        val complement = target - nums[i]
        if(seen.contains(complement)){
            return intArrayOf(seen[complement]!!, i)
        }
        seen[nums[i]] = i
    }

    return intArrayOf()
}