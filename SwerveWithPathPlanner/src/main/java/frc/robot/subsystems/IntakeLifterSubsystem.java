// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.MechanismConstants.OperatorConstants;
import edu.wpi.first.math.controller.PIDController;

public class IntakeLifterSubsystem extends SubsystemBase {
  // https://docs.wpilib.org/en/stable/docs/software/commandbased/pid-subsystems-commands.html
  private final PIDController pid;

  // Current setpoint in Rotations
  private double setpointRotations = 0.0;

  public IntakeLifterSubsystem() {
    OperatorConstants.krkIntakeLifterMotor.getConfigurator().apply(OperatorConstants.defaultConfig);
    OperatorConstants.krkIntakeLifterMotor.setSafetyEnabled(OperatorConstants.SET_SAFETY_TRUE);

    // Create PID controller with constants from MechanismConstants
    pid = new PIDController(
        OperatorConstants.MotorSettings.LIFTER_kP,
        OperatorConstants.MotorSettings.LIFTER_kI,
        OperatorConstants.MotorSettings.LIFTER_kD);
    pid.setTolerance(OperatorConstants.MotorSettings.LIFTER_POSITION_TOLERANCE_ROTATIONS);

    zeroLifterEncoder();
  }

  @Override
  public void periodic() {
    double currentRotations = readRotorPositionRotations();

    // calculate PID
    double output = pid.calculate(currentRotations, setpointRotations);

    // Clamping
    if (output > 1.0) output = 1.0;
    if (output < -1.0) output = -1.0;

    // Send percent output to TalonFX
    OperatorConstants.krkIntakeLifterMotor.set(output);

    SmartDashboard.putNumber("Intake Lifter Position", currentRotations);
    SmartDashboard.putNumber("Intake Lifter Setpoint", setpointRotations);
    SmartDashboard.putNumber("Intake Lifter PID Output", output);
  }

  public void setLifterPercent(double percent) {
    setpointRotations = readRotorPositionRotations(); // cancel position control by keeping setpoint
    pid.reset();
    OperatorConstants.krkIntakeLifterMotor.set(percent);
  }

  public void setLifterPositionRotations(double rotations) {
    setpointRotations = rotations;
    pid.reset();
  }

  public boolean atSetpoint() {
    return pid.atSetpoint();
  }

  public void zeroLifterEncoder() {
    OperatorConstants.krkIntakeLifterMotor.getConfigurator().setPosition(0.0);
  }

  // Helper to read the rotor position in encoder Rotations. Uses Phoenix6 rotor position signal.
  private double readRotorPositionRotations() {
    return OperatorConstants.krkIntakeLifterMotor.getPosition().getValueAsDouble();
  }

}
