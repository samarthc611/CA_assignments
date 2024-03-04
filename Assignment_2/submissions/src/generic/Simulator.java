package generic;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.lang.Math;

import generic.Operand.OperandType;


public class Simulator {

	public static HashMap<String,String> getType = new HashMap<String, String>(){{
		put("add","r3");
		put("sub","r3");
		put("mul","r3");
		put("div","r3");
		put("and","r3");
		put("or","r3");
		put("xor","r3");
		put("slt","r3");
		put("sll","r3");
		put("srl","r3");
		put("sra","r3");
		put("addi","r2i");
		put("subi","r2i");
		put("muli","r2i");
		put("divi","r2i");
		put("andi","r2i");
		put("ori","r2i");
		put("xori","r2i");
		put("slti","r2i");
		put("slli","r2i");
		put("srli","r2i");
		put("srai","r2i");
		put("load","r2i_ldst");
		put("store","r2i_ldst");
		put("beq","r2i_b");
		put("bgt","r2i_b");
		put("bne","r2i_b");
		put("blt","r2i_b");
		put("jmp","ri");
		put("end","end");
	}};

	public static HashMap<String,String> opTable = new HashMap<String, String>(){{
		put("add", "00000");
		put("sub", "00010");
		put("mul", "00100");
		put("div", "00110");
		put("and", "01000");
		put("or", "01010");
		put("xor", "01100");
		put("slt", "01110");
		put("sll", "10000");
		put("srl", "10010");
		put("sra", "10100");
		put("addi", "00001");
		put("subi", "00011");
		put("muli", "00101");
		put("divi", "00111");
		put("andi", "01001");
		put("ori", "01011");
		put("xori", "01101");
		put("slti", "01111");
		put("slli", "10001");
		put("srli", "10011");
		put("srai", "10101");
		put("load", "10110");
		put("store", "10111");
		put("beq", "11001");
		put("bne", "11010");
		put("blt", "11011");
		put("bgt", "11100");
		put("jmp", "11000");
		put("end","11101");

	}};

	// public static int binaryStringToNumber(String binaryString) {
    //     int result = 0;
    //     for (int i = 0; i < binaryString.length(); i++) {
    //         char digit = binaryString.charAt(i);
    //         if (digit == '1') {
    //             result = (result << 1) | 1;
    //         } else if (digit == '0') {
    //             result = (result << 1);
    //         } else {
    //             throw new IllegalArgumentException("Invalid binary digit: " + digit);
    //         }
    //     }
    //     return result;
    // }
	static FileInputStream inputcodeStream = null;
	
	public static void setupSimulation(String assemblyProgramFile)
	{	
		int firstCodeAddress = ParsedProgram.parseDataSection(assemblyProgramFile);
		ParsedProgram.parseCodeSection(assemblyProgramFile, firstCodeAddress);
		ParsedProgram.printState();
	}
	
