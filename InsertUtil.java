import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InsertUtil {

	public static void insertData(RandomAccessFile file,String type,Object data)throws IOException, ParseException{
		//System.out.println("hhhhh"+String.valueOf(data));
		//System.out.println("length in seek is :" + file.length());
		file.seek(file.length());
		if(type .equals (CONSTANTS.BYTE)){
			file.write(Byte.parseByte(String.valueOf(data)));
		}else if(type .equals( CONSTANTS.SHORT)){
			file.writeShort(Short.parseShort(String.valueOf(data)));
		}else if(type .equals (CONSTANTS.INT)){
			file.writeInt(Integer.parseInt(String.valueOf(data)));
		}else if(type .equals (CONSTANTS.LONG)){
			file.writeLong(Long.parseLong(String.valueOf(data)));
		}else if(type .equals (CONSTANTS.CHAR_N)){
		//	System.out.println("ddddd--->"+String.valueOf(data));
			file.writeByte((String.valueOf(data).length()));
			file.writeBytes(String.valueOf(data));
		}else if(type .equals (CONSTANTS.VARCHAR_N)){//TODO: Check how to implement varchar
			//System.out.println("ddddd--->"+String.valueOf(data)+" "+String.valueOf(data).length());
			file.writeByte((String.valueOf(data).length()));
			file.writeBytes(String.valueOf(data));
		}else if(type .equals( CONSTANTS.FLOAT)){
			file.writeFloat(Float.parseFloat(String.valueOf(data)));
		}else if(type .equals (CONSTANTS.DOUBLE)){
			file.writeDouble(Double.parseDouble(String.valueOf(data)));
		}else if(type .equals( CONSTANTS.DATETIME)){ //TODO: Check how to implement timestamp
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
			file.writeLong(sdf.parse(String.valueOf(data)).getTime());
		}else if(type .equals (CONSTANTS.DATE)){//TODO: Check how to implement date
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
			file.writeLong(sdf.parse(String.valueOf(data)).getTime());
			//file.writeByte((String.valueOf(data).length()));
			//file.writeBytes(String.valueOf(data));
		}else {
			file.write(null);
		}
		
	}
	public static void insertSelectData(RandomAccessFile file,String type,Object data)throws IOException{
		//System.out.println("hhhhh"+String.valueOf(data));
		file.seek(file.length());
		if(type .equals (CONSTANTS.BYTE)){
			file.write((byte)data);
		}else if(type .equals( CONSTANTS.SHORT)){
			file.writeShort((short)data);
		}else if(type .equals (CONSTANTS.INT)){
			file.writeInt(Integer.parseInt(String.valueOf(data)));
		}else if(type .equals (CONSTANTS.LONG)){
			file.writeLong((long)data);
		}else if(type .equals (CONSTANTS.CHAR_N)){
		//	System.out.println("ddddd--->"+String.valueOf(data));
			file.writeByte((String.valueOf(data).length()));
			file.writeBytes(String.valueOf(data));
		}else if(type .equals (CONSTANTS.VARCHAR_N)){//TODO: Check how to implement varchar
			//System.out.println("ddddd--->"+String.valueOf(data)+" "+String.valueOf(data).length());
			file.writeByte((String.valueOf(data).length()));
			file.writeBytes(String.valueOf(data));
		}else if(type .equals( CONSTANTS.FLOAT)){
			file.writeFloat((float)data);
		}else if(type .equals (CONSTANTS.DOUBLE)){
			file.writeDouble((double)data);
		}else if(type .equals( CONSTANTS.DATETIME)){ //TODO: Check how to implement timestamp
			/* String dateVal = "2016-04-05";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
			sdf.parse(ds).getTime(); */
			
			file.writeByte(data.toString().length());
			file.writeBytes(data.toString());
		}else if(type .equals (CONSTANTS.DATE)){//TODO: Check how to implement date
			file.writeByte(data.toString().length());
			file.writeBytes(data.toString());
		}else {
			file.write(null);
		}
		
	}
	public static String getData(RandomAccessFile file,String type)throws IOException,EOFException{
		
		if(type .equals (CONSTANTS.BYTE)){
			return String.valueOf(file.readByte());
		}else if(type .equals (CONSTANTS.SHORT)){
			
			return String.valueOf(file.readShort());
		}else if(type .equals (CONSTANTS.INT)){
			int readVal=file.readInt();
			//System.out.println("file read"+file.readInt());
			// filePointer = file.readLong();
			return String.valueOf(readVal);
			
		}else if(type .equals( CONSTANTS.LONG)){
			return String.valueOf(file.readLong());
		}else if(type .equals(CONSTANTS.CHAR_N)){
			byte len = file.readByte();
			StringBuilder b = new StringBuilder();
			for(int i=0;i< len;i++){
				b.append((char)file.readByte());
			}
			// filePointer = file.readLong();
			return b.toString();
		}else if(type .equals (CONSTANTS.VARCHAR_N)){//TODO: Check how to implement varchar
			//System.out.println("FILE file.readByte() is :"+file.readByte());
			byte len = file.readByte();		
			// System.out.println("FILE length is :"+len+" "+file.readByte()+" "+file.readByte());
			String b = "";
			for(int i=0;i< len;i++){			
				// System.out.println("FILE READ:2"+file.readByte());
				b+=((char)file.readByte());
				// System.out.println("B----->"+b);
			}
			// filePointer = file.readLong();
			return b;
		}else if(type .equals( CONSTANTS.FLOAT)){
			return String.valueOf(file.readFloat());
		}else if(type .equals (CONSTANTS.DOUBLE)){
			return String.valueOf(file.readDouble());
		}else if(type .equals( CONSTANTS.DATETIME)){ //TODO: Check how to implement timestamp
			return String.valueOf(file.readLong());
			
		}else if(type .equals( CONSTANTS.DATE)){//TODO: Check how to implement date
			Date date = new Date(file.readLong());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
			
			return sdf.format(date);
			
		}else {
			return "NULL";
		}
		
	}
	
}
