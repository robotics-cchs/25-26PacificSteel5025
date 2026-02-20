// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.MechanismConstants.OperatorConstants;

public class ConveyorSubsystem extends SubsystemBase {
  double dir = OperatorConstants.FORWARD;
  boolean currentToggleStatus = false;
  /** Creates a new ConveyorSubsystem. */
  public ConveyorSubsystem() {
    OperatorConstants.krkConveyorMotor.getConfigurator().apply(OperatorConstants.defaultConfig);
    OperatorConstants.krkConveyorMotor.setSafetyEnabled(true);
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Conveyor Motor",OperatorConstants.krkConveyorMotor.getMotorVoltage().getValueAsDouble());
    SmartDashboard.putBoolean("Current Conveyor Toggle Status", currentToggleStatus);
    OperatorConstants.krkConveyorMotor.set(OperatorConstants.MotorSettings.CONVEYOR_SPEED*dir*(currentToggleStatus?1:0)); // Sets the speed to shootSpeed when toggled
  }

  public void toggle() {
    currentToggleStatus = !currentToggleStatus;
  }

  public void forward() {
    if (dir!=OperatorConstants.FORWARD) {
      dir = OperatorConstants.REVERSE;
    }
  }
  public void reverse() {
    if (dir!=OperatorConstants.REVERSE) {
      dir = OperatorConstants.FORWARD;
    }
  }

  public void stop() {
    currentToggleStatus = false;
  }
}
