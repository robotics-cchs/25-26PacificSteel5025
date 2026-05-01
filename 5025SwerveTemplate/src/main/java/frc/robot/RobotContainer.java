// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.net.PortForwarder;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.FollowPathCommand;
import com.pathplanner.lib.commands.PathfindingCommand;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.RobotModeTriggers;
import frc.robot.constants.StateConstants;
import frc.robot.constants.PoseConstants;
import frc.robot.constants.SwerveConstants;
import frc.robot.constants.MechanismConstants.OperatorConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.VisionSubsystem;
import frc.robot.subsystems.StateHandler;
import frc.robot.subsystems.mechanisms.ExampleMechanismSubsystem;
import frc.robot.telemetry.Telemetry;

public class RobotContainer {
    double speed = 1.00;
    
    // Drive Speed Located Here
    private double MaxSpeed = speed * SwerveConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
    private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity

    // Setting up bindings for necessary control of the swerve drive platform
    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
    private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();

    private final Telemetry logger = new Telemetry(MaxSpeed);

    public final CommandSwerveDrivetrain drivetrain = SwerveConstants.createDrivetrain();

    private final VisionSubsystem vision = new VisionSubsystem(drivetrain);
    private final StateHandler stateHandler = new StateHandler(drivetrain);

    /* Path follower */
    private final SendableChooser<Command> autoChooser;
    
    // Initialization of Subsystems
    private final StateHandler m_StateHandler = new StateHandler(drivetrain);

    // Commands
    Command stateCommand = Commands.runOnce(() -> {
        m_StateHandler.setState(StateConstants.STATES.SPECIAL);
    }, m_StateHandler);
    
    public RobotContainer() {
        configureAutoBindings();
        configureBindings();
        
        autoChooser = AutoBuilder.buildAutoChooser("Tests");
        SmartDashboard.putData("Auto Mode", autoChooser);
        // Warmup PathPlanner to avoid Java pauses
        FollowPathCommand.warmupCommand().schedule();
        PathfindingCommand.warmupCommand().schedule();
        PortForwarder.add(5800, "photonvision.local", 5800);
        SmartDashboard.putBoolean("Camera Connected?", vision.isCameraConnected());
    }
    // Path target and commands are handled by StateHandler now.
    
    private void configureAutoBindings() {
        // Register Commands
        NamedCommands.registerCommand("ExampleToggle", stateCommand);
    }
    private void configureBindings() {
        // Note that X is defined as forward according to WPILib convention,
        // and Y is defined as to the left according to WPILib convention.
        drivetrain.setDefaultCommand(
            // Drivetrain will execute this command periodically
            drivetrain.applyRequest(() ->
                drive.withVelocityX(OperatorConstants.controllerOne.getLeftY() * -MaxSpeed / 1) // Drive forward with negative Y (forward)
                    .withVelocityY(OperatorConstants.controllerOne.getLeftX() * -MaxSpeed / 1) // Drive left with negative X (left)
                    // .withRotationalRate((-OperatorConstants.controllerOne.getRightX() + m_autoAlign.robotRotationOffset(drivetrain, OperatorConstants.controllerOne.b().getAsBoolean())) * MaxAngularRate) // Drive counterclockwise with negative X (left)
                    .withRotationalRate((-OperatorConstants.controllerOne.getRightX())* MaxAngularRate) // Drive counterclockwise with negative X (left)
            )
        );

        // Idle while the robot is disabled. This ensures the configured
        // neutral mode is applied to the drive motors while disabled.
        final var idle = new SwerveRequest.Idle();
        RobotModeTriggers.disabled().whileTrue(
            drivetrain.applyRequest(() -> idle).ignoringDisable(true)
        );

        // Drivetrain Bindings
        OperatorConstants.controllerOne.x().whileTrue(drivetrain.applyRequest(() -> brake));
        OperatorConstants.controllerOne.back().onTrue(drivetrain.runOnce(drivetrain::seedFieldCentric));
        OperatorConstants.controllerOne.y().toggleOnTrue(stateHandler.pathfindToCurrentTarget());
        OperatorConstants.controllerOne.povLeft().onTrue(stateHandler.setTarget(PoseConstants.SHOOT_LEFT));
        OperatorConstants.controllerOne.povRight().onTrue(stateHandler.setTarget(PoseConstants.SHOOT_RIGHT));
        OperatorConstants.controllerOne.povDown().onTrue(stateHandler.setTarget(PoseConstants.SHOOT_MID));
        
        // Mechanism Bindings
        OperatorConstants.controllerOne.b().onTrue(stateCommand); //

        drivetrain.registerTelemetry(logger::telemeterize);
    }

    public Command getAutonomousCommand() {
        /* Run the path selected from the auto chooser */
        return autoChooser.getSelected();
    }
}