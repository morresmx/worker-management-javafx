# Worker and Shift Management Program on Java

## Features
  - Create Shifts: Define shifts with specific start and end times.
  - Add Workers: Add both main and regular workers with unique IDs.
  - Schedule Shifts: Assign workers to specific shifts.
  - View Assignments: Display a table of all workers, shifts, and assignments.

## Usage

  - Launch the application: After running the project, the main window will appear.
  - Create a Shift: Click on "Create Shift" and fill in the details.
  - Add Workers: Click on "Add Main Worker" or "Add Regular Worker" and provide worker details.
  - Schedule a Shift: Click on "Schedule Shift", select a shift, and assign workers.
  - View Assignments: See the table of current assignments including workers and their shifts.

## Project Structure

  - `src/main/java/org/example/gymbeam4/Main.java` : Main class to launch the application.
  - `src/main/java/org/example/gymbeam4/WorkerManager.java` : Manages workers and shifts.
  - `src/main/java/org/example/gymbeam4/Worker.java` : Represents a worker.
  - `src/main/java/org/example/gymbeam4/Shift.java` : Represents a shift.
  - `src/main/java/org/example/gymbeam4/WorkerFactory.java` : Factory pattern to create workers.

## Dependencies

  - JavaFX: For building the user interface.
  - Maven: For managing project dependencies and building the project.
