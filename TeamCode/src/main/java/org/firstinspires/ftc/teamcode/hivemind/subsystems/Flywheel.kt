package org.firstinspires.ftc.teamcode.hivemind.subsystems

import com.bylazar.configurables.annotations.Configurable
import dev.nextftc.control.ControlSystem
import dev.nextftc.control.KineticState
import dev.nextftc.control.builder.controlSystem
import dev.nextftc.control.feedback.PIDCoefficients
import dev.nextftc.control.feedforward.BasicFeedforwardParameters
import dev.nextftc.core.commands.utility.InstantCommand
import dev.nextftc.core.subsystems.Subsystem
import dev.nextftc.ftc.ActiveOpMode.telemetry
import dev.nextftc.hardware.controllable.MotorGroup
import dev.nextftc.hardware.impl.MotorEx


@Configurable
object Flywheel : Subsystem {
    private const val OFF = 0.0
    private const val TOP = 1050.0

    @JvmField
    var velPidCoefficients = PIDCoefficients(0.007, 0.0, 0.0)  // P, I, D for velocity PID

    @JvmField
    var basicFFCoefficients = BasicFeedforwardParameters(0.0009, 0.02, 0.0)  // kS, kV, kA

    var flywheel = MotorGroup(
        MotorEx("flywheel_left"),
        MotorEx("flywheel_right")
    )

    private val controller: ControlSystem = controlSystem {
        velPid(velPidCoefficients)
        basicFF(basicFFCoefficients)
    }

    var velocity: Double = 0.0
        set(value) {
            field = value.coerceIn(0.0, 1800.0)
            setTargetVelocity(field)
        }

    val off = InstantCommand { setTargetVelocity(OFF) }

    val top = InstantCommand { setTargetVelocity(TOP) }

    override fun periodic() {
        flywheel.power = controller.calculate(flywheel.state)
        telemetry.addData("Velocity", flywheel.velocity)
    }

    override fun initialize() {
        flywheel.power = OFF
    }

    fun setTargetVelocity(targetVelocity: Double) {
        controller.goal = KineticState(0.0, targetVelocity, 0.0)
    }
}
