.PHONY: clean

Release/SudokuSolver.jar: SudokuSolver.java
	javac SudokuSolver.java
	jar cfe Release/SudokuSolver.jar SudokuSolver *.class

clean:
	rm *.class
