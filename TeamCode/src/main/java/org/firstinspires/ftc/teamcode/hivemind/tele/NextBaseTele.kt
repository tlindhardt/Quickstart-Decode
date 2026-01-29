package org.firstinspires.ftc.teamcode.hivemind.tele

import dev.nextftc.bindings.BindingManager
import dev.nextftc.core.commands.delays.Delay
import dev.nextftc.core.commands.groups.ParallelGroup
import dev.nextftc.core.commands.groups.SequentialGroup
import dev.nextftc.core.components.BindingsComponent
import dev.nextftc.core.components.SubsystemComponent
import dev.nextftc.extensions.pedro.PedroComponent
import dev.nextftc.extensions.pedro.PedroComponent.Companion.follower
import dev.nextftc.extensions.pedro.PedroDriverControlled
import dev.nextftc.ftc.ActiveOpMode
import dev.nextftc.ftc.Gamepads
import dev.nextftc.ftc.NextFTCOpMode
import dev.nextftc.ftc.components.BulkReadComponent
import org.firstinspires.ftc.teamcode.hivemind.pedroPathing.Constants
import kotlin.time.Duration.Companion.seconds

abstract class NextBaseTele(val botCentric: Boolean = true) : NextFTCOpMode() {
    private var lastAdjustTime = 0.0

//    lateinit var sensor: NormalizedColorSensor

    init {
        addComponents(
            PedroComponent(Constants::createFollower),
            SubsystemComponent(),
            BulkReadComponent,
            BindingsComponent
        )
    }

    override fun onStartButtonPressed() {
        // DRIVER CONTROLS
        Gamepads.gamepad1.rightTrigger.greaterThan(.3)
//            .whenBecomesTrue { Intake.forward.schedule() }
//            .whenBecomesFalse { Intake.off.schedule() }
        Gamepads.gamepad1.leftTrigger.greaterThan(.3)
//            .whenBecomesTrue { Intake.reverse.schedule() }
//            .whenBecomesFalse { Intake.off.schedule() }

        // SHOOTER CONTROLS
        Gamepads.gamepad2.rightTrigger.greaterThan(.3)
//            .whenTrue { Flywheel.top.schedule() }
//            .whenFalse { Flywheel.off.schedule() }
//        Gamepads.gamepad2.square.and(Gamepads.gamepad2.rightTrigger.greaterThan(0.3))
//            .whenTrue {
//                SequentialGroup(
//                    Fries.fireLeft,
//                    Delay(0.2.seconds),
//                    Fries.intakeLeft
//                ).schedule()
//            }
//        Gamepads.gamepad2.triangle.and(Gamepads.gamepad2.rightTrigger.greaterThan(0.3))
//            .whenTrue {
//                SequentialGroup(
//                    Fries.fireCenter,
//                    Delay(0.2.seconds),
//                    Fries.intakeCenter
//                ).schedule()
//            }
//        Gamepads.gamepad2.circle.and(Gamepads.gamepad2.rightTrigger.greaterThan(0.3))
//            .whenTrue {
//                SequentialGroup(
//                    Fries.fireRight,
//                    Delay(0.2.seconds),
//                    Fries.intakeRight
//                ).schedule()
//            }
        Gamepads.gamepad2.cross.and(Gamepads.gamepad2.rightTrigger.greaterThan(0.3))
            .whenTrue {
                ParallelGroup(
//                    Intake.off,
                    SequentialGroup(
//                        Fries.fireLeft,
                        Delay(0.2.seconds),
//                        Fries.intakeLeft,
//                        Fries.fireCenter,
                        Delay(0.2.seconds),
//                        Fries.intakeCenter,
//                        Fries.fireRight,
                        Delay(0.2.seconds),
//                        Fries.intakeRight
                    )
                ).schedule()
            }

        // DELETE ME
//        Gamepads.gamepad1.square
//            .whenTrue {
//                SequentialGroup(
//                    Fries.fireLeft,
//                    Delay(0.2.seconds),
//                    Fries.intakeLeft
//                ).schedule()
//            }
//        Gamepads.gamepad1.triangle
//            .whenTrue {
//                SequentialGroup(
//                    Fries.fireCenter,
//                    Delay(0.2.seconds),
//                    Fries.intakeCenter
//                ).schedule()
//            }
//        Gamepads.gamepad1.circle
//            .whenTrue {
//                SequentialGroup(
//                    Fries.fireRight,
//                    Delay(0.2.seconds),
//                    Fries.intakeRight
//                ).schedule()
//            }
//        Gamepads.gamepad1.cross
//            .whenTrue {
//                ParallelGroup(
//                    SequentialGroup(
//                        Fries.fireLeft,
//                        Delay(0.2.seconds),
//                        Fries.intakeLeft,
//                        Fries.fireCenter,
//                        Delay(0.2.seconds),
//                        Fries.intakeCenter,
//                        Fries.fireRight,
//                        Delay(0.2.seconds),
//                        Fries.intakeRight
//                    )
//                ).schedule()
//            }
//        Gamepads.gamepad1.dpadUp
//            .whenTrue {
//                if (runtime - lastAdjustTime >= 0.1) {
//                    Flywheel.velocity += 50
//                    lastAdjustTime = runtime
//                }
//            }
//        Gamepads.gamepad1.dpadDown
//            .whenTrue {
//                if (runtime - lastAdjustTime >= 0.1) {
//                    Flywheel.velocity -= 50
//                    lastAdjustTime = runtime
//                }
//            }
//        Gamepads.gamepad1.dpadLeft
//            .whenTrue { Flywheel.close.schedule() }
//        Gamepads.gamepad1.dpadRight
//            .whenTrue { Flywheel.off.schedule() }
//        Gamepads.gamepad2.dpadLeft
//            .whenTrue { Hood.open.schedule() }
//        Gamepads.gamepad2.dpadRight
//            .whenTrue { Hood.close.schedule() }
        Gamepads.gamepad2.leftBumper
//            .whenTrue { Fries.holdAll.schedule() }
        Gamepads.gamepad2.rightBumper
//            .whenTrue { Fries.intakeAll.schedule() }
    }

