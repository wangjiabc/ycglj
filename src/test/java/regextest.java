import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class regextest {

	public static void main(String[] args) {
		
		String REGEX = "region=";
		Pattern pattern=Pattern.compile(REGEX);
		Matcher matcher=pattern.matcher("region=");
		
		System.out.println(matcher.find());
		
	}
	
}
