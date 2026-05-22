package org.firstinspires.ftc.teamcode.hivemind.tele

import com.qualcomm.robotcore.eventloop.opmode.TeleOp

//@TeleOp(name = "Testing")
//class TestingTeleop : Testing()

@TeleOp(name = "Blue - Teleop")
class BlueFieldCentricHivemindTeleOp : NextBaseTele(isBlue = true)

@TeleOp(name = "Red - Teleop")
class RedFieldCentricHivemindTeleOp : NextBaseTele(isBlue = false)

@TeleOp(name = "Blue - Calibration")
class BlueCalibrationTeleop : NextBaseTele(isBlue = true, isCalibration = true)

@TeleOp(name = "Red - Calibration")
class RedCalibrationTeleop : NextBaseTele(isBlue = true, isCalibration = true)