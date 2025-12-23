import com.bylazar.configurables.annotations.Configurable
import com.bylazar.telemetry.PanelsTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.*
import org.firstinspires.ftc.teamcode.TuningController

@Configurable
@TeleOp
class FlywheelTuning : LinearOpMode() {
    val panelsTelemetry = PanelsTelemetry.telemetry

    private var batteryVoltageSensor: VoltageSensor? = null

    @Throws(InterruptedException::class)
    override fun runOpMode() {
        // Change my id
        val myMotor = hardwareMap.get(DcMotorEx::class.java, "shooter")
        myMotor.direction = DcMotorSimple.Direction.REVERSE
        myMotor.mode = DcMotor.RunMode.RUN_USING_ENCODER
        val tuningController = TuningController()

        batteryVoltageSensor = hardwareMap.voltageSensor.iterator().next()
        setPIDFCoefficients(myMotor, MOTOR_VELO_PID)

        var lastKp = 0.0
        var lastKi = 0.0
        var lastKd = 0.0
        var lastKf = motorVelocityF

        panelsTelemetry.addLine("Ready!")
        panelsTelemetry.update()

        waitForStart()

        while (opModeIsActive()) {
            val targetVelo: Double = tuningController.update()
            myMotor.velocity = targetVelo

            panelsTelemetry.addData("targetVelocity", targetVelo)
            panelsTelemetry.addData("velocity", myMotor.velocity)
            panelsTelemetry.addData("error", targetVelo - myMotor.velocity)

            panelsTelemetry.addData(
                "upperBound",
                TuningController.rpmToTicksPerSecond(TuningController.TESTING_MAX_SPEED * 1.15)
            )
            panelsTelemetry.addData("lowerBound", 0)

            if (lastKp != MOTOR_VELO_PID.p || lastKi != MOTOR_VELO_PID.i || lastKd != MOTOR_VELO_PID.d || lastKf != MOTOR_VELO_PID.f) {
                setPIDFCoefficients(myMotor, MOTOR_VELO_PID)

                lastKp = MOTOR_VELO_PID.p
                lastKi = MOTOR_VELO_PID.i
                lastKd = MOTOR_VELO_PID.d
                lastKf = MOTOR_VELO_PID.f
            }

            tuningController.update()
            panelsTelemetry.update()
        }
    }

    private fun setPIDFCoefficients(motor: DcMotorEx, coefficients: PIDFCoefficients) {
        motor.setPIDFCoefficients(
            DcMotor.RunMode.RUN_USING_ENCODER, PIDFCoefficients(
                coefficients.p,
                coefficients.i,
                coefficients.d,
                coefficients.f * 12 / batteryVoltageSensor!!.getVoltage()
            )
        )
    }

    companion object {
        var MOTOR_VELO_PID: PIDFCoefficients = PIDFCoefficients(0.0, 0.0, 0.0, 0.0)

        val motorVelocityF: Double
            get() =// see https://docs.google.com/document/d/1tyWrXDfMidwYyP_5H4mZyVgaEswhOC35gvdmP-V-5hA/edit#heading=h.61g9ixenznbx
                32767 * 60.0 / (TuningController.MOTOR_MAX_RPM * TuningController.MOTOR_TICKS_PER_REV)
    }
}