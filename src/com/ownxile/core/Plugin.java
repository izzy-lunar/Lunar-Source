package com.ownxile.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.python.core.Py;
import org.python.core.PyException;
import org.python.core.PyFunction;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import com.ownxile.config.FileConfig;
import com.ownxile.config.GameConfig;
import com.ownxile.util.Logger;
import com.ownxile.util.file.FileLog;

public class Plugin {

	private static int loaded;
	public static PythonInterpreter pythonInterpreter = new PythonInterpreter();

	static {
		Plugin.pythonInterpreter.setOut(new Logger(System.out));
		Plugin.pythonInterpreter.setErr(new Logger(System.err));
	}

	public static Object callFunc(Class<?> c, String funcName, Object... binds) {
		try {
			final PyObject obj = Plugin.pythonInterpreter.get(funcName);
			if (obj != null && obj instanceof PyFunction) {
				final PyFunction func = (PyFunction) obj;
				final PyObject[] objects = new PyObject[binds.length];
				for (int i = 0; i < binds.length; i++) {
					final Object bind = binds[i];
					objects[i] = Py.java2py(bind);
				}
				return func.__call__(objects).__tojava__(c);
			} else {
				return null;
			}
		} catch (final PyException ex) {
			ex.printStackTrace();
			// FileLog.writeLog("plugin_errors", ex.getMessage());
			return null;
		}
	}

	public static boolean execute(String funcName, Object... binds) {
		try {
			final PyObject obj = Plugin.pythonInterpreter.get(funcName);
			if (obj != null && obj instanceof PyFunction) {
				final PyFunction func = (PyFunction) obj;
				final PyObject[] objects = new PyObject[binds.length];
				for (int i = 0; i < binds.length; i++) {
					final Object bind = binds[i];
					objects[i] = Py.java2py(bind);
				}
				func.__call__(objects);
				return true;
			} else {
				return false;
			}
		} catch (final PyException ex) {
			ex.printStackTrace();
			// FileLog.writeLog("plugin_errors", ex.getMessage());
			return false;
		}
	}

	public static PyObject getVariable(String variable) {
		try {
			return Plugin.pythonInterpreter.get(variable);
		} catch (final PyException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void load() throws IOException {
		Plugin.pythonInterpreter.cleanup();
		final File scriptDir = new File(FileConfig.SCRIPT_DIRECTORY);
		if (scriptDir.isDirectory() && !scriptDir.getName().startsWith(".")) {
			final File[] children = scriptDir.listFiles();
			for (final File child : children) {
				if (child.isFile()) {
					if (child.getName().endsWith(
							FileConfig.SCRIPT_FILE_EXTENSION)) {
						pythonInterpreter.execfile(new FileInputStream(child));
						loaded++;
					}
				} else {
					Plugin.recurse(child.getPath());
				}
			}
		}
		System.out.println("Loaded " + loaded + " plugins.");
		Plugin.loaded = 0;
	}

	private static void recurse(String dir) throws IOException {
		final File scriptDir = new File(dir);
		if (scriptDir.isDirectory() && !scriptDir.getName().startsWith(".")) {
			final File[] children = scriptDir.listFiles();
			for (final File child : children) {
				if (child.isFile()) {
					if (child.getName().endsWith(
							FileConfig.SCRIPT_FILE_EXTENSION)) {
						if (GameConfig.PRINT_PLUGIN_DIRECTORIES)
							System.out.println(child.getPath());
						Plugin.pythonInterpreter.execfile(new FileInputStream(
								child));
						Plugin.loaded++;
					}
				} else {
					Plugin.recurse(child.getPath());
				}
			}
		}
	}
}
