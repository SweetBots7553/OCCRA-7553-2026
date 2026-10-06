package frc.robot.constants

import kotlin.math.abs

const val kDriverPort = 0
const val kCoDriverPort = 1

const val kDeadbandThreshold = 0.2

fun deadbandOutput(axis: Double): Double {    
    if (abs(axis ) > kDeadbandThreshold) {
        return axis
    }

    return 0.0
}