package org.firstinspires.ftc.teamcode.hivemind.subsystems

import com.pedropathing.geometry.Pose
import dev.nextftc.extensions.pedro.PedroComponent.Companion.follower
import dev.nextftc.hardware.driving.DriverControlledCommand
import dev.nextftc.control.KineticState
import dev.nextftc.control.feedback.AngleType
import dev.nextftc.control.feedback.AngularFeedback
import dev.nextftc.control.feedback.FeedbackType
import dev.nextftc.control.feedback.PIDCoefficients
import dev.nextftc.control.feedback.PIDElement
import org.firstinspires.ftc.teamcode.hivemind.poses.BlueBottomPoses
import org.firstinspires.ftc.teamcode.hivemind.poses.RedBottomPoses
import java.util.function.Supplier
import kotlin.math.abs
import kotlin.math.atan2

class AutoShootPedroDriverControlled @JvmOverloads constructor(
    drivePower: Supplier<Double>,
    strafePower: Supplier<Double>,
    turnPower: Supplier<Double>,
    private val autoTracking: Supplier<Boolean>,
    private val resetPinpoint: Supplier<Boolean>,
    private val robotCentric: Boolean,
    val isBlue: Boolean,
    val isCalibration: Boolean
) : DriverControlledCommand(drivePower, strafePower, turnPower) {

    val headingCoefficients = PIDCoefficients(kP = 1.0, kI = 0.0, kD = 0.155)
    val autoRotationDeadband = Math.toRadians(3.0)

    private val headingController = AngularFeedback(
        AngleType.RADIANS,
        PIDElement(FeedbackType.POSITION, headingCoefficients)
    )

    override fun start() {
        if (isCalibration) {
            if (isBlue) {
                follower.setStartingPose(BlueBottomPoses().getStartPose())
            } else {
                follower.setStartingPose(RedBottomPoses().getStartPose())
            }
        } else {
            if (isBlue) {
                follower.setStartingPose(BlueBottomPoses().getEndPose())
            } else {
                follower.setStartingPose(RedBottomPoses().getEndPose())
            }
        }
        follower.startTeleopDrive()
    }

    override fun calculateAndSetPowers(powers: DoubleArray) {
        var (drive, strafe, turn) = powers
        if (resetPinpoint.get()) {
            var heading = RedBottomPoses().getEndPose().heading
            if (isBlue) {
                heading = BlueBottomPoses().getEndPose().heading
            }
            follower.pose = Pose(follower.pose.x, follower.pose.y, heading)
        }

//        if (autoTracking.get()) {
//            val targetX = if (isBlue) 0.0 else 144.0
//            val error = AngleType.RADIANS.normalize(calculateHeading(follower.pose, targetX) - follower.heading)
//            turn = if (abs(error) < autoRotationDeadband) 0.0
//                   else headingController.calculate(KineticState(position = error, velocity = -follower.angularVelocity)).coerceIn(-1.0, 1.0)
//        } else {
        headingController.reset()
        turn *= 0.55
//        }

        // Because pedro is oriented in the red direction we need to flip it for blue for our auto movements to work
        if (isBlue) {
            drive *= -1
            strafe *= -1
        }

//        ActiveOpMode.telemetry.addData("x", x)
//        ActiveOpMode.telemetry.addData("y", y)

        follower.setTeleOpDrive(drive, strafe, turn, robotCentric)
    }

    override fun stop(interrupted: Boolean) {
        if (interrupted) follower.breakFollowing()
    }

    private fun calculateHeading(pose: Pose, targetX: Double): Double {
        val dx = targetX - pose.x
        val dy = 134.0 - pose.y
        return atan2(dy, dx)
    }
}