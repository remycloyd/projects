package AmazonInterview;

public class solution 
{
	public static void main(String[] args) 
	{
		calculateSumOfNumbersInString("19881129");
	}
	static void calculateSumOfNumbersInString(String inputString) 
	{
		String temp = "";
		int sum = 0;
		for(int i = 0; i< inputString.length(); i++)
		{
			char ch = inputString.charAt(i);	// point at individual character
			if(Character.isDigit(ch))			// if that character is a digit
				{
				temp += ch;						// save to temp						
				sum += Integer.parseInt(temp);  // increment sum
				temp = "0";      				// reset temp
				}
		}
		System.out.print(sum + Integer.parseInt(temp));
	}
}
//function should return a positive integer representing the sum of the numbers in the input string.
// the sequence of the consecutive digits is considered as one number 