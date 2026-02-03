package org.firstinspires.ftc.teamcode.hivemind.subsystems

import dev.nextftc.core.subsystems.SubsystemGroup
import dev.nextftc.hardware.impl.ServoEx

object Lights : SubsystemGroup(ColorSensors) {

    private val leftLight = ServoEx("left_light")
    private val centerLight = ServoEx("center_light")
    private val rightLight = ServoEx("right_light")

    override fun initialize() {
        leftLight.position = Color.EMPTY.value
        centerLight.position = Color.EMPTY.value
        rightLight.position = Color.EMPTY.value
    }

    override fun periodic() {
        val colorOrder = ColorSensors.colorOrder
        leftLight.position = colorOrder[0].value
        centerLight.position = colorOrder[1].value
        rightLight.position = colorOrder[2].value
    }

}