package frc.robot.subsystems


import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX
import edu.wpi.first.wpilibj.PneumaticsModuleType
import edu.wpi.first.wpilibj.Solenoid
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.constants.kIntakeCANID
import frc.robot.constants.kIntakeConfig
import frc.robot.constants.kPneumaticDosChannel
import frc.robot.constants.kPneumaticUnoChannel
import java.util.concurrent.locks.ReentrantLock

class Intake : SubsystemBase() {

    private val intakeMotor = WPI_VictorSPX(kIntakeCANID)

    private val penumaticUno = Solenoid(PneumaticsModuleType.CTREPCM, kPneumaticUnoChannel)
    private val penumaticDos = Solenoid(PneumaticsModuleType.CTREPCM, kPneumaticDosChannel)

    init {
        intakeMotor.configAllSettings(kIntakeConfig)
    }
    
    private fun unoextended(b: Boolean) {
        if (b == penumaticUno.get())
            return
            
        penumaticUno.set(b)
    }
    
    private fun dosextended(b: Boolean) {
        if (b == penumaticDos.get())
            return
        
        penumaticDos.set(b)
    }

    private fun unoextend(b: () -> Boolean): Command {
        return runOnce {
            unoextended(b())
        }
    }
    
    private fun dosextend(b: () -> Boolean): Command {
        return runOnce { 
            dosextended(b())
        }
    }
    
    fun extend(b: () -> Boolean): Command {
        return Commands.parallel(unoextend(b), dosextend(b))
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