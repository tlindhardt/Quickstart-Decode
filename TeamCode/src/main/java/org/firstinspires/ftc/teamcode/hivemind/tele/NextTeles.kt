package org.firstinspires.ftc.teamcode.hivemind.tele

import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp(name = "Testing")
class TestingTeleop : Testing()

@TeleOp(name = "Next Hivemind - Field Centric")
class FieldCentricHivemindTeleOp : NextBaseTele(false)

@TeleOp(name = "Next Hivemind - Bot Centric")
class BotCentricHivemindTeleOp : NextBaseTele()