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


open class TeleOpJaceDrive(var botCentric: Boolean = true) : LinearOpMode() {

    var isShooting: Boolean = false
    var drivespeed:  Double = 1.0
    lateinit var follower: Follower
    lateinit var shootTimer: ElapsedTime
    lateinit var feeder: Servo
    lateinit var shooter: DcMotor
    //
    lateinit var leftDrive: DcMotor
    private var rightDrive: DcMotor? = null
    private var leftbackDrive: DcMotor? = null
    private var rightbackDrive: DcMotor? = null
    var FORWARD_SPEED: Double = drivespeed
    //
    var shootpower1: Double = 2.0

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
        //
        leftbackDrive = hardwareMap.get(DcMotor::class.java, "left back")
        rightbackDrive = hardwareMap.get(DcMotor::class.java, "right back")
        leftDrive = hardwareMap.get(DcMotor::class.java, "left front")
        rightDrive = hardwareMap.get(DcMotor::class.java, "right front")
  //      leftDrive!!.setDirection(DcMotor.Direction.REVERSE)
    //    rightDrive!!.setDirection(DcMotor.Direction.FORWARD)
        telemetry.addData("Status", "Ready to run") //
        telemetry.update()
        //
//        return listOf(
//            Pose(94.0, 0.0, Math.toRadians(45.0)),
//        )


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
            doShooting2()
            //
            drivewithDpad()
            shootpower()
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

    private fun shootpower(){
        if (gamepad2.triangle){
            if (shootpower1 == 2.0) {
                shootpower1 = 1.0
            }
        } else {
            shootpower1 = 2.0
        }
    }


    private fun doShooting2() {
        var shooterPower = gamepad2.right_trigger/shootpower1.toDouble()
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

    private fun drivewithDpad(){
        if (gamepad1.dpad_up){
            leftDrive.setPower(FORWARD_SPEED);
            rightDrive?.setPower(FORWARD_SPEED);
            rightbackDrive?.setPower(FORWARD_SPEED);
            leftbackDrive?.setPower(FORWARD_SPEED);
        }
        if (gamepad1.dpad_down){
            leftDrive?.setPower(-FORWARD_SPEED);
            rightDrive?.setPower(-FORWARD_SPEED);
            rightbackDrive?.setPower(-FORWARD_SPEED);
            leftbackDrive?.setPower(-FORWARD_SPEED);
        }
        if (gamepad1.dpad_right){
            leftbackDrive?.setPower(-FORWARD_SPEED);
            rightbackDrive?.setPower(FORWARD_SPEED);
            leftDrive?.setPower(FORWARD_SPEED);
            rightDrive?.setPower(-FORWARD_SPEED);
        }
        if (gamepad1.dpad_left){
            leftbackDrive?.setPower(FORWARD_SPEED);
            rightbackDrive?.setPower(-FORWARD_SPEED);
            leftDrive?.setPower(-FORWARD_SPEED);
            rightDrive?.setPower(FORWARD_SPEED);
        }
    }




//    private fun drivetoshooting(){
//        if (gamepad1.square){
//
//        }
//    }
}
// || gamepad2.a