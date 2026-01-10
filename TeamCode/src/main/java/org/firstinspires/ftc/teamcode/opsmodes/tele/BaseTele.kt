package org.firstinspires.ftc.teamcode.opsmodes.tele

import com.pedropathing.follower.Follower
import com.pedropathing.geometry.Pose
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver
import com.qualcomm.hardware.limelightvision.Limelight3A
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.*
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.teamcode.opsmodes.pedroPathing.Constants
import org.firstinspires.ftc.teamcode.opsmodes.shared.Utils

open class BaseTele(val botCentric: Boolean = true, val isBlue: Boolean) : LinearOpMode() {

    var isShooting: Boolean = false
    var rpm: Int = 500
    lateinit var follower: Follower
    lateinit var shootTimer: ElapsedTime
    lateinit var adjustTimer: ElapsedTime
    lateinit var feeder: Servo
    lateinit var shooter: DcMotorEx
    lateinit var voltageSensor: VoltageSensor
    lateinit var pinpoint: GoBildaPinpointDriver
    lateinit var limelight: Limelight3A

    override fun runOpMode() {
        limelight = hardwareMap.get(Limelight3A::class.java, "limelight")
        feeder = hardwareMap.get(Servo::class.java, "feeder");
        shooter = hardwareMap.get(DcMotorEx::class.java, "shooter");
        voltageSensor = hardwareMap.get(VoltageSensor::class.java, "Control Hub")
        pinpoint = hardwareMap.get(GoBildaPinpointDriver::class.java, "pinpoint")
        shooter.direction = DcMotorSimple.Direction.FORWARD
        shooter.mode = DcMotor.RunMode.RUN_USING_ENCODER
        shooter.setPIDFCoefficients(
            DcMotor.RunMode.RUN_USING_ENCODER,
            PIDFCoefficients(200.0, 0.0, 20.0, 14.5 * 12 / voltageSensor.voltage)
        )
        follower = Constants.createFollower(hardwareMap)
        follower.setStartingPose(Pose())
        follower.update()
        shootTimer = ElapsedTime()
        adjustTimer = ElapsedTime()
        isShooting = false
        limelight.pipelineSwitch(0)
        limelight.start()

        waitForStart()
        feeder.position = 1.0
        follower.startTeleopDrive();

        while (opModeIsActive()) {
            limelight.updateRobotOrientation(pinpoint.getHeading(AngleUnit.DEGREES))
            val lLResult = limelight.latestResult

            if (lLResult != null && lLResult.isValid && lLResult.fiducialResults.map { it.fiducialId}.contains(if (isBlue) 21 else 24)) {
                val botposeMt2 = lLResult.botpose_MT2
                val distance = Utils.getDistanceFromTags(lLResult.ta)
                telemetry.addData("Calculated Distance", distance)
                rpm = Utils.getRpmFromDistance(distance).toInt()
                telemetry.addData("Calculated Velocity", rpm)
                telemetry.addData("Target X", lLResult.tx)
                telemetry.addData("Target Y", lLResult.ty)
                telemetry.addData("Target Area", lLResult.ta)
                telemetry.addData("Botpose", botposeMt2.toString())
            }

            follower.update()
            follower.setTeleOpDrive(
                -gamepad1.left_stick_y.toDouble(),
                -gamepad1.left_stick_x.toDouble(),
                -gamepad1.right_stick_x.toDouble(),
                botCentric
            )
//            adjustShooting()
            doShooting()
            idle()
            telemetry.addData("Desired Velocity", rpm)
            telemetry.addData("Shooter Velocity", shooter.velocity)
            telemetry.update()
        }
    }

    private fun adjustShooting() {
        if (adjustTimer.time() > .1) {
            if (gamepad2.dpad_up) {
                rpm += 50
                adjustTimer.reset()
            } else if (gamepad2.dpad_down) {
                rpm -= 50
                adjustTimer.reset()
            }

            if (rpm > 2300) {
                rpm = 500
            }

            if (rpm < 500) {
                rpm = 2300
            }
        }
    }

    private fun doShooting() {
        if (gamepad2.right_trigger > 0) {
            shooter.velocity = rpm.toDouble()
        } else {
            shooter.velocity = 0.0
        }
        if (!isShooting && gamepad2.a) {
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