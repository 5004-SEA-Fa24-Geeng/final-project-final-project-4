[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/IE0ITl4j)

# Final Project for CS 5004 - (Car Rental System)

Car Rental System - MVC Structured Java Application

## Contents
- [Team Members](#team-members)
- [Brief Description](#brief-description)
- [Project Structure](#project-structure)
- [Links to Documentation](#links-to-documentation)
- [Requirements](#requirements)
- [Compile and Run](#compile-and-run)


## Team Members

- [Lucy(Minglu) Sun](https://github.com/ooodddee)
- [Echo(Huanhuan) Yan](https://github.com/echoyanxx)
- [Chichi Zhang](https://github.com/chichizhang0510)



## Brief Description

This project implements a command-line car rental system using Java, designed with a clean Model-View-Controller (MVC) architecture. 

It enables users to interact with the system through a text-based menu, supporting operations such as browsing available vehicles, booking cars, viewing bookings, and managing user data. Additional features like search, filtering, and CSV exports improve usability and simulate real-world rental service operations.

The system uses structured CSV files for data persistence and offers a menu-driven, interactive experience.

This system addresses real-world needs for accessible and flexible rental options, particularly in areas with limited public transportation, providing a user-friendly rental experience via simple yet effective backend services.





## Project Structure

```
final-project-final-project-4/
├── src/
│   ├── main/
│   │   ├── java/               # All Java source code (MVC structure under student/)
│   │   └── resources/          # CSV data files (e.g., cars.csv, users.csv)
│   └── test/                   # (Optional) Unit test files
│
├── Manual/                     # User manual & screenshots
│   ├── UserManual.md           # Markdown version of the user manual
│   └── images/                 # All GUI & CLI screenshots used in the manual
│
├── DesignDocuments/           
│   ├── JavaDoc/                # Generated JavaDoc HTML files
│   ├── FinalDesignDocument.md  # Class diagram, architecture & detailed design
│   ├── Proposal.md             # Initial project proposal
│   └── README.md               # Description of design documents
│
├── Instructions/               # (Optional) Instructions or deliverable notes
├── data/                       # (Optional) Sample CSV data (if not under resources)
├── build/                      # Compiled bytecode (automatically generated)
├── .idea/ .gradle/             # IDE and Gradle configuration
├── gradle/                     # Gradle build scripts
├── README.md                   # 🌟 Project overview (you are here)
```





## Links to Documentation

| Document                                                     | Description                                                  |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| [Project Proposal](./DesignDocuments/Proposal.md)            | Initial project plan, problem description and team collaboration |
| [Final Design Document](./DesignDocuments/FinalDesignDocument.md) | Detailed architecture, UML, and design explanations          |
| [User Manual](./Manual/UserManual.md)                        | Step-by-step guide on how to use the system (with screenshots) |
| [Generated JavaDoc](./DesignDocuments/JavaDoc/index.html)    | Full API documentation generated from Java source code       |





## Requirements

| Requirement    | Description                  |
| -------------- | ---------------------------- |
| Java Version   | Java 17+                     |
| JDK            | Required for compilation     |
| IDE (Optional) | IntelliJ IDEA or Eclipse     |
| Data Files     | `cars.csv`, `users.csv`      |
| CSV Access     | Application reads/writes CSV |





## Compile and Run

You can run the Car Rental System using either the **command line** or an **IDE** (such as IntelliJ IDEA or Eclipse). Both options are explained below.



### Using Command Line

Make sure you have **Java 17 or later** installed and configured in your system PATH.

Compile the project using your IDE or via terminal:

```bash
javac -d out src/main/java/student/Main.java
```

Run the program:

```bash
java -cp out student.Main
```

When prompted:

```
Launch in CLI or GUI mode? (cli/gui):
```

Type `cli` to run in command-line mode, or `gui` to launch the Swing GUI.



### Using an IDE (IntelliJ / Eclipse)

#### IntelliJ IDEA

1. Open the project via `File > Open` and select the project root.
2. Make sure the SDK is set to Java 17+ in `File > Project Structure > SDKs`.
3. Navigate to `src/main/java/student/Main.java`.
4. Right-click on `Main.java` and select `Run 'Main.main()'`.
5. Enter `cli` or `gui` in the console when prompted.

#### Eclipse

1. Import the project: `File > Import > Existing Projects into Workspace`.
2. Open `Main.java`, then choose `Run As > Java Application`.
3. Choose CLI or GUI when prompted.





# Thank You

For more details, see the [User Manual](./Manual/UserManual.md) . Enjoy using the Car Rental System!
