// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonPoseEstimator.PoseStrategy;
import org.photonvision.targeting.PhotonPipelineResult;

import java.util.Optional;

import frc.robot.telemetry.Logger;

public class VisionSubsystem extends SubsystemBase {

  // ── Camera config ──────────────────────────────────────────────────────────
  private static final String CAMERA_NAME = "Default";

  private static final Transform3d ROBOT_TO_CAMERA = new Transform3d(
      new Translation3d(Units.inchesToMeters(3.5), 0.0, Units.inchesToMeters(25)),
      new Rotation3d(0.0, Math.toRadians(10.0), 0.0));

  // ── Noise model ────────────────────────────────────────────────────────────
  private static final Matrix<N3, N1> SINGLE_TAG_STD_DEVS =
      VecBuilder.fill(1.0, 1.0, Math.toRadians(20));
  private static final Matrix<N3, N1> MULTI_TAG_STD_DEVS  =
      VecBuilder.fill(0.3, 0.3, Math.toRadians(5));

  // ── Sanity-check limits ────────────────────────────────────────────────────
  private static final double MAX_AMBIGUITY  = 0.2;
  private static final double FIELD_LENGTH_M = 16.54; // 2026 REBUILT: 651.2 in
  private static final double FIELD_WIDTH_M  =  8.07; // 2026 REBUILT: 317.7 in (was 8.21 in 2025)

  // ── Members ────────────────────────────────────────────────────────────────
  private final PhotonCamera            camera;
  private final PhotonPoseEstimator     poseEstimator;
  private final CommandSwerveDrivetrain drivetrain;

  private Optional<EstimatedRobotPose> lastEstimate = Optional.empty();

  // ── Constructor ────────────────────────────────────────────────────────────
  public VisionSubsystem(CommandSwerveDrivetrain drivetrain) {
    this.drivetrain = drivetrain;

    camera = new PhotonCamera(CAMERA_NAME);

    AprilTagFieldLayout layout =
        AprilTagFieldLayout.loadField(AprilTagFields.k2026RebuiltAndymark); // ✓ already correct

    poseEstimator = new PhotonPoseEstimator(
        layout,
        PoseStrategy.MULTI_TAG_PNP_ON_COPROCESSOR,
        ROBOT_TO_CAMERA);

    poseEstimator.setMultiTagFallbackStrategy(PoseStrategy.LOWEST_AMBIGUITY);
  }

  // ── Periodic ───────────────────────────────────────────────────────────────
  @Override
  public void periodic() {
    Logger.recordOutput("Vision/CameraConnected", camera.isConnected());

    PhotonPipelineResult result = camera.getLatestResult();
    
    Logger.recordOutput("Vision/TagsVisible", result.getTargets().size());

    lastEstimate = poseEstimator.update(result);

    lastEstimate.ifPresent(est -> {
      Logger.recordOutput("Vision/LatestEstimate", est.estimatedPose.toPose2d());

      if (!isGoodEstimate(est)) return;

      Matrix<N3, N1> stdDevs = selectStdDevs(est);

      drivetrain.addVisionMeasurement(
          est.estimatedPose.toPose2d(),
          est.timestampSeconds,
          stdDevs);
    });
  }

  // ── Helpers ────────────────────────────────────────────────────────────────

  public Optional<EstimatedRobotPose> getLastEstimate() {
    return lastEstimate;
  }

  public boolean isCameraConnected() {
    return camera.isConnected();
  }

  private boolean isGoodEstimate(EstimatedRobotPose est) {
    if (est.targetsUsed.isEmpty()) return false;

    var pose = est.estimatedPose.toPose2d();
    if (pose.getX() < 0 || pose.getX() > FIELD_LENGTH_M) return false;
    if (pose.getY() < 0 || pose.getY() > FIELD_WIDTH_M)  return false;

    if (est.targetsUsed.size() == 1 &&
        est.targetsUsed.get(0).getPoseAmbiguity() > MAX_AMBIGUITY) return false;

    double pitchDeg = Math.abs(drivetrain.getPigeon2().getPitch().getValueAsDouble());
    if (pitchDeg > 5.0) return false; // tune this threshold (for the bump to not affect vision)

    return true;
  }

  private Matrix<N3, N1> selectStdDevs(EstimatedRobotPose est) {
    double avgDist = 0.0;
    for (var target : est.targetsUsed) {
      avgDist += target.getBestCameraToTarget().getTranslation().getNorm();
    }
    avgDist /= est.targetsUsed.size();

    // The uncertainty roughly scales quadratically with distance.
    double scalar = 1.0 + (avgDist * avgDist) / 10.0;

    Matrix<N3, N1> baseDevs = est.targetsUsed.size() > 1 ? MULTI_TAG_STD_DEVS : SINGLE_TAG_STD_DEVS;
    
    return VecBuilder.fill(
        baseDevs.get(0, 0) * scalar,
        baseDevs.get(1, 0) * scalar,
        baseDevs.get(2, 0) * scalar
    );
  }
}