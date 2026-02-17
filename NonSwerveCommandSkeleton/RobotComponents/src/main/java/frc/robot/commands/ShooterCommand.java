// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.OperatorConstants;
import frc.robot.subsystems.ShooterSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class ShooterCommand extends Command {
  private final ShooterSubsystem m_shooterSubsystem;

  private double speedCounter = OperatorConstants.INIT_SHOOTER_SPEED;
  private double shooterSpeed;
  private boolean shooterActivated;
  private final double MIN_SPEED_COUNTER = OperatorConstants.INIT_SHOOTER_SPEED;
  private final double MAX_SPEED_COUNTER = 0.3;
  
  /** Creates a new ShooterCommand. */
  public ShooterCommand(ShooterSubsystem subsystem) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_shooterSubsystem = subsystem;
    addRequirements(m_shooterSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    shooterSpeed = 0;
    shooterActivated = false;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(OperatorConstants.controllerOne.getBButtonPressed()) {
      shooterActivated = !shooterActivated;
    }
    if(OperatorConstants.controllerOne.getYButtonPressed() && speedCounter != MAX_SPEED_COUNTER) {
      speedCounter = speedCounter + 0.025;
    }
    if(OperatorConstants.controllerOne.getAButtonPressed() && speedCounter != MIN_SPEED_COUNTER) {
      speedCounter = speedCounter - 0.025;
    }

    SmartDashboard.putBoolean("Shooter Activated", shooterActivated);

    if(shooterActivated) {
      shooterSpeed = OperatorConstants.INIT_SHOOTER_SPEED + speedCounter;
      m_shooterSubsystem.shooterSpeed(shooterSpeed);
    } 
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    if(interrupted) { m_shooterSubsystem.stop(); }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
