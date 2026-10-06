package frc.robot.constants

import com.revrobotics.spark.config.SparkMaxConfig
import edu.wpi.first.units.Units
import edu.wpi.first.units.measure.Distance

// this is the distance so It can calculate rotation. 
val kTrackWidth: Distance = Distance.ofBaseUnits(490.0, Units.Millimeter)

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
        
        config.closedLoop.pid(0.0, 0.0, 0.0) 
        
        
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