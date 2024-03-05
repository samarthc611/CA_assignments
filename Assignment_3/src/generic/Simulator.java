package generic;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import processor.Clock;
import processor.Processor;

public class Simulator {
		
	static Processor processor;
	static boolean simulationComplete;
	static int noOfInstructions;
	static int data;
	
	public static void setupSimulation(String assemblyProgramFile, Processor p)
	{
		Simulator.processor = p;
		loadProgram(assemblyProgramFile);
		
		simulationComplete = false;
	}
	
	static void loadProgram(String assemblyProgramFile)
	{
		/*
		 * TODO
		 * 1. load the program into memory according to the program layout described
		 *    in the ISA specification
		
		 * 2. set PC to the address of the first instruction in the main
		 * 3. set the following registers:
		 *     x0 = 0
		 *     x1 = 65535
		 *     x2 = 65535
		 */
		int address = 0;
		try {
			FileInputStream fileInputStream = new FileInputStream(assemblyProgramFile);
			DataInputStream dataInputStream = new DataInputStream(fileInputStream);

			
			int pc  = dataInputStream.readInt();
			processor.getRegisterFile().setProgramCounter(pc);
			
			while ((data = fileInputStream.read()) != -1) {
				processor.getMainMemory().setWord(address, dataInputStream.readInt());
				address = address + 1;
			}
			dataInputStream.close(); // Don't forget to close the stream
		} catch (IOException e) {
			// Handle the IOException, or simply print the stack trace for debugging
			e.printStackTrace();
		}
		processor.getRegisterFile().setValue(0, 0);
		processor.getRegisterFile().setValue(1, 65535);
		processor.getRegisterFile().setValue(2, 65535);

	}

	public static void setNoofInsts(int inst){
		noOfInstructions = inst;
	}
	public static int getNoofInsts(){
		return noOfInstructions;
	}
	
	public static void simulate()
	{
		
		int cycles = 0;

		
		while(simulationComplete == false)
		{
			processor.getIFUnit().performIF();
			processor.getOFUnit().performOF();
			processor.getEXUnit().performEX();
			processor.getMAUnit().performMA();
			processor.getRWUnit().performRW();
			Clock.incrementClock();
			cycles += 1;
		}
		Statistics.setNumberOfInstructions(noOfInstructions);
		Statistics.setNumberOfCycles(cycles);
	}
	
	public static void setSimulationComplete(boolean value)
	{
		simulationComplete = value;
	}
}

