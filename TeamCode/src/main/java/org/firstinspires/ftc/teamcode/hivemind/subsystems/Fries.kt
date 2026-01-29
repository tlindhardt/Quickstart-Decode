package org.firstinspires.ftc.teamcode.hivemind.subsystems

import dev.nextftc.core.subsystems.Subsystem
import dev.nextftc.hardware.impl.ServoEx
import dev.nextftc.hardware.positionable.SetPosition
import dev.nextftc.hardware.positionable.SetPositions

object Fries : Subsystem {

    enum class FryConfig(val fire: Double, val intake: Double, val hold: Double) {
        LEFT(0.22, 0.035, 0.15),
        CENTER(0.78, 0.97, 0.84),
        RIGHT(0.80, 1.0, 0.84),
    }

    private val leftFry = ServoEx("left_fry")
    private val centerFry = ServoEx("center_fry")
    private val rightFry = ServoEx("right_fry")


    val fireLeft = SetPosition(leftFry, FryConfig.LEFT.fire).requires(this)
    val intakeLeft = SetPosition(leftFry, FryConfig.LEFT.intake).requires(this)
    val fireCenter = SetPosition(centerFry, FryConfig.CENTER.fire).requires(this)
    val intakeCenter = SetPosition(centerFry, FryConfig.CENTER.intake).requires(this)
    val fireRight = SetPosition(rightFry, FryConfig.RIGHT.fire).requires(this)
    val intakeRight = SetPosition(rightFry, FryConfig.RIGHT.intake).requires(this)
    val fireAll = SetPositions(
        leftFry to FryConfig.LEFT.fire,
        centerFry to FryConfig.CENTER.fire,
        rightFry to FryConfig.RIGHT.fire
    ).requires(this)

    val intakeAll = SetPositions(
        leftFry to FryConfig.LEFT.intake,
        centerFry to FryConfig.CENTER.intake,
        rightFry to FryConfig.RIGHT.intake
    ).requires(this)

    val holdAll = SetPositions(
        leftFry to FryConfig.LEFT.hold,
        centerFry to FryConfig.CENTER.hold,
        rightFry to FryConfig.RIGHT.hold
    ).requires(this)

    override fun initialize() {
        leftFry.position = FryConfig.LEFT.intake
        centerFry.position = FryConfig.CENTER.intake
        rightFry.position = FryConfig.RIGHT.intake
    }
}