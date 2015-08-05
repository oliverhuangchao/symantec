package chaoh;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;

public class fileIO {
	// create an map between item name and id
	static void init_vendor_itemlist(Hashtable<String, Integer> map) throws IOException{
		String fileName = "itemlist.txt";
		String line = null;
		try {
            FileReader fileReader =  new FileReader(fileName);

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
            	String[] parts = line.split("\t");
            	map.put(parts[0], Integer.parseInt(parts[1]));
            }    

            bufferedReader.close();            
        }
        catch(FileNotFoundException ex) {
            System.out.println(fileName + "doesn't exist");                
        }
	}
	
	
	static void add_item_itemlist(vendingMachine machine) throws IOException{
		String fileName = "items.txt";
		String line = null;
		try {
            FileReader fileReader =  new FileReader(fileName);

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
            	String[] parts = line.split(",");
            	float money = Float.parseFloat(parts[1]);
            	machine.additem(parts[0], money);
            }    
            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println(fileName + "doesn't exist");                
        }
	}
	
	
	static void read_customer_list(String fileName,vendingMachine machine) throws IOException{
		fileName = "customers.txt";
		String line = null;
		try {
            FileReader fileReader =  new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while((line = bufferedReader.readLine()) != null) {
            	String[] parts = line.split(",");
            	int code = Integer.parseInt(parts[0]);
            	float money = Float.parseFloat(parts[1]);
            	String res = machine.transaction(code, money);
            	save2file("resutl.txt", res);
            }
            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println(fileName + "doesn't exist");                
        }
	}
	
	
	
	// save contents to file
	static void save2file(String filename,String content){
		filename = "result.txt";
		try {
			File file = new File(filename);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.write("\n");
			bw.close();
			//System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) throws IOException {
		save2file("result.txt", "hello world");
		save2file("result.txt", "chao huang");
	}
}
