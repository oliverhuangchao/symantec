package chaoh;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

public class vendingMachine {
	
	
	public static vendingMachine getInstance() throws IOException{
		if(singleMachine == null)
			singleMachine = new vendingMachine();
		return singleMachine;
	}
	
	private static vendingMachine singleMachine;// 
	private float _revenue;
	private int _typeSize;
	
	// key: item id
	// value: item number
	// how many items for this id
	private Hashtable<Integer, Integer> _item_count;
	
	// key is the id:0,1,2 refers to $5, $1, $0.25,
	// value is how manys coins for each price
	private ArrayList<Integer> _coinList;
	
	// key is the id, value is the item name 
	private Hashtable<String, Integer> _name2id; //previously setted
	
	// transfer id to its name
	private Hashtable<Integer, String> _id2name; 	
	
	// get price through id
	private Hashtable<Integer, Float> _id2price;
	
	protected void _init_function() throws IOException{
		/* put some change coins into the machine */
		for(int i=0;i<3;i++)
			_coinList.add(100);
		fileIO.init_vendor_itemlist(_name2id);
		
		for (Iterator<String> it = _name2id.keySet().iterator(); it.hasNext(); ) {
			String name = it.next();
			int id = _name2id.get(name);
		    _item_count.put(id, 0);
		    _id2name.put(id, name);
		}
	}
	
	protected vendingMachine() throws IOException{
		_revenue = (float) 0.0;
		_typeSize = 100;
		_item_count = new Hashtable<Integer,Integer>();
		_coinList = new ArrayList<Integer>();
		_name2id = new Hashtable<String, Integer>();
		_id2name = new Hashtable<Integer,String>();
		_id2price = new Hashtable<Integer,Float>();
		_init_function();
	}
	
	// add item into the vendor if ths shopper want
	protected void additem(String item_name, float item_price){
		int id = _name2id.get(item_name);
		if(_item_count.containsKey(id)){
			_item_count.put(id, _item_count.get(id)+1);
		}
		else {
			_item_count.put(id, 1);
			_id2name.put(id, item_name);
		}
		if(!_id2price.containsKey(id))
			_id2price.put(id, item_price);
	}
	
	// buy something from the vendor
	protected String transaction(int inputcode, float money){
		/*------ if the user input the wrong code------*/
		if(inputcode > _typeSize){
			String res = "code out of range";
			System.out.println(res);
			return res;
		}
		// if we do not have this item for this code
		if(!_item_count.containsKey(inputcode)){
			//System.out.println(_item_count);
			String res = String.format("%d not exist in this vendor",inputcode);
			System.out.println(res);
			return res;
		}
		// if this item is sold out
		if(_item_count.get(inputcode) == 0 ){
			String res = String.format("Out of %s",_id2name.get(inputcode));
			System.out.println(res);
			return res;
		}
		// the money is not enough
		if(money < _id2price.get(inputcode)){
			String res = "money is not enough";
			System.out.println(res);
			return res;
		}
		//VenderItem item = new VenderItem(_id2name.get(inputcode), _id2price.get(inputcode));
		String res = String.format("success, %.2f",money-_id2price.get(inputcode));
		_item_count.put(inputcode, _item_count.get(inputcode)-1);
		_revenue += _id2price.get(inputcode);
		System.out.println(res);
		return res;
	}
	
	// return the current revenue
	protected float getCurrent() {
		return _revenue;
	}
	
	
	// print the food and its count
	protected void show_all_food(){
		System.out.println("--------- current machine ---------");
		for (Iterator<Integer> it = _item_count.keySet().iterator(); it.hasNext(); ) {
		    Integer key = it.next();
		    String name = _id2name.get(key);
		    Integer count = _item_count.get(key);
		    System.out.printf("Code is %d, %s has %d items, price is %.2f \n",key,name,count,_id2price.get(key));
		    //System.out.println("");
		    
		}
		System.out.printf("Current revenue is %.2f \n\n\n",_revenue);
	}
	
}
