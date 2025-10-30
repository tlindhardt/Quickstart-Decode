package org.firstinspires.ftc.teamcode.opsmodes.tele

import com.pedropathing.follower.Follower
import com.pedropathing.geometry.Pose
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.hardware.VoltageSensor
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.opsmodes.pedroPathing.Constants
import org.firstinspires.ftc.teamcode.opsmodes.shared.Utils
import kotlin.Boolean


open class TeleOpJace(var botCentric: Boolean = true) : LinearOpMode() {

    var isShooting: Boolean = false
    var drivespeed:  Double = 1.0
    lateinit var follower: Follower
    lateinit var shootTimer: ElapsedTime
    lateinit var feeder: Servo
    lateinit var shooter: DcMotor
    //
    lateinit var rightfrontmoter: DcMotor
    lateinit var leftfrontmoter: DcMotor
    lateinit var rightbackmoter: DcMotor
    lateinit var leftbackmoter: DcMotor
    //
    lateinit var voltageSensor: VoltageSensor


    override fun runOpMode() {
        feeder = hardwareMap.get(Servo::class.java, "feeder")
        shooter = hardwareMap.get(DcMotor::class.java, "shooter")
        voltageSensor = hardwareMap.get(VoltageSensor::class.java, "Control Hub")
        follower = Constants.createFollower(hardwareMap)
        follower.setStartingPose(Pose())
        follower.update()
        shootTimer = ElapsedTime()
        isShooting = false


        waitForStart()

        feeder.position = 1.0
        follower.startTeleopDrive()


        while (opModeIsActive()) {
            follower.update()
            follower.maxPowerScaling = drivespeed.toDouble()
            follower.setTeleOpDrive(
                -gamepad1.left_stick_y.toDouble(),
                -gamepad1.left_stick_x.toDouble(),
                -gamepad1.right_stick_x.toDouble(),
                botCentric

            )
            doShooting()
            drivepower()
            teleOpMode()
            //
           // doShooting2()
            //
            idle()
        }
    }


    private fun doShooting() {
        var shooterPower = gamepad1.right_trigger.toDouble()
        val desiredPower = Utils.getShootingPower(voltageSensor)
        if (shooterPower > desiredPower) {
            shooterPower = desiredPower
        }
        shooter.power = shooterPower
        if (!isShooting && gamepad1.a && shooter.power >= 0.75) {
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

    private fun drivepower() {
        if (gamepad1.square) {
            if (drivespeed == .5) {
                drivespeed = 1.0
            } else {
                drivespeed = .5
            }
        }

    }

    private fun teleOpMode(){
        if (gamepad1.left_bumper){
            if (botCentric == true){
                botCentric = false
            } else{
                botCentric = true
            }
        }
    }

    private fun doShooting2() {
        var shooterPower = gamepad2.right_trigger.toDouble()
        val desiredPower = Utils.getShootingPower(voltageSensor)
        if (shooterPower > desiredPower) {
            shooterPower = desiredPower
        }
        shooter.power = shooterPower
        if (!isShooting && gamepad2.a && shooter.power >= 0.75) {
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


//    private fun driveforwardandback(){
//        if (gamepad1.dpad_up){
//            rightfrontmoter = drivespeed.
//            leftfrontmoter = drivespeed.
//            rightbackmoter = drivespeed.
//            leftfrontmoter = drivespeed.
//        }
//    }
}
// || gamepad2.a