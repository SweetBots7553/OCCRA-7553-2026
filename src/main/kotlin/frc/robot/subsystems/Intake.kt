package frc.robot.subsystems


import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX
import edu.wpi.first.networktables.NetworkTableInstance
import edu.wpi.first.wpilibj.AnalogInput
import edu.wpi.first.wpilibj.PneumaticsModuleType
import edu.wpi.first.wpilibj.Solenoid
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.constants.IntakeConstants
import kotlin.math.abs


object Intake : SubsystemBase() {
    private val solenoid = Solenoid(IntakeConstants.kPneumaticHubCANID, PneumaticsModuleType.CTREPCM, IntakeConstants.kPneumaticChannel)
    private val intakeMotor = WPI_VictorSPX(IntakeConstants.kIntakeMotorCANID)

    init {
        intakeMotor.configAllSettings(IntakeConstants.kIntakeMotorConfig)
    }
    
    val isExtended: Boolean 
    get() {
        return solenoid.get()
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
        return intake { 0.0 }
    }
     
    private val ballSensor: AnalogInput = AnalogInput(IntakeConstants.kIntakeAnalogSensorChannel)
     
    val isBallDetected: Boolean
    get() {
        return abs(ballSensor.value) != 0
    }
    
    val countedBalls: Int
    get() {
        return currentCount
    }
    
    private var isBallDetectedPast: Boolean = false
    private var currentCount: Int = 0
    
    fun clearCount() {
        currentCount = 0
    }

    override fun periodic() {
        SmartDashboard.putBoolean("Intake/Intake_Extended", solenoid.get())
        SmartDashboard.putNumber("Intake/Intake_Motor", intakeMotor.get())
        SmartDashboard.putBoolean("Intake/Ball_is_Detected", isBallDetected)

        if (isBallDetected && !isBallDetectedPast) 
            currentCount++

        SmartDashboard.putNumber("Intake/Current", countedBalls.toDouble())
        
        isBallDetectedPast = isBallDetected
                
    }
        
        
}