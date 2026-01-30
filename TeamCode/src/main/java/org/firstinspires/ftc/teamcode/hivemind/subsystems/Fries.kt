package org.firstinspires.ftc.teamcode.hivemind.subsystems

import dev.nextftc.core.subsystems.Subsystem
import dev.nextftc.hardware.impl.ServoEx
import dev.nextftc.hardware.positionable.SetPosition
import dev.nextftc.hardware.positionable.SetPositions

object Fries : Subsystem {

    enum class FryConfig(val fire: Double, val close: Double, val intake: Double) {
        LEFT(0.27, 0.15, 0.05),
        CENTER(0.74, 0.86, 0.96),
        RIGHT(0.77, 0.89, 0.99),
    }

    private val leftFry = ServoEx("left_fry")
    private val centerFry = ServoEx("center_fry")
    private val rightFry = ServoEx("right_fry")


    val fireLeft = SetPosition(leftFry, FryConfig.LEFT.fire).requires(this)
    val closeLeft = SetPosition(leftFry, FryConfig.LEFT.close).requires(this)
    val intakeLeft = SetPosition(leftFry, FryConfig.LEFT.intake).requires(this)
    val fireCenter = SetPosition(centerFry, FryConfig.CENTER.fire).requires(this)
    val closeCenter = SetPosition(centerFry, FryConfig.CENTER.close).requires(this)
    val intakeCenter = SetPosition(centerFry, FryConfig.CENTER.intake).requires(this)
    val fireRight = SetPosition(rightFry, FryConfig.RIGHT.fire).requires(this)
    val closeRight = SetPosition(rightFry, FryConfig.RIGHT.close).requires(this)
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

    override fun initialize() {
        leftFry.position = FryConfig.LEFT.intake
        centerFry.position = FryConfig.CENTER.intake
        rightFry.position = FryConfig.RIGHT.intake
    }
}