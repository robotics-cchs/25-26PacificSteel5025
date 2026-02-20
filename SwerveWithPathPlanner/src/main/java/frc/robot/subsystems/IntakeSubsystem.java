// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.MechanismConstants.OperatorConstants;

public class IntakeSubsystem extends SubsystemBase {
  double dir = OperatorConstants.FORWARD;
  boolean currentToggleStatus = false;
  double targetUp = .25; //change with rotation value of intake being up
  double targetDown = 0; //change with rotation value of intake being down
  double target = targetUp;
  boolean forceIntakeLifterStop = false;
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
    OperatorConstants.krkIntakeMotor.set(OperatorConstants.MotorSettings.INTAKE_SPEED*dir*(currentToggleStatus?1:0)); // Sets the speed to shootSpeed when toggled
    if (forceIntakeLifterStop) {
      OperatorConstants.krkIntakeLifterMotor.set(0);
    } else if (OperatorConstants.krkIntakeLifterMotor.getPosition().getValueAsDouble()-target > .1) {
      OperatorConstants.krkIntakeLifterMotor.set(OperatorConstants.MotorSettings.INTAKE_LIFTER_SPEED);
    } else if (OperatorConstants.krkIntakeLifterMotor.getPosition().getValueAsDouble()-target < -.1) {
      OperatorConstants.krkIntakeLifterMotor.set(-OperatorConstants.MotorSettings.INTAKE_LIFTER_SPEED);
    } else {
      OperatorConstants.krkIntakeLifterMotor.set(0);
    }
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

  public void up() {
    target = targetUp;
  }

  public void down() {
    target = targetDown;
  }

  public void stop() {
    currentToggleStatus = false;
    forceIntakeLifterStop = true;
  }
}
