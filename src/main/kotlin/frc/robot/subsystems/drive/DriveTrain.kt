package frc.robot.subsystems.drive

import com.revrobotics.PersistMode
import com.revrobotics.ResetMode
import com.revrobotics.spark.SparkBase
import com.revrobotics.spark.SparkLowLevel.MotorType
import com.revrobotics.spark.SparkMax
import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
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
import java.util.function.BooleanSupplier
import java.util.function.DoubleSupplier
import kotlin.math.abs

object DriveTrain : SubsystemBase() {

    private val kinematics = DifferentialDriveKinematics(kTrackWidth)

    private val frontLeftNeo = SparkMax(kFrontLeftNeo, MotorType.kBrushless)
    private val frontRightNeo = SparkMax(kFrontRightNeo, MotorType.kBrushless)
    private val backLeftNeo = SparkMax(kBackLeftNeo, MotorType.kBrushless)
    private val backRightNeo = SparkMax(kBackRightNeo, MotorType.kBrushless)
    
    init {
        frontLeftNeo.configure(kTrainConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters)
        frontRightNeo.configure(kTrainConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters)
        backLeftNeo.configure(kBackLeftConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters)
        backRightNeo.configure(kBackRightConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters)
        
        val chassisTab = Shuffleboard.getTab("Chassis")
         // Use addNumber and explicitly pass the lambda as a DoubleSupplier
        chassisTab.addNumber("front-left-motor", DoubleSupplier { frontLeftNeo.get() })
        chassisTab.addNumber("front-right-motor", DoubleSupplier { frontRightNeo.get() })
        chassisTab.addNumber("back-left-motor", DoubleSupplier { backLeftNeo.get() })
        chassisTab.addNumber("back-right-motor", DoubleSupplier { backRightNeo.get() })

        // Booleans also require a BooleanSupplier to auto-update
        chassisTab.addBoolean("front-left-setpoint", BooleanSupplier { abs(frontLeftNeo.closedLoopController.setpoint) <= abs(frontLeftNeo.get()) })
        chassisTab.addBoolean("front-right-setpoint",   BooleanSupplier { abs(frontRightNeo.closedLoopController.setpoint) <= abs(frontRightNeo.get()) })
        chassisTab.addBoolean("back-left-setpoint", BooleanSupplier { abs(backLeftNeo.closedLoopController.setpoint) <= abs(backLeftNeo.get()) })
        chassisTab.addBoolean("back-right-setpoint", BooleanSupplier { abs(backRightNeo.closedLoopController.setpoint) <= abs(backRightNeo.get()) })

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
}