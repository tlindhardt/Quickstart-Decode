package org.firstinspires.ftc.teamcode.opsmodes.auto

import com.pedropathing.geometry.Pose
import com.qualcomm.robotcore.eventloop.opmode.Autonomous


@Autonomous(name = "Nxt Hivemind - Blue Bottom")
open class NxtBlueBottomHivemindAuto : NxtAuto() {
    override fun buildPathList(): List<Pose> {
        return listOf(
            Pose(0.0, 0.0),
            Pose(90.0, 0.0, Math.toRadians(50.0)),
            Pose(101.0, 30.0, Math.toRadians(180.0)),
            Pose(78.0, 30.0, Math.toRadians(180.0)),
            Pose(90.0, 0.0, Math.toRadians(50.0)),
            Pose(120.0, 0.0, Math.toRadians(90.0))
        )
    }
}

@Autonomous(name = "Nxt Hivemind - Red Bottom")
open class NxtRedBottomHivemindAuto : NxtBlueBottomHivemindAuto() {
    override fun buildPathList(): List<Pose> {
        val pathList = super.buildPathList()
        return listOf(
            pathList[0].mirror(),
            pathList[1].mirror(),
            pathList[2].mirror(),
            pathList[3].mirror(),
            pathList[4].mirror(),
            pathList[5].mirror(),
        )
    }
}

@Autonomous(name = "Hivemind - Blue Bottom")
open class BlueBottomHivemindAuto : BaseAuto() {
    override fun buildPathList(): List<Pose> {
        return listOf(
            Pose(0.0, 0.0),
            Pose(90.0, 0.0, Math.toRadians(50.0)),
            Pose(120.0, 0.0, Math.toRadians(90.0))
        )
    }
}

@Autonomous(name = "Hivemind - Red Bottom")
class RedBottomHivemindAuto : BlueBottomHivemindAuto() {
    override fun buildPathList(): List<Pose> {
        val pathList = super.buildPathList()
        return listOf(
            pathList[0].mirror(),
            pathList[1].mirror(),
            pathList[2].mirror(),
        )
    }
}

@Autonomous(name = "Hivemind - Blue Top")
open class BlueTopHivemindAuto : BaseAuto() {
    override fun buildPathList(): List<Pose> {
        return listOf(
            Pose(0.0, 0.0),
            Pose(46.0, 0.0, Math.toRadians(185.0)),
            Pose(24.0, 24.0, Math.toRadians(-135.0))
        )
    }
}

@Autonomous(name = "Hivemind - Red Top")
class RedTopHivemindAuto : BlueTopHivemindAuto() {
    override fun buildPathList(): List<Pose> {
        val pathList = super.buildPathList()
        return listOf(
            pathList[0],
            Pose(pathList[1].x, pathList[1].y, -pathList[1].heading),
            Pose(pathList[2].x, -pathList[2].y, -pathList[2].heading),
        )
    }
}