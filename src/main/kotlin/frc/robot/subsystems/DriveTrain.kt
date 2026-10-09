package frc.robot.subsystems

import com.revrobotics.PersistMode
import com.revrobotics.ResetMode
import com.revrobotics.spark.SparkBase
import com.revrobotics.spark.SparkLowLevel.MotorType
import com.revrobotics.spark.SparkMax
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics
import edu.wpi.first.wpilibj.Notifier
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.constants.kBackLeftConfig
import frc.robot.constants.kBackLeftNeo
import frc.robot.constants.kBackRightConfig
import frc.robot.constants.kBackRightNeo
import frc.robot.constants.kFrontLeftNeo
import frc.robot.constants.kFrontRightNeo

import frc.robot.constants.kSpeedFactor

import frc.robot.constants.kTrackWidth
import frc.robot.constants.kTrainConfig

class DriveTrain : SubsystemBase() {

    private val kinematics = DifferentialDriveKinematics(kTrackWidth)

    private val frontLeftNeo: SparkMax = SparkMax(kFrontLeftNeo, MotorType.kBrushless)
    private val frontRightNeo: SparkMax = SparkMax(kFrontRightNeo, MotorType.kBrushless)
    private val backLeftNeo: SparkMax = SparkMax(kBackLeftNeo, MotorType.kBrushless)
    private val backRightNeo: SparkMax = SparkMax(kBackRightNeo, MotorType.kBrushless)

    private val frontLeftEncoder = frontLeftNeo.encoder
    private val frontRightEncoder = frontRightNeo.encoder
    private val backLeftEncoder = backLeftNeo.encoder
    private val backRightEncoder = backRightNeo.encoder

    private val currentPose = Pose2d()
    private val resetPose = Pose2d()

    private val odometryNotifier = Notifier {

        zeroEncoders()
    }

    init {
        frontLeftNeo.configure(kTrainConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters)
        frontRightNeo.configure(kTrainConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters)
        backLeftNeo.configure(kBackLeftConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters)
        backRightNeo.configure(kBackRightConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters)

        odometryNotifier.startPeriodic(0.1)

        // shuffel board stuff UwU

        val driveTab = Shuffleboard.getTab("DriveTrain")

        driveTab.addDouble("FrontLeftMotor") {
            frontLeftNeo.encoder.velocity
        }

        driveTab.addDouble("FrontRightMotor") {
            frontRightNeo.encoder.velocity
        }

        driveTab.addDouble("BackLeftMotor") {
            backLeftNeo.encoder.velocity
        }

        driveTab.addDouble("BackRightMotor") {
            backRightNeo.encoder.velocity
        }

        val odoTab = Shuffleboard.getTab("Odometry")

    }

    fun drive(x: () -> Double, y: () -> Double, r: () -> Double): Command {
        return run {
            drive(
                ChassisSpeeds(
                    x() * kSpeedFactor,
                    y() * kSpeedFactor,
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

    private fun zeroEncoders() {
        val z = 0.0

        frontLeftEncoder.position = z
        frontRightEncoder.position = z
        backLeftEncoder.position = z
        backRightEncoder.position = z
    }


}