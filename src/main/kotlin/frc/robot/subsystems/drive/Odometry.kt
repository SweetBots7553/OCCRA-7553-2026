package frc.robot.subsystems.drive

import com.pathplanner.lib.auto.AutoBuilder
import com.pathplanner.lib.config.RobotConfig
import com.pathplanner.lib.util.DriveFeedforwards
import com.revrobotics.spark.SparkMax
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry
import edu.wpi.first.math.kinematics.DifferentialDriveWheelPositions
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds
import edu.wpi.first.wpilibj.ADXRS450_Gyro
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.constants.kPathController

class Odometry(
    driveTrain: DriveTrain, 
    frontLeftNeo: SparkMax, 
    frontRightNeo: SparkMax, 
    private val kinematics: DifferentialDriveKinematics) : SubsystemBase() {

    private val frontLeftEncoder = frontLeftNeo.encoder
    private val frontRightEncoder = frontRightNeo.encoder
    private val gyro = ADXRS450_Gyro()

    private val resetPose = Pose2d()
        
    private val odometry =
        DifferentialDriveOdometry (
            gyro.rotation2d, 
            frontLeftEncoder.position, 
            frontRightEncoder.position, 
            resetPose
        )

    val currentPose: Pose2d
        get() {
            return odometry.poseMeters
        }
            
    private val wheelpositions: DifferentialDriveWheelPositions
        get() {
            return DifferentialDriveWheelPositions(frontLeftEncoder.position, frontRightEncoder.position)
        }
        
    private val wheelvelocitys: DifferentialDriveWheelSpeeds
        get() {
            return DifferentialDriveWheelSpeeds(frontLeftEncoder.velocity, frontRightEncoder.velocity)
        }
        
    private val chassisSpeeds: ChassisSpeeds
        get() {            
            return kinematics.toChassisSpeeds(wheelvelocitys)
        } 
            
    init {
        val config = RobotConfig.fromGUISettings()
        
        AutoBuilder.configure (
            { currentPose }, 
            { resetPose }, 
            { chassisSpeeds },
            {speeds: ChassisSpeeds, _: DriveFeedforwards ->
                driveTrain.drive(speeds)
            },
            kPathController,
            config, 
            {false}, 
            driveTrain
        )
    }
    
    fun reset() {
        odometry.resetPosition(gyro.rotation2d, wheelpositions, resetPose)
    }
    
    override fun periodic() {
        odometry.update(gyro.rotation2d, wheelpositions)
    }
}