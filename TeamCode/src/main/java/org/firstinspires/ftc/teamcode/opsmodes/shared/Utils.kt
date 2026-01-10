package org.firstinspires.ftc.teamcode.opsmodes.shared

import kotlin.math.pow

class Utils {
    companion object {

        fun getDistanceFromTags(ta: Double): Double {
            return 72.42571 * ta.pow(-0.5444446)
        }

        fun getRpmFromDistance(distance: Double): Double {
            return 951.834 + 10.48168 * distance - 0.03041669 * distance.pow(2)
        }
    }
}