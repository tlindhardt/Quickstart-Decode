package org.firstinspires.ftc.teamcode.hivemind.auto

import com.pedropathing.geometry.BezierLine
import com.pedropathing.geometry.Pose
import com.pedropathing.paths.PathChain
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import dev.nextftc.core.commands.CommandManager
import dev.nextftc.core.commands.delays.Delay
import dev.nextftc.core.commands.groups.ParallelGroup
import dev.nextftc.core.commands.groups.SequentialGroup
import dev.nextftc.core.components.BindingsComponent
import dev.nextftc.core.components.SubsystemComponent
import dev.nextftc.extensions.pedro.FollowPath
import dev.nextftc.extensions.pedro.PedroComponent
import dev.nextftc.extensions.pedro.PedroComponent.Companion.follower
import dev.nextftc.ftc.ActiveOpMode
import dev.nextftc.ftc.NextFTCOpMode
import dev.nextftc.ftc.components.BulkReadComponent
import org.firstinspires.ftc.teamcode.hivemind.pedroPathing.Constants
import org.firstinspires.ftc.teamcode.hivemind.subsystems.*
import kotlin.time.Duration.Companion.seconds

@Autonomous(name = "Far Blue Bottom")
class FarAutoBlue : NextFTCOpMode() {

    lateinit var paths: Paths

    init {
        addComponents(
            PedroComponent(Constants::createFollower),
            SubsystemComponent(Camera, ColorSensors, Flywheel, Fries, Intake, Lights),
            BulkReadComponent,
            BindingsComponent
        )
    }

    private fun buildPath(startPose: Pose, endPose: Pose): PathChain {
        return follower.pathBuilder()
            .setGlobalDeceleration()
            .addPath(BezierLine(startPose, endPose))
            .setLinearHeadingInterpolation(startPose.heading, endPose.heading)
            .build()
    }


    override fun onStartButtonPressed() {
        val startPose = Pose(39.5, 8.5, Math.toRadians(90.0))
        val shootPose = Pose(50.0, 12.5, Math.toRadians(105.5))
        val secondShootPose = Pose(50.0, 12.5, Math.toRadians(105.5))
        val interPose = Pose(36.0, 6.5, Math.toRadians(180.0))
        val scoopPose = Pose(14.5, 6.5, Math.toRadians(180.0))
        val endPose = Pose(38.0, 12.5, Math.toRadians(180.0))
        val startChain = buildPath(startPose, shootPose)
        val interChain = buildPath(shootPose, interPose)
        val scoopChain = buildPath(interPose, scoopPose)
        val scoopShootChain = buildPath(scoopPose, secondShootPose)
        val endChain = buildPath(shootPose, endPose)
        follower.setStartingPose(startPose)

        SequentialGroup(
            // Start Delay
            Flywheel.long,
            Delay(3.seconds),

            // Shoot preload
            FollowPath(startChain),
            Delay(0.1.seconds),
            Fries.startShooting,
            Fries.fireLeft,
            Delay(0.3.seconds),
            Fries.fireCenter,
            Delay(0.3.seconds),
            Fries.fireRight,
            Delay(0.3.seconds),
            Fries.endShooting,
            Fries.intakeAll,

            // Gather base
            Intake.forward,
            FollowPath(interChain),
            FollowPath(scoopChain),

            // Shoot base
            ParallelGroup(
                FollowPath(scoopShootChain, true, 0.7),
                SequentialGroup(
                    Delay(0.3.seconds),
                    Intake.off,
                )
            ),
            Delay(0.1.seconds),
            Fries.startShooting,
            Fries.fireLeft,
            Delay(0.3.seconds),
            Fries.fireCenter,
            Delay(0.3.seconds),
            Fries.fireRight,
            Delay(0.3.seconds),
            Fries.endShooting,
            Fries.intakeAll,

            // Wait for clear
            Delay(1.seconds),// Gather base
            Intake.forward,
            FollowPath(interChain),
            FollowPath(scoopChain),

            // Shoot base
            ParallelGroup(
                FollowPath(scoopShootChain, true, 0.7),
                SequentialGroup(
                    Delay(0.3.seconds),
                    Intake.off,
                )
            ),
            Delay(0.1.seconds),
            Fries.startShooting,
            Fries.fireLeft,
            Delay(0.3.seconds),
            Fries.fireCenter,
            Delay(0.3.seconds),
            Fries.fireRight,
            Delay(0.3.seconds),
            Fries.endShooting,
            Fries.intakeAll,

            // Wait for clear
            Delay(1.seconds),// Gather base
            Intake.forward,
            FollowPath(interChain),
            FollowPath(scoopChain),

            // Shoot base
            ParallelGroup(
                FollowPath(scoopShootChain, true, 0.7),
                SequentialGroup(
                    Delay(0.3.seconds),
                    Intake.off,
                )
            ),
            Delay(0.1.seconds),
            Fries.startShooting,
            Fries.fireLeft,
            Delay(0.3.seconds),
            Fries.fireCenter,
            Delay(0.3.seconds),
            Fries.fireRight,
            Delay(0.3.seconds),
            Fries.endShooting,
            Fries.intakeAll,

            // Wait for clear
            Delay(1.seconds),// Gather base
            Intake.forward,
            FollowPath(interChain),
            FollowPath(scoopChain),

            // Shoot base
            ParallelGroup(
                FollowPath(scoopShootChain, true, 0.7),
                SequentialGroup(
                    Delay(0.3.seconds),
                    Intake.off,
                )
            ),
            Delay(0.1.seconds),
            Fries.startShooting,
            Fries.fireLeft,
            Delay(0.3.seconds),
            Fries.fireCenter,
            Delay(0.3.seconds),
            Fries.fireRight,
            Delay(0.3.seconds),
            Fries.endShooting,
            Fries.intakeAll,

            FollowPath(endChain),

            Flywheel.off
        ).schedule()
    }

    override fun onInit() {
        Fries.startRunning.schedule()
    }

    override fun onUpdate() {
        ActiveOpMode.telemetry.update()
    }

    override fun onWaitForStart() {
        ActiveOpMode.telemetry.update()
    }

    override fun onStop() {
        Flywheel.off.schedule()
        Fries.intakeAll.schedule()
        Intake.off.schedule()
        Fries.stopRunning.schedule()
        Fries.endShooting.schedule()
        Camera.orderFound = false
        Fries.hasStarted = false
        CommandManager.cancelAll()
    }
}