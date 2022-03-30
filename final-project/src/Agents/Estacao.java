package Agents;

import java.util.Random;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class Estacao extends Agent {

	Random rand = new Random();
	
	//vari�veis globais
	private int x_loc;
	private int y_loc;
	private int ocupacao = rand.nextInt(10);
	private int capacidade = 15;
	private int n_APE = 0;
	private int APE_est = 30;
	private int pos_x;
	private int pos_y;
	private int distance_APE;
	private int count_info = 0;
	
	
	AID Interface = new AID();
	private String inter;

//------------------------------------------------------------------------SETUP-----------------------------------------------------------------------------------

	protected void setup() {
		//System.out.println("****************************************************ESTA��O INICIALIZA***************************************************");
		super.setup();
		Object[] args = getArguments();
		int arg1 = (int) args[0]; // this returns the String "1"
		int arg2 = (int) args[1]; // this returns the String "arg2"
		int arg3 = (int) args[2];// this returns the String "argument3"
		int arg4 = (int) args[3];

		x_loc = rand.nextInt(arg2 + 1 - arg1) + arg1;
		y_loc = rand.nextInt(arg4 + 1 - arg3) + arg3;

		
		addBehaviour(new Recebe());
		
		
		// Registo nas p�ginas amarelas
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("estacao"); //
		sd.setName("aluguer de bcc");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
			System.out.println("Esta��o | Agent " + getAID().getName() + " registering.");
		} catch (FIPAException fe) { // Caso n�o se consiga registar no servidor-d� mensagem de erro
			fe.printStackTrace();
		}

	}

