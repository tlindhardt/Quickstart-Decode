package org.firstinspires.ftc.teamcode.hivemind.tele

import dev.nextftc.bindings.BindingManager
import dev.nextftc.core.commands.delays.Delay
import dev.nextftc.core.commands.groups.SequentialGroup
import dev.nextftc.core.components.BindingsComponent
import dev.nextftc.core.components.SubsystemComponent
import dev.nextftc.extensions.pedro.PedroComponent
import dev.nextftc.ftc.ActiveOpMode
import dev.nextftc.ftc.Gamepads
import dev.nextftc.ftc.NextFTCOpMode
import dev.nextftc.ftc.components.BulkReadComponent
import org.firstinspires.ftc.teamcode.hivemind.pedroPathing.Constants
import org.firstinspires.ftc.teamcode.hivemind.subsystems.*
import kotlin.time.Duration.Companion.seconds

abstract class NextBaseTele(val isBlue: Boolean) : NextFTCOpMode() {

    lateinit var driverControlled: AutoShootPedroDriverControlled

    init {
        addComponents(
            PedroComponent(Constants::createFollower),
            SubsystemComponent(Camera, ColorSensors, Flywheel, Fries, Intake, Lights),
            BulkReadComponent,
            BindingsComponent
        )
    }

    override fun onStartButtonPressed() {
        Camera.readyToRead = true
        Fries.startRunning.schedule()
        Gamepads.gamepad1.dpadUp
            .whenBecomesTrue { driverControlled.x++ }
        Gamepads.gamepad1.dpadDown
            .whenBecomesTrue { driverControlled.x-- }
        Gamepads.gamepad1.dpadRight
            .whenBecomesTrue { driverControlled.y++ }
        Gamepads.gamepad1.dpadLeft
            .whenBecomesTrue { driverControlled.y-- }

        // SHOOTER CONTROLS
        Gamepads.gamepad2.rightBumper
            .whenBecomesTrue { Intake.forward.schedule() }
            .whenBecomesFalse { Intake.off.schedule() }
        Gamepads.gamepad2.leftBumper
            .whenBecomesTrue { Intake.reverse.schedule() }
            .whenBecomesFalse { Intake.off.schedule() }
        Gamepads.gamepad2.rightTrigger.greaterThan(.3)
            .whenBecomesTrue { Flywheel.auto(isBlue).schedule() }
            .whenBecomesFalse { Flywheel.off.schedule() }
//        Gamepads.gamepad2.leftTrigger.greaterThan(.3)
//            .whenBecomesTrue { Flywheel.close.schedule() }
//            .whenBecomesFalse { Flywheel.off.schedule() }
        Gamepads.gamepad2.cross.and(Gamepads.gamepad2.rightTrigger.greaterThan(0.3))
            .whenBecomesTrue {
                Fries.fireAllSorted(false).schedule()
            }
        Gamepads.gamepad2.square.and(Gamepads.gamepad2.rightTrigger.greaterThan(0.3))
            .whenBecomesTrue {
                SequentialGroup(
                    Fries.startShooting,
                    Fries.fireLeft,
                    Delay(0.2.seconds),
                    Fries.intakeLeft,
                    Fries.endShooting
                ).schedule()
            }
        Gamepads.gamepad2.triangle.and(Gamepads.gamepad2.rightTrigger.greaterThan(0.3))
            .whenBecomesTrue {
                SequentialGroup(
                    Fries.startShooting,
                    Fries.fireCenter,
                    Delay(0.2.seconds),
                    Fries.intakeCenter,
                    Fries.endShooting
                ).schedule()
            }
        Gamepads.gamepad2.circle.and(Gamepads.gamepad2.rightTrigger.greaterThan(0.3))
            .whenBecomesTrue {
                SequentialGroup(
                    Fries.startShooting,
                    Fries.fireRight,
                    Delay(0.2.seconds),
                    Fries.intakeRight,
                    Fries.endShooting
                ).schedule()
            }
    }

    override fun onInit() {
        Camera.readyToRead = false
        driverControlled = AutoShootPedroDriverControlled(
            Gamepads.gamepad1.leftStickY.negate(),
            Gamepads.gamepad1.leftStickX.negate(),
            Gamepads.gamepad1.rightStickX.negate(),
            Gamepads.gamepad1.cross,
            false,
            isBlue
        )
        driverControlled()
    }

    override fun onUpdate() {
        ActiveOpMode.telemetry.update()
        BindingManager.update()
    }

    override fun onStop() {
        BindingManager.reset()
        Flywheel.off.schedule()
        Fries.intakeAll.schedule()
        Intake.off.schedule()
        Fries.stopRunning.schedule()
        Fries.endShooting.schedule()
        driverControlled.stop(true)
        Camera.orderFound = false
        Fries.hasStarted = false
    }

    override fun onWaitForStart() {
        ActiveOpMode.telemetry.update()
    }
}