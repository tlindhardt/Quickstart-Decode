package org.firstinspires.ftc.teamcode.opsmodes.next.opmodes

import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp(name = "Next Hivemind - Field Centric")
class FieldCentricHivemindTeleOp : NextBaseTele(false)

@TeleOp(name = "Next Hivemind - Bot Centric")
class BotCentricHivemindTeleOp : NextBaseTele()