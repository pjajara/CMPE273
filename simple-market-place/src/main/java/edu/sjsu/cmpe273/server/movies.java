package edu.sjsu.cmpe273.server;

public class movies {
private int sum_issued_movies;
	
	public movies()
	{
		System.out.println("Created a movie");
		sum_issued_movies=0;
	}
	
	public int issuemovie()
	{
		sum_issued_movies++;
		if(sum_issued_movies % 100 == 0)
		{
			System.out.println("sum of issued movies="+sum_issued_movies);
		}
		return sum_issued_movies;
	}
	
	
	
	public int getsum_issued_movies()
	{
		return sum_issued_movies;
	}
}
