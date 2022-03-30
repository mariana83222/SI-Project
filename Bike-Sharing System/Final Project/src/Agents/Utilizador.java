package Agents;

import java.util.Random;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;

import jade.core.behaviours.CompositeBehaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import java.util.ArrayList;
import java.util.Calendar;

public class Utilizador extends Agent {
	
	//variáveis globais
	
	//atributos
	private int dest_x;
	private int dest_y;
	private int x_ini;
	private int y_ini;
	private int v = 2; // velocidade
	
	private int dist_total;
	private int duracao;
	private int x_atual = 0;
	private int y_atual = 0;
	private int dist_atual;

	private double vx;
	private double vy;
	
	//----------
    private int x_34;
    private int y_34;
	private int ini;
	private int dest;
	private int x_loc;
	private int y_loc;
    private String est_final;
    private int t;
	private int count_incentivo = 0;
	private int t34;
	private int dist_34;
	private int i;
	DFAgentDescription[] result;
	AID receiverEst = new AID();
	AID receiverFINAL = new AID();
	private int percurso_normal = 0;
	
	private int count= 0;
	
	
//------------------------------------------------------------------------SETUP------------------------------------------------------------------------------	

	protected void setup() {
		super.setup();
		addBehaviour(new PedidoBCC());
		addBehaviour(new Recebe());
		//addBehaviour(new Calculos());
		//addBehaviour(new Viagem(this, 1000));
	}

//---------------------------------------------------------------PEDIDO DE BICICLETA-----------------------------------------------------------------------
	public class PedidoBCC extends OneShotBehaviour {

		public void action() {
			// DF
			// Update the list of seller agents
			DFAgentDescription template = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			sd.setType("estacao");
			template.addServices(sd);
			try {
				result = DFService.search(myAgent, template);

				Random rand = new Random();
				ini = rand.nextInt(result.length); // gera i aleatório para array
				dest = rand.nextInt(result.length);
				while (ini == dest) {
					dest = rand.nextInt(result.length);
				}
				for (i = 0; i < result.length; ++i) { // para cada elemento da lista result, envia uma mensagem
					if (i == ini) {

						ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
						msg.addReceiver(result[i].getName());
						msg.setContent("Pedido de bicicleta (início)");
						//System.out.println("Utilizador | Pede coord + ocup à estação inicial");
						send(msg);

					}
					if (i == dest) {
						ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
						msg.addReceiver(result[i].getName());
						msg.setContent("Pedido de bicicleta (final)");
						//System.out.println("Utilizador | Pede coord à estação final");
						send(msg);
						//est_final = result[i].getName();
					}
				}

				/*
				 * } else { System.out.println("Agent " + getLocalName() + " not found!");
				 */

			} catch (FIPAException fe) {
				fe.printStackTrace();
			}

		}

	}

//---------------------------------------------------------------RECEBE MENSAGENS ESTACAO--------------------------------------------------------------------

	public class Recebe extends CyclicBehaviour {
		
		private ArrayList utilizadorAPE = new ArrayList(); // array para inserir os ID estação
		int count = 0;
		private ArrayList dist = new ArrayList();

