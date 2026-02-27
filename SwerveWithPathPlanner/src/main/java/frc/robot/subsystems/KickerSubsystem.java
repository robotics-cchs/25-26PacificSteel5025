// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.MechanismConstants.OperatorConstants;

public class KickerSubsystem extends SubsystemBase {
  
  double dir = OperatorConstants.FORWARD;
  double kickerSpeed = OperatorConstants.MotorSettings.KICKER_SPEED_BASE;

  boolean currentToggleStatus = false;

  /** Creates a new KickerSubsystem. */
  public KickerSubsystem() {
    OperatorConstants.krkRightKickerMotor.getConfigurator().apply(OperatorConstants.invertedDefaultConfig);
    OperatorConstants.krkRightKickerMotor.setSafetyEnabled(OperatorConstants.SET_SAFETY_TRUE);

    OperatorConstants.krkLeftKickerMotor.getConfigurator().apply(OperatorConstants.defaultConfig);
    OperatorConstants.krkLeftKickerMotor.setSafetyEnabled(OperatorConstants.SET_SAFETY_TRUE);
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Right Kicker Motor",OperatorConstants.krkRightKickerMotor.getMotorVoltage().getValueAsDouble());
    SmartDashboard.putNumber("Left Kicker Motor",OperatorConstants.krkLeftKickerMotor.getMotorVoltage().getValueAsDouble());
    SmartDashboard.putBoolean("Current Kicker Toggle Status", currentToggleStatus);

    OperatorConstants.krkLeftKickerMotor.set(dir*kickerSpeed*(currentToggleStatus?1:0)); // Sets the speed to shootSpeed when toggled
    OperatorConstants.krkRightKickerMotor.set(dir*kickerSpeed*(currentToggleStatus?1:0)); // Sets the speed to shootSpeed when toggled
  }

  public void toggle() {
    currentToggleStatus = !currentToggleStatus;
  }

  public void inc() {
    kickerSpeed += 0.025*((kickerSpeed < 1.0)?1:0); // Increases by 1/16 if below 1
  }
  public void dec() {
    kickerSpeed -= 0.025*((kickerSpeed > 0.1)?1:0); // Decreases by 1/16 if above 0.1
  }

  public void forward() {
    dir = OperatorConstants.FORWARD;
  }
  public void reverse() {
    dir = OperatorConstants.REVERSE;
  }

  public void stop() {
    currentToggleStatus = false;
  }
}