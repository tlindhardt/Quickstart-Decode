package org.firstinspires.ftc.teamcode.opsmodes.tele

import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp(name = "Hivemind - Field Centric")
class FieldCentricHivemindTeleOp : BaseTele(false)

@TeleOp(name = "Hivemind - Bot Centric")
class BotCentricHivemindTeleOp : BaseTele()

@TeleOp(name = "Hivemind - TeleOp Jace")
class HivemindTeleOpJace : TeleOpJace(false)

@TeleOp(name = "Hivemind - TeleOp Jace drive")
class HivemindTeleOpJaceDrive : TeleOpJaceDrive(false)
