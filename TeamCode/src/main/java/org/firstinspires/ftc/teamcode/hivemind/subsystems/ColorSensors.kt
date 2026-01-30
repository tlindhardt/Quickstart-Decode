package org.firstinspires.ftc.teamcode.hivemind.subsystems

import com.qualcomm.robotcore.hardware.NormalizedColorSensor
import dev.nextftc.core.subsystems.SubsystemGroup
import dev.nextftc.ftc.ActiveOpMode.hardwareMap
import dev.nextftc.ftc.ActiveOpMode.telemetry

object ColorSensors : SubsystemGroup(
    Intake,
    Lights,
    Fries
) {
    lateinit var leftSensor: NormalizedColorSensor
    lateinit var centerSensor: NormalizedColorSensor
    lateinit var rightSensor: NormalizedColorSensor

    override fun initialize() {
        leftSensor = hardwareMap.get(NormalizedColorSensor::class.java, "left_color")
        leftSensor.gain = 4.toFloat()

        centerSensor = hardwareMap.get(NormalizedColorSensor::class.java, "center_color")
        centerSensor.gain = 4.toFloat()

        rightSensor = hardwareMap.get(NormalizedColorSensor::class.java, "right_color")
        rightSensor.gain = 4.toFloat()
    }

    override fun periodic() {
        var found = false
        val leftColors = leftSensor.normalizedColors
        if (leftColors.alpha > .1) {
            found = true
            Fries.closeLeft.schedule()
//            if (leftColors.green > leftColors.blue) {
//                Lights.leftGreen.schedule()
//            } else {
//                Lights.leftPurple.schedule()
//            }
        } else {
//            Lights.leftOff.schedule()
        }

        val centerColors = centerSensor.normalizedColors
        if (centerColors.alpha > .1) {
            found = true
            Fries.closeCenter.schedule()
//            if (centerColors.green > centerColors.blue) {
//                Lights.centerGreen.schedule()
//            } else {
//                Lights.centerPurple.schedule()
//            }
        } else {
//            Lights.centerOff.schedule()
        }

        val rightColors = rightSensor.normalizedColors
        if (rightColors.alpha > .1) {
            found = true
            Fries.closeRight.schedule()
//            if (rightColors.green > rightColors.blue) {
//                Lights.rightGreen.schedule()
//            } else {
//                Lights.rightPurple.schedule()
//            }
        } else {
//            Lights.rightOff.schedule()
        }

        if (found) {
            Intake.off.schedule()
        }
    }
}