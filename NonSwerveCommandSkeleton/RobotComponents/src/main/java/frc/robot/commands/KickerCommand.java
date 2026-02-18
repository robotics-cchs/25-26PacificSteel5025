// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.OperatorConstants;
import frc.robot.subsystems.KickerSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class KickerCommand extends Command {
  private final KickerSubsystem m_kickerSubsystem;

  private double kickerSpeed;
  
  /** Creates a new KickerCommand. */
  public KickerCommand(KickerSubsystem subsystem) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_kickerSubsystem = subsystem;
    addRequirements(m_kickerSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    kickerSpeed = 0;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    kickerSpeed = OperatorConstants.INIT_KICKER_SPEED;
    m_kickerSubsystem.kickerSpeed(kickerSpeed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    if(interrupted) { m_kickerSubsystem.stop(); }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
