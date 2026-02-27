// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
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

  private final int leftShooterPort = 41;
  private final int rightShooterPort = 42;
  private final int conveyorPort = 33;
  private final int kickerPort = 52;
  private final int intakePort = 51;
  private final int controllerOnePort = 0;

  private double toggleShooter = 0;
  private double toggleIntake = 0;
  private double toggleConveyor = 0;

  double kP = 1;
  double kI = 0;
  double kD = 0;

  double targetVelocity;
  double currentVelocity;

  double output;


  private final TalonFX leftShooter = new TalonFX(leftShooterPort);
  private final TalonFX rightShooter = new TalonFX(rightShooterPort);
  private final TalonSRX conveyor = new TalonSRX(conveyorPort);
  private final TalonFX kicker = new TalonFX(kickerPort);
  private final TalonFX intake = new TalonFX(intakePort);
  private final XboxController controllerOne = new XboxController(controllerOnePort);

  PIDController pid = new PIDController(kP, kI, kD);


  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  public Robot() {
    pid.setTolerance(5.0);
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    SmartDashboard.putNumber("Target Velocity", 0.5);

    conveyor.configFactoryDefault();
    conveyor.setInverted(false);

    leftShooter.getConfigurator().apply(new com.ctre.phoenix6.configs.TalonFXConfiguration());
    rightShooter.getConfigurator().apply(new com.ctre.phoenix6.configs.TalonFXConfiguration());
    kicker.getConfigurator().apply(new com.ctre.phoenix6.configs.TalonFXConfiguration());
    intake.getConfigurator().apply(new com.ctre.phoenix6.configs.TalonFXConfiguration());
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    if (controllerOne.getRightBumperButton()) {
      controllerOne.setRumble(RumbleType.kLeftRumble,0.5);
    } else {
      controllerOne.setRumble(RumbleType.kLeftRumble,0);
    }
    SmartDashboard.putBoolean("Intake toggled", toggleIntake!=0);
    SmartDashboard.putBoolean("Shooter toggled", (toggleShooter)!=0);
    SmartDashboard.putBoolean("Conveyor toggled", toggleConveyor!=0);
    SmartDashboard.putNumber("Shooter Velocity", rightShooter.getVelocity().getValueAsDouble());
    SmartDashboard.putBoolean("Kicker toggled", ((controllerOne.getAButton()?0:1)*toggleShooter*toggleConveyor)!=0);
  }

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
    m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
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
  public void teleopInit() {
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    if (controllerOne.getBButtonPressed()) {
      if (toggleConveyor==0) {
        toggleConveyor = 1;
      } else {
        toggleConveyor = 0;
      }
    }
    if (controllerOne.getXButtonPressed()) {
      toggleShooter = toggleShooter++;
      if (toggleShooter <= 24 && toggleShooter != 0) {
        rightShooter.set(0.4);
        leftShooter.set(0.4);
      } else {
        rightShooter.set(0.4+(.025*(toggleShooter-1)));
        leftShooter.set(0.4+(.025*(toggleShooter-1)));
      }
      
    }
    if (controllerOne.getYButtonPressed()) {
      if (toggleIntake==0) {
        toggleIntake = 1;
      } else {
        toggleIntake = 0;
      }
    }
    currentVelocity = rightShooter.getVelocity().getValueAsDouble();
    targetVelocity = SmartDashboard.getNumber("Target Speed", 0.5);
    output = pid.calculate(currentVelocity, targetVelocity);
    SmartDashboard.putNumber("PID Output", output);
    // (I changed something that might not need these next two lines -- line 148 and following)
    //leftShooter.set(-toggleShooter*Math.max(-1.0, Math.min(1.0, output)));
    //rightShooter.set(toggleShooter*Math.max(-1.0, Math.min(1.0, output)));
    intake.set(toggleIntake);
    conveyor.set(TalonSRXControlMode.PercentOutput, toggleConveyor);
    kicker.set((controllerOne.getRightBumperButton()?-1:0)*toggleShooter*toggleConveyor);
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {
    leftShooter.set(0);
    rightShooter.set(0);
    intake.set(0);
    conveyor.set(TalonSRXControlMode.PercentOutput,0);
    kicker.set(0);
    toggleConveyor = 0;
    toggleShooter = 0;
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