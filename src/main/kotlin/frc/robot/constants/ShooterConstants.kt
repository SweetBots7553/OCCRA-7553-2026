package frc.robot.constants

import com.revrobotics.spark.config.SparkBaseConfig
import com.revrobotics.spark.config.SparkMaxConfig
import edu.wpi.first.units.Units
import edu.wpi.first.units.measure.Distance
import kotlin.math.PI

const val kShooterMotorCANID = 6

// m/s
const val kShooterSpeed = 3.0

private val kShooterWheelDiameter = Distance.ofBaseUnits(10.0, Units.Millimeter)
private const val kShooterGearRatio = 1/1

// likely to be a sparkmax, but idk 
val kShooterConfig: SparkMaxConfig
    get() {
        val config = SparkMaxConfig()
        
        config.smartCurrentLimit(45)
        
        config.closedLoop
            .maxOutput(1.0)
            .pid(0.45, 0.0, 0.20) 
            
        val positionFactor = PI * kShooterWheelDiameter.`in`(Units.Meters) / kShooterGearRatio
            
        config.encoder
            .positionConversionFactor(positionFactor)
            .velocityConversionFactor(positionFactor / 60)
        
        
        config.idleMode(SparkBaseConfig.IdleMode.kCoast)    
        
        return config
        
    }

const val kIndexerMotorCANID = 7
const val kIndexerSpeed = 3.0

private val kIndexerWheelDiameter = Distance.ofBaseUnits(10.0, Units.Millimeters)
private const val kIndexerGearRatio = 1/1

val kIndexerConfig: SparkMaxConfig
    get() {
        val config = SparkMaxConfig()
        
        config.smartCurrentLimit(45)
        
        config.closedLoop
            .maxOutput(1.0)
            .pid(0.45, 0.0, 0.20) 
            
        val positionFactor = PI * kIndexerWheelDiameter.`in`(Units.Meters) / kIndexerGearRatio
            
        config.encoder
            .positionConversionFactor(positionFactor)
            .velocityConversionFactor(positionFactor / 60)
        
        
        config.idleMode(SparkBaseConfig.IdleMode.kCoast)    
        
        return config
        
    }

