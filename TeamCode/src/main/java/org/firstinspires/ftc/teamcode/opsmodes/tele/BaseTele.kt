package org.firstinspires.ftc.teamcode.opsmodes.tele

import com.pedropathing.follower.Follower
import com.pedropathing.geometry.Pose
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.*
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.teamcode.opsmodes.pedroPathing.Constants
import kotlin.math.atan2

open class BaseTele(val botCentric: Boolean = true) : LinearOpMode() {

    // Measured using Fusion, where 0,0 is red loading zone
    enum class Depot(val x: Double, val y: Double, val heading: Double) {
        RED(144.0, 144.0, 34.0),
        BLUE(0.0, 144.0, 146.0)
    }

    var isShooting: Boolean = false
    lateinit var follower: Follower
    lateinit var shootTimer: ElapsedTime
    lateinit var feeder: Servo
    lateinit var shooter: DcMotorEx
    lateinit var voltageSensor: VoltageSensor

    override fun runOpMode() {
        feeder = hardwareMap.get(Servo::class.java, "feeder");
        shooter = hardwareMap.get(DcMotorEx::class.java, "shooter");
        voltageSensor = hardwareMap.get(VoltageSensor::class.java, "Control Hub")
        shooter.direction = DcMotorSimple.Direction.FORWARD
        shooter.mode = DcMotor.RunMode.RUN_USING_ENCODER
        shooter.setPIDFCoefficients(
            DcMotor.RunMode.RUN_USING_ENCODER,
            PIDFCoefficients(200.0, 0.0, 20.0, 14.5 * 12 / voltageSensor.voltage)
        )
        follower = Constants.createFollower(hardwareMap)
        follower.setStartingPose(Pose(7.0, 7.0, 0.0))
        follower.update()
        shootTimer = ElapsedTime()
        isShooting = false

        waitForStart()
        feeder.position = 1.0
        follower.startTeleopDrive();
        while (opModeIsActive()) {
            var turn = -gamepad1.right_stick_x.toDouble()
            if (gamepad1.cross) {
                val blueHeading = calculateHeading(follower.pose, Depot.BLUE)
                turn = AngleUnit.normalizeRadians(blueHeading - follower.heading).coerceIn(-1.0, 1.0)
            } else if (gamepad1.square) {
                val redHeading = calculateHeading(follower.pose, Depot.RED)
                turn = AngleUnit.normalizeRadians(redHeading - follower.heading).coerceIn(-1.0, 1.0)
            }

            follower.update()
            follower.setTeleOpDrive(
                -gamepad1.left_stick_y.toDouble(),
                -gamepad1.left_stick_x.toDouble(),
                turn,
                botCentric
            )
            doShooting()
            idle()
            telemetry.addData("Shoot Power", shooter.power)
            telemetry.update()
        }
    }

    private fun calculateHeading(pose: Pose, depot: Depot): Double {
        telemetry.addData("currentX", pose.x)
        telemetry.addData("currentY", pose.y)
        val dx = depot.x - pose.x
        val dy = depot.y - pose.y
        return atan2(dy, dx)
    }

    private fun doShooting() {

        if (gamepad2.right_trigger.toDouble() > 0.3) {
            shooter.velocity = 1350.0
        } else {
            shooter.velocity = 0.0
        }
        if (!isShooting && gamepad2.a && shooter.power >= 0.30) {
            shootTimer.reset()
            isShooting = true
        }
        if (isShooting) {
            if (shootTimer.time() < 0.2) {
                feeder.position = 0.5
            }
            if (shootTimer.time() >= 0.2) {
                feeder.position = 1.0
                isShooting = false
            }
        }
    }

}