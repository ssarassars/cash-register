*Write a cash register program dealing with transactions */


public class CashRegister{
  /* these static attributes keep a count of how many    */
  /* times the updateMoney and allLoonies methods        */
  /* are called (do not use these!)                      */
  public static int updateMoneyCount = 0;
  public static int allLooniesCount  = 0;
    
 
  /* attributes                                */
  /* you need to define your own attributes.   */
  /* they should be private                    */
  
  private int n1, n5, n10, n20, n50;
  private int total;
  private int[] valueTypes = {1, 5, 10, 20, 50};
  
  
  /* constructors */
  /* different ways of specifying the money amount */
  public CashRegister(){
    /* creates a register with zero money */
    this(new Money());
  }
  
  public CashRegister(int[] money){
    /* creates a register with money specified in money array [loonies, 5s, 10s, 20s, 50s] */
      this(new Money(money[0], money[1], money[2], money[3], money[4]));
  }
  
  public CashRegister(Money money){
    /* creates a register with the money specified in the money object  */  
    this(money.d1, money.d5, money.d10, money.d20, money.d50);
  }
  
  public CashRegister(int n1, int n5, int n10, int n20, int n50){
   /* creates  a regster with specified loonies n1, fives n5, tens n10 , twenties n20, fifties n50*/
    this.n1 = n1;
    this.n5 = n5;
    this.n10 = n10;
    this.n20 = n20;
    this.n50 = n50;
    this.total = 0;
    
  }
  
 
  /* ------------------------------------------------------------- 
   *   getters 
   * ------------------------------------------------------------- */
   
  /* returns number of loonies in the register */
  public int get1(){return this.n1;}
  
  /* returns number of five dollar bills in the register */
  public int get5(){return this.n5;}

  /* returns number of ten dollar bills in the register */
  public int get10(){return this.n10;}
 
  /* returns number of twenty dollar bills in the register */
 public int get20(){return this.n20;}
 
   /* returns number of fifty dollar bills in the register */
  public int get50(){return this.n50;}
  
    /* returns total value of all money in register      */
  public int getTotalValue(){
    return this.getTotalValueOfMoney(getMoney());
  }
  
  /* returns all money in register as an array [loonies, 5s, 10s, 20s, 50s]   */
  public int[] getAll(){
    return  new int[] {get1(), get5(), get10(), get20(), get50()};
  }
  

  /* returns Money object that corresponds to all money  in the register */
  public Money getMoney(){ 
    return new Money(get1(), get5(), get10(), get20(), get50()); 
  }
  /*Setting money to money array index*/
  public void setAll(int[] money) {
    this.n1 = money[0];
    this.n5 = money[1];
    this.n10 = money[2];
    this.n20 = money[3];
    this.n50 = money[4];
  }
  
  //Get total value of money in the cash register
  public int getTotalValueOfMoney (Money money) {
    int total = 0; 
    if (money != null) {
      total += money.d1 + money.d5 * 5 + money.d10 * 10 + money.d20 * 20 + money.d50 * 50;
    }
    
    return total;
  }
//Allocating change in terms of value types
  public int[] getChangeFromPurchase (int value) {
    int[] newMoney = new int [5];
    int counter = valueTypes.length - 1;
    for (int i=counter; i>=0; i--){
          newMoney[i] = (int) (value / valueTypes[i]);
          value = value % valueTypes[i];
    }
    
    return newMoney;
    
  }
  //Shuffling the allocation of value type bills to make exact change
   public int[] getShuffledRegisterMoney (int[] moneyInRegister, int value, int startIndex) {
    int[] newMoney = new int [5];
    int counter = valueTypes.length - 1;
    
    for (int i=counter; i>=0; i--){
      newMoney[i] = 0;
      if(startIndex >= i) {
          newMoney[i] = (int) (value / valueTypes[i]) + moneyInRegister[i];
          value = value % valueTypes[i];
      }
    }
    
    return newMoney;
    
  }
  //Checking to see if register has sufficient money to return change
  public boolean isChangeValid(int[] change){
    int[] moneyInRegister = this.getAll();
    for (int i=0; i<moneyInRegister.length; i++) {
      if (change[i] > moneyInRegister[i]){
        return false;
      }
    }
    
    return true;
  }
  //Calculate total money in register after retrning change
   public void reduceMoneyInRegister(int[] money){
    int[] moneyInRegister = this.getAll();
    for (int i=0; i<moneyInRegister.length; i++) {
      moneyInRegister[i] = moneyInRegister[i] - money[i];
    }
    
    this.setAll(moneyInRegister);
  }
   
   //Add money to cash regsiter after receiving payment for item
  public void addMoneyToCahRegisterTotal (Money money) {
    this.n1 = money.d1;
    this.n5 = money.d5;
    this.n10 = money.d10;
    this.n20 = money.d20;
    this.n50 = money.d50;
  }
  


  
  /* ------------------------------------------------------------- 
   *   methods 
   * ------------------------------------------------------------- */
  
  
  protected Money purchaseItem(Item item, Money payment){
    /*-----------------------------------------------------------
     * process a purchase transaction
     * 
     * preconditions: -item and payment are both non-null
     * 
     * postconditions: -if the payment was not enough for the 
     *                  purchase returns null
     *                 -if payment was enough then returns a money
     *                  object with the change given (might be zero)
     * 
     * side effects: -if payment was enough, the money in the
     *                cash register is updated with the price
     *                of the transaction
     *               -if when making change, the cash register is 
     *                unable to make proper change, it will call
     *                the updateMoney() method to modify its money distribution.
    *                If it is still unable to make exact change, then 
    *                the allLoonies() method is called.
     *-------------------------------------------------------------------
     */
    if (item != null && payment != null) {
      int totalValueOfMoney = this.getTotalValueOfMoney(payment);
      if (totalValueOfMoney > item.getPrice()) {
        this.addMoneyToCahRegisterTotal(payment);
        
        //handle change for the user
        int change = totalValueOfMoney - item.getPrice();
        int[] potentialChange = this.getChangeFromPurchase(change);

        if (!this.isChangeValid(potentialChange)) {
           this.updateMoney();
           potentialChange = this.getChangeFromPurchase(change);
           if (!this.isChangeValid(potentialChange)) {
             this.allLoonies();
             potentialChange = this.getChangeFromPurchase(change);
           }
           
        }
        
        this.reduceMoneyInRegister(potentialChange);
        return new Money (potentialChange[0], potentialChange[1], potentialChange[2], potentialChange[3], potentialChange[4]);
      }
    }
    
    return null;
  }
  
  
  
