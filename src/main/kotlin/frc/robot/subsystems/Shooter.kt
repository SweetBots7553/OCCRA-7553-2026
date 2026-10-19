package frc.robot.subsystems

import com.revrobotics.PersistMode
import com.revrobotics.ResetMode
import com.revrobotics.spark.SparkBase
import com.revrobotics.spark.SparkFlex
import com.revrobotics.spark.SparkLowLevel
import com.revrobotics.spark.SparkMax
import edu.wpi.first.networktables.NetworkTableInstance
import edu.wpi.first.units.Units
import edu.wpi.first.units.measure.Voltage
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.SubsystemBase
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine
import frc.robot.constants.ShooterConstants

object Shooter : SubsystemBase() {

    private val shooterMotor = SparkFlex(ShooterConstants.kShooterMotorCANID, SparkLowLevel.MotorType.kBrushless)

    init {
        shooterMotor.configure(ShooterConstants.kShooterConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters)

        SmartDashboard.putNumber("Shooter/Shooter_Motor", shooterMotor.get())
    }

    private fun shoot(dutycycle: Double) {
        shooterMotor.closedLoopController.setSetpoint(dutycycle, SparkBase.ControlType.kDutyCycle)
    }

    fun shoot(): Command {
        return startEnd({
            shoot(ShooterConstants.kShooterSpeed)
        }, {
            shoot(0.0)
        })
    }
    
    // sysid

    private val routine: SysIdRoutine

    init {
        val config = SysIdRoutine.Config()

        val mechanism = SysIdRoutine.Mechanism(
            { voltage ->
                shooterMotor.setVoltage(voltage)
            },
            { log ->

                val voltage: Voltage = Units.Volts.of(shooterMotor.busVoltage * shooterMotor.appliedOutput)

                log.motor("shooter")
                    .voltage(voltage)
                    .linearPosition(Units.Meters.of(shooterMotor.encoder.position))
                    .linearVelocity(Units.MetersPerSecond.of(shooterMotor.encoder.velocity))
            },
            this
        )

        routine = SysIdRoutine(config, mechanism)
    }

    private fun sysIdQuasistatic(dir: SysIdRoutine.Direction): Command {
        return routine.quasistatic(dir)
    }

    private fun sysIdDynamic(dir: SysIdRoutine.Direction): Command {
        return routine.dynamic(dir)
    }

    fun forwardsysIdQuasistatic(): Command {
        return sysIdDynamic(SysIdRoutine.Direction.kForward)
    }

    fun forwardsysIdDynamic(): Command {
        return sysIdDynamic(SysIdRoutine.Direction.kForward)
    }

    fun reversesysIdQuasistatic(): Command {
        return sysIdQuasistatic(SysIdRoutine.Direction.kReverse)
    }

    fun reversesysIdDynamic(): Command {
        return sysIdDynamic(SysIdRoutine.Direction.kReverse)
    }


}