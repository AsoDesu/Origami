package dev.asodesu.origami.testplugin.behaviours

import dev.asodesu.origami.engine.Behaviour
import dev.asodesu.origami.engine.BehaviourApplicable

class TestBehaviour(val applied: BehaviourApplicable) : Behaviour() {
    var number = 1

    constructor(applicable: BehaviourApplicable, number: Int) : this(applicable) {
        this.number = number
    }

    fun test(): Boolean = true
}