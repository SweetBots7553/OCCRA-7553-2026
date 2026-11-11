package frc.robot.subsystems


import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX
import edu.wpi.first.math.MathUtil
import edu.wpi.first.wpilibj.AnalogInput
import edu.wpi.first.wpilibj.PneumaticsModuleType
import edu.wpi.first.wpilibj.Solenoid
import edu.wpi.first.wpilibj.event.BooleanEvent
import edu.wpi.first.wpilibj.event.EventLoop
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

    val isExtended: Boolean get() = solenoid.get()

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

    val isBallDetected: Boolean get() = ballSensor.voltage > IntakeConstants.kBallDetectionThreshold
    val countedBalls: Int get() = currentCount

    private var currentCount: Int = 0
    private var sensorEnabled: Boolean = true

    val hasBalls: Boolean get() = countedBalls >= 2
    val clearCount: Command = runOnce { currentCount = 0 }

    fun enabled(enabled: Boolean): Command {
        return runOnce {
            sensorEnabled = enabled
        }
    }

    private val eventLoop: EventLoop = EventLoop()
    private val countEvent: BooleanEvent = BooleanEvent(eventLoop) { isBallDetected }

    init {
        countEvent.ifHigh {
            val ballDetected = isBallDetected && sensorEnabled

            if (ballDetected && sign(intakeMotor.get()) == 1.0)
                MathUtil.clamp(currentCount++, 0, 3)
            else if (ballDetected && sign(intakeMotor.get()) == -1.0)
                MathUtil.clamp(currentCount--, 0, 3)
        }
    }

    override fun periodic() {
        eventLoop.poll()
        SmartDashboard.putBoolean("Intake/Intake_Extended", solenoid.get())
        SmartDashboard.putNumber("Intake/Intake_Motor", intakeMotor.get())
        SmartDashboard.putBoolean("Intake/Ball_is_Detected", isBallDetected)
        SmartDashboard.putNumber("Intake/Sensor_Current", ballSensor.voltage)
        SmartDashboard.putNumber("Intake/Current_Ball_Count", countedBalls.toDouble())
    }


}