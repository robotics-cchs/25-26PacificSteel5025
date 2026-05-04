// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.Set;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathConstraints;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.PoseConstants;
import frc.robot.constants.StateConstants.STATES;
import frc.robot.subsystems.mechanisms.ExampleMechanismSubsystem;
import frc.robot.subsystems.mechanisms.ExampleMechanismSubsystem.State;

public class StateMachine extends SubsystemBase {
  private final CommandSwerveDrivetrain drivetrain;
  public final ExampleMechanismSubsystem m_ExampleMechanismSubsystem = new ExampleMechanismSubsystem();
  Pose2d currentTarget = PoseConstants.SHOOT_MID;
  PathConstraints constraints = new PathConstraints(
    3.0, 3.0,
    Units.degreesToRadians(540), Units.degreesToRadians(720)
  );
  
  /** Creates a new StateMachine. */
  public StateMachine(CommandSwerveDrivetrain drivetrain) {
    this.drivetrain = drivetrain;
  }

  public Command pathfindToCurrentTarget() {
    return Commands.defer(
        () -> AutoBuilder.pathfindToPose(currentTarget, constraints, 0.0),
        Set.of(drivetrain)
    );
  }

  public void setPathfindingTarget(Pose2d target) {
    this.currentTarget = target;
  }

  public Command setTarget(Pose2d target) {
    return new InstantCommand(() -> setPathfindingTarget(target));
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public Command setState(STATES state) {
    switch (state) {
      case SHOOT_MID:
        return Commands.parallel(
          m_ExampleMechanismSubsystem.setStateCommand(State.FORWARD),
          setTarget(PoseConstants.SHOOT_MID),
          pathfindToCurrentTarget()
        );
      default:
        return Commands.parallel(
          m_ExampleMechanismSubsystem.setStateCommand(State.IDLE)
        );
    }
  }
}
