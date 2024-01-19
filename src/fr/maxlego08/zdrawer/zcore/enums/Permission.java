package fr.maxlego08.zdrawer.zcore.enums;

public enum Permission {
	
	EXAMPLE_PERMISSION

	;

	private String permission;

	private Permission() {
		this.permission = this.name().toLowerCase().replace("_", ".");
	}

	public String getPermission() {
		return permission;
	}

}
