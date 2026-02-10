// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.MechanismConstants.OperatorConstants;

public class KickerSubsystem extends SubsystemBase {
  double shootSpeed = .5;
  /** Creates a new ShooterSubsystem. */
  public KickerSubsystem() {
    OperatorConstants.sLeftKickerMotor.configFactoryDefault();
    OperatorConstants.sLeftKickerMotor.setInverted(true);
    OperatorConstants.sRightKickerMotor.configFactoryDefault();
    OperatorConstants.sRightKickerMotor.setInverted(false);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void toggle(Boolean toggle) {
    OperatorConstants.sLeftKickerMotor.set(TalonSRXControlMode.PercentOutput, toggle?shootSpeed:0); // Sets the speed to shootSpeed when toggled
    OperatorConstants.sRightKickerMotor.set(TalonSRXControlMode.PercentOutput, toggle?shootSpeed:0); // Sets the speed to shootSpeed when toggled
  }

  public void inc() {
    shootSpeed += 0.0625*((shootSpeed < 1.0)?1:0); // Increases by 1/16 if below 1
  }
  public void dec() {
    shootSpeed -= 0.0625*((shootSpeed > 0.1)?1:0); // Decreases by 1/16 if above 0.1
  }

  public void stop() {
    OperatorConstants.sLeftKickerMotor.set(TalonSRXControlMode.PercentOutput, 0); // Zeroes the speed
    OperatorConstants.sRightKickerMotor.set(TalonSRXControlMode.PercentOutput, 0); // Zeroes the speed
  }
}
