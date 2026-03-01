// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.
package frc.robot.helpers;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathConstraints;

import org.photonvision.PhotonUtils;

import frc.robot.subsystems.CommandSwerveDrivetrain;

public class ShooterAlignHelper extends Command {
    private final CommandSwerveDrivetrain m_drivetrain;
    private final Pose2d m_targetPose;

    public ShooterAlignHelper(CommandSwerveDrivetrain drivetrain, Pose2d targetPose) {
        m_drivetrain = drivetrain;
        m_targetPose = targetPose;
        addRequirements(m_drivetrain);
    }

    @Override
    public void initialize() {
        Pose2d robotPose = m_drivetrain.getState().Pose;

        // get ideal angle to target
        Rotation2d targetYaw = PhotonUtils.getYawToPose(robotPose, m_targetPose);

        // stay in same place, but rotate to face the target
        Pose2d targetPose = new Pose2d(robotPose.getX(), robotPose.getY(), targetYaw);

        PathConstraints constraints = new PathConstraints(
                3.0, 3.0,
                Units.degreesToRadians(540), Units.degreesToRadians(720));

        Command pathfindingCommand = AutoBuilder.pathfindToPose(
                targetPose,
                constraints,
                0.0 // Goal end velocity in meters/sec
        );
    }
}
