package frc.robot

import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import edu.wpi.first.wpilibj2.command.button.CommandXboxController
import frc.robot.constants.deadbandOutput
import frc.robot.constants.kCoDriverPort
import frc.robot.constants.kDriverPort
import frc.robot.subsystems.DriveTrain
import frc.robot.subsystems.Intake

class RobotContainer {
    
    // operator controllers
    private val driverController = CommandXboxController(kDriverPort)
    private val coDriverController = CommandXboxController(kCoDriverPort)  
    
    // subsystems
    private val driveTrain = DriveTrain()
    private val intake = Intake()

    init {
        driveTrain.defaultCommand = driveTrain.drive(
            { deadbandOutput(driverController.leftX) },
            { deadbandOutput(driverController.leftY) },
            { deadbandOutput(driverController.rightX) }
        )
    }

    val autonomousCommand: Command
        get() = Commands.print("No autonomous command configured")
}
