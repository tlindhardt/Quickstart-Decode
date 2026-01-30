package org.firstinspires.ftc.teamcode.hivemind.subsystems

import dev.nextftc.core.subsystems.Subsystem
import dev.nextftc.hardware.impl.ServoEx
import dev.nextftc.hardware.positionable.SetPosition

object Lights : Subsystem {

    enum class Color(val value: Double) {
        GREEN(0.5),
        PURPLE(0.722),
        OFF(0.0)
    }

    private val leftLight = ServoEx("left_light")
    private val centerLight = ServoEx("center_light")
    private val rightLight = ServoEx("right_light")

    val leftOff = SetPosition(leftLight, Color.OFF.value).requires(this)
    val leftGreen = SetPosition(leftLight, Color.GREEN.value).requires(this)
    val leftPurple = SetPosition(leftLight, Color.PURPLE.value).requires(this)

    val centerOff = SetPosition(centerLight, Color.OFF.value).requires(this)
    val centerGreen = SetPosition(centerLight, Color.GREEN.value).requires(this)
    val centerPurple = SetPosition(centerLight, Color.PURPLE.value).requires(this)

    val rightOff = SetPosition(rightLight, Color.OFF.value).requires(this)
    val rightGreen = SetPosition(rightLight, Color.GREEN.value).requires(this)
    val rightPurple = SetPosition(rightLight, Color.PURPLE.value).requires(this)

    override fun initialize() {
        leftLight.position = Color.OFF.value
        centerLight.position = Color.OFF.value
        rightLight.position = Color.OFF.value
    }

}