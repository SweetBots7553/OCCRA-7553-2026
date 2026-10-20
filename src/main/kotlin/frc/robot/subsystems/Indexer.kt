package frc.robot.subsystems

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX
import edu.wpi.first.wpilibj.AnalogInput
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.constants.IndexerConstants

object Indexer : SubsystemBase() {

    private val indexerMotor = WPI_VictorSPX(IndexerConstants.kIndexerMotorCANID)
    
    // private val ballSensor = AnalogInput(IndexerConstants.kIndexerSensorChannel)

    init {
        indexerMotor.configAllSettings(IndexerConstants.kIndexerConfig)
        indexerMotor.inverted = true
    }

    private fun index(dutycycle: Double) {
        indexerMotor.set(dutycycle)
    }
    
    // fun ballDetected(): Boolean = ballSensor.value != 0
       
    fun index(dutycycle: () -> Double): Command {
        return runOnce { 
            index ( dutycycle() )
        }
    }
    
    fun stop(): Command {
        return runOnce { 
            index { 0.0 }
        }
    }

    override fun periodic() {
        SmartDashboard.putNumber("Indexer/Indexer_Motor", indexerMotor.get())
    }
}