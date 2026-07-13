package createTest

class TestingClass {
    fun getPrice(price:Double, discount:Int):Double{
        return price - price*discount/100
    }
}