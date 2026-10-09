package frc.robot.constants

import com.ctre.phoenix.motorcontrol.can.VictorSPXConfiguration
import com.revrobotics.spark.config.SparkBaseConfig
import com.revrobotics.spark.config.SparkMaxConfig
import edu.wpi.first.units.Units
import edu.wpi.first.units.measure.Distance
import kotlin.math.PI

const val kIndexerMotorCANID = 7

val kIndexerConfig: VictorSPXConfiguration
    get() {
        val config = VictorSPXConfiguration()
        
        return config
    }