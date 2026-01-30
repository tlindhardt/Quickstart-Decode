package org.firstinspires.ftc.teamcode.hivemind.auto

import com.pedropathing.follower.Follower
import com.pedropathing.geometry.BezierLine
import com.pedropathing.geometry.Pose
import com.pedropathing.paths.PathChain

class PathsBuilder {

    companion object {
        val topStartPose = Pose(25.5, 129.5, Math.toRadians(135.0))
        val bottomStartPose = Pose(57.0, 7.5, Math.toRadians(90.0))
        val shootPose = Pose(57.0, 100.0, Math.toRadians(142.0))
        val topSpikeStartPose = Pose(24.0, 96.0, Math.toRadians(270.0))
        val topSpikeEndPose = Pose(24.0, 86.0, Math.toRadians(270.0))
        val centerSpikeStartPose = Pose(24.0, 72.0, Math.toRadians(270.0))
        val centerSpikeEndPose = Pose(24.0, 62.0, Math.toRadians(270.0))
        val bottomSpikeStartPose = Pose(24.0, 48.0, Math.toRadians(270.0))
        val bottomSpikeEndPose = Pose(24.0, 38.0, Math.toRadians(270.0))
        val loadStartPose = Pose(15.0, 9.0, Math.toRadians(180.0))
        val loadEndPose = Pose(10.0, 9.0, Math.toRadians(180.0))

        fun build(isBlue: Boolean, isTop: Boolean, follower: Follower): Paths {
            val startPose = if (isTop) topStartPose else bottomStartPose
            return Paths(
                startPose,
                buildPath(startPose, shootPose, isBlue, follower),

                // Top Spike
                Pair(
                    buildPath(shootPose, topSpikeStartPose, isBlue, follower),
                    buildPath(topSpikeStartPose, topSpikeEndPose, isBlue, follower)
                ),
                buildPath(topSpikeEndPose, shootPose, isBlue, follower),

                // Center Spike
                Pair(
                    buildPath(shootPose, centerSpikeStartPose, isBlue, follower),
                    buildPath(centerSpikeStartPose, centerSpikeEndPose, isBlue, follower),
                ),
                buildPath(centerSpikeEndPose, shootPose, isBlue, follower),

                // Bottom Spike
                Pair(
                    buildPath(shootPose, bottomSpikeStartPose, isBlue, follower),
                    buildPath(bottomSpikeStartPose, bottomSpikeEndPose, isBlue, follower),
                ),
                buildPath(bottomSpikeEndPose, shootPose, isBlue, follower),

                // Load Zone
                Pair(
                    buildPath(shootPose, loadStartPose, isBlue, follower),
                    buildPath(loadStartPose, loadEndPose, isBlue, follower),
                ),
                buildPath(loadEndPose, shootPose, isBlue, follower),
            )
        }

        private fun buildPath(startPose: Pose, endPose: Pose, isBlue: Boolean, follower: Follower): PathChain {
            val startPose = if (isBlue) startPose else startPose.mirror()
            val endPose = if (isBlue) endPose else endPose.mirror()
            return follower.pathBuilder()
                .addPath(BezierLine(startPose, endPose))
                .setLinearHeadingInterpolation(startPose.heading, endPose.heading)
                .build()
        }
    }
}