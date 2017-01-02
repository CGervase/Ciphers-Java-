import java.util.Scanner;

public class Cipher {
  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);
    System.out.println("Please indicate the cipher you wish to use.\n1: Caesar\n2: Atbash\n3: Affine\n4: Vigenère");
    int cipher = valInt(1, 4);
    System.out.println("\nWould you like to encrypt or decrypt?\n1: Encrypt\n0: Decrypt");
    int mode = valInt(0, 1);
    if (mode == 0)
      mode--;
    System.out.println("Please enter the text: ");
    String text = input.nextLine();
    char[] letters = text.toUpperCase().toCharArray();
    
    if (cipher == 1)
      caesar(letters, mode);
    else if (cipher == 2)
      atbash(letters);
    else if (cipher == 3)
      affine(letters, mode);
    else if (cipher == 4)
      vigenere(letters, mode);
  }
  
  public static int valInt(int min, int max) {
    Scanner input = new Scanner(System.in);
    boolean check = true;
    int num = -1;
    
    do {
      try {
        num = input.nextInt();
        if (num < min || num > max)
          throw new NumberFormatException("Input is out of range.");
        else
          check = false;
      }
      catch (Exception e) {
        System.out.println("Please choose a valid option.");
        System.out.print("");
        input.nextLine();
      }
    } while (check);
    
    return num;
  }
  
  public static void caesar(char[] letters, int mode) {
    System.out.println("Please enter the shift to be used (right for encr, left for decr):");
    int shift = valInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
    shift *= mode;
    int c;
    
    for (int i = 0; i < letters.length; i++) {
      c = letters[i];
      if (c > 64 && c < 91) {
        c = ((c - 65 + shift) % 26 + 26) % 26 + 65;
        letters[i] = (char) c;
      }
    }
    
    String text = new String(letters);
    System.out.println("Result: " + text);
  }
  
  public static void atbash(char[] letters) {
    int c;
    for (int i = 0; i < letters.length; i++) {
      c = letters[i];
      if (c > 64 && c < 91) {
        c = 25 - (c - 65) + 65;
        letters[i] = (char) c;
      }
    }
    
    String text = new String(letters);
    System.out.println("Result: " + text);
  }
  
  public static void affine(char[] letters, int mode) {
    System.out.println("Please enter the multiplicative key to be used:");
    int key = valInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
    System.out.println("Please enter the shift to be used (right for encr, left for decr):");
    int shift = valInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
    int c;
    
    for (int i = 0; i < letters.length; i++) {
      c = letters[i];
      if (c > 64 && c < 91) {
        if (mode > 0) {
          c = ((c - 65) * key + shift) % 26 + 65;
        }
        else {
          int invKey = modInv(key, 26);
          c = ((invKey * (c - 65 - shift)) % 26 + 26) % 26 + 65;
        }
        letters[i] = (char) c;
      }
    }
    
    String text = new String(letters);
    System.out.println("Result: " + text);
  }
  
  public static void vigenere(char[] letters, int mode) {
    Scanner input = new Scanner(System.in);
    System.out.println("Please enter the keyword to be used:");
    String key = input.next();
    char[] keyChars = key.toUpperCase().toCharArray();
    int error = 0;
    int c, shift;
    
    for (int i = 0; i < letters.length; i++) {
      c = letters[i];
      if (c > 64 && c < 91) {
        shift = (keyChars[(i - error) % keyChars.length] - 65) * mode;
        c = ((c - 65 + shift) % 26 + 26) % 26 + 65;
      }
      else
        error++;
      letters[i] = (char) c;
    }
    
    String text = new String(letters);
    System.out.println("Result: " + text);
  }
  
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