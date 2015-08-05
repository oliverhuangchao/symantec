package chaoh;

import java.io.IOException;



public class tranaction {

	public static void main(String[] args) throws IOException {
		vendingMachine machine = vendingMachine.getInstance();
		//machine.show_all_food();
		fileIO.add_item_itemlist(machine);
		machine.show_all_food();
		fileIO.read_customer_list("customers.txt",machine);
		machine.show_all_food();
		System.out.println("finish the result");
	}

}
