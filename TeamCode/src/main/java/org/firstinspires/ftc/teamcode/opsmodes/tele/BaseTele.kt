package org.firstinspires.ftc.teamcode.opsmodes.tele

import com.pedropathing.follower.Follower
import com.pedropathing.geometry.Pose
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.PIDFCoefficients
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.hardware.VoltageSensor
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.opsmodes.pedroPathing.Constants
import org.firstinspires.ftc.teamcode.opsmodes.shared.Utils

open class BaseTele(val botCentric: Boolean = true) : LinearOpMode() {

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
        shooter.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER,
            PIDFCoefficients(200.0, 0.0, 20.0, 14.5 * 12 / voltageSensor.voltage)
        )
        follower = Constants.createFollower(hardwareMap)
        follower.setStartingPose(Pose())
        follower.update()
        shootTimer = ElapsedTime()
        isShooting = false

        waitForStart()
        feeder.position = 1.0
        follower.startTeleopDrive();

        while (opModeIsActive()) {
            follower.update()
            follower.setTeleOpDrive(
                -gamepad1.left_stick_y.toDouble(),
                -gamepad1.left_stick_x.toDouble(),
                -gamepad1.right_stick_x.toDouble(),
                botCentric
            )
            doShooting()
            idle()
            telemetry.addData("Shoot Power", shooter.power)
            telemetry.update()
        }
    }

    private fun doShooting() {
        var shooterPower = gamepad2.right_trigger.toDouble()
        val desiredPower = Utils.getShootingPower(voltageSensor)
        if (shooterPower > desiredPower) {
            shooterPower = desiredPower
        }
        shooter.power = shooterPower
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