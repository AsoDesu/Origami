package dev.asodesu.origami.tests.`object`

import dev.asodesu.origami.behaviours.Behaviour
import dev.asodesu.origami.objects.builder.GameObject
import dev.asodesu.origami.util.randomString
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.random.Random
import kotlin.test.assertEquals

class GameObjectTests {

    class StringBehaviour(val string: String) : Behaviour()
    class IntBehaviour(val int: Int) : Behaviour()

    @Test
    fun testBuildingAndRetrieval() {
        val testStr = randomString(8)

        val gameObject = GameObject {
            name("testGameObject1")
            behaviours {
                add(StringBehaviour(testStr))
            }
        }

        // test retrieving a behaviour
        val gottenObject = assertDoesNotThrow("threw when getting behaviour") { gameObject.get(StringBehaviour::class) }
        assertEquals(gottenObject.string, testStr, "returned behaviour was not expected")

        // test retrieving a non-existent behaviour
        assertThrows<NoSuchElementException> { gameObject.get(IntBehaviour::class) }
    }

    @Test
    fun testAdding() {
        val testInt = Random.nextInt(0, 50)
        val gameObject = GameObject {
            name("testGameObject1")
        }

        gameObject.global().add(IntBehaviour(testInt))

        // test retrieving a behaviour
        val gottenObject = assertDoesNotThrow("threw when getting behaviour") { gameObject.get(IntBehaviour::class) }
        assertEquals(gottenObject.int, testInt, "returned behaviour was not expected")
    }

}