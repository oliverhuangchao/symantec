package chaoh;

import java.io.IOException;

public class tranactionMain {

	public static void main(String[] args) throws IOException {
		
		//create a singleton machine here
		vendingMachine machine = vendingMachine.getInstance();
		
		// fill all the thing into the machine
		// you can also add a single item into the machine
		// like machine.additem(item_name, item_price)
		fileIO.add_item_itemlist(machine);
		
		
		// show the current machine status
		// every time you want to know the machine status you can call this function
		machine.show_all_food();
		
		// do the transaction using customers.txt
		fileIO.read_customer_list("customers.txt",machine);
		
		machine.show_all_food();
		System.out.println("finish the result");
	}

}