	public static void assemble(String objectProgramFile)
	{
		//TODO your assembler code
		//1. open the objectProgramFile in binary mode
		//FileOutputStream outputStream = new FileOutputStream(objectProgramFile);
			
            //BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
		
        
		try(DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(objectProgramFile));)
		{
			dataOutputStream.writeInt(ParsedProgram.symtab.get("main"));
			int i=0;
			// System.out.println(Integer.toBinaryString(-1));
			// System.out.println(toBinary(-1, 5));
			while(i < ParsedProgram.data.size())
			{
				dataOutputStream.writeInt(ParsedProgram.data.get(i));
				i++;
			}
			i = ParsedProgram.symtab.get("main");
			while(true)
			{
				// dataOutputStream.writeInt(opTable.get(ParsedProgram.code.get(0).getOperationType()));
				// System.out.println("Hey there!");
				// String temp = "";
				String temp = opTable.get(ParsedProgram.getInstructionAt(i).getOperationType().toString());
				
				if(getType.get(ParsedProgram.getInstructionAt(i).getOperationType().toString())=="r3")
				{
					String rs1 = Integer.toBinaryString(ParsedProgram.getInstructionAt(i).getSourceOperand1().value);
					rs1 = String.format("%05d",Integer.parseInt(rs1));
					temp += rs1;

					String rs2 = Integer.toBinaryString(ParsedProgram.getInstructionAt(i).getSourceOperand2().value);
					rs2 = String.format("%05d",Integer.parseInt(rs2));
					temp += rs2;

					String rd = Integer.toBinaryString(ParsedProgram.getInstructionAt(i).getDestinationOperand().value);
					rd = String.format("%05d",Integer.parseInt(rd));
					temp += rd;
					temp += "000000000000";
				}
				else if(getType.get(ParsedProgram.getInstructionAt(i).getOperationType().toString())=="r2i")
				{
					String rs1 = Integer.toBinaryString(ParsedProgram.getInstructionAt(i).getSourceOperand1().value);
					rs1 = String.format("%05d",Integer.parseInt(rs1));
					temp += rs1;

					String rd = Integer.toBinaryString(ParsedProgram.getInstructionAt(i).getDestinationOperand().value);
					rd = String.format("%05d",Integer.parseInt(rd));
					temp += rd;

					String imm = Integer.toBinaryString(ParsedProgram.getInstructionAt(i).getSourceOperand2().value);
					imm = String.format("%017d", Integer.parseInt(imm));
					temp += imm;
				}
				else if(getType.get(ParsedProgram.getInstructionAt(i).getOperationType().toString())=="r2i_ldst")
				{
					String rs1 = Integer.toBinaryString(ParsedProgram.getInstructionAt(i).getSourceOperand1().value);
					rs1 = String.format("%05d",Integer.parseInt(rs1));
					temp += rs1;

					String rd = Integer.toBinaryString(ParsedProgram.getInstructionAt(i).getDestinationOperand().value);
					rd = String.format("%05d",Integer.parseInt(rd));
					temp += rd;

					String imm = Integer.toBinaryString(ParsedProgram.symtab.get(ParsedProgram.getInstructionAt(i).getSourceOperand2().labelValue));
					// String imm = ParsedProgram.symtab.get(ParsedProgram.getInstructionAt(i).getSourceOperand2().labelValue).toString();
					imm = String.format("%017d", Integer.parseInt(imm));
					temp += imm;
				}
				else if(getType.get(ParsedProgram.getInstructionAt(i).getOperationType().toString())=="r2i_b")
				{
					String rs1 = Integer.toBinaryString(ParsedProgram.getInstructionAt(i).getSourceOperand1().value);
					rs1 = String.format("%05d",Integer.parseInt(rs1));
					// rs1 = rs1.substring(27, 31);
					temp += rs1;

					String rd = Integer.toBinaryString(ParsedProgram.getInstructionAt(i).getSourceOperand2().value);
					rd = String.format("%05d",Integer.parseInt(rd));
					// rd = rd.substring(27, 31);
					temp += rd;

					String imm;
					// String imm = Integer.toBinaryString();
					// String imm = ParsedProgram.symtab.get(ParsedProgram.getInstructionAt(i).getSourceOperand2().labelValue).toString();
					int wannabe_negative = ParsedProgram.symtab.get(ParsedProgram.getInstructionAt(i).getDestinationOperand().labelValue) - ParsedProgram.getInstructionAt(i).getProgramCounter();
					if(wannabe_negative < 0)
					{
						// System.out.println("helo");
						wannabe_negative = -wannabe_negative;
						String complementer = Integer.toBinaryString(wannabe_negative);
						complementer=String.format("%017d", Integer.parseInt(complementer));
						char[] comp_arr = complementer.toCharArray();
						for(int x = 0; x < complementer.length(); x++)
						{
							if(comp_arr[x] == '1')
							{
								comp_arr[x]= '0';
							}
							else
								comp_arr[x] = '1';
						}
						int carry=1;
						for(int x=complementer.length()-1;x>=0;x--){
							if(comp_arr[x]=='1' && carry==1){
								comp_arr[x]='0';
							}
							else if(comp_arr[x]=='0' && carry==1){
								comp_arr[x]='1';
								break;
							}
						}
						// complementer = comp_arr.toString();
						complementer = String.valueOf(comp_arr);
						imm=complementer;
					}
					else{
						imm = String.format("%017d", Integer.parseInt(Integer.toBinaryString(wannabe_negative)));
					}
					temp+=imm;
					// imm = imm.substring(15, 31);
					
				}
				else if(getType.get(ParsedProgram.getInstructionAt(i).getOperationType().toString())=="ri")
				{
					temp+="00000";
					String imm;
					int wannabe_negative = ParsedProgram.symtab.get(ParsedProgram.getInstructionAt(i).getDestinationOperand().labelValue) - ParsedProgram.getInstructionAt(i).getProgramCounter();
					if(wannabe_negative < 0)
					{
						// System.out.println("helo");
						wannabe_negative = -wannabe_negative;
						String complementer = Integer.toBinaryString(wannabe_negative);
						complementer=String.format("%022d", Integer.parseInt(complementer));
						char[] comp_arr = complementer.toCharArray();
						for(int x = 0; x < complementer.length(); x++)
						{
							if(comp_arr[x] == '1')
							{
								comp_arr[x]= '0';
							}
							else
							comp_arr[x]= '1';
						}
						int carry=1;
						for(int x=complementer.length()-1;x>=0;x--){
							if(comp_arr[x]=='1' && carry==1){
								comp_arr[x]='0';
							}
							else if(comp_arr[x]=='0' && carry==1){
								comp_arr[x]='1';
								break;
							}
						}
						// complementer = comp_arr.toString();
						complementer = String.valueOf(comp_arr);
						// int two_complement = (int)Long.parseLong(complementer);
						// two_complement++;
						// complementer = Integer.toBinaryString(two_complement);
						// double a = wannabe_negative;
						// double lna = Math.log(wannabe_negative);
						// double ln_base = Math.log(2);
						// double pow = lna /ln_base;
						// int power = (int)pow;
						// power++;
						// double complementer = Math.pow(2, power);
						// int power_of_two = (int)complementer;
						
						// complementer = String.format("", null)
						imm = complementer;
					}
					else
						imm = String.format("%022d", Integer.parseInt(Integer.toBinaryString(wannabe_negative)));
					// String rd = Integer.toBinaryString(ParsedProgram.symtab.get(ParsedProgram.getInstructionAt(i).getSourceOperand2().labelValue) - ParsedProgram.getInstructionAt(i).getProgramCounter());
					// rd = String.format("%05d",Integer.parseInt(rd));
					// temp += rd;

					// String rs1 = Integer.toBinaryString(ParsedProgram.getInstructionAt(i).getSourceOperand1().value);
					// rs1 = String.format("%05d",rs1);
					// temp += rs1;

					// imm = Integer.toBinaryString(ParsedProgram.getInstructionAt(i).getDestinationOperand().value);
					// imm = Integer.toBinaryString(wannabe_negative);
					// imm = String.format("%022d", Integer.parseInt(imm));
					// imm = imm.substring(10, 31);
					temp+= imm;
				}
				else
				{
					String s = "0";
					s = String.format("%027d", Integer.parseInt(s));
					temp += s;
				}
				// temp += Integer.toBinaryString(ParsedProgram.getInstructionAt(i).getSourceOperand1().value);
				// System.out.println(Integer.toBinaryString(Integer.parseInt(opTable.get(ParsedProgram.getInstructionAt(i).getOperationType().toString()),2)));
				System.out.println(temp);
				// int num = binaryStringToNumber(temp);
				int val = (int)Long.parseLong(temp,2);
				byte[] binaryCode = ByteBuffer.allocate(4).putInt(val).array();
				dataOutputStream.write(binaryCode);
				// if(opTable.get(ParsedProgram.getInstructionAt(i).getOperationType().toString())=="11101")
				// 	break;
				if(i == ParsedProgram.code.size() + ParsedProgram.symtab.get("main") - 1)
					break;
				i++;
			}
			// System.out.println("Hello");
			// System.out.println(ParsedProgram.code.get(0).getOperationType());
			
			dataOutputStream.close();
		}
		catch (IOException e) {
            // Handle IO exception
            e.printStackTrace();
        }


		//2. write the firstCodeAddress to the file
		//3. write the data to the file
		//4. assemble one instruction at a time, and write to the file
		//5. close the file
		
	}
	public static void main(String[] args) {
		// File myfile = new File("temp.txt");
		// myfile.createNewFile();
		System.out.println(ParsedProgram.symtab.get(".text"));
	}
	
}
