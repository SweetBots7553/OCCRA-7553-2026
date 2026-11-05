package frc.robot

import com.pathplanner.lib.auto.NamedCommands
import edu.wpi.first.math.MathUtil
import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.wpilibj.event.BooleanEvent
import edu.wpi.first.wpilibj.event.EventLoop
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.CommandScheduler
import edu.wpi.first.wpilibj2.command.Commands
import edu.wpi.first.wpilibj2.command.button.CommandXboxController
import frc.robot.constants.IndexerConstants
import frc.robot.constants.IntakeConstants
import frc.robot.constants.OperatorConstats
import frc.robot.subsystems.drive.DriveTrain
import frc.robot.subsystems.Indexer
import frc.robot.subsystems.Intake
import frc.robot.subsystems.Shooter
import frc.robot.subsystems.drive.Odometry
import frc.robot.subsystems.drive.genAutoEntries

object RobotContainer {
    // operator controllers
    private val driverController = CommandXboxController(OperatorConstats.kDriverPort)
    private val coDriverController = CommandXboxController(OperatorConstats.kCoDriverPort)

    // subsystems
    private val driveTrain = DriveTrain
    private val odometry = Odometry(driveTrain)
    private val intake = Intake
    private val shooter = Shooter
    private val indexer = Indexer
    
   

    // private val eventLoop: EventLoop = EventLoop()
    
     private val robotCommands: RobotCommands = RobotCommands(intake, shooter, indexer)
    
    // private val hasBallEvent: BooleanEvent = BooleanEvent(eventLoop) {
    //     robotCommands.hasBalls
    // }
    
    // private val notHasBallEvent: BooleanEvent = BooleanEvent(eventLoop) {
    //     !robotCommands.hasBalls
    // }
    
    
    // controls
    init {
        val threshold = OperatorConstats.kDeadbandThreshold
        
        // arcade control
        driveTrain.defaultCommand = driveTrain.drive(
            { -MathUtil.applyDeadband(driverController.leftY, threshold) },
            { -MathUtil.applyDeadband(driverController.rightX, threshold) }
        )
        
        coDriverController.b().whileTrue(robotCommands.intakeCommand).onFalse(robotCommands.stopIntake)
        coDriverController.leftBumper().whileTrue(robotCommands.outTakeCommand).onFalse(robotCommands.stopOutTaking)
        coDriverController.a().whileTrue(robotCommands.shootCommand).onFalse(robotCommands.stopShootCommand)
    }

    
    // auto

    val auto = genAutoEntries()
    
    
    val autonomousCommand: Command
        get() = Commands.sequence(driveTrain.drive({-0.3}, {0.0}), Commands.waitSeconds(1.4).andThen(driveTrain.drive({0.0}, {0.0}), robotCommands.shootCommand, Commands.waitSeconds(2.0), robotCommands.stopShootCommand))
}
