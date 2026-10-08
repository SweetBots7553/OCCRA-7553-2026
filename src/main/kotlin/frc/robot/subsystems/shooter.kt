package frc.robot.subsystems

import com.revrobotics.spark.SparkLowLevel
import com.revrobotics.spark.SparkMax
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.constants.kIndexerMotorCANID
import frc.robot.constants.kShooterMotorCANID

class Shooter : SubsystemBase() {
    
    private val shooterMotor = SparkMax(kShooterMotorCANID, SparkLowLevel.MotorType.kBrushless)
    private val indexerMotor = SparkMax(kIndexerMotorCANID, SparkLowLevel.MotorType.kBrushless)
    
    init {
        
        
        
        
    } 
    
    
}