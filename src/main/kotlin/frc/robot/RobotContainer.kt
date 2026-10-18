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
            { MathUtil.applyDeadband(driverController.rightY, threshold) },
            { MathUtil.applyDeadband(driverController.leftX, threshold) }
        )

        coDriverController.x().whileTrue(outTakeCommand)
        coDriverController.b().whileTrue(intakeCommand)
        coDriverController.a().whileTrue(shootCommand)


        // tmp 
        // driverController.x().whileTrue(driveTrain.forwardsysIdQuasistatic())
        // driverController.y().whileFalse(driveTrain.reversesysIdQuasistatic())

        // driverController.a().whileTrue(driveTrain.forwardsysIdDynamic())
        // driverController.b().whileTrue(driveTrain.reversesysIdDynamic())
    }
    // commands

    // pathplanner autos
    init {
        NamedCommands.registerCommand("Intake", intakeCommand)
        NamedCommands.registerCommand("OutTake", outTakeCommand)
        NamedCommands.registerCommand("Shoot", shootCommand)
    }

    private val intakeCommand: Command
        get() {
            val intake = Commands.parallel(indexer.index { -IntakeConstants.kIntakeSpeed }, intake.intake { -IntakeConstants.kIntakeSpeed })

            return Commands.sequence(extend, intake)

        }
    private val outTakeCommand: Command
        get() {
            val outTake = Commands.parallel(indexer.index { IntakeConstants.kIntakeSpeed }, intake.intake { IntakeConstants.kIntakeSpeed })

            return Commands.sequence(extend, outTake)
        }

    private val shootCommand: Command
        get() {
            val indexing = indexer.index { IntakeConstants.kIntakeSpeed }.withTimeout(1.0).andThen(indexer.index { 0.0 })
            val shooting = shooter.shoot().withTimeout(1.0)

            val final = indexer.index { -IntakeConstants.kIntakeSpeed }

            return Commands.sequence(extend, intake.intake { -IntakeConstants.kIntakeSpeed }, indexing, shooting, final)
        }
        
    private val extend: Command
        get() = intake.extend()

    // auto

    val auto = genAutoEntries()
    
    val autonomousCommand: Command
        get() = auto.auto
}
