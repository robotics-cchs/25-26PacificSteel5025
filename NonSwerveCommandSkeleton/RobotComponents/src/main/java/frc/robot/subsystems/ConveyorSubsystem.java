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

public class ConveyorSubsystem extends SubsystemBase {
  /** Creates a new ConveyorSubsystem. */ 
  public ConveyorSubsystem() {
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

    OperatorConstants.tfxLeftConveyorMotor.setVoltage(OperatorConstants.MAX_VOLTAGE);
    OperatorConstants.tfxRightConveyorMotor.setVoltage(OperatorConstants.MAX_VOLTAGE); 

    OperatorConstants.tfxLeftConveyorMotor.setSafetyEnabled(OperatorConstants.SET_SAFETY);
    OperatorConstants.tfxRightConveyorMotor.setSafetyEnabled(OperatorConstants.SET_SAFETY); 

    OperatorConstants.tfxLeftConveyorMotor.getConfigurator().apply(OperatorConstants.commonConfigs); 
    OperatorConstants.tfxLeftConveyorMotor.getConfigurator().apply(OperatorConstants.commonConfigs); 
    OperatorConstants.tfxLeftConveyorMotor.getConfigurator().apply(OperatorConstants.motorConfiguration); 
    OperatorConstants.tfxLeftConveyorMotor.getConfigurator().apply(OperatorConstants.motorConfiguration);

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Conveyor Left", OperatorConstants.tfxLeftConveyorMotor.get()); 
    SmartDashboard.putNumber("Conveyor Right", OperatorConstants.tfxRightConveyorMotor.get()); 
    
    SmartDashboard.putBoolean("Conveyor Left", OperatorConstants.tfxLeftConveyorMotor.isSafetyEnabled()); 
    SmartDashboard.putBoolean("Conveyor Right", OperatorConstants.tfxRightConveyorMotor.isSafetyEnabled()); 
  }

  public void conveyorForwardSpeed(double speed) {
    OperatorConstants.tfxLeftConveyorMotor.set(speed);
    OperatorConstants.tfxRightConveyorMotor.set(speed);
  }

  public void conveyorReverseSpeed(double speed) {
    OperatorConstants.tfxLeftConveyorMotor.set(speed);
    OperatorConstants.tfxRightConveyorMotor.set(speed);
  }

  public void stop() {
    OperatorConstants.tfxLeftConveyorMotor.set(0);
    OperatorConstants.tfxRightConveyorMotor.set(0);
  }
}
