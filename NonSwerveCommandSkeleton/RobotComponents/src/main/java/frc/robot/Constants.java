package frc.robot;
// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.=

import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj.XboxController;

/** Add your docs here. */
public class Constants {
    public static class OperatorConstants {

        // Controller/Joystick Ports
        public static final int controllerOnePort = 0;
        public static final int controllerTwoPort = 1;

        // TalonFX MotorController Ports: 50 ... 59
        public static final int tfxPort50 = 50;
        public static final int tfxPort51 = 51;
        public static final int tfxPort52 = 52;
        public static final int tfxPort53 = 53;
        public static final int tfxPort54 = 54;
        public static final int tfxPort55 = 55;
        public static final int tfxPort56 = 56;

        // Constants
        public static final double MAX_VOLTAGE = 12.2;
        public static final double MAX_AMPS = 50;
        public static final boolean SET_SAFETY_TRUE = true;

        // Constant Speeds
        public static final double INIT_CONVEYOR_SPEED = 0.200;
        public static final double INIT_INTAKE_SPEED = 0.200;
        public static final double INIT_INTAKELIFTER_SPEED = 0.125;
        public static final double INIT_KICKER_SPEED = 0.400;
        public static final double INIT_SHOOTER_SPEED = 0.400;

        // Initialize Controllers/Joysticks.
        public static final XboxController controllerOne = new XboxController(controllerOnePort);
        public static final XboxController controllerTwo = new XboxController(controllerTwoPort);

        // Initialize TalonFX Non-Swerve Motor Controllers
        public static final TalonFX tfxLeftShooterMotor = new TalonFX(tfxPort50); // Kraken X60 : Shooter
        public static final TalonFX tfxRightShooterMotor = new TalonFX(tfxPort51); // Kraken X60 : Shooter

        public static final TalonFX tfxIntakeMotor = new TalonFX(tfxPort52); // Kraken X44 : Intake
        public static final TalonFX tfxIntakeLifterMotor = new TalonFX(tfxPort53); // Kraken X60 : Intake Lifter

        public static final TalonFX tfxConveyorMotor = new TalonFX(tfxPort54); // Kraken X44 : Conveyor

        public static final TalonFX tfxLeftKickerMotor = new TalonFX(tfxPort55); // Kraken X44 : Kicker
        public static final TalonFX tfxRightKickerMotor = new TalonFX(tfxPort56); // Kraken X44 : Kicker
    }
}
