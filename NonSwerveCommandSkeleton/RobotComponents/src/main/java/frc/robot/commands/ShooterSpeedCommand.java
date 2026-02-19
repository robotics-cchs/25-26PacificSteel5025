// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.OperatorConstants;
import frc.robot.subsystems.ShooterSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class ShooterSpeedCommand extends Command {
  
  private final ShooterSubsystem m_shooterSubsystem;
  private double shooterSpeed = OperatorConstants.INIT_SHOOTER_SPEED;
  private final double MIN_SPEED_COUNTER = 0.125;
  private final double MAX_SPEED_COUNTER = 0.800;

  /** Creates a new ShooterSpeedCommand. */
  public ShooterSpeedCommand(ShooterSubsystem subsystem) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_shooterSubsystem = subsystem;
    addRequirements(m_shooterSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    shooterSpeed = OperatorConstants.tfxLeftShooterMotor.get();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
   
    if(OperatorConstants.controllerOne.getYButtonPressed() && shooterSpeed < MAX_SPEED_COUNTER) {
      shooterSpeed = (shooterSpeed + 0.025);
    }

    if(OperatorConstants.controllerOne.getAButtonPressed() && shooterSpeed > MIN_SPEED_COUNTER) {
      shooterSpeed = (shooterSpeed - 0.025);
    }

    if(OperatorConstants.tfxLeftShooterMotor.isAlive() && OperatorConstants.tfxRightShooterMotor.isAlive()) {
      m_shooterSubsystem.shooterSpeed(shooterSpeed);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) { 
    if(interrupted) { 
      m_shooterSubsystem.stop(); 
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
