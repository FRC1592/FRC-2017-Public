
package org.usfirst.frc.team1592.robot;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

import org.usfirst.frc.team1592.arch.swerve.SwerveSubsystem;
import org.usfirst.frc.team1592.arch.utils.Match;
import org.usfirst.frc.team1592.robot.PeriodicTasks.LedState;
import org.usfirst.frc.team1592.robot.PeriodicTasks.Navigation;
import org.usfirst.frc.team1592.robot.PeriodicTasks.SDUpdater;
import org.usfirst.frc.team1592.robot.commands.Auto.FakeAuto;
import org.usfirst.frc.team1592.robot.commands.Auto.FakeAuto.AutoMode;
import org.usfirst.frc.team1592.robot.subsystems.Ballarator;
import org.usfirst.frc.team1592.robot.subsystems.GearFlapper;
import org.usfirst.frc.team1592.robot.subsystems.Trident;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
    public static SwerveSubsystem chassis;
    public static Trident trident = new Trident();
    public static Ballarator gather = new Ballarator();
    public static GearFlapper gearFlapper = new GearFlapper();
	public static final PowerDistributionPanel pdp = new PowerDistributionPanel();

    public static SDUpdater sdUpdater;
    public static Navigation nav;
    public static LedState ledState;

	public static OI oi;

    Command autonomousCommand;
    SendableChooser<FakeAuto> chooser;

    public static Match match = new Match();

    public void robotInit() {
    	chassis = new SwerveSubsystem(RobotMap.swerveDrive,RobotMap.navX,Constants.TurnPID);
		oi = new OI();
        chooser = new SendableChooser<FakeAuto>();
        
        sdUpdater = new SDUpdater();
        nav = new Navigation();
        ledState = new LedState();

        chooser.addObject("Drive over line", new FakeAuto(AutoMode.DriveOLS));
        chooser.addObject("Drive over line", new FakeAuto(AutoMode.DriveOLC));
        chooser.addObject("Gear: Left - Left", new FakeAuto(AutoMode.GearLL));
        chooser.addObject("Gear: Left - Center", new FakeAuto(AutoMode.GearLC));
        chooser.addObject("Gear: Left - Right", new FakeAuto(AutoMode.GearLR));
        chooser.addObject("Gear: Center - Left", new FakeAuto(AutoMode.GearCL));
        chooser.addObject("Gear: Center - Center", new FakeAuto(AutoMode.GearCC));
        chooser.addObject("Gear: Center - Right", new FakeAuto(AutoMode.GearCR));
        chooser.addObject("Gear: Right - Left", new FakeAuto(AutoMode.GearRL));
        chooser.addObject("Gear: Right - Center", new FakeAuto(AutoMode.GearRC));
        chooser.addObject("Gear: Right - Right", new FakeAuto(AutoMode.GearRR));
        chooser.addObject("Hopper: Left", new FakeAuto(AutoMode.HopperL));
        chooser.addObject("Hopper: Center", new FakeAuto(AutoMode.HopperC));
        chooser.addObject("Shoot: Left Gear", new FakeAuto(AutoMode.ShootLGear));
        chooser.addObject("Shoot: Hopper", new FakeAuto(AutoMode.ShootHopper));
        
        SmartDashboard.putData("Auto mode", chooser);
        SmartDashboard.putData("Swerve", chassis);
        SmartDashboard.putData("Trident", trident);
        SmartDashboard.putData("NavX", RobotMap.navX);
        
        //Telemetry        
        RobotMap.dataStream.addDataSupplier("Voltage", ()->pdp.getVoltage());
        RobotMap.dataStream.addDataSupplier("PosX", ()->nav.getPosition_FT().getX());
        RobotMap.dataStream.addDataSupplier("PosY", ()->nav.getPosition_FT().getY());
//        OperatingSystemMXBean bean = ManagementFactory.getOperatingSystemMXBean();
//		RobotMap.dataStream.addDataSupplier("CPU", ()->bean.getSystemLoadAverage());
        
        //Initialize robot position and pose
    	Robot.nav.setPosition(0.0,0.0);
    }

    public void disabledInit(){
    	match.disabledInit();
    	System.out.println("Disabled");

		RobotMap.gearFlap.set(0);
    	
    	//Write the disable init data to the log
//        RobotMap.dataLogger.outputEvent("DisableInit");
		RobotMap.dataLogger.pause();
        
    	//Flush any remaining data in the logging buffer
//		RobotMap.dataLogger.flush();
    }
	
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

    public void autonomousInit() {
    	RobotMap.dataLogger.run();
    	
    	Robot.chassis.setRobotAngle(180d);
    	Robot.chassis.setSetpoint(Robot.chassis.getAngle_Deg());
    	match.autoInit();
    	System.out.println(((FakeAuto)chooser.getSelected()).getRealCommand().getClass().getSimpleName()); //Print out the selected auto
        autonomousCommand = ((FakeAuto) chooser.getSelected()).getRealCommand();
        
        //Write the auto init data to the log
//        RobotMap.dataLogger.outputEvent("AutoInit");
        //Log Data
//        RobotMap.dataLogger.outputData();
    	
    	// schedule the autonomous command (example)
        if (autonomousCommand != null) autonomousCommand.start();
    }

    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
        //Log Data
//        RobotMap.dataLogger.outputData();
    }

    public void teleopInit() {
    	RobotMap.dataLogger.run();
    	
    	match.teleopInit();
    	
    	//Write the teleop event to logs
//        RobotMap.dataLogger.outputEvent("TeleopInit");
        
    	//Log data
//        RobotMap.dataLogger.outputData();
//        
        if (autonomousCommand != null) autonomousCommand.cancel();
        
        //Slot 1 is feed forward only; 0 has closed-loop control
        chassis.setVelPIDFDrive(0);
    }

    public void teleopPeriodic() {
    	//TODO: temp
//        System.out.println("===========");
//        System.out.println("vel act: " + Robot.chassis.getVelocityBody_FPS());
        Scheduler.getInstance().run();
        
    	//Log data
//        RobotMap.dataLogger.outputData();
    }

    public void testPeriodic() {
    	RobotMap.dataLogger.run();
        LiveWindow.run();
    }
}
