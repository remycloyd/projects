package AmazonInterview;

public class reverseAlphabetCharsOnly 
{
	public static void main(String[] args) 
	{
		reverser("2je3re$my1");
	}
	static void reverser(String inputString)
	{
		char[] inputChar = inputString.toCharArray();
		int right = inputString.length() -1;
		int left = 0;
		while (left < right)
		{
			if (!Character.isAlphabetic(inputChar[left])) // if left pointer doesn't point at a letter, increment pointer
				left++;
			else if(!Character.isAlphabetic(inputChar[right])) // if right pointer doesn't point at a letter, decrement pointer
				right--;
			else // POINTING AT TWO LETTERS 
			{	// REVERSE LOGIC
				char temp = inputChar[left];  
				inputChar[left] = inputChar[right]; 
				inputChar[right] = temp;
				left++;
				right--;
			}
//			left++;  WAS LOCATED HERE. INCREMENTATION OF POINTERS HAPPENED NO MATTER WHAT WAS FOUND. 
//			right--;	
		}
		System.out.print(new String(inputChar));
	}

}
//fix all logical errors in code, function reverseAlpha accepts one string argument the function returns a string representing the reversed string
// in such a way that the positions of the special chars are not affected.