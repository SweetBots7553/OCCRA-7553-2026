package frc.robot

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
    
    private val primeShooter: Command
        get() {
            val disableSensor = intake.disableSensor(true)

            val backup = 
                Commands.sequence(indexer.outdexing, 
                Commands.waitSeconds(0.4), 
                indexer.stop)

            return Commands.sequence(
                intake.extend, 
                    shooter.intake, 
                    backup, 
                    shooter.shoot)
        }

    val shootCommand: Command
        get() {
            val shoot = indexer.indexing.alongWith(shooter.shoot)
            
            return Commands.sequence(primeShooter, Commands.waitSeconds(0.8), shoot, intake.clearCount)
        }

    val intakeCommand: Command
        get() {
            val enableSensor = intake.disableSensor(false)

            val intakeStart = 
                Commands.sequence(
                    intake.extend, 
                        intake.intake
                            .alongWith(indexer.indexing))

            return enableSensor.andThen(intakeStart)
        }
        
    val outTakeCommand: Command 
        get(){
            val enableSensor = intake.disableSensor(false)
            
            val intakeStart = intake.extend.andThen(intake.outTake.alongWith(indexer.outdexing))   
        
            return enableSensor.andThen(intakeStart.alongWith(shooter.shoot)).andThen(intake.clearCount)
        }
    

    private val stop: Command = Commands.sequence(intake.stop, indexer.stop, shooter.stop, intake.unextend)

    val stopIntake: Command = Commands.sequence(Commands.waitUntil { intake.hasBalls }, stop)
    
    val stopShootCommand: Command = Commands.sequence(indexer.stop, shooter.stop, intake.unextend)
    
    val stopOutTaking: Command = Commands.sequence(Commands.waitUntil { !intake.hasBalls }, stop)
}