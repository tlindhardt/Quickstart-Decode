package org.firstinspires.ftc.teamcode.hivemind.subsystems

import dev.nextftc.core.commands.Command
import dev.nextftc.core.commands.delays.Delay
import dev.nextftc.core.commands.groups.SequentialGroup
import dev.nextftc.core.commands.utility.InstantCommand
import dev.nextftc.core.commands.utility.LambdaCommand
import dev.nextftc.core.subsystems.SubsystemGroup
import dev.nextftc.hardware.impl.ServoEx
import dev.nextftc.hardware.positionable.SetPosition
import dev.nextftc.hardware.positionable.SetPositions
import kotlin.time.Duration.Companion.seconds

object Fries : SubsystemGroup(ColorSensors, Camera) {

    enum class FryConfig(val fire: Double, val close: Double, val intake: Double) {
        LEFT(0.31, 0.17, 0.06),
        CENTER(0.69, 0.85, 0.96),
        RIGHT(0.73, 0.87, 0.98),
    }

    private val leftFry = ServoEx("left_fry")
    private val centerFry = ServoEx("center_fry")
    private val rightFry = ServoEx("right_fry")
    var isRunning: Boolean = false

    var hasStarted: Boolean = false

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

    val fireAllSorted: (Boolean) -> Command = { it ->
        LambdaCommand()
            .setStart {
                isShooting = true
                hasStarted = false
            }
            .setUpdate {
                if (!hasStarted) {
                    hasStarted = true
                    val commands = mutableListOf<Command>()
                    val positions = listOf(fireLeft, fireCenter, fireRight)
                    val delays = mutableListOf(0.2.seconds, 0.2.seconds, 0.2.seconds)
                    val usedIndices = mutableSetOf<Int>()

                    for ((obeliskIndex, expectedColor) in Camera.obeliskOrder.withIndex()) {
                        // Try to grab the desired color
                        var itemIndex = ColorSensors.colorOrder.indices.firstOrNull {
                            it !in usedIndices && ColorSensors.colorOrder[it] == expectedColor
                        }

                        if (it && itemIndex != null) {
                            // Set 1s delay if current is Green OR next obelisk item is Green
                            if (itemIndex < 3) {  // Changed from < 2 to < 3 (i.e., all positions)
                                if (expectedColor == Color.GREEN ||
                                    (obeliskIndex + 1 < Camera.obeliskOrder.size &&
                                            Camera.obeliskOrder[obeliskIndex + 1] == Color.GREEN)) {
                                    delays[itemIndex] = 0.8.seconds
                                }
                            }
                        }

                        // If we cant find that color, fall back empty
                        if (itemIndex == null) {
                            itemIndex = ColorSensors.colorOrder.indices.firstOrNull {
                                it !in usedIndices
                                        && ColorSensors.colorOrder[it] == Color.EMPTY
                            }
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
                            commands.add(Delay(delays[itemIndex]))
                            usedIndices.add(itemIndex)
                            // Add in an extra fire for center in case we stick
                            if (itemIndex == 1) {
                                commands.add(intakeCenter)
                                commands.add(Delay(0.2.seconds))
                                commands.add(fireCenter)
                                commands.add(Delay(0.1.seconds))
                                usedIndices.add(itemIndex)
                            }
                        }
                    }

                    SequentialGroup(
                        *commands.toTypedArray(),
                        intakeAll,
                        endShooting,
                        InstantCommand {
                            hasStarted = false
                        }
                    ).schedule()
                }
            }
            .setIsDone { !isShooting }
            .requires(this)

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