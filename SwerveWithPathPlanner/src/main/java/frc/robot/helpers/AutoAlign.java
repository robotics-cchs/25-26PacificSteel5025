// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.helpers;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.constants.MechanismConstants.OperatorConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;

import org.photonvision.PhotonUtils;

import com.ctre.phoenix6.swerve.SwerveDrivetrain;
import com.ctre.phoenix6.swerve.SwerveDrivetrain.SwerveDriveState;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;


public class AutoAlign {
  private final PIDController pid;
  /** Creates a new AutoAlign. */

  public static Pose2d targetPose() {
      Alliance alliance = DriverStation.getAlliance().orElse(Alliance.Blue);
      if (alliance == Alliance.Red) {
          return new Pose2d(11.9,4.0,new Rotation2d());
      } else {
          return new Pose2d(4.650,4.0,new Rotation2d());
      }
  }
  public AutoAlign() {
    pid = new PIDController(
        OperatorConstants.MotorSettings.ALIGN_P,
        OperatorConstants.MotorSettings.ALIGN_I,
        OperatorConstants.MotorSettings.ALIGN_D);
    pid.setTolerance(OperatorConstants.MotorSettings.ALIGN_TOLERANCE);
    pid.enableContinuousInput(-Math.PI, Math.PI);
  }

  public double robotRotationOffset(CommandSwerveDrivetrain robot, boolean tracking) {
    Pose2d predLocation = robot.getState().Pose.transformBy(new Transform2d(robot.getState().Speeds.vxMetersPerSecond/50,robot.getState().Speeds.vyMetersPerSecond/50,new Rotation2d(robot.getState().Speeds.omegaRadiansPerSecond/50)));
    double yaw = PhotonUtils.getYawToPose(predLocation, targetPose()).getRadians();
    double offset = pid.calculate(predLocation.getRotation().getRadians(), predLocation.getRotation().getRadians()+yaw);

    SmartDashboard.putNumber("Offset", offset);
    SmartDashboard.putBoolean("Tracking?", tracking);
    
    if (!tracking) offset = 0;
    if (Math.abs(yaw) < .25) offset *= .1;
    if (Math.abs(yaw) < .5) offset *= .5;
    return MathUtil.clamp(offset, -1.0, 1.0);
    
  }
}
