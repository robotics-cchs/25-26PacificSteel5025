// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static edu.wpi.first.units.Units.Amps;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.configs.VoltageConfigs;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.OperatorConstants;

public class ShooterSubsystem extends SubsystemBase {
  // Initialize Motor Configuration
  // URL: https://v6.docs.ctr-electronics.com/en/latest/docs/api-reference/api-usage/configuration.html
  // URL: https://api.ctr-electronics.com/phoenix6/stable/java/com/ctre/phoenix6/configs/package-summary.html
  MotorOutputConfigs leftMotorConfigs = new MotorOutputConfigs();
  MotorOutputConfigs rightMotorConfigs = new MotorOutputConfigs();
  TalonFXConfiguration commonConfigs = new TalonFXConfiguration();
  VoltageConfigs voltageConfigs = new VoltageConfigs();

  /** Creates a new ShooterSubsystem. */
  public ShooterSubsystem() {
    commonConfigs
      .withMotorOutput(
        new MotorOutputConfigs()
          .withNeutralMode(NeutralModeValue.Coast))
      .withCurrentLimits(
        new CurrentLimitsConfigs()
          .withStatorCurrentLimit(Amps.of(OperatorConstants.MAX_AMPS))
          .withStatorCurrentLimitEnable(true))
      .withCurrentLimits(
        new CurrentLimitsConfigs()
          .withSupplyCurrentLimit(Amps.of(OperatorConstants.MAX_AMPS))
          .withSupplyCurrentLimitEnable(true));

    rightMotorConfigs.Inverted = InvertedValue.Clockwise_Positive;
    leftMotorConfigs.Inverted = InvertedValue.CounterClockwise_Positive;

    voltageConfigs.PeakForwardVoltage = OperatorConstants.MAX_VOLTAGE;

    OperatorConstants.tfxLeftShooterMotor.setSafetyEnabled(OperatorConstants.SET_SAFETY_TRUE);
    OperatorConstants.tfxRightShooterMotor.setSafetyEnabled(OperatorConstants.SET_SAFETY_TRUE);

    OperatorConstants.tfxLeftShooterMotor.getConfigurator().apply(commonConfigs);
    OperatorConstants.tfxLeftShooterMotor.getConfigurator().apply(leftMotorConfigs);
    OperatorConstants.tfxLeftShooterMotor.getConfigurator().apply(voltageConfigs);

    OperatorConstants.tfxRightShooterMotor.getConfigurator().apply(commonConfigs);
    OperatorConstants.tfxRightShooterMotor.getConfigurator().apply(rightMotorConfigs);
    OperatorConstants.tfxRightShooterMotor.getConfigurator().apply(voltageConfigs);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Left Shooter", OperatorConstants.tfxLeftShooterMotor.get());
    SmartDashboard.putNumber("Right Shooter", OperatorConstants.tfxRightShooterMotor.get());

    SmartDashboard.putBoolean("Left Shooter Safety", OperatorConstants.tfxLeftShooterMotor.isSafetyEnabled());
    SmartDashboard.putBoolean("Right Shooter Safety", OperatorConstants.tfxRightShooterMotor.isSafetyEnabled());

    SmartDashboard.putBoolean("Left Shooter Motor Activated", OperatorConstants.tfxLeftShooterMotor.isAlive());
    SmartDashboard.putBoolean("Right Shooter Motor Activated", OperatorConstants.tfxRightShooterMotor.isAlive());

    SmartDashboard.putNumber("Left Shooter Voltage", voltageConfigs.PeakForwardVoltage);
    SmartDashboard.putNumber("Right Shooter Voltage", voltageConfigs.PeakForwardVoltage);
  }

  public void shooterSpeed(double speed) {
    OperatorConstants.tfxLeftShooterMotor.set(speed);
    OperatorConstants.tfxRightShooterMotor.set(speed);
  }

  public void stop() {
    OperatorConstants.tfxLeftShooterMotor.set(0);
    OperatorConstants.tfxRightShooterMotor.set(0);
  }
}
