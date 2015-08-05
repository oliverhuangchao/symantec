package chaoh;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

public class vendingMachine {
    /*
     *  singleton definition for vendor machine
     * */
	public static vendingMachine getInstance() throws IOException{
		if(singleMachine == null)
			singleMachine = new vendingMachine();
		return singleMachine;
	}
	/*
	 *  private class members go here
	 * */
	private static vendingMachine singleMachine;// 
	
	private float _revenue;
	
	private int _typeSize;
	
	private Hashtable<Integer, Integer> _item_count;// how many items for this type, id->number
	
	private ArrayList<Integer> _coinList;// current coins list, contains numbers of $1, $5, $0.25
	
	private Hashtable<String, Integer> _name2id; //previously settled
	
	private Hashtable<Integer, String> _id2name;// transfer id to its name
	
	private Hashtable<Integer, Float> _id2price;// get price through id
	/*
	 * initialize function will be called once we create a new machine 
	 */
	private void _init_function() throws IOException{
		for(int i=0;i<3;i++) // put some change coins into the machine 
			_coinList.add(100);
		fileIO.init_vendor_itemlist(_name2id);
		for (Iterator<String> it = _name2id.keySet().iterator(); it.hasNext(); ) {
			String name = it.next();
			int id = _name2id.get(name);
		    _item_count.put(id, 0);
		    _id2name.put(id, name);
		}
	}
	
	/*
	 * construction function for vendor machine
	 */
	private vendingMachine() throws IOException{
		_revenue = (float) 0.0;
		_typeSize = 100;
		_item_count = new Hashtable<Integer,Integer>();
		_coinList = new ArrayList<Integer>();
		_name2id = new Hashtable<String, Integer>();
		_id2name = new Hashtable<Integer,String>();
		_id2price = new Hashtable<Integer,Float>();
		_init_function();
	}
	
	/*
	 * add item into the vendor if the shopper want to do 
	 */
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
	
	/*
	 *  buy something from the vendor
	 *  return value is a String due to the requirement
	 *  return a Item is more reasonable
	 *  */
	protected String transaction(int inputcode, float money){
		/*------ if the user input the wrong code------*/
		/*if(inputcode > _typeSize){
			String res = "code out of range";
			System.out.println(res);
			return res;
		}*/
		if((money*100)%25 != 0){
			String res = "Invalid denomination";
			System.out.println(res);
			return res;
		}
				
		if(inputcode > _typeSize || !_item_count.containsKey(inputcode)){// if we do not have this item for this code
			String res = String.format("%d not exist in this vendor",inputcode);
			System.out.println(res);
			return res;
		}
		
		if(_item_count.get(inputcode) == 0 ){// if this item is sold out
			String res = String.format("Out of %s",_id2name.get(inputcode));
			System.out.println(res);
			return res;
		}
		
		if(money < _id2price.get(inputcode)){// the money is not enough
			String res = "your money is not enough";
			System.out.println(res);
			return res;
		}
		
		// if charge is not enough
		int return_money_cents = (int) ((money-_id2price.get(inputcode))*100);
		ArrayList<Integer> return_detail = return_change(return_money_cents);
		if(return_detail.isEmpty() && return_money_cents > 0){
			System.out.println(return_money_cents);
			String res = "our change is not enough";
			System.out.println(res);
			return res;
		}
		
		
		float tmp = money*100; //update the coin list
		_coinList.set(0, _coinList.get(0)+(int) (tmp % 500));
		_coinList.set(1, _coinList.get(1)+(int) ((tmp % 500) / 100));
		_coinList.set(2, _coinList.get(2)+(int) ((tmp % 100) / 25));
		_item_count.put(inputcode, _item_count.get(inputcode)-1);
		_revenue += _id2price.get(inputcode);

		StringBuffer res = new StringBuffer();
		res.append(String.format("success, $%.2f",(money-_id2price.get(inputcode))));
		if(return_detail.isEmpty())
			res.append(": exact fit the price!");
		else{
			res.append(":[");
			for(int i=0;i<return_detail.size();i++){
				res.append("$"+String.valueOf((float)return_detail.get(i)/100));
				if(i != return_detail.size()-1)
					res.append(",");
			}
			res.append("].");
		}
		
		System.out.println(res);
		return res.toString();
	}
	
	/* ---------- return the current revenue ---------- */
	protected float getCurrent() {
		return _revenue;
	}
	
	/* ---------- return the change ---------- */
	@SuppressWarnings("unchecked")
	private ArrayList<Integer> return_change(int change_money){
		ArrayList<Integer> coins = new ArrayList<Integer>();
		coins.add(500);coins.add(100);coins.add(25);
		ArrayList<Integer> path = new ArrayList<Integer>();
		ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
		coin_dfs_helper(coins,_coinList, 0, change_money, path, result);
		if(!result.isEmpty()){
			_coinList = (ArrayList<Integer>) result.get(1).clone();
			return result.get(0);
		}
		else {
			return null;
		}
	}
	
	/* ---------- calculate how to charge the result ---------- */
	private void coin_dfs_helper(ArrayList<Integer> coins, ArrayList<Integer> coins_counts, int pos, int sum, ArrayList<Integer> path,ArrayList<ArrayList<Integer>> result){
		if(!result.isEmpty())
			return;
		if(sum == 0){
			@SuppressWarnings("unchecked")
			ArrayList<Integer> coins_counts_result = (ArrayList<Integer>) coins_counts.clone();
			result.add(path);
			result.add(coins_counts_result);
			return;
		}
		if(sum < 0)
			return;
		for(int i = pos;i<coins.size();i++){
			if(coins_counts.get(i) == 0)
				continue;
			@SuppressWarnings("unchecked")
			ArrayList<Integer> tmp = (ArrayList<Integer>) path.clone();
			coins_counts.set(i, coins_counts.get(i)-1);
			tmp.add(coins.get(i));
			coin_dfs_helper(coins, coins_counts,i, sum-coins.get(i), tmp,result);
			coins_counts.set(i, coins_counts.get(i)+1);
		}
	}
	
	/* ---------- print the food and its count ---------- */
	protected void show_all_food(){
		System.out.println("--------- current machine status ---------");
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
