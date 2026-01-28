// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
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

  private final int tsrxMotorOnePort = 32; //Need to adjust as necessary
  private final int tsrxMotorTwoPort = 33; //Need to adjust as necessary

  private final int tfxMotorOnePort = 51; //Need to adjust as necessary
  private final int tfxMotorTwoPort = 22; //Need to adjust as necessary

  private final int controllerOnePort = 0; //Need to adjust as necessary
  private final int joystickOnePort = 1; //Need to adjust as necessary

  private final int MAX_SHOOTERSPEED_COUNT = 5;
  private final int MIN_SHOOTERSPEED_COUNT = 0;


  private final TalonSRX tsrxMotorOne = new TalonSRX(tsrxMotorOnePort);
  private final TalonSRX tsrxMotorTwo = new TalonSRX(tsrxMotorTwoPort);
  private final TalonFX tfxMotorOne = new TalonFX(tfxMotorOnePort);
  private final TalonFX tfxMotorTwo = new TalonFX(tfxMotorTwoPort);

  private final MotorOutputConfigs motorConfiguration = new MotorOutputConfigs();

  private final XboxController controllerOne = new XboxController(controllerOnePort);
  private final Joystick joystickOne = new Joystick(controllerOnePort);

  private int shooterSpeedCounter = 0;
  private int controllerDPadValue = 0;

  private double tsrxMotorOneSpeed = 0;
  private double tsrxMotorTwoSpeed = 0;
  private double tfxMotorOneSpeed = 0;
  private double tfxMotorTwoSpeed = 0;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  public Robot() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    tsrxMotorOne.configFactoryDefault();
    tsrxMotorTwo.configFactoryDefault();
    tfxMotorOne.getConfigurator().apply(new TalonFXConfiguration());
    tfxMotorTwo.getConfigurator().apply(new TalonFXConfiguration());

    tsrxMotorOne.setInverted(false); //need to adjust as necessary
    tsrxMotorTwo.setInverted(false); //need to adjust as necessary

    motorConfiguration.Inverted = InvertedValue.Clockwise_Positive; 
    tfxMotorOne.getConfigurator().apply(motorConfiguration);
    tfxMotorTwo.getConfigurator().apply(motorConfiguration);
    
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
    tsrxMotorOneSpeed = joystickOne.getRawAxis(0);
    tsrxMotorTwoSpeed = joystickOne.getRawAxis(1);
    tfxMotorOneSpeed = controllerOne.getRawAxis(4);
    tfxMotorTwoSpeed = controllerOne.getRawAxis(5);
    
    controllerDPadValue = controllerOne.getPOV();

    SmartDashboard.putNumber("TSRX Motor One", tsrxMotorOneSpeed);
    SmartDashboard.putNumber("TSRX Motor Two", tsrxMotorTwoSpeed);
    SmartDashboard.putNumber("TFX Motor One", tfxMotorOneSpeed);
    SmartDashboard.putNumber("TFX Motor Two", tfxMotorTwoSpeed);
    SmartDashboard.putNumber("D-Pad POV", controllerDPadValue);
    SmartDashboard.putNumber("Shooter Counter", shooterSpeedCounter);

    tsrxMotorOne.set(TalonSRXControlMode.PercentOutput, tsrxMotorOneSpeed);
    tsrxMotorTwo.set(TalonSRXControlMode.PercentOutput, tsrxMotorTwoSpeed);
    tfxMotorTwo.set(tfxMotorTwoSpeed);

    if(controllerOne.getXButtonPressed() && shooterSpeedCounter != MAX_SHOOTERSPEED_COUNT) {
      SmartDashboard.putBoolean("X Button Pressed", controllerOne.getXButtonPressed());
      shooterSpeedCounter++;
      switch(shooterSpeedCounter) {
        case 1:
        tfxMotorOneSpeed = (tfxMotorOneSpeed * 0.25);
        break;
        case 2:
        tfxMotorOneSpeed = (tfxMotorOneSpeed * 0.50);
        break;
        case 3:
        tfxMotorOneSpeed = (tfxMotorOneSpeed * 0.75);
        break;
        case 4:
        tfxMotorOneSpeed = (tfxMotorOneSpeed * 1.00);
        break; 
      }
      tfxMotorOne.set(tfxMotorOneSpeed);
    }

    if(controllerOne.getAButtonPressed() && shooterSpeedCounter != MIN_SHOOTERSPEED_COUNT) {
      SmartDashboard.putBoolean("A Button Pressed", controllerOne.getAButtonPressed());
      shooterSpeedCounter--;
      switch(shooterSpeedCounter) {
        case 1:
        tfxMotorOneSpeed = (tfxMotorOneSpeed * 0.25);
        break;
        case 2:
        tfxMotorOneSpeed = (tfxMotorOneSpeed * 0.50);
        break;
        case 3:
        tfxMotorOneSpeed = (tfxMotorOneSpeed * 0.75);
        break;
        case 4:
        tfxMotorOneSpeed = (tfxMotorOneSpeed * 1.00);
        break; 
      }
      tfxMotorOne.set(tfxMotorOneSpeed);
    }
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {
    tsrxMotorOne.set(TalonSRXControlMode.PercentOutput,0);
    tsrxMotorTwo.set(TalonSRXControlMode.PercentOutput,0);
    tfxMotorOne.set(0);
    tfxMotorTwo.set(0);
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
