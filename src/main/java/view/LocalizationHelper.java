package view;

import java.util.Locale;
import java.util.ResourceBundle;

import controller.CurrentUser;

/**
 * Class for handling localizations in views
 * 
 * @author Kerkko
 */
public class LocalizationHelper {

	/**
	 * Private constructor for helper class
	 */
	private LocalizationHelper() {
	}

	/**
	 * Method for getting localization according to user preference
	 * 
	 * @return localization ResourceBundle
	 */
	public static ResourceBundle getLocalization() {
		ResourceBundle localization;
		if (CurrentUser.getUser().getLanguage() != null) {
			localization = ResourceBundle.getBundle("TextResources", CurrentUser.getUser().getLanguage());
		} else {
			localization = ResourceBundle.getBundle("TextResources", Locale.getDefault());
		}
		return localization;
	}
}
