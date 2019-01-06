package cz.GravelCZLP.TracerBlocker;

import java.lang.reflect.Constructor;

import com.comphenix.protocol.ProtocolManager;

import cz.GravelCZLP.TracerBlocker.Common.Loader;

/**
 * Created by GravelCZLP on 5.7.17.
 */
public enum Version {
 
	TBV1_12("1.12", "cz.GravelCZLP.TracerBlocker.v1_12.Loader_v1_12");

	private String version, className;

	Version(String v, String className) {
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

	public static Loader getLoaderByVersion(String version, TracerBlocker tracerBlocker, ProtocolManager protocolManager) {
		for(Version ver : values()) {
			if(version.contains(ver.getVersion())) {
				return ver.getLoader(tracerBlocker, protocolManager);
			}
		}
		return null;
	}

	public String getVersion() { return version; }

	public Loader getLoader(TracerBlocker tracerBlocker, ProtocolManager protocolManager) {
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
			Constructor<?> constructor = clazz.getConstructor(TracerBlocker.class, ProtocolManager.class);
			return ((Loader) constructor.newInstance(tracerBlocker, protocolManager));
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getClazzName() { return className; }

}
