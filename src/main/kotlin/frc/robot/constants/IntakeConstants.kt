package frc.robot.constants

import com.ctre.phoenix.motorcontrol.can.VictorSPXConfiguration

object IntakeConstants {
    // dutycycle
    const val kIntakeSpeed = 0.75
    const val kIntakeMotorCANID = 31

    val kIntakeMotorConfig: VictorSPXConfiguration
        get() {
            val config = VictorSPXConfiguration()

            
            return config
        }

    const val kPneumaticChannel = 0
    const val kPneumaticHubCANID = 0
}