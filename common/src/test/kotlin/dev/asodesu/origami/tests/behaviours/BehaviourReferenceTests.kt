package dev.asodesu.origami.tests.behaviours

import dev.asodesu.origami.behaviours.Behaviour
import dev.asodesu.origami.objects.GameObject
import dev.asodesu.origami.objects.builder.GameObject
import dev.asodesu.origami.scene.Scene
import dev.asodesu.origami.util.randomString
import java.lang.IllegalStateException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class BehaviourReferenceTests {

    class StringBehaviour(val string: String) : Behaviour()
    class TestBehaviourTwo : Behaviour()
    class StringReferenceBehaviour : Behaviour() {
        val stringBehaviour by reference<StringBehaviour>()
        fun getStr() = stringBehaviour.string
    }

    class ExternalReferenceBehaviour(refObject: GameObject) : Behaviour() {
        val stringBehaviour by refObject.reference<StringBehaviour>()
        fun getStr() = stringBehaviour.string
    }

    class TestScene(override val name: String) : Scene()
    class SceneReferenceBehaviour : Behaviour() {
        val stringBehaviour by sceneReference<StringBehaviour>()
        fun getStr() = stringBehaviour.string
    }

    @Test
    fun testLocalReference() {
        val testStr = randomString(8)
        val gameObject = GameObject {}

        val behaviour = StringReferenceBehaviour()
        gameObject.global().add(behaviour)
        assertThrows<IllegalStateException>("reference did not throw when resolved") { behaviour.getStr() }

        gameObject.global().add(StringBehaviour(testStr))

        val str = assertDoesNotThrow("reference threw when retrieved") { behaviour.getStr() }
        assertEquals(str, testStr, "referenced behaviour was not correct")
    }

    @Test
    fun testLocalReferenceOnInit() {
        val testStr = randomString(8)
        val gameObject = GameObject {
            behaviours {
                add(StringBehaviour(testStr))
                add(StringReferenceBehaviour())
            }
        }

        val ref = gameObject.get(StringReferenceBehaviour::class)
        val str = assertDoesNotThrow("reference threw when retrieved") { ref.getStr() }
        assertEquals(str, testStr, "referenced behaviour was not correct")
    }

    @Test
    fun testExternalReferenceOnInit() {
        val testStr = randomString(8)
        val holderObject = GameObject {
            name("holderObject")
            behaviours {
                add(StringBehaviour(testStr))
            }
        }

        val referenceObject = GameObject {
            name("refObject")
            behaviours {
                add(ExternalReferenceBehaviour(holderObject))
            }
        }

        val ref = referenceObject.get(ExternalReferenceBehaviour::class)
        val str = assertDoesNotThrow("reference threw when retrieved") { ref.getStr() }
        assertEquals(str, testStr, "referenced behaviour was not correct")
    }

    @Test
    fun testSceneReferenceTest() {
        val scene = TestScene("testScene")
        val gameObject = GameObject {}

        val ref = SceneReferenceBehaviour()
        gameObject.scene(scene).add(ref)

        assertThrows<IllegalStateException>("resolving reference did not throw") { ref.getStr() }

        scene.add(StringBehaviour("testStr"))
        assertDoesNotThrow("resolving reference failed after behaviour was added to scene") { ref.getStr() }

        scene.remove(StringBehaviour::class)
        assertThrows<IllegalStateException>("resolving reference did not throw") { ref.getStr() }
    }

}