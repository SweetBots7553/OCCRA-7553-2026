package frc.robot.subsystems

import com.revrobotics.PersistMode
import com.revrobotics.ResetMode
import com.revrobotics.spark.SparkBase
import com.revrobotics.spark.SparkLowLevel
import com.revrobotics.spark.SparkMax
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.constants.kIndexerConfig
import frc.robot.constants.kIndexerMotorCANID
import frc.robot.constants.kShooterConfig
import frc.robot.constants.kShooterMotorCANID
import frc.robot.constants.kShooterSpeed

class Shooter : SubsystemBase() {

    private val shooterMotor = SparkMax(kShooterMotorCANID, SparkLowLevel.MotorType.kBrushless)

    init {
        shooterMotor.configure(kShooterConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters)
    }

    fun shoot(speed: Double) {
        shooterMotor.closedLoopController.setSetpoint(speed, SparkBase.ControlType.kVelocity)
    }

    fun shoot(): Command {
        return startEnd({
            shoot(kShooterSpeed)
        }, {
            shoot(0.0)
        })
    }
}