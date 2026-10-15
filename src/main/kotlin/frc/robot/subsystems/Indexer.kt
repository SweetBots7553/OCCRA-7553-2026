package frc.robot.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.constants.kIndexerConfig
import frc.robot.constants.kIndexerMotorCANID

object Indexer : SubsystemBase() {

    private val indexerMotor = WPI_VictorSPX(kIndexerMotorCANID)

    init {
        indexerMotor.configAllSettings(kIndexerConfig)
        
        val tab = Shuffleboard.getTab("Indexer")
        
        tab.addDouble("Shooter-Motor-Speed") { indexerMotor.get() }
    }

    private fun index(dutycycle: Double) {
        indexerMotor.set(dutycycle)
    } 

    fun index(dutycycle: () -> Double): Command {
        return startEnd({
            index(dutycycle())
        }, {
            index(0.0)
        })
    }

}