package frc.robot.subsystems.drive

import com.ctre.phoenix6.hardware.Pigeon2
import com.pathplanner.lib.auto.AutoBuilder
import com.pathplanner.lib.config.RobotConfig
import com.pathplanner.lib.util.DriveFeedforwards
import com.revrobotics.RelativeEncoder
import edu.wpi.first.math.controller.PIDController
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry
import edu.wpi.first.math.kinematics.DifferentialDriveWheelPositions
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.Timer
import edu.wpi.first.wpilibj.smartdashboard.Field2d
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.constants.OdometryConstants
import kotlin.math.atan2

class Odometry(
    private val driveTrain: DriveTrain,
    private val kinematics: DifferentialDriveKinematics,
    private val frontLeftEncoder: RelativeEncoder,
    private val frontRightEncoder: RelativeEncoder,
    private val backLeftEncoder: RelativeEncoder,
    private val backRightEncoder: RelativeEncoder
) : SubsystemBase() {  

    private val gyro = Pigeon2(OdometryConstants.kPigionCAN)
    private var relativePosition = Pose2d()
    
    private val resetPose = Pose2d()

    private val odometry =
        DifferentialDriveOdometry(
            gyro.rotation2d,
            frontLeftEncoder.position,
            frontRightEncoder.position,
            resetPose
        )

    val currentPose: Pose2d get() = odometry.poseMeters

    private val wheelpositions: DifferentialDriveWheelPositions
        get() = DifferentialDriveWheelPositions(
            frontLeftEncoder.position,
            frontRightEncoder.position
        )

    private val wheelvelocitys: DifferentialDriveWheelSpeeds
        get() = DifferentialDriveWheelSpeeds(
            frontLeftEncoder.velocity,
            frontRightEncoder.velocity
        )

    private val chassisSpeeds: ChassisSpeeds get() = kinematics.toChassisSpeeds(wheelvelocitys)

    fun resetOdometry() {
        odometry.resetPosition(gyro.rotation2d, wheelpositions, resetPose)
    }
    
    init {
        AutoBuilder.configure(
           { currentPose }, 
            { resetPose },
            { chassisSpeeds }, 
             { speeds: ChassisSpeeds, _: DriveFeedforwards ->
                driveTrain.drive(speeds)
            },
            OdometryConstants.kPathController,
            RobotConfig.fromGUISettings(),
            {true},
           this, driveTrain 
        )
    }
    

    private val field = Field2d()
    private val alignmentpidControl = PIDController(OdometryConstants.kP, 0.0, OdometryConstants.kD).apply { 
        
    }
    
    val alignFactor: Double get() = alignmentpidControl.calculate(currentPose.rotation.radians)
        
    val updateRelativePose: Command        
        get() {
            return runOnce { 
                relativePosition = currentPose
                
                val delta = currentPose.minus(relativePosition)
                alignmentpidControl.setpoint = atan2(delta.y, delta.x)
            }
        }
    

    // auto    
    override fun periodic() {
        odometry.update(gyro.rotation2d, wheelpositions)

        SmartDashboard.putNumber("odometry/pose/x", currentPose.x)
        SmartDashboard.putNumber("odometry/pose/y", currentPose.y)
        SmartDashboard.putNumber("odometry/pose/rotation", currentPose.rotation.degrees)

        field.robotPose = currentPose
        SmartDashboard.putData("field/", field)
        SmartDashboard.putNumber("timer/", DriverStation.getMatchTime())

        SmartDashboard.putNumber("align/relativePosition", relativePosition.x)
        SmartDashboard.putNumber("align/relativePosition", relativePosition.y)
        SmartDashboard.putNumber("align/rotationFactor", alignFactor)
        
    }
}