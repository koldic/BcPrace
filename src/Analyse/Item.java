/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analyse;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
/**
 *
 * @author David
 */
public class Item {
    private final String value;
    private final String parentKey;
    private int lenght;
    private String dateFormat;
    private Locale dateLocal;
    private List<DateFormatCount> dateFormatCount = null;
    private static List<String> dateFormats = null;
    private static List<String> zipPatterns = null;

    private boolean notNull = true;
    private boolean isInt = false;
    private boolean isUnsignedInt = false;
    private boolean isLong = false;
    private boolean isUnsignedLong = false;
    private boolean isDouble = false;
    private boolean isFloat = false;
    private boolean checkDate = true;
    private boolean isDate = false;
    private boolean isZipCode = false;
    private boolean isPhoneNumber = false;
    private boolean checkGPS = true;
    private boolean isGPS = false;
    private boolean isCreditCardNumber = false;
    private boolean isBirdthNumber = false;
    private boolean isStreetAddress = false;
    private boolean isGtin = false;
    private boolean isUrl = false;
    private boolean isString = false;
    private boolean isPossibleId = false;
    
    public Item(String key, String val) {
        //System.out.println("checking: " + key + "is empty?");
        if (val == null || val.isEmpty()){
            this.value = null;
            this.notNull = false;
            this.lenght = 0;
        }
        
        else {
            this.value = val;
            this.lenght = value.length();
        }
        
        this.parentKey = key;
        
        //System.out.println(this.parentKey +" value: "+ this.value);
    }
    
    public Item(String value) {
        
        if (value.isEmpty()){
            this.value = null;
            this.notNull = false;
        }
        
        else {
            this.value = value;
        }
        
        this.parentKey = null;
        this.lenght = value.length();
    }
    
    
    
    public String getValue(){
        return this.value;
    }
    
    public String getParent(){
        return this.parentKey;
    }

    public boolean isNotNull() {
        return notNull;
    }

    public int getLenght() {
        return lenght;
    }

    public Locale getDateLocal() {
        return dateLocal;
    }

    public boolean isIsInt() {
        return isInt;
    }

    public boolean isIsUnsignedInt() {
        return isUnsignedInt;
    }

    public boolean isPossibleId() {
        return isPossibleId;
    }

    public boolean isIsDate() {
        return isDate;
    }

