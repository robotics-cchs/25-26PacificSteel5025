// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.mechanisms;

import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.MechanismConstants.Configs;
import frc.robot.constants.MechanismConstants.HardwareConstants;
import frc.robot.constants.MechanismConstants.Settings;
import frc.robot.telemetry.Logger;

public class ExampleMechanismSubsystem extends SubsystemBase {

  public enum State {
    IDLE,
    FORWARD,
    REVERSE
  }

  private State currentState = State.IDLE;
  private double targetVelocityRps = 0.0;

  private final TalonFX m_motor;
  private final VelocityVoltage m_velocityVoltage = new VelocityVoltage(0.0);

  /** Creates a new ExampleMechanismSubsystem. */
  public ExampleMechanismSubsystem() {
    m_motor = new TalonFX(HardwareConstants.tfxPort50);
    m_motor.getConfigurator().apply(Configs.defaultConfig);
    m_motor.setSafetyEnabled(Settings.SET_SAFETY_TRUE);
  }

  @Override
  public void periodic() {
    double actualVelocityRps = m_motor.getVelocity().getValueAsDouble();

    Logger.recordOutput("ExampleMechanism/MotorVoltage", m_motor.getMotorVoltage().getValueAsDouble());
    Logger.recordOutput("ExampleMechanism/MotorCurrentAmps", m_motor.getStatorCurrent().getValueAsDouble());
    Logger.recordOutput("ExampleMechanism/State", currentState.toString());
    Logger.recordOutput("ExampleMechanism/TargetVelocityRps", targetVelocityRps);
    Logger.recordOutput("ExampleMechanism/ActualVelocityRps", actualVelocityRps);

    // Determine target velocity based on state
    switch (currentState) {
      case FORWARD:
        targetVelocityRps = Settings.SHOOT_VELOCITY_RPS;
        break;
      case REVERSE:
        targetVelocityRps = Settings.INTAKE_VELOCITY_RPS;
        break;
      case IDLE:
      default:
        targetVelocityRps = 0.0;
        break;
    }

    // Drive using onboard PID + Feedforward closed loop control (Slot 0 Configured)
    m_motor.setControl(m_velocityVoltage.withVelocity(targetVelocityRps));
  }

  public void setState(State state) {
    this.currentState = state;
  }

  /**
   * Command factory to change the state of this mechanism.
   *
   * @param state The target state
   * @return A command that sets the state and requires this subsystem
   */
  public Command runStateCommand(State state) {
    return Commands.runOnce(() -> setState(state), this);
  }

  /**
   * Run the shooter at a custom target velocity (like a standard WPILib PID command setpoint).
   *
   * @param velocityRps Target velocity in rotations per second
   * @return A command that sets the custom velocity state and runs until interrupted
   */
  public Command runVelocityCommand(double velocityRps) {
    return runEnd(
        () -> {
            setState(State.FORWARD); // Set state to forward to allow setpoint control
            targetVelocityRps = velocityRps;
        },
        () -> setState(State.IDLE)
    );
  }

  /**
   * Legacy wrapper for state changes, deprecated in favor of runStateCommand.
   */
  @Deprecated
  public Command setStateCommand(State state) {
    return runStateCommand(state);
  }
}