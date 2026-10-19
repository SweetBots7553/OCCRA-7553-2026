package frc.robot.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX
import edu.wpi.first.networktables.NetworkTableInstance
import edu.wpi.first.wpilibj.AnalogInput
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.constants.IndexerConstants

object Indexer : SubsystemBase() {

    private val indexerMotor = WPI_VictorSPX(IndexerConstants.kIndexerMotorCANID)
    
    private val sensor = AnalogInput(IndexerConstants.kIndexerSensorChannel)

    init {
        indexerMotor.configAllSettings(IndexerConstants.kIndexerConfig)

        SmartDashboard.putNumber("Indexer/Indexer_Motor", indexerMotor.get())
    }

    private fun index(dutycycle: Double) {
        indexerMotor.set(dutycycle)
    }
    
    fun ballDetected(): Boolean = sensor.value != 0
       
    fun index(dutycycle: () -> Double): Command {
        return startEnd({
            index(dutycycle())
        }, {
            index(0.0)
        })
    }
}