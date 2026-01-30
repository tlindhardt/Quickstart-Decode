package org.firstinspires.ftc.teamcode.hivemind.tele

import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp(name = "Testing")
class TestingTeleop : Testing()

@TeleOp(name = "Blue - Field Centric")
class BlueFieldCentricHivemindTeleOp : NextBaseTele(isBlue = true)

@TeleOp(name = "Red - Field Centric")
class RedFieldCentricHivemindTeleOp : NextBaseTele(isBlue = false)