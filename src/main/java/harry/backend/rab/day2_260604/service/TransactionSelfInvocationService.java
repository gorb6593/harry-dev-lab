package harry.backend.rab.day2_260604.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
public class TransactionSelfInvocationService {

	public TransactionProbeResponse compareExternalAndInternalCall() {
		TransactionState selfInvocationState = transactionalReadOnlyState();

		return new TransactionProbeResponse(
			false,
			false,
			selfInvocationState.active(),
			selfInvocationState.readOnly()
		);
	}

	@Transactional(readOnly = true)
	public TransactionState transactionalReadOnlyState() {
		return new TransactionState(
			TransactionSynchronizationManager.isActualTransactionActive(),
			TransactionSynchronizationManager.isCurrentTransactionReadOnly()
		);
	}

	public record TransactionState(
		boolean active,
		boolean readOnly
	) {
	}
}
