package frc.robot.subsystems


import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX
import edu.wpi.first.networktables.NetworkTableInstance
import edu.wpi.first.wpilibj.Compressor
import edu.wpi.first.wpilibj.PneumaticHub
import edu.wpi.first.wpilibj.PneumaticsModuleType
import edu.wpi.first.wpilibj.Solenoid
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.constants.kIntakeCANID
import frc.robot.constants.kIntakeConfig
import frc.robot.constants.kPneumaticChannel
import frc.robot.constants.kPneumaticHubCANID

object Intake : SubsystemBase() {

 
    private val solenoid = Solenoid(kPneumaticHubCANID,PneumaticsModuleType.CTREPCM, kPneumaticChannel)
    // private val commpressor = Compressor(PneumaticsModuleType.CTREPCM)

    private val intakeMotor = WPI_VictorSPX(kIntakeCANID)

    init {
        intakeMotor.configAllSettings(kIntakeConfig)
        // commpressor.enableDigital()
        
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
        return startEnd({
            intake(dutycycle())
        }, {
            intake(0.0)
        })
    }
    
    private val table = NetworkTableInstance.getDefault().getTable("Intake")
    private val intakeSpeedEntry = table.getEntry("Intake-speed")


    override fun periodic() {
        intakeSpeedEntry.setDouble(intakeMotor.get())
    }
}