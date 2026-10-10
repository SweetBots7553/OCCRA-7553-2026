package frc.robot.subsystems

import com.pathplanner.lib.auto.AutoBuilder
import com.pathplanner.lib.config.RobotConfig
import com.revrobotics.PersistMode
import com.revrobotics.ResetMode
import com.revrobotics.spark.SparkBase
import com.revrobotics.spark.SparkLowLevel.MotorType
import com.revrobotics.spark.SparkMax
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry
import edu.wpi.first.wpilibj.ADXRS450_Gyro
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
    private val gyro = ADXRS450_Gyro()


    private val resetPose = Pose2d()

    var relativeSpeeds = ChassisSpeeds()

    val currentPose: Pose2d
        get() {
            return odometry.poseMeters
        }

    private val odometry =
        DifferentialDriveOdometry(gyro.rotation2d, frontLeftEncoder.position, frontRightEncoder.position, resetPose)

    init {
        frontLeftNeo.configure(kTrainConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters)
        frontRightNeo.configure(kTrainConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters)
        backLeftNeo.configure(kBackLeftConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters)
        backRightNeo.configure(kBackRightConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters)


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


        val config = RobotConfig.fromGUISettings()


        // AutoBuilder.configure(
        //     { currentPose },
        //     { resetPose },
        //     { relativeSpeeds }, 
        // )

        //         // Load the RobotConfig from the GUI settings. You should probably
        //         // store this in your Constants file
        //         RobotConfig config;
        //         try {
        //             config = RobotConfig.fromGUISettings();
        //         } catch (Exception e) {
        //             // Handle exception as needed
        //             e.printStackTrace();
        //         }

        //         // Configure AutoBuilder last
        //         AutoBuilder.configure(
        //             this::getPose, // Robot pose supplier
        //             this::resetPose, // Method to reset odometry (will be called if your auto has a starting pose)
        //             this::getRobotRelativeSpeeds, // ChassisSpeeds supplier. MUST BE ROBOT RELATIVE
        //             (speeds, feedforwards
        //         ) -> driveRobotRelative(speeds), // Method that will drive the robot given ROBOT RELATIVE ChassisSpeeds. Also optionally outputs individual module feedforwards
        //         new PPLTVController (0.02), // PPLTVController is the built in path following controller for differential drive trains
        //         config, // The robot configuration
        //         () -> {
        //             // Boolean supplier that controls when the path will be mirrored for the red alliance
        //             // This will flip the path being followed to the red side of the field.
        //             // THE ORIGIN WILL REMAIN ON THE BLUE SIDE

        //             var alliance = DriverStation.getAlliance();
        //             if (alliance.isPresent()) {
        //                 return alliance.get() == DriverStation.Alliance.Red;
        //             }
        //             return false;
        //         },
        //         this // Reference to this subsystem to set requirements
        //         );
        //     }
        // }

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

        synchronized(relativeSpeeds) {
            relativeSpeeds = speeds
        }
    }

    fun stop() {
        drive(ChassisSpeeds())
    }

    fun reset() {

    }


    override fun periodic() {
        odometry.update(gyro.rotation2d, frontLeftEncoder.position, frontRightEncoder.position)
    }

}