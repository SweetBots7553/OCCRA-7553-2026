package frc.robot.subsystems

import com.revrobotics.spark.SparkMax
import edu.wpi.first.wpilibj.PneumaticsModuleType
import edu.wpi.first.wpilibj.Solenoid
import edu.wpi.first.wpilibj2.command.SubsystemBase

class intake : SubsystemBase()  {
    
    private val intakeMotor = null
    
    private val penumaticUno = Solenoid(PneumaticsModuleType.CTREPCM, 0)
    private val penumaticDos = Solenoid(PneumaticsModuleType.CTREPCM, 1)
    
    
}