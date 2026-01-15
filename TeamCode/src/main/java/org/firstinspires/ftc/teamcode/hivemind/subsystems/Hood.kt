package org.firstinspires.ftc.teamcode.hivemind.subsystems

import dev.nextftc.core.subsystems.Subsystem
import dev.nextftc.hardware.impl.ServoEx
import dev.nextftc.hardware.positionable.SetPosition

object Hood : Subsystem {
    private const val OPEN = 0.75
    private const val CLOSE = 0.75
    private val hood = ServoEx("hood")

    val open = SetPosition(hood, OPEN).requires(this)
    val close = SetPosition(hood, CLOSE).requires(this)

    override fun initialize() {
        hood.position = CLOSE
    }
}