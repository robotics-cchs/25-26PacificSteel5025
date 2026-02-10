package frc.robot.constants;
// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.AnalogEncoder;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.Encoder;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

/** Add your docs here. */
public class MechanismConstants {
    public static class OperatorConstants {

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
        public static final int tsrxPort40 = 40;
        public static final int tsrxPort41 = 41;
        public static final int tsrxPort42 = 42;
        public static final int tsrxPort43 = 43;
        public static final int tsrxPort44 = 44;
        public static final int tsrxPort45 = 45;
        public static final int tsrxPort46 = 46;
        public static final int tsrxPort47 = 47;
        public static final int tsrxPort48 = 48;
        public static final int tsrxPort49 = 49;
        public static final int tsrxPort50 = 50;
        public static final int tfxPort51 = 51;
        public static final int tfxPort52 = 52;
        public static final int tfxPort53 = 53;
        public static final int tfxPort54 = 54;
        public static final int tfxPort55 = 55;
        public static final int tfxPort56 = 56;
        public static final int tfxPort57 = 57;
        public static final int tfxPort58 = 58;
        public static final int tfxPort59 = 59;
       
        // Pneumatics Ports: 60 ... 69
        public static final int pneumaticPort60 = 60;
        public static final int pneumaticPort61 = 61;
        public static final int pneumaticPort62 = 62;
        public static final int pneumaticPort63 = 63;
        public static final int pneumaticPort64 = 64;
        public static final int pneumaticPort65 = 65;
        public static final int pneumaticPort66 = 66;
        public static final int pneumaticPort67 = 67;
        public static final int pneumaticPort68 = 68;
        public static final int pneumaticPort69 = 69;

        // Initialize Controllers/Joysticks.
        public static final Joystick joystickOne = new Joystick(joystickOnePort);
        public static final Joystick joystickTwo = new Joystick(joystickTwoPort);
        public static final CommandXboxController controllerOne = new CommandXboxController(controllerOnePort);
        public static final CommandXboxController controllerTwo = new CommandXboxController(controllerTwoPort);

        // Initialize Non-Swerve Encoders
        public static final Encoder quadEncoder = new Encoder(quadEncoderChannelA, quadEncoderChannelB);
        public static final AnalogEncoder absEncoder = new AnalogEncoder(absEncoderChannelA);
        public static final DutyCycleEncoder dceEncder = new DutyCycleEncoder(dceEncoderChannelA);

        // Initialize TalonSRX Non-Swerve MotorControllers
        public static final TalonSRX sLeftShooterMotor = new TalonSRX(tsrxPort40); //Running during entire comp.
        public static final TalonSRX sRightShooterMotor = new TalonSRX(tsrxPort41); //Running during entire comp.

        public static final TalonSRX sLeftIntakeMotor = new TalonSRX(tsrxPort42);
        public static final TalonSRX sRightIntakeMotor = new TalonSRX(tsrxPort43);

        public static final TalonSRX sLeftIntakeLifterMotor = new TalonSRX(tsrxPort44);
        public static final TalonSRX sRightIntakeLifterMotor = new TalonSRX(tsrxPort45);

        public static final TalonSRX sLeftConveyorMotor = new TalonSRX(tsrxPort46);
        public static final TalonSRX sRightConveyorMotor = new TalonSRX(tsrxPort47);

        public static final TalonSRX sLeftKickerMotor = new TalonSRX(tsrxPort48);
        public static final TalonSRX sRightKickerMotor = new TalonSRX(tsrxPort49);
        public static final TalonSRX randomMotor = new TalonSRX(tsrxPort50);

        // Initialize TalonFX Non-Swerve Motor Controllers
        public static final TalonFX motor = new TalonFX(tfxPort51);

        
        // Initialize Pneumatics

    }
}
