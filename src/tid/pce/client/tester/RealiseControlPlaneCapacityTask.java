package tid.pce.client.tester;


import java.util.Hashtable;
import java.util.LinkedList;
import java.util.TimerTask;
import java.util.logging.Logger;

import tid.pce.pcep.objects.LSP;
import tid.netManager.NetworkLSPManager;
import tid.pce.client.PCCPCEPSession;
import tid.pce.client.emulator.AutomaticTesterStatistics;

import tid.rsvp.objects.subobjects.EROSubobject;
import tid.pce.pcep.constructs.UpdateRequest;
import tid.pce.pcep.messages.PCEPUpdate;
import tid.pce.pcep.objects.GeneralizedBandwidthSSON;

public class RealiseControlPlaneCapacityTask  extends TimerTask {

	private Logger log;
	private GeneralizedBandwidthSSON GB;
	private AutomaticTesterStatistics stats;
	private LinkedList<LSP> lspList;
	private boolean bidirectional; 
	private PCCPCEPSession PCEPsession;

	
	public RealiseControlPlaneCapacityTask(LinkedList<LSP> lspList,AutomaticTesterStatistics stats, boolean bidirectional, GeneralizedBandwidthSSON GB
			, PCCPCEPSession PCEPsession){
		log=Logger.getLogger("PCCClient");
		this.stats=stats;
		this.lspList=lspList;
		this.GB=GB;
		this.bidirectional=bidirectional;
		this.PCEPsession=PCEPsession;
	}
		
	@Override
	public void run() {
		log.info("Deleting LSP, releasing capacity "+lspList.getFirst().getLspId());
		if (stats != null)
			stats.releaseNumberActiveLSP();
		
		//FIXME: hacer bien el borrado
		// Create Message Upd
		PCEPUpdate updMssg = new PCEPUpdate();
		LinkedList <UpdateRequest> urList = new LinkedList <UpdateRequest>();
		UpdateRequest ur = new UpdateRequest();
		ur.setLSP(lspList.getFirst());
		
		urList.add(ur);
		updMssg.setUpdateRequestList(urList);
		System.out.println("Enviamos un mensaje de borrado de LSP");
		if (PCEPsession == null){
			log.info("PCEP Sesion Null salimos!");
			System.exit(-1);
		}if (updMssg == null){
			log.info("El mensaje a enviar es null");
		}
		PCEPsession.crm.sendPCEPMessage(updMssg);

	}//End run
}