package org.firstinspires.ftc.teamcode.hivemind.subsystems

import dev.nextftc.core.subsystems.SubsystemGroup
import dev.nextftc.hardware.impl.MotorEx
import dev.nextftc.hardware.powerable.SetPower

object Intake : SubsystemGroup(ColorSensors) {
    private const val OFF = 0.0
    private const val FORWARD = -1.0
    private const val REVERSE = 1.0
    private val motor = MotorEx("intake")
    val off = SetPower(motor, OFF).requires(this)
    val forward = SetPower(motor, FORWARD).requires(this)
    val reverse = SetPower(motor, REVERSE).requires(this)

    override fun initialize() {
        motor.power = OFF
    }

    override fun periodic() {
//        if (ColorSensors.colorOrder.filter { it != Color.EMPTY }.size == 3) {
//            motor.power = OFF
//        }
    }
}
