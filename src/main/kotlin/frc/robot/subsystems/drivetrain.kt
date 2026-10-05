package frc.robot.subsystems

import com.revrobotics.spark.SparkLowLevel.MotorType
import com.revrobotics.spark.SparkMax
import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics
import edu.wpi.first.units.Units
import edu.wpi.first.units.measure.Distance

class DriveTrain {
     
    private val kinematics = DifferentialDriveKinematics(Distance.ofBaseUnits(490.0, Units.Millimeter))
    
    private val frontLeftNeo: SparkMax = SparkMax(1, MotorType.kBrushless)
    private val frontRightNeo: SparkMax = SparkMax(2, MotorType.kBrushless)
    private val backLeftNeo: SparkMax = SparkMax(3, MotorType.kBrushless)
    private val backRightNeo: SparkMax = SparkMax(4, MotorType.kBrushless)
    
    
    init { 
        
    }
    
    fun drive(speeds: ChassisSpeeds) {
        val v = kinematics.toWheelSpeeds(speeds)
        
        
        
        
    }
    
    
}