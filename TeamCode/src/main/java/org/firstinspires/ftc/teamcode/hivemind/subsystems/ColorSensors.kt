package org.firstinspires.ftc.teamcode.hivemind.subsystems

import com.qualcomm.robotcore.hardware.NormalizedColorSensor
import dev.nextftc.core.commands.utility.InstantCommand
import dev.nextftc.core.subsystems.SubsystemGroup
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
        var found1 = false
        var found2 = false
        var found3 = false
        val leftColors = leftSensor.normalizedColors
        if (leftColors.alpha > .1) {
            found1 = true
            if (!isShooting) {
                Fries.closeLeft.schedule()
            }
            if (leftColors.green > leftColors.blue) {
                Lights.leftGreen.schedule()
            } else {
                Lights.leftPurple.schedule()
            }
        } else {
            Lights.leftOff.schedule()
        }

        val centerColors = centerSensor.normalizedColors
        if (centerColors.alpha > .1) {
            found2 = true
            if (!isShooting) {
                Fries.closeCenter.schedule()
            }
            if (centerColors.green > centerColors.blue) {
                Lights.centerGreen.schedule()
            } else {
                Lights.centerPurple.schedule()
            }
        } else {
            Lights.centerOff.schedule()
        }

        val rightColors = rightSensor.normalizedColors
        if (rightColors.alpha > .1) {
            found3 = true
            if (!isShooting) {
                Fries.closeRight.schedule()
            }
            if (rightColors.green > rightColors.blue) {
                Lights.rightGreen.schedule()
            } else {
                Lights.rightPurple.schedule()
            }
        } else {
            Lights.rightOff.schedule()
        }

        if (found1 && found2 && found3) {
            Intake.off.schedule()
        }
    }
}