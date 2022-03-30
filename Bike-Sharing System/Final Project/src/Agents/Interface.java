package Agents;

import java.util.ArrayList;
//import java.util.ArrayList;

import java.util.Random;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class Interface extends Agent{
	

	
	private ArrayList<Integer> Estacao0=  new ArrayList(3); // x lox, y loc e ocup
	private ArrayList<Integer> Estacao1=  new ArrayList(3);
	private ArrayList<Integer> Estacao2=  new ArrayList(3);
	private ArrayList<Integer> Estacao3=  new ArrayList(3);
	private ArrayList<Integer> Estacao4=  new ArrayList(3);
	private ArrayList<Integer> Estacao5=  new ArrayList(3);
	private ArrayList<Integer> Estacao6=  new ArrayList(3);
	private ArrayList<Integer> Estacao7=  new ArrayList(3);
	private ArrayList<Integer> Estacao8=  new ArrayList(3);
	
	protected void setup() {
		
		super.setup();
		
	
	//Behaviours
	addBehaviour(new ContactaEstacao());
	addBehaviour(new Recebe());
	}

	
//----------------------------------------------------------------CONTACTA ESTACAO PARA PEDIR INFORMA��O----------------------------------------------------
	
	public class ContactaEstacao extends OneShotBehaviour {
		
		 public void action() {
			 try {
				//contacta todas as esta��es 
					DFAgentDescription dfd = new DFAgentDescription();
					ServiceDescription sd = new ServiceDescription();
					sd.setType("estacao");
					dfd.addServices(sd);
					
					DFAgentDescription[] result = DFService.search(this.myAgent, dfd);
					String[] estacao; //array de AID das estacoes
					estacao = new String[result.length]; //array de AID das estacoes com tamanho do array result

			for (int i=0; i < result.length; ++i) { //para cada elemento da lista result, envia uma mensagem
				estacao[i] = result[i].getName().getLocalName();
				
				//envia msg a cada estacao para ela enviar a sua ocupacao
				ACLMessage mensagem = new ACLMessage(ACLMessage.CFP);
				AID receiver = new AID();
				receiver.setLocalName(estacao[i]);
				mensagem.addReceiver(receiver);
				mensagem.setContent("Pedido de informa��o");
				myAgent.send(mensagem); 
			   }//fim for
			
				} catch (FIPAException e) {
				e.printStackTrace();
				}
					
					
					
				} //fim action
		 
		
		
		 } //fim oneshot
			 
	
			
//---------------------------------------------------------------Recebe info continuamente e imprime-a--------------------------------------------------------------------
public class Recebe extends CyclicBehaviour {
	
	
	private int ocupacao0;
	private int ocupacao1;
	private int ocupacao2;
	private int ocupacao3;
	private int ocupacao4;
	private int ocupacao5;
	private int ocupacao6;
	private int ocupacao7;
	private int ocupacao8;
	
	private int count=0;
	public void action() {
		 ACLMessage msg = receive();
		 if (msg != null) {
			 System.out.println("Interface | " + " recebeu" + msg.getContent());
			 if (msg.getPerformative() == ACLMessage.INFORM) {
				 
					if (msg.getContent().contains("info")) { // recebe informa��o incial das esta��es (coordenadas + ocupacao)
						count++;
						String[] conteudo = msg.getContent().split(","); 
						
						//String ID_est = msg.getSender().getLocalName();
						String agentname = msg.getSender().getLocalName();
						// Get last char from Agent Name: "1", "2", "3" and save in String agentname
						String n = agentname.substring(7);
						
					
						if (agentname.contains("0")) { //caso seja o seller 1
							//adiciona no �ndice 0
							Estacao0.add(0, Integer.parseInt(conteudo[0]));
							Estacao0.add(1, Integer.parseInt(conteudo[1]));
							Estacao0.add(2, Integer.parseInt(conteudo[2]));
						}
							
					   else if (agentname.contains("1")) { //caso seja o seller 1
						   Estacao1.add(0, Integer.parseInt(conteudo[0]));
							Estacao1.add(1, Integer.parseInt(conteudo[1]));
							Estacao1.add(2, Integer.parseInt(conteudo[2]));
						}
						else if (agentname.contains("2")) { //caso seja o seller 1
							Estacao2.add(0, Integer.parseInt(conteudo[0]));
							Estacao2.add(1, Integer.parseInt(conteudo[1]));
							Estacao2.add(2, Integer.parseInt(conteudo[2]));
						}
						else if (agentname.contains("3")) { //caso seja o seller 1
							Estacao3.add(0, Integer.parseInt(conteudo[0]));
							Estacao3.add(1, Integer.parseInt(conteudo[1]));
							Estacao3.add(2, Integer.parseInt(conteudo[2]));
						}
						else if (agentname.contains("4")) { //caso seja o seller 1
							Estacao4.add(0, Integer.parseInt(conteudo[0]));
							Estacao4.add(1, Integer.parseInt(conteudo[1]));
							Estacao4.add(2, Integer.parseInt(conteudo[2]));
						}
						else if (agentname.contains("5")) { //caso seja o seller 1
							Estacao5.add(0, Integer.parseInt(conteudo[0]));
							Estacao5.add(1, Integer.parseInt(conteudo[1]));
							Estacao5.add(2, Integer.parseInt(conteudo[2]));
						}
						else if (agentname.contains("6")) { //caso seja o seller 1
							Estacao6.add(0, Integer.parseInt(conteudo[0]));
							Estacao6.add(1, Integer.parseInt(conteudo[1]));
							Estacao6.add(2, Integer.parseInt(conteudo[2]));
						}
						else if (agentname.contains("7")) { //caso seja o seller 1
							Estacao7.add(0, Integer.parseInt(conteudo[0]));
							Estacao7.add(1, Integer.parseInt(conteudo[1]));
							Estacao7.add(2, Integer.parseInt(conteudo[2]));
						}
						else if (agentname.contains("8")) { //caso seja o seller 1
							Estacao8.add(0, Integer.parseInt(conteudo[0]));
							Estacao8.add(1, Integer.parseInt(conteudo[1]));
							Estacao8.add(2, Integer.parseInt(conteudo[2]));
						} 
					
					 
					   
					  
					   if (Estacao0.size() == 3  && Estacao1.size() == 3  && Estacao2.size() == 3 && Estacao3.size() == 3  && Estacao4.size() == 3  && Estacao5.size() == 3 && Estacao6.size() == 3 && Estacao7.size() == 3 && Estacao8.size() == 3) {
						  
						  ocupacao0 = Estacao0.get(2);
						  ocupacao1 = Estacao1.get(2);
					      ocupacao2 = Estacao2.get(2);
						  ocupacao3 = Estacao3.get(2);
						  ocupacao4 = Estacao4.get(2);
						  ocupacao5 = Estacao5.get(2);
						  ocupacao6= Estacao6.get(2);
						  ocupacao7 =	Estacao7.get(2);
						  ocupacao8 =	Estacao8.get(2);
						   
						   
						   System.out.print(
									"********************************************************************\n");
							System.out.print(
									"                  INFO INICIAL DAS ESTA��ES                                                  \n");
							System.out.print(
									"********************************************************************\n");
							System.out.print(
									"| ESTA��O | x_loc | y_loc | ocupacao \n");
							System.out.println("| Estacao0|   " + Estacao0.get(0) + "  |   " + Estacao0.get(1) + "  |" + ocupacao0);
							System.out.println("| Estacao1|   " + Estacao1.get(0) + "  |   " + Estacao1.get(1) + "  |" + ocupacao1);
							System.out.println("| Estacao2|   " + Estacao2.get(0) + "  |   " + Estacao2.get(1) + "  |" + ocupacao2);
							System.out.println("| Estacao3|   " + Estacao3.get(0) + "  |   " + Estacao3.get(1) + "  |" + ocupacao3);
							System.out.println("| Estacao4|   " + Estacao4.get(0) + "  |   " + Estacao4.get(1) + "  |" + ocupacao4);
							System.out.println("| Estacao5|   " + Estacao5.get(0) + "  |   " + Estacao5.get(1) + "  |" + ocupacao5);
							System.out.println("| Estacao6|   " + Estacao6.get(0) + "  |   " + Estacao6.get(1) + "  |" + ocupacao6);
							System.out.println("| Estacao7|   " + Estacao7.get(0) + "  |   " + Estacao7.get(1) + "  |" + ocupacao7);
							System.out.println("| Estacao8|   " + Estacao8.get(0) + "  |   " + Estacao8.get(1) + "  |" + ocupacao8);
						    
						
						
						
							System.out.print("\n");
							System.out.print("\n");
							System.out.print("\n");
						   
					   }
					}//fim info
					else if (msg.getContent().equals("Ocupa��o aumentou")) {
						
						
						
						
						//atualiza informa��o apresentada
						String est = msg.getSender().getLocalName();
						if (est.contains("0")) { //caso tenha sido a info da estacao 0
							ocupacao0 = Estacao0.get(2) + 1 ;
							System.out.println("A ocupa��o do Agente Esta��o0 aumentou para: " + ocupacao0 );
						}
						else if (est.contains("1")) { //caso tenha sido a info da estacao 0
						 ocupacao1 = Estacao1.get(2) + 1 ;
						 System.out.println("A ocupa��o do Agente Esta��o1 aumentou para: " + ocupacao1 );
						}
						else if (est.contains("2")) { //caso tenha sido a info da estacao 0
							ocupacao2 = Estacao2.get(2) + 1 ;
							System.out.println("A ocupa��o do Agente Esta��o2 aumentou para: " + ocupacao2 );
						}
						else if (est.contains("3")) { //caso tenha sido a info da estacao 0
							ocupacao3 = Estacao3.get(2) + 1 ;
							System.out.println("A ocupa��o do Agente Esta��o3 aumentou para: " + ocupacao3 );
						}
						else if (est.contains("4")) { //caso tenha sido a info da estacao 0
						 ocupacao4 = Estacao4.get(2) + 1 ;
						 System.out.println("A ocupa��o do Agente Esta��o4 aumentou para: " + ocupacao4);
						}
						
						else if (est.contains("5")) { //caso tenha sido a info da estacao 0
							ocupacao5 = Estacao5.get(2) + 1 ;
							System.out.println("A ocupa��o do Agente Esta��o5 aumentou para: " + ocupacao5 );
						}
						else if (est.contains("6")) { //caso tenha sido a info da estacao 0
							ocupacao6 = Estacao6.get(2) + 1 ;
							System.out.println("A ocupa��o do Agente Esta��o6 aumentou para: " + ocupacao6 );
						}
						else if (est.contains("7")) { //caso tenha sido a info da estacao 0
							ocupacao7 = Estacao7.get(2) + 1 ;
							System.out.println("A ocupa��o do Agente Esta��o7 aumentou para: " + ocupacao7);
						}
						else if (est.contains("8")) { //caso tenha sido a info da estacao 0
						   ocupacao8 = Estacao8.get(2) + 1 ;
						   System.out.println("A ocupa��o do Agente Esta��o8 aumentou para: " + ocupacao8 );
							
						}
						 System.out.print(
									"********************************************************************\n");
							System.out.print(
									"                  INFO ATUALIZADA                                   \n");
							System.out.print(
									"********************************************************************\n");
							System.out.print(
									"| ESTA��O | x_loc | y_loc | ocupacao \n");
						    
							System.out.println("| Estacao0|   " + Estacao0.get(0) + "  |   " + Estacao0.get(1) + "  |" + ocupacao0);
							System.out.println("| Estacao1|   " + Estacao1.get(0) + "  |   " + Estacao1.get(1) + "  |" + ocupacao1);
							System.out.println("| Estacao2|   " + Estacao2.get(0) + "  |   " + Estacao2.get(1) + "  |" + ocupacao2);
							System.out.println("| Estacao3|   " + Estacao3.get(0) + "  |   " + Estacao3.get(1) + "  |" + ocupacao3);
							System.out.println("| Estacao4|   " + Estacao4.get(0) + "  |   " + Estacao4.get(1) + "  |" + ocupacao4);
							System.out.println("| Estacao5|   " + Estacao5.get(0) + "  |   " + Estacao5.get(1) + "  |" + ocupacao5);
							System.out.println("| Estacao6|   " + Estacao6.get(0) + "  |   " + Estacao6.get(1) + "  |" + ocupacao6);
							System.out.println("| Estacao7|   " + Estacao7.get(0) + "  |   " + Estacao7.get(1) + "  |" + ocupacao7);
							System.out.println("| Estacao8|   " + Estacao8.get(0) + "  |   " + Estacao8.get(1) + "  |" + ocupacao8);
							
						
						
							System.out.print("\n");
							System.out.print("\n");
							System.out.print("\n");
						
						
						
					}
                    else if (msg.getContent().equals("Ocupa��o diminuiu")) {
                    	
						//atualiza informa��o apresentada
						String est = msg.getSender().getLocalName();
						if (est.contains("0")) { //caso tenha sido a info da estacao 0
							ocupacao0 = Estacao0.get(2)  - 1 ;
							 System.out.println("A ocupa��o do Agente Esta��o0 diminuiu para: " + ocupacao0 );
							
						}
						else if (est.contains("1")) { //caso tenha sido a info da estacao 0
						 ocupacao1 = Estacao1.get(2) -  1 ;
						 System.out.println("A ocupa��o do Agente Esta��o1 diminuiu para: " + ocupacao1 );
						}
						else if (est.contains("2")) { //caso tenha sido a info da estacao 0
							ocupacao2 = Estacao2.get(2) - 1 ;
							 System.out.println("A ocupa��o do Agente Esta��o2 diminuiu para: " + ocupacao2 );
						}
						else if (est.contains("3")) { //caso tenha sido a info da estacao 0
							ocupacao3 = Estacao3.get(2) - 1 ;
							 System.out.println("A ocupa��o do Agente Esta��o3 diminuiu para: " + ocupacao3);
						}
						else if (est.contains("4")) { //caso tenha sido a info da estacao 0
						 ocupacao4 = Estacao4.get(2) - 1 ;
						 System.out.println("A ocupa��o do Agente Esta��o4 diminuiu para: " + ocupacao4 );
						}
						
						else if (est.contains("5")) { //caso tenha sido a info da estacao 0
							ocupacao5 = Estacao5.get(2) - 1 ;
							 System.out.println("A ocupa��o do Agente Esta��o5 diminuiu para: " + ocupacao5 );
						}
						else if (est.contains("6")) { //caso tenha sido a info da estacao 0
							ocupacao6 = Estacao6.get(2) - 1 ;
							 System.out.println("A ocupa��o do Agente Esta��o6 diminuiu para: " + ocupacao6 );
						}
						else if (est.contains("7")) { //caso tenha sido a info da estacao 0
							ocupacao7 = Estacao7.get(2) - 1 ;
							 System.out.println("A ocupa��o do Agente Esta��o7 diminuiu para: " + ocupacao7 );
						}
						else if (est.contains("8")) { //caso tenha sido a info da estacao 0
						   ocupacao8 = Estacao8.get(2) - 1 ;
						   System.out.println("A ocupa��o do Agente Esta��o8 diminuiu para: " + ocupacao8 );
							
						}
						 System.out.print(
									"********************************************************************\n");
							System.out.print(
									"                  INFO ATUALIZADA                                   \n");
							System.out.print(
									"********************************************************************\n");
							System.out.print(
									"| ESTA��O | x_loc | y_loc | ocupacao \n");
						    
							System.out.println("| Estacao0|   " + Estacao0.get(0) + "  |   " + Estacao0.get(1) + "  |" + ocupacao0);
							System.out.println("| Estacao1|   " + Estacao1.get(0) + "  |   " + Estacao1.get(1) + "  |" + ocupacao1);
							System.out.println("| Estacao2|   " + Estacao2.get(0) + "  |   " + Estacao2.get(1) + "  |" + ocupacao2);
							System.out.println("| Estacao3|   " + Estacao3.get(0) + "  |   " + Estacao3.get(1) + "  |" + ocupacao3);
							System.out.println("| Estacao4|   " + Estacao4.get(0) + "  |   " + Estacao4.get(1) + "  |" + ocupacao4);
							System.out.println("| Estacao5|   " + Estacao5.get(0) + "  |   " + Estacao5.get(1) + "  |" + ocupacao5);
							System.out.println("| Estacao6|   " + Estacao6.get(0) + "  |   " + Estacao6.get(1) + "  |" + ocupacao6);
							System.out.println("| Estacao7|   " + Estacao7.get(0) + "  |   " + Estacao7.get(1) + "  |" + ocupacao7);
							System.out.println("| Estacao8|   " + Estacao8.get(0) + "  |   " + Estacao8.get(1) + "  |" + ocupacao8);
							System.out.print("\n");
							System.out.print("\n");
							System.out.print("\n");
					}
				} 
		 } else {
				block();
			}
		 }
	
			 
		 }
	

} 
	
	
	
	
	
	
	
	
	
	
	
		


