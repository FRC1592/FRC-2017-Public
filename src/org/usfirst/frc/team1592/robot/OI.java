package org.usfirst.frc.team1592.robot;

import org.usfirst.frc.team1592.robot.commands.SetChassisFeedFwdOnly;
import org.usfirst.frc.team1592.robot.commands.SetGyroAngle;
import org.usfirst.frc.team1592.robot.commands.Ballarator.Eject;
import org.usfirst.frc.team1592.robot.commands.Ballarator.Gather;
import org.usfirst.frc.team1592.robot.commands.Chassis.DriveAboutPoint;
import org.usfirst.frc.team1592.robot.commands.Chassis.DriveAboutPoint.PivotQuad;
import org.usfirst.frc.team1592.robot.commands.Chassis.ToggleFieldOriented;
import org.usfirst.frc.team1592.robot.commands.GearFlapper.GatherGear;
import org.usfirst.frc.team1592.robot.commands.GearFlapper.GearEject;
import org.usfirst.frc.team1592.robot.commands.GearFlapper.GearStow;
import org.usfirst.frc.team1592.robot.commands.GearFlapper.Slurp;
import org.usfirst.frc.team1592.robot.commands.Trident.Climb;
import org.usfirst.frc.team1592.robot.commands.Trident.Shoot;
import org.usfirst.frc.team1592.robot.commands.Trident.SnugClimb;
import org.usfirst.frc.team1592.robot.commands.Trident.StopShooter;
import org.usfirst.frc.team1592.robot.triggers.MotorStalled;
import org.usfirst.frc.team1592.robot.triggers.SlurpGear;

import com.qtech.first.qLibs.joysticks.XboxGamepad;
import com.qtech.first.qLibs.joysticks.XboxButton;
import com.qtech.first.qLibs.joysticks.XboxButton.ButtonName;

import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.Trigger;

/**
Left Trigger (Back)                   Right Trigger (Back)

   _.-'BUMP `-._                          _,-'BUMP'-._
,-'             `-.,__________________,.-'      .-.    `-.
/      .-Y .                ___                ( Y )      \
/    ,' .-. `.      ____   / X \   _____    .-. `-` .-.    \
/   -X |   | +X    (Back) | / \ | (Start)  ( X )   ( B )   |
/    `. `-' ,'    __       \___/            `-` ,-. `-`    |
|      `+Y `   ,-`  `-.          .-Y .         ( A )       |
|             / -'  `- \       ,'  .  `.        `-`        |
|            |    POV   |     -X -  - +X                   |
!             \ -.  ,- /       `.  '  ,'                   |
|              `-.__,-'          `+Y `                     |
|                  ________________                        /
|             _,-'`                ``-._                  /
|          ,-'                          `-.              /  Based on 10/10 ASCII ART BYosrevad
\       ,'                                 `.           /
 `.__,-'                                     `-.______,'     */

public class OI {
  //Joysticks
    public XboxGamepad driver = new XboxGamepad(0);
    public XboxGamepad manip = new XboxGamepad(1);
    
  //Buttons
    //Chassis
    private Button changeFieldOriented = new XboxButton(driver,ButtonName.START);
    private Button resetGyro = new XboxButton(driver,ButtonName.BACK);
    
    //Trident
    private Button shoot = new XboxButton(manip, ButtonName.RIGHT_BUMPER);
    private Button stopShooter = new XboxButton(manip, ButtonName.RIGHT_TRIGGER);
    private Button climb = new XboxButton(manip, ButtonName.START);
    private Button snugClimb = new XboxButton(manip, ButtonName.BACK);
    
    public Trigger climbLimit = new MotorStalled(RobotMap.tridentMaster, 70, 100);
    
    //Ballarator
    private Button gather = new XboxButton(driver, ButtonName.LEFT_BUMPER);
    private Button eject = new XboxButton(driver, ButtonName.LEFT_TRIGGER);
//    private Button test = new XboxButton(driver, ButtonName.Y);
    private Button lockNorthQuad = new XboxButton(driver, ButtonName.Y);
    private Button lockEastQuad = new XboxButton(driver, ButtonName.B);
    private Button lockSouthQuad = new XboxButton(driver, ButtonName.A);
    private Button lockWestQuad = new XboxButton(driver, ButtonName.X);
    
    //GearFlapper
    private Button gearGather = new XboxButton(driver, ButtonName.RIGHT_BUMPER);
    private Button triggered = new XboxButton(driver, ButtonName.LEFT_BUMPER); //Because kyle forgets what button to use
    
    private Button stowGearFlap = new XboxButton(manip, ButtonName.Y);
    private Button deployGearFlap = new XboxButton(manip, ButtonName.A);
    //TODO: temporary
//    private Button drive2distTest = new XboxButton(manip,ButtonName.B);
    private Trigger slurpGear = new SlurpGear();
    
    // Center on Boiler
//    private Button centerOnBoiler = new XboxButton(manip, ButtonName.X);
    
    // Center on Gear
//    private Button dockOnGear = new XboxButton(manip, ButtonName.BACK);
//    private Button centerOnGear = new XboxButton(manip, ButtonName.B);
    
    // TODO: Add to Master: Fake Auto during telemetry
    //private Button fakeAuto = new XboxButton(manip, ButtonName.B);
    
    public OI() {
//    	test.whenPressed(new SetShooterSpeeds(1d));
    	
//    	drive2distTest.whileHeld(new Drive2Point(new Vector(10,0)));
    	
      //Buttons
    	//Chassis
    	changeFieldOriented.whenPressed(new ToggleFieldOriented());
//    	lockNorthQuad.whileHeld(new DriveAboutPoint(PivotQuad.north));
    	lockNorthQuad.whenPressed(new SetChassisFeedFwdOnly());
    	lockEastQuad.whileHeld(new DriveAboutPoint(PivotQuad.east));
//    	lockEastQuad.whenPressed(new Turn2HeadingMP(90.0,new PIDConfig(Constants.TurnPID.kP, Constants.TurnPID.kI, 0.0)));
    	lockSouthQuad.whileHeld(new DriveAboutPoint(PivotQuad.south));
    	lockWestQuad.whileHeld(new DriveAboutPoint(PivotQuad.west));
    	resetGyro.whenPressed(new SetGyroAngle(180.0));
    	
    	//Trident
    	shoot.whileHeld(new Shoot());
    	stopShooter.whileHeld(new StopShooter());
    	climb.whileHeld(new Climb());
    	snugClimb.whileHeld(new SnugClimb());
    	
    	//Ballarator
    	gather.whileHeld(new Gather());
    	eject.whileHeld(new Eject());
    	
    	//GearFlapper
    	gearGather.whileHeld(new GatherGear());
    	triggered.whileHeld(new GatherGear());
//    	gearEject.whileHeld(new EjectGear());
    	deployGearFlap.whileHeld(new GearEject());
    	stowGearFlap.whileHeld(new GearStow());
    	
    	//Triggers
    	slurpGear.whenActive(new Slurp());
    	
    	// Vision
//    	centerOnBoiler.whileHeld(new CenterOnBoiler());
//    	centerOnGear.whileHeld(new CenterOnGear());
    	//centerOnGear.whileHeld(new CenterOnGear(9));
    	
    	// TODO: Add to master: Fake Auto during telemetry
    	//fakeAuto.whileHeld(new FakeAuto());
    } //lim h>0 (sin(3(x+h))-sin(x)
//                      h
}

