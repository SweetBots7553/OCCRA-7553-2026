package frc.robot.subsystems.drive

import com.pathplanner.lib.auto.AutoBuilder
import com.pathplanner.lib.config.RobotConfig
import com.pathplanner.lib.util.DriveFeedforwards
import com.revrobotics.RelativeEncoder
import com.revrobotics.spark.SparkMax
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry
import edu.wpi.first.math.kinematics.DifferentialDriveWheelPositions
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds
import edu.wpi.first.networktables.NetworkTableInstance
import edu.wpi.first.wpilibj.ADXRS450_Gyro
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.event.EventLoop
import edu.wpi.first.wpilibj.smartdashboard.Field2d
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.constants.OdometryConstants

class Odometry(
    private val driveTrain: DriveTrain,
    private val kinematics: DifferentialDriveKinematics,
    private val frontLeftEncoder: RelativeEncoder,
    private val frontRightEncoder: RelativeEncoder,
    private val backLeftEncoder: RelativeEncoder,
    private val backRightEncoder: RelativeEncoder
) : SubsystemBase() {

    private val gyro = ADXRS450_Gyro()
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
    // auto    

    init {
        // disable holographic movement 
        val config = RobotConfig.fromGUISettings()

        AutoBuilder.configure(
            { currentPose },
            { resetPose },
            { chassisSpeeds },
            { speeds: ChassisSpeeds, _: DriveFeedforwards ->
                driveTrain.drive(speeds)
            },
            OdometryConstants.kPathController,
            config,
            {
                false
            },
            driveTrain
        )
    }

    override fun periodic() {
        odometry.update(gyro.rotation2d, wheelpositions)

        SmartDashboard.putNumber("odometry/pose/x", currentPose.x)
        SmartDashboard.putNumber("odometry/pose/y", currentPose.y)
        SmartDashboard.putNumber("odometry/pose/rotation", currentPose.rotation.degrees)
    }
}