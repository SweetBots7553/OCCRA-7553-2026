package frc.robot.constants

import com.revrobotics.spark.config.SparkBaseConfig
import com.revrobotics.spark.config.SparkMaxConfig
import edu.wpi.first.units.Units
import edu.wpi.first.units.measure.Distance
import kotlin.math.PI

// m/s
const val kSpeedFactor = 10.0

// this is the distance so It can calculate rotation. 
val kTrackWidth: Distance = Units.Millimeter.of(500.0)
private val kWheelDiameter: Distance = Units.Millimeters.of(154.4)
private const val kGearRatio = 10.75

// to be determined
const val kFrontLeftNeo = 23
const val kFrontRightNeo = 21
const val kBackLeftNeo = 24
const val kBackRightNeo = 22

// still need to pid tune

val kTrainConfig: SparkMaxConfig 
    get() {
        val config = SparkMaxConfig() 
        
        // So a motor doesn't overclock
        config.smartCurrentLimit(45)
        
        config.closedLoop
            .pid(0.1, 0.0, 0.0) 
            .maxOutput(1.0)
        
        val positionFactor = PI * kWheelDiameter.`in`(Units.Meters) / kGearRatio 
        
        config.encoder
            .positionConversionFactor(positionFactor)
            .velocityConversionFactor(positionFactor / 60) 
            

            
        config.idleMode(SparkBaseConfig.IdleMode.kCoast)
        
        return config 
    }
    
val kBackLeftConfig: SparkMaxConfig 
    get() {
        val config = kTrainConfig
        
        config.follow(kFrontLeftNeo)
        
        return config
    }
    
val kBackRightConfig: SparkMaxConfig
    get() {
        val config = kTrainConfig
        
        config.follow(kFrontRightNeo) 
        
        return config
    }