package org.firstinspires.ftc.teamcode.hivemind.auto

import com.pedropathing.geometry.Pose
import com.pedropathing.paths.PathChain

data class Paths(
    val startPose: Pose,
    val initialShootPath: PathChain,
    val topSpike: Pair<PathChain, PathChain>,
    val topSpikeShootPath: PathChain,
    val bumpPath: PathChain,
    val centerSpike: Pair<PathChain, PathChain>,
    val centerSpikeShootPath: PathChain,
    val bottomSpike: Pair<PathChain, PathChain>,
    val bottomSpikeShootPath: PathChain,
    val load: Pair<PathChain, PathChain>,
    val loadShootPath: PathChain,
    val endPath: PathChain,
)