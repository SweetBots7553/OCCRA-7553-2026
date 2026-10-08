package frc.robot.constants

import com.revrobotics.spark.config.SparkBaseConfig
import com.revrobotics.spark.config.SparkMaxConfig
import edu.wpi.first.units.Units
import edu.wpi.first.units.measure.Distance
<<<<<<< HEAD
import kotlin.math.PI

// m/s
const val kSpeedFactor = 10.0

// this is the distance so It can calculate rotation. 
val kTrackWidth: Distance = Distance.ofBaseUnits(500.0, Units.Millimeter)
private val kWheelDiameter: Distance = Distance.ofBaseUnits(154.4, Units.Millimeter)  
private const val kGearRatio = 10.75
=======

// this is the distance so It can calculate rotation. 
val kTrackWidth: Distance = Distance.ofBaseUnits(490.0, Units.Millimeter)
>>>>>>> refs/remotes/origin/main

// to be determined
const val kFrontLeftNeo = 0
const val kFrontRightNeo = 1
const val kBackLeftNeo = 2
const val kBackRightNeo = 3

// still need to pid tune

val kTrainConfig: SparkMaxConfig 
    get() {
        val config = SparkMaxConfig() 
        
        // So a motor doesn't overclock
        config.smartCurrentLimit(45)
<<<<<<< HEAD
        
        config.closedLoop
            .pid(0.45, 0.0, 0.25) 
            .maxOutput(1.0)
        
        val positionFactor = PI * kWheelDiameter.`in`(Units.Meters) / kGearRatio 
        
        config.encoder
            .positionConversionFactor(positionFactor)
            .velocityConversionFactor(positionFactor / 60) 
            
        config.idleMode(SparkBaseConfig.IdleMode.kCoast)
        
=======
        config.idleMode(SparkBaseConfig.IdleMode.kCoast)
        
        
        
        
        
        
        config.closedLoop.pid(0.0, 0.0, 0.0) 
        
        
>>>>>>> refs/remotes/origin/main
        return config 
    }
    
val kBackLeftConfig: SparkMaxConfig 
    get() {
        val config = kTrainConfig
        
        config.follow(kFrontLeftNeo)
        
<<<<<<< HEAD
=======
        
>>>>>>> refs/remotes/origin/main
        return config
    }
    
val kBackRightConfig: SparkMaxConfig
    get() {
        val config = kTrainConfig
        
        config.follow(kFrontRightNeo) 
        
        return config
    }