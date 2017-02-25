/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analyse;

/**
 *
 * @author David
 */
public enum ResultEnum {
    IntNumber,
    UnsignedIntNumber,
    LongNumber,
    UnsignedLongNumber,
    FloatNumber,
    DoubleNumber,
    ZipCode,
    PhoneNumber,
    StreetAddress,
    CreditCard,
    BirdthNumber,
    Url,
    Gtin,
    Gps,
    Date,
    StringText,
    IsNull;
    
    private int count = 0;

    public int getCount() {
        return this.count;
    }

    public void addCount() {
        this.count++;
        //System.out.println("adding: " + this);
    }
    
    public static void resetCount(){
        for (ResultEnum re : ResultEnum.values()){
            re.count = 0;
        }
    }
    
}
