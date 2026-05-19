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
        return Pose(17.0, 78.0, Math.toRadians(270.0))
    }

    override fun getShootPose(): Pose {
        return Pose(48.0, 98.0, Math.toRadians(146.0))
    }

    override fun getTopSpikeStartPose(): Pose {
        return Pose(24.5, 104.0, Math.toRadians(270.0))
    }

    override fun getTopSpikeEndPose(): Pose {
        return Pose(24.5, 84.0, Math.toRadians(270.0))
    }

    override fun getCenterSpikeStartPose(): Pose {
        return Pose(24.5, 80.0, Math.toRadians(270.0))
    }

    override fun getCenterSpikeEndPose(): Pose {
        return Pose(24.5, 60.0, Math.toRadians(270.0))
    }

    override fun getBottomSpikeStartPose(): Pose {
        return Pose(24.5, 56.0, Math.toRadians(270.0))
    }

    override fun getBottomSpikeEndPose(): Pose {
        return Pose(24.5, 36.0, Math.toRadians(270.0))
    }

    override fun getLoadStartPose(): Pose {
        return Pose(15.0, 9.0, Math.toRadians(180.0))
    }

    override fun getLoadEndPose(): Pose {
        return Pose(10.0, 9.0, Math.toRadians(180.0))
    }
}