---
trigger: always_on
---

# 5025 FRC Software Rules

## Core Philosophy

You are an expert FRC software engineering assistant.

Generate code that matches the style and architecture of this robot codebase.

The project uses:

* WPILib command-based
* CTRE Phoenix 6 swerve
* PhotonVision
* AdvantageKit-style logging
* modular subsystem organization
* state-driven behavior

Primary priorities:

1. Competition reliability
2. Readability
3. Fast debugging
4. Minimal regression risk
5. Preserving architecture consistency
6. Student maintainability

Do not overengineer solutions.

The robot software should feel:

* clean
* modular
* predictable
* easy to debug during competition

---

# Critical Architecture Preservation Rules

Preserve the existing architecture style at all costs.

Generated code MUST:

* follow the existing project structure
* match naming conventions
* match formatting style
* preserve subsystem boundaries
* preserve state-machine patterns
* preserve utility structure
* preserve constants organization

Do NOT:

* rewrite subsystems unnecessarily
* introduce a new framework
* replace architecture patterns
* reorganize unrelated files
* convert the codebase to a different style

Prefer minimal targeted edits.

When modifying code:

* preserve existing APIs
* preserve tuning behavior
* preserve driver controls
* preserve autonomous behavior

All generated code should look like it was written by the existing team.

---

# Swerve Rules

The robot uses CTRE Phoenix 6 swerve.

DO NOT:

* rewrite drivetrain architecture
* replace CTRE swerve code
* replace generated CTRE structures
* invent custom swerve kinematics layers
* modify drivetrain internals unless explicitly requested

Assume the CTRE swerve implementation is correct and production-ready.

Only interact with the drivetrain through existing public APIs.

Generated code must integrate cleanly with the existing CTRE swerve structure.

Avoid unnecessary drivetrain modifications.

---

# Vision Rules

The robot uses:

* a single PhotonVision camera
* approximately 100ms total vision latency

Vision measurements must be treated carefully.

Always account for:

* high latency
* pose uncertainty
* single-camera instability
* temporary target loss

Do NOT blindly trust vision updates.

Prefer:

* filtered measurements
* conservative pose correction
* timestamp-aware vision integration
* sanity checks before accepting measurements

Vision should improve localization stability, not destabilize it.

Avoid aggressive vision correction behavior.

Never assume continuous target visibility.

---

# Command-Based Rules

Use command-based architecture correctly.

RobotContainer:

* controller bindings
* autonomous registration
* trigger configuration

Subsystems:

* own hardware
* expose clean APIs
* manage mechanism state

Commands:

* express robot actions
* remain reusable
* avoid hardware-specific implementation details

Prefer:

* reusable commands
* small focused commands
* command factories
* explicit sequencing

Avoid:

* giant commands
* deeply nested command logic
* duplicated behavior

---

# State Machine Rules

Prefer explicit subsystem states.

Examples:

* IDLE
* INTAKING
* HOLDING
* SCORING
* ZEROING

Avoid:

* hidden booleans
* implicit behavior
* scattered state logic

Subsystem behavior should always be:

* predictable
* inspectable
* easy to log

---

# Logging Rules

Use structured logging compatible with AdvantageKit-style workflows.

Log:

* subsystem states
* setpoints
* vision estimates
* important sensor values
* autonomous progression
* command transitions
* faults and warnings

Avoid:

* excessive console spam
* redundant telemetry
* meaningless logs

Logs should help diagnose:

* autonomous failures
* latency issues
* vision instability
* tuning problems
* CAN issues
* mechanism failures

---

# Constants Rules

All constants must remain centralized.

Use organized constants structures:

* DriveConstants
* VisionConstants
* ElevatorConstants

Avoid magic numbers.

Document units clearly.

Group constants logically:

* CAN IDs
* PID gains
* geometry
* motion constraints
* current limits
* tolerances

---

# Performance Rules

Robot timing matters.

Avoid:

* Thread.sleep
* blocking operations
* unnecessary allocations in periodic
* excessive object creation
* expensive computations in loops

Consider:

* scheduler timing
* CAN utilization
* garbage collection pressure
* vision processing delays

Generated code should remain lightweight and deterministic.

---

# Autonomous Rules

Autonomous behavior must:

* remain deterministic
* fail safely
* stop safely
* tolerate interruptions

Prefer:

* PathPlanner
* event markers
* reusable autonomous commands
* named commands

Avoid:

* fragile timing logic
* hardcoded delays
* duplicated autonomous sequences

Autonomous code should be easy to tune quickly during competition.

---

# Modification Safety Rules

Before major changes:

* summarize intended modifications
* explain affected systems
* explain architectural impact
* identify possible risks

Avoid touching unrelated systems.

Preserve:

* tuning values
* subsystem APIs
* logging infrastructure
* autonomous behavior
* driver controls

Prefer incremental improvements over rewrites.

---

# Code Generation Rules

Generated code MUST:

* compile cleanly
* match existing style
* preserve architecture consistency
* minimize unnecessary rewrites
* integrate naturally with the current codebase

Generated code should appear indistinguishable from hand-written team code.

Always:

* explain why a change is needed
* explain expected behavior changes
* explain risks if relevant

Never:

* hallucinate APIs
* invent WPILib functionality
* silently remove behavior
* introduce unrelated refactors

---

# Student Maintainability Rules

Code should remain understandable during stressful competition conditions.

Prefer:

* clear naming
* explicit logic
* readable abstractions
* predictable behavior

Avoid:

* excessive abstraction
* giant utility classes
* overengineered frameworks
* unnecessarily generic systems

The robot should be debuggable quickly in the pit.

---

# Preferred Stack

Strongly prefer:

* Java
* WPILib command-based
* CTRE Phoenix 6
* PhotonVision
* PathPlanner
* AdvantageKit-style logging

Use modern WPILib patterns and avoid deprecated APIs.