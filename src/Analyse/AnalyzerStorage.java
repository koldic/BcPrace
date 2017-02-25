/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analyse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author David
 */
public class AnalyzerStorage{
    private List<Item> items;
    private final String key;
    private int count = 0;
    private final int limit;
    private boolean limitReached;

    public AnalyzerStorage(String key, int limit) {
        this.items = new ArrayList<>();
        this.key = key;
        this.limit = limit;
        this.limitReached = false;
    }
    
    public void AddToStorage(String value){
        if (count < limit){
            items.add(new Item (this.key, value));
            count++;
        }
        else {
            this.limitReached = true;
        }
    }
    
    public void AddToStorage(String parent, String value){
        if (count < limit){
            items.add(new Item (parent, value));
            count++;
        }
        else {
            this.limitReached = true;
        }
    }

    
    public boolean isLimitReached() {
        return limitReached;
    }

    public String getKey() {
        return key;
    }

    public int getCount() {
        return count;
    }
    
    public Item getItemInPosition(int index){
        return items.get(index);
    }
    
    public boolean storageNotEmpty(){
        return !(this.items.isEmpty() || this.items == null); //????
    }
    
    public void checkTypes(){
        for (Item item : this.items){
            
            if (item.isNotNull()){
                item.ChecTypes();
            }
        }
    }
    
    public Item sumResults(){
        Item finalItem = new Item (this.key, this.key);
        Map<ItemType, Boolean> resultField;
        ResultEnum rsEnum = null;
        ResultEnum.resetCount();
        //rsEnum.resetCount();
        int maxLenght = 0;
        
        for (Item item : this.items){
            //System.out.println("key: " + item.getParent() + " value: " + item.getValue());
            if (item.getLenght() > maxLenght){
                maxLenght = item.getLenght();
            }
            
            resultField = item.provideResultField();
            
            if(resultField.get(ItemType.IsNull)){
                rsEnum.IsNull.addCount();
            }
            
            if(resultField.get(ItemType.StringText)){
                rsEnum.StringText.addCount();
            }
            else{
                
                if(resultField.get(ItemType.CreditCard)){
                    rsEnum.CreditCard.addCount();
                }
                if(resultField.get(ItemType.Gtin)){
                    rsEnum.Gtin.addCount();
                }
                if(resultField.get(ItemType.PhoneNumber)){
                    rsEnum.PhoneNumber.addCount();
                }
                if(resultField.get(ItemType.ZipCode)){
                    rsEnum.ZipCode.addCount();
                }
                if(resultField.get(ItemType.UnsignedIntNumber)){
                    rsEnum.UnsignedIntNumber.addCount();
                }
                else if(resultField.get(ItemType.IntNumber)){
                    rsEnum.IntNumber.addCount();
                }
                else {
                    
                    if(resultField.get(ItemType.UnsignedLongNumber)){
                        rsEnum.UnsignedLongNumber.addCount();
                    }
                    else if(resultField.get(ItemType.LongNumber)){
                        rsEnum.LongNumber.addCount();
                    }
                    else{
                        
                        if(resultField.get(ItemType.DoubleNumber)){
                            rsEnum.DoubleNumber.addCount();
                        }
                        if(resultField.get(ItemType.FloatNumber)){
                            rsEnum.FloatNumber.addCount();
                        }
                        if(resultField.get(ItemType.BirdthNumber)){
                            rsEnum.BirdthNumber.addCount();
                        }
                        if(resultField.get(ItemType.Date)){
                            rsEnum.Date.addCount();
                            finalItem.setDateFormatCount(item.getDateFormat(), item.getDateLocal());
                        }
                        if(resultField.get(ItemType.Gps)){
                            rsEnum.Gps.addCount();
                        }
                        if(resultField.get(ItemType.StreetAddress)){
                            rsEnum.StreetAddress.addCount();
                        }
                        if(resultField.get(ItemType.Url)){
                            rsEnum.Url.addCount();
                        }
                    }
                    
                }
            }
        }
        //System.out.println("value: " + this.key+ " " + rsEnum.IntNumber.getCount());
        finalItem.setLenght(maxLenght);
        for (ResultEnum rs : ResultEnum.values()){
            
            if (rs != null){
                
                if (rs.getCount() > 0 && ((double)(rs.getCount() / this.count) > 0.5)){                    
                    finalItem.setProperties(rs);
                }
                
                if (rs.getCount() > 0 && ((double)(rs.getCount() / this.count) > 0.05) && (rs == ResultEnum.IsNull)){
                    finalItem.setNotNull(false);
                }
            }                
        }
        
        if (finalItem.isIsInt() || finalItem.isIsUnsignedInt()){
            
            List values = Arrays.asList(this.getValues());
            Set inputSet = new HashSet(values);

            if(inputSet.size() == values.size()){
                finalItem.setId(true);
                //System.out.println("unique " + values);
            }
        }

        return finalItem;
    }
    
    public boolean compareKey(String compartedKey){
        return this.key.equals(compartedKey); //???
    }
    
    private String[] getValues(){
        String[] values = new String[items.size()];
        for(int i=0;i<items.size();i++) {
            values[i]= items.get(i).getValue();
        }
        return values;
    }
    
}