    override fun onInit() {
//        sensor = hardwareMap.get(NormalizedColorSensor::class.java, "center_color")
//        sensor.gain = 4.toFloat()
        val driverControlled = PedroDriverControlled(
            Gamepads.gamepad1.leftStickY.negate(),
            Gamepads.gamepad1.leftStickX.negate(),
            Gamepads.gamepad1.rightStickX.negate(),
            botCentric
        )
        driverControlled()
    }

    override fun onUpdate() {
        ActiveOpMode.telemetry.addData("x", follower.pose.x)
        ActiveOpMode.telemetry.addData("y", follower.pose.y)
        ActiveOpMode.telemetry.addData("heading", follower.pose.heading)
//        val normalizedColors = sensor.normalizedColors
//        ActiveOpMode.telemetry.addData("red", normalizedColors.red)
//        ActiveOpMode.telemetry.addData("green", normalizedColors.green)
//        ActiveOpMode.telemetry.addData("blue", normalizedColors.blue)
//        ActiveOpMode.telemetry.addData("alpha", normalizedColors.alpha)
//
//
//        ActiveOpMode.telemetry.addData("normRed", normalizedColors.red / normalizedColors.alpha)
//        ActiveOpMode.telemetry.addData("normGreen", normalizedColors.green / normalizedColors.alpha)
//        ActiveOpMode.telemetry.addData("normBlue", normalizedColors.blue / normalizedColors.alpha)
        ActiveOpMode.telemetry.update()
        BindingManager.update()
    }

    override fun onStop() {
        BindingManager.reset()
    }

    override fun onWaitForStart() {
        ActiveOpMode.telemetry.update()
    }
}