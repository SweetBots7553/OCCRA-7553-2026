package frc.robot

import com.pathplanner.lib.auto.NamedCommands
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.constants.IndexerConstants
import frc.robot.constants.IntakeConstants
import frc.robot.constants.ShooterConstants
import frc.robot.subsystems.Indexer
import frc.robot.subsystems.Intake
import frc.robot.subsystems.Shooter

class RobotCommands(
    private val intake: Intake,
    private val shooter: Shooter,
    private val indexer: Indexer
) :
    SubsystemBase() {
        
        
    private val hasBalls: Boolean 
        get() {
            return intake.countedBalls >= 2
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
        get() {
            val disableSensor = intake.disableSensor(true)

            val backup =
                indexer.index { -IndexerConstants.kIndexerSpeed }
                    .andThen(Commands.waitSeconds(0.4))
                    .andThen(indexer.stop())

            val runShooter = shooter.shoot { ShooterConstants.kShooterSpeed }

            return Commands.sequence(extend, shooter.shoot { -ShooterConstants.kShooterSpeed }, runShooter.alongWith(backup))
        }

    val shootCommand: Command
        get() {
            val shoot = indexer.index { IndexerConstants.kIndexerSpeed }
            
            return primeShooter.andThen(Commands.waitUntil { shooter.isShooterPrimed }.andThen(shoot).andThen(intake.clearCount()))
        }

    val stopShootCommand: Command
        get() {
            return indexer
                .stop()
                .andThen(shooter.stop()).andThen(unextend)
        }


    val intakeCommand: Command
        get() {
            val enableSensor = intake.disableSensor(false)

            val intakeStart = extend
                .andThen(intake.intake { IntakeConstants.kIntakeSpeed }
                    .alongWith(indexer.index { IndexerConstants.kIndexerSpeed }))

    

            return enableSensor.andThen(intakeStart)
        }
        
    val outTakeCommand: Command 
        get(){
            val enableSensor = intake.disableSensor(false)
            
            val intakeStart = extend
                .andThen(intake.intake { -IntakeConstants.kIntakeSpeed }
                    .alongWith(indexer.index { -IndexerConstants.kIndexerSpeed }))
            val shooterStart = shooter.shoot { -ShooterConstants.kShooterSpeed }
                    
                    
            return enableSensor.andThen(intakeStart.alongWith(shooterStart)).andThen(intake.clearCount())
        }
    


    val stopIntake: Command
    get() {
        return Commands.waitUntil { hasBalls }.andThen(intake.stop().andThen(indexer.stop()).andThen(unextend))
    }

    val stopOutTaking: Command
    get() {
        return Commands.waitUntil { !hasBalls }.andThen(intake.stop().andThen(indexer.stop().alongWith(shooter.stop())).andThen(unextend))
    }
    
    override fun periodic() { 
        // SmartDashboard.putString("CoDriver/Current_CoDriver_Commands", currentCommand.name)
    }
    
}
