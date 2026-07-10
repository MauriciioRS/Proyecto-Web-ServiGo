import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
public class GenPasswords {
  public static void main(String[] args) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    for (String password : args) {
      System.out.println(password + " => " + encoder.encode(password));
    }
  }
}
