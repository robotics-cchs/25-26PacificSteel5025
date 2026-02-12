// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static edu.wpi.first.units.Units.Amps;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.OperatorConstants;

public class KickerSubsystem extends SubsystemBase {
  /** Creates a new KickerSubsystem. */
  public KickerSubsystem() {
    // Initialize Motor Configuration
    // URL: https://v6.docs.ctr-electronics.com/en/latest/docs/api-reference/api-usage/configuration.html
    // URL: https://api.ctr-electronics.com/phoenix6/stable/java/com/ctre/phoenix6/configs/package-summary.html
    MotorOutputConfigs motorConfiguration = new MotorOutputConfigs();
    TalonFXConfiguration commonConfigs = new TalonFXConfiguration();

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

    motorConfiguration.Inverted = InvertedValue.Clockwise_Positive;

    OperatorConstants.tfxLeftKickerMotor.setSafetyEnabled(OperatorConstants.SET_SAFETY_TRUE);
    OperatorConstants.tfxRightKickerMotor.setSafetyEnabled(OperatorConstants.SET_SAFETY_TRUE);

    OperatorConstants.tfxLeftKickerMotor.setVoltage(OperatorConstants.MAX_VOLTAGE);
    OperatorConstants.tfxRightKickerMotor.setVoltage(OperatorConstants.MAX_VOLTAGE);

    OperatorConstants.tfxLeftKickerMotor.getConfigurator().apply(commonConfigs);
    OperatorConstants.tfxLeftKickerMotor.getConfigurator().apply(motorConfiguration);

    OperatorConstants.tfxRightKickerMotor.getConfigurator().apply(commonConfigs);
    OperatorConstants.tfxRightKickerMotor.getConfigurator().apply(motorConfiguration);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Left Kicker", OperatorConstants.tfxLeftKickerMotor.get());
    SmartDashboard.putNumber("Right Kicker", OperatorConstants.tfxRightKickerMotor.get());

    SmartDashboard.putBoolean("Left Kicker Safety", OperatorConstants.tfxLeftKickerMotor.isSafetyEnabled());
    SmartDashboard.putBoolean("Right Kicker Safety", OperatorConstants.tfxRightKickerMotor.isSafetyEnabled());
  }

  public void kickerSpeed(double speed) {
    OperatorConstants.tfxLeftKickerMotor.set(speed);
    OperatorConstants.tfxRightKickerMotor.set(speed);
  }

  public void stop() {
    OperatorConstants.tfxLeftKickerMotor.set(0);
    OperatorConstants.tfxRightKickerMotor.set(0);
  }
}
