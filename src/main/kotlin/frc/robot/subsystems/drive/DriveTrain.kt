package frc.robot.subsystems.drive

import com.revrobotics.PersistMode
import com.revrobotics.ResetMode
import com.revrobotics.spark.SparkBase
import com.revrobotics.spark.SparkLowLevel.MotorType
import com.revrobotics.spark.SparkMax
import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics
import edu.wpi.first.networktables.NetworkTable
import edu.wpi.first.networktables.NetworkTableInstance
import edu.wpi.first.units.Units
import edu.wpi.first.units.VoltageUnit
import edu.wpi.first.units.measure.Voltage
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.SubsystemBase
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine
import frc.robot.constants.kBackLeftConfig
import frc.robot.constants.kBackLeftNeo
import frc.robot.constants.kBackRightConfig
import frc.robot.constants.kBackRightNeo
import frc.robot.constants.kFrontLeftNeo
import frc.robot.constants.kFrontRightNeo
import frc.robot.constants.kRotationSpeedFactor

import frc.robot.constants.kSpeedFactor

import frc.robot.constants.kTrackWidth
import frc.robot.constants.kTrainConfig
import java.util.function.BooleanSupplier
import java.util.function.DoubleSupplier
import kotlin.math.abs

object DriveTrain : SubsystemBase() {

    private val kinematics = DifferentialDriveKinematics(kTrackWidth)

    private val frontLeftNeo = SparkMax(kFrontLeftNeo, MotorType.kBrushless)
    private val frontRightNeo = SparkMax(kFrontRightNeo, MotorType.kBrushless)
    private val backLeftNeo = SparkMax(kBackLeftNeo, MotorType.kBrushless)
    private val backRightNeo = SparkMax(kBackRightNeo, MotorType.kBrushless)

    // config
    init {
        frontLeftNeo.configure(kTrainConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters)
        frontRightNeo.configure(kTrainConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters)
        backLeftNeo.configure(kBackLeftConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters)
        backRightNeo.configure(kBackRightConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters)
    }

    // NetworkTables

    private val table = NetworkTableInstance.getDefault().getTable("Chassis")

    private val frontLeftMotorEntry = table.getEntry("front-left-motor")
    private val frontRightMotorEntry = table.getEntry("front-right-motor")
    private val backLeftMotorEntry = table.getEntry("back-left-motor")
    private val backRightMotorEntry = table.getEntry("back-right-motor")

    private val frontLeftSetpointEntry = table.getEntry("front-left-setpoint")
    private val frontRightSetpointEntry = table.getEntry("front-right-setpoint")
    private val backLeftSetpointEntry = table.getEntry("back-left-setpoint")
    private val backRightSetpointEntry = table.getEntry("back-right-setpoint")

    override fun periodic() {
        frontLeftMotorEntry.setDouble(frontLeftNeo.get())
        frontRightMotorEntry.setDouble(frontRightNeo.get())
        backLeftMotorEntry.setDouble(backLeftNeo.get())
        backRightMotorEntry.setDouble(backRightNeo.get())

        frontLeftSetpointEntry.setBoolean(abs(frontLeftNeo.closedLoopController.setpoint) <= abs(frontLeftNeo.get()))
        frontRightSetpointEntry.setBoolean(abs(frontRightNeo.closedLoopController.setpoint) <= abs(frontRightNeo.get()))
        backLeftSetpointEntry.setBoolean(abs(backLeftNeo.closedLoopController.setpoint) <= abs(backLeftNeo.get()))
        backRightSetpointEntry.setBoolean(abs(backRightNeo.closedLoopController.setpoint) <= abs(backRightNeo.get()))
    }

    fun drive(x: () -> Double, r: () -> Double): Command {
        return run {
            drive(
                ChassisSpeeds(
                    x() * kSpeedFactor,
                    0.0,
                    r() * kRotationSpeedFactor
                )
            )
        }

    }

    fun drive(speeds: ChassisSpeeds) {
        val v = kinematics.toWheelSpeeds(speeds)

        val leftSpeed = v.leftMetersPerSecond
        val rightSpeed = v.rightMetersPerSecond

        frontLeftNeo.closedLoopController.setSetpoint(leftSpeed, SparkBase.ControlType.kVelocity)
        frontRightNeo.closedLoopController.setSetpoint(rightSpeed, SparkBase.ControlType.kVelocity)
    }

    fun stop() {
        drive(ChassisSpeeds())
    }

    private val routine: SysIdRoutine

    // routine setup
    init {
        val config = SysIdRoutine.Config(
            null,
            Units.Volts.of(4.0),
            null,
        )

        val mechanism = SysIdRoutine.Mechanism(
            { voltage ->
                frontLeftNeo.setVoltage(voltage)
                frontRightNeo.setVoltage(voltage)
            },
            { log ->

                val leftVoltage: Voltage = Voltage.ofBaseUnits(
                    frontLeftNeo.busVoltage * frontLeftNeo.appliedOutput,
                    Units.Volts
                )

                val rightVoltage: Voltage = Voltage.ofBaseUnits(
                    frontRightNeo.busVoltage * frontRightNeo.appliedOutput,
                    Units.Volts
                )

                log.motor("left-motor")
                    .voltage(leftVoltage)
                    .linearPosition(Units.Meters.of(frontLeftNeo.encoder.position))
                    .linearVelocity(Units.MetersPerSecond.of(frontLeftNeo.encoder.velocity))

                log.motor("right-motor")
                    .voltage(rightVoltage)
                    .linearPosition(Units.Meters.of(frontRightNeo.encoder.position))
                    .linearVelocity(Units.MetersPerSecond.of(frontRightNeo.encoder.velocity))

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