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
          .withStatorCurrentLimit(Amps.of(40))
          .withStatorCurrentLimitEnable(true))
      .withCurrentLimits(
        new CurrentLimitsConfigs()
          .withSupplyCurrentLimit(Amps.of(40))
          .withSupplyCurrentLimitEnable(true));

    OperatorConstants.motorConfiguration.Inverted = InvertedValue.Clockwise_Positive;

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
    SmartDashboard.putData("Stator Current", OperatorConstants.tfxLeftIntakeMotor.getStatorCurrent());
    
    
    OperatorConstants.tfxLeftIntakeMotor.getSupplyCurrent();
  }
  public void intakeLifterSpeed(double speed) {

  }

  public void intakeInSpeed(double speed) {

  }

  public void intakeOutSpeed(double speed) {

  }

  public void stop() {

  }
}