//----------------------------------------------------------RECEBE MENSAGENS UTILIZADOR-----------------------------------------------------------------------	
	public class Recebe extends CyclicBehaviour {
		
		
		private int previs�o_lug_livres;
		public void action() {
			ACLMessage msg = receive();
			if (msg != null) {
				System.out.println("Esta��o | Agent " + getAID().getName() + " recebeu" + msg.getContent());
				// --------------------------------------------------ENVIA COORDENADAS------------------------------------------------------------------
				if (msg.getPerformative() == ACLMessage.REQUEST) {

					if (msg.getContent().equals("Pedido de bicicleta (in�cio)")) {
						
						if (ocupacao < 15) { //caso haja pelo menos uma bcc dispon�vel
							if (count_info == 1) {
								ACLMessage response = msg.createReply();
								response.setPerformative(ACLMessage.INFORM);
								response.setContent(x_loc + "," + y_loc + "," + ocupacao + "," + "inicial"); 
								//System.out.println("Esta��o inicial | A coordenada de x �: " + x_loc + " e a coordenada de y �: " + y_loc);
								send(response);

							}
													} 
						else if(ocupacao == 15) { //caso estejam todas ocupadas, informa o utilizador
							 ACLMessage response = msg.createReply();
								response.setPerformative(ACLMessage.INFORM);
								response.setContent("N�o existem bicicletas dispon�veis nesta esta��o"); //estacao inicial
								//System.out.println("N�o existem bicicletas dispon�veis nesta esta��o");
								send(response);	 
							
						 }

					} else if (msg.getContent().equals("Pedido de bicicleta (final)")) {
						ACLMessage response = msg.createReply();
						response.setPerformative(ACLMessage.INFORM);
						response.setContent(x_loc + "," + y_loc + "," + ocupacao + "," + "final"); // estacao final
						//System.out.println("Esta��o final | A coordenada de x �: " + x_loc + " e a coordenada de y �: " + y_loc);
						send(response);
//----------------------------------------------------------------------------------VERIFICA��O APE--------------------------------------------------------------------------
					} else if (msg.getContent().contains("Pedido de verifica��o APE")) {
						 
						//obtem posi��o atual do utilizador
						String[] conteudo = msg.getContent().split(","); 
					     pos_x = Integer.parseInt(conteudo[1]);
					     pos_y = Integer.parseInt(conteudo[2]);
					     
					     //verifica se esse utilizador est� na sua APE
					     distance_APE = (int) Math.sqrt(((Math.pow((pos_x - x_loc), 2)) + (Math.pow((pos_y - y_loc), 2))));
					     
							if (distance_APE <= APE_est) { // caso esteja dentro da sua APE
								n_APE++;
								
					          //inicia decis�o incentivo
								//previs�o_lug_livres = ocupacao - n_APE;
								previs�o_lug_livres = 12;
								
								
			                          //--------------------------------------------------------------------Envio Incentivo----------------------------------------------------------
									if (previs�o_lug_livres >= 12 & previs�o_lug_livres <= 15) {
										//if (previs�o_lug_livres != 0 ) {
											ACLMessage response = msg.createReply(); 
											 response.setPerformative(ACLMessage.CFP); 
											 response.setContent("incentivo15" + "," +  x_loc + "," + y_loc); 
											 myAgent.send(response);
											
								          } 
									 if (previs�o_lug_livres >=9 & previs�o_lug_livres <= 11) {
										ACLMessage response = msg.createReply(); 
										 response.setPerformative(ACLMessage.CFP); 
										 response.setContent("incentivo10"); 
										 myAgent.send(response);
										 
								     } 
									else if (previs�o_lug_livres >=6 & previs�o_lug_livres <= 8) {
										ACLMessage response = msg.createReply(); 
										 response.setPerformative(ACLMessage.CFP); 
										 response.setContent("incentivo5"); 
										 myAgent.send(response);
										 
								     }  
								
							}
					

					}
				}
//---------------------------------------------------------------------------------------------------CONFIRMACAO DE ALUGUER------------------------------------------------------------------
			 else if(msg.getPerformative() == ACLMessage.CONFIRM) { //quando o utilizador confirma aluguer de bcc
						if(msg.getContent().equals("Confirma aluguer")) {
					 		//atualiza ocupacao
					        ocupacao++;
					        //envia mensagem para a interface: 
					       Interface.setLocalName(inter);
					        ACLMessage mensg = new ACLMessage(ACLMessage.INFORM);
							mensg.addReceiver(Interface);
							mensg.setContent("Ocupa��o aumentou");
							//System.out.println("Utilizador | Pede coord + ocup � esta��o inicial");
							send(mensg);
						}
						
				 }
				
				
//--------------------------------------------------------------------------------------------RECEBE DECIS�ES INCENTIVO----------------------------------------------------------
				else if (msg.getPerformative() == ACLMessage.INFORM) { 
					
				
					 
					 if(msg.getContent().equals("Aceita incentivo") || (msg.getContent().equals("N�o aceita incentivo"))) { //utilizador vai devolver na esta��o alternativa, j� n�o precisa de ser considerado para previs�o
						 n_APE--;
						 
					 }
					 else if(msg.getContent().equals("Tentativa de devolu��o")) {
							if (ocupacao >= 1 && ocupacao <= 15) { //qd houver pelo menos 1 lugar livre
								ACLMessage response = msg.createReply();
								response.setPerformative(ACLMessage.CONFIRM);
								response.setContent("Confirma devolu��o"); 
								myAgent.send(response);
								ocupacao--;
								System.out.println("ESTACAO CONFIRMA DEVOLUCAO");
								
								//envia mensagem para a interface: 
								Interface.setLocalName(inter);
						        ACLMessage mensg = new ACLMessage(ACLMessage.INFORM);
								mensg.addReceiver(Interface);
								mensg.setContent("Ocupa��o diminuiu");
								//System.out.println("Utilizador | Pede coord + ocup � esta��o inicial");
								send(mensg);
							} 
							else  if (ocupacao == 0){ 
								//envia msg
								ACLMessage response = msg.createReply();
								response.setPerformative(ACLMessage.CONFIRM);
								response.setContent("N�o � poss�vel devolver"); 
								myAgent.send(response);
							}
							
							
						}

					
				}// fim else if inform
				else if (msg.getPerformative() == ACLMessage.PROPOSE) { //recebe contra proposta para aumentar valor do desconto
					n_APE--; // se rejeita, sai da APE porque devolve na esta��o final prevista, se aceita, vai devolver naquela, n�o � necess�rio para a previs�o
					if (previs�o_lug_livres >=10 & previs�o_lug_livres <= 11) {  //aceita a contra-proposta
						ACLMessage response = msg.createReply();
						response.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
						response.setContent("Aceita contra-proposta" + "," + x_loc + "," + y_loc); 
						myAgent.send(response);
					
					}
					if (previs�o_lug_livres == 9) { 
						ACLMessage response = msg.createReply();
						response.setPerformative(ACLMessage.REJECT_PROPOSAL);
						response.setContent("N�o aceita contra-proposta"); 
						myAgent.send(response);
						//utilizador vai devolver naquela esta��o
						
					}
				}
				//--------------------------------------------------------------------INTERA��O INTERFACE-------------------------------------------
				else if (msg.getPerformative() == ACLMessage.CFP) {  //recebe pedido de info de interface 
					count_info++;
					inter = msg.getSender().getLocalName();
					//envia coordenadas e ocupacao
					ACLMessage response = msg.createReply(); //cria uma nova ACLMessage que seja uma resposta a esta mensagem.
					response.setPerformative(ACLMessage.INFORM); //responde � msg da interface mas agora no tipo INFORM
					response.setContent(x_loc + "," + y_loc + "," + ocupacao + "," + "info"); //envia as suas coordenadas
					myAgent.send(response);
					
				}
				
				
			} else {
				block();
				// doDelete();
			}

		}

	}

	

	
	
	
// --------------------------------------------------------------------MORTE------------------------------------------------------------------------
	protected void takeDown() {
		System.out.println("Ending Estacao");
		try {
			DFService.deregister(this);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		super.takeDown();

	}
}



