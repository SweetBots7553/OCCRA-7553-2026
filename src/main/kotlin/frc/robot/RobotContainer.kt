package frc.robot

import com.pathplanner.lib.auto.AutoBuilder
import com.pathplanner.lib.auto.NamedCommands
import edu.wpi.first.math.MathUtil
import edu.wpi.first.networktables.NetworkTable
import edu.wpi.first.networktables.NetworkTableInstance
import edu.wpi.first.networktables.NetworkTablesJNI
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
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

class RobotContainer {
    // operator controllers
    private val driverController = CommandXboxController(kDriverPort)
    private val coDriverController = CommandXboxController(kCoDriverPort)

    // subsystems
    private val driveTrain = DriveTrain
    private val intake = Intake
    private val shooter = Shooter
    private val indexer = Indexer
    
    // pathplanner autos
    init {
        NamedCommands.registerCommand("Intake", intakeCommand)
        NamedCommands.registerCommand("OutTake", outTakeCommand)
        NamedCommands.registerCommand("Shoot", shootCommand)
        NamedCommands.registerCommand("Extend", extend)
        NamedCommands.registerCommand("UnExtend", unextend)
    }

    // controls
    init {
        // arcade control
        driveTrain.defaultCommand = driveTrain.drive(
            { MathUtil.applyDeadband(driverController.rightX, kDeadbandThreshold) },
            { MathUtil.applyDeadband(driverController.leftY, kDeadbandThreshold) }
        )
        
        coDriverController.b().whileTrue(intakeCommand).onFalse(unextend)       
    }
    // commands

    private val intakeCommand: Command
        get() {
            return Commands.sequence(extend, intake.intake { kIntakeSpeed }.alongWith(indexer.index { kIntakeSpeed }))

        }
    private val outTakeCommand: Command
        get() = intake.intake { -kIntakeSpeed }.alongWith(indexer.index { -kIntakeSpeed })

    private val shootCommand: Command
        get() = shooter.shoot()

    private val extend: Command
        get() = intake.extend { true }

    private val unextend: Command
        get() = intake.extend { false }

    // auto

    // private val autoChooser = SendableChooser<String>()

    // init {
    //     val tab = Shuffleboard.getTab("Auto")

    //     autoChooser.addOption("test", "test")


    //     tab.add { autoChooser }
    // }


    // val autonomousCommand: Command
    //     get() = AutoBuilder.buildAuto(autoChooser.selected)
        
    val autonomousCommand: Command
        get() = Commands.none()


}
