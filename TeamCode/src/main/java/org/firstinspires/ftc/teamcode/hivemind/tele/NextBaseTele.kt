package org.firstinspires.ftc.teamcode.hivemind.tele

import dev.nextftc.core.commands.delays.Delay
import dev.nextftc.core.commands.groups.SequentialGroup
import dev.nextftc.core.commands.utility.InstantCommand
import dev.nextftc.core.components.BindingsComponent
import dev.nextftc.core.components.SubsystemComponent
import dev.nextftc.extensions.pedro.PedroComponent
import dev.nextftc.extensions.pedro.PedroDriverControlled
import dev.nextftc.ftc.ActiveOpMode
import dev.nextftc.ftc.Gamepads
import dev.nextftc.ftc.NextFTCOpMode
import dev.nextftc.ftc.components.BulkReadComponent
import org.firstinspires.ftc.teamcode.hivemind.pedroPathing.Constants
import org.firstinspires.ftc.teamcode.hivemind.subsystems.Feeder
import org.firstinspires.ftc.teamcode.hivemind.subsystems.Shooter
import kotlin.time.Duration.Companion.seconds

abstract class NextBaseTele(val botCentric: Boolean = true) : NextFTCOpMode() {
    var isShooting: Boolean = false

    init {
        addComponents(
            PedroComponent(Constants::createFollower),
            SubsystemComponent(Shooter, Feeder),
            BulkReadComponent,
            BindingsComponent
        )
    }

    override fun onStartButtonPressed() {
        // Turn on shooter if pressed 30% at least
        Gamepads.gamepad2.rightTrigger
            .greaterThan(.3)
            .whenBecomesTrue { Shooter.on.schedule() }
            .whenBecomesFalse { Shooter.off.schedule() }

        // Fire shot if shooter is running and if the fire button is pressed.
        // Only allow 1 shot at a time and have a .3 second wait between shots.
        (Gamepads.gamepad2.rightTrigger.greaterThan(0.3) and Gamepads.gamepad2.a)
            .whenBecomesTrue {
                if (!isShooting) {
                    SequentialGroup(
                        InstantCommand { isShooting = true },
                        Feeder.open,
                        Delay(0.2.seconds),
                        Feeder.close,
                        Delay(0.3.seconds),
                        InstantCommand { isShooting = false }
                    ).schedule()
                }
            }
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
    }

    override fun onWaitForStart() {
        ActiveOpMode.telemetry.update()
    }
}