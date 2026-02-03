package org.firstinspires.ftc.teamcode.hivemind.poses

import com.pedropathing.geometry.Pose

open class RedTopPoses : RedBottomPoses() {

    override fun getStartPose(): Pose {
        return Pose(115.5, 125.5, Math.toRadians(45.0))
    }

}