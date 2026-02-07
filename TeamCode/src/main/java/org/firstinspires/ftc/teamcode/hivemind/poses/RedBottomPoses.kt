package org.firstinspires.ftc.teamcode.hivemind.poses

import com.pedropathing.geometry.Pose

open class RedBottomPoses : BlueBottomPoses() {

    override fun getStartPose(): Pose {
        return super.getStartPose().mirror()
    }

    override fun getEndPose(): Pose {
        return super.getEndPose().mirror()
    }

    override fun getBumpPose(): Pose {
        val bumpPose = super.getBumpPose().mirror()
        return Pose(bumpPose.x + 1, bumpPose.y, bumpPose.heading)
    }

    override fun getShootPose(): Pose {
        val shootPose = super.getShootPose().mirror()
        return Pose(shootPose.x, shootPose.y, Math.toRadians(37.0))
    }

    override fun getTopSpikeStartPose(): Pose {
        return super.getTopSpikeStartPose().mirror()
    }

    override fun getTopSpikeEndPose(): Pose {
        return super.getTopSpikeEndPose().mirror()
    }

    override fun getCenterSpikeStartPose(): Pose {
        return super.getCenterSpikeStartPose().mirror()
    }

    override fun getCenterSpikeEndPose(): Pose {
        return super.getCenterSpikeEndPose().mirror()
    }

    override fun getBottomSpikeStartPose(): Pose {
        return super.getBottomSpikeStartPose().mirror()
    }

    override fun getBottomSpikeEndPose(): Pose {
        return super.getBottomSpikeEndPose().mirror()
    }

    override fun getLoadStartPose(): Pose {
        return super.getLoadStartPose().mirror()
    }

    override fun getLoadEndPose(): Pose {
        return super.getLoadEndPose().mirror()
    }
}