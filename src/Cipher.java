import java.util.Scanner;

public class Cipher {
  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);
    System.out.println("Please indicate the cipher you wish to use.\n1: Caesar\n2: Atbash\n3: Affine\n4: Vigenère");
    int cipher = valInt(1, 4);  //integer corresponding to type of cipher
    System.out.println("\nWould you like to encrypt or decrypt?\n1: Encrypt\n0: Decrypt");
    int mode = valInt(0, 1);    //1 for encryption, 0 for decryption
    if (mode == 0)    //if decryption,
      mode--;         //change from 0 to -1 for use in algorithms
    System.out.println("Please enter the text: ");
    String text = input.nextLine();
    char[] letters = text.toUpperCase().toCharArray();    //individual uppercase characters of the input text
    
    //indicate which cipher to use
    if (cipher == 1)
      caesar(letters, mode);
    else if (cipher == 2)
      atbash(letters);
    else if (cipher == 3)
      affine(letters, mode);
    else if (cipher == 4)
      vigenere(letters, mode);
  }
  
  //validates integer within a range (min to max inclusive)
  public static int valInt(int min, int max) {
    Scanner input = new Scanner(System.in);
    boolean check = true; //boolean for try/catch loop
    int num = -1;         //initialize num with placeholder
    
    do {
      try {
        num = input.nextInt();
        if (num < min || num > max)   //number is out of range
          throw new NumberFormatException("Input is out of range.");
        else
          check = false;              //number is in range, exit loop
      }
      catch (Exception e) {
        System.out.println("Please choose a valid option.");
        System.out.print("");
        input.nextLine();
      }
    } while (check);
    
    return num;
  }
  
  //caesar cipher
  public static void caesar(char[] letters, int mode) {
    System.out.println("Please enter the shift to be used (right for encr, left for decr):");
    int shift = valInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
    shift *= mode;    //if decryption, multiplies shift by -1 to reverse shift direction
    int c;
    
    for (int i = 0; i < letters.length; i++) {  //loop for each character in input
      c = letters[i];           //ascii integer of character
      if (c > 64 && c < 91) {   //if character is a letter (A = 65 and Z = 90)
        c = ((c - 65 + shift) % 26 + 26) % 26 + 65;   //reduce letter to 0-25 range, shift letter mod 26, return to 65-90 range
        letters[i] = (char) c;  //return letter from integer to character
      }
    }
    
    String text = new String(letters);
    System.out.println("Result: " + text);
  }
  
  //atbash cipher
  public static void atbash(char[] letters) {
    int c;
    for (int i = 0; i < letters.length; i++) {  //loop for each character in input
      c = letters[i];           //ascii integer of character
      if (c > 64 && c < 91) {   //if character is a letter (A = 65 and Z = 90)
        c = 25 - (c - 65) + 65; //reverses letter in the alphabet (a -> z, b -> y, c -> x, etc.)
        letters[i] = (char) c;  //return letter from integer to character
      }
    }
    
    String text = new String(letters);
    System.out.println("Result: " + text);
  }
  
  //affine cipher
  public static void affine(char[] letters, int mode) {
    System.out.println("Please enter the multiplicative key to be used:");
    int key = valRelPrime(26);   //multiplicative key
    System.out.println("Please enter the shift to be used (right for encr, left for decr):");
    int shift = valInt(Integer.MIN_VALUE, Integer.MAX_VALUE); //magnitude of shift 
    int c;
    
    for (int i = 0; i < letters.length; i++) {
      c = letters[i];         //ascii integer of character
      if (c > 64 && c < 91) { //if character is a letter (A = 65 and Z = 90)
        if (mode > 0) {       //encryption
          c = ((c - 65) * key + shift) % 26 + 65; //reduce letter to 0-25 range, multiply by key, shift mod 26, return to 65-90 range
        }
        else {    //decryption (mode is -1)
          int invKey = modInv(key, 26);     //modular inverse of key
          c = ((invKey * (c - 65 - shift)) % 26 + 26) % 26 + 65;  //reduce letter to 0-25, shift * multiply by key, mod 26, return to 65-90
        }
        letters[i] = (char) c;    //return letter from integer to character
      }
    }
    
    String text = new String(letters);
    System.out.println("Result: " + text);
  }
  
  //vigenere cipher
  public static void vigenere(char[] letters, int mode) {
    Scanner input = new Scanner(System.in);
    System.out.println("Please enter the keyword to be used:");
    String key = input.next();    //key to be used
    char[] keyChars = key.toUpperCase().toCharArray();  //individual uppercase characters of key
    int error = 0;    //count of non-letter characters
    int c, shift;
    
    for (int i = 0; i < letters.length; i++) {
      c = letters[i];           //ascii integer of character
      if (c > 64 && c < 91) {   //if character is a letter (A = 65 and Z = 90)
        shift = (keyChars[(i - error) % keyChars.length] - 65) * mode;    //ascii integer of designated character in key
        c = ((c - 65 + shift) % 26 + 26) % 26 + 65;            //reduce letter to 0-25 range, shift letter mod 26, return to 65-90 range
      }
      else          //character is not a letter
        error++;    //increment counter so correct letter in key is used
      letters[i] = (char) c;  //return letter from integer to character
    }
    
    String text = new String(letters);
    System.out.println("Result: " + text);
  }
  
  //greatest common divisor, using Euclid's algorithm
  public static int gcd(int x, int y) {
    int z;
    
    while (y != 0) {
      z = x;
      x = y;
      y = z % y + y % y;
    }
    
    return x;
  }
  
  //ensures key is coprime to 26
  public static int valRelPrime (int b) {
    Scanner input = new Scanner(System.in);
    boolean check = true;   //boolean for try/catch loop
    int a = -1;             //initialized with placeholder
    
    do {
      try {
        a = valInt(Integer.MIN_VALUE, Integer.MAX_VALUE); //validates integer
        if (gcd(a, b) != 1)       //if key is not coprime to 26
          throw new NumberFormatException("Input is not coprime with " + b + ".");
        else
          check = false;          //number is in range, exit loop
      }
      catch (Exception e) {
        System.out.println("Please choose an integer that is coprime with " + b + ".");
        System.out.print("");
      }
    } while (check);
    
    return a;
  }
  
  //modular inverse algorithm
  public static int modInv(int a, int m) {
    int v = 1;
    int d = a;
    int u = (a == 1) ? 1 : 0;
    int t = 1 - u;
    
    if (t == 1) {
      int c = m % a;
      u = (int) Math.floor(m / a);
      while (c != 1 && t == 1) {
        int q = (int) Math.floor(d / c);
        d = (d % c) + c % c;
        v += (q * u);
        t = (d != 1) ? 1 : 0;
        if (t == 1) {
          q = (int) Math.floor(c / d);
          c = (c % d) + d % d;
          u += (q * v);
        }
      }
      u = v * (1 - t) + t * (m - u);
    }
    return u;
  }
}
