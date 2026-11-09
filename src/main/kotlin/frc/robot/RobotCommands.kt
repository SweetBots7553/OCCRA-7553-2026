package frc.robot

import com.pathplanner.lib.auto.NamedCommands
import edu.wpi.first.wpilibj.event.BooleanEvent
import edu.wpi.first.wpilibj.event.EventLoop
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.Commands
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.subsystems.Indexer
import frc.robot.subsystems.Intake
import frc.robot.subsystems.Shooter

class RobotCommands(
    private val intake: Intake,
    private val shooter: Shooter,
    private val indexer: Indexer
) :
    SubsystemBase() {

    private val eventLoop: EventLoop = EventLoop()
    private val hasBallEvent: BooleanEvent = BooleanEvent(eventLoop) { intake.hasBalls }
    private var automatic: Boolean = false

    init {
        // commands auto
        NamedCommands.registerCommand("shoot", shootCommand)
        NamedCommands.registerCommand("stop-shoot", stopShootCommand)

        NamedCommands.registerCommand("intake", intakeCommand)
        NamedCommands.registerCommand("stop-intake", stopIntake)

        NamedCommands.registerCommand("outTake", outTakeCommand)
        NamedCommands.registerCommand("stop-outTake", stopOutTaking)

        hasBallEvent.ifHigh {
            if (automatic) {

            }
        }

    }

    private val primeShooter: Command
        get() {
            val disableSensor = intake.enabled(false)

            val backup =
                Commands.sequence(
                    indexer.outdexing,
                    Commands.waitSeconds(0.4),
                    indexer.stop
                )

            return Commands.sequence(
                intake.extend,
                shooter.intake,
                backup,
                shooter.shoot
            )
        }

    val shootCommand: Command
        get() {
            return Commands.sequence(
                primeShooter,
                Commands.waitUntil { shooter.isShooterPrimed },
                indexer.indexing,
                intake.clearCount
            )
        }

    val intakeCommand: Command
        get() {
            val enableSensor = intake.enabled(true)

            val intakeStart =
                Commands.sequence(intake.extend, intake.intake.alongWith(indexer.indexing))

            return enableSensor.andThen(intakeStart)
        }

    val outTakeCommand: Command
        get() {
            val enableSensor = intake.enabled(true)

            val intakeStart = intake.extend.andThen(intake.outTake.alongWith(indexer.outdexing))

            return enableSensor.andThen(intakeStart.alongWith(shooter.shoot)).andThen(intake.clearCount)
        }


    private val stop: Command = Commands.sequence(intake.stop, indexer.stop, shooter.stop, intake.unextend)

    val stopIntake: Command get() = Commands.sequence(Commands.waitUntil { intake.hasBalls }, stop)

    val stopShootCommand: Command get() = Commands.sequence(indexer.stop, shooter.stop, intake.unextend)

    val stopOutTaking: Command get() = Commands.sequence(Commands.waitUntil { !intake.hasBalls }, stop)

    val toggleAutomatic: Command get() = runOnce { }


    override fun periodic() {
        eventLoop.poll()
    }
}