// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.MechanismConstants.OperatorConstants;

public class ConveyorSubsystem extends SubsystemBase {
  double dir = 1;
  boolean currentToggleStatus = false;
  /** Creates a new ConveyorSubsystem. */
  public ConveyorSubsystem() {
    OperatorConstants.krkLeftConveyorMotor.getConfigurator().apply(OperatorConstants.defaultConfig);
    OperatorConstants.krkRightConveyorMotor.getConfigurator().apply(OperatorConstants.invertedDefaultConfig);
    OperatorConstants.krkLeftConveyorMotor.setSafetyEnabled(true);
    OperatorConstants.krkRightConveyorMotor.setSafetyEnabled(true);
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Right Intake Motor",OperatorConstants.krkRightConveyorMotor.getMotorVoltage().getValueAsDouble());
    SmartDashboard.putNumber("Left Intake Motor",OperatorConstants.krkLeftConveyorMotor.getMotorVoltage().getValueAsDouble());
    SmartDashboard.putBoolean("Current Shooter Toggle Status", currentToggleStatus);
    OperatorConstants.krkLeftConveyorMotor.set(dir*(currentToggleStatus?1:0)); // Sets the speed to shootSpeed when toggled
    OperatorConstants.krkRightConveyorMotor.set(dir*(currentToggleStatus?1:0)); // Sets the speed to shootSpeed when toggled
  }

  public void toggle() {
    currentToggleStatus = !currentToggleStatus;
  }

  public void forward() {
    if (dir!=1) {
      dir = -1;
    }
  }
  public void reverse() {
    if (dir!=-1) {
      dir = 1;
    }
  }

  public void stop() {
    currentToggleStatus = false;
  }
}
