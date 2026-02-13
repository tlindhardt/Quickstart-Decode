package org.firstinspires.ftc.teamcode.hivemind.subsystems

import dev.nextftc.core.subsystems.Subsystem
import dev.nextftc.ftc.ActiveOpMode
import dev.nextftc.ftc.ActiveOpMode.hardwareMap
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.vision.VisionPortal
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor

object Camera : Subsystem {

    lateinit var aprilTag: AprilTagProcessor
    lateinit var visionPortal: VisionPortal

    var obeliskOrder: List<Color> = listOf(Color.PURPLE, Color.GREEN, Color.PURPLE)
    var orderFound = false
    var readyToRead = false

    override fun initialize() {
        aprilTag = AprilTagProcessor.easyCreateWithDefaults();
        visionPortal = VisionPortal.easyCreateWithDefaults(hardwareMap.get(WebcamName::class.java, "camera"), aprilTag)
    }

    override fun periodic() {
        ActiveOpMode.telemetry.addData("readToRun", readyToRead)
        ActiveOpMode.telemetry.addData("orderFound", orderFound)
        ActiveOpMode.telemetry.addData("order", obeliskOrder)
        if (readyToRead) {
            if (!orderFound) {
                val currentDetections: List<AprilTagDetection> = aprilTag.detections
                for (detection in currentDetections) {
                    if (detection.id == 21 || detection.id == 22 || detection.id == 23) {
                        if (detection.id == 21) {
                            obeliskOrder = listOf(Color.GREEN, Color.PURPLE, Color.PURPLE)
                        } else if (detection.id == 22) {
                            obeliskOrder = listOf(Color.PURPLE, Color.GREEN, Color.PURPLE)
                        } else {
                            obeliskOrder = listOf(Color.PURPLE, Color.PURPLE, Color.GREEN)
                        }
                        orderFound = true
                    }
                }
            } else {
                visionPortal.stopStreaming()
            }
        }
    }
}