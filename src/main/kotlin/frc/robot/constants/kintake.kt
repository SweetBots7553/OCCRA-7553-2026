package frc.robot.constants

import com.revrobotics.spark.SparkMax
import com.revrobotics.spark.config.SparkBaseConfig
import com.revrobotics.spark.config.SparkMaxConfig
import edu.wpi.first.units.Units
import edu.wpi.first.units.measure.Distance
import kotlin.math.PI

// m/s
const val kIntakeSpeed = 3.0

const val kMotorCANID = 5

private val kIntakeWheelDiameter = Distance.ofBaseUnits(10.0, Units.Millimeter)
private const val kGearRatio = 1/1

const val kPneumaticUnoChannel = 0
const val kPneumaticDosChannel = 1

// likely to be a sparkmax, but idk 
val kMotorConfig: SparkMaxConfig
    get() {
        val config = SparkMaxConfig()
        
        config.smartCurrentLimit(45)
        
        config.closedLoop
            .maxOutput(1.0)
            .pid(0.45, 0.0, 0.20) 
            
        val positionFactor = PI * kIntakeWheelDiameter.`in`(Units.Meters) / kGearRatio
            
        config.encoder
            .positionConversionFactor(positionFactor)
            .velocityConversionFactor(positionFactor / 60)
        
        
        config.idleMode(SparkBaseConfig.IdleMode.kCoast)    
        
        return config
        
    }

