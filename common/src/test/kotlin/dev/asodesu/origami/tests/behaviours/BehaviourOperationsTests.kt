package dev.asodesu.origami.tests.behaviours

import dev.asodesu.origami.behaviours.Behaviour
import dev.asodesu.origami.objects.GameObject
import dev.asodesu.origami.objects.builder.GameObject
import dev.asodesu.origami.util.randomString
import java.lang.IllegalStateException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.random.Random
import kotlin.test.assertEquals

class BehaviourOperationsTests {

    class StringBehaviour(val string: String) : Behaviour() {
        fun addInt(int: Int) = add(IntBehaviour(int))
    }
    class IntBehaviour(val int: Int) : Behaviour() {
        fun replaceString(string: String) = replace(StringBehaviour(string))
    }

    @Test
    fun testLocalAddition() {
        val testStr = randomString(8)
        val testInt = Random.nextInt(0, 50)
        val gameObject = GameObject {}

        val stringBehaviour = StringBehaviour(testStr).also { gameObject.global().add(it) }
        assertDoesNotThrow("thew when adding behaviour") { stringBehaviour.addInt(testInt) }
        val intBehaviour = assertDoesNotThrow("could not retrieve added behaviour") { gameObject.get(IntBehaviour::class) }
        assertEquals(intBehaviour.int, testInt, "returned behaviour was not of expected")
    }

}