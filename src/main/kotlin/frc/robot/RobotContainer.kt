package frc.robot

import com.pathplanner.lib.auto.AutoBuilder
import com.pathplanner.lib.auto.NamedCommands
import edu.wpi.first.math.MathUtil
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import edu.wpi.first.wpilibj2.command.button.CommandXboxController
import frc.robot.constants.IntakeConstants
import frc.robot.constants.OperatorConstats
import frc.robot.subsystems.drive.DriveTrain
import frc.robot.subsystems.Indexer
import frc.robot.subsystems.Intake
import frc.robot.subsystems.Shooter
import frc.robot.subsystems.drive.Auto
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
        

        coDriverController.leftBumper().whileTrue(outTakeCommand).onFalse(unextend)
        coDriverController.rightBumper().whileTrue(intakeCommand).onFalse(unextend)
        coDriverController.a().whileTrue(shootCommand).onFalse(unextend)


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
        NamedCommands.registerCommand("Intake", intakeCommand)
        NamedCommands.registerCommand("OutTake", outTakeCommand)
        NamedCommands.registerCommand("Shoot", shootCommand)
        NamedCommands.registerCommand("Extend", extend)
        NamedCommands.registerCommand("UnExtend", unextend)
        
    }

    private val intakeCommand: Command
        get() {
            val intake = Commands.parallel(indexer.index { -IntakeConstants.kIntakeSpeed }, intake.intake { IntakeConstants.kIntakeSpeed })

            return Commands.sequence(extend, intake.onlyWhile { !indexer.ballDetected() })

        }
    private val outTakeCommand: Command
        get() {
            val outTake = Commands.parallel(indexer.index { IntakeConstants.kIntakeSpeed }, intake.intake { -IntakeConstants.kIntakeSpeed })

            return Commands.sequence(extend, outTake)
        }

    private val shootCommand: Command
        get() {
            return Commands.parallel(
                shooter.shoot(), 
                    Commands.sequence(
                        indexer.index { -IntakeConstants.kIntakeSpeed }, 
                        Commands.waitSeconds(0.6),
                        intakeCommand
                    )
                )
        }
        
    private val extend: Command
        get() = intake.extend { true }
        
    private val unextend: Command 
        get() = intake.extend { false }

    // auto

    val auto = genAutoEntries()
    
    val autonomousCommand: Command
        get() = auto.auto
}
