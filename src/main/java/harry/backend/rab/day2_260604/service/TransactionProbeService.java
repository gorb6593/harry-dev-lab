package harry.backend.rab.day2_260604.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionProbeService {

	@Transactional(readOnly = true)
	public String currentTransactionMode() {
		return "readOnly";
	}
}
