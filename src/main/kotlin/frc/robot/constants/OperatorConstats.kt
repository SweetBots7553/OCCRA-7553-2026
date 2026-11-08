package frc.robot.constants

import edu.wpi.first.math.MathUtil
import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.wpilibj2.command.button.CommandXboxController
import frc.robot.RobotCommands
import frc.robot.subsystems.drive.DriveTrain
import kotlin.math.abs

object OperatorConstats {
    const val K_DEADBAND_THRESHOLD = 0.20

    fun driverController(driveTrain: DriveTrain, commands: RobotCommands): CommandXboxController {
        val controller = CommandXboxController(0)

        driveTrain.defaultCommand = driveTrain.drive(
            { -MathUtil.applyDeadband(controller.leftY, K_DEADBAND_THRESHOLD) },
            { -MathUtil.applyDeadband(controller.rightX, K_DEADBAND_THRESHOLD) }
        )

        return controller
    }

    fun coDriverController(commands: RobotCommands): CommandXboxController {
        val controller = CommandXboxController(1)

        controller.b().whileTrue(commands.intakeCommand).onFalse(commands.stopIntake)
        controller.leftBumper().whileTrue(commands.outTakeCommand).onFalse(commands.stopOutTaking)
        controller.a().whileTrue(commands.shootCommand).onFalse(commands.stopShootCommand)

        return controller
    }
}