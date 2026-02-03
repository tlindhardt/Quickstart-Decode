package org.firstinspires.ftc.teamcode.hivemind.auto

import com.pedropathing.follower.Follower
import com.pedropathing.geometry.BezierLine
import com.pedropathing.geometry.Pose
import com.pedropathing.paths.PathChain
import org.firstinspires.ftc.teamcode.hivemind.poses.BlueBottomPoses
import org.firstinspires.ftc.teamcode.hivemind.poses.BlueTopPoses
import org.firstinspires.ftc.teamcode.hivemind.poses.Poses
import org.firstinspires.ftc.teamcode.hivemind.poses.RedBottomPoses
import org.firstinspires.ftc.teamcode.hivemind.poses.RedTopPoses

class PathsBuilder {

    companion object {

        fun build(isBlue: Boolean, isTop: Boolean, follower: Follower): Paths {
            val paths = getPaths(isBlue, isTop)
            return Paths(
                paths.getStartPose(),
                buildPath(paths.getStartPose(), paths.getShootPose(), follower),

                // Top Spike
                Pair(
                    buildPath(paths.getShootPose(), paths.getTopSpikeStartPose(), follower),
                    buildPath(paths.getTopSpikeStartPose(), paths.getTopSpikeEndPose(), follower)
                ),
                buildPath(paths.getBumpPose(), paths.getShootPose(), follower),
                buildPath(paths.getTopSpikeEndPose(), paths.getBumpPose(), follower),

                // Center Spike
                Pair(
                    buildPath(paths.getShootPose(), paths.getCenterSpikeStartPose(), follower),
                    buildPath(paths.getCenterSpikeStartPose(), paths.getCenterSpikeEndPose(), follower),
                ),
                buildPath(paths.getCenterSpikeEndPose(), paths.getShootPose(), follower),

                // Bottom Spike
                Pair(
                    buildPath(paths.getShootPose(), paths.getBottomSpikeStartPose(), follower),
                    buildPath(paths.getBottomSpikeStartPose(), paths.getBottomSpikeEndPose(), follower),
                ),
                buildPath(paths.getBottomSpikeEndPose(), paths.getShootPose(), follower),

                // Load Zone
                Pair(
                    buildPath(paths.getShootPose(), paths.getLoadStartPose(), follower),
                    buildPath(paths.getLoadStartPose(), paths.getLoadEndPose(), follower),
                ),
                buildPath(paths.getLoadEndPose(), paths.getShootPose(), follower),
                // Parking
                buildPath(paths.getShootPose(), paths.getEndPose(), follower),
            )
        }

        private fun getPaths(isBlue: Boolean, isTop: Boolean): Poses {
            if (isBlue) {
                if (isTop) {
                    return BlueTopPoses()
                }
                return BlueBottomPoses()
            } else {
                if (isTop) {
                    return RedTopPoses()
                }
                return RedBottomPoses()
            }
        }

        private fun buildPath(startPose: Pose, endPose: Pose, follower: Follower): PathChain {
            return follower.pathBuilder()
                .setGlobalDeceleration()
                .addPath(BezierLine(startPose, endPose))
                .setLinearHeadingInterpolation(startPose.heading, endPose.heading)
                .build()
        }
    }
}