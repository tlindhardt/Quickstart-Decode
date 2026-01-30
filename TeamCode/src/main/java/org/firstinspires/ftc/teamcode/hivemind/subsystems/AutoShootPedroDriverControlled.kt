package org.firstinspires.ftc.teamcode.hivemind.subsystems

import com.pedropathing.geometry.Pose
import dev.nextftc.extensions.pedro.PedroComponent.Companion.follower
import dev.nextftc.hardware.driving.DriverControlledCommand
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import java.util.function.Supplier
import kotlin.math.atan2

class AutoShootPedroDriverControlled @JvmOverloads constructor(
    drivePower: Supplier<Double>,
    strafePower: Supplier<Double>,
    turnPower: Supplier<Double>,
    private val robotCentric: Boolean,
    val isBlue: Boolean
) : DriverControlledCommand(drivePower, strafePower, turnPower) {

    enum class Depot(val x: Double, val y: Double, val heading: Double) {
        RED(144.0, 138.0, 34.0),
        BLUE(0.0, 138.0, 146.0)
    }

    var isAutoTracking: Boolean = false

    override fun start() {
        follower.startTeleopDrive()
    }

    override fun calculateAndSetPowers(powers: DoubleArray) {
        var (drive, strafe, turn) = powers

        if (isAutoTracking) {
            if (isBlue) {
                val blueHeading = calculateHeading(follower.pose, Depot.BLUE)
                turn = AngleUnit.normalizeRadians(blueHeading - follower.heading).coerceIn(-1.0, 1.0)
            } else {
                val redHeading = calculateHeading(follower.pose, Depot.RED)
                turn = AngleUnit.normalizeRadians(redHeading - follower.heading).coerceIn(-1.0, 1.0)
            }
        }

        follower.setTeleOpDrive(drive, strafe, turn, robotCentric)
    }

    override fun stop(interrupted: Boolean) {
        if (interrupted) follower.breakFollowing()
    }

    private fun calculateHeading(pose: Pose, depot: Depot): Double {
        val dx = depot.x - pose.x
        val dy = depot.y - pose.y
        return atan2(dy, dx)
    }
}