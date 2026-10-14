package frc.robot.subsystems.drive

import com.revrobotics.PersistMode
import com.revrobotics.ResetMode
import com.revrobotics.spark.SparkBase
import com.revrobotics.spark.SparkLowLevel.MotorType
import com.revrobotics.spark.SparkMax
import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.SubsystemBase
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

class DriveTrain : SubsystemBase() {

    private val kinematics = DifferentialDriveKinematics(kTrackWidth)

    private val frontLeftNeo = SparkMax(kFrontLeftNeo, MotorType.kBrushless)
    private val frontRightNeo = SparkMax(kFrontRightNeo, MotorType.kBrushless)
    private val backLeftNeo: SparkMax = SparkMax(kBackLeftNeo, MotorType.kBrushless)
    private val backRightNeo: SparkMax = SparkMax(kBackRightNeo, MotorType.kBrushless)
    
    init {
        frontLeftNeo.configure(kTrainConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters)
        frontRightNeo.configure(kTrainConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters)
        backLeftNeo.configure(kBackLeftConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters)
        backRightNeo.configure(kBackRightConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters)
    }

    fun drive(x: () -> Double, y: () -> Double, r: () -> Double): Command {
        return run {
            drive(
                ChassisSpeeds(
                    x() * kSpeedFactor / 2,
                    y() * kSpeedFactor / 2,
                    r() * kSpeedFactor
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
}