package org.firstinspires.ftc.teamcode.hivemind.auto

import com.pedropathing.paths.PathChain
import dev.nextftc.core.commands.Command
import dev.nextftc.core.commands.delays.Delay
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
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

abstract class NextBaseAuto(val isBlue: Boolean, val isTop: Boolean) : NextFTCOpMode() {

    lateinit var paths: Paths

    init {
        addComponents(
            PedroComponent(Constants::createFollower),
            SubsystemComponent(),
            BulkReadComponent,
            BindingsComponent
        )
    }

    override fun onStartButtonPressed() {
        SequentialGroup(
            // Start Delay
            Delay(100.milliseconds),

            // Shoot Preload
            driveAndShoot(paths.initialShootPath),

            // Shoot Top Spike
            driveAndLoad(paths.topSpike),
            driveAndShoot(paths.topSpikeShootPath),

            // Shoot Center Spike
            driveAndLoad(paths.centerSpike),
            driveAndShoot(paths.centerSpikeShootPath),

            // Shoot Bottom Spike
            driveAndLoad(paths.bottomSpike),
            driveAndShoot(paths.bottomSpikeShootPath),

            //Shoot Load
            driveAndLoad(paths.load),
            driveAndShoot(paths.loadShootPath),

            FollowPath(paths.centerSpike.first),
        ).schedule()
    }

    override fun onInit() {
        paths = PathsBuilder.build(isBlue, isTop, follower)
        follower.setStartingPose(paths.startPose)
    }

    override fun onUpdate() {
        ActiveOpMode.telemetry.addData("x", follower.pose.x)
        ActiveOpMode.telemetry.addData("y", follower.pose.y)
        ActiveOpMode.telemetry.update()
    }

    override fun onWaitForStart() {
        ActiveOpMode.telemetry.update()
    }

    val driveAndShoot: (PathChain) -> Command = {
        SequentialGroup(
//            Flywheel.top,
            FollowPath(it),
//            Fries.fireLeft,
            Delay(0.2.seconds),
//            Fries.fireCenter,
            Delay(0.2.seconds),
//            Fries.fireRight,
            Delay(0.2.seconds),
//            Flywheel.off,
//            Fries.intakeAll
        )
    }

    val driveAndLoad: (Pair<PathChain, PathChain>) -> Command = {
        SequentialGroup(
            FollowPath(it.first),
//            Intake.forward,
            FollowPath(it.second),
//            Intake.off,
        )
    }
}