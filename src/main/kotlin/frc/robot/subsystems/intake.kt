package frc.robot.subsystems

<<<<<<< HEAD
import com.revrobotics.spark.SparkBase
import com.revrobotics.spark.SparkLowLevel
import com.revrobotics.spark.SparkMax
import edu.wpi.first.units.measure.AngularVelocity
import edu.wpi.first.wpilibj.PneumaticsModuleType
import edu.wpi.first.wpilibj.Solenoid
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.constants.kIntakeSpeed
import frc.robot.constants.kMotorCANID
import frc.robot.constants.kPneumaticDosChannel
import frc.robot.constants.kPneumaticUnoChannel
import java.util.concurrent.locks.ReentrantLock

class Intake : SubsystemBase()  {
    private val intakeMotor = SparkMax(kMotorCANID, SparkLowLevel.MotorType.kBrushless)
    
    private val penumaticUno = Solenoid(PneumaticsModuleType.CTREPCM, kPneumaticUnoChannel)
    private val penumaticDos = Solenoid(PneumaticsModuleType.CTREPCM, kPneumaticDosChannel)
    
    private val penumaticLock: ReentrantLock = ReentrantLock()
    
    fun extended(b: Boolean) { 
        if (b == penumaticUno.get())
            return
        
        // this just so you don't set it too fast
        synchronized(penumaticLock) {
            penumaticUno.set(b)
            penumaticDos.set(b)

            Thread.sleep(1)
        }
    }
    
    fun extend(b: () -> Boolean): Command {
        return runOnce {
            extended(b())
        }
    }
    
    fun intake(speed: Double) {
        intakeMotor.closedLoopController.setSetpoint(speed, SparkBase.ControlType.kVelocity)
    }
    
    fun intake(): Command {
        return runEnd({
            intake(kIntakeSpeed)
        }, {
            intake(0.0)
        })
    }
=======
import com.revrobotics.spark.SparkLowLevel.MotorType
import com.revrobotics.spark.SparkMax
import edu.wpi.first.wpilibj.PneumaticsModuleType
import edu.wpi.first.wpilibj.Solenoid
import edu.wpi.first.wpilibj2.command.SubsystemBase

class Intake : SubsystemBase() {
    
    private val intakeMotor = SparkMax(0, MotorType.kBrushless)
    
    private val extender = Solenoid(PneumaticsModuleType.CTREPCM, 0)
    private val extenderTwin = Solenoid(PneumaticsModuleType.CTREPCM, 0)
    
    init {
        // intakeMotor.configure()
    }
    
    
    
>>>>>>> refs/remotes/origin/main
}