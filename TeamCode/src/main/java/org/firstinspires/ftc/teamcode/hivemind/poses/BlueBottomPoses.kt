package org.firstinspires.ftc.teamcode.hivemind.poses

import com.pedropathing.geometry.Pose

open class BlueBottomPoses : Poses() {

    override fun getStartPose(): Pose {
        return Pose(61.5, 8.5, Math.toRadians(90.0))
    }

    override fun getEndPose(): Pose {
        return Pose(26.0, 84.0, Math.toRadians(180.0))
    }

    override fun getBumpPose(): Pose {
        return Pose(16.0, 78.0, Math.toRadians(270.0))
    }

    override fun getPrimaryShootPose(): Pose {
        return Pose(52.0, 98.0, Math.toRadians(132.0))
    }

    override fun getShootPose(): Pose {
        return Pose(52.0, 98.0, Math.toRadians(140.5))
    }

    override fun getTopSpikeStartPose(): Pose {
        return Pose(27.0, 104.0, Math.toRadians(270.0))
    }

    override fun getTopSpikeEndPose(): Pose {
        return Pose(24.0, 82.0, Math.toRadians(270.0))
    }

    override fun getCenterSpikeStartPose(): Pose {
        return Pose(27.0, 80.0, Math.toRadians(270.0))
    }

    override fun getCenterSpikeEndPose(): Pose {
        return Pose(24.0, 58.0, Math.toRadians(270.0))
    }

    override fun getBottomSpikeStartPose(): Pose {
        return Pose(27.0, 56.0, Math.toRadians(270.0))
    }

    override fun getBottomSpikeEndPose(): Pose {
        return Pose(24.0, 32.0, Math.toRadians(270.0))
    }

    override fun getLoadStartPose(): Pose {
        return Pose(15.0, 9.0, Math.toRadians(180.0))
    }

    override fun getLoadEndPose(): Pose {
        return Pose(10.0, 9.0, Math.toRadians(180.0))
    }
}