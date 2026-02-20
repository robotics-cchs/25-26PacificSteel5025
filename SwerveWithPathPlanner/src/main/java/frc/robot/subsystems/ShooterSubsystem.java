// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.MechanismConstants.OperatorConstants;

public class ShooterSubsystem extends SubsystemBase {
  double shootSpeed = OperatorConstants.MotorSettings.SHOOTER_SPEED_BASE;
  boolean currentToggleStatus = false;
  /** Creates a new ShooterSubsystem. */
  public ShooterSubsystem() {
    OperatorConstants.krkLeftKickerMotor.getConfigurator().apply(OperatorConstants.defaultConfig);
    OperatorConstants.krkRightKickerMotor.getConfigurator().apply(OperatorConstants.invertedDefaultConfig);
    OperatorConstants.krkLeftKickerMotor.setSafetyEnabled(true);
    OperatorConstants.krkRightKickerMotor.setSafetyEnabled(true);
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Right Shooter Motor",OperatorConstants.krkRightShooterMotor.getMotorVoltage().getValueAsDouble());
    SmartDashboard.putNumber("Left Shooter Motor",OperatorConstants.krkLeftShooterMotor.getMotorVoltage().getValueAsDouble());
    SmartDashboard.putBoolean("Current Shooter Toggle Status", currentToggleStatus);
    OperatorConstants.krkLeftShooterMotor.set(shootSpeed*(currentToggleStatus?1:0)); // Sets the speed to shootSpeed when toggled
    OperatorConstants.krkRightShooterMotor.set(shootSpeed*(currentToggleStatus?1:0)); // Sets the speed to shootSpeed when toggled
  }

  public void toggle() {
    currentToggleStatus = !currentToggleStatus;
  }

  public void inc() {
    shootSpeed += 0.025*((shootSpeed < 1.0)?1:0); // Increases by 1/16 if below 1
  }
  public void dec() {
    shootSpeed -= 0.025*((shootSpeed > 0.1)?1:0); // Decreases by 1/16 if above 0.1
  }

  public void stop() {
    currentToggleStatus = false;
  }
}
