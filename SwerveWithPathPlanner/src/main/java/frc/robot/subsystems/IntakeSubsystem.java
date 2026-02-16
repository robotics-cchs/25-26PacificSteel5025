// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.MechanismConstants.OperatorConstants;

// TODO
// - Work on intake down and up code

public class IntakeSubsystem extends SubsystemBase {
  double dir = 1;
  boolean currentToggleStatus = false;
  /** Creates a new IntakeSubsystem. */
  public IntakeSubsystem() {
    OperatorConstants.krkLeftIntakeMotor.getConfigurator().apply(OperatorConstants.defaultConfig);
    OperatorConstants.krkRightIntakeMotor.getConfigurator().apply(OperatorConstants.invertedDefaultConfig);
    OperatorConstants.krkLeftIntakeMotor.setSafetyEnabled(true);
    OperatorConstants.krkRightIntakeMotor.setSafetyEnabled(true);
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Right Intake Motor",OperatorConstants.krkRightIntakeMotor.getMotorVoltage().getValueAsDouble());
    SmartDashboard.putNumber("Left Intake Motor",OperatorConstants.krkLeftIntakeMotor.getMotorVoltage().getValueAsDouble());
    SmartDashboard.putBoolean("Current Shooter Toggle Status", currentToggleStatus);
    OperatorConstants.krkLeftIntakeMotor.set(dir*(currentToggleStatus?1:0)); // Sets the speed to shootSpeed when toggled
    OperatorConstants.krkRightIntakeMotor.set(dir*(currentToggleStatus?1:0)); // Sets the speed to shootSpeed when toggled
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
