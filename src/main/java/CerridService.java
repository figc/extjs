import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CerridService {

	private String aendpoint;
	private String bendpoint;
	private String cendpoint;
	
	private EndpointWorker worker;
	
	@Autowired
	public CerridService(String aendpoint, String bendpoint, String cendpoint) {
		this.aendpoint = aendpoint;
		this.bendpoint = bendpoint;
		this.cendpoint = cendpoint;
	}
	
	@PostConstruct
	public void checkEnpoints() {
		if (aendpoint == null
				|| bendpoint == null
				|| cendpoint == null) {
			// start worker
			worker = new CerridEndpointWorker(this);
			worker.start();
		}
	}
}

/**
 * 
 */
interface EndpointWorker {
	void start();
	void stop();
	void isRunning();
}

/**
 * 
 */
class CerridEndpointWorker implements EndpointWorker {
	
	private CerridService delegate;
	
	public CerridEndpointWorker(CerridService delegate) {
		this.delegate = delegate;
	}

	@Override
	public void start() {
		
	}

	@Override
	public void stop() {
		
	}

	@Override
	public void isRunning() {
		
	}
	
}