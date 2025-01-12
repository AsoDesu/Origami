package dev.asodesu.origami.tests.`object`

import dev.asodesu.origami.behaviours.Behaviour
import dev.asodesu.origami.objects.builder.GameObject
import dev.asodesu.origami.objects.repository.GameObjectRegistry
import dev.asodesu.origami.tests.behaviours.BehaviourReferenceTests
import dev.asodesu.origami.tests.scene.SceneTests
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.assertEquals

class GameObjectInheritanceTest {

    class StringBehaviour(val string: String) : Behaviour()
    class IntBehaviour(val int: Int) : Behaviour()

    @Test
    fun testInheritance() {
        val parent = GameObject {
            name("parentObject")
            behaviours {
                add(StringBehaviour("hello"))
            }
        }

        val child = GameObject {
            extends(parent)
            name("childObject")
        }

        assertDoesNotThrow { child.get(StringBehaviour::class) }
    }

    @Test
    fun testDisposal() {
        val registry = GameObjectRegistry()
        val testOne = BehaviourReferenceTests.TestScene("scene_one")
        val testTwo = BehaviourReferenceTests.TestScene("scene_two")

        val parent = GameObject {
            extends(registry)
            name("parentObject")
            behaviours {
                add(StringBehaviour("hello"))
            }
        }

        val child = GameObject {
            extends(parent)
            name("childObject")
            behaviours(testOne) {
                add(IntBehaviour(34))
            }
            behaviours(testTwo) {
                add(SceneTests.SceneTwoTestBehaviour())
            }
        }

        val childTwo = GameObject {
            extends(child)
            name("childObject_2")
            behaviours(testTwo) {
                add(SceneTests.GlobalTestBehaviour())
            }
        }

        println(parent.debugInfo())
        assertEquals(parent.children.size, 1, "parent should have one child")
        assertEquals(registry.children.size, 1, "should not be more than 1 child in the registry")

        println("removing childObject")
        child.dispose()
        println(parent.debugInfo())

        assertEquals(parent.children.size, 0, "parent should have no children")
        assertEquals(registry.children.size, 1, "should still be 1 child in the registry")
    }

}