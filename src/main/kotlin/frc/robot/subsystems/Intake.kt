package frc.robot.subsystems


import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX
import edu.wpi.first.networktables.NetworkTableInstance
import edu.wpi.first.wpilibj.PneumaticsModuleType
import edu.wpi.first.wpilibj.Solenoid
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.constants.IntakeConstants


object Intake : SubsystemBase() {
    private val solenoid = Solenoid(IntakeConstants.kPneumaticHubCANID, PneumaticsModuleType.CTREPCM, IntakeConstants.kPneumaticChannel)
    private val intakeMotor = WPI_VictorSPX(IntakeConstants.kIntakeMotorCANID)

    init {
        intakeMotor.configAllSettings(IntakeConstants.kIntakeMotorConfig)
    }

    private fun extend(b: Boolean) {
        if (b == solenoid.get())
            return

        solenoid.set(b)
    }

    fun extend(b: () -> Boolean): Command {
        return runOnce {
            extend(b())
        }
    }
    
    private fun intake(dutycycle: Double) {
        intakeMotor.set(dutycycle)
    }

    fun intake(dutycycle: () -> Double): Command {
        return runOnce { 
            intake(dutycycle())
        }
    }
    
     fun stop(): Command {
        return runOnce { 
           intake(0.0)
        }
     }
         

    override fun periodic() {
        SmartDashboard.putBoolean("Intake/Intake_Extended", solenoid.get())
        SmartDashboard.putNumber("Intake/Intake_Motor", intakeMotor.get())
    }
}