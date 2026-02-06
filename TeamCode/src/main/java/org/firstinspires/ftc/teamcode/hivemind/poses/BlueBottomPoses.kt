package org.firstinspires.ftc.teamcode.hivemind.poses

import com.pedropathing.geometry.Pose

open class BlueBottomPoses : Poses() {

    override fun getStartPose(): Pose {
        return Pose(63.5, 8.5, Math.toRadians(90.0))
    }

    override fun getEndPose(): Pose {
        return Pose(26.0, 74.0, Math.toRadians(180.0))
    }

    override fun getBumpPose(): Pose {
        return Pose(19.5, 74.0, Math.toRadians(270.0))
    }

    override fun getShootPose(): Pose {
        return Pose(54.0, 98.0, Math.toRadians(145.0))
    }

    override fun getTopSpikeStartPose(): Pose {
        return Pose(25.0, 100.0, Math.toRadians(270.0))
    }

    override fun getTopSpikeEndPose(): Pose {
        return Pose(25.0, 84.0, Math.toRadians(270.0))
    }

    override fun getCenterSpikeStartPose(): Pose {
        return Pose(25.0, 76.0, Math.toRadians(270.0))
    }

    override fun getCenterSpikeEndPose(): Pose {
        return Pose(25.0, 62.0, Math.toRadians(270.0))
    }

    override fun getBottomSpikeStartPose(): Pose {
        return Pose(25.0, 52.0, Math.toRadians(270.0))
    }

    override fun getBottomSpikeEndPose(): Pose {
        return Pose(25.0, 38.0, Math.toRadians(270.0))
    }

    override fun getLoadStartPose(): Pose {
        return Pose(15.0, 9.0, Math.toRadians(180.0))
    }

    override fun getLoadEndPose(): Pose {
        return Pose(10.0, 9.0, Math.toRadians(180.0))
    }
}