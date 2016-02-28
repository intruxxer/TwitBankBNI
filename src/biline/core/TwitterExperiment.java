package biline.core;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TwitterExperiment {

	public TwitterExperiment() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		Date currentDate            	 = new Date();
		
		SimpleDateFormat dateDigit		 = new SimpleDateFormat("dd");
		SimpleDateFormat monthDigit		 = new SimpleDateFormat("MM");
		SimpleDateFormat yearDigit		 = new SimpleDateFormat("yyyy");
		
		Date convertedValidDate = null;
		try {
				//Change from String to Date() by Parsing the String of Date.
				DateFormat parser  = new SimpleDateFormat("yyyy-MM-dd");
				convertedValidDate = (Date) parser.parse("2016-02-27");
		} catch (ParseException pe) {
				pe.printStackTrace();
		}
		
		String valid_date_digit  = dateDigit.format(convertedValidDate);
        String valid_month_digit = monthDigit.format(convertedValidDate);
        String valid_year_digit  = yearDigit.format(convertedValidDate);
		String[] strMonths = new String[] { "Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember" };
		System.out.println("Date in Indonesia: " + valid_date_digit + " " + strMonths[Integer.parseInt(valid_month_digit)-1] + " " + valid_year_digit);

	}

}
