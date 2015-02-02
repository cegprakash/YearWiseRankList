public class User implements Comparable<User>{
	int rank;
	String userName;
	String name;
	String email;
	int problems_count;
	String college;
	String year;
	
	@Override
	public int compareTo(User other) {
		int yearMe = getYear(year);
		int yearOther = getYear(other.year);
		if(yearMe == yearOther)
			return rank-other.rank;
		return yearMe-yearOther;
	}
	
	public int getYear(String S)
	{
		if(S.compareTo("I")==0)
			return 1;
		else if(S.compareTo("II")==0)
			return 2;
		else if(S.compareTo("III")==0)
			return 3;
		else if(S.compareTo("IV")==0)
			return 4;
		return 1000;
	}
}