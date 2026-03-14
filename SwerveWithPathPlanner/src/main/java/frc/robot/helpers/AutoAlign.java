// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.helpers;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.constants.MechanismConstants.OperatorConstants;

import org.photonvision.PhotonUtils;

import edu.wpi.first.math.controller.PIDController;


public class AutoAlign {
  private final PIDController pid;
  /** Creates a new AutoAlign. */

  public static Pose2d targetPose(Pose2d robotLocation) {
      Alliance alliance = DriverStation.getAlliance().orElse(Alliance.Blue);
      if (alliance == Alliance.Red) {
        if (robotLocation.getX() > 12) {
          return new Pose2d(11.9,4.0,new Rotation2d());
        } else {
          if (robotLocation.getY()>4) {
            return new Pose2d(11.9,5.6,new Rotation2d());
          } else {
            return new Pose2d(11.9,2.5,new Rotation2d());
          }
        }
      } else {
        if (robotLocation.getX() < 4) {
          return new Pose2d(4.650,4.0,new Rotation2d());
        } else {
          if (robotLocation.getY()>4) {
            return new Pose2d(4.650,5.6,new Rotation2d());
          } else {
            return new Pose2d(4.650,2.5,new Rotation2d());
          }
        }
        
      }
  }
  public AutoAlign() {
    pid = new PIDController(
        OperatorConstants.MotorSettings.ALIGN_P,
        OperatorConstants.MotorSettings.ALIGN_I,
        OperatorConstants.MotorSettings.ALIGN_D);
    pid.setTolerance(OperatorConstants.MotorSettings.ALIGN_TOLERANCE);
  }

  public double robotRotationOffset(Pose2d robotLocation, Rotation2d robotDir, boolean tracking) {
    double yaw = PhotonUtils.getYawToPose(robotLocation, targetPose(robotLocation)).getRotations();
    double offset = pid.calculate(0, yaw);

    SmartDashboard.putNumber("Offset", offset);
    SmartDashboard.putBoolean("Tracking?", tracking);

    if (offset > 1.0) offset = 1.0;
    if (offset < -1.0) offset = -1.0;

    if ((((robotLocation.getX() > 3.5)&&(robotLocation.getX() < 5))||((robotLocation.getX() < 13)&&(robotLocation.getX() > 11.5)))) offset = 0;
    
    if (!tracking) offset = 0;

    return offset;
    
  }
}
