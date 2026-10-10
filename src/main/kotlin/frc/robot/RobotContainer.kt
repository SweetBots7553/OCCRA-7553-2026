package frc.robot

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import edu.wpi.first.wpilibj2.command.button.CommandXboxController
import frc.robot.constants.deadbandOutput
import frc.robot.constants.kCoDriverPort
import frc.robot.constants.kDriverPort
import frc.robot.subsystems.DriveTrain
import frc.robot.subsystems.Indexer
import frc.robot.subsystems.Intake
import frc.robot.subsystems.Shooter

class RobotContainer {

    // operator controllers
    private val driverController = CommandXboxController(kDriverPort)
    private val coDriverController = CommandXboxController(kCoDriverPort)

    // subsystems
    private val driveTrain = DriveTrain()
    // private val intake = Intake()
    // private val shooter = Shooter()
    // private val indexer = Indexer()


    init {
        // arcade control
        driveTrain.defaultCommand = driveTrain.drive(
            { deadbandOutput(driverController.leftX) },
            { deadbandOutput(driverController.leftY) },
            { deadbandOutput(driverController.rightX) }
        )

        // coDriverController.rightBumper().whileTrue(shootCommand)
        // coDriverController.leftBumper().whileTrue(intakeCommand)

        // coDriverController.a().onTrue(intake.extend { true })
        // coDriverController.b().onTrue(intake.extend { false })
    }

    // val intakeCommand: Command
    //     get() = Commands.parallel(intake.intake(), indexer.index { intake.intakeSpeed })

    // val shootCommand: Command
    //     get() = Commands.parallel(shooter.shoot(), indexer.index { shooter.shooterSpeed })


    val autonomousCommand: Command
        get() = Commands.print("No autonomous command configured")


}
