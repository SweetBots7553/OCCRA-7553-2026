package frc.robot.subsystems.drive

import com.pathplanner.lib.auto.AutoBuilder
import edu.wpi.first.wpilibj.Filesystem
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import edu.wpi.first.wpilibj2.command.SubsystemBase

class Auto(vararg options: String): SubsystemBase() {

    private val autoChooser = SendableChooser<String>()

    init {


        autoChooser.setDefaultOption("None", "None")

        options.forEach { o ->
            autoChooser.addOption(o, o)    
        }
    }

    val auto: Command 
        get() {
            if (autoChooser.selected == "None") {
                return Commands.none()
            }

            return AutoBuilder.buildAuto(autoChooser.selected)
        }

    override fun periodic() {
        SmartDashboard.putData("Auto/Auto_Chooser", autoChooser)
    }
}

// this most likely isn't going to be used, but I thought it would be cool to implement. 
fun genAutoEntries(): Auto {
    val deployDirectory = Filesystem.getDeployDirectory()
    val autos = deployDirectory
        .resolve("pathplanner")
        .resolve("autos")

    val entries = autos.list()
        ?.mapNotNull { auto ->
            auto.removeSuffix(".auto")
                .takeIf { it.isNotBlank() }
        }
        .orEmpty()

    entries.forEach { println(it) }

    return Auto(*entries.toTypedArray())
}