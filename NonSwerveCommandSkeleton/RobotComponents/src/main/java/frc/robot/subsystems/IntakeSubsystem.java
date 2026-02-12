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

public class IntakeSubsystem extends SubsystemBase {
  /** Creates a new IntakeSubsystem. */
  public IntakeSubsystem() {
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
    
    OperatorConstants.tfxIntakeMotor.setVoltage(OperatorConstants.MAX_VOLTAGE);
    OperatorConstants.tfxIntakeLifterMotor.setVoltage(OperatorConstants.MAX_VOLTAGE);

    OperatorConstants.tfxIntakeMotor.setSafetyEnabled(OperatorConstants.SET_SAFETY_TRUE);
    OperatorConstants.tfxIntakeLifterMotor.setSafetyEnabled(OperatorConstants.SET_SAFETY_TRUE);

    OperatorConstants.tfxIntakeMotor.getConfigurator().apply(commonConfigs);
    OperatorConstants.tfxIntakeMotor.getConfigurator().apply(motorConfiguration);

    OperatorConstants.tfxIntakeLifterMotor.getConfigurator().apply(commonConfigs);
    OperatorConstants.tfxIntakeLifterMotor.getConfigurator().apply(motorConfiguration);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Left Intake Lifter", OperatorConstants.tfxIntakeLifterMotor.get());
    SmartDashboard.putNumber("Left Intake", OperatorConstants.tfxIntakeMotor.get());

    SmartDashboard.putBoolean("Intake Lifter Safety", OperatorConstants.tfxIntakeLifterMotor.isSafetyEnabled());
    SmartDashboard.putBoolean("Intake Safety", OperatorConstants.tfxIntakeMotor.isSafetyEnabled());
  }

  public void intakeUpSpeed(double speed) {
    OperatorConstants.tfxIntakeLifterMotor.set(-speed);
  }

  public void intakeDownSpeed(double speed) {
    OperatorConstants.tfxIntakeLifterMotor.set(speed);
  }

  public void intakeInSpeed(double speed) {
    OperatorConstants.tfxIntakeMotor.set(speed);
  }

  public void intakeOutSpeed(double speed) {
    OperatorConstants.tfxIntakeMotor.set(-speed);
  }

  public void stop() {
    OperatorConstants.tfxIntakeLifterMotor.set(0);
    OperatorConstants.tfxIntakeMotor.set(0);
  }
}
