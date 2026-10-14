package frc.robot.subsystems


import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX
import edu.wpi.first.wpilibj.PneumaticHub
import edu.wpi.first.wpilibj.PneumaticsModuleType
import edu.wpi.first.wpilibj.Solenoid
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.constants.kIntakeCANID
import frc.robot.constants.kIntakeConfig
import frc.robot.constants.kPneumaticChannel
import frc.robot.constants.kPneumaticHubCANID

class Intake : SubsystemBase() {

    private val penumaticHub = PneumaticHub(kPneumaticHubCANID)
    
    private val intakeMotor = WPI_VictorSPX(kIntakeCANID)
    private val penumatic = Solenoid(PneumaticsModuleType.REVPH, kPneumaticChannel)
    
    init {
        intakeMotor.configAllSettings(kIntakeConfig)
    }
    
    private fun extend(b: Boolean) {
        if (b == penumatic.get())
            return
            
        penumatic.set(b)
    }

    fun extend(b: () -> Boolean): Command {
        return runOnce {
            extend(b())
        }
    }
    
    private fun intake(dutycycle: Double) {
        intakeMotor.set( dutycycle)
    }

    fun intake(dutycycle :() -> Double): Command {
        return startEnd({
            intake(dutycycle)
        }, {
            intake(0.0)
        })
    }
}