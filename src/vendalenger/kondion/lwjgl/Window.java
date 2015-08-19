/*
 * Copyright 2015 Neal Nicdao
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package vendalenger.kondion.lwjgl;

import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwHideWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.Date;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL11;

public class Window {

	private static GLFWKeyCallback keyCallback;

	private static long window;

	private static int windowWidth, windowHeight;

	public static void end() {
		glfwDestroyWindow(Window.getWindow());
	}

	public static int getHeight() {
		return windowHeight;
	}

	public static int getWidth() {
		return windowWidth;
	}

	public static long getWindow() {
		return window;
	}

	public static void setWindowVisible(boolean b) {
		if (b)
			glfwShowWindow(window);
		else
			glfwHideWindow(window);
	}

	/**
	 * The stock way of initializing lwjgl
	 *
	 * @param width
	 *            Width of the game window.
	 * @param height
	 *            Height of the game window.
	 * @param resizable
	 *            determine whenever the window is resizable.
	 * @param fullscreen
	 *            determine whenever the window is full screen.
	 * @param title
	 *            The window title.
	 */
	public static void initGL(int width, int height, boolean resizable,
			boolean fullscreen, String title) {
		System.out.println("Initialize...");
		System.out.println("Create window:");
		windowWidth = width;
		windowHeight = height;
		GLFWErrorCallback errorCallback;
		glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));
		if (glfwInit() != GL_TRUE) {
			throw new IllegalStateException(
					"Error in initializing GLFW Error callback");
		}

		glfwDefaultWindowHints(); // optional, the current window hints are
									// already the default
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden
												// after creation
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be resizable
		
		window = glfwCreateWindow(width, height, title, NULL, NULL);
		if (window == NULL) {
			System.err.println("Error in initializing GLFW Window");
		}

		keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action,
					int mods) {
				if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
					glfwSetWindowShouldClose(window, GL_TRUE); // We will detect
																// this in our
																// rendering
																// loop
			}
		};
		glfwSetKeyCallback(window, keyCallback);

		ByteBuffer mode = glfwGetVideoMode(glfwGetPrimaryMonitor());

		glfwSetWindowPos(window, (GLFWvidmode.width(mode) - width) / 2,
				(GLFWvidmode.height(mode) - height) / 2);

		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);
		
		System.out.println("GLFW Version: " + GLFW.glfwGetVersionString());
		//System.out.println("OpenGL Version: " + GL11.glGetString(GL11.GL_VERSION));

		// TTT.Two();
	}

	/**
	 * Sets LWJGL native library path.
	 */
	public static void setNatives() {
		String dataModel = System.getProperty("sun.arch.data.model");
		// new File("").getAbsolutePath() is getting the directory
		String path = new File("").getAbsolutePath();
		if (!path.endsWith(File.separator))
			path += File.separator;
		path += "lwjgl";

		System.out.println("Today's Date: " + new Date().toString());
		System.out.println("On directory: " + new File("").getAbsolutePath());
		System.out.println("Detected OS: " + System.getProperty("os.name")
				+ " " + System.getProperty("os.version"));
		System.out.println("Architecture: " + System.getProperty("os.arch"));
		System.out.println("Data model: " + dataModel);
		String os = System.getProperty("os.name").toLowerCase();
		System.out.println("CPU Cores: "
				+ Runtime.getRuntime().availableProcessors());

		/* Detect the Operating system then load the appropriate lwjgl natives */
		if (os.contains("nix") || os.contains("aix") || os.contains("nux")) {
			System.out.println("Loading Linux LWJGL Natives, finally.");
			path += "/linux";
		} else if (os.toLowerCase().contains("win")) {
			System.out
					.println("Loading Windows LWJGL Natives. Windows is fine, but still. get a Linux.");
			path += "\\windows";
		} else if (os.contains("mac")) {
			System.out
					.println("Loading Macintosh Apples LWJGL Natives and Linux is better that this expensible OS, so get a Linux.");
			path += "/getalinux";
		}

		/* 64, or 32 bit? */
		if (dataModel.equals("64")) {
			/* If amd64 */
			path += File.separator + "x64";
		} else {
			/* If x86 or i386 */
			path += File.separator + "x86";
		}
		System.err.println(path);
		System.setProperty("org.lwjgl.librarypath", path);
	}

	public static void update() {
		glfwSwapBuffers(window); // swap the color buffers
		glfwPollEvents();
	}
}
