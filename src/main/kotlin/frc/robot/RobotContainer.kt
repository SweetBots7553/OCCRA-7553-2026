package frc.robot

import com.pathplanner.lib.auto.NamedCommands
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import edu.wpi.first.wpilibj2.command.button.CommandXboxController
import frc.robot.constants.deadbandOutput
import frc.robot.constants.kCoDriverPort
import frc.robot.constants.kDriverPort
import frc.robot.constants.kIntakeSpeed
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
        regesterAutos()
        configBinds()
    }
    
    // pathplanner autos
    private fun regesterAutos() {
        // NamedCommands.registerCommand("Intake",  intakeCommand)
        // NamedCommands.registerCommand("OutTake", outTakeCommand)
        // NamedCommands.registerCommand("Shoot", shootCommand)
        // NamedCommands.registerCommand("Extend", intake.extend { true })
        // NamedCommands.registerCommand("UnExtend", intake.extend { false })
    }
    
    // controls
    private fun configBinds() {
        // arcade control
        driveTrain.defaultCommand = driveTrain.drive(
            { deadbandOutput(driverController.leftX) },
            { deadbandOutput(driverController.leftY) },
            { deadbandOutput(driverController.rightX) }
        )
    } 

    // commands
    
    // private val intakeCommand: Command
    //     get() = Commands.parallel(intake.intake { kIntakeSpeed }, indexer.index { intake.intakeSpeedDutyCycle })
        
    // private val outTakeCommand: Command
    //     get() = Commands.parallel(intake.intake { -kIntakeSpeed }, indexer.index { intake.intakeSpeedDutyCycle })

    // private val shootCommand: Command
    //     get() = Commands.parallel(shooter.shoot(), indexer.index { shooter.shooterSpeedDutyCycle })
        
    
    val autonomousCommand: Command
        get() = Commands.print("No autonomous command configured")
        
}
