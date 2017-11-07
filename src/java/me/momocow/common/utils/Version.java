package me.momocow.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @see <a href="http://semver.org/">Semantic Versioning</a>
 * @author momocow
 */
public class Version {
	/**
	 * @see <a href="https://github.com/sindresorhus/semver-regex">Regexp source</a>
	 */
	private static final String SEMVER_REGEXP = "^v?(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)(-[\\da-z\\-]+(?:\\.[\\da-z\\-]+)*)?(\\+[\\da-z\\-]+(?:\\.[\\da-z\\-]+)*)?$";

	private int major;
	private int minor;
	private int patch;
	private String preRelease;
	private String buildMeta;
	
	public Version (String verStrIn) throws VersionFormatException {
		Matcher matcher = Pattern.compile(SEMVER_REGEXP).matcher(verStrIn);
		if (!matcher.find()) {
			throw new VersionFormatException(verStrIn);
		}
		
		this.major = Integer.parseInt(matcher.group(1));
		this.minor = Integer.parseInt(matcher.group(2));
		this.patch = Integer.parseInt(matcher.group(3));
		this.preRelease = matcher.group(4);
		this.buildMeta = matcher.group(5);
	}
	
	public Version (int majorIn, int minorIn, int patchIn) {
		this (majorIn, minorIn, patchIn, null);
	}
	
	public Version (int majorIn, int minorIn, int patchIn, String preReleaseIn) {
		this.major = majorIn;
		this.minor = minorIn;
		this.patch = patchIn;
		this.preRelease = preReleaseIn;
	}
	
	public boolean gt (Version verObj) {
		if (verObj == null) {
			return true;
		}
		else if (this.major != verObj.major) {
			return this.major > verObj.major;
		} else if (this.minor != verObj.minor) {
			return this.minor > verObj.minor;
		} else if (this.patch != verObj.patch) {
			return this.patch > verObj.patch;
		} else if (this.preRelease != null && verObj.preRelease != null) {
			return this.preRelease.compareTo(verObj.preRelease) > 0;
		} else {
			// once the other version has the same major, minor and patch identifier as the current version
			// and does not contain a pre-release identifier,
			// the current version cannot be greater than the other version, equal to it at most.
			return verObj.preRelease == null;
		}
	}
	
	public boolean gte (Version verObj) {
		return this.eq(verObj) || this.gt(verObj);
	}
	
	public boolean lt (Version verObj) {
		return !this.gte(verObj);
	}
	
	public boolean lte (Version verObj) {
		return !this.gt(verObj);
	}
	
	/**
	 * @see <a href="http://semver.org/#spec-item-11">Versioning Precedence</a>
	 * @return
	 */
	public boolean eq (Version verObj) {
		if (verObj == null) return false;
		
		return this.major == verObj.major && this.minor == verObj.minor && this.patch == verObj.patch
				&& this.preRelease.equals(verObj.preRelease);
	}
	
	/**
	 * @see <a href="http://semver.org/#spec-item-11">Versioning Precedence</a>
	 * @return
	 */
	public boolean neq (Version verObj) {
		return !this.eq(verObj);
	}
	
	public SemVerIdentifier diff (Version verObj) {
		if (verObj == null || this.major != verObj.major) {
			return SemVerIdentifier.MAJOR;
		} else if (this.minor != verObj.minor) {
			return SemVerIdentifier.MINOR;
		} else if (this.patch != verObj.patch) {
			return SemVerIdentifier.PATCH;
		} else if (!this.preRelease.equals(verObj.preRelease)) {
			return SemVerIdentifier.PRE_RELEASE;
		} else {
			return SemVerIdentifier.BUILD_METADATA;
		}
	}
	
	/**
	 * <p>This is <b>NOT</b> a versioning precedence equivalence comparison. <br>
	 * The <u>build metadata</u> should also be equal for this method to return <i>true</i>.</p>
	 * <p>If a versioning precedence equivalence comparison is required, use {@link #eq(Version)} instead.</p>
	 * @see #eq(Version)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Version)) {
			return false;
		}
		
		return this.toString().equals(obj.toString());
	}
	
	@Override
	public String toString () {
	    String ext = "";
	    if (this.preRelease != null) {
	        ext += this.preRelease;
	    }
	    if (this.buildMeta != null) {
	        ext += this.buildMeta;
	    }
	    return String.format("%d.%d.%d%s", this.major, this.minor, this.patch, ext);
	}
	
	public static enum SemVerIdentifier {
		MAJOR, MINOR, PATCH, PRE_RELEASE, BUILD_METADATA
	}
	
	public static class VersionFormatException extends RuntimeException{

		private static final long serialVersionUID = -732596009316670869L;
				
		public VersionFormatException (String verStrIn) {
			this (verStrIn, "Invalid version format.");
		}
		
		public VersionFormatException (String verStrIn, String msg) {
			super(String.format("%s The version string = \"%s\". Check the Semantic Versioning spec at http://semver.org/", 
					msg, verStrIn));
		}
	}
}