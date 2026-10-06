package frc.robot.subsystems

import com.revrobotics.spark.SparkLowLevel.MotorType
import com.revrobotics.spark.SparkMax
import edu.wpi.first.wpilibj.PneumaticsModuleType
import edu.wpi.first.wpilibj.Solenoid
import edu.wpi.first.wpilibj2.command.SubsystemBase

class Intake : SubsystemBase() {
    
    private val intakeMotor = SparkMax(0, MotorType.kBrushless)
    
    private val extender = Solenoid(PneumaticsModuleType.CTREPCM, 0)
    private val extenderTwin = Solenoid(PneumaticsModuleType.CTREPCM, 0)
    
    init {
        // intakeMotor.configure()
    }
    
    
    
}