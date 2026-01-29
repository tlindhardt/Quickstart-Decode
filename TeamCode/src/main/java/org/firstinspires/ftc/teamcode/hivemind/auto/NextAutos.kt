package org.firstinspires.ftc.teamcode.hivemind.auto

import com.qualcomm.robotcore.eventloop.opmode.Autonomous

@Autonomous(name = "Blue Bottom")
open class BlueBottom : NextBaseAuto(true, false)

@Autonomous(name = "Red Bottom")
open class RedBottom : NextBaseAuto(false, false)

@Autonomous(name = "Blue Top")
open class BlueTop : NextBaseAuto(true, true)

@Autonomous(name = "Red Top")
open class RedTop : NextBaseAuto(false, true)