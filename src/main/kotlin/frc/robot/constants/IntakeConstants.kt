package frc.robot.constants

import com.ctre.phoenix.motorcontrol.can.VictorSPXConfiguration

// dutycycle
const val kIntakeSpeed = 0.5
const val kIntakeCANID = 31

val kIntakeConfig: VictorSPXConfiguration
    get() {
        val config = VictorSPXConfiguration()

        return config
    }

const val kPneumaticChannel = 0
const val kPneumaticHubCANID = 0


