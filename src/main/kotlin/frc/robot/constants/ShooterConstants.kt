package frc.robot.constants

import com.revrobotics.spark.config.SparkBaseConfig
import com.revrobotics.spark.config.SparkFlexConfig
import com.revrobotics.spark.config.SparkMaxConfig
import edu.wpi.first.units.Units
import edu.wpi.first.units.measure.Distance
import kotlin.math.PI

object ShooterConstants  {
    const val kShooterMotorCANID = 33

    // m/s 
    const val kShooterSpeed = 0.5
    private val kShooterWheelDiameter = Units.Millimeters.of(90.0)

    // likely to be a sparkmax, but idk 
    val kShooterConfig: SparkFlexConfig
        get() {
            val config = SparkFlexConfig()
            
            config.inverted(true)
            
            config.smartCurrentLimit(45)

            config.closedLoop
                .maxOutput(1.0)
                .pid(0.0, 0.0, 0.0)

            val positionFactor = PI * kShooterWheelDiameter.`in`(Units.Meters)

            config.encoder
                .positionConversionFactor(positionFactor)
                .velocityConversionFactor(positionFactor / 60.0)


            config.idleMode(SparkBaseConfig.IdleMode.kCoast)

            return config

        }
}
