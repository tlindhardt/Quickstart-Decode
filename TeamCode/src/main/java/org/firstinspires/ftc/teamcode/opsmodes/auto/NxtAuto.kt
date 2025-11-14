package org.firstinspires.ftc.teamcode.opsmodes.auto

import com.bylazar.telemetry.PanelsTelemetry
import com.pedropathing.follower.Follower
import com.pedropathing.geometry.BezierLine
import com.pedropathing.geometry.Pose
import com.pedropathing.paths.PathChain
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.hardware.VoltageSensor
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.opsmodes.pedroPathing.Constants
import org.firstinspires.ftc.teamcode.opsmodes.shared.PathState
import org.firstinspires.ftc.teamcode.opsmodes.shared.Utils

abstract class NxtAuto : LinearOpMode() {

    // Do not delay more than 15 seconds
    val AUTO_WAIT_TIME = 0.0
    val REV_WAIT_TIME = 2.0
    val SHOOT_DELAY_TIME = 3.0
    var pathState = PathState.WAIT
    var shotNumber = 0
    var paths = mutableListOf<PathChain>()
    lateinit var follower: Follower
    lateinit var shootTimer: ElapsedTime
    lateinit var feeder: Servo
    lateinit var shooter: DcMotor
    lateinit var voltageSensor: VoltageSensor

    abstract fun buildPathList(): List<Pose>

    override fun runOpMode() {
        feeder = hardwareMap.get(Servo::class.java, "feeder");
        shooter = hardwareMap.get(DcMotor::class.java, "shooter");
        voltageSensor = hardwareMap.get(VoltageSensor::class.java, "Control Hub")
        shootTimer = ElapsedTime()
        follower = Constants.createFollower(hardwareMap)
        val telemetryM = PanelsTelemetry.telemetry
        feeder.position = 1.0
        buildPath()

        waitForStart()
        shootTimer.reset()

        while (opModeIsActive()) {
            updatePath()
            follower.update()
            telemetryM.update()
            telemetryM.debug("position", follower.getPose())
            telemetryM.debug("velocity", follower.getVelocity())

            idle()
            telemetry.addData("Shoot Power", shooter.power)
            telemetry.update()
        }
    }

    private fun buildPath() {
        val pathList = buildPathList();
        follower.setStartingPose(pathList[0])

        for(i in 0 until pathList.size - 1) {
            val start = pathList[i]
            val end = pathList[i + 1]
            paths.add(
                follower.pathBuilder()
                    .setGlobalDeceleration()
                    .addPath(BezierLine(start, end))
                    .setLinearHeadingInterpolation(start.heading, end.heading)
                    .build()
            )
        }

//        bottomShootPath = follower.pathBuilder()
//            .setGlobalDeceleration()
//            .addPath(BezierLine(startingPose, shootingPose))
//            .setLinearHeadingInterpolation(startingPose.heading, shootingPose.heading)
//            .build()
//
//        shootPaths = listOf(
//            follower.pathBuilder()
//                .setGlobalDeceleration()
//                .addPath(BezierLine(shootingPose, shoot_1_1))
//                .setLinearHeadingInterpolation(shootingPose.heading, shoot_1_1.heading)
//                .build(),
//            follower.pathBuilder()
//                .setGlobalDeceleration()
//                .addPath(BezierLine(shoot_1_1, shoot_1_2))
//                .setLinearHeadingInterpolation(shoot_1_1.heading, shoot_1_2.heading)
//                .build(),
//            follower.pathBuilder()
//                .setGlobalDeceleration()
//                .addPath(BezierLine(shoot_1_2, shootingPose))
//                .setLinearHeadingInterpolation(shoot_1_2.heading, shootingPose.heading)
//                .build()
//        )
//
//        endPath = follower.pathBuilder()
//            .setGlobalDeceleration()
//            .addPath(BezierLine(shootingPose, endPose))
//            .setLinearHeadingInterpolation(shootingPose.heading, endPose.heading)
//            .build()
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
                    shooter.power = Utils.getShootingPower(voltageSensor)
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
                follower.followPath(paths[0])
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
                    pathState = PathState.NXT_DRIVE_TO_SHOOT_1_1
                }
            }

            PathState.NXT_DRIVE_TO_SHOOT_1_1 -> {
                follower.followPath(paths[1])
                pathState = PathState.NXT_DRIVE_TO_SHOOT_1_2
            }

            PathState.NXT_DRIVE_TO_SHOOT_1_2 -> {
                follower.followPath(paths[2])
                pathState = PathState.NXT_DRIVE_TO_SHOOT_1_3
            }

            PathState.NXT_DRIVE_TO_SHOOT_1_3 -> {
                follower.followPath(paths[3])
                pathState = PathState.DRIVE_TO_END
            }

            PathState.DRIVE_TO_END -> {
                follower.followPath(paths[4])
                pathState = PathState.STOP
                shooter.power = 0.0
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