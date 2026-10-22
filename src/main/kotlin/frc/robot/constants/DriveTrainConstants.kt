package frc.robot.constants

import com.revrobotics.spark.config.SparkBaseConfig
import com.revrobotics.spark.config.SparkMaxConfig
import edu.wpi.first.units.Units
import edu.wpi.first.units.measure.Distance
import kotlin.math.PI

object DriveTrainConstants {
    // TODO: need to get rev logs from Advantage scope, and use sysid to get the feedforwards, also fix gear ratio, and diameter. 

    // m/s
    const val kSpeedFactor = 5.0
    const val kRotationPerSecond = 8.0 

    // this is the distance so It can calculate rotation. 
    val kTrackWidth: Distance = Units.Millimeter.of(500.0)
    private val kWheelDiameter: Distance = Units.Inches.of(6.0)
    private const val kGearRatio = 10.75

    const val kFrontLeftNeoCANID = 23
    const val kFrontRightNeoCANID = 21
    const val kBackLeftNeoCANID = 24
    const val kBackRightNeoCANID = 22

    // still need to pid tune

    val kFrontConfig: SparkMaxConfig
        get() {
            val config = SparkMaxConfig()
            // config.inverted(true)

            // So a motor doesn't overclock
            config.smartCurrentLimit(55)

            config.closedLoop
                .pid(0.3, 0.0, 0.005)
                .maxOutput(1.0)
                .Kv(1)
                .Ka(1)
                
                

            val positionFactor = PI * kWheelDiameter.`in`(Units.Meters) / kGearRatio

            config.encoder
                .positionConversionFactor(positionFactor)
                .velocityConversionFactor(positionFactor / 60.0)

            config.idleMode(SparkBaseConfig.IdleMode.kCoast)

            return config
        }
        
    val kFrontLeftConfig: SparkBaseConfig
        get() {
            val config = SparkMaxConfig()
            config.apply(kFrontConfig)
        
             config.inverted(true)
            
            
            return config
        } 

    val kBackLeftConfig: SparkMaxConfig
        get() {
            val config = SparkMaxConfig()
            config.apply(kFrontConfig)
            
            config.inverted(true)
            
            config.follow(kFrontLeftNeoCANID)
            
            
            return config
        }

    val kFrontRightConfig: SparkMaxConfig
        get() {
            val config = SparkMaxConfig()
            config.apply(kFrontConfig)
            
            
            
            return config
        }

        
    val kBackRightConfig: SparkMaxConfig
        get() {
            val config = SparkMaxConfig()
            config.apply(kFrontConfig)
            
    
    
            config.follow(kFrontRightNeoCANID)
            

            return config
        }
} 