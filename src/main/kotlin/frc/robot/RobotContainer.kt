package frc.robot

import com.pathplanner.lib.auto.NamedCommands
import edu.wpi.first.math.MathUtil
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.CommandScheduler
import edu.wpi.first.wpilibj2.command.Commands
import edu.wpi.first.wpilibj2.command.button.CommandXboxController
import frc.robot.constants.IntakeConstants
import frc.robot.constants.OperatorConstats
import frc.robot.subsystems.drive.DriveTrain
import frc.robot.subsystems.Indexer
import frc.robot.subsystems.Intake
import frc.robot.subsystems.Shooter
import frc.robot.subsystems.drive.genAutoEntries

object RobotContainer {
    // operator controllers
    private val driverController = CommandXboxController(OperatorConstats.kDriverPort)
    private val coDriverController = CommandXboxController(OperatorConstats.kCoDriverPort)

    // subsystems
    private val driveTrain = DriveTrain
    private val intake = Intake
    private val shooter = Shooter
    private val indexer = Indexer

    // controls
    init {
        
        val threshold = OperatorConstats.kDeadbandThreshold
        
        // arcade control
        driveTrain.defaultCommand = driveTrain.drive(
            { MathUtil.applyDeadband(driverController.leftY, threshold) },
            { MathUtil.applyDeadband(driverController.rightX, threshold) }
        )
        

        // coDriverController.leftBumper().whileTrue(outTakeCommand).onFalse(stopOutTakeCommand)
        // coDriverController.rightBumper().whileTrue(intakeCommand).onFalse(stopIntake)
        // coDriverController.a().whileTrue(shootCommand).onFalse(unextend)


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
        // NamedCommands.registerCommand("Intake", intakeCommand)
        // NamedCommands.registerCommand("OutTake", outTakeCommand)
        // NamedCommands.registerCommand("Shoot", shootCommand)
        // NamedCommands.registerCommand("Extend", extend)
        // NamedCommands.registerCommand("UnExtend", unextend)
        
    }

    // private val intakeCommand: Command
    //     get() {
    //         val intakeCmd = Commands.parallel(indexer.index { -IntakeConstants.kIntakeSpeed }, intake.intake { IntakeConstants.kIntakeSpeed })
            
    //         val seq = Commands.sequence(extend, intakeCmd)
        
    //         return seq

    //     }
        
    // private val stopIntake: Command
    //     get() {
    //         val end = Commands.parallel(intake.stop(), indexer.stop(), unextend)
            
    //         return end
    //     }
        
        
    // private val outTakeCommand: Command
    //     get() {
    //         val outTakeCmd = Commands.parallel(indexer.index { IntakeConstants.kIntakeSpeed }, intake.intake { -IntakeConstants.kIntakeSpeed })

    //         val seq = Commands.sequence(extend, outTakeCmd)
            
    //         return seq
    //     }
        
    // private val stopOutTakeCommand: Command 
    //     get() {
    //         val end = Commands.parallel(indexer.stop(), intake.stop(), unextend)
             
    //         return end 
    //     }

    // private val shootCommand: Command
    //     get() {
            
    //         val seq = Commands.sequence()
    //         val end = Commands.parallel()
            
            
    //         return Commands.startEnd({
                
    //         }, {
                
    //         })
        
    //     }
        
    private val extend: Command
        get() = intake.extend { true }
        
    private val unextend: Command 
        get() = intake.extend { false }

    // auto

    val auto = genAutoEntries()
    
    val autonomousCommand: Command
        get() = auto.auto
}
