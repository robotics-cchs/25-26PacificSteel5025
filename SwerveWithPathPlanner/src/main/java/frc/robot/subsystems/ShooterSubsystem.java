// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.MechanismConstants.OperatorConstants;
import frc.robot.helpers.ShooterSpeedHelper;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;

import java.util.function.Supplier;

public class ShooterSubsystem extends SubsystemBase {
  
  double shooterSpeed = OperatorConstants.MotorSettings.SHOOTER_SPEED_BASE;
  
  double calculationSpeed = 0.0;

  boolean currentToggleStatus = false;

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
  Pose2d shotLocationPose = new Pose2d(4.650,4, new Rotation2d());

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
    SmartDashboard.putNumber("Right Shooter Motor",OperatorConstants.krkRightShooterMotor.getMotorVoltage().getValueAsDouble());
    SmartDashboard.putNumber("Left Shooter Motor",OperatorConstants.krkLeftShooterMotor.getMotorVoltage().getValueAsDouble());
    SmartDashboard.putNumber("Shooter Speed", shooterSpeed);
    SmartDashboard.putBoolean("Current Shooter Toggle Status", currentToggleStatus);

    OperatorConstants.krkLeftShooterMotor.set(shooterSpeed*(currentToggleStatus?1:0)); // Sets the speed to shootSpeed when toggled
    OperatorConstants.krkRightShooterMotor.set(shooterSpeed*(currentToggleStatus?1:0)); // Sets the speed to shootSpeed when toggled

    robotPose = getPose();
    // could be used to adjust the shooter speed automatically
    calculationSpeed = ShooterSpeedHelper.quadraticShooterSpeed(robotPose, shotLocationPose, 0.001, 0.05, 0.2); // adjust coefficients based on testing and requirements
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
    shooterSpeed -= 0.025*((shooterSpeed > 0.1)?1:0); // Decreases by 1/16 if above 0.1
  }

  public void stop() {
    currentToggleStatus = false;
  }
}