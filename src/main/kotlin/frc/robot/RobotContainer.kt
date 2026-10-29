package frc.robot

import com.pathplanner.lib.auto.NamedCommands
import edu.wpi.first.math.MathUtil
import edu.wpi.first.math.kinematics.ChassisSpeeds
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

    // controls
    init {
        
        val threshold = OperatorConstats.kDeadbandThreshold
        
        // arcade control
        driveTrain.defaultCommand = driveTrain.drive(
            { -MathUtil.applyDeadband(driverController.leftY, threshold) },
            { -MathUtil.applyDeadband(driverController.rightX, threshold) }
        )
        
    
        
        driverController.a().toggleOnTrue(extendControl())
        
        driverController.leftTrigger().whileTrue(intakeCommand()).onFalse(stopIntake())
        driverController.rightBumper().whileTrue(outTakeCommand()).onFalse(stopIntake())
        driverController.rightTrigger().whileTrue(shootCommand()).onFalse(stopShootCommand())
        

        // coDriverController.leftBumper().whileTrue(outTakeCommand()).onFalse(stopIntake())
        // coDriverController.rightBumper().whileTrue(intakeCommand()).onFalse(stopIntake())
        // coDriverController.a().whileTrue(shootCommand()).onFalse(stopShootCommand())


        // tmp 
        // driverController.x().whileTrue(driveTrain.forwardsysIdQuasistatic())
        // driverController.y().whileFalse(driveTrain.reversesysIdQuasistatic())

        // driverController.a().whileTrue(driveTrain.forwardsysIdDynamic())
        // driverController.b().whileTrue(driveTrain.reversesysIdDynamic())
        
        // driverController.a().whileTrue(shooter.forwardsysIdQuasistatic())
    }
    // commands

    // pathplanner autos
    init {
        NamedCommands.registerCommand("Intake", intakeCommand())
        NamedCommands.registerCommand("OutTake", outTakeCommand())
        NamedCommands.registerCommand("Shoot", shootCommand())
        NamedCommands.registerCommand("StopIntake", stopIntake())
        NamedCommands.registerCommand("StopShooter", stopShootCommand())
    }

    private fun intakeCommand(): Command = extend().andThen(intake.intake { IntakeConstants.kIntakeSpeed }.alongWith(indexer.index { IndexerConstants.kIndexerSpeed  }))
       
    private fun outTakeCommand(): Command = extend().andThen(intake.intake { -IntakeConstants.kIntakeSpeed }.alongWith(indexer.index { -IndexerConstants.kIndexerSpeed  })).alongWith(shooter.outShoot())
   
        
    private fun stopIntake(): Command = Commands.sequence(indexer.index {0.0}, intake.stop(), shooter.stop())
    
    private fun shootCommand(): Command = Commands.parallel(extend(), shooter.shoot(), Commands.waitSeconds(0.5).andThen(indexer.index { IndexerConstants.kIndexerSpeed }))
        
    private fun primeCommand(): Command = Commands.sequence(indexer.index { -IndexerConstants.kIndexerSpeed * 0.5 }, Commands.waitSeconds(0.2), indexer.index { 0.0 }, shooter.shoot())
    
    private fun stopShootCommand(): Command = indexer.index { 0.0 }.andThen(shooter.stop())
    
    private fun extend(): Command = intake.extend { true }
        
    private fun unextend(): Command  = intake.extend { false }

    private fun extendControl(): Command {
        
        return intake.extend { !intake.isExtended }
        
    } 
    
    // auto

    val auto = genAutoEntries()
    
    val autonomousCommand: Command
        get() = Commands.sequence(driveTrain.drive({-0.3}, {0.0}), Commands.waitSeconds(1.4).andThen(driveTrain.drive({0.0}, {0.0}), shootCommand(), Commands.waitSeconds(0.8), stopShootCommand()))
}
