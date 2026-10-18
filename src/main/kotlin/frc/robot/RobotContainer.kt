package frc.robot

import com.pathplanner.lib.auto.NamedCommands
import edu.wpi.first.math.MathUtil
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import edu.wpi.first.wpilibj2.command.button.CommandXboxController
import frc.robot.constants.kCoDriverPort
import frc.robot.constants.kDeadbandThreshold
import frc.robot.constants.kDriverPort
import frc.robot.constants.kIntakeSpeed
import frc.robot.subsystems.drive.DriveTrain
import frc.robot.subsystems.Indexer
import frc.robot.subsystems.Intake
import frc.robot.subsystems.Shooter

object RobotContainer {
    // operator controllers
    private val driverController = CommandXboxController(kDriverPort)
    private val coDriverController = CommandXboxController(kCoDriverPort)

    // subsystems
    private val driveTrain = DriveTrain
    private val intake = Intake
    private val shooter = Shooter
    private val indexer = Indexer

    // controls
    init {
        // arcade control
        driveTrain.defaultCommand = driveTrain.drive(
            { MathUtil.applyDeadband(driverController.rightY, kDeadbandThreshold) },
            { MathUtil.applyDeadband(driverController.leftX, kDeadbandThreshold) }
        )

        coDriverController.x().whileTrue(outTakeCommand).onFalse(unextend)
        coDriverController.b().whileTrue(intakeCommand).onFalse(unextend)
        coDriverController.a().whileTrue(shootCommand).onFalse(unextend)


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
        NamedCommands.registerCommand("Extend", extend)
        NamedCommands.registerCommand("UnExtend", unextend)
    }

    private val intakeCommand: Command
        get() {
            val intake = Commands.parallel(indexer.index { -kIntakeSpeed }, intake.intake { -kIntakeSpeed })

            return Commands.sequence(extend, intake)

        }
    private val outTakeCommand: Command
        get() {
            val outTake = Commands.parallel(indexer.index { kIntakeSpeed }, intake.intake { kIntakeSpeed })

            return Commands.sequence(extend, outTake)
        }

    private val shootCommand: Command
        get() {
            val indexing = indexer.index { kIntakeSpeed }.withTimeout(1.0).andThen(indexer.index { 0.0 })
            val shooting = shooter.shoot().withTimeout(1.0)

            val final = indexer.index { -kIntakeSpeed }

            return Commands.sequence(extend, intake.intake { -kIntakeSpeed }, indexing, shooting, final)
        }

    private val extend: Command
        get() = intake.extend { true }

    private val unextend: Command
        get() = intake.extend { false }

    // auto

    private val autoChooser = SendableChooser<String>()

    init {
        autoChooser.addOption("test", "test")
    }


    // val autonomousCommand: Command
    //     get() = AutoBuilder.buildAuto(autoChooser.selected)

    val autonomousCommand: Command
        get() = Commands.none()


}
