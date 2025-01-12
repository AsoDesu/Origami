package dev.asodesu.origami.tests.scene

import dev.asodesu.origami.scene.GlobalScene
import dev.asodesu.origami.scene.asChildOf
import dev.asodesu.origami.tests.scene.SceneTests.GlobalTestBehaviour
import dev.asodesu.origami.tests.scene.SceneTests.SceneOneTestBehaviour
import dev.asodesu.origami.tests.scene.SceneTests.SceneTwoTestBehaviour
import dev.asodesu.origami.tests.scene.SceneTests.TestScene
import java.lang.IllegalArgumentException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class SceneInheritanceTests {

    @Test
    fun testSceneHierarchy() {
        val parentScene = TestScene("parentScene")
        val parentScene2 = TestScene("parentScene2")
        val childScene = TestScene("childScene")

        assertThrows<IllegalArgumentException>("did not throw when adding child of self") { parentScene.addChild(parentScene) }
        assertThrows<IllegalArgumentException>("did not throw when removing a non-child") { parentScene.removeChild(childScene) }
        assertDoesNotThrow { parentScene.addChild(childScene) }

        assertEquals(parentScene.gameObjects.size, 1, "did not have exactly one child")
        assertEquals(childScene.parent, parentScene, "child scene's parent was not set correctly")

        assertThrows<IllegalArgumentException>("did not throw when adding assigned child") { parentScene2.addChild(childScene) }
        assertThrows<IllegalArgumentException>("did not throw when removing child that was not assigned") { parentScene2.removeChild(childScene) }
    }

    @Test
    fun testSceneBehaviourInheritance() {
        val parentScene = TestScene("parentScene")
        val childScene = TestScene("childScene").asChildOf(parentScene)

        GlobalScene.add(GlobalTestBehaviour())
        parentScene.add(SceneOneTestBehaviour())
        childScene.add(SceneTwoTestBehaviour())

        assertDoesNotThrow("could not get from parent: GlobalScene") { parentScene.get(GlobalTestBehaviour::class) }
        assertDoesNotThrow("could not get from global two layers deep") { childScene.get(GlobalTestBehaviour::class) }

        assertDoesNotThrow("could not get from parent scene") { childScene.get(SceneOneTestBehaviour::class) }
    }

    @Test
    fun testSceneRemoval() {
        val parentScene = TestScene("parentScene")
        parentScene.add(SceneOneTestBehaviour())

        val childScene = TestScene("childScene").asChildOf(parentScene)

        // assure was added correctly
        assertEquals(parentScene.gameObjects.size, 1, "child was not added correctly")
        assertEquals(childScene.parent, parentScene, "child's parent scene was not assigned correctly")
        assertDoesNotThrow("could not retrieve parent behaviour") { childScene.get(SceneOneTestBehaviour::class) }

        println(parentScene.debugInfo())

        assertDoesNotThrow("threw when removing child") { parentScene.removeChild(childScene) }
        assertEquals(parentScene.gameObjects.size, 0, "child was not removed from children")
        assertEquals(childScene.parent, GlobalScene, "child's parent scene was not re-assigned")
        assertThrows<NoSuchElementException>("did not fail to retrieve parent behaviour") { childScene.get(SceneOneTestBehaviour::class) }
    }

}