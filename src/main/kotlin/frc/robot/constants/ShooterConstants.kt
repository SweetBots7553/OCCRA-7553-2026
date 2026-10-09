package frc.robot.constants

import com.revrobotics.spark.config.SparkBaseConfig
import com.revrobotics.spark.config.SparkMaxConfig
import edu.wpi.first.units.Units
import edu.wpi.first.units.measure.Distance
import kotlin.math.PI

const val kShooterMotorCANID = 6

// m/s
const val kShooterSpeed = 0.1
private val kShooterWheelDiameter = Distance.ofBaseUnits(90.0, Units.Millimeter)

// likely to be a sparkmax, but idk 
val kShooterConfig: SparkMaxConfig
    get() {
        val config = SparkMaxConfig()
        
        config.smartCurrentLimit(45)
        
        config.closedLoop
            .maxOutput(1.0)
            .pid(0.45, 0.0, 0.20) 
            
        val positionFactor = PI * kShooterWheelDiameter.`in`(Units.Meters) 
            
        config.encoder
            .positionConversionFactor(positionFactor)
            .velocityConversionFactor(positionFactor / 60)
        
        
        config.idleMode(SparkBaseConfig.IdleMode.kCoast)    
        
        return config
        
    }



