package frc.robot.constants

import com.ctre.phoenix.motorcontrol.can.VictorSPXConfiguration
import com.ctre.phoenix6.configs.TalonFXConfiguration
import com.revrobotics.spark.config.SparkBaseConfig
import com.revrobotics.spark.config.SparkMaxConfig
import edu.wpi.first.units.Units
import edu.wpi.first.units.measure.Distance
import kotlin.math.PI

// dutycycle
const val kIntakeSpeed = 0.1
const val kIntakeCANID = 5

val kIntakeConfig: VictorSPXConfiguration 
    get() {
        val config = VictorSPXConfiguration()
        
        
        
        return config
    }

const val kPneumaticUnoChannel = 0
const val kPneumaticDosChannel = 1



