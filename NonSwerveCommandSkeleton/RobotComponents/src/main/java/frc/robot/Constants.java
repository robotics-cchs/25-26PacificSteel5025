package frc.robot;
// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

import edu.wpi.first.wpilibj.XboxController;

/** Add your docs here. */
public class Constants {
    public static class OperatorConstants {

        // Controller/Joystick Ports
        public static final int controllerOnePort = 0;
        public static final int controllerTwoPort = 1;

        // Swerve Encoder Ports: 00 ... 09
        
        // Misc Encoder Ports: 10 ... 19

        // Swerve Turn Ports: 20 ... 29
        
        // Swerve Drive Ports: 30 ... 39
        
        // Misc MC Ports: 40 ... 59
        public static final int MCPort40 = 40;
        public static final int MCPort41 = 41;
        public static final int MCPort42 = 42;
        public static final int MCPort43 = 43;
        public static final int MCPort44 = 44;
        public static final int MCPort45 = 45;
        public static final int MCPort46 = 46;
        public static final int MCPort47 = 47;
        public static final int MCPort48 = 48;
        public static final int MCPort49 = 49;
        public static final int MCPort50 = 50;
        public static final int MCPort51 = 51;
        public static final int MCPort52 = 52;
        public static final int MCPort53 = 53;
        public static final int MCPort54 = 54;
        public static final int MCPort55 = 55;
        public static final int MCPort56 = 56;
        public static final int MCPort57 = 57;
        public static final int MCPort58 = 58;
        public static final int MCPort59 = 59;
       
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

        // Initialize Controllers/Joysticks
        public static final XboxController controllerOne = new XboxController(controllerOnePort);
        public static final XboxController controllerTwo = new XboxController(controllerTwoPort);

        // Initialize Non-Swerve Encoders

        // Initialize Non-Swerve MotorControllers

        // Initialize Pneumatics

    }
}
