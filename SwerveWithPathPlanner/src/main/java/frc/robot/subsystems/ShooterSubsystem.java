// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.MechanismConstants.OperatorConstants;

public class ShooterSubsystem extends SubsystemBase {
  
  double shooterSpeed = OperatorConstants.MotorSettings.SHOOTER_SPEED_BASE;
  
  boolean currentToggleStatus = false;
  
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
    SmartDashboard.putBoolean("Current Shooter Toggle Status", currentToggleStatus);

    OperatorConstants.krkLeftShooterMotor.set(shooterSpeed*(currentToggleStatus?1:0)); // Sets the speed to shootSpeed when toggled
    OperatorConstants.krkRightShooterMotor.set(shooterSpeed*(currentToggleStatus?1:0)); // Sets the speed to shootSpeed when toggled
  }

  public void toggle() {
    currentToggleStatus = !currentToggleStatus;
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