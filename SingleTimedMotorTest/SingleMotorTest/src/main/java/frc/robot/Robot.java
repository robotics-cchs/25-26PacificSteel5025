// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The methods in this class are called automatically corresponding to each mode, as described in
 * the TimedRobot documentation. If you change the name of this class or the package after creating
 * this project, you must also update the Main.java file in the project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  private final int tsrxMotorOnePort = 31; //Need to adjust
  private final int leftShooterMotorPort = 50;
  private final int rightShooterMotorPort = 51;
  private final int leftKickerMotorPort = 52;
  private final int rightKickerMotorPort= 55;
  private final int intakeRotatorMotorPort = 54;
  private final int intakeBallsMotorPort = 56; //Need to adjust
  private final int conveyorMotorPort = 53;

  private final int xboxControllerOnePort = 0;
  private final int xboxControllerTwoPort = 1;
  private final int joystickOnePort = 2;

  private final MotorOutputConfigs motorConfigs = new MotorOutputConfigs();

  private final TalonSRX tsrxMotorOne = new TalonSRX(tsrxMotorOnePort);
  private final TalonFX leftShooterMotor = new TalonFX(leftShooterMotorPort);
  private final TalonFX rightShooterMotor = new TalonFX(rightShooterMotorPort);
  private final TalonFX leftKickerMotor = new TalonFX(leftKickerMotorPort);
  private final TalonFX rightKickerMotor = new TalonFX(rightKickerMotorPort);
  private final TalonFX intakeRotatorMotor = new TalonFX(intakeRotatorMotorPort);
  private final TalonFX intakeBallsMotor = new TalonFX(intakeBallsMotorPort);
  private final TalonFX conveyorMotor = new TalonFX(conveyorMotorPort);
  
  private final Joystick joystickOne = new Joystick(joystickOnePort);
  private final XboxController controllerOne = new XboxController(xboxControllerOnePort);
  private final XboxController controllerTwo = new XboxController(xboxControllerTwoPort);

  private int kickshooterIncrCount = 0;
  private int shooterOnVal = 0;
  private int kickerOnVal = 0;
  private int intakeBallsOnVal = 0;
  private int intakeRotatorOnVal = 0;
  private int conveyorOnVal = 0;

  private double tsrxMotorOneSpeed = 0;
  private double kickshootSpeed = 0.4;
  private double intakeBallsSpeed = 0.05;
  private double intakeRotatorSpeed = 0.01;
  private double conveyorSpeed = 0.15;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  public Robot() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    tsrxMotorOne.configFactoryDefault();
    tsrxMotorOne.setInverted(true);

    rightShooterMotor.getConfigurator().apply(new TalonFXConfiguration());
    motorConfigs.Inverted = InvertedValue.CounterClockwise_Positive;
    rightShooterMotor.getConfigurator().apply(motorConfigs);

    leftShooterMotor.getConfigurator().apply(new TalonFXConfiguration());
    motorConfigs.Inverted = InvertedValue.Clockwise_Positive;
    leftShooterMotor.getConfigurator().apply(motorConfigs);
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
   
  @Override
  public void robotPeriodic() {}

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    // tsrxMotorOne.set(TalonSRXControlMode.PercentOutput, controllerOne.getRawAxis(0));
    // tfxMotorOne.set(controllerOne.getRawAxis(1));
    SmartDashboard.putNumber("Shooter and Kicker Speed", kickshootSpeed);
    SmartDashboard.putNumber("Intake Speed", intakeBallsOnVal);

