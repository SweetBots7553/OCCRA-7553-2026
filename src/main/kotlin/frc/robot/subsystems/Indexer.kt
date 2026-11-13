package frc.robot.subsystems

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX
import edu.wpi.first.wpilibj.AnalogInput
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.constants.IndexerConstants

object Indexer : SubsystemBase() {

    private val indexerMotor = WPI_VictorSPX(IndexerConstants.kIndexerMotorCANID)

    init {
        indexerMotor.configAllSettings(IndexerConstants.kIndexerConfig)
        indexerMotor.inverted = true
    }

    private fun index(dutycycle: Double) {
        indexerMotor.set(dutycycle)
    }

    private fun index(dutycycle: () -> Double): Command = runOnce { index ( dutycycle() ) }
    
    val indexing: Command get() = index { IndexerConstants.kIndexerSpeed }
    val outdexing: Command get() = index { -IndexerConstants.kIndexerSpeed }
    
    val stop: Command get() = index { 0.0 }

    override fun periodic() {
        SmartDashboard.putNumber("Indexer/Indexer_Motor", indexerMotor.get())
    }
}