package bankAccountStorage;

import java.util.ArrayList;

import application.User;

public class test
{
	static ArrayList<User> allUsers;

	public static void main(String[] args)
	{
		allUsers = new ArrayList<User>();
		allUsers.add(new User("Abby", "A", "username", "a@gmail.com", "500", "A", "500"));
		allUsers.add(new User("Barry", "B", "usernameb", "b@gmail.com", "500", "A", "500"));
		allUsers.add(new User("Charlie", "C", "usernamec", "c@gmail.com", "500", "A", "500"));
		allUsers.add(new User("Dave", "D", "usernamed", "d@gmail.com", "500", "A", "500"));
		
		System.out.println(test.findUser("d@gmail.com"));
	}
	
	private static User findUser(String email)
	{
		
		User returnUser = null;
		
		int start = 0;
		int middle = allUsers.size() / 2;
		int end = allUsers.size() - 1;
		
		while(returnUser == null)
		{
			if(email.compareTo(allUsers.get(middle).getEmail()) > 0)
			{
				start = middle + 1;
				middle = start + ((end - start) / 2); 
			}
			else if(email.compareTo(allUsers.get(middle).getEmail()) < 0)
			{
				end = middle - 1;
				middle = (end - start) / 2;
			}
			else
			{
				returnUser = allUsers.get(middle);
			}
			
		}
		
		return returnUser;
		
	}
	
	
}
