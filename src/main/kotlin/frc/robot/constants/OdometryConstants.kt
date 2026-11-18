package frc.robot.constants

import com.pathplanner.lib.config.RobotConfig
import com.pathplanner.lib.controllers.PPLTVController
import com.pathplanner.lib.controllers.PathFollowingController

object OdometryConstants {

    val kPathController = PPLTVController(0.1)
    val kPigionCAN = 62 

    const val kAlignmentSpeed = 2.0
    
    const val kP = 0.1
    const val kD = 0.0
    

}