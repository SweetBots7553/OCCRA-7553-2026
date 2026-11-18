package frc.robot.constants

import edu.wpi.first.math.MathUtil
import edu.wpi.first.wpilibj2.command.button.CommandXboxController
import frc.robot.RobotCommands
import frc.robot.subsystems.drive.DriveTrain

object OperatorConstants {
    const val K_DEADBAND_THRESHOLD = 0.20

    fun driverController(driveTrain: DriveTrain, commands: RobotCommands): CommandXboxController {
        val controller = CommandXboxController(0)

        driveTrain.defaultCommand = driveTrain.drive(
            { -MathUtil.applyDeadband(controller.leftY, K_DEADBAND_THRESHOLD) },
            { -MathUtil.applyDeadband(controller.rightX, K_DEADBAND_THRESHOLD) }
        )
        
        //   driveTrain.defaultCommand = driveTrain.drive(
        //     { -MathUtil.applyDeadband(controller.leftY, K_DEADBAND_THRESHOLD) },
        //     { -MathUtil.applyDeadband(MathUtil.clamp(controller.rightX + driveTrain.alignFactor, -1.0, 1.0), K_DEADBAND_THRESHOLD) }
        // )
        
        // controller.leftBumper().whileTrue(driveTrain.drive(
        //     { -MathUtil.applyDeadband(controller.leftY, K_DEADBAND_THRESHOLD) },
        //     { -MathUtil.applyDeadband(MathUtil.clamp(controller.rightX + driveTrain.alignFactor, -1.0, 1.0), K_DEADBAND_THRESHOLD) }
        // ))
        
        controller.a().onTrue(driveTrain.updateRelativePosition)
        
        
        
        return controller
    }

    fun coDriverController(commands: RobotCommands): CommandXboxController {
        val controller = CommandXboxController(1)

        controller.b().whileTrue(commands.intakeCommand).onFalse(commands.stopIntake)
        controller.leftBumper().whileTrue(commands.outTakeCommand).onFalse(commands.stopOutTaking)
        controller.a().whileTrue(commands.shootCommand).onFalse(commands.stopShootCommand)

        return controller
    }
    
    fun debugController(driveTrain: DriveTrain, commands: RobotCommands): CommandXboxController {
        
        val controller = CommandXboxController(1)

        // controller.b().whileTrue(commands.intakeCommand).onFalse(commands.stopIntake)
        // controller.leftBumper().whileTrue(commands.outTakeCommand).onFalse(commands.stopOutTaking)
        // controller.a().whileTrue(commands.shootCommand).onFalse(commands.stopShootCommand)
        
        
        
        driveTrain.defaultCommand = driveTrain.drive(
            { -MathUtil.applyDeadband(controller.leftY, K_DEADBAND_THRESHOLD) },
            { -MathUtil.applyDeadband(controller.rightX, K_DEADBAND_THRESHOLD) }
        )
        

        
        return controller
    }
}