		public void action() {
			ACLMessage msg = receive();
			if (msg != null) {
				 System.out.println("Utilizador | Agent "+getAID().getName()+ " recebeu" + msg.getContent());
				 
//----------------------------------------------------------------RECEBE COORDENADAS ESTACAO INICIAL E FINAL-------------------------------------------------------------------
				if (msg.getPerformative() == ACLMessage.INFORM) {
					if (msg.getContent().contains("inicial")) {
																								
						String[] conteudo = msg.getContent().split(",");
						int ocupacao = Integer.parseInt(conteudo[2]);

						x_ini = Integer.parseInt(conteudo[0]);
						y_ini = Integer.parseInt(conteudo[1]);
						ACLMessage response = msg.createReply();
						response.setPerformative(ACLMessage.CONFIRM);
						response.setContent("Confirma aluguer");
						send(response);
						
						// Inicia viagem no percurso normal
						int percurso_normal = 1;
						myAgent.addBehaviour(new Calculos());
					
					}
					 else if (msg.getContent().contains("Não existem bicicletas disponíveis nesta estação")) {
						 //tem de selecionar outra estação inicial
						Random rand = new Random();
						int  novo_ini = rand.nextInt(result.length); //gera i aleatório para array
						 
						 if (novo_ini != ini  && novo_ini != dest) { //enquanto esse número corresponder à inicial anterior ou à final
							 ini = novo_ini;
						 }
						 else {
							 novo_ini = rand.nextInt(result.length); //gera uma nova 
						 }
							ACLMessage response = msg.createReply(); 
							response.setPerformative(ACLMessage.REQUEST); 
							response.addReceiver(result[ini].getName());
							response.setContent("Pedido de bicicleta (início)");
							//System.out.println("Utilizador | Pede coord + ocup à estação inicial");
							send(response);
					 }
					else if (msg.getContent().contains("final")) {
						est_final = msg.getSender().getLocalName();
						String[] conteudo = msg.getContent().split(",");
						dest_x = Integer.parseInt(conteudo[0]);
						dest_y = Integer.parseInt(conteudo[1]);
						//System.out.println("coordenadas de destino:" + dest_x + "  " + dest_y);
					}//fim final
				

					
				} // if receive
//------------------------------------------------------------RECEBE INCENTIVOS---------------------------------------------------------------------------------
				else if (msg.getPerformative() == ACLMessage.CFP) {
					if (msg.getContent().contains("incentivo5")) { // rejeita
						//System.out.println("Utilizador | Agent " + getAID().getName() + " recebeu" + msg.getContent() + "da estação: " + msg.getSender().getLocalName());
					    //est_final = est_final;
					    ACLMessage response = msg.createReply();
						response.setPerformative(ACLMessage.INFORM);
						response.setContent("Não aceita incentivo");
						myAgent.send(response);
						
					} 
					else if (msg.getContent().contains("incentivo10")) { // negoceia
						//PROPOSTA DE NEGOCIAÇÃO
						ACLMessage response = msg.createReply();
						response.setPerformative(ACLMessage.PROPOSE);
						response.setContent("Contra-proposta: " + "desconto 15%");
						myAgent.send(response);

						
					}
					if (msg.getContent().contains("incentivo15")) { // aceita
						count_incentivo++;

						if (count_incentivo == 1) { // aceitar o primeiro incentivo que chegar
							
							//muda a estação final
							est_final = msg.getSender().getLocalName();
							
							//envia mensagem a confirmar que aceita o incentivo
							ACLMessage response = msg.createReply();
							response.setPerformative(ACLMessage.INFORM);
							response.setContent("Aceita incentivo");
							myAgent.send(response);
							
							//substitui as coordenadas iniciais e finais pelas novas
							String[] conteudo = msg.getContent().split(",");
							x_ini = x_34;
							y_ini = x_34;
							dest_x = Integer.parseInt(conteudo[1]);
							dest_y = Integer.parseInt(conteudo[2]);
							
                            //volta a calcular dist, duracao e velocidades
							myAgent.addBehaviour(new Calculos());

							

						}
					} // fim if incentivo 15
					
				} // fim CFP
				
				else if (msg.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) { //estação rejeita proposta, utilizador volta a fazer os cálculos
					count_incentivo++;
					if (count_incentivo == 1) {
						//substitui as coordenadas iniciais e finais pelas novas
						String[] conteudo = msg.getContent().split(",");
						x_ini = x_34;
						y_ini = x_34;
						dest_x = Integer.parseInt(conteudo[1]);
						dest_y = Integer.parseInt(conteudo[2]);
						
                        //volta a calcular dist, duracao e velocidades
						myAgent.addBehaviour(new Calculos());
						
					}
				}
				
				
				else if (msg.getPerformative() == ACLMessage.CONFIRM) {
					if (msg.getContent().equals("Confirma devolução")) { //caso receba confirmacao devolucao
						//devolvido = 1;
						System.out.println("BICICLETA DEVOLVIDA");
						doDelete();
					}
					else if (msg.getContent().equals("Não é possível devolver")) { //caso não possa devolver, volta a tentar, até receber a confirmação
						ACLMessage response = msg.createReply();
						response.setPerformative(ACLMessage.INFORM);
						response.setContent("Tentativa de devolução");
						myAgent.send(response);
					}
					
				}

			} // fim do if receive
			else {
				block();
			} // doDelete();

		} // if action
	} // if cyclic

//-----------------------------------------------------------CÁLCULOS DE VELOCIDADE E CENAS-------------------------------------------------------------------

	public class Calculos extends OneShotBehaviour {

	@Override
	public void action() {
		// TODO Auto-generated method stub
		
		if (dest_x != 0 && dest_y !=0) {
			// Cálculo entre est inicial e final: percurso total a percorrer
			dist_total = (int) Math.sqrt(((Math.pow((x_ini - dest_x), 2)) + (Math.pow((y_ini - dest_y), 2))));
			
			
			
			// duracao da viagem
			duracao = (int) dist_total/ v;
		}
		
		
		dist_34 = (int) ((3 * dist_total) / 4);
		
		
		// tempo que demora a percorrer 3/4 do percuso
		t34 = (int) ((3 * duracao) / 4);
		
		// Cálculo velocidades horizontal e vertical
		vx = (double)(dest_x - x_ini) / duracao;
		vy = (double) (dest_y - y_ini) / duracao;
		
	
		
		t=0;
		
		myAgent.addBehaviour(new Viagem(myAgent, 1000));
		

		
		
		
		
	}
		
	}
//---------------------------------------------------------------------VIAGEM--------------------------------------------------------------------

	private class Viagem extends TickerBehaviour {
    

	public Viagem(Agent a, long period) {
		super(a, period);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onTick() {
		// TODO Auto-generated method stub
        t++;
		
		int x_atual = (int) (x_ini + (vx * t));
		int y_atual = (int) (y_ini + (vy * t));
		
		

		if (t>=t34 && count_incentivo ==0) { // quando tiverem passado 3/4 do tempo
			
			// envia mensagem às restantes estações a pedir coordenadas
			for (int i = 0; i < result.length; ++i) {
				if (i != ini && i != dest) { // envia mensagens a cada estação menos à inicial e à destino
					ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
					msg.addReceiver(result[i].getName());
					msg.setContent("Pedido de verificação APE" + "," + x_atual + "," + y_atual);
					//System.out.println("Utilizador | Pedido de verificação APE");
					send(msg);
				}
			} // for 
		} 
		if (t >= duracao) {
			x_atual = dest_x;
			y_atual = dest_y;
			System.out.println("FIM DA VIAGEM");
			receiverFINAL.setLocalName(est_final); //envia mensagem para essa estação
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.addReceiver(receiverFINAL);
			msg.setContent("Tentativa de devolução");
			send(msg);
			
			
		}
		
		
	
	
			
		
		
	}
		
	} 
	
	
	
	
//---------------------------------------------------------------------MORTE------------------------------------------------------------------------------
	protected void takeDown() {
		super.takeDown();
		System.out.println(this.getLocalName() + " a terminar...");
	}
}
