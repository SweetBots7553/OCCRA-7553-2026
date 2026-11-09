package frc.robot.subsystems


import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX
import edu.wpi.first.math.MathUtil
import edu.wpi.first.wpilibj.AnalogInput
import edu.wpi.first.wpilibj.PneumaticsModuleType
import edu.wpi.first.wpilibj.Solenoid
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.constants.IntakeConstants
import kotlin.math.sign


object Intake : SubsystemBase() {
    private val solenoid =
        Solenoid(IntakeConstants.kPneumaticHubCANID, PneumaticsModuleType.CTREPCM, IntakeConstants.kPneumaticChannel)
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

    private fun intake(dutycycle: () -> Double): Command {
        return runOnce {
            intake(dutycycle())
        }
    }

    val intake: Command = intake { IntakeConstants.kIntakeSpeed }
    val outTake: Command = intake { -IntakeConstants.kIntakeSpeed }

    val extend: Command = extend { true }
    val unextend: Command = extend { false }

    val stop: Command = intake { 0.0 }

    private val ballSensor: AnalogInput = AnalogInput(IntakeConstants.kIntakeAnalogSensorChannel)

    val isBallDetected: Boolean
        get() {
            return ballSensor.voltage > IntakeConstants.kBallDetectionThreshold
        }

    val countedBalls: Int
        get() {
            return currentCount
        }

    private var isBallDetectedPast: Boolean = false
    private var currentCount: Int = 0
    private var sensorEnabled: Boolean = true

    val hasBalls: Boolean = countedBalls >= 2
    val clearCount: Command = runOnce { currentCount = 0 }

    fun enabled(enabled: Boolean): Command {
        return runOnce {
            sensorEnabled = enabled
        }
    }

    override fun periodic() {
        val ballDetected = isBallDetected

        SmartDashboard.putBoolean("Intake/Intake_Extended", solenoid.get())
        SmartDashboard.putNumber("Intake/Intake_Motor", intakeMotor.get())
        SmartDashboard.putBoolean("Intake/Ball_is_Detected", ballDetected)

        SmartDashboard.putNumber("Intake/Sensor_Current", ballSensor.voltage)

        if (ballDetected && !isBallDetectedPast && sensorEnabled && sign(intakeMotor.get()) == 1.0)
            MathUtil.clamp(currentCount++, 0, 3)
        else if (ballDetected && !isBallDetectedPast && sensorEnabled && sign(intakeMotor.get()) == -1.0)
            MathUtil.clamp(currentCount--, 0, 3)

        SmartDashboard.putNumber("Intake/Current_Ball_Count", countedBalls.toDouble())

        isBallDetectedPast = isBallDetected
    }


}