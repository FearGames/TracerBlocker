package cz.GravelCZLP.TracerBlocker;

import java.lang.reflect.Constructor;

import cz.GravelCZLP.TracerBlocker.Common.Loader;

/**
 * Created by GravelCZLP on 5.7.17.
 */
public enum Version {
 
	TBV1_12("1.12", "cz.GravelCZLP.TracerBlocker.v1_12.Loader_v1_12"),
	TBV1_13("1.13", "cz.GravelCZLP.TracerBlocker.v1_13.Loader_v1_13"),
	TBV1_8_8("1.8.8", "cz.GravelCZLP.TracerBlocker.v1_8.Loader_v1_8");

	private String version, className;

	private Version(String v, String className) {
		version = v;
		this.className = className;
	}

	public static boolean isVersionSupported(String v) {
		for(Version ver : values()) {
			if(v.contains(ver.getVersion())) {
				return true;
			}
		}
		return false;
	}

	public static Loader getLoaderByVersion(String version, TracerBlocker tracerBlocker) {
		for(Version ver : values()) {
			if(version.contains(ver.getVersion())) {
				return ver.getLoader(tracerBlocker);
			}
		}
		return null;
	}

	public String getVersion() { return version; }

	public Loader getLoader(TracerBlocker tracerBlocker) {
		Class<?> clazz = null;
		try {
			clazz = Class.forName(getClazzName());
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		if(clazz == null) {
			return null;
		}
		try {
			Constructor<?> constructor = clazz.getConstructor(TracerBlocker.class);
			return ((Loader) constructor.newInstance(tracerBlocker));
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getClazzName() { 
		return className; 
	}

	public static String getMaximumSupported()
	{
		return TBV1_13.version;
	}

	
	public static String getMinimumSupported()
	{
		return TBV1_8_8.version;
	}
}
