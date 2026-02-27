// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.MechanismConstants.OperatorConstants;

public class IntakeSubsystem extends SubsystemBase {
  double dir = OperatorConstants.FORWARD;
  double lifterDir = OperatorConstants.FORWARD;
  boolean currentToggleStatus = false;
  boolean currentLifterToggleStatus = false;

  /** Creates a new IntakeSubsystem. */
  public IntakeSubsystem() {
    OperatorConstants.krkIntakeMotor.getConfigurator().apply(OperatorConstants.defaultConfig);
    OperatorConstants.krkIntakeMotor.setSafetyEnabled(true);
    OperatorConstants.krkIntakeLifterMotor.getConfigurator().apply(OperatorConstants.defaultConfig);
    OperatorConstants.krkIntakeLifterMotor.setSafetyEnabled(true);
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Intake Lifter Motor",OperatorConstants.krkIntakeLifterMotor.getMotorVoltage().getValueAsDouble());
    SmartDashboard.putNumber("Intake Motor",OperatorConstants.krkIntakeMotor.getMotorVoltage().getValueAsDouble());
    SmartDashboard.putBoolean("Current Intake Toggle Status", currentToggleStatus);
    SmartDashboard.putBoolean("Current Intake Lifter Toggle Status", currentLifterToggleStatus);
    OperatorConstants.krkIntakeMotor.set(OperatorConstants.MotorSettings.INTAKE_SPEED*dir*(currentToggleStatus?1:0)); // Sets the speed to intakeSpeed when toggled
    OperatorConstants.krkIntakeMotor.set(OperatorConstants.MotorSettings.INTAKE_LIFTER_SPEED*dir*(currentToggleStatus?1:0)); // Sets the speed to intakeLifterSpeed when toggled
  }

  public void toggle() {
    currentToggleStatus = !currentToggleStatus;
  }

  public void toggleLifter() {
    currentLifterToggleStatus = !currentLifterToggleStatus;
  }


  public void forward() {
    if (dir!=OperatorConstants.REVERSE) {
      dir = OperatorConstants.FORWARD;
    }
  }

  public void reverse() {
    if (dir!=OperatorConstants.FORWARD) {
      dir = OperatorConstants.REVERSE;
    }
  }

  public void down() {
    if (lifterDir!=OperatorConstants.FORWARD) {
      lifterDir = OperatorConstants.REVERSE;
    }
  }

  public void up() {
    if (lifterDir!=OperatorConstants.REVERSE) {
      lifterDir = OperatorConstants.FORWARD;
    }
  }

  public void stop() {
    currentToggleStatus = false;
    currentLifterToggleStatus = false;
  }
}