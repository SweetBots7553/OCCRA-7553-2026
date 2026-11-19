package frc.robot

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import edu.wpi.first.wpilibj2.command.button.CommandXboxController
import frc.robot.constants.OperatorConstants
import frc.robot.subsystems.drive.DriveTrain
import frc.robot.subsystems.Indexer
import frc.robot.subsystems.Intake
import frc.robot.subsystems.Shooter
import frc.robot.subsystems.drive.genAutoEntries

// import frc.robot.subsystems.drive.genAutoEntries

object RobotContainer {
    // subsystems
    private val driveTrain = DriveTrain
    private val intake = Intake
    private val shooter = Shooter
    private val indexer = Indexer

    private val robotCommands: RobotCommands = RobotCommands(intake, shooter, indexer)

    private val driverController: CommandXboxController = OperatorConstants.driverController(driveTrain, robotCommands)
    private val coDriverController: CommandXboxController = OperatorConstants.coDriverController(robotCommands)

    
    // auto
    // val auto = genAutoEntries()
    // val autonomousCommand: Command get() = auto.auto

    val autonomousCommand: Command
        get() = Commands.sequence(
            driveTrain.drive({ -0.3 }, { 0.0 }),
            Commands.waitSeconds(1.4).andThen(
                driveTrain.drive({ 0.0 }, { 0.0 }),
                robotCommands.shootCommand,
                Commands.waitSeconds(2.0),
                robotCommands.stopShootCommand
            )
        )
}
