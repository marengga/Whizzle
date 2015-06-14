package com.marengga.whizzle.utils;

import android.content.Context;
import com.marengga.whizzle.R;

public class Utils {

	//Set all the navigation icons and always to set "zero 0" for the item is a category
	public static int[] iconNavigation = new int[] { 
		0, 
		R.drawable.ic_action_update,	// newsfeed
		R.drawable.ic_action_library, 	// library
		R.drawable.ic_action_message,	// message
		R.drawable.ic_action_team,	 	// team
		R.drawable.ic_action_contact, 	// contacts
		R.drawable.ic_action_chat, 		// chat
		0, 
		R.drawable.ic_action_settings,	// settings
		R.drawable.ic_action_about 		// about
		};	
	
	//get title of the item navigation
	public static String getTitleItem(Context context, int position){		
		String[] judul = context.getResources().getStringArray(R.array.nav_menu_items);  
		return judul[position];
	}
	
}
