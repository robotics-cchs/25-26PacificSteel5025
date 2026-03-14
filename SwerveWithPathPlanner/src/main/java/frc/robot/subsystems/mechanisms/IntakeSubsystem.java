// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.mechanisms;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.MechanismConstants.OperatorConstants;

public class IntakeSubsystem extends SubsystemBase {
  
  double dir = OperatorConstants.FORWARD;
  double intakeLifter = 0;

  boolean currentToggleStatus = false;
  
  /** Creates a new IntakeSubsystem. */
  public IntakeSubsystem() {
    OperatorConstants.krkIntakeMotor.getConfigurator().apply(OperatorConstants.defaultConfig);
    OperatorConstants.krkIntakeMotor.setSafetyEnabled(OperatorConstants.SET_SAFETY_TRUE);
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Intake Motor",OperatorConstants.krkIntakeMotor.getMotorVoltage().getValueAsDouble());
    SmartDashboard.putBoolean("Current Intake Toggle Status", currentToggleStatus);
    OperatorConstants.krkIntakeMotor.set(OperatorConstants.MotorSettings.INTAKE_SPEED*dir*(currentToggleStatus?1:0)); // Sets the speed to intakeSpeed when toggled
    OperatorConstants.krkIntakeLifterMotor.set(intakeLifter);
  }

  public void toggle() {
    currentToggleStatus = !currentToggleStatus;
  }

  public void forward() {
    if (dir!=OperatorConstants.FORWARD) {
      dir = OperatorConstants.FORWARD;
    }
  }

  public void reverse() {
    if (dir!=OperatorConstants.REVERSE) {
      dir = OperatorConstants.REVERSE;
    }
  }

  public void intakeLifterSpeed(double speed) {
    intakeLifter = speed;
  }

  public void stop() {
    currentToggleStatus = false;
  }
}