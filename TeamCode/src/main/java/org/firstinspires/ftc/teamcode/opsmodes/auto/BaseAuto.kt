package org.firstinspires.ftc.teamcode.opsmodes.auto

import com.bylazar.telemetry.PanelsTelemetry
import com.pedropathing.follower.Follower
import com.pedropathing.geometry.BezierLine
import com.pedropathing.geometry.Pose
import com.pedropathing.paths.PathChain
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver
import com.qualcomm.hardware.limelightvision.Limelight3A
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.PIDFCoefficients
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.hardware.VoltageSensor
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.teamcode.opsmodes.pedroPathing.Constants
import org.firstinspires.ftc.teamcode.opsmodes.shared.PathState
import org.firstinspires.ftc.teamcode.opsmodes.shared.Utils

abstract class BaseAuto(val isBlue: Boolean) : LinearOpMode() {

    // Do not delay more than 15 seconds
    val AUTO_WAIT_TIME = 0.0
    val REV_WAIT_TIME = 0.0
    val SHOOT_DELAY_TIME = .5
    var pathState = PathState.WAIT
    var shotNumber = 0
    var rpm: Int = 500
    lateinit var follower: Follower
    lateinit var bottomShootPath: PathChain
    lateinit var endPath: PathChain
    lateinit var shootTimer: ElapsedTime
    lateinit var feeder: Servo
    lateinit var shooter: DcMotorEx
    lateinit var voltageSensor: VoltageSensor
    lateinit var pinpoint: GoBildaPinpointDriver
    lateinit var limelight: Limelight3A

    abstract fun buildPathList(): List<Pose>

    override fun runOpMode() {
        limelight = hardwareMap.get(Limelight3A::class.java, "limelight")
        pinpoint = hardwareMap.get(GoBildaPinpointDriver::class.java, "pinpoint")
        feeder = hardwareMap.get(Servo::class.java, "feeder");
        shooter = hardwareMap.get(DcMotorEx::class.java, "shooter");
        voltageSensor = hardwareMap.get(VoltageSensor::class.java, "Control Hub")
        shooter.direction = DcMotorSimple.Direction.FORWARD
        shooter.mode = DcMotor.RunMode.RUN_USING_ENCODER
        shooter.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER,
            PIDFCoefficients(200.0, 0.0, 20.0, 14.5 * 12 / voltageSensor.voltage)
        )
        shootTimer = ElapsedTime()
        follower = Constants.createFollower(hardwareMap)
        val telemetryM = PanelsTelemetry.telemetry
        feeder.position = 1.0
        limelight.pipelineSwitch(0)
        limelight.start()
        buildPath()

        waitForStart()
        shootTimer.reset()

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

            updatePath()
            follower.update()
            telemetryM.update()
            telemetryM.debug("position", follower.getPose())
            telemetryM.debug("velocity", follower.getVelocity())

            idle()
            telemetry.addData("Shoot Power", shooter.velocity)
            telemetry.update()
        }
    }

    private fun buildPath() {
        val pathList = buildPathList();
        val startingPose = pathList[0]
        val shootingPose = pathList[1]
        val endPose = pathList[2]

        follower.setStartingPose(startingPose)

        bottomShootPath = follower.pathBuilder()
            .setGlobalDeceleration()
            .addPath(BezierLine(startingPose, shootingPose))
            .setLinearHeadingInterpolation(startingPose.heading, shootingPose.heading)
            .build()

        endPath = follower.pathBuilder()
            .setGlobalDeceleration()
            .addPath(BezierLine(shootingPose, endPose))
            .setLinearHeadingInterpolation(shootingPose.heading, endPose.heading)
            .build()
    }

    private fun updatePath() {
        if (follower.isBusy) {
            return
        }

        when (pathState) {
            PathState.WAIT -> {
                if (shootTimer.time() <= AUTO_WAIT_TIME) {
                    return
                } else {
                    shootTimer.reset()
                    shooter.velocity = rpm.toDouble()
                    pathState = PathState.REV_SHOOT
                }
            }
            PathState.REV_SHOOT -> {
                if (shootTimer.time() <= REV_WAIT_TIME) {
                    return
                } else {
                    pathState = PathState.DRIVE_TO_SHOOT
                }
            }

            PathState.DRIVE_TO_SHOOT -> {
                follower.followPath(bottomShootPath)
                pathState = PathState.SHOOT_START
            }

            PathState.SHOOT_START -> {
                shootTimer.reset()
                pathState = PathState.SHOOT
            }

            PathState.SHOOT -> {
                if (shotNumber < 3) {
                    val delay = getDelay(shotNumber)
                    if (shootTimer.time() >= 1 + delay && shootTimer.time() < 1.2 + delay) {
                        feeder.position = 0.5
                    }
                    if (shootTimer.time() >= 1.2 + delay && shootTimer.time() < 1.4 + delay) {
                        feeder.position = 1.0
                        shotNumber++
                    }
                } else {
                    if (shootTimer.time() > 3.0) {
                        pathState = PathState.DRIVE_TO_END
                    }
                }
            }

            PathState.DRIVE_TO_END -> {
                follower.followPath(endPath)
                pathState = PathState.STOP
                shooter.velocity = 0.0
            }

            else -> {
                return
            }
        }
    }

    fun getDelay(shotNumber: Int): Double {
        return shotNumber * SHOOT_DELAY_TIME
    }
}