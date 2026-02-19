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

public class ConveyorSubsystem extends SubsystemBase {
  // Initialize Motor Configuration
  // URL: https://v6.docs.ctr-electronics.com/en/latest/docs/api-reference/api-usage/configuration.html
  // URL: https://api.ctr-electronics.com/phoenix6/stable/java/com/ctre/phoenix6/configs/package-summary.html
  MotorOutputConfigs motorConfigs = new MotorOutputConfigs();
  TalonFXConfiguration commonConfigs = new TalonFXConfiguration();
  VoltageConfigs voltageConfigs = new VoltageConfigs();

  /** Creates a new ConveyorSubsystem. */ 
  public ConveyorSubsystem() {
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
    
    motorConfigs.Inverted = InvertedValue.Clockwise_Positive;
    voltageConfigs.PeakForwardVoltage = OperatorConstants.MAX_VOLTAGE;

    OperatorConstants.tfxConveyorMotor.setSafetyEnabled(OperatorConstants.SET_SAFETY_TRUE);

    OperatorConstants.tfxConveyorMotor.getConfigurator().apply(commonConfigs);  
    OperatorConstants.tfxConveyorMotor.getConfigurator().apply(motorConfigs); 
    OperatorConstants.tfxConveyorMotor.getConfigurator().apply(voltageConfigs);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Conveyor", OperatorConstants.tfxConveyorMotor.get()); 
    SmartDashboard.putNumber("Conveyor Voltage", voltageConfigs.PeakForwardVoltage);
    SmartDashboard.putBoolean("Conveyor", OperatorConstants.tfxConveyorMotor.isSafetyEnabled()); 
    SmartDashboard.putBoolean("Conveyor Activated", OperatorConstants.tfxConveyorMotor.isAlive());
  }

  public void conveyorSpeed(double speed) {
    OperatorConstants.tfxConveyorMotor.set(speed);
  }

  public void conveyorForwardSpeed(double speed) {
    OperatorConstants.tfxConveyorMotor.set(speed);
  }

  public void conveyorReverseSpeed(double speed) {
    OperatorConstants.tfxConveyorMotor.set(speed);
  }

  public void stop() {
    OperatorConstants.tfxConveyorMotor.set(0);
  }
}
