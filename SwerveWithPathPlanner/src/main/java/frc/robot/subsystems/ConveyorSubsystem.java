// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.MechanismConstants.OperatorConstants;

public class ConveyorSubsystem extends SubsystemBase {
  public double conveyorDir = 1;
  public double conveyorSpeed = 1;
  public boolean currentToggleStatus = false;
  /** Creates a new ShooterSubsystem. */
  public ConveyorSubsystem() {
    OperatorConstants.krkLeftConveyorMotor.getConfigurator().apply(OperatorConstants.defaultConfig);
    OperatorConstants.krkRightConveyorMotor.getConfigurator().apply(OperatorConstants.invertedDefaultConfig);
    OperatorConstants.krkLeftConveyorMotor.setVoltage(OperatorConstants.maxVoltage);
    OperatorConstants.krkLeftConveyorMotor.setSafetyEnabled(true);
    OperatorConstants.krkRightConveyorMotor.setVoltage(OperatorConstants.maxVoltage);
    OperatorConstants.krkRightConveyorMotor.setSafetyEnabled(true);
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Right Conveyor Motor",OperatorConstants.krkRightConveyorMotor.getMotorVoltage().getValueAsDouble());
    SmartDashboard.putNumber("Left Conveyor Motor",OperatorConstants.krkLeftConveyorMotor.getMotorVoltage().getValueAsDouble());
    SmartDashboard.putBoolean("Current Conveyor Toggle Status", currentToggleStatus);
  }

  public void toggle(Boolean toggle) {
    OperatorConstants.krkLeftConveyorMotor.set(conveyorDir*(toggle?conveyorSpeed:0)); // Sets the speed to shootSpeed when toggled
    OperatorConstants.krkRightConveyorMotor.set(conveyorDir*(toggle?conveyorSpeed:0)); // Sets the speed to shootSpeed when toggled
    currentToggleStatus = toggle;
  }

  public void forward() {
    conveyorDir = 1;
  }
  public void reverse() {
    conveyorDir = -1;
  }

  public void stop() {
    OperatorConstants.krkLeftConveyorMotor.stopMotor(); // Zeroes the speed
    OperatorConstants.krkRightConveyorMotor.stopMotor(); // Zeroes the speed
  }
}
