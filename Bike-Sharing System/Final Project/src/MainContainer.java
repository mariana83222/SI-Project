
import jade.core.Runtime;

import java.awt.List;
import java.util.ArrayList;
import java.util.Random;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class MainContainer {

	Runtime rt;
	ContainerController container;

	public ContainerController initContainerInPlatform(String host, String port, String containerName) {
		// Get the JADE runtime interface (singleton)
		this.rt = Runtime.instance();

		// Create a Profile, where the launch arguments are stored
		Profile profile = new ProfileImpl();
		profile.setParameter(Profile.CONTAINER_NAME, containerName);
		profile.setParameter(Profile.MAIN_HOST, host);
		profile.setParameter(Profile.MAIN_PORT, port);
		// create a non-main agent container
		ContainerController container = rt.createAgentContainer(profile);
		return container;
	}

	public void initMainContainerInPlatform(String host, String port, String containerName) {

		// Get the JADE runtime interface (singleton)
		this.rt = Runtime.instance();

		// Create a Profile, where the launch arguments are stored
		Profile prof = new ProfileImpl();
		prof.setParameter(Profile.CONTAINER_NAME, containerName);
		prof.setParameter(Profile.MAIN_HOST, host);
		prof.setParameter(Profile.MAIN_PORT, port);
		prof.setParameter(Profile.MAIN, "true");
		prof.setParameter(Profile.GUI, "true");

		// create a main agent container
		this.container = rt.createMainContainer(prof);
		rt.setCloseVM(true);

	}

	public void startAgentInPlatform(String name, String classpath, Object[] objects) {
		try {
			AgentController ac = container.createNewAgent(name, classpath, objects);
			ac.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		MainContainer a = new MainContainer();

		a.initMainContainerInPlatform("localhost", "9888", "MainContainer");
		int n;

		ArrayList<Integer[]> where = new ArrayList<Integer[]>();
		Integer[] a1 = { 1, 33, 1, 33};
		where.add(a1);

		Integer[] a2 = { 34, 66, 1, 33};
		where.add(a2);

		Integer[] a3 = { 67, 100, 1, 33};
		where.add(a3);

		
		Integer[] a4 = { 1, 33, 34, 66 };
		where.add(a4);

		Integer[] a5 = {34, 66, 34, 66};
		where.add(a5);

		Integer[] a6 = { 67, 100, 34, 66 };
		where.add(a6);

		
		Integer[] a7 = { 1, 33, 67, 100 };
		where.add(a7);

		Integer[] a8 = { 34, 66, 67, 100 };
		where.add(a8);

		Integer[] a9 = { 67, 100, 67, 100 };
		where.add(a9);

		

		int limit_est = 9; // numero de estações

		// Start Agents Estacao!
		for (n = 0; n < 9; n++) {

			Object[] args_input = new Object[4];
			Integer[] x = where.get(n);
			// System.out.println(x[0]+ " " + x[1]);
			// args_input.add();

			a.startAgentInPlatform("Estacao" + n, "Agents.Estacao", new Object[] { x[0], x[1], x[2], x[3] });
			// n++;
		}

		// Let all Taxis be ready
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Start Interface
		a.startAgentInPlatform("Interface", "Agents.Interface", new Object[]{});
		//a.startAgentInPlatform("Utilizador", "Agents.Utilizador", new Object[] {});

		// Start first 10 Agents Customers!
		
		// Start first 10 Agents Customers!
		for (n = 0; n < 10; n++) {
					a.startAgentInPlatform("Utilizador" + n, "Agents.Utilizador", new Object[] {});
					n++;
				}

				int limit_customers = 25; // Limit number of Customers
				// Start Agents Customers!
				for (n = 10; n < limit_customers; n++) {
					try {
						a.startAgentInPlatform("Utilizador" + n, "Agents.Utilizador", new Object[] {});
						Thread.sleep(500);
						n++;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}  

	}
}
