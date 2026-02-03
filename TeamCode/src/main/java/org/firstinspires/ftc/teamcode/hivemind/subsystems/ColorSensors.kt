package org.firstinspires.ftc.teamcode.hivemind.subsystems

import com.qualcomm.robotcore.hardware.NormalizedColorSensor
import dev.nextftc.core.subsystems.Subsystem
import dev.nextftc.ftc.ActiveOpMode.hardwareMap

object ColorSensors : Subsystem {
    lateinit var leftSensor: NormalizedColorSensor
    lateinit var centerSensor: NormalizedColorSensor
    lateinit var rightSensor: NormalizedColorSensor

    var colorOrder: MutableList<Color> = mutableListOf(Color.EMPTY, Color.EMPTY, Color.EMPTY)

    override fun initialize() {
        leftSensor = hardwareMap.get(NormalizedColorSensor::class.java, "left_color")
        leftSensor.gain = 4.toFloat()

        centerSensor = hardwareMap.get(NormalizedColorSensor::class.java, "center_color")
        centerSensor.gain = 4.toFloat()

        rightSensor = hardwareMap.get(NormalizedColorSensor::class.java, "right_color")
        rightSensor.gain = 4.toFloat()
    }

    override fun periodic() {
        val leftColors = leftSensor.normalizedColors
        if (leftColors.alpha > .08) {
            if (leftColors.green > leftColors.blue) {
                colorOrder[0] = Color.GREEN
            } else {
                colorOrder[0] = Color.PURPLE
            }
        } else {
            colorOrder[0] = Color.EMPTY
        }

        val centerColors = centerSensor.normalizedColors
        if (centerColors.alpha > .08) {
            if (centerColors.green > centerColors.blue) {
                colorOrder[1] = Color.GREEN
            } else {
                colorOrder[1] = Color.PURPLE
            }
        } else {
            colorOrder[1] = Color.EMPTY
        }

        val rightColors = rightSensor.normalizedColors
        if (rightColors.alpha > .08) {
            if (rightColors.green > rightColors.blue) {
                colorOrder[2] = Color.GREEN
            } else {
                colorOrder[2] = Color.PURPLE
            }
        } else {
            colorOrder[2] = Color.EMPTY
        }
    }
}