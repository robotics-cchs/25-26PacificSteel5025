package frc.robot;
// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.=

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.AnalogEncoder;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.Encoder;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;

/** Add your docs here. */
public class Constants {
    public static class OperatorConstants {

        // Controller/Joystick Ports
        public static final int controllerOnePort = 0;
        public static final int controllerTwoPort = 1;
        public static final int joystickOnePort = 2;
        public static final int joystickTwoPort = 3;

        // Swerve Encoder Ports: 00 ... 09
        
        // Misc Encoder Ports: 10 ... 19
        /**
         * Values can not be all 0 otherwise it throws an error and does not allow
         * for program to be initiated on Roborio
         */
        public static final int quadEncoderChannelA = 0;
        public static final int quadEncoderChannelB = 1;
        public static final int absEncoderChannelA = 2;
        public static final int dceEncoderChannelA = 3;

        // Swerve Turn Ports: 20 ... 29
        
        // Swerve Drive Ports: 30 ... 39
        
        // TalonSRX MotorController Ports: 40 ... 49
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

        // TalonFX MotorController Ports: 50 ... 59
        public static final int tfxPort50 = 50;
        public static final int tfxPort51 = 51;
        public static final int tfxPort52 = 52;
        public static final int tfxPort53 = 53;
        public static final int tfxPort54 = 54;
        public static final int tfxPort55 = 55;
        public static final int tfxPort56 = 56;
        public static final int tfxPort57 = 57;
        public static final int tfxPort58 = 58;
        public static final int tfxPort59 = 59;

        // Constants
        public static final double MAX_VOLTAGE = 12.2;
        public static final double MAX_AMPS = 50;
        public static final boolean SET_SAFETY_TRUE = true;

        // Constant Speeds
        public static final double INIT_CONVEYOR_SPEED = 0.125;
        public static final double INIT_INTAKE_SPEED = 0.125;
        public static final double INIT_INTAKELIFTER_SPEED = 0.125;
        public static final double INIT_KICKER_SPEED = 0.125;
        public static final double INIT_SHOOTER_SPEED = 0.125;

        // Initialize Controllers/Joysticks.
        public static final Joystick joystickOne = new Joystick(joystickOnePort);
        public static final Joystick joystickTwo = new Joystick(joystickTwoPort);
        public static final XboxController controllerOne = new XboxController(controllerOnePort);
        public static final XboxController controllerTwo = new XboxController(controllerTwoPort);

        // Initialize Non-Swerve Encoders
        public static final Encoder quadEncoder = new Encoder(quadEncoderChannelA, quadEncoderChannelB);
        public static final AnalogEncoder absEncoder = new AnalogEncoder(absEncoderChannelA);
        public static final DutyCycleEncoder dceEncder = new DutyCycleEncoder(dceEncoderChannelA);

        // Initialize TalonSRX MotorControllers
        // public static final TalonSRX tsrxLeftShooterMotor = new TalonSRX(tsrxPort40);
        // public static final TalonSRX tsrxRightShooterMotor = new TalonSRX(tsrxPort41); 

        // public static final TalonSRX tsrxLeftIntakeMotor = new TalonSRX(tsrxPort42);
        // public static final TalonSRX tsrxRightIntakeMotor = new TalonSRX(tsrxPort43);

        // public static final TalonSRX tsrxLeftIntakeLifterMotor = new TalonSRX(tsrxPort44);
        // public static final TalonSRX tsrxRightIntakeLifterMotor = new TalonSRX(tsrxPort45);

        // public static final TalonSRX tsrxLeftConveyorMotor = new TalonSRX(tsrxPort46);
        // public static final TalonSRX tsrxRightConveyorMotor = new TalonSRX(tsrxPort47);

        // public static final TalonSRX tsrxLeftKickerMotor = new TalonSRX(tsrxPort48);
        // public static final TalonSRX tsrxRightKickerMotor = new TalonSRX(tsrxPort49);

        // Initialize TalonFX Non-Swerve Motor Controllers
        public static final TalonFX tfxLeftShooterMotor = new TalonFX(tfxPort50); // Kraken X60
        public static final TalonFX tfxRightShooterMotor = new TalonFX(tfxPort51); // Kraken X60

        public static final TalonFX tfxIntakeMotor = new TalonFX(tfxPort52); // Kraken X44
        public static final TalonFX tfxIntakeLifterMotor = new TalonFX(tfxPort53); // Kraken X60

        public static final TalonFX tfxConveyorMotor = new TalonFX(tfxPort54); // Kraken X44

        public static final TalonFX tfxLeftKickerMotor = new TalonFX(tfxPort55); // Kraken X44
        public static final TalonFX tfxRightKickerMotor = new TalonFX(tfxPort56); // Kraken X44
    }
}
