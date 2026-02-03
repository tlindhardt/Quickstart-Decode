package org.firstinspires.ftc.teamcode.hivemind.poses

import com.pedropathing.geometry.Pose

open class BlueTopPoses : BlueBottomPoses() {

    override fun getStartPose(): Pose {
        return Pose(25.75, 127.5, Math.toRadians(144.0))
    }
}