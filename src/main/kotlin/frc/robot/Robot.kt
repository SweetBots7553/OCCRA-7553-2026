package frc.robot

import edu.wpi.first.cameraserver.CameraServer
import edu.wpi.first.net.WebServer
import edu.wpi.first.wpilibj.DataLogManager
import edu.wpi.first.wpilibj.Filesystem
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.CommandScheduler
import edu.wpi.first.wpilibj.TimedRobot
import frc.robot.utils.Elastic
import org.littletonrobotics.urcl.URCL

object Robot : TimedRobot() {
    private var autonomousCommand: Command? = null
    private var robotContainer: RobotContainer? = null

    override fun robotInit() {
        
        // logging unoffical 
        DataLogManager.start()
        URCL.start()
        URCL.start(DataLogManager.getLog())
        
        // saving layouts for elastic
        WebServer.start(5800, Filesystem.getDeployDirectory().canonicalPath)
        
        // enabling usb cameras
        CameraServer.startAutomaticCapture()
        
        robotContainer = RobotContainer
    }

    override fun robotPeriodic() {
        CommandScheduler.getInstance().run()
    }

    override fun disabledInit() {}

    override fun disabledPeriodic() {}

    override fun disabledExit() {}

    override fun autonomousInit() {
        autonomousCommand = robotContainer?.autonomousCommand

        CommandScheduler.getInstance().schedule(autonomousCommand)
    }

    override fun autonomousPeriodic() {}

    override fun autonomousExit() {

    }

    override fun teleopInit() {
        autonomousCommand?.cancel()
    }

    override fun teleopPeriodic() {}

    override fun teleopExit() {}

    override fun testInit() {
        CommandScheduler.getInstance().cancelAll()
    }

    override fun testPeriodic() {}

    override fun testExit() {}
}