  protected Money returnItem(Item item){
    /*-----------------------------------------------------------
     * return an item (giving money back) 
     * 
     * preconditions: -item is non-null
     * 
     * postconditions: -if the register has enough money to give back 
    *                  for the item then that value is returned as
    *                  a money object. (The money object corresponds to 
    *                  the actual number of loonies/bill given back.)
    *                 -otherwise, returns null.
    * 
     * side effects: -if the register has enough money but cannot give 
    *                this amount exactly, it calls the updateMoney() 
    *                method to try to give the exact value. 
    *                If this also fails then the allLoonies() method
    *                is called.
    *               -the amount of money in the register after the method
    *                is reduced by the price if the register was able to 
    *                give this value.
    *-------------------------------------------------------------------
     */
    
    if (item != null) {
      if (this.getTotalValue() > item.getPrice()) {
          int[] potentialChange = this.getChangeFromPurchase(item.getPrice());
          if (!this.isChangeValid(potentialChange)) {
           this.updateMoney();
           potentialChange = this.getChangeFromPurchase(item.getPrice());
           if (!this.isChangeValid(potentialChange)) {
             this.allLoonies();
             potentialChange = this.getChangeFromPurchase(item.getPrice());
           }
          }
          
         this.reduceMoneyInRegister(potentialChange);
        return new Money (potentialChange[0], potentialChange[1], potentialChange[2], potentialChange[3], potentialChange[4]);
          
      }
    }
    
    return null;
  }
  
  protected CashRegister updateMoney(){ 
    /*----------------------------------------------------
     * Purpose is to change the distribution of loonies/bills
    * while keeping the total value the same.
    * For example, 10 loonies might be exchanged for 2 five 
    * dollar bills.
     * 
    * preconditions - none
    * postconditions - returns itself (this)
    * side effects - the distribution of loonies/bills is possibly 
    *                changed in some way (it may not change) while
    *                the total value remains the same
     *-----------------------------------
     */
    
    /* DO NOT CHANGE THIS LINE */
  updateMoneyCount += 1;
    
    
    /*----------------------------------------------------
     * add your code below this comment block                                  
     *--------------------------------------------------- */
  int[] moneyInRegister = this.getAll();
  int highestValueInRegister = 0;
  int counter = moneyInRegister.length - 1;
  int index = 0;
  //split the highest currency into lower currency
  for (int i=counter; i>=0; i--) {
    if (moneyInRegister[i] > 0){
      highestValueInRegister = moneyInRegister[i] * valueTypes[i];
      index = i - 1;
      break;
    }
  }
  
  int[] newMoney = this.getShuffledRegisterMoney(moneyInRegister, highestValueInRegister, index);
  
  this.setAll(newMoney);

    /* DO NOT CHANGE THIS LINE */ 
    /* your method must return this */
    return this;
  }




  
  public CashRegister allLoonies(){
    /*--------------------------------------------------------------------
     * Purpose is to change all bills in the register to loonies.
  *
  * preconditions - none
  * postconditions - returns itself (this)
  * side effects - all money in the register is changed to loonies
  *                while the total value remains the same
     *------------------------------------------------------------------
     */
    
    /* DO NOT CHANGE THIS LINE */
  allLooniesCount += 1;
    
    
    /*----------------------------------------------------
     * add your code below this comment block                            
     *--------------------------------------------------- */
 
  int highestValueInRegister = 0;
  int counter = 4;
  
  //split the highest currency into lower currency
  for (int i=counter; i>=1; i--) {
    int[] moneyInRegister = this.getAll();
    if (moneyInRegister[i] > 0){
      highestValueInRegister = moneyInRegister[i] * valueTypes[i];
      int[] newMoney = this.getShuffledRegisterMoney(moneyInRegister, highestValueInRegister, i-1);
      this.setAll(newMoney);
    }
  }
    /* DO NOT CHANGE THIS LINE */ 
    /* your method must return this */
    return this;
  }
  //Testing
  public static void main (String [] args){
    Item gum = new Item("Gum", 20);
    System.out.println(gum.getName());
    
    CashRegister cash = new CashRegister(); 
    System.out.println(cash.get20());

    
    Money m = cash.purchaseItem(gum, new Money(3,1,1,0,1));
    int[] array = m.getAll();
    System.out.println("money object with " + cash.getTotalValueOfMoney(m));
    
    for (int i=0; i<array.length; i++)
    System.out.println("index " + i + " is " + array[i]);

    int total = cash.getTotalValue();
    System.out.println("total is " + total);

    int loonies = cash.get1();
    System.out.println(loonies);
    
    int fives = cash.get5();
    System.out.println(fives);
    
    int allLoonies = cash.allLoonies().get1();
    System.out.println("all loonies is " + allLoonies);

    fives = cash.get5();
    System.out.println(fives);
  }
}