    public void setLenght(int lenght) {
        this.lenght = lenght;
    }
    
    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }

    public void setId(boolean possibleId) {
        this.isPossibleId = possibleId;
    }
    
    
    
    public void ChecTypes(){
        if (this.notNull){
            //System.out.println("value: " + this.value +" key: " + this.parentKey);
        
            this.chechkInt();
            this.chechkUnsignedInt();
            this.chechkLong();
            this.chechkUnsignedLong();
            this.chechkFloat();
            this.chechkDouble();
            this.checkZipCode();
            this.checkPhoneNumber();
            this.checkStreetAddress();
            this.checkCreditCard();
            this.checkBirdthNumber();
            this.checkGtin();
            this.checkUrl();
            
            if (this.checkGPS){
                this.checkGps();
            }

            if (this.checkDate && this.value.matches(".*[0-9].*.*[0-9].*")){
                this.chechkDate("en"); // US date format first - en
            }
        }
    }
    
    public  Map<ItemType, Boolean> provideResultField(){
        Map<ItemType, Boolean> resultField = new EnumMap<>(ItemType.class);
        
        resultField.put(ItemType.BirdthNumber, this.isBirdthNumber);
        resultField.put(ItemType.CreditCard, this.isCreditCardNumber);
        resultField.put(ItemType.Date, this.isDate);
        resultField.put(ItemType.DoubleNumber, this.isDouble);
        resultField.put(ItemType.FloatNumber, this.isFloat);
        resultField.put(ItemType.Gps, this.isGPS);
        resultField.put(ItemType.Gtin, this.isGtin);
        resultField.put(ItemType.IntNumber, this.isInt);
        resultField.put(ItemType.LongNumber, this.isLong);
        resultField.put(ItemType.PhoneNumber, this.isPhoneNumber);
        resultField.put(ItemType.StreetAddress, this.isStreetAddress);
        resultField.put(ItemType.UnsignedIntNumber, this.isUnsignedInt);
        resultField.put(ItemType.UnsignedLongNumber, this.isUnsignedLong);
        resultField.put(ItemType.Url, this.isUrl);
        resultField.put(ItemType.ZipCode, this.isZipCode);
        resultField.put(ItemType.StringText, this.isString);
        resultField.put(ItemType.IsNull, (!this.notNull));
        
        if(!this.isString){
            boolean allFalse = false;
            for (ItemType itemType : resultField.keySet()){
                if (itemType != ItemType.IsNull){
                    allFalse = allFalse | resultField.get(itemType);
                }
            }
            resultField.replace(ItemType.StringText, allFalse);
        }
            
        return resultField;
    }
  
    
    // max 11 characters (10 digits and +/-)
    private void chechkInt(){
        try{
            Integer.parseInt(value);
            this.isInt = true;
            this.checkDate = false;
            this.checkGPS = false;
        }catch(NumberFormatException e){
            //System.out.println("Not Int" + this.lenght);
        }
    }
    
    // max 10 digits
    private void chechkUnsignedInt(){
        try{
            Integer.parseUnsignedInt(value);
            this.isUnsignedInt = true;
            this.checkDate = false;
            this.checkGPS = false;
        }catch(NumberFormatException e){
            //System.out.println("Not Unsigned Int" + this.lenght);
        }
    }
    
    // max 20 characters (19 digits and +/-)
    private void chechkLong(){
        try{
            Long.parseLong(value);
            this.isLong = true;
            this.checkDate = false;
            this.checkGPS = false;
        }catch(NumberFormatException e){
            //System.out.println("Not Long" + this.lenght);
        }
    }
    
    // max 19 digits
    private void chechkUnsignedLong(){
        try{
            Long.parseUnsignedLong(value);
            this.isUnsignedLong = true;
            this.checkDate = false;
            this.checkGPS = false;
        }catch(NumberFormatException e){
            //System.out.println("Not Unsigned Long" + this.lenght);
        }
    }
    
    // range 1.7e–308 to 1.7e+308 - size 8bit
    private void chechkDouble(){
        try{
            Double.parseDouble(value);
            this.isDouble = true;
            this.checkDate = false;
            this.checkGPS = false;
        }catch(NumberFormatException e){
            //System.out.println("Not double - English" + this.lenght);
        }
        try{
            String tmp = this.value;
            tmp = tmp.replaceAll(",",".");
            
            Double.parseDouble(tmp);
            this.isDouble = true;
            this.checkDate = false;
            this.checkGPS = false;
        } catch (NumberFormatException e) {
            //System.out.println("Not double - Czech" + this.lenght);
        }
    }
    
    // range from 3.4e–038 to 3.4e+038 - size 4bit
    private void chechkFloat(){
        try{
            Float.parseFloat(value);
            this.isFloat= true;
            this.checkDate = false;
            this.checkGPS = false;
        }catch(NumberFormatException e){
            //System.out.println("Not float - English" + this.lenght);
        }
        try{
            String tmp = this.value;
            tmp = tmp.replaceAll(",",".");
            
            Float.parseFloat(tmp);
            this.isFloat = true;
            this.checkDate = false;
            this.checkGPS = false;
        } catch (NumberFormatException e) {
            //System.out.println("Not float - Czech" + this.lenght);
        }
    }
    
    
    private void chechkDate(String languageTag){
        if (Item.dateFormats == null) {
            this.createDateFormats();
        }
        
        DateTimeFormatter formatter;
        
        for (String dFormat : Item.dateFormats){
            
            Locale locale = Locale.forLanguageTag(languageTag); 
            formatter = DateTimeFormatter.ofPattern(dFormat, locale); //.forLanguageTag("cs-CZ") Locale.getDefault(
            
            try {
                LocalDate date = LocalDate.parse(value, formatter);
                this.isDate = true;
                this.checkDate = false;
                this.isString = false;
                this.dateFormat = dFormat;
                this.dateLocal = locale;
                //System.out.println("is date" + dFormat);
                break;
            } catch (DateTimeParseException ex) {
                this.isString = true;
                //System.out.println("Not Date - format: " + dFormat);
            }
        }
        
        if (this.checkDate){
            //System.out.println("Checking Czech dates");
            this.checkDate = false;
            chechkDate("cs-CZ");
        }
        //System.out.println(Locale.ENGLISH.toLanguageTag());
    }
    
    private void createDateFormats(){
        Item.dateFormats = new ArrayList<>();
        //System.out.println("Creating formats...");
        dateFormats.add("MMMM yyyy");
        dateFormats.add("d/M/y");                                   //1/4/2016
        dateFormats.add("d.M.y");                                   //1.4.2016
        dateFormats.add("d M y");                                   //1 4 2016
        dateFormats.add("d-M-y");                                   //1-4-2016
        dateFormats.add("M/d/y");                                   //6/30/2014
        dateFormats.add("M.d.y");                                   //6.30.2014
        dateFormats.add("M d y");                                   //6 30 2014
        dateFormats.add("M-d-y");                                   //6-30-2014
        dateFormats.add("y/M/d");                                   //2014/3/20
        dateFormats.add("y.M.d");                                   //2014.3.20
        dateFormats.add("y M d");                                   //2014 3 20
        dateFormats.add("y-M-d");                                   //2014-3-20
        dateFormats.add("y/d/M");                                   //2014/20/3
        dateFormats.add("y.d.M");                                   //2014.20.3
        dateFormats.add("y d M");                                   //2014 20 3
        dateFormats.add("y-d-M");                                   //2014-20-3
        dateFormats.add("d.M.y HH:mm:ss");                          //4.1.2001 12:08:56
        dateFormats.add("d M y HH:mm:ss");                          //4 1 2001 12:08:56
        dateFormats.add("d/M/y HH:mm:ss");                          //4/1/2001 12:08:56
        dateFormats.add("M.d.y HH:mm:ss");                          //4.30.2001 12:08:56
        dateFormats.add("M d y HH:mm:ss");                          //4 30 2001 12:08:56
        dateFormats.add("M/d/y HH:mm:ss");                          //1/30/2001 12:08:56
        dateFormats.add("M.d.y h:mm:ss a");                         //06.01.2000 10:01:50 AM
        dateFormats.add("M d y h:mm:ss a");                         //06 01 2000 10:01:50 AM
        dateFormats.add("M/d/y h:mm:ss a");                         //06/01/2000 10:01:50 AM
        dateFormats.add("d.M.y h:mm:ss a");                         //30.01.2000 10:01:50 AM
        dateFormats.add("d M y h:mm:ss a");                         //30 01 2000 10:01:50 AM
        dateFormats.add("d/M/y h:mm:ss a");                         //30/01/2000 10:01:50 AM
        dateFormats.add("d.M.y HH:mm:ss Z");                        //4.1.2001 12:08:56 -0700
        dateFormats.add("d M y HH:mm:ss Z");                        //4 1 2001 12:08:56 -0700
        dateFormats.add("d/M/y HH:mm:ss Z");                        //4/1/2001 12:08:56 -0700
        dateFormats.add("M.d.y HH:mm:ss Z");                        //4.30.2001 12:08:56 -0700
        dateFormats.add("M d y HH:mm:ss Z");                        //4 30 2001 12:08:56 -0700
        dateFormats.add("M/d/y HH:mm:ss Z");                        //4/30/2001 12:08:56 -0700
        dateFormats.add("MMM d, yyyy");                             //Jun 1, 2014                   Čer 1, 2014
        dateFormats.add("MMMM d, yyyy");                            //June 30, 2014                 Června 30, 2014
        dateFormats.add("MMMM d. yyyy");                            //June 30. 2014                 Června 30. 2014
        dateFormats.add("d. MMMM yyyy");                            //30. June 2014                 28. září 2015
        dateFormats.add("eeee, MMMM d, yyyy");                      //Monday, June 30, 2014         Pondělí, června 30, 2014
        dateFormats.add("eeee, MMM d, yyyy");                       //Monday, Jun 30, 2014          Pondělí, Čer 30, 2014
        dateFormats.add("eee, MMM d, yyyy");                        //Mo, Jun 30, 2014              Po, Čer 30, 2014
        dateFormats.add("eee, MMMM d, yyyy");                       //Mo, June 30, 2014             Po, června 30, 2014
        dateFormats.add("eee, d MMM yyyy HH:mm:ss Z");              //Wed, 4 Jul 2001 12:08:56 -0700
        dateFormats.add("d-MMMM-y");                                //01-June-14
        dateFormats.add("d-MMM-y");                                 //01-Jun-2014
        dateFormats.add("yyyy.MM.dd G 'at' HH:mm:ss z");            //2001.07.04 AD at 12:08:56 PDT
        dateFormats.add("eee, MMM d, ''yy");                        //Wed, Jul 4, '01
        //dateFormats.add("hh:mm a");                               //12:08 PM
        //dateFormats.add("hh 'o''clock' a, zzzz");                 //12 o'clock PM, Pacific Daylight Time
        //dateFormats.add("K:mm a, z");                             //0:08 PM, PDT
        //dateFormats.add("yyyyy.MMMMM.dd GGG hh:mm a");            //02001.July.04 AD 12:08 PM
        dateFormats.add("'Month' q 'in' QQQ");                      //Month 2 in Q2
        dateFormats.add("yyMMddHHmmssZ");                           //010704120856-0700
        dateFormats.add("yyyy-MM-dd'T'HH:mm:ss.SSSZ");              //2001-07-04T12:08:56.235-0700
        dateFormats.add("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");            //2001-07-04T12:08:56.235-07:00
        dateFormats.add("YYYY-'W'ww-u");                            //2001-W27-3
        //System.out.println(ObjectSizeCalculator.getObjectSize(Item.dateFormats));
    }
    
    private void checkZipCode() {
        
        if (Item.zipPatterns == null) {
            Item.zipPatterns = new ArrayList<>();
        }
        
        Item.zipPatterns.add("\\d{3}( \\d{2})?|(\\d{5})?");       // Czech - 747 28 or 74728
        Item.zipPatterns.add("^[a-zA-Z]{1,2}[0-9][0-9A-Za-z]{0,1} {0,1}[0-9][A-Za-z]{2}$");         //UK - NP26 3PS
        Item.zipPatterns.add("^\\d{5}\\p{Punct}?\\s?(?:\\d{4})?$");                                 //USA - 63405-8926
        
        for (String pattern : Item.zipPatterns){
            
            if(value.matches(pattern)){
                //System.out.println(pattern);
                this.isZipCode = true;
                this.checkDate = false;
                this.checkGPS = false;
                break;
            }
        }
    }
    
    private void checkPhoneNumber() {
        
        if(value.matches("^(\\+420)? ?[1-9][0-9]{2} ?[0-9]{3} ?[0-9]{3}$")){ // only Czech numbers (+420)774855182
            //System.out.println("Telefon sedí");
            this.isPhoneNumber = true;
            this.checkDate = false;
        } 
    }
    
    private void checkStreetAddress() {
        
        if(value.matches("^(.*[^0-9]+) (([1-9][0-9]*))?/([1-9][0-9]*[a-cA-C]?)$")){     //Sladovní 103/3    or  Mlýnská 466/86
            //System.out.println("Ulice");
            this.isStreetAddress = true;
            this.checkDate = false;
            this.checkGPS = false;
        } 
    }
    
    private void checkGps(){
        
        // 50.081819602, 14.44152832
        
        if(value.matches("^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?),\\s*[-+]?(180(\\.0+)?|((1[0-7]\\d)|([1-9]?\\d))(\\.\\d+)?)$")){     //"([SNsn][\\s]*)?((?:[\\+-]?[0-9]*[\\.,][0-9]+)|(?:[\\+-]?[0-9]+))(?:(?:[^ms'′\"″,\\.\\dNEWnew]?)|(?:[^ms'′\"″,\\.\\dNEWnew]+((?:[\\+-]?[0-9]*[\\.,][0-9]+)|(?:[\\+-]?[0-9]+))(?:(?:[^ds°\"″,\\.\\dNEWnew]?)|(?:[^ds°\"″,\\.\\dNEWnew]+((?:[\\+-]?[0-9]*[\\.,][0-9]+)|(?:[\\+-]?[0-9]+))[^dm°'′,\\.\\dNEWnew]*))))([SNsn]?)[^\\dSNsnEWew]+([EWew][\\s]*)?((?:[\\+-]?[0-9]*[\\.,][0-9]+)|(?:[\\+-]?[0-9]+))(?:(?:[^ms'′\"″,\\.\\dNEWnew]?)|(?:[^ms'′\"″,\\.\\dNEWnew]+((?:[\\+-]?[0-9]*[\\.,][0-9]+)|(?:[\\+-]?[0-9]+))(?:(?:[^ds°\"″,\\.\\dNEWnew]?)|(?:[^ds°\"″,\\.\\dNEWnew]+((?:[\\+-]?[0-9]*[\\.,][0-9]+)|(?:[\\+-]?[0-9]+))[^dm°'′,\\.\\dNEWnew]*))))([EWew]?)"
            System.out.println("GPS");
            this.isGPS = true;
            //this.checkDate = false;
            this.isString = false;
        } 
        // 50° 4' 54.5505674" N or 12°30'23.256547\"E
        else if(value.matches("[0-9]{1,2}[:|°]? ?[0-9]{1,2}[:|']? ?(?:\\b[0-9]+(?:\\.[0-9]*)?|\\.[0-9]+\\b)\"? ?((N)|(S)|(E)|(W))?")){
            System.out.println("GPS - N or E or S or W");
            this.isGPS = true;
            //this.checkDate = false;
            this.isString = false;
        }
        else{
            this.isString = true;
        }
    }
    
    private void checkCreditCard(){
        String tmp = this.value.replaceAll("-", "");
        tmp = tmp.replaceAll(" ", "");
        // Visa - 4024007146051387
        if(tmp.matches("^4[0-9]{12}(?:[0-9]{3})?$")){     
            //System.out.println("Credit Card - Visa");
            this.isCreditCardNumber = true;
            this.checkDate = false;
            this.checkGPS = false;
        } 
        // MasterCard - 5294336448055352
        else if(tmp.matches("^5[1-5][0-9]{14}$")){
            //System.out.println("Credit Card - MasterCard");
            this.isCreditCardNumber = true;
            this.checkDate = false;
            this.checkGPS = false;
        }
    }
    
    private void checkBirdthNumber(){
        
        // Validation since 1. 1. 1954 - example 070421/0001
        if(value.matches("^[0-9]{2}(([0,2,5,7]{1}[1-9]{1})|([1,3,6,8]{1}[0-2]{1}))(([0]{1}[1-9]{1})|([1,2]{1}[0-9]{1})|([3]{1}[0-1]{1}))[/ ]?[0-9]{3,4}$")){     //^[0-9]{2}(([01-12]{2}))[01-31]{2}[/ ]?[0-9]{3,4}$
            String tmp = this.value.replaceAll("/", "");
            tmp = tmp.replaceAll(" ", "");
            if (Long.parseLong(tmp)%11 == 0){
                //System.out.println("Birdth Number Valid");
                this.isBirdthNumber = true;
                this.checkDate = false;
                this.checkGPS = false;
            }
        } 
    }
    
    // GTIN - 10614141000415 UPC - 501234567890 EAN - 00000000000130 or 4011200296908
    private void checkGtin()
    {
        if(value.matches("^(\\d{8}|\\d{12,14})$")){
            int l = this.lenght - 1;
            int checksum = 0;
            int weight;
            int val;
            String v = this.value;

            for (int i = 0; i < l; i++) {
                val = v.charAt(i) - '0';
                weight = i % 2 == 0 ? 1 : 3;
                checksum += val * weight;
            }
            int chk = 10 - checksum % 10;

            char ch = (char)('0' + chk);
            v = v.substring(0, l) + ch;

            if (chk == (v.charAt(l) - '0')) {
                this.isGtin = true;
                this.checkDate = false;
                this.checkGPS = false;
                //System.out.println("GTIN Valid");
            }
        }
    }
    
    private void checkUrl(){
        
        // http://cdn2.gossipcenter.com/sites/default/files/imagecache/story_header/photos/tom-cruise-020514sp.jpg
        if(value.matches("^(http|https|ftp)\\://([a-zA-Z0-9\\.\\-]+(\\:[a-zA-Z0-9\\.&amp;%\\$\\-]+)*@)?((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])|([a-zA-Z0-9\\-]+\\.)*[a-zA-Z0-9\\-]+\\.[a-zA-Z]{2,4})(\\:[0-9]+)?(/[^/][a-zA-Z0-9\\.\\,\\?\\'\\\\/\\+&amp;%\\$#\\=~_\\-@]*)*$")){
            //System.out.println("Valit url");
            this.isUrl = true;
            this.checkDate = false;
            this.checkGPS = false;
        }  
    }
    
    
    public void setProperties(ResultEnum resultEnum){

        if(resultEnum == ResultEnum.StringText){
            this.isString = true;
        }
        else if(resultEnum == ResultEnum.IntNumber){
            this.isInt = true;
        }
        else if(resultEnum == ResultEnum.UnsignedIntNumber){
            this.isUnsignedInt = true;
        }
        else if(resultEnum == ResultEnum.DoubleNumber){
            this.isDouble = true;
        }
        else if(resultEnum == ResultEnum.LongNumber){
            this.isLong = true;
        }
        else if(resultEnum == ResultEnum.UnsignedLongNumber){
            this.isUnsignedLong = true;
        }
        else if(resultEnum == ResultEnum.FloatNumber){
            this.isFloat = true;
        }
        else if(resultEnum == ResultEnum.BirdthNumber){
            this.isBirdthNumber = true;
        }
        else if(resultEnum == ResultEnum.CreditCard){
            this.isCreditCardNumber = true;
        }
        else if(resultEnum == ResultEnum.Date){
            this.isDate = true;
        }
        else if(resultEnum == ResultEnum.Gps){
            this.isGPS = true;
        }
        else if(resultEnum == ResultEnum.Gtin){
            this.isGtin = true;
        }
        else if(resultEnum == ResultEnum.PhoneNumber){
            this.isPhoneNumber = true;
        }
        else if(resultEnum == ResultEnum.StreetAddress){
            this.isStreetAddress = true;
        }
        else if(resultEnum == ResultEnum.ZipCode){
            this.isZipCode = true;
        }
        else if(resultEnum == ResultEnum.Url){
            this.isUrl = true;
        }
    
    }
    
    // used only in Analyzer Storage to count different Date types
    public void setDateFormatCount(String dFormat, Locale local) {
        if (this.dateFormatCount == null){
            this.dateFormatCount = new ArrayList<>();
            System.out.println(dFormat);
            this.dateFormatCount.add(new DateFormatCount(dFormat, local));
        }
        else{
            boolean missingFormat = false;
            for (DateFormatCount item : this.dateFormatCount){
                if (item.getDateFormat().equals(dFormat)){
                    item.adddDateFormatCount();
                    missingFormat = false;
                    break;
                }
                else{
                    missingFormat = true;
                }
            }
            if (missingFormat){
                this.dateFormatCount.add(new DateFormatCount(dFormat, local));
            }
        }
        
    }

    public List<DateFormatCount> getDateFormatCount() {
        if (this.dateFormatCount == null){
            return null;
        }
        
        return dateFormatCount;
    }

    public String getDateFormat() {
        return dateFormat;
    }
       
}


