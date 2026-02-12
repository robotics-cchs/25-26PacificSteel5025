// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.MechanismConstants.OperatorConstants;

public class ShooterSubsystem extends SubsystemBase {
  public double shootSpeed = .5;
  public boolean currentToggleStatus = false;
  /** Creates a new ShooterSubsystem. */
  public ShooterSubsystem() {
    OperatorConstants.krkLeftKickerMotor.getConfigurator().apply(OperatorConstants.defaultConfig);
    OperatorConstants.krkRightKickerMotor.getConfigurator().apply(OperatorConstants.invertedDefaultConfig);
    OperatorConstants.krkLeftKickerMotor.setVoltage(OperatorConstants.maxVoltage);
    OperatorConstants.krkLeftKickerMotor.setSafetyEnabled(true);
    OperatorConstants.krkRightKickerMotor.setVoltage(OperatorConstants.maxVoltage);
    OperatorConstants.krkRightKickerMotor.setSafetyEnabled(true);
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Right Conveyor Motor",OperatorConstants.krkRightConveyorMotor.getMotorVoltage().getValueAsDouble());
    SmartDashboard.putNumber("Left Conveyor Motor",OperatorConstants.krkLeftConveyorMotor.getMotorVoltage().getValueAsDouble());
    SmartDashboard.putBoolean("Current Conveyor Toggle Status", currentToggleStatus);
  }

  public void toggle(Boolean toggle) {
    OperatorConstants.krkLeftShooterMotor.set(toggle?shootSpeed:0); // Sets the speed to shootSpeed when toggled
    OperatorConstants.krkRightShooterMotor.set(toggle?shootSpeed:0); // Sets the speed to shootSpeed when toggled
    currentToggleStatus = toggle;
  }

  public void inc() {
    shootSpeed += 0.0625*((shootSpeed < 1.0)?1:0); // Increases by 1/16 if below 1
  }
  public void dec() {
    shootSpeed -= 0.0625*((shootSpeed > 0.1)?1:0); // Decreases by 1/16 if above 0.1
  }

  public void stop() {
    OperatorConstants.krkLeftShooterMotor.stopMotor(); // Zeroes the speed
    OperatorConstants.krkRightShooterMotor.stopMotor(); // Zeroes the speed
  }
}
