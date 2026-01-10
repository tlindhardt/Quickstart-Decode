package org.firstinspires.ftc.teamcode.opsmodes.tele

import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp(name = "Hivemind - Blue Field Centric")
class BlueFieldCentricHivemindTeleOp : BaseTele(false, true)

@TeleOp(name = "Hivemind - Red Field Centric")
class RedFieldCentricHivemindTeleOp : BaseTele(false, false)

//@TeleOp(name = "Hivemind - Bot Centric")
//class BotCentricHivemindTeleOp : BaseTele()