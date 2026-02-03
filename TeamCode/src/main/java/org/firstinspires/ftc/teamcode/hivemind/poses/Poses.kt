package org.firstinspires.ftc.teamcode.hivemind.poses

import com.pedropathing.geometry.Pose

abstract class Poses {
    abstract fun getStartPose(): Pose
    abstract fun getEndPose(): Pose
    abstract fun getBumpPose(): Pose
    abstract fun getShootPose(): Pose
    abstract fun getTopSpikeStartPose(): Pose
    abstract fun getTopSpikeEndPose(): Pose
    abstract fun getCenterSpikeStartPose(): Pose
    abstract fun getCenterSpikeEndPose(): Pose
    abstract fun getBottomSpikeStartPose(): Pose
    abstract fun getBottomSpikeEndPose(): Pose
    abstract fun getLoadStartPose(): Pose
    abstract fun getLoadEndPose(): Pose
}