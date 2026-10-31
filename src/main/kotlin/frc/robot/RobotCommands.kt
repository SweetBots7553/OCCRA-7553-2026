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

class RobotCommands(
    private val intake: Intake,
    private val shooter: Shooter,
    private val indexer: Indexer
) :
    SubsystemBase() {

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
                    .andThen(Commands.waitSeconds(0.2))
                    .andThen(indexer.stop())

            val runShooter = shooter.shoot { ShooterConstants.kShooterSpeed }

            return Commands.sequence(disableSensor, backup, runShooter)
        }

    val shootCommand: Command
        get() {
            return primeShooter
                .andThen(indexer.index { IndexerConstants.kIndexerSpeed }
                    .onlyIf { shooter.isShooterPrimed() })
        }

    private val stopShootCommand: Command
        get() {
            return indexer
                .stop()
                .andThen(shooter.stop())
        }


    val intakeCommand: Command
        get() {
            val enableSensor = intake.disableSensor(false)

            val intakeStart = extend
                .andThen(intake.intake { IntakeConstants.kIntakeSpeed }
                    .alongWith(indexer.index { IndexerConstants.kIndexerSpeed }))

            val stop = stopIntake.onlyIf { intake.countedBalls <= 2 }

            return Commands.sequence(enableSensor, intakeStart, stop)
        }


    private val stopIntake: Command = Commands.sequence(
        indexer.stop(),
        intake.stop(),
        shooter.stop(),
        unextend
    )
}
