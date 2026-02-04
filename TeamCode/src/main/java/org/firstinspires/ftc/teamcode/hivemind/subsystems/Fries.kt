package org.firstinspires.ftc.teamcode.hivemind.subsystems

import dev.nextftc.core.commands.Command
import dev.nextftc.core.commands.delays.Delay
import dev.nextftc.core.commands.groups.SequentialGroup
import dev.nextftc.core.commands.utility.InstantCommand
import dev.nextftc.core.subsystems.SubsystemGroup
import dev.nextftc.hardware.impl.ServoEx
import dev.nextftc.hardware.positionable.SetPosition
import dev.nextftc.hardware.positionable.SetPositions
import kotlin.time.Duration

object Fries : SubsystemGroup(ColorSensors, Camera) {

    enum class FryConfig(val fire: Double, val close: Double, val intake: Double) {
        LEFT(0.27, 0.15, 0.05),
        CENTER(0.74, 0.86, 0.96),
        RIGHT(0.77, 0.89, 0.99),
    }

    private val leftFry = ServoEx("left_fry")
    private val centerFry = ServoEx("center_fry")
    private val rightFry = ServoEx("right_fry")
    var isRunning: Boolean = false

    var isShooting: Boolean = false

    var startRunning = InstantCommand { isRunning = true }

    var stopRunning = InstantCommand { isRunning = true }

    var startShooting = InstantCommand { isShooting = true }
    var endShooting = InstantCommand {
        isShooting = false
        ColorSensors.leftLock = false
        ColorSensors.centerLock = false
        ColorSensors.rightLock = false
    }

    val fireLeft = SetPosition(leftFry, FryConfig.LEFT.fire).requires(this)
    val intakeLeft = SetPosition(leftFry, FryConfig.LEFT.intake).requires(this)
    val fireCenter = SetPosition(centerFry, FryConfig.CENTER.fire).requires(this)
    val intakeCenter = SetPosition(centerFry, FryConfig.CENTER.intake).requires(this)
    val fireRight = SetPosition(rightFry, FryConfig.RIGHT.fire).requires(this)
    val intakeRight = SetPosition(rightFry, FryConfig.RIGHT.intake).requires(this)
    val intakeAll = SetPositions(
        leftFry to FryConfig.LEFT.intake,
        centerFry to FryConfig.CENTER.intake,
        rightFry to FryConfig.RIGHT.intake
    ).requires(this)

    val fireAllSorted: (Duration) -> Command = { it ->
        isShooting = true

        val commands = mutableListOf<Command>()
        val positions = listOf(fireLeft, fireCenter, fireRight)
        val usedIndices = mutableSetOf<Int>()

        for (expectedColor in Camera.obeliskOrder) {
            // Try to grab the desired color
            var itemIndex = ColorSensors.colorOrder.indices.firstOrNull {
                it !in usedIndices && ColorSensors.colorOrder[it] == expectedColor
            }

            // If we cant find that color, fall back to the other color
            if (itemIndex == null) {
                itemIndex = ColorSensors.colorOrder.indices.firstOrNull {
                    it !in usedIndices
                            && ColorSensors.colorOrder[it] != expectedColor
                            && ColorSensors.colorOrder[it] != Color.EMPTY
                }
            }

            // If we find it, add the command to the list
            if (itemIndex != null) {
                commands.add(positions[itemIndex])
                usedIndices.add(itemIndex)
            }
        }

        SequentialGroup(
            *commands.flatMap { cmd ->
                listOf(cmd, Delay(it))
            }.toTypedArray(),
            intakeAll,
            endShooting
        )
    }

    override fun initialize() {
        leftFry.position = FryConfig.LEFT.intake
        centerFry.position = FryConfig.CENTER.intake
        rightFry.position = FryConfig.RIGHT.intake
    }

    override fun periodic() {
        if (isRunning) {
            if (!isShooting) {
                if (ColorSensors.colorOrder[0] != Color.EMPTY) {
                    ColorSensors.leftLock = true
                    leftFry.position = FryConfig.LEFT.close
                }
                if (ColorSensors.colorOrder[1] != Color.EMPTY) {
                    ColorSensors.centerLock = true
                    centerFry.position = FryConfig.CENTER.close
                }
                if (ColorSensors.colorOrder[2] != Color.EMPTY) {
                    ColorSensors.rightLock = true
                    rightFry.position = FryConfig.RIGHT.close
                }
            }
        }
    }
}