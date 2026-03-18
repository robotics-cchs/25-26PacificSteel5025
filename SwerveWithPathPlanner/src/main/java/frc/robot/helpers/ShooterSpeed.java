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

public class ShooterSpeed {
    public ShooterSpeed() {
        
    }
    public static Pose2d targetPose() {
      Alliance alliance = DriverStation.getAlliance().orElse(Alliance.Blue);
      if (alliance == Alliance.Red) {
        return new Pose2d(11.9,4.0,new Rotation2d());
      } else {
        return new Pose2d(4.650,4.0,new Rotation2d());
      }
    }
    public static double targetSpeed(Pose2d robotLocation) {
        
        return 0;
    }

}
