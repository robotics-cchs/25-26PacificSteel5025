// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.MechanismConstants.OperatorConstants;
import edu.wpi.first.math.controller.PIDController;

public class IntakeLifterSubsystem extends SubsystemBase {
  // https://docs.wpilib.org/en/stable/docs/software/advanced-controls/controllers/pidcontroller.html#pid-control-in-wpilib
  private final PIDController pid;

  // current setpoint
  private double setpointRotations = 0.0;

  private double up = OperatorConstants.MotorSettings.LIFTER_UP_SETPOINT;
  private double mid = OperatorConstants.MotorSettings.LIFTER_MIDDLE_SETPOINT;
  private double down = OperatorConstants.MotorSettings.LIFTER_DOWN_SETPOINT;

  public IntakeLifterSubsystem() {
    OperatorConstants.krkIntakeLifterMotor.getConfigurator().apply(OperatorConstants.defaultConfig);
    OperatorConstants.krkIntakeLifterMotor.setSafetyEnabled(OperatorConstants.SET_SAFETY_TRUE);

    // create PID controller using constants
    pid = new PIDController(
        OperatorConstants.MotorSettings.LIFTER_kP,
        OperatorConstants.MotorSettings.LIFTER_kI,
        OperatorConstants.MotorSettings.LIFTER_kD);
    pid.setTolerance(OperatorConstants.MotorSettings.LIFTER_POSITION_TOLERANCE_ROTATIONS);

    zeroLifterEncoder();
  }

  @Override
  public void periodic() {
    double currentRotations = OperatorConstants.krkIntakeLifterMotor.getPosition().getValueAsDouble();

    // calculate PID
    double output = pid.calculate(currentRotations, setpointRotations);

    // we dont want anything more or less than 100%
    if (output > 1.0) output = 1.0;
    if (output < -1.0) output = -1.0;

    // send da percent output to TalonFX
    OperatorConstants.krkIntakeLifterMotor.set(output);

    SmartDashboard.putNumber("Intake Lifter Position", currentRotations);
    SmartDashboard.putNumber("Intake Lifter Setpoint", setpointRotations);
    SmartDashboard.putNumber("Intake Lifter PID Output", output);
    SmartDashboard.putBoolean("Intake Lifter At Position?", atSetpoint());
  }

  public void setLifterPercent(double percent) { // for manual use
    setpointRotations = OperatorConstants.krkIntakeLifterMotor.getPosition().getValueAsDouble(); // cancel pid control by keeping setpoint
    pid.reset();
    OperatorConstants.krkIntakeLifterMotor.set(percent);
  }

  public void setLifterPositionRotations(double rotations) { // for manual setting
    setpointRotations = rotations;
    pid.reset();
  }

  public boolean atSetpoint() {
    return pid.atSetpoint();
  }

  /**
   * This might also be broken. Why?
   * 1. LIFTER_MIDDLE_SETPOINT is set to a -12.5, LIFTER_DOWN_SETPOINT is set to a -25.0. We need to verify that
   * setPointRotation is actually staying in the negatives and that both if statement are working
   * 
   * How do we check?
   * We need to use SmartDashboard and pay attention to the number that is popping up. Since variables up and mid are in the 
   * negatives we need to make sure our setpoint rotation is negative and staying negative also
   */
  public void setLifterUp() {
    if (setpointRotations < up) {
      setpointRotations += 12.5;
    }
  }

  public void setLifterDown() {
    if (setpointRotations > down) {
      setpointRotations -= 12.5;
    }
  }

  public void zeroLifterEncoder() {
    OperatorConstants.krkIntakeLifterMotor.getConfigurator().setPosition(0.0);
  }

}
