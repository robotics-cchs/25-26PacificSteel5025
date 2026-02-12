// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.MechanismConstants.OperatorConstants;

public class IntakeSubsystem extends SubsystemBase {
  public double intakeDir = 1;
  public double intakeSpeed = 1;
  public boolean currentToggleStatus = false;
  /** Creates a new ShooterSubsystem. */
  public IntakeSubsystem() {
    OperatorConstants.krkLeftIntakeMotor.getConfigurator().apply(OperatorConstants.defaultConfig);
    OperatorConstants.krkRightIntakeMotor.getConfigurator().apply(OperatorConstants.invertedDefaultConfig);
    OperatorConstants.krkLeftIntakeMotor.setVoltage(OperatorConstants.maxVoltage);
    OperatorConstants.krkLeftIntakeMotor.setSafetyEnabled(true);
    OperatorConstants.krkRightIntakeMotor.setVoltage(OperatorConstants.maxVoltage);
    OperatorConstants.krkRightIntakeMotor.setSafetyEnabled(true);
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Right Intake Motor",OperatorConstants.krkRightIntakeMotor.getMotorVoltage().getValueAsDouble());
    SmartDashboard.putNumber("Left Intake Motor",OperatorConstants.krkLeftIntakeMotor.getMotorVoltage().getValueAsDouble());
    SmartDashboard.putBoolean("Current Intake Toggle Status", currentToggleStatus);
  }

  public void toggle(Boolean toggle) {
    OperatorConstants.krkLeftIntakeMotor.set(intakeDir*(toggle?intakeSpeed:0)); // Sets the speed to shootSpeed when toggled
    OperatorConstants.krkRightIntakeMotor.set(intakeDir*(toggle?intakeSpeed:0)); // Sets the speed to shootSpeed when toggled
    currentToggleStatus = toggle;
  }

  public void forward() {
    intakeDir = 1;
  }
  public void reverse() {
    intakeDir = -1;
  }

  public void stop() {
    OperatorConstants.krkLeftIntakeMotor.stopMotor(); // Zeroes the speed
    OperatorConstants.krkRightIntakeMotor.stopMotor(); // Zeroes the speed
  }
}
