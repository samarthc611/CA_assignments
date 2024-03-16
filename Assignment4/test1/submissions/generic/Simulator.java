package generic;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

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

			byte[] buffer = new byte[4]; // 32 bits = 4 bytes
			fileInputStream.read(buffer);
			int pc  = ByteBuffer.wrap(buffer).getInt();
			processor.getRegisterFile().setProgramCounter(pc);
			System.out.print("the pc is");
			System.out.println(pc);
			
			
			while (fileInputStream.read(buffer) != -1) {
				data = ByteBuffer.wrap(buffer).getInt();
				// System.out.print("data is ");
				// System.out.println(data);
				processor.getMainMemory().setWord(address, data);
				// System.out.println(processor.getMainMemory().getWord(address));
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
			// if(getNoofInsts() == 20)
			// 	setSimulationComplete(true);
		}
		System.out.println("Memory for fibo:");
		for(int j = 65524; j < 65536; j++)
		{
			System.out.println(processor.getMainMemory().getWord(j));

		}
		Statistics.setNumberOfInstructions(noOfInstructions);
		Statistics.setNumberOfCycles(cycles);
	}
	
	public static void setSimulationComplete(boolean value)
	{
		simulationComplete = value;
	}
}

