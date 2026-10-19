package frc.robot.subsystems.drive

import com.revrobotics.PersistMode
import com.revrobotics.ResetMode
import com.revrobotics.spark.SparkBase
import com.revrobotics.spark.SparkLowLevel.MotorType
import com.revrobotics.spark.SparkMax
import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics
import edu.wpi.first.networktables.NetworkTableInstance
import edu.wpi.first.units.Units
import edu.wpi.first.units.measure.Voltage
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.SubsystemBase
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine
import frc.robot.constants.DriveTrainConstants

import kotlin.math.abs

object DriveTrain : SubsystemBase() {

    private val kinematics = DifferentialDriveKinematics(DriveTrainConstants.kTrackWidth)

    private val frontLeftNeo = SparkMax(DriveTrainConstants.kFrontLeftNeoCANID, MotorType.kBrushless)
    private val frontRightNeo = SparkMax(DriveTrainConstants.kFrontRightNeoCANID, MotorType.kBrushless)
    private val backLeftNeo = SparkMax(DriveTrainConstants.kBackLeftNeoCANID, MotorType.kBrushless)
    private val backRightNeo = SparkMax(DriveTrainConstants.kBackRightNeoCANID, MotorType.kBrushless)

    private val odometry = Odometry(this, frontLeftNeo, frontRightNeo, kinematics)

    // config
    init {
        frontLeftNeo.configure(DriveTrainConstants.kFrontConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters)
        frontRightNeo.configure(DriveTrainConstants.kFrontRightConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters)
        backLeftNeo.configure(DriveTrainConstants.kBackLeftConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters)
        backRightNeo.configure(DriveTrainConstants.kBackRightConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters)

     
    }

    fun drive(x: () -> Double, r: () -> Double): Command {
        return run {
            drive(
                ChassisSpeeds(
                    x() * DriveTrainConstants.kSpeedFactor,
                    0.0,
                    r() * DriveTrainConstants.kRotationPerSecond 
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
        backLeftNeo.closedLoopController.setSetpoint(leftSpeed, SparkBase.ControlType.kVelocity)
        backRightNeo.closedLoopController.setSetpoint(rightSpeed, SparkBase.ControlType.kVelocity)
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

    override fun periodic() {
        SmartDashboard.putNumber("chassis/speeds/Front_left_Velocity", frontLeftNeo.encoder.velocity)
        SmartDashboard.putNumber("chassis/speeds/Front_right_Velocity", frontRightNeo.encoder.velocity)
        SmartDashboard.putNumber("chassis/speeds/Back_left_Velocity", backLeftNeo.encoder.velocity)
        SmartDashboard.putNumber("chassis/speeds/Back_right_Velocity", backRightNeo.encoder.velocity)

        SmartDashboard.putNumber("chassis/setpoints/Front_left_Setpoint", frontLeftNeo.closedLoopController.setpoint)
        SmartDashboard.putNumber("chassis/setpoints/Front_right_Setpoint", frontRightNeo.closedLoopController.setpoint)
        SmartDashboard.putNumber("chassis/setpoints/Back_left_Setpoint", backLeftNeo.closedLoopController.setpoint)
        SmartDashboard.putNumber("chassis/setpoints/Back_right_Setpoint", backRightNeo.closedLoopController.setpoint)
    }
}