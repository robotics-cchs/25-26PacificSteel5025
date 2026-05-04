// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.mechanisms;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.RobotController;
import frc.robot.constants.MechanismConstants.HardwareConstants;
import frc.robot.constants.MechanismConstants.Settings;
import frc.robot.constants.MechanismConstants.Configs;
import frc.robot.telemetry.Logger;

public class ExampleMechanismSubsystem extends SubsystemBase {

  public enum State {
    IDLE,
    FORWARD,
    REVERSE
  }

  private State currentState = State.IDLE;
  private final double motorSpeed = Settings.EX_MOTOR_SPEED;

  /** Creates a new IntakeSubsystem. */
  public ExampleMechanismSubsystem() {
    HardwareConstants.krkExampleMotor.getConfigurator().apply(Configs.defaultPowerConfig);
    HardwareConstants.krkExampleMotor.setSafetyEnabled(Settings.SET_SAFETY_TRUE);
  }

  @Override
  public void periodic() {
    Logger.recordOutput("ExampleMechanism/MotorVoltage", HardwareConstants.krkExampleMotor.getMotorVoltage().getValueAsDouble());
    Logger.recordOutput("ExampleMechanism/State", currentState.toString());

    switch (currentState) {
      case FORWARD:
        HardwareConstants.krkExampleMotor.set(motorSpeed * Settings.FORWARD);
        break;
      case REVERSE:
        HardwareConstants.krkExampleMotor.set(motorSpeed * Settings.REVERSE);
        break;
      case IDLE:
      default:
        HardwareConstants.krkExampleMotor.set(0);
        break;
    }
  }

  public void setState(State state) {
    this.currentState = state;
  }

  public Command setStateCommand(State state) {
    return Commands.runOnce(() -> setState(state), this);
  }


}