package com.library.lms.model;

/**
 * Defines all possible user roles in the library management system.
 * Roles follow the Spring Security convention of ROLE_ prefix.
 */
public enum ERole {
    ROLE_MEMBER("Regular library member"),
    ROLE_LIBRARIAN("Library staff with management privileges"),
    ROLE_ADMIN("System administrator with full access");

    private final String description;

    ERole(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Converts a string to corresponding ERole enum value
     * @param roleString the string representation of role
     * @return matching ERole enum value
     * @throws IllegalArgumentException if no matching role found
     */
    public static ERole fromString(String roleString) {
        if (roleString != null) {
            // Try exact match first
            for (ERole role : ERole.values()) {
                if (roleString.equalsIgnoreCase(role.name()) ||
                    roleString.equalsIgnoreCase(role.name().replace("ROLE_", ""))) {
                    return role;
                }
            }
        }
        throw new IllegalArgumentException("No role constant with name '" + roleString + "' found");
    }

    /**
     * Gets the simple name of the role (without ROLE_ prefix)
     * @return role name without prefix
     */
    public String getSimpleName() {
        return this.name().replace("ROLE_", "");
    }
}
