// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.mechanisms;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.MechanismConstants.OperatorConstants;
import frc.robot.constants.MechanismConstants.OperatorConstants.MotorSettings;
import frc.robot.helpers.ShooterSpeed;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;

import java.util.function.Supplier;

public class ShooterSubsystem extends SubsystemBase {
  
  double shooterSpeed = OperatorConstants.MotorSettings.SHOOTER_SPEED_BASE;
  PIDController pid = new PIDController(OperatorConstants.MotorSettings.SHOOTER_P, OperatorConstants.MotorSettings.SHOOTER_I, OperatorConstants.MotorSettings.SHOOTER_D);
  SimpleMotorFeedforward ff = new SimpleMotorFeedforward(OperatorConstants.MotorSettings.SHOOTER_S, OperatorConstants.MotorSettings.SHOOTER_V);
  double calculationSpeed = 0.0;

  boolean currentToggleStatus = false;
  boolean autoShotSpeed = true;

  // to get the robot pose from swerve, asked AI for clarification, and then read and understood what it does
  private Supplier<Pose2d> poseSupplier = () -> new Pose2d();

  // Called from RobotContainer to provide drivetrain pose
  public void setPoseSupplier(Supplier<Pose2d> supplier) {
      this.poseSupplier = supplier == null ? () -> new Pose2d() : supplier;
  }

  // Current robot pose from drivetrain
  public Pose2d getPose() {
      return poseSupplier.get();
  }

  Pose2d robotPose = new Pose2d();

  // Calculate the distance using the utility method
  double distanceToTarget = 0.0;
  /** Creates a new ShooterSubsystem. */
  public ShooterSubsystem() {
    OperatorConstants.krkRightShooterMotor.getConfigurator().apply(OperatorConstants.defaultConfig);
    OperatorConstants.krkRightShooterMotor.setSafetyEnabled(OperatorConstants.SET_SAFETY_TRUE);

    OperatorConstants.krkLeftShooterMotor.getConfigurator().apply(OperatorConstants.invertedDefaultConfig);
    OperatorConstants.krkLeftShooterMotor.setSafetyEnabled(OperatorConstants.SET_SAFETY_TRUE);
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Right Shooter Motor Velocity",OperatorConstants.krkRightShooterMotor.getVelocity().getValueAsDouble());
    SmartDashboard.putNumber("Right Shooter Motor",OperatorConstants.krkRightShooterMotor.getMotorVoltage().getValueAsDouble());
    SmartDashboard.putNumber("Shooter Speed", shooterSpeed);
    SmartDashboard.putBoolean("Current Shooter Toggle Status", currentToggleStatus);
    SmartDashboard.putNumber("Shooter PID Error", pid.getError());

    robotPose = getPose();
    
    double targetRPM = ShooterSpeed.targetSpeed(robotPose) * 2 * Math.PI;

    double currentRPM = OperatorConstants.krkRightShooterMotor.getVelocity().getValueAsDouble() * 2 * Math.PI;

    double ffVolts = ff.calculate(targetRPM);

    double pidOutput = pid.calculate(currentRPM,targetRPM);

    double outputVolts = ffVolts + pidOutput;

    if (outputVolts > 12.0) outputVolts = 12.0;
    if (outputVolts < -12.0) outputVolts = -12.0;
    if (autoShotSpeed) {
      OperatorConstants.krkLeftShooterMotor.setVoltage(outputVolts*(currentToggleStatus?1:0)); // Sets the speed to shootSpeed when toggled
      OperatorConstants.krkRightShooterMotor.setVoltage(outputVolts*(currentToggleStatus?1:0)); // Sets the speed to shootSpeed when toggled
    }
    else {
      if (!OperatorConstants.controllerOne.b().getAsBoolean()) {
        OperatorConstants.krkLeftShooterMotor.set(shooterSpeed*(currentToggleStatus?1:0)); // Sets the speed to shootSpeed when toggled
        OperatorConstants.krkRightShooterMotor.set(shooterSpeed*(currentToggleStatus?1:0)); // Sets the speed to shootSpeed when toggled
      } else {
        OperatorConstants.krkLeftShooterMotor.setVoltage(outputVolts*(currentToggleStatus?1:0)); // Sets the speed to shootSpeed when toggled
        OperatorConstants.krkRightShooterMotor.setVoltage(outputVolts*(currentToggleStatus?1:0)); // Sets the speed to shootSpeed when toggled
      }
    }
  }

  public void toggle() {
    currentToggleStatus = !currentToggleStatus;
  }

  public void on() {
    currentToggleStatus = true;
  }

  public void off() {
    currentToggleStatus = false;
  }

  public void inc() {
    shooterSpeed += 0.025*((shooterSpeed < 1.0)?1:0); // Increases by 1/16 if below 1
  }
  public void dec() {
    shooterSpeed -= 0.025*((shooterSpeed > 0.0)?1:0); // Decreases by 1/16 if above 0.1
  }
  public void teleopEnable() {
    shooterSpeed = OperatorConstants.MotorSettings.TELEOP_SPEED_BASE;
    autoShotSpeed = false;
  }

  public void stop() {
    currentToggleStatus = false;
  }
  
}