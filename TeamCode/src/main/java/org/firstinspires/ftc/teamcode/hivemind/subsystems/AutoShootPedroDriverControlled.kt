package org.firstinspires.ftc.teamcode.hivemind.subsystems

import com.pedropathing.geometry.Pose
import dev.nextftc.extensions.pedro.PedroComponent.Companion.follower
import dev.nextftc.ftc.ActiveOpMode
import dev.nextftc.hardware.driving.DriverControlledCommand
import org.firstinspires.ftc.teamcode.hivemind.poses.BlueBottomPoses
import org.firstinspires.ftc.teamcode.hivemind.poses.RedBottomPoses
import java.util.function.Supplier
import kotlin.math.atan2

class AutoShootPedroDriverControlled @JvmOverloads constructor(
    drivePower: Supplier<Double>,
    strafePower: Supplier<Double>,
    turnPower: Supplier<Double>,
    private val autoTracking: Supplier<Boolean>,
    private val robotCentric: Boolean,
    val isBlue: Boolean
) : DriverControlledCommand(drivePower, strafePower, turnPower) {

    val autoRotationMultiplier = 0.5

    var x = 144.0
    var y = 134.0
//
//    enum class Depot(val x: Double, val y: Double) {
//        RED_TOP(150.0, 144.0),
//        RED(144.0, 140.0),
//        BLUE_TOP(-8.0, 144.0),
//        BLUE(0.0, 140.0)
//    }

    override fun start() {
        if (isBlue) {
            follower.setStartingPose(BlueBottomPoses().getEndPose())
        } else {
            follower.setStartingPose(RedBottomPoses().getEndPose())
        }
        follower.startTeleopDrive()
    }

    override fun calculateAndSetPowers(powers: DoubleArray) {
        var (drive, strafe, turn) = powers

//        if (autoTracking.get()) {
////            val depot = getDepot(follower.pose, isBlue)
//            if (isBlue) {
//                val blueHeading = calculateHeading(follower.pose)
//                turn = (AngleUnit.normalizeRadians(blueHeading - follower.heading) * autoRotationMultiplier).coerceIn(
//                    -1.0,
//                    1.0
//                )
//            } else {
//                val redHeading = calculateHeading(follower.pose)
//                turn = (AngleUnit.normalizeRadians(redHeading - follower.heading) * autoRotationMultiplier).coerceIn(
//                    -1.0,
//                    1.0
//                )
//            }
//        }

        // Because pedro is oriented in the red direction we need to flip it for blue for our auto movements to work
        if (isBlue) {
            drive *= -1
            strafe *= -1
        }

        ActiveOpMode.telemetry.addData("x", x)
        ActiveOpMode.telemetry.addData("y", y)

        follower.setTeleOpDrive(drive, strafe, turn, robotCentric)
    }
//
//    private fun getDepot(pose: Pose, isBlue: Boolean): Depot {
//        if (isBlue) {
//            if (pose.y > 110) {
//                return Depot.BLUE_TOP
//            } else return Depot.BLUE
//        } else {
//            if (pose.y > 110) {
//                return Depot.RED_TOP
//            } else return Depot.RED
//        }
//
//    }

    override fun stop(interrupted: Boolean) {
        if (interrupted) follower.breakFollowing()
    }

    private fun calculateHeading(pose: Pose): Double {
        val dx = x - pose.x
        val dy = y - pose.y
        return atan2(dy, dx)
    }
}