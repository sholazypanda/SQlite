import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * @author Shobhika Panda
 * @version 1.0 <b>This is an example of how to read/write binary data files
 *          using RandomAccessFile class</b>
 *
 */
public class DavisBaseLite {
	// This can be changed to whatever you like
	static String prompt = "davisql>";

	/*
	 * This example does not dynamically load a table schema to be able to
	 * read/write any table -- there is exactly ONE hardcoded table schema.
	 * These are the variables associated with that hardecoded table schema.
	 * Your database engine will need to define these on-the-fly from whatever
	 * table schema you use from your information_schema
	 */

	static RandomAccessFile schemataTableFile;
	static RandomAccessFile entryInfoschemataTableFile;
	static int id;
	static String name;
	static short quantity;
	static float probability;
	static String schemaName;
	static String defaultSchemaName = "information_schema";
	static RandomAccessFile columnsfieldtableIdIndex;
	static RandomAccessFile columnstypetableIdIndex;
	static RandomAccessFile columnsnulltableIdIndex;
	static RandomAccessFile columnskeytableIdIndex;
	static HashMap<String,String> globalUserMap=new HashMap<>();

	public static void main(String[] args) throws IOException, ParseException {
		/* Display the welcome splash screen */
		splashScreen();

		Scanner scanner = new Scanner(System.in).useDelimiter(";");
		String userCommand; // Variable to collect user input from the prompt
		
		 File dirStructureFile = new
		 File("Schema/Tables/Columns");
		 
		 boolean schema = dirStructureFile.mkdirs(); if (schema) {
		 System.out.println("Directories created"); } else {
		 System.out.println("Directories already exist"); }
		 
		InitClass.makeSchemas();
		
		do { // do-while !exit
			System.out.print(prompt);
			userCommand = scanner.next().trim();
			String firstWord = "";
			String secondWord = "";

			if (userCommand.contains(" ")) {
				firstWord = userCommand.substring(0, userCommand.indexOf(" "));
				secondWord = userCommand.split("\\s+")[1];
			}
			//System.out.println(secondWord);
			HashMap<String, String> hm = new HashMap<>();

			// List<String> remainingList= new ArrayList<>();
			if (secondWord.toLowerCase().equals("table".toLowerCase()) && (firstWord.contains("create") || firstWord.contains("CREATE"))) {

				//System.out.println("Hello");
				String remainingList;
				int indexOfLastBracket = userCommand.lastIndexOf(")");

				String newString = userCommand.replaceFirst("\\(", ",");

				remainingList = newString.substring(userCommand.indexOf(" "), indexOfLastBracket);
				// System.out.println("newString"+newString.trim());
				hm.put(firstWord, remainingList);
			} else if (firstWord.contains("insert") || firstWord.contains("INSERT")) {
				// String myReplacedString = userCommand.replaceAll(/\(\s\)/g,
				// "");
				String resultString = userCommand.replaceAll("\\s+(?=[^()]*\\))", "");
				String replaceBrackets = resultString.replace("(", "").replace(")", "");
				String replaceApostrophe = replaceBrackets.replaceAll("'", "");
				//System.out.println("replaceApostrophe"+replaceApostrophe);
				hm.put(firstWord, replaceApostrophe);
			} else if(firstWord.contains("select") || firstWord.contains("SELECT") ) {
				String replaceApostrophe = userCommand.replaceAll("'", "");
				hm.put(firstWord, replaceApostrophe);
			}
			/*
			 * This switch handles a very small list of commands of known
			 * syntax. You will probably want to write a parse(userCommand)
			 * method to to interpret more complex commands.
			 */
			switch (firstWord.toUpperCase()) {
			case "USE":
				schemaName = userCommand.substring(userCommand.indexOf(" ")).trim();
				
				//System.out.println(schemaName);
				break;
			case "display all":
				// displayAllRecords();
				break;
			case "CREATE":
				//System.out.println("Hello" + secondWord);
				switch (secondWord.toUpperCase()) {

				case "SCHEMA":
					createSchema(userCommand);
					break;
				case "TABLE":
					createTable(hm, schemaName);

				}
				break;
			case "INSERT":
				insertTable(hm, schemaName);
				break;
			case "SHOW":
				//System.out.println("Hello" + secondWord);
				switch (secondWord.toUpperCase()) {

				case "SCHEMAS":
					showSchemas(userCommand,schemaName);
					break;
				case "TABLES":
					showTables(userCommand,schemaName);

				}
				break;
			case "SELECT":
				selectTable(hm, schemaName);
				break;
			case "DROP":
				dropTable(userCommand,schemaName);
				break;
			case "help":
				help();
				break;
			case "version":
				version();
				break;
			default:
				System.out.println("I didn't understand the command: \"" + userCommand + "\"");
			}
		} while (!userCommand.equals("exit"));
		System.out.println("Exiting...");

	} /* End main() method */
	
	
	private static void dropTable(String userCommand, String schemaName) throws IOException {
		// TODO Auto-generated method stub
		String arrString[]=userCommand.split(" ");
		if(schemaName==null){
			schemaName="information_schema";
		}
		//System.out.println(arrString[2]);
		//RandomAccessFile schemataTableFileinfo = new RandomAccessFile(
			//	CONSTANTS.BASEDIR + arrString[2]+ ".schemata.tbl", "rw");
		
		//RandomAccessFile schemataTableFile = new RandomAccessFile(
				//CONSTANTS.TABLESPATH + schemaName + "." + arrString[2] + ".table_name.tbl", "rw");
		File f = new File(CONSTANTS.TABLESPATH + schemaName + "." + arrString[2] + ".table_name.tbl");
		f.delete();
		File f2=new File(CONSTANTS.BASEDIR + arrString[2]+ ".schemata.tbl");
		f2.delete();
		File folder = new File(CONSTANTS.COLUMNSPATH);
		File[] listOfFiles = folder.listFiles();

		    for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile()) {
		    	//  System.out.println(listOfFiles[i].getName());
		    	  String splitIt[]=listOfFiles[i].getName().split("\\.");
		    	//System.out.println(splitIt[1]);
		    	  if(splitIt.length > 2 && splitIt[1].toLowerCase().equals(arrString[2].toLowerCase())){
		    		  //System.out.println("Hey");
		    		  File f3=new File(CONSTANTS.COLUMNSPATH+listOfFiles[i].getName());
		    		  f3.delete();
		    	  }
		        //System.out.println("File " + listOfFiles[i].getName());
		      } 
		    }
		    String sourcePathcolumns=CONSTANTS.COLUMNSPATH;		
			RandomAccessFile infoColumns=new RandomAccessFile(sourcePathcolumns+"information_schema.columns.tbl","rw");  
			dropUtil(infoColumns,arrString[2]);
			RandomAccessFile schemataTableFileinfo = new RandomAccessFile(
					CONSTANTS.TABLESPATH+"information_schema.table.tbl","rw");
			dropUtil2(schemataTableFileinfo,arrString[2]);
			System.out.println("Drop Table Successful");
			//dropUtil(schemataTableFileinfo,arrString[2]);

		/*
		String sourcePathschema=CONSTANTS.BASEDIR;
		RandomAccessFile infoSchema=new RandomAccessFile(sourcePathschema+"information_schema.schemata.tbl","rw");
		deleteRecords(infoSchema,sourcePathschema,arrString[2]);
		String sourcePathtables=CONSTANTS.TABLESPATH;
		RandomAccessFile infoTable=new RandomAccessFile(sourcePathtables+"information_schema.table.tbl","rw");
		deleteRecords(infoTable,sourcePathtables,arrString[2]);
		String sourcePathcolumns=CONSTANTS.COLUMNSPATH;		
		RandomAccessFile infoColumns=new RandomAccessFile(sourcePathtables+"information_schema.columns.tbl","rw");
		deleteRecords(infoColumns,sourcePathcolumns,arrString[2]);*/
	}
	private static void dropUtil(RandomAccessFile inputFile,String tableName) throws IOException{
		
			inputFile.seek(InitClass.columnsLength);
		int count = 0;
		String line = null;
		while ((line = inputFile.readLine()) != null) {
			//System.out.println("line is :" + line);
			count++;
		}
		//System.out.println(count);
		inputFile.seek(InitClass.columnsLength);
		int index = 0;
		HashMap<String,List<String>> hm=new HashMap<>();
		List<String> list=new ArrayList<>();
		int leftPad = 0;
		while (index < count) {
			byte varcharLength = inputFile.readByte();
			String schema = "";
			String table = "";
			String field="";
			String type="";
			String nullvalues="";
			String key="";
			int in=0;
			for (int i = 0; i < varcharLength; i++)
				// System.out.print((char)readdataTableFile.readByte());
			schema += (char) inputFile.readByte();
			varcharLength = inputFile.readByte();
			for (int i = 0; i < varcharLength; i++)
				// System.out.print((char)readdataTableFile.readByte());
			table += (char) inputFile.readByte();
			//System.out.println(table);
			varcharLength = inputFile.readByte();
			//System.out.println(schema);
			
			for (int i = 0; i < varcharLength; i++)
				// System.out.print((char)readdataTableFile.readByte());
			type += (char) inputFile.readByte();
			list.add(type);
			//field.out.println(table);
			varcharLength = inputFile.readByte();
			in=inputFile.readInt();
			list.add(String.valueOf(in));
			for (int i = 0; i < varcharLength; i++)
				// System.out.print((char)readdataTableFile.readByte());
			nullvalues += (char) inputFile.readByte();
			//field.out.println(table);
			list.add(nullvalues);
			varcharLength = inputFile.readByte();
			for (int i = 0; i < varcharLength; i++)
				// System.out.print((char)readdataTableFile.readByte());
			key += (char) inputFile.readByte();
			list.add(key);
			//field.out.println(table);
			varcharLength = inputFile.readByte();	
			
			hm.put(schema+","+table, list);
			
			
			//hm.put(field.trim(), type.trim());
			index++;
			inputFile.readLine();
			
		}
	
			inputFile.setLength(InitClass.columnsLength);
		
		for(String str:hm.keySet()){
			String arrS[]=str.split(",");
			if(arrS[1].toLowerCase().equals(tableName.toLowerCase())){
				hm.remove(str);
			}
		}
		for(String str:hm.keySet()){
			for(int i=0;i<list.size();i++){
				inputFile.writeByte(list.get(i).length());
				inputFile.writeBytes(list.get(i));
			}
		}
	}
	private static void dropUtil2(RandomAccessFile inputFile,String tableName) throws IOException{
		inputFile.seek(InitClass.tablesLength);
		int count = 0;
		String line = null;
		while ((line = inputFile.readLine()) != null) {
			//System.out.println("line is :" + line);
			count++;
		}
		//System.out.println(count);
		inputFile.seek(InitClass.tablesLength);
		int index = 0;
		HashMap<String,List<String>> hm=new HashMap<>();
		List<String> list=new ArrayList<>();
		int leftPad = 0;
		while (index < count) {
			byte varcharLength = inputFile.readByte();
			String schema = "";
			String table = "";
			String field="";
			String type="";
			String nullvalues="";
			String key="";
			long in=0;
			for (int i = 0; i < varcharLength; i++)
				// System.out.print((char)readdataTableFile.readByte());
			schema += (char) inputFile.readByte();
			varcharLength = inputFile.readByte();
			list.add(schema);
			for (int i = 0; i < varcharLength; i++)
				// System.out.print((char)readdataTableFile.readByte());
			table += (char) inputFile.readByte();
			//System.out.println(table);
			varcharLength = inputFile.readByte();
			//System.out.println(schema);
			list.add(table);
			
			in=inputFile.readLong();
			list.add(String.valueOf(in));	
			
			hm.put(schema+","+table, list);
			
			
			//hm.put(field.trim(), type.trim());
			index++;
			inputFile.readLine();
			
		}
	
			inputFile.setLength(InitClass.tablesLength);
		
		for(String str:hm.keySet()){
			String arrS[]=str.split(",");
			if(arrS[1].toLowerCase().equals(tableName.toLowerCase())){
				hm.remove(str);
			}
		}
		for(String str:hm.keySet()){
			for(int i=0;i<list.size();i++){
				inputFile.writeByte(list.get(i).length());
				inputFile.writeBytes(list.get(i));
			}
		}
	}
	
