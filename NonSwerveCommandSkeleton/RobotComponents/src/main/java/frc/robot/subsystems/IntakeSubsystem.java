// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static edu.wpi.first.units.Units.Amps;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.OperatorConstants;

public class IntakeSubsystem extends SubsystemBase {
  /** Creates a new IntakeSubsystem. */
  public IntakeSubsystem() {
    OperatorConstants.commonConfigs
      .withMotorOutput(
        new MotorOutputConfigs()
          .withNeutralMode(NeutralModeValue.Brake))
      .withCurrentLimits(
        new CurrentLimitsConfigs()
          .withStatorCurrentLimit(Amps.of(OperatorConstants.MAX_AMPS))
          .withStatorCurrentLimitEnable(true))
      .withCurrentLimits(
        new CurrentLimitsConfigs()
          .withSupplyCurrentLimit(Amps.of(OperatorConstants.MAX_AMPS))
          .withSupplyCurrentLimitEnable(true));

    OperatorConstants.motorConfiguration.Inverted = InvertedValue.Clockwise_Positive;
    
    OperatorConstants.tfxLeftIntakeMotor.setVoltage(OperatorConstants.MAX_VOLTAGE);
    OperatorConstants.tfxLeftIntakeLifterMotor.setVoltage(OperatorConstants.MAX_VOLTAGE);
    OperatorConstants.tfxRightIntakeMotor.setVoltage(OperatorConstants.MAX_VOLTAGE);
    OperatorConstants.tfxRightIntakeLifterMotor.setVoltage(OperatorConstants.MAX_VOLTAGE);

    OperatorConstants.tfxLeftIntakeMotor.setSafetyEnabled(OperatorConstants.SET_SAFETY);
    OperatorConstants.tfxLeftIntakeLifterMotor.setSafetyEnabled(OperatorConstants.SET_SAFETY);
    OperatorConstants.tfxRightIntakeMotor.setSafetyEnabled(OperatorConstants.SET_SAFETY);
    OperatorConstants.tfxRightIntakeLifterMotor.setSafetyEnabled(OperatorConstants.SET_SAFETY);

    OperatorConstants.tfxLeftIntakeMotor.getConfigurator().apply(OperatorConstants.commonConfigs);
    OperatorConstants.tfxLeftIntakeMotor.getConfigurator().apply(OperatorConstants.motorConfiguration);

    OperatorConstants.tfxRightIntakeMotor.getConfigurator().apply(OperatorConstants.commonConfigs);
    OperatorConstants.tfxLeftIntakeMotor.getConfigurator().apply(OperatorConstants.motorConfiguration);

    OperatorConstants.tfxLeftIntakeLifterMotor.getConfigurator().apply(OperatorConstants.commonConfigs);
    OperatorConstants.tfxRightIntakeLifterMotor.getConfigurator().apply(OperatorConstants.motorConfiguration);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Left Intake Lifter", OperatorConstants.tfxLeftIntakeLifterMotor.get());
    SmartDashboard.putNumber("Right Intake Lifter", OperatorConstants.tfxRightIntakeLifterMotor.get());
    SmartDashboard.putNumber("Left Intake", OperatorConstants.tfxLeftIntakeMotor.get());
    SmartDashboard.putNumber("Right Intake", OperatorConstants.tfxRightIntakeMotor.get());

    SmartDashboard.putBoolean("Left Intake Lifter Safety", OperatorConstants.tfxLeftIntakeLifterMotor.isSafetyEnabled());
    SmartDashboard.putBoolean("Right Intake Lifter Safety", OperatorConstants.tfxRightIntakeLifterMotor.isSafetyEnabled());
    SmartDashboard.putBoolean("Left Intake", OperatorConstants.tfxLeftIntakeMotor.isSafetyEnabled());
    SmartDashboard.putBoolean("Right Intake", OperatorConstants.tfxRightIntakeMotor.isSafetyEnabled());
  }

  public void intakeUpSpeed(double speed) {
    OperatorConstants.tfxLeftIntakeLifterMotor.set(speed);
    OperatorConstants.tfxRightIntakeLifterMotor.set(speed);
  }

  public void intakeDownSpeed(double speed) {
    OperatorConstants.tfxLeftIntakeLifterMotor.set(speed);
    OperatorConstants.tfxRightIntakeLifterMotor.set(speed);
  }

  public void intakeInSpeed(double speed) {
    OperatorConstants.tfxLeftIntakeMotor.set(speed);
    OperatorConstants.tfxRightIntakeMotor.set(speed);
  }

  public void intakeOutSpeed(double speed) {
    OperatorConstants.tfxLeftIntakeMotor.set(speed);
    OperatorConstants.tfxRightIntakeMotor.set(speed);
  }

  public void stop() {
    OperatorConstants.tfxLeftIntakeLifterMotor.set(0);
    OperatorConstants.tfxRightIntakeLifterMotor.set(0);
    OperatorConstants.tfxLeftIntakeMotor.set(0);
    OperatorConstants.tfxRightIntakeMotor.set(0);
  }
}
