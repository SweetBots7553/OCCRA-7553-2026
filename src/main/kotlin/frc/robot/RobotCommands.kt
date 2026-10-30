package frc.robot

import com.pathplanner.lib.auto.NamedCommands
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.constants.IndexerConstants
import frc.robot.constants.IntakeConstants
import frc.robot.constants.ShooterConstants
import frc.robot.subsystems.Indexer
import frc.robot.subsystems.Intake
import frc.robot.subsystems.Shooter

class RobotCommands(private val intake: Intake, private val shooter: Shooter,  private val indexer: Indexer) : SubsystemBase() {
    
    init {
        
    }

    private val extend: Command 
    get() {
        return intake.extend { true }
    }
    
    private val unextend: Command
    get() {
        return intake.extend { false }
    }
    
    private val primeShooter: Command
    get(){
        val indexBackup = indexer.index { -IndexerConstants.kIndexerSpeed }.andThen( Commands.waitSeconds(0.1)).andThen(indexer.stop())
        val shooterPrime = shooter.shoot { ShooterConstants.kShooterSpeed }
        
        return indexBackup.andThen(shooterPrime).onlyIf { shooter.isShooterPrimed() }
    }
           
    val shootCommand: Command
    get() {
        return primeShooter.andThen(indexer.index { IndexerConstants.kIndexerSpeed })
    }
    
    private val stopShootCommand: Command
    get() {
        return indexer
            .stop()
            .andThen(shooter.stop())
    }
    

    val intakeCommand: Command
    get() {
        val i = extend
            .andThen(intake.intake { IntakeConstants.kIntakeSpeed }
            .alongWith(indexer.index { IndexerConstants.kIndexerSpeed  }))
            
        return i
    }
    
    private val stopIntake: Command = Commands.sequence(
        indexer.stop(), 
            intake.stop(), 
            shooter.stop(), 
            unextend
        )
        
    
    
    
    
    
    //  init {
    //     NamedCommands.registerCommand("Intake", intakeCommand())
    //     NamedCommands.registerCommand("OutTake", outTakeCommand())
    //     NamedCommands.registerCommand("Shoot", shootCommand())
    //     NamedCommands.registerCommand("StopIntake", stopIntake())
    //     NamedCommands.registerCommand("StopShooter", stopShootCommand())
    // }

    
       
    // private fun outTakeCommand(): Command = extend().andThen(intake.intake { -IntakeConstants.kIntakeSpeed }.alongWith(indexer.index { -IndexerConstants.kIndexerSpeed  })).alongWith(shooter.outShoot())
   
    // private fun shootCommand(): Command = Commands.parallel(extend(), shooter.shoot(), Commands.waitSeconds(0.5).andThen(indexer.index { IndexerConstants.kIndexerSpeed }))
        
    // private fun primeCommand(): Command = Commands.sequence(indexer.index { -IndexerConstants.kIndexerSpeed * 0.5 }, Commands.waitSeconds(0.2), indexer.index { 0.0 }, shooter.shoot())
    
    // private fun stopShootCommand(): Command = indexer.index { 0.0 }.andThen(shooter.stop())
}
