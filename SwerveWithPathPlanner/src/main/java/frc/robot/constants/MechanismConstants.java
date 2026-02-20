package frc.robot.constants;
// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

import static edu.wpi.first.units.Units.Amps;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;

import edu.wpi.first.wpilibj.AnalogEncoder;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.Encoder;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

/** Add your docs here. */
public class MechanismConstants {
    public static class OperatorConstants {
        public final class MotorSettings {

            public static final double INTAKE_SPEED = 0.5;
            public static final double INTAKE_LIFTER_SPEED = 0.5;
            public static final double KICKER_SPEED_BASE = 0.4;
            public static final double SHOOTER_SPEED_BASE = 0.4;
            public static final double CONVEYOR_SPEED = 0.5;

            private MotorSettings() {}
        }
        public static final double FORWARD = 1;
        public static final double REVERSE = -1;

        public static final double maxVoltage = 12.2;
        public static final double maxCurrentSupply = 60;
        public static final double maxCurrentStator = 60;
        // Controller/Joystick Ports
        public static final int controllerOnePort = 0;
        public static final int controllerTwoPort = 1;
        public static final int joystickOnePort = 2;
        public static final int joystickTwoPort = 3;

        // Swerve Encoder Ports: 00 ... 09
        
        // Misc Encoder Ports: 10 ... 19
        public static final int quadEncoderChannelA = 0;
        public static final int quadEncoderChannelB = 1;
        public static final int absEncoderChannelA = 0;
        public static final int dceEncoderChannelA = 0;

        // Swerve Turn Ports: 20 ... 29
        
        // Swerve Drive Ports: 30 ... 39
        
        // Misc MC Ports: 40 ... 59
        // public static final int tfxPort40 = 40;
        // public static final int tfxPort41 = 41;
        // public static final int tfxPort42 = 42;
        // public static final int tfxPort43 = 43;
        // public static final int tfxPort44 = 44;
        // public static final int tfxPort45 = 45;
        // public static final int tfxPort46 = 46;
        // public static final int tfxPort47 = 47;
        // public static final int tfxPort48 = 48;
        // public static final int tfxPort49 = 49;
        public static final int tfxPort50 = 50;
        public static final int tfxPort51 = 51;
        public static final int tfxPort52 = 52;
        public static final int tfxPort53 = 53;
        public static final int tfxPort54 = 54;
        public static final int tfxPort55 = 55;
        public static final int tfxPort56 = 56;
        // public static final int tfxPort57 = 57;
        // public static final int tfxPort58 = 58;
        // public static final int tfxPort59 = 59;
       
        // Pneumatics Ports: 60 ... 69
        // public static final int pneumaticPort60 = 60;
        // public static final int pneumaticPort61 = 61;
        // public static final int pneumaticPort62 = 62;
        // public static final int pneumaticPort63 = 63;
        // public static final int pneumaticPort64 = 64;
        // public static final int pneumaticPort65 = 65;
        // public static final int pneumaticPort66 = 66;
        // public static final int pneumaticPort67 = 67;
        // public static final int pneumaticPort68 = 68;
        // public static final int pneumaticPort69 = 69;

        // Initialize Controllers/Joysticks.
        // public static final Joystick joystickOne = new Joystick(joystickOnePort);
        // public static final Joystick joystickTwo = new Joystick(joystickTwoPort);
        public static final CommandXboxController controllerOne = new CommandXboxController(controllerOnePort);
        public static final CommandXboxController controllerTwo = new CommandXboxController(controllerTwoPort);

        // Initialize Non-Swerve Encoders
        public static final Encoder quadEncoder = new Encoder(quadEncoderChannelA, quadEncoderChannelB);
        public static final AnalogEncoder absEncoder = new AnalogEncoder(absEncoderChannelA);
        public static final DutyCycleEncoder dceEncder = new DutyCycleEncoder(dceEncoderChannelA);

        // Initialize TalonFX Non-Swerve MotorControllers
        public static final TalonFX krkLeftShooterMotor = new TalonFX(tfxPort50); //Running during entire comp.
        public static final TalonFX krkRightShooterMotor = new TalonFX(tfxPort51); //Running during entire comp.

        public static final TalonFX krkIntakeMotor = new TalonFX(tfxPort52);
        public static final TalonFX krkIntakeLifterMotor = new TalonFX(tfxPort53);

        public static final TalonFX krkConveyorMotor = new TalonFX(tfxPort55);

        public static final TalonFX krkLeftKickerMotor = new TalonFX(tfxPort55);
        public static final TalonFX krkRightKickerMotor = new TalonFX(tfxPort56);

        // Initialize TalonFX Non-Swerve Motor Controllers
        public static final TalonFX motor = new TalonFX(tfxPort51);
        
        public static final TalonFXConfiguration defaultConfig = new TalonFXConfiguration().withCurrentLimits(
            new CurrentLimitsConfigs()
                .withStatorCurrentLimit(Amps.of(maxCurrentStator)) // Makes stator current limits
                .withStatorCurrentLimitEnable(true) // Enables the current limits
                .withSupplyCurrentLimit(Amps.of(maxCurrentSupply)) // Makes supply current limits
                .withSupplyCurrentLimitEnable(true) // Enables the current limits
        ).withMotorOutput(
            new MotorOutputConfigs()
                .withInverted(InvertedValue.Clockwise_Positive) // Says to not inverse the motor
        );
        public static final TalonFXConfiguration invertedDefaultConfig = new TalonFXConfiguration().withCurrentLimits(
            new CurrentLimitsConfigs()
                .withStatorCurrentLimit(Amps.of(maxCurrentStator)) // Makes stator current limits
                .withStatorCurrentLimitEnable(true) // Enables the current limits
                .withSupplyCurrentLimit(Amps.of(maxCurrentSupply)) // Makes supply current limits
                .withSupplyCurrentLimitEnable(true) // Enables the current limits
        ).withMotorOutput(
            new MotorOutputConfigs()
                .withInverted(InvertedValue.CounterClockwise_Positive) // Says to inverse the motor
        );

        // Initialize Pneumatics

    }
}
