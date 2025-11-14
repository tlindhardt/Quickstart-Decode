package org.firstinspires.ftc.teamcode.opsmodes.auto

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode

@Autonomous(name = "Auto")
class Auto : LinearOpMode() {

    override fun runOpMode() {
        //init code goes here

        waitForStart()
        //post start init code goes here

        while (opModeIsActive()) {
            //state machine code goes here

            idle()
        }
    }
}