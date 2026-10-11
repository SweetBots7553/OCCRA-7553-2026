package frc.robot.constants

import com.ctre.phoenix.motorcontrol.can.VictorSPXConfiguration
import edu.wpi.first.units.Units
import edu.wpi.first.units.measure.Distance
import kotlin.math.PI

const val kIndexerMotorCANID = 7

val kIndexerConfig: VictorSPXConfiguration
    get() {
        val config = VictorSPXConfiguration()
        val pid = config.slot0
        
        pid.kP = 0.45
        pid.kI = 0.0
        pid.kD = 0.25
        pid.kF = 0.0 
        
        config.slot0 = pid
        return config
    }