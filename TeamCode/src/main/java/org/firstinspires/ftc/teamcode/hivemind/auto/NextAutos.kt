package org.firstinspires.ftc.teamcode.hivemind.auto

import com.qualcomm.robotcore.eventloop.opmode.Autonomous

@Autonomous(name = "Blue Bottom")
open class BlueBottom : NextBaseAuto(isBlue = true, isTop = false)

@Autonomous(name = "Red Bottom")
open class RedBottom : NextBaseAuto(isBlue = false, isTop = false)

@Autonomous(name = "Blue Top")
open class BlueTop : NextBaseAuto(isBlue = true, isTop = true)

@Autonomous(name = "Red Top")
open class RedTop : NextBaseAuto(isBlue = false, isTop = true)