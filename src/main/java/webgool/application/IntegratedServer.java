package webgool.application;

import java.util.concurrent.atomic.AtomicBoolean;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

import javax.swing.JFrame;

import org.glassfish.embeddable.*;


import webgool.view.HMIServer;

/**
 * IntegratedServer class is the entry point of webgool.<br>
 * This class contains a GlassFish server instance that is parameterized and started. It also instantiates the gool translator.<br>
 * @author Charbel FOUREL
 * @version 0.1a
 * 
 */
public class IntegratedServer {

	private int port;
	private AtomicBoolean initialise = new AtomicBoolean();
	private GlassFish glassfish;

	/**
	 * Constructor with port definition
	 * @param port
	 */
	public IntegratedServer(int port) {
		this.port = port;
	}

	/**
	 * Parameterized the GlassFish server.<br>
	 * @return the IntegratedServer instance.<br>
	 * @throws Exception
	 */
	public IntegratedServer init() throws Exception{
		if ( initialise.get() ) {
			throw new RuntimeException("Server already started.");
		}
		BootstrapProperties bootstrapProperties = new BootstrapProperties();
		GlassFishRuntime glassfishRuntime = GlassFishRuntime.bootstrap(bootstrapProperties);

		GlassFishProperties glassfishProperties = new GlassFishProperties();
		glassfishProperties.setPort("http-listener", port);
		glassfish = glassfishRuntime.newGlassFish(glassfishProperties);
		initialise.set(true);
		return this;
	}

	/**
	 * Check that the initialization has correctly be done.<br>
	 */
	private void check() {
		if ( !initialise.get() ) {
			throw new RuntimeException("Server is not started.");
		}
	}


	/**
	 * Starts the glassfish server. It first check that there is no server instance already executed.<br>
	 * @return the IntegratedServer instance.<br>
	 * @throws Exception
	 */
	public IntegratedServer start() throws Exception{
		check();
		glassfish.start();
		glassfish.getDeployer().deploy(new File("build"));
		return this;
	}

	/**
	 * Stops the current server instance.<br>
	 * @return the stopped IntegratedServer instance.<br>
	 * @throws Exception
	 */
	public IntegratedServer stop() throws Exception{
		check();
		glassfish.stop();
		return this;
	}

	/**
	 * webgool entry point
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception {

		String value = System.getProperty("X11");
		System.out.println("X11 = " + value);

		if (value.equals("true")){
			JFrame serveur = new HMIServer();

			// server start
			final IntegratedServer runner = new IntegratedServer(2009).init().start();

			// For safety reason, the close cross of the window is associated with a new listener
			serveur.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent we) {
					try {
						runner.stop();
						System.exit(0);
					} catch (Exception e) {
						System.out.println("ERROR: **Server can't safely stop.**");
					}
				}
			});
		}else{
			// server start
			final IntegratedServer runner = new IntegratedServer(2009).init().start();
			class ShutDownThread extends Thread {
				public void run() {
					System.out.println( "Server shutdown..." );
					try {
						runner.stop();
					} catch (Exception e) {
						System.out.println("ERROR: **Server can't safely stop.**");
					} finally{
						System.out.println( "Server shutdown complete." );
					}
				}
			};
			Runtime.getRuntime().addShutdownHook( new ShutDownThread() );
		}
	}	
}