/*
	private static void deleteRecords(RandomAccessFile inputFile,String sourcePath,String lineToRemove) throws IOException{
		//File inputFile = new File("myFile.txt");
		File tempFile = new File(sourcePath+"myTempFile.txt");

		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

		
		String currentLine;

		while((currentLine = reader.readLine()) != null) {
		    // trim newline when comparing with lineToRemove
		    String trimmedLine = currentLine.trim();
		    if(trimmedLine.equals(lineToRemove)) continue;
		    writer.write(currentLine + System.getProperty("line.separator"));
		}
		writer.close(); 
		reader.close(); 
		boolean successful = tempFile.renameTo(inputFile);
	}*/
	private static void showSchemas(String userCommand,String schemaName) throws IOException {
		// TODO Auto-generated method stub
		//System.out.println("schemmooo");
		RandomAccessFile schemataTableFileinfo = new RandomAccessFile(
				CONSTANTS.BASEDIR + "information_schema.schemata.tbl", "rw");
		//schemataTableFileinfo.seek(0);
		int count = 0;
		String line = null;
		while ((line = schemataTableFileinfo.readLine()) != null) {
			count++;
		}
		//System.out.println(count);
		schemataTableFileinfo.seek(0);
		int index = 0;
		List<String> list=new ArrayList<>();
		
		int leftPad = 0;
		System.out.println("**********");
		System.out.println("SCHEMA NAMES");
		System.out.println("**********");
		
		while (index < count) {
			byte varcharLength = schemataTableFileinfo.readByte();
			String field = "";
			String type = "";
			
			for (int i = 0; i < varcharLength; i++)
				// System.out.print((char)readdataTableFile.readByte());
			field += (char) schemataTableFileinfo.readByte();
			varcharLength = schemataTableFileinfo.readByte();
			
			System.out.println(field);
			list.add(field);
				
			
				
			
			//hm.put(field.trim(), type.trim());
			index++;
			schemataTableFileinfo.readLine();

		}
		System.out.println("**********");
		
	}
	private static void showTables(String userCommand,String schemaName) throws IOException {
		// TODO Auto-generated method stub
		//System.out.println("SHOW TABLES");
		if(schemaName==null){
			schemaName="information_schema";
		}
		RandomAccessFile tablesTableFile = new RandomAccessFile(CONSTANTS.TABLESPATH + "information_schema.table.tbl", "rw");
		//schemataTableFileinfo.seek(0);
		int count = 0;
		String line = null;
		while ((line = tablesTableFile.readLine()) != null) {
			count++;
		}
		//System.out.println(count);
		tablesTableFile.seek(0);
		int index = 0;
		List<String> list=new ArrayList<>();
		System.out.println("**********");
		System.out.println("TABLE NAMES");
		System.out.println("**********");
		int leftPad = 0;
		while (index < count) {
			byte varcharLength = tablesTableFile.readByte();
			String schema = "";
			String table = "";
			
			for (int i = 0; i < varcharLength; i++)
				// System.out.print((char)readdataTableFile.readByte());
			schema += (char) tablesTableFile.readByte();
			varcharLength = tablesTableFile.readByte();
			for (int i = 0; i < varcharLength; i++)
				// System.out.print((char)readdataTableFile.readByte());
			table += (char) tablesTableFile.readByte();
			//System.out.println(table);
			varcharLength = tablesTableFile.readByte();
			//System.out.println(schema);
			System.out.println(table);
			if(schemaName.equals(schema))
			list.add(table);
				
			
				
			
			//hm.put(field.trim(), type.trim());
			index++;
			tablesTableFile.readLine();

		}
		System.out.println("**********");
	}
	

	public static void createSchema(String userCommand) {
		String arr[] = userCommand.split(" ");
		//System.out.println("am here" + arr[2]);
		try {

			File schemaTfile = new File(CONSTANTS.BASEDIR + arr[2] + ".schemata.tbl");
			File schemaInfo = new File(CONSTANTS.BASEDIR + "information_schema.schemata.tbl");
			if (!schemaTfile.exists()) {
				RandomAccessFile schemataTableFile = new RandomAccessFile(CONSTANTS.BASEDIR + arr[2] + ".schemata.tbl",
						"rw");
				schemataTableFile.seek(schemataTableFile.length());
				schemataTableFile.writeByte(arr[2].length());
				schemataTableFile.writeBytes(arr[2]);
				schemataTableFile.writeByte("\n".length());
				schemataTableFile.writeBytes("\n");
			} else {
				System.out.println("File already exists");
			}

			RandomAccessFile schemataTableFileinfo = new RandomAccessFile(
					CONSTANTS.BASEDIR + "information_schema.schemata.tbl", "rw");
			schemataTableFileinfo.seek(schemataTableFileinfo.length());
			schemataTableFileinfo.writeByte(arr[2].length());
			schemataTableFileinfo.writeBytes(arr[2]);
			schemataTableFileinfo.writeByte("\n".length());
			schemataTableFileinfo.writeBytes("\n");

		} catch (Exception e) {
			System.out.println(e);
		}

	}

	public static void createTable(HashMap<String, String> hashMap, String schemaName) throws IOException {
		String listofStrings = null;
		for (String str : hashMap.keySet()) {

			listofStrings = hashMap.get(str);
		}
		String ll = listofStrings.trim();
		
		if (ll.contains("PRIMARY KEY")) {
			//System.out.println("Here");
			ll.replace("PRIMARY KEY", "PRIMARYKEY");
		}
		//System.out.println("ll------>" + ll);
		boolean flag = true;
		String tableName = "";
		String arrString[] = ll.split(",");
		List<String> field = new ArrayList<>();
		List<String> type = new ArrayList<>();
		List<String> nullvalues = new ArrayList<>();
		List<String> key = new ArrayList<>();

		for (int i = 0; i < arrString.length; i++) {
			// System.out.println(arrString.length);
			// System.out.println(arrString[i]+"-----");

			String newArrString[] = arrString[i].split(" ");

		//	System.out.println(newArrString[0] + " " + newArrString[1] + "---" + newArrString.length);
			if (flag) {

				tableName = newArrString[1].trim();
				// System.out.println("tablename"+tableName);
				flag = false;

			} else if (!newArrString[0].isEmpty() && !newArrString[1].isEmpty()) {
				// System.out.println("Hello"+newArrString[0]);
				field.add(i - 1, newArrString[0].trim());
				type.add(i - 1, newArrString[1].trim());
				if (newArrString.length > 2) {
					nullvalues.add(i - 1, newArrString[2].trim());
					key.add(i - 1, newArrString[3].trim());
				}
			} else {
				System.out.println("Invalid syntax");
			}
			//System.out.println("length" + arrString.length);
			/*
			for (int k = 0; k < arrString.length; k++) {
				System.out.println("in:" + arrString[k]);
			}

			System.out.println("we RE DONE with printing it");
			*/
		}

		// String tablepath = "/Users/shobhikapanda/Desktop/Schema/Tables/";
		// System.out.println("schemaNAme"+"tableMName"+schemaName+tableName);
		if (schemaName != null) {

			try {
				entryIntoInformationSchemaTable(schemaName, tableName, CONSTANTS.TABLESPATH);
				schemataTableFile = new RandomAccessFile(
						CONSTANTS.TABLESPATH + schemaName + "." + tableName + ".table_name.tbl", "rw");
				// entryInfoschemataTableFile = new
				// RandomAccessFile("information_schema.table.tbl", "rw");
			} catch (Exception e) {
				System.out.println(e);
			}
		} else {
			try {
				schemaName = defaultSchemaName;
				entryIntoInformationSchemaTable(schemaName, tableName, CONSTANTS.TABLESPATH);
				schemataTableFile = new RandomAccessFile(
						CONSTANTS.TABLESPATH + defaultSchemaName + "." + tableName + ".tbl", "rw");
			} catch (Exception e) {
				System.out.println(e);
			}
		}

		createTableUtil(arrString, schemaName, field, type, nullvalues, key, tableName);

	}

	public static void createTableUtil(String[] arrString, String schemaName, List<String> field, List<String> type,
			List<String> nullvalues, List<String> key, String tableName) throws IOException {
		String columnsfieldtableIdIndexName = "";
		String nullV = "", keyV = "";
		int index = 0;
		for (int i = 0; i < arrString.length - 1; i++) {
			//System.out.println("field" + field.get(i));
			//System.out.println("type" + type.get(i));
			columnsfieldtableIdIndexName = schemaName + "." + tableName + "." + field.get(i) + "." + "ndx";
			// System.out.println("index file name-->" +
			// columnsfieldtableIdIndexName);

			try {
				RandomAccessFile columnsfieldtableIdIndex = new RandomAccessFile(
						CONSTANTS.COLUMNSPATH + columnsfieldtableIdIndexName, "rw");

				schemataTableFile.seek(schemataTableFile.length());
				schemataTableFile.writeByte(field.get(i).length());

				schemataTableFile.writeBytes(field.get(i));

				schemataTableFile.writeByte(type.get(i).length());

				schemataTableFile.writeBytes(type.get(i));

				if (nullvalues != null && nullvalues.size() != 0 && i < nullvalues.size()) {
					nullV = nullvalues.get(i);
					schemataTableFile.writeByte(nullvalues.get(i).length());
					schemataTableFile.writeBytes(nullvalues.get(i));
				} else {
					nullV = "YES";
					schemataTableFile.writeByte("YES".length());
					schemataTableFile.writeBytes("YES");
				}
				if (key != null && key.size() != 0 && i < nullvalues.size()) {
					keyV = key.get(i);
					schemataTableFile.writeByte(key.get(i).length());
					schemataTableFile.writeBytes(key.get(i));

				} else {
					keyV = "";
					schemataTableFile.writeByte("".length());
					schemataTableFile.writeBytes("");
				}
				schemataTableFile.writeByte("\n".length());
				schemataTableFile.writeBytes("\n");
				// TODO index out of bound exception
				entryIntoInformationSchemaColumns(schemaName, tableName, field.get(i), type.get(i), nullV, keyV,
						CONSTANTS.COLUMNSPATH, ++index);
			
			} 
			catch (Exception e) {
				e.printStackTrace();
				System.out.println(e);
			}
			

		}
		RandomAccessFile columnsTableFile = new RandomAccessFile(CONSTANTS.COLUMNSPATH + "information_schema.columns.tbl",
				"rw");
		columnsTableFile.seek(columnsTableFile.length());
		columnsTableFile.writeByte("\n".length());
		columnsTableFile.writeBytes("\n");
		System.out.println("Create Table successful");
	}

	public static void entryIntoInformationSchemaTable(String schemaName, String tableName, String tablePath) {

		try {
			RandomAccessFile tablesTableFile = new RandomAccessFile(tablePath + "information_schema.table.tbl", "rw");
			tablesTableFile.seek(tablesTableFile.length());
			tablesTableFile.writeByte(schemaName.length()); // TABLE_SCHEMA
			tablesTableFile.writeBytes(schemaName);
			tablesTableFile.writeByte(tableName.length()); // TABLE_NAME
			tablesTableFile.writeBytes(tableName);
			tablesTableFile.writeLong(1);
			tablesTableFile.writeByte("\n".length());
			tablesTableFile.writeBytes("\n");

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void entryIntoInformationSchemaColumns(String schemaName, String tableName, String field, String type,
			String nullvalues, String key, String columnPath, int index) {
		//System.out.println("Hello" + index + " " + nullvalues + " " + key);
		try {
			RandomAccessFile columnsTableFile = new RandomAccessFile(columnPath + "information_schema.columns.tbl",
					"rw");
			columnsTableFile.seek(columnsTableFile.length());
			columnsTableFile.writeByte(schemaName.length()); // TABLE_SCHEMA
			columnsTableFile.writeBytes(schemaName);
			columnsTableFile.writeByte(tableName.length()); // TABLE_NAME
			columnsTableFile.writeBytes(tableName);
			columnsTableFile.writeByte(field.length()); // COLUMN_NAME
			columnsTableFile.writeBytes(field);
			columnsTableFile.writeInt(index); // ORDINAL_POSITION
			columnsTableFile.writeByte(type.length()); // COLUMN_TYPE
			columnsTableFile.writeBytes(type);
			columnsTableFile.writeByte(nullvalues.length()); // IS_NULLABLE
			columnsTableFile.writeBytes(nullvalues);
			columnsTableFile.writeByte(key.length()); // COLUMN_KEY
			columnsTableFile.writeBytes(key);
			

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void insertTable(HashMap<String, String> hashMap, String schemaName) throws IOException, ParseException {
		String listofStrings = "";

		for (String str : hashMap.keySet()) {
			// System.out.println("str--"+str);
			if (str.equals("INSERT")) {
				listofStrings = hashMap.get(str);
			}
		}
		// System.out.println("list it out "+listofStrings);
		String tableName = "";

		String splitItOut[] = listofStrings.split(" ");
		tableName = splitItOut[2];
		//System.out.println("lenght is :" + splitItOut.length);
		//System.out.println("First  is :" + splitItOut[0]);
		//System.out.println("second  is :" + splitItOut[1]);
		//System.out.println("third  is :" + splitItOut[2]);
		//System.out.println("Fourth  is :" + splitItOut[3]);
		//System.out.println("Fifth is :" + splitItOut[4]);
		//System.out.println("six  is :" + splitItOut[5]);
		Object[] arrField = splitItOut[3].split(",");
		Object[] arrValues = splitItOut[5].split(",");
		String readCreateTableFileName = CONSTANTS.TABLESPATH + schemaName + "." + tableName + "." + "table_name"
				+ ".tbl";
		//System.out.println(">>>>" + readCreateTableFileName);
		RandomAccessFile readdataTableFile = new RandomAccessFile(readCreateTableFileName, "rw");
		String insertTableFileName = CONSTANTS.TABLESPATH + schemaName + "." + tableName + "." + "insertData" + ".dat";
		RandomAccessFile insertdataTableFile = new RandomAccessFile(insertTableFileName, "rw");
		HashMap<String, String> hm = new HashMap<>();
		HashMap<String, String> userMap = new HashMap<>();
		List<String> orderedList=new ArrayList<>();

		// HashMap<String,Integer> hIndexed=new HashMap<>();
		try {
			int count = 0;
			String line = null;
			while ((line = readdataTableFile.readLine()) != null) {
				count++;
			}
			readdataTableFile.seek(0);
			int index = 0;
			while (index < count) {
				byte varcharLength = readdataTableFile.readByte();
				String field = "";
				String type = "";
				String nullValue = "";
				String key = "";
				for (int i = 0; i < varcharLength; i++)
					// System.out.print((char)readdataTableFile.readByte());
					field += (char) readdataTableFile.readByte();
				varcharLength = readdataTableFile.readByte();

				for (int i = 0; i < varcharLength; i++)
					// System.out.print((char)readdataTableFile.readByte());
					type += (char) readdataTableFile.readByte();
				varcharLength = readdataTableFile.readByte();
				for (int i = 0; i < varcharLength; i++)
					// System.out.print((char)readdataTableFile.readByte());
					nullValue += (char) readdataTableFile.readByte();
				varcharLength = readdataTableFile.readByte();
				for (int i = 0; i < varcharLength; i++)
					// System.out.print((char)readdataTableFile.readByte());
					key += (char) readdataTableFile.readByte();
			//	System.out.println("Type is :" + type + " Field is :" + field + " length of key is :" + type.length()
					//	+ "Null is :" + nullValue + "Key is:" + key);
				hm.put(field.trim(), type.trim());
				// hIndexed.put(field, index++);
				index++;
				readdataTableFile.readLine();

			}
			readdataTableFile.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
		if (arrField != null) {
			for (int i = 0; i < arrField.length; i++) {
				//System.out.println("F is :" + arrField[i] + " V is :" + arrValues[i]);
				userMap.put(String.valueOf(arrField[i]).trim(), String.valueOf(arrValues[i]).trim());
				//globalUserMap.put(String.valueOf(arrField[i]).trim(), String.valueOf(arrValues[i]).trim());
				orderedList.add(String.valueOf(arrField[i]).trim());
			}
		}
		if (arrValues != null) {
			// print hm
			/*
			for (String key : hm.keySet()) {
				System.out.println("Key is :" + key + " value is :" + hm.get(key));
			}
			*/
			Long filePointer = insertdataTableFile.getFilePointer() + insertdataTableFile.length();
			//System.out.println("printint user map");
			/*
			for(String key : userMap.keySet()) {
				System.out.println("Key is :" + key + " value is :" + userMap.get(key));
			}
			*/
			for(int i=0;i<orderedList.size();i++){
			
				

				//System.out.println("temp" + orderedList.get(i));
				String type = hm.get(orderedList.get(i));
				//System.out.println("type" + type);
				if (type.contains("(")) {
					type = type.split("\\(")[0];
				}
			//	System.out.println(type);

				String line="";
				
				String columnFieldName = CONSTANTS.COLUMNSPATH + schemaName + "." + tableName + "." + orderedList.get(i).trim() + "."
						+ "ndx";
				columnsfieldtableIdIndex = new RandomAccessFile(columnFieldName, "rw");
				
				
				
				if (columnsfieldtableIdIndex.length() != 0) {
					//TreeMap<Object, Long> indexupdate=createTreeMap(type);
					TreeMap<String, Long> indexupdate = new TreeMap<>();
					//System.out.println("in if");
					int count = 0, index = 0;
					while ((line = columnsfieldtableIdIndex.readLine()) != null) {
						if(line.length() !=0) {
						count++;
						}
					}
					//System.out.println("count is : " + count + " for file :" + columnFieldName);
					columnsfieldtableIdIndex.seek(0);
					while (index < count) {
						byte len = columnsfieldtableIdIndex.readByte();
						//System.out.println("length---->"+len);
						String b = "";
						for (int j = 0; j < len; j++) {
							b += ((char) columnsfieldtableIdIndex.readByte());

						}
						long fp = columnsfieldtableIdIndex.readLong();
						//System.out.println("b:"+b);
						//System.out.println("fp"+filePointer);
						indexupdate.put(b.trim(), fp);
						// len=columnsfieldtableIdIndex.readByte();
						index++;
						columnsfieldtableIdIndex.readLine();
					}
					indexupdate.put(userMap.get(orderedList.get(i).trim()).trim(), filePointer);
					columnsfieldtableIdIndex.setLength(0);
					writeUtil(indexupdate, columnsfieldtableIdIndex);

				}
				else{
					//System.out.println("always in else");
					//System.out.println("File trying to acces is :" + columnFieldName);
					//System.out.println("the column is  :" + orderedList.get(i));
					//System.out.println("dsfsdf"+userMap.get(orderedList.get(i).trim()));
					//System.out.println("d*****df"+userMap.get(orderedList.get(i).trim()).trim().length());
					//columnsfieldtableIdIndex.writeByte("1993-12-18".length());
					//columnsfieldtableIdIndex.writeBytes("1993-12-18");
					columnsfieldtableIdIndex.writeByte(userMap.get(orderedList.get(i).trim()).trim().length());
					columnsfieldtableIdIndex.writeBytes(userMap.get(orderedList.get(i).trim()).trim());
					columnsfieldtableIdIndex.writeLong(filePointer);
					columnsfieldtableIdIndex.writeByte("\n".length());
					columnsfieldtableIdIndex.writeBytes("\n");
				}
				//columnsfieldtableIdIndex.seek(0);
				// indexupdate.put(userMap.get(temp.trim()), columnsfieldtableIdIndex.getFilePointer());
				//

				InsertUtil.insertData(insertdataTableFile, type.toUpperCase(), userMap.get(orderedList.get(i).trim()).trim());

				columnsfieldtableIdIndex.close();
				
			
		}
			insertdataTableFile.close();
			System.out.println("Insert table successfull");
			// insertdataTableFile.close();
			// System.out.println(insertdataTableFile.readInt());

		} else {
			System.out.println("Please enter values for insert statement");
		}
	}
	
	public static TreeMap<?,Long> createTreeMap(String type){
		TreeMap<Object,Long> tMap;
		if(type .equals (CONSTANTS.BYTE)){
			return new TreeMap<Byte,Long>();
		}else if(type .equals( CONSTANTS.SHORT)){
			return new TreeMap<Short,Long>();
		}else if(type .equals (CONSTANTS.INT)){
			return new TreeMap<Integer,Long>();
		}else if(type .equals (CONSTANTS.LONG)){
			return new TreeMap<Long,Long>();
		}else if(type .equals (CONSTANTS.CHAR_N)){
		//	System.out.println("ddddd--->"+String.valueOf(data));
			return new TreeMap<String,Long>();
			
		}else if(type .equals (CONSTANTS.VARCHAR_N)){//TODO: Check how to implement varchar
			//System.out.println("ddddd--->"+String.valueOf(data)+" "+String.valueOf(data).length());
			return new TreeMap<String,Long>();
		}else if(type .equals( CONSTANTS.FLOAT)){
			return new TreeMap<Float,Long>();
		}else if(type .equals (CONSTANTS.DOUBLE)){
			return new TreeMap<Double,Long>();
		}else if(type .equals( CONSTANTS.DATETIME)){ //TODO: Check how to implement timestamp
			/* String dateVal = "2016-04-05";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
			sdf.parse(ds).getTime(); */
			return new TreeMap<Date,Long>();
		}else if(type .equals (CONSTANTS.DATE)){//TODO: Check how to implement date
			return new TreeMap<Date,Long>();
		}
		else
			return null;
	}
	public static void writeUtil(TreeMap<String, Long> treeMap, RandomAccessFile file) throws IOException {
		for (String temp : treeMap.keySet()) {
			columnsfieldtableIdIndex.writeByte(temp.trim().length());
			columnsfieldtableIdIndex.writeBytes(temp.trim());
			columnsfieldtableIdIndex.writeLong(treeMap.get(temp.trim()));
			columnsfieldtableIdIndex.writeByte("\n".length());
			columnsfieldtableIdIndex.writeBytes("\n");
		}

	}
	public static void selectTable(HashMap<String,String> hashMap,String schemaName) throws IOException, ParseException{
		String listofStrings="";
		for (String str : hashMap.keySet()) {
			// System.out.println("str--"+str);
			
			
			if (str.equals("SELECT") || str.equals("select")) {
				listofStrings = hashMap.get(str);
			}
		}
			//System.out.println("listofStrings"+listofStrings);
			String arrString[]=listofStrings.split(" ");
			//System.out.println(arrString.length + " "+arrString[1] );
			/*
			for(int i=0;i>arrString.length;i++){
				System.out.println(arrString[i]);
			}*/
			RandomAccessFile readdataTableFile = new RandomAccessFile(
					CONSTANTS.TABLESPATH + schemaName + "." + arrString[3] + ".table_name.tbl", "rw");
			//HashMap<String,String> hmFields=new HashMap<>();
			List<String> hmFields=new ArrayList<>();
			
			writeSelectUtil(readdataTableFile,hmFields);
			System.out.println();
			String type="";
			//System.out.println(line("*",80));
			if(arrString[1].equals("*") && arrString.length > 4 ){
				arrString[7]=arrString[7].replaceAll("'", "");
				Object comparingValue = null;
				if(arrString[7].matches("\\d+")){
					type="int";
					comparingValue = new Integer(Integer.parseInt(arrString[7].trim()));
				}
				else if(arrString[7].matches("\\d{4}-\\d{2}-\\d{2}")){
					type="date";
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
					comparingValue = new Long(sdf.parse(String.valueOf(arrString[7])).getTime());
				}
				else {
					type="string";
					comparingValue = new String(arrString[7]);
				}
				String columnFieldName = CONSTANTS.COLUMNSPATH + schemaName + "." + arrString[3].trim() + "." + arrString[5].trim() + "."
						+ "ndx";
				columnsfieldtableIdIndex = new RandomAccessFile(columnFieldName, "rw");
				List<Long> fpList=new ArrayList<>();
				if (columnsfieldtableIdIndex.length() != 0) {
					String line="";
					long fp;
					//System.out.println("in if");
					int count = 0, index = 0;
					while ((line = columnsfieldtableIdIndex.readLine()) != null) {
						if(line.length()!=0)
						count++;
					}
					//System.out.println("count is : " + count + " for file :" + columnFieldName);
					columnsfieldtableIdIndex.seek(0);
					while (index < count) {
						byte len = columnsfieldtableIdIndex.readByte();
						//System.out.println("length---->"+len);
						String b = "";
						for (int j = 0; j < len; j++) {
							b += ((char) columnsfieldtableIdIndex.readByte());

						}
						Object currentValue = null;
						if(type.equals("int")) {
							currentValue = new Integer(Integer.parseInt(b));
						} else if(type.equals("string")) {
							currentValue = new String(b);
						} 
						else if(type.equals("date")){
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
							currentValue = new Long(sdf.parse(String.valueOf(b)).getTime());
						}
						if(arrString[6].trim().equals("=")){
							if(type.equals("int")) {
								if(((Integer)currentValue).compareTo((Integer)comparingValue) ==0){
									fpList.add(columnsfieldtableIdIndex.readLong());
								}
							} else if(type.equals("string")) {
								if(((String)currentValue).compareTo((String)comparingValue) ==0){
									fpList.add(columnsfieldtableIdIndex.readLong());
								}
								
							}
							else if(type.equals("date")){
								if(((Long)currentValue).compareTo((Long)comparingValue) ==0){
									fpList.add(columnsfieldtableIdIndex.readLong());
								}
							}
							/* if(((Integer)currentValue).compareTo((Integer)comparingValue))){
								fpList.add(columnsfieldtableIdIndex.readLong());
							} */
						}
						else if(arrString[6].trim().equals(">"))
						{
							if(type.equals("int")) {
								if(((Integer)currentValue).compareTo((Integer)comparingValue) >0){
									fpList.add(columnsfieldtableIdIndex.readLong());
								}
							} else if(type.equals("string")) {
								if(((String)currentValue).compareTo((String)comparingValue) >0){
									fpList.add(columnsfieldtableIdIndex.readLong());
								}
							} 
							else if(type.equals("date")){
								if(((Long)currentValue).compareTo((Long)comparingValue) > 0){
									fpList.add(columnsfieldtableIdIndex.readLong());
								}
							}
							
							/* if(b.compareTo(String.valueOf(arrString[7].trim()))>0){
								fpList.add(columnsfieldtableIdIndex.readLong());
							} */
						}
						else if(arrString[6].trim().equals("<"))
						{
							if(type.equals("int")) {
								if(((Integer)currentValue).compareTo((Integer)comparingValue) <0){
									fpList.add(columnsfieldtableIdIndex.readLong());
								}
							} else if(type.equals("string")) {
								if(((String)currentValue).compareTo((String)comparingValue) < 0){
									fpList.add(columnsfieldtableIdIndex.readLong());
								}
							} 
							else if(type.equals("date")){
								if(((Long)currentValue).compareTo((Long)comparingValue) > 0){
									fpList.add(columnsfieldtableIdIndex.readLong());
								}
							}
							/*if(b.compareTo(String.valueOf(arrString[7].trim()))<0){
								fpList.add(columnsfieldtableIdIndex.readLong());
							}*/
						}
					
						index++;
						columnsfieldtableIdIndex.readLine();
					}
					String insertTableFileName = CONSTANTS.TABLESPATH + schemaName + "." + arrString[3] + "." + "insertData" + ".dat";
					RandomAccessFile insertdataTableFile = new RandomAccessFile(insertTableFileName, "rw");
					String ff="";
					//
					for(int i=0;i<fpList.size();i++){
						insertdataTableFile.seek(fpList.get(i));
						for(int j=0;j<hmFields.size();j++){
						// System.out.println("fpList.get(i)"+fpList.get(i));
						String actualString=hmFields.get(j).trim();
						String newString=actualString.replaceAll("[^A-Za-z]+", "");
						System.out.format("%12s",InsertUtil.getData(insertdataTableFile,newString.toUpperCase()));
						//System.out.print(InsertUtil.getData(insertdataTableFile,newString.toUpperCase()));
						//System.out.println("\t");
						}
						System.out.println();
					}
					
					System.out.println(line("*",80));
			}
			}
			else if(arrString[1].equals("*") && arrString.length<=4){
				//System.out.println("Hello");
			
					
				String insertTableFileName = CONSTANTS.TABLESPATH + schemaName + "." + arrString[3] + "." + "insertData" + ".dat";
				RandomAccessFile insertdataTableFile = new RandomAccessFile(insertTableFileName, "rw");
				//flag=hmFields.size();
				//writeSelectUtil(insertdataTableFile,hmValues,flag);
				int count = 0;
				String line = null;
				while ((line = insertdataTableFile.readLine()) != null) {
					count++;
				}
				Long filePointer = insertdataTableFile.getFilePointer();
				insertdataTableFile.seek(0);
				for(int index=0;index<=count;index++){
					for(int i=0;i<hmFields.size();i++){
						String actualString=hmFields.get(i).trim();
						String newString=actualString.replaceAll("[^A-Za-z]+", "");
						//System.out.println(newString);
						System.out.format("%12s", InsertUtil.getData(insertdataTableFile,newString.toUpperCase()));
						//System.out.print(InsertUtil.getData(insertdataTableFile,newString.toUpperCase()));
						//System.out.print("\t");
						
					}
					System.out.println();
				//insertdataTableFile.readLine();
				}
				//System.out.println();
				System.out.println(line("*",80));
				
			}
			else{
				System.out.println("Bad syntax");
			}
		
			
	}
	public static void  writeSelectUtil(RandomAccessFile file,List<String> list) throws IOException{
		int count = 0;
		String line = null;
		while ((line = file.readLine()) != null) {
			count++;
		}
		file.seek(0);
		int index = 0;
		
		System.out.println(line("*",80));
		int leftPad = 0;
		while (index < count) {
			byte varcharLength = file.readByte();
			String field = "";
			String type = "";
			
			for (int i = 0; i < varcharLength; i++)
				// System.out.print((char)readdataTableFile.readByte());
			field += (char) file.readByte();
			varcharLength = file.readByte();

			for (int i = 0; i < varcharLength; i++)
				// System.out.print((char)readdataTableFile.readByte());
				type += (char) file.readByte();
			varcharLength = file.readByte();
			list.add(type.trim());
				//leftPad -=  20;
				//System.out.println(String.format("%-20s" , field ));
			System.out.format("%12s", field);
				// System.out.print(field);
				// System.out.print("\t");
			
			//hm.put(field.trim(), type.trim());
			index++;
			file.readLine();

		}
		
	}
	/**
	 * Help: Display supported commands
	 */
	public static void help() {
		System.out.println(line("*", 80));
		System.out.println();
		System.out.println("\tdisplay all;   Display all records in the table.");
		System.out.println("\tget; <id>;     Display records whose ID is <id>.");
		System.out.println("\tversion;       Show the program version.");
		System.out.println("\thelp;          Show this help information");
		System.out.println("\texit;          Exit the program");
		System.out.println();
		System.out.println();
		System.out.println(line("*", 80));
	}

	/**
	 * Display the welcome "splash screen"
	 */
	public static void splashScreen() {
		System.out.println(line("*", 80));
		System.out.println("Welcome to DavisBaseLite"); // Display the string.
		version();
		System.out.println("Type \"help;\" to display supported commands.");
		System.out.println(line("*", 80));
	}

	/**
	 * @param s
	 *            The String to be repeated
	 * @param num
	 *            The number of time to repeat String s.
	 * @return String A String object, which is the String s appended to itself
	 *         num times.
	 */
	public static String line(String s, int num) {
		String a = "";
		for (int i = 0; i < num; i++) {
			a += s;
		}
		return a;
	}

	/**
	 * @param num
	 *            The number of newlines to be displayed to <b>stdout</b>
	 */
	public static void newline(int num) {
		for (int i = 0; i < num; i++) {
			System.out.println();
		}
	}

	public static void version() {
		System.out.println("DavisBaseLite v1.0\n");
	}

	/**
	 * This method reads a binary table file using a hard-coded table schema.
	 * Your query must be able to read a binary table file using a dynamically
	 * constructed table schema from the information_schema
	 */

}
