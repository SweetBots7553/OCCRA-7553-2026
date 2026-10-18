package frc.robot.subsystems.drive

import com.pathplanner.lib.auto.AutoBuilder
import edu.wpi.first.wpilibj.Filesystem
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands

class Auto(vararg options: String) {
    
    private val autoChooser = SendableChooser<String>()
    
    init {
        autoChooser.setDefaultOption("None", "None")
        
        options.forEach { o ->
            autoChooser.addOption(o, o)    
        }

        SmartDashboard.putData { autoChooser }
    }
    
    val auto: Command 
        get() {
            if (autoChooser.selected == "None") {
                return Commands.none()
            }
            
            return AutoBuilder.buildAuto(autoChooser.selected)
        }
    
}

// this most likely isn't going to be used, but I thought it would be cool to implement. 
// fun genAutoEntries(): Auto {
//     val deployDirectory = Filesystem.getDeployDirectory()

//     deployDirectory.toPath("")
    
    
    
// }
