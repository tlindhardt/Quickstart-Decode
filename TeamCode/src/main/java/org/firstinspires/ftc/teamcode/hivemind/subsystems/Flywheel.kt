package org.firstinspires.ftc.teamcode.hivemind.subsystems

import com.bylazar.configurables.annotations.Configurable
import dev.nextftc.control.ControlSystem
import dev.nextftc.control.KineticState
import dev.nextftc.control.builder.controlSystem
import dev.nextftc.control.feedback.PIDCoefficients
import dev.nextftc.control.feedforward.BasicFeedforwardParameters
import dev.nextftc.core.subsystems.Subsystem
import dev.nextftc.hardware.controllable.MotorGroup
import dev.nextftc.hardware.controllable.RunToVelocity
import dev.nextftc.hardware.impl.MotorEx


@Configurable
object Flywheel : Subsystem {
    private const val OFF = 0.0
    private const val ON = 1000.0

    @JvmField
    var velPidCoefficients = PIDCoefficients(0.005, 0.0, 0.0)  // P, I, D for velocity PID

    @JvmField
    var basicFFCoefficients = BasicFeedforwardParameters(0.01, 0.02, 0.03)  // kS, kV, kA

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
            controller.goal = KineticState(0.0, value, 0.0)
        }

    val off = RunToVelocity(controller, OFF).requires(this)
    val on = RunToVelocity(controller, ON).requires(this)

    override fun periodic() {
        flywheel.power = controller.calculate(flywheel.state)
    }

    override fun initialize() {
        flywheel.power = OFF
    }
}
