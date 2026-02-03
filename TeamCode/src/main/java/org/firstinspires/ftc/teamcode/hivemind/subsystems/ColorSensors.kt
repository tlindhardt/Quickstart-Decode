package org.firstinspires.ftc.teamcode.hivemind.subsystems

import com.qualcomm.robotcore.hardware.NormalizedColorSensor
import dev.nextftc.core.commands.utility.InstantCommand
import dev.nextftc.core.subsystems.SubsystemGroup
import dev.nextftc.ftc.ActiveOpMode
import dev.nextftc.ftc.ActiveOpMode.hardwareMap

object ColorSensors : SubsystemGroup(
    Intake,
    Lights,
    Fries
) {
    lateinit var leftSensor: NormalizedColorSensor
    lateinit var centerSensor: NormalizedColorSensor
    lateinit var rightSensor: NormalizedColorSensor

    var isShooting: Boolean = false
    var isRunning: Boolean = false

    var colorOrder: ColorOrder = ColorOrder()

    var startRunning = InstantCommand { isRunning = true }

    var stopRunning = InstantCommand { isRunning = true }

    var startShooting = InstantCommand { isShooting = true }
    var endShooting = InstantCommand { isShooting = false }

    override fun initialize() {
        leftSensor = hardwareMap.get(NormalizedColorSensor::class.java, "left_color")
        leftSensor.gain = 4.toFloat()

        centerSensor = hardwareMap.get(NormalizedColorSensor::class.java, "center_color")
        centerSensor.gain = 4.toFloat()

        rightSensor = hardwareMap.get(NormalizedColorSensor::class.java, "right_color")
        rightSensor.gain = 4.toFloat()
    }

    override fun periodic() {
        if (isRunning) {
            var found1 = false
            var found2 = false
            var found3 = false
            val leftColors = leftSensor.normalizedColors
            if (leftColors.alpha > .08) {
                found1 = true
                if (!isShooting) {
                    Fries.closeLeft.schedule()
                }
                if (leftColors.green > leftColors.blue) {
                    Lights.leftGreen.schedule()
                    colorOrder.leftLane = Color.GREEN
                } else {
                    Lights.leftPurple.schedule()
                    colorOrder.leftLane = Color.PURPLE
                }
            } else {
                Lights.leftOff.schedule()
            }

            val centerColors = centerSensor.normalizedColors
            if (centerColors.alpha > .08) {
                found2 = true
                if (!isShooting) {
                    Fries.closeCenter.schedule()
                }
                if (centerColors.green > centerColors.blue) {
                    Lights.centerGreen.schedule()
                    colorOrder.centerLane = Color.GREEN
                } else {
                    Lights.centerPurple.schedule()
                    colorOrder.centerLane = Color.PURPLE
                }
            } else {
                Lights.centerOff.schedule()
            }

            val rightColors = rightSensor.normalizedColors
            if (rightColors.alpha > .08) {
                found3 = true
                if (!isShooting) {
                    Fries.closeRight.schedule()
                }
                if (rightColors.green > rightColors.blue) {
                    Lights.rightGreen.schedule()
                    colorOrder.leftLane = Color.GREEN
                } else {
                    Lights.rightPurple.schedule()
                    colorOrder.leftLane = Color.PURPLE
                }
            } else {
                Lights.rightOff.schedule()
            }

            ActiveOpMode.telemetry.addData("left", leftColors)
            ActiveOpMode.telemetry.addData("center", centerColors)
            ActiveOpMode.telemetry.addData("right", rightColors)

            if (found1 && found2 && found3) {
                Intake.off.schedule()
            }
        }
    }
}