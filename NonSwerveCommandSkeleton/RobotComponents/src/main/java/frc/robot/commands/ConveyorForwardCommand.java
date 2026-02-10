// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ConveyorSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class ConveyorForwardCommand extends Command {
  private final ConveyorSubsystem m_conveyorSubsystem;

  private double conveyorForwardSpeed;
  
  /** Creates a new ConveyorForwardCommand. */
  public ConveyorForwardCommand(ConveyorSubsystem subsystem) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_conveyorSubsystem = subsystem;
    addRequirements(m_conveyorSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    conveyorForwardSpeed = 0;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    m_conveyorSubsystem.conveyorForwardSpeed(conveyorForwardSpeed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    if(interrupted) { m_conveyorSubsystem.stop(); }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