// DRIVER ONE CONTROLS     

    //---SHOOTER BUTTONS---
    // this B button will turn the shooter on/off at a set speed
    // can alter this starting speed above variable at (kickshootSpeed = ...)
    if (controllerOne.getBButtonPressed()) {
      shooterOnVal = shooterOnVal + 1;
      leftShooterMotor.getConfigurator().apply(new TalonFXConfiguration());
      motorConfigs.Inverted = InvertedValue.CounterClockwise_Positive;
      leftShooterMotor.getConfigurator().apply(motorConfigs);
      rightShooterMotor.getConfigurator().apply(new TalonFXConfiguration());
      motorConfigs.Inverted = InvertedValue.Clockwise_Positive;
      rightShooterMotor.getConfigurator().apply(motorConfigs);
      if (shooterOnVal == 1) {
        rightShooterMotor.set(kickshootSpeed);
        leftShooterMotor.set(kickshootSpeed);
      } else {
        shooterOnVal = 0;
        rightShooterMotor.set(0);
        leftShooterMotor.set(0);
      }
    }
    // this Y button will increase the shooter speed by 0.025
    // this A button will decrease the shooter speed by 0.025
    // if the kickshootSpeed is changed, make sure the max increment count is (1-kickshootSpeed)/0.025
    if (controllerOne.getYButtonPressed() && kickshooterIncrCount < 26) {
      kickshooterIncrCount = kickshooterIncrCount + 1;
      kickshootSpeed = (kickshootSpeed + 0.025);
      rightShooterMotor.set(kickshootSpeed);
      leftShooterMotor.set(kickshootSpeed);
      rightKickerMotor.set(kickshootSpeed);
      leftKickerMotor.set(kickshootSpeed);
    }
    if (controllerOne.getAButtonPressed() && kickshooterIncrCount > -16 ) {
      kickshooterIncrCount = kickshooterIncrCount - 1;
      kickshootSpeed = (kickshootSpeed - 0.025);
      rightShooterMotor.set(kickshootSpeed);
      leftShooterMotor.set(kickshootSpeed);
      rightKickerMotor.set(kickshootSpeed);
      leftKickerMotor.set(kickshootSpeed);
    }

    //---KICKER SWITCH DIRECTIONS---
    // this LB button should push the balls away from the shooter and can turn kicker on/off
    if (controllerOne.getLeftBumperButtonPressed()) {
      kickerOnVal = kickerOnVal + 1;
      leftKickerMotor.getConfigurator().apply(new TalonFXConfiguration());
      motorConfigs.Inverted = InvertedValue.Clockwise_Positive;
      leftKickerMotor.getConfigurator().apply(motorConfigs);
      rightKickerMotor.getConfigurator().apply(new TalonFXConfiguration());
      motorConfigs.Inverted = InvertedValue.CounterClockwise_Positive;
      rightKickerMotor.getConfigurator().apply(motorConfigs);
      if (kickerOnVal == 1) {
        leftKickerMotor.set(kickshootSpeed);
        rightKickerMotor.set(kickshootSpeed);
      } else {
        kickerOnVal = 0;
        leftKickerMotor.set(0);
        rightKickerMotor.set(0);
      }
    }
    // this RB button will pull the balls  towards the shooter and can turn kicker on/off
    if (controllerOne.getRightBumperButtonPressed()) {
      kickerOnVal = kickerOnVal + 1;
      leftKickerMotor.getConfigurator().apply(new TalonFXConfiguration());
      motorConfigs.Inverted = InvertedValue.CounterClockwise_Positive;
      leftKickerMotor.getConfigurator().apply(motorConfigs);
      rightKickerMotor.getConfigurator().apply(new TalonFXConfiguration());
      motorConfigs.Inverted = InvertedValue.Clockwise_Positive;
      rightKickerMotor.getConfigurator().apply(motorConfigs);
      if (kickerOnVal == 1) {
        leftKickerMotor.set(kickshootSpeed);
        rightKickerMotor.set(kickshootSpeed);
      } else {
        kickerOnVal = 0;
        leftKickerMotor.set(0);
        rightKickerMotor.set(0);
      }
    }


///DRIVER TWO CONTROLS

    //---INTAKE---
    //this X button is to turn the intakeBalls on/off
    if (controllerTwo.getXButtonPressed()) {
      intakeBallsOnVal = intakeBallsOnVal + 1;
      if (intakeBallsOnVal == 1) {
        intakeBallsMotor.set(intakeBallsSpeed);
      } else {
        intakeBallsOnVal = 0;
        intakeBallsMotor.set(0);
      }
    }

    //this Y button is to make the intakeRotator go up at set angle **check to see if right direction
    if (controllerTwo.getYButtonPressed()) {
      intakeRotatorMotor.getConfigurator().apply(new TalonFXConfiguration());
      motorConfigs.Inverted = InvertedValue.CounterClockwise_Positive;
      intakeRotatorMotor.getConfigurator().apply(motorConfigs);
      if (intakeRotatorOnVal == 1) {
        //add something to make it move up at set angle
        intakeRotatorMotor.set(0.005);
        intakeRotatorMotor.setPosition(0);
        intakeRotatorOnVal = 0;
      }
    }
    //this A button is to make the intakeRotator go down at set angle **check to see if right direction
    if (controllerTwo.getAButtonPressed()) {
      intakeRotatorMotor.getConfigurator().apply(new TalonFXConfiguration());
      motorConfigs.Inverted = InvertedValue.Clockwise_Positive;
      intakeRotatorMotor.getConfigurator().apply(motorConfigs);
      if (intakeRotatorOnVal == 0){
        //add something to make it move down at set angle
        intakeRotatorMotor.set(0.005);
        intakeRotatorMotor.setPosition(90);
        intakeRotatorOnVal = 1;
      } 
    }

    //---CONVEYOR--- 
    //this B button is to turn the conveyor at set speed on and off
    if (controllerTwo.getBButtonPressed()) {
      conveyorOnVal = conveyorOnVal + 1;
      if (conveyorOnVal == 1) {
        conveyorMotor.set(0.15);
      } else {
        conveyorOnVal = 0;
        conveyorMotor.set(0);
      }
    }
    
    //this RB button is to make the belts move towards the shooter **make sure right direction
    if (controllerTwo.getRightBumperButtonPressed()) {
      conveyorMotor.getConfigurator().apply(new TalonFXConfiguration());
      motorConfigs.Inverted = InvertedValue.Clockwise_Positive;
      conveyorMotor.getConfigurator().apply(motorConfigs);
    }
    //this LB button is to make the belts move away from the shooter
    if (controllerTwo.getLeftBumperButtonPressed()) {
      conveyorMotor.getConfigurator().apply(new TalonFXConfiguration());
      motorConfigs.Inverted = InvertedValue.CounterClockwise_Positive;
      conveyorMotor.getConfigurator().apply(motorConfigs);
    }
   
  }

 

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {
    tsrxMotorOne.set(TalonSRXControlMode.PercentOutput, 0);
    tsrxMotorOne.set(TalonSRXControlMode.PercentOutput, tsrxMotorOneSpeed);
    
    leftShooterMotor.set(0);
    rightShooterMotor.set(0);
    leftKickerMotor.set(0);
    rightKickerMotor.set(0);
    intakeBallsMotor.set(0);
    intakeRotatorMotor.set(0);
    conveyorMotor.set(0);

  }

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
