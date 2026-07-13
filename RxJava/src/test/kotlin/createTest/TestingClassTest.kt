package createTest

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TestingClassTest {
    @Test
    fun getPrice() {
        val price = 100.0
        val discount = 10
        val testObject = TestingClass()

        val result = testObject.getPrice(price, discount)

        assertEquals(90.0, result)
    }

}