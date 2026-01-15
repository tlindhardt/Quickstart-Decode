package org.firstinspires.ftc.teamcode.hivemind.tele

import dev.nextftc.bindings.BindingManager
import dev.nextftc.core.commands.delays.Delay
import dev.nextftc.core.commands.groups.ParallelGroup
import dev.nextftc.core.commands.groups.SequentialGroup
import dev.nextftc.core.components.BindingsComponent
import dev.nextftc.core.components.SubsystemComponent
import dev.nextftc.extensions.pedro.PedroComponent
import dev.nextftc.extensions.pedro.PedroDriverControlled
import dev.nextftc.ftc.ActiveOpMode
import dev.nextftc.ftc.Gamepads
import dev.nextftc.ftc.NextFTCOpMode
import dev.nextftc.ftc.components.BulkReadComponent
import org.firstinspires.ftc.teamcode.hivemind.pedroPathing.Constants
import org.firstinspires.ftc.teamcode.hivemind.subsystems.Flywheel
import org.firstinspires.ftc.teamcode.hivemind.subsystems.Fries
import org.firstinspires.ftc.teamcode.hivemind.subsystems.Hood
import org.firstinspires.ftc.teamcode.hivemind.subsystems.Intake
import kotlin.time.Duration.Companion.seconds

abstract class NextBaseTele(val botCentric: Boolean = true) : NextFTCOpMode() {

    init {
        addComponents(
            PedroComponent(Constants::createFollower),
            SubsystemComponent(Flywheel, Fries, Hood, Intake),
            BulkReadComponent,
            BindingsComponent
        )
    }

    override fun onStartButtonPressed() {
        // DRIVER CONTROLS
        Gamepads.gamepad1.rightTrigger.greaterThan(.3)
            .whenBecomesTrue { Intake.forward.schedule() }
            .whenBecomesFalse { Intake.off.schedule() }
        Gamepads.gamepad1.leftTrigger.greaterThan(.3)
            .whenBecomesTrue { Intake.reverse.schedule() }
            .whenBecomesFalse { Intake.off.schedule() }

        // SHOOTER CONTROLS
        Gamepads.gamepad2.rightTrigger.greaterThan(.3)
            .whenTrue { Flywheel.on.schedule() }
            .whenFalse { Flywheel.off.schedule() }
        Gamepads.gamepad2.square
//        Gamepads.gamepad2.square.and(Gamepads.gamepad2.rightTrigger.greaterThan(0.3))
            .whenTrue {
                SequentialGroup(
                    Fries.fireLeft,
                    Delay(0.2.seconds),
                    Fries.intakeLeft
                ).schedule()
            }
        Gamepads.gamepad2.triangle
//        Gamepads.gamepad2.triangle.and(Gamepads.gamepad2.rightTrigger.greaterThan(0.3))
            .whenTrue {
                SequentialGroup(
                    Fries.fireCenter,
                    Delay(0.2.seconds),
                    Fries.intakeCenter
                ).schedule()
            }
        Gamepads.gamepad2.circle
//        Gamepads.gamepad2.circle.and(Gamepads.gamepad2.rightTrigger.greaterThan(0.3))
            .whenTrue {
                SequentialGroup(
                    Fries.fireRight,
                    Delay(0.2.seconds),
                    Fries.intakeRight
                ).schedule()
            }
        Gamepads.gamepad2.cross
//        Gamepads.gamepad2.cross.and(Gamepads.gamepad2.rightTrigger.greaterThan(0.3))
            .whenTrue {
                ParallelGroup(
                    Intake.off,
                    SequentialGroup(
                        Fries.fireAll,
                        Delay(0.2.seconds),
                        Fries.intakeAll
                    )
                ).schedule()
            }

        // DELETE ME
        Gamepads.gamepad2.dpadLeft
            .whenTrue { Hood.open.schedule() }
        Gamepads.gamepad2.dpadRight
            .whenTrue { Hood.close.schedule() }
        Gamepads.gamepad2.leftBumper
            .whenTrue { Intake.forward.schedule() }
        Gamepads.gamepad2.rightBumper
            .whenTrue { Intake.off.schedule() }
    }

    override fun onInit() {
        val driverControlled = PedroDriverControlled(
            Gamepads.gamepad1.leftStickY.negate(),
            Gamepads.gamepad1.leftStickX.negate(),
            Gamepads.gamepad1.rightStickX.negate(),
            botCentric
        )
        driverControlled()
    }

    override fun onUpdate() {
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