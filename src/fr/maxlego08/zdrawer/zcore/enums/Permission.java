package fr.maxlego08.zdrawer.zcore.enums;

public enum Permission {
	
	ZDRAWER_USE,
	ZDRAWER_GIVE,
	ZDRAWER_GIVE_DRAWER,
	ZDRAWER_GIVE_CRAFT,
	ZDRAWER_RELOAD,
	ZDRAWER_CLEAR,
	ZDRAWER_CONVERT,
	ZDRAWER_PLACE,
	ZDRAWER_PURGE, ZDRAWER_DEBUG;

	private String permission;

	private Permission() {
		this.permission = this.name().toLowerCase().replace("_", ".");
	}

	public String getPermission() {
		return permission;
	}

}
