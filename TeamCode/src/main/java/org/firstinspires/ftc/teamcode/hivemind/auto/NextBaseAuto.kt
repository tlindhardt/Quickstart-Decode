package org.firstinspires.ftc.teamcode.hivemind.auto

import com.pedropathing.geometry.BezierLine
import com.pedropathing.geometry.Pose
import com.pedropathing.paths.PathChain
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
import org.firstinspires.ftc.teamcode.hivemind.subsystems.Flywheel
import org.firstinspires.ftc.teamcode.hivemind.subsystems.Fries
import org.firstinspires.ftc.teamcode.hivemind.subsystems.Intake
import kotlin.time.Duration.Companion.seconds

abstract class NextBaseAuto : NextFTCOpMode() {
    lateinit var shootPath: PathChain
    lateinit var endPath: PathChain

    init {
        addComponents(
            PedroComponent(Constants::createFollower),
            SubsystemComponent(Flywheel, Fries, Intake),
            BulkReadComponent,
            BindingsComponent
        )
    }

    abstract fun buildPathList(): List<Pose>


    override fun onStartButtonPressed() {
        SequentialGroup(

            // Start Delay
            Delay(1.seconds),

            Fries.intakeAll,
            Intake.forward,
            Flywheel.close,
            Delay(1.seconds),

            // Drive
            FollowPath(shootPath),

            // Shoot
            Intake.off,
            SequentialGroup(
                Fries.fireLeft,
                Delay(0.2.seconds),
                Fries.intakeLeft,
                Fries.fireCenter,
                Delay(0.2.seconds),
                Fries.intakeCenter,
                Fries.fireRight,
                Delay(0.2.seconds),
                Fries.intakeRight
            ),

            // End
            FollowPath(endPath),
            Flywheel.off,
        ).schedule()
    }

    override fun onInit() {
        buildPath()
    }

    override fun onUpdate() {
        ActiveOpMode.telemetry.update()
    }

    override fun onWaitForStart() {
        ActiveOpMode.telemetry.update()
    }

    private fun buildPath() {
        val pathList = buildPathList();
        val startingPose = pathList[0]
        val shootingPose = pathList[1]
        val endPose = pathList[2]

        follower.setStartingPose(startingPose)

        shootPath = follower.pathBuilder()
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
}