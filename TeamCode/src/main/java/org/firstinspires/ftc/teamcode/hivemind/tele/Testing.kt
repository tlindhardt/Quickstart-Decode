package org.firstinspires.ftc.teamcode.hivemind.tele

import com.bylazar.telemetry.PanelsTelemetry
import com.qualcomm.robotcore.hardware.VoltageSensor
import dev.nextftc.core.components.BindingsComponent
import dev.nextftc.core.components.SubsystemComponent
import dev.nextftc.ftc.ActiveOpMode
import dev.nextftc.ftc.NextFTCOpMode
import dev.nextftc.ftc.components.BulkReadComponent
import org.firstinspires.ftc.teamcode.hivemind.subsystems.Flywheel

abstract class Testing() : NextFTCOpMode() {
    val panelsTelemetry = PanelsTelemetry.telemetry

    private var batteryVoltageSensor: VoltageSensor? = null
    val tuningController = TuningController()

    init {
        addComponents(
            SubsystemComponent(Flywheel),
            BulkReadComponent,
            BindingsComponent
        )
    }

    override fun onInit() {
        batteryVoltageSensor = hardwareMap.voltageSensor.iterator().next()
        Flywheel.top.schedule()
    }

    override fun onUpdate() {

        val targetVelo: Double = tuningController.update()
        Flywheel.velocity = targetVelo

        panelsTelemetry.addData("targetVelocity", targetVelo)
        panelsTelemetry.addData("velocity", Flywheel.flywheel.velocity)
        panelsTelemetry.addData("error", targetVelo - Flywheel.flywheel.velocity)

        panelsTelemetry.addData(
            "upperBound",
            TuningController.rpmToTicksPerSecond(TuningController.TESTING_MAX_SPEED * 1.15)
        )
        panelsTelemetry.addData("lowerBound", 0)

        tuningController.update()
        panelsTelemetry.update()
        ActiveOpMode.telemetry.update()
    }

    override fun onWaitForStart() {
        ActiveOpMode.telemetry.update()
    }
}