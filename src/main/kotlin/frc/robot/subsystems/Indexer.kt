package frc.robot.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.constants.kIndexerConfig
import frc.robot.constants.kIndexerMotorCANID

class Indexer : SubsystemBase() {
    
    private val indexerMotor = WPI_VictorSPX(kIndexerMotorCANID)
    
    init {
        indexerMotor.configAllSettings(kIndexerConfig)
    }
    
    fun index(speed: Double) {
        indexerMotor.set(ControlMode.Velocity, speed)   
    }
    
    fun index(speed: () -> Double): Command {
        return startEnd({
           index(speed()) 
        }, {
          index(0.0)  
        })
    }
    
}