package org.firstinspires.ftc.teamcode.hivemind.auto

import com.qualcomm.robotcore.eventloop.opmode.Autonomous

@Autonomous(name = "Blue Bottom")
open class BlueBottom : NextBaseAuto(
    PathsBuilder.build(
        isBlue = true,
        isTop = false
    )
)

@Autonomous(name = "Red Bottom")
open class RedBottom : NextBaseAuto(
    PathsBuilder.build(
        isBlue = false,
        isTop = false
    )
)

@Autonomous(name = "Blue Top")
open class BlueTop : NextBaseAuto(
    PathsBuilder.build(
        isBlue = true,
        isTop = true
    )
)

@Autonomous(name = "Red Top")
open class RedTop : NextBaseAuto(
    PathsBuilder.build(
        isBlue = false,
        isTop = true
    )
)