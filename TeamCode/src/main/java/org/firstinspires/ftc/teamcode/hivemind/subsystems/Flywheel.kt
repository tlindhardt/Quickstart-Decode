package org.firstinspires.ftc.teamcode.hivemind.subsystems

import com.bylazar.configurables.annotations.Configurable
import com.pedropathing.geometry.Pose
import dev.nextftc.control.ControlSystem
import dev.nextftc.control.KineticState
import dev.nextftc.control.builder.controlSystem
import dev.nextftc.control.feedback.PIDCoefficients
import dev.nextftc.control.feedforward.BasicFeedforwardParameters
import dev.nextftc.core.commands.Command
import dev.nextftc.core.commands.utility.InstantCommand
import dev.nextftc.core.subsystems.Subsystem
import dev.nextftc.extensions.pedro.PedroComponent.Companion.follower
import dev.nextftc.ftc.ActiveOpMode
import dev.nextftc.ftc.Gamepads
import dev.nextftc.hardware.controllable.MotorGroup
import dev.nextftc.hardware.impl.MotorEx
import kotlin.math.abs
import kotlin.math.hypot


@Configurable
object Flywheel : Subsystem {

    var isBlue = true

    var useAuto = false

    var desiredPower = OFF

    enum class Depot(val x: Double, val y: Double) {
        RED(144.0, 144.0),
        BLUE(0.0, 144.0)
    }

    private const val OFF = 0.0
    private const val AUTONOMOUS = 1050.0
    private const val CLOSE = 1050.0
    private const val MID = 1150.0
    private const val LONG = 1350.0

    @JvmField
    var velPidCoefficients = PIDCoefficients(0.01, 0.0, 0.0)  // P, I, D for velocity PID

    @JvmField
    var basicFFCoefficients = BasicFeedforwardParameters(0.0005, 0.02, 0.0)  // kS, kV, kA

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

    val off = InstantCommand {
        useAuto = false
        setTargetVelocity(OFF)
    }

    val autonomous = InstantCommand {
        useAuto = false
        setTargetVelocity(AUTONOMOUS)
    }

    val long = InstantCommand {
        useAuto = false
        setTargetVelocity(LONG)
    }

    val auto: (Boolean) -> Command = {
        InstantCommand {
            isBlue = it
            useAuto = true
        }
    }

    override fun periodic() {
        if (useAuto) {
            val depot: Depot = if (isBlue) Depot.BLUE else Depot.RED
            val distance = calculateDistance(follower.pose, depot)
            setTargetVelocity(calculateRpm(distance))
        }
        flywheel.power = controller.calculate(flywheel.state)
        ActiveOpMode.telemetry.addData("velocity", flywheel.velocity)
        ActiveOpMode.telemetry.addData("desiredPower", desiredPower)
        if (desiredPower > OFF && (flywheel.velocity >= desiredPower * 0.99)) {
            Gamepads.gamepad2.gamepad().rumble(100)
        }
    }

    override fun initialize() {
        flywheel.power = OFF
    }

    fun setTargetVelocity(targetVelocity: Double) {
        desiredPower = targetVelocity
        controller.goal = KineticState(0.0, targetVelocity, 0.0)
    }

    private fun calculateDistance(pose: Pose, depot: Depot): Double {
        return hypot(abs(depot.x - pose.x), abs(depot.y - pose.y))
    }

    private fun calculateRpm(distance: Double): Double {
//        if (distance > 120) {
//            return LONG
//        } else if (distance > 90) {
//            return MID
//        }
        return CLOSE
    }
}
