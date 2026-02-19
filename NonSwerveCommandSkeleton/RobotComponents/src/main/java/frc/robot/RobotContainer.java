// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Constants.OperatorConstants;

import frc.robot.commands.ConveyorActivationCommand;
import frc.robot.commands.ConveyorForwardCommand;
import frc.robot.commands.ConveyorReverseCommand;
import frc.robot.commands.IntakeLifterDownCommand;
import frc.robot.commands.IntakeInCommand;
import frc.robot.commands.IntakeLifterUpCommand;
import frc.robot.commands.KickerCommand;
import frc.robot.commands.ShooterActivationCommand;
import frc.robot.commands.ShooterSpeedCommand;

import frc.robot.subsystems.ConveyorSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.KickerSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

public class RobotContainer {
  // The robots subsystems and commands are defined below
  private final ConveyorSubsystem m_conveyorSubsystem = new ConveyorSubsystem();
  private final IntakeSubsystem m_intakeSubsystem = new IntakeSubsystem();
  private final KickerSubsystem m_kickerSubsystem = new KickerSubsystem();
  private final ShooterSubsystem m_shooterSubsystem = new ShooterSubsystem();
  
  // The robotics controllers are defined below
  private final CommandXboxController m_xboxControllerOne = new CommandXboxController(OperatorConstants.controllerOnePort);
  private final CommandXboxController m_xboxControllerTwo = new CommandXboxController(OperatorConstants.controllerTwoPort);

  // A chooser for autonomous commands
  SendableChooser<Command> m_chooser = new SendableChooser<>();

  public RobotContainer() {
    configureBindings();
    CameraServer.startAutomaticCapture();
  }

  private void configureBindings() {
    // Bindings will need to be configured based on need
    // Controller One Bindings --- Shooter
    m_xboxControllerOne.b().onChange(new ShooterActivationCommand(m_shooterSubsystem)); // Activate/Deactivate Shooter
    m_xboxControllerOne.y().onChange(new ShooterSpeedCommand(m_shooterSubsystem)); // Increase shooter speed by 0.025
    m_xboxControllerOne.a().onChange(new ShooterSpeedCommand(m_shooterSubsystem)); // Decrease shooter speed by 0.025

    // Controller One Bindings --- Kicker
    m_xboxControllerOne.rightBumper().whileTrue(new KickerCommand(m_kickerSubsystem)); // On & Off push ball towards into shooter
    m_xboxControllerOne.leftBumper().whileTrue(new KickerCommand(m_kickerSubsystem)); // On & Off push ball away from shooter
    
     // Controller Two Bindings --- Intake
    m_xboxControllerTwo.x().onChange(new IntakeInCommand(m_intakeSubsystem)); // Activate/Deactivate Intake
    m_xboxControllerTwo.y().whileTrue(new IntakeLifterUpCommand(m_intakeSubsystem)); // Intake Up
    m_xboxControllerTwo.a().whileTrue(new IntakeLifterDownCommand(m_intakeSubsystem)); // Intake Down

    // Controller Two Bindings --- Conveyor
    // m_xboxControllerTwo.b().onChange(new ConveyorActivationCommand(m_conveyorSubsystem)); // Activate/Deactivate Conveyor
    m_xboxControllerTwo.rightBumper().whileTrue(new ConveyorForwardCommand(m_conveyorSubsystem)); // Move conveyor towards the shooter
    m_xboxControllerTwo.leftBumper().whileTrue(new ConveyorReverseCommand(m_conveyorSubsystem)); // Move conveyor away from shooter
   
  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
