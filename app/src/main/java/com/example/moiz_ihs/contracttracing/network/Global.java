package com.example.moiz_ihs.contracttracing.network;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Global {
    public static String USERNAME;
	public static String PASSWORD;
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US);
	public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss a", Locale.US);
	
	public static String[] errorMessage = {"No response from server", "Could not connect to server", 
		"Invalid http protocol", "An error occured, Invalid response from server", "A patient with same MR number already exists", "Request not completed, error code: ", "Unsupported encoding scheme"};
	public static ArrayList<String> errorMessagesList = new ArrayList<String>(Arrays.asList(errorMessage));
	public static final int DATE_TIME = 6;
	public static final int DATE = 7;
	public static final int TIME = 8;
	public static final int PATIENT_LOAD_VIA_TAG = 9;
	public static final int PERIOD_REOPEN = 10;
	public static final int PERIOD_CREATE = 11;
	public static Boolean AGE_DOB_DEADLOCK = false;
	
	public static List<Integer> SCOREABLE_QUESTIONS = Arrays.asList(new Integer[]{9, 11, /*12,*/13,14,56,15,16,17,18});
